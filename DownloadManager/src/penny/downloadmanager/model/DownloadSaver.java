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
import penny.downloadmanager.model.db.Download;
import penny.downloadmanager.util.PropertyChangeCounter;
import penny.parser.LinkState;
import penny.recmd5.MD5State;

/**
 *
 * @author john
 */
public class DownloadSaver implements ListEventListener<Download>, PropertyChangeListener {

    private ObservableElementList<Download> downloads;
    private DownloadDAO dao;
    private List<String> saveProps;
    private boolean saveDelete;
    private Map<Download, MD5State> md5ValuesMap;
    private Map<Download, LinkState> linkStateValuesMap;
    private Map<Download, String> wordStateValuesMap;
    private Map<Download, Long> lastSaveTime;

    public DownloadSaver(ObservableElementList<Download> downloads) {
        saveProps = new ArrayList<String>();
        saveProps.add(Download.PROP_CONTENTTYPE);
        saveProps.add(Download.PROP_DOWNLOADTIME);
        saveProps.add(Download.PROP_DOWNLOADED);
        saveProps.add(Download.PROP_FILE);
        saveProps.add(Download.PROP_FILEEXTENTION);
        saveProps.add(Download.PROP_HOST);
        saveProps.add(Download.PROP_LOCATIONS);
        //saveProps.add(Download.PROP_MD5);
        //saveProps.add(Download.PROP_LINKSTATE);
        saveProps.add(Download.PROP_WORDBUFFER);
        saveProps.add(Download.PROP_MESSAGE);
        saveProps.add(Download.PROP_PATH);
        saveProps.add(Download.PROP_PROTOCOL);
        saveProps.add(Download.PROP_PROTOCOLFILENAME);
        saveProps.add(Download.PROP_QUERY);
        saveProps.add(Download.PROP_RESPONSECODE);
        saveProps.add(Download.PROP_SAVEPATH);
        saveProps.add(Download.PROP_SIZE);
        saveProps.add(Download.PROP_STATUS);
        saveProps.add(Download.PROP_TEMPPATH);
        saveProps.add(Download.PROP_URL);
        this.downloads = downloads;
        dao = DAOFactory.getInstance().getDownloadDAO();
        saveDelete = true;
        this.downloads.addListEventListener(this);
        PropertyChangeCounter counter = new PropertyChangeCounter("downloads");
        for (Download d : downloads) {
            d.addPropertyChangeListener(this);
            d.addPropertyChangeListener(counter);
        }

        md5ValuesMap = new HashMap<Download, MD5State>();
        linkStateValuesMap = new HashMap<Download, LinkState>();
        wordStateValuesMap = new HashMap<Download, String>();
        lastSaveTime = new HashMap<Download, Long>();
    }

    public boolean isSaveDelete() {
        return saveDelete;
    }

    public void setSaveDelete(boolean saveDelete) {
        this.saveDelete = saveDelete;
    }

    public void saveAllDownloads() {
        downloads.getReadWriteLock().readLock().lock();
        for (Download d : downloads) {
            dao.updateDownload(d);
        }
        downloads.getReadWriteLock().readLock().unlock();
    }

    public void updateMD5(Download d) {
        if (Model.generateMD5(d)) {
            MD5State oldValue = md5ValuesMap.get(d);
            MD5State md5 = d.getMD5();
            boolean doUpdate = false;
            if (oldValue == null || !oldValue.equals(md5)) {
                doUpdate = true;
            }
            if (doUpdate) {
                if (oldValue == null) {
                    oldValue = new MD5State();
                }
                oldValue.copy(md5);
                dao.updateDownload(d, Download.PROP_MD5);
            }
        }
    }

    public void updateLinkState(Download d) {
        if (Model.parseLinks(d)) {
            LinkState oldValue = linkStateValuesMap.get(d);
            LinkState state = d.getLinkState();
            boolean doUpdate = false;
            if (oldValue == null || !oldValue.equals(state)) {
                doUpdate = true;
            }
            if (doUpdate) {
                if (oldValue == null) {
                    oldValue = new LinkState();
                }
                oldValue.copy(state);
                dao.updateDownload(d, Download.PROP_LINKSTATE);
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
                    Download d1 = (Download) listChanges.getOldValue();
                    changeList.getReadWriteLock().readLock().unlock();

                    d1.removePropertyChangeListener(this);
                    md5ValuesMap.remove(d1);
                    linkStateValuesMap.remove(d1);
                    wordStateValuesMap.remove(d1);

                    if (saveDelete) {
                        dao.deleteDownload(d1.getId());
                    }
                    break;
                case ListEvent.INSERT:
                    changeList.getReadWriteLock().readLock().lock();
                    Download d2 = (Download) changeList.get(sourceIndex);
                    changeList.getReadWriteLock().readLock().unlock();

                    d2.addPropertyChangeListener(this);
                    dao.insertDownload(d2);
                    break;
                case ListEvent.UPDATE:

                    changeList.getReadWriteLock().readLock().lock();
                    Download d3 = (Download) changeList.get(sourceIndex);
                    changeList.getReadWriteLock().readLock().unlock();

                    updateMD5(d3);
                    updateLinkState(d3);
                    break;
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Download d = (Download) evt.getSource();

        if (d.getStatus() == DownloadStatus.DOWNLOADING && (evt.getPropertyName().equals(Download.PROP_DOWNLOADTIME) || evt.getPropertyName().equals(Download.PROP_DOWNLOADED))) {
            dao.updateDownload(d, Download.PROP_DOWNLOADTIME);
            dao.updateDownload(d, Download.PROP_DOWNLOADED);
        } else if (saveProps.contains(evt.getPropertyName())) {
            dao.updateDownload(d, evt.getPropertyName());
        } else if (evt.getPropertyName().equals(Download.PROP_SRCLINKS)) {
            dao.saveLink(d.getId(), (String) evt.getNewValue(), Download.SRC);
        } else if (evt.getPropertyName().equals(Download.PROP_HREFLINKS)) {
            dao.saveLink(d.getId(), (String) evt.getNewValue(), Download.HREF);
        } else if (evt.getPropertyName().equals(Download.PROP_WORDS)) {
            dao.saveWord(d.getId(), (String) evt.getNewValue());
        }
    }
}
