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
import penny.downloadmanager.model.db.DAOFactory;
import penny.downloadmanager.model.db.Download;
import penny.downloadmanager.model.db.DownloadDAO;
import penny.downloadmanager.util.PropertyChangeCounter;
import penny.parser.LinkState;
import penny.recmd5.MD5State;

/**
 *
 * @author john
 */
public class DownloadSaver implements ListEventListener<Download>, PropertyChangeListener {

    class HrefLinkSaver implements ListEventListener<String> {

        private Download download;

        public HrefLinkSaver(Download download) {
            this.download = download;
            download.addHrefLinksListener(this);
        }

        @Override
        public void listChanged(ListEvent<String> listChanges) {

            List<String> addLinks = new ArrayList<String>();
            List<String> removeLinks = new ArrayList<String>();
            while (listChanges.next()) {
                int sourceIndex = listChanges.getIndex();
                int changeType = listChanges.getType();

                switch (changeType) {
                    case ListEvent.DELETE:
                        removeLinks.add(listChanges.getOldValue());
                        break;
                    case ListEvent.INSERT:
                        addLinks.add(download.getHrefLinks().get(sourceIndex));
                    case ListEvent.UPDATE:

                        break;
                }
            }
            
            if(addLinks.size() > 0) {
                DAOFactory.getInstance().getLinkDAO().addLinks(download.getId(), addLinks, Download.HREF);
            }
            
            if(removeLinks.size() > 0) {
                DAOFactory.getInstance().getLinkDAO().deleteLinks(download.getId(), removeLinks);
            }
        }
    }

    class SrcLinkSaver implements ListEventListener<String> {

        private Download download;

        public SrcLinkSaver(Download download) {
            this.download = download;
            download.addSrcLinksListener(this);
        }

        @Override
        public void listChanged(ListEvent<String> listChanges) {

            List<String> addLinks = new ArrayList<String>();
            List<String> removeLinks = new ArrayList<String>();

            EventList changeList = listChanges.getSourceList();
            while (listChanges.next()) {
                int sourceIndex = listChanges.getIndex();
                int changeType = listChanges.getType();

                switch (changeType) {
                    case ListEvent.DELETE:
                        removeLinks.add(listChanges.getOldValue());
                        break;
                    case ListEvent.INSERT:
                        addLinks.add(download.getSrcLinks().get(sourceIndex));
                        break;
                    case ListEvent.UPDATE:

                        break;
                }
            }
            
            if(addLinks.size() > 0) {
                DAOFactory.getInstance().getLinkDAO().addLinks(download.getId(), addLinks, Download.SRC);
            }
            
            if(removeLinks.size() > 0) {
                DAOFactory.getInstance().getLinkDAO().deleteLinks(download.getId(), removeLinks);
            }
        }
    }

    class LocationSaver implements ListEventListener<String> {

        private Download download;

        public LocationSaver(Download download) {
            this.download = download;
            download.addLocationsListener(this);
        }

        @Override
        public void listChanged(ListEvent<String> listChanges) {

            List<String> addLocations = new ArrayList<>();
            List<String> removeLocations = new ArrayList<>();

            EventList changeList = listChanges.getSourceList();
            while (listChanges.next()) {
                int sourceIndex = listChanges.getIndex();
                int changeType = listChanges.getType();

                switch (changeType) {
                    case ListEvent.DELETE:
                        removeLocations.add(listChanges.getOldValue());
                        break;
                    case ListEvent.INSERT:
                        addLocations.add(download.getSrcLinks().get(sourceIndex));
                        break;
                    case ListEvent.UPDATE:

                        break;
                }
            }
            
            if(addLocations.size() > 0) {
                DAOFactory.getInstance().getLinkDAO().addLinks(download.getId(), addLocations, Download.REDIRECT);
            }
            
            if(removeLocations.size() > 0) {
                DAOFactory.getInstance().getLinkDAO().deleteLinks(download.getId(), removeLocations);
            }
        }
    }

    class WordSaver implements ListEventListener<String> {

        private Download download;

        public WordSaver(Download download) {
            this.download = download;
            download.addWordsListener(this);
        }

