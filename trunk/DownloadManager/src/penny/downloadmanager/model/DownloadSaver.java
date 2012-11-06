/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import penny.download.DownloadStatus;
import penny.downloadmanager.model.db.DAOFactory;
import penny.downloadmanager.model.db.DownloadDAO;
import penny.downloadmanager.model.db.DownloadData;
import penny.parser.LinkState;
import penny.recmd5.MD5State;

/**
 *
 * @author john
 */
public class DownloadSaver implements ListEventListener<DownloadData>, PropertyChangeListener {

    private ObservableElementList<DownloadData> downloads;
    private DownloadDAO dao;
    private List<String> saveProps;
    private boolean saveDelete;
    private Map<DownloadData, MD5State> md5ValuesMap;
    private Map<DownloadData, LinkState> linkStateValuesMap;
    private Map<DownloadData, String> wordStateValuesMap;
    private Map<DownloadData, Long> lastSaveTime;

    public DownloadSaver(ObservableElementList<DownloadData> downloads) {
        saveProps = new ArrayList<String>();
        saveProps.add(DownloadData.PROP_CONTENTTYPE);
        saveProps.add(DownloadData.PROP_DOWNLOADTIME);
        saveProps.add(DownloadData.PROP_DOWNLOADED);
        saveProps.add(DownloadData.PROP_FILE);
        saveProps.add(DownloadData.PROP_FILEEXTENTION);
        saveProps.add(DownloadData.PROP_HOST);
        saveProps.add(DownloadData.PROP_LOCATIONS);
        //saveProps.add(DownloadData.PROP_MD5);
        //saveProps.add(DownloadData.PROP_LINKSTATE);
        saveProps.add(DownloadData.PROP_WORDBUFFER);
        saveProps.add(DownloadData.PROP_MESSAGE);
        saveProps.add(DownloadData.PROP_PATH);
        saveProps.add(DownloadData.PROP_PROTOCOL);
        saveProps.add(DownloadData.PROP_PROTOCOLFILENAME);
        saveProps.add(DownloadData.PROP_QUERY);
        saveProps.add(DownloadData.PROP_RESPONSECODE);
        saveProps.add(DownloadData.PROP_SAVEPATH);
        saveProps.add(DownloadData.PROP_SIZE);
        saveProps.add(DownloadData.PROP_STATUS);
        saveProps.add(DownloadData.PROP_TEMPPATH);
        saveProps.add(DownloadData.PROP_URL);
        this.downloads = downloads;
        dao = DAOFactory.getInstance().getDownloadDAO();
        saveDelete = true;
        this.downloads.addListEventListener(this);
        for (DownloadData d : downloads) {
            d.addPropertyChangeListener(this);
        }

        md5ValuesMap = new HashMap<DownloadData, MD5State>();
        linkStateValuesMap = new HashMap<DownloadData, LinkState>();
        wordStateValuesMap = new HashMap<DownloadData, String>();
        lastSaveTime = new HashMap<DownloadData, Long>();
    }

    public boolean isSaveDelete() {
        return saveDelete;
    }

    public void setSaveDelete(boolean saveDelete) {
        this.saveDelete = saveDelete;
    }

    public void saveAllDownloads() {
        downloads.getReadWriteLock().readLock().lock();
        for(DownloadData d : downloads) {
            dao.updateDownload(d);
        }
        downloads.getReadWriteLock().readLock().unlock();
    }

    public void updateMD5(DownloadData d) {
        if (Model.generateMD5(d)) {
            MD5State oldValue = md5ValuesMap.get(d);
            MD5State md5 = d.getMD5();
            boolean doUpdate = false;
            if (oldValue == null || !oldValue.equals(md5)) {
                doUpdate = true;
            }
            if (doUpdate) {
                if(oldValue == null) {
                    oldValue = new MD5State();
                }
                oldValue.copy(md5);
                dao.updateDownload(d, DownloadData.PROP_MD5);
            }
        }
    }

    public void updateLinkState(DownloadData d) {
        if (Model.parseLinks(d)) {
            LinkState oldValue = linkStateValuesMap.get(d);
            LinkState state = d.getLinkState();
            boolean doUpdate = false;
            if (oldValue == null || !oldValue.equals(state)) {
                doUpdate = true;
            }
            if (doUpdate) {
                if(oldValue == null) {
                    oldValue = new LinkState();
                }
                oldValue.copy(state);
                dao.updateDownload(d, DownloadData.PROP_LINKSTATE);
            }
        }
    }

    @Override
    public void listChanged(ListEvent listChanges) {

        EventList changeList = listChanges.getSourceList();

        while (listChanges.next()) {
            int sourceIndex = listChanges.getIndex();
            int changeType = listChanges.getType();

            switch (changeType) {
                case ListEvent.DELETE:
                    changeList.getReadWriteLock().readLock().lock();
                    DownloadData d1 = (DownloadData) listChanges.getOldValue();
                    changeList.getReadWriteLock().readLock().unlock();

                    d1.removePropertyChangeListener(this);
                    md5ValuesMap.remove(d1);
                    linkStateValuesMap.remove(d1);
                    wordStateValuesMap.remove(d1);

                    if (saveDelete) {
                        dao.deleteDownload(d1.getUrl().toString());
                    }
                    break;
                case ListEvent.INSERT:
                    changeList.getReadWriteLock().readLock().lock();
                    DownloadData d2 = (DownloadData) changeList.get(sourceIndex);
                    changeList.getReadWriteLock().readLock().unlock();

                    d2.addPropertyChangeListener(this);
                    dao.insertDownload(d2);
                    break;
                case ListEvent.UPDATE:

                    changeList.getReadWriteLock().readLock().lock();
                    DownloadData d3 = (DownloadData) changeList.get(sourceIndex);
                    changeList.getReadWriteLock().readLock().unlock();

                    updateMD5(d3);
                    updateLinkState(d3);
                    break;
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        DownloadData d = (DownloadData) evt.getSource();

        if(d.getStatus() == DownloadStatus.DOWNLOADING && (evt.getPropertyName().equals(DownloadData.PROP_DOWNLOADTIME) || evt.getPropertyName().equals(DownloadData.PROP_DOWNLOADED))) {
            long lastTime = lastSaveTime.get(d) == null ? 0 : lastSaveTime.get(d);
            if(d.getDownloadTime() - lastTime > 1000000000) {
                dao.updateDownload(d, DownloadData.PROP_DOWNLOADTIME);
                dao.updateDownload(d, DownloadData.PROP_DOWNLOADED);
                lastSaveTime.put(d, d.getDownloadTime());
            }

        } else if(saveProps.contains(evt.getPropertyName())) {
            dao.updateDownload(d, evt.getPropertyName());
        } else if (evt.getPropertyName().equals(DownloadData.PROP_SRCLINKS)) {
            dao.saveLink(d.getUrl().toString(), (String) evt.getNewValue(), DownloadData.SRC);
        } else if (evt.getPropertyName().equals(DownloadData.PROP_HREFLINKS)) {
            dao.saveLink(d.getUrl().toString(), (String) evt.getNewValue(), DownloadData.HREF);
        } else if (evt.getPropertyName().equals(DownloadData.PROP_WORDS)) {
            dao.saveWord(d.getUrl().toString(), (String) evt.getNewValue());
        }
    }
}
