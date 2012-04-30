/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import penny.downloadmanager.model.db.DAOFactory;
import penny.downloadmanager.model.db.DownloadDAO;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class DownloadSaver implements ListEventListener<DownloadData>, PropertyChangeListener {

    private ObservableElementList<DownloadData> downloads;
    private DownloadDAO dao;
    private List<String> saveProps;
    private boolean saveDelete;

    public DownloadSaver(ObservableElementList<DownloadData> downloads) {
        saveProps = new ArrayList<String>();
        saveProps.add(DownloadData.PROP_CONTENTTYPE);
        saveProps.add(DownloadData.PROP_DOWNLOADTIME);
        saveProps.add(DownloadData.PROP_FILE);
        saveProps.add(DownloadData.PROP_FILEEXTENTION);
        saveProps.add(DownloadData.PROP_HOST);
        saveProps.add(DownloadData.PROP_LOCATIONS);
        saveProps.add(DownloadData.PROP_MD5);
        saveProps.add(DownloadData.PROP_LINKSTATE);
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
    }

    public boolean isSaveDelete() {
        return saveDelete;
    }

    public void setSaveDelete(boolean saveDelete) {
        this.saveDelete = saveDelete;
    }

    public void saveAllDownloads() {

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

                    if (saveDelete) {
                        dao.deleteDownload(d1);
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
                    //SAVE MD5 and linkState here! these objects are equal (to self) when a PropertyChangeEvent is fired so propertyChange is not called

                    changeList.getReadWriteLock().readLock().lock();
                    DownloadData d3 = (DownloadData) changeList.get(sourceIndex);
                    changeList.getReadWriteLock().readLock().unlock();
                    dao.updateDownload(d3, DownloadData.PROP_MD5);
                    dao.updateDownload(d3, DownloadData.PROP_LINKSTATE);
                    dao.updateDownload(d3, DownloadData.PROP_WORDBUFFER);
                    break;
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        DownloadData d = (DownloadData) evt.getSource();

        boolean update = false;

        if (evt.getPropertyName().equals(DownloadData.PROP_STATUS)) {
            dao.updateDownload(d, DownloadData.PROP_DOWNLOADED);
        }

        if (saveProps.contains(evt.getPropertyName())) {
            update = dao.updateDownload(d, evt.getPropertyName());
        } else if (evt.getPropertyName().equals(DownloadData.PROP_SRCLINKS)) {
            update = dao.saveLink(d.getUrl().toString(), (String) evt.getNewValue(), DownloadData.SRC);
        } else if (evt.getPropertyName().equals(DownloadData.PROP_HREFLINKS)) {
            update = dao.saveLink(d.getUrl().toString(), (String) evt.getNewValue(), DownloadData.HREF);
        } else if (evt.getPropertyName().equals(DownloadData.PROP_WORDS)) {
            update = dao.saveWord(d.getUrl().toString(), (String) evt.getNewValue());
        }

        if (!update && saveProps.contains(evt.getPropertyName())) {
            Logger.getLogger(DownloadSaver.class.getName()).log(Level.SEVERE, "Did not save {0} for {1}", new Object[]{evt.getPropertyName(), d.getUrl()});
        }
    }
}