        @Override
        public void listChanged(ListEvent<String> listChanges) {

            List<String> addWords = new ArrayList<String>();
            List<String> removeWords = new ArrayList<String>();
            
            while (listChanges.next()) {
                int sourceIndex = listChanges.getIndex();
                int changeType = listChanges.getType();

                switch (changeType) {
                    case ListEvent.DELETE:
                        removeWords.add(listChanges.getOldValue());
                        break;
                    case ListEvent.INSERT:
                        addWords.add(download.getWords().get(sourceIndex));
                        break;
                    case ListEvent.UPDATE:

                        break;
                }
            }
            if (addWords.size() > 0) {
                DAOFactory.getInstance().getWordDAO().addWords(download.getId(), addWords);
            }
            if (removeWords.size() > 0) {
                DAOFactory.getInstance().getWordDAO().deleteWords(download.getId(), removeWords);
            }
        }
    }
    private ObservableElementList<Download> downloads;
    private DownloadDAO dao;
    private List<String> saveProps;
    private boolean saveDelete;
    private Map<Download, MD5State> md5ValuesMap;
    private Map<Download, LinkState> linkStateValuesMap;
    private Map<Download, WordSaver> wordSavers;
    private Map<Download, HrefLinkSaver> hrefSavers;
    private Map<Download, SrcLinkSaver> srcSavers;
    private Map<Download, LocationSaver> locationSavers;

    public DownloadSaver(ObservableElementList<Download> downloads) {
        saveProps = new ArrayList<String>();
        saveProps.add(Download.PROP_URL);
        saveProps.add(Download.PROP_SIZE);
        saveProps.add(Download.PROP_DOWNLOADED);
        saveProps.add(Download.PROP_STATUS);
        saveProps.add(Download.PROP_DOWNLOADTIME);
        saveProps.add(Download.PROP_ATTEMPTS);
        saveProps.add(Download.PROP_HOPS);
        saveProps.add(Download.PROP_CONTENTTYPE);
        saveProps.add(Download.PROP_HOST);
        saveProps.add(Download.PROP_PROTOCOL);
        saveProps.add(Download.PROP_QUERY);
        saveProps.add(Download.PROP_PATH);
        saveProps.add(Download.PROP_FILE);
        saveProps.add(Download.PROP_PROTOCOLFILENAME);
        saveProps.add(Download.PROP_FILEEXSENTION);
        saveProps.add(Download.PROP_MESSAGE);
        saveProps.add(Download.PROP_RESPONSECODE);
        saveProps.add(Download.PROP_LOCATIONS);
        //saveProps.add(Download.PROP_MD5);
        //saveProps.add(Download.PROP_LINKSTATE);
        saveProps.add(Download.PROP_WORDBUFFER);
        saveProps.add(Download.PROP_SAVEPATH);
        saveProps.add(Download.PROP_TEMPPATH);
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
        
        wordSavers = new HashMap<Download, WordSaver>();
        hrefSavers = new HashMap<Download, HrefLinkSaver>();
        srcSavers = new HashMap<Download, SrcLinkSaver>();
        locationSavers = new HashMap<Download, LocationSaver>();
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

                    if (saveDelete) {
                        dao.deleteDownload(d1.getId());
                    }


                    WordSaver wordSaver = wordSavers.get(d1);
                    d1.removeWordsListener(wordSaver);
                    wordSavers.remove(d1);

                    HrefLinkSaver hrefSaver = hrefSavers.get(d1);
                    d1.removeHrefLinksListener(hrefSaver);
                    hrefSavers.remove(d1);

                    SrcLinkSaver srcSaver = srcSavers.get(d1);
                    d1.removeSrcLinksListener(srcSaver);
                    srcSavers.remove(d1);

                    LocationSaver locationSaver = locationSavers.get(d1);
                    d1.removeSrcLinksListener(locationSaver);
                    locationSavers.remove(d1);
                    break;
                case ListEvent.INSERT:
                    changeList.getReadWriteLock().readLock().lock();
                    Download d2 = (Download) changeList.get(sourceIndex);
                    changeList.getReadWriteLock().readLock().unlock();

                    d2.addPropertyChangeListener(this);
                    dao.insertDownload(d2);

                    wordSavers.put(d2, new WordSaver(d2));
                    hrefSavers.put(d2, new HrefLinkSaver(d2));
                    srcSavers.put(d2, new SrcLinkSaver(d2));
                    locationSavers.put(d2, new LocationSaver(d2));
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

        if (saveProps.contains(evt.getPropertyName())) {
            dao.updateDownload(d, evt.getPropertyName());
        } 
//        else if (evt.getPropertyName().equals(Download.PROP_SRCLINKS)) {
//            DAOFactory.getInstance().getLinkDAO().addLink(d.getId(), (String) evt.getNewValue(), Download.SRC);
//        } else if (evt.getPropertyName().equals(Download.PROP_HREFLINKS)) {
//            DAOFactory.getInstance().getLinkDAO().addLink(d.getId(), (String) evt.getNewValue(), Download.HREF);
//        } else if (evt.getPropertyName().equals(Download.PROP_WORDS)) {
//            DAOFactory.getInstance().getWordDAO().addWord(d.getId(), (String) evt.getNewValue());
//        }
    }
}
