/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model.db;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.swing.EventListModel;
import java.net.URL;
import java.util.*;
import penny.download.AbstractDownload;
import penny.parser.LinkState;
import penny.recmd5.MD5State;

/**
 * Facade for Download DB info
 * @author john
 */
public class Download extends AbstractDownload implements Comparable<Download> {
    
    public static final String PROP_ID = "ID";
    private UUID id;

    public static final String PROP_MD5 = "MD5";
    private MD5State md5;

    public static final String PROP_LINKSTATE = "linkState";
    private LinkState linkState;

    public static final String PROP_WORDBUFFER = "wordBuffer";
    private String wordBuffer;

    public static final String PROP_SAVEPATH = "savePath";
    private String savePath;

    public static final String PROP_TEMPPATH = "tempPath";
    private String tempPath;

    public static final String PROP_HREFLINKS = "hrefLinks";
    private EventList<String> hrefLinks;

    public static final String PROP_SRCLINKS = "srcLinks";
    private EventList<String> srcLinks;

    public static final String PROP_WORDS = "words";
    private EventList<String> words;
    
    protected Map<String, Object> extraProps;

    public static final String HREF = "href";
    public static final String SRC = "src";
    public static final String REDIRECT = "redirect";
    
    public static final List<String> propertyNames;    
    
    static {
        List<String> l = new ArrayList<String>();

        l.add(Download.PROP_ATTEMPTS);
        l.add(Download.PROP_HOPS);
        l.add(Download.PROP_CONTENTTYPE);
        l.add(Download.PROP_DOWNLOADED);
        l.add(Download.PROP_DOWNLOADTIME);
        l.add(Download.PROP_FILE);
        l.add(Download.PROP_PROTOCOLFILENAME);
        l.add(Download.PROP_FILEEXSENTION);
        l.add(Download.PROP_HOST);
        l.add(Download.PROP_LOCATIONS);
        l.add(Download.PROP_MESSAGE);
        l.add(Download.PROP_PATH);
        l.add(Download.PROP_PROTOCOL);
        l.add(Download.PROP_QUERY);
        l.add(Download.PROP_RESPONSECODE);
        l.add(Download.PROP_RETRYTIME);
        l.add(Download.PROP_SIZE);
        l.add(Download.PROP_STATUS);
        l.add(Download.PROP_URL);
        l.add(Download.PROP_ID);
        l.add(Download.PROP_MD5);
        l.add(Download.PROP_LINKSTATE);
        l.add(Download.PROP_WORDBUFFER);
        l.add(Download.PROP_SAVEPATH);
        l.add(Download.PROP_TEMPPATH);
        l.add(Download.PROP_CANSTOP);
        l.add(Download.PROP_CANQUEUE);
        l.add(Download.PROP_HREFLINKS);
        l.add(Download.PROP_SRCLINKS);
        l.add(Download.PROP_WORDS);
        l.add(Download.PROP_BYTESPERSECOND);
        l.add(Download.PROP_PROGRESS);
        l.add(Download.PROP_TIMELEFT);
        
        propertyNames = Collections.unmodifiableList(l);
    }
    
    public Download() {
        System.out.println("only use Download() in an IDE visual editor");
    }

    public Download(UUID id) {
        super();
        this.id = id;
        hrefLinks = GlazedLists.threadSafeList(new BasicEventList<String>());
        srcLinks = GlazedLists.threadSafeList(new BasicEventList<String>());
        words = GlazedLists.threadSafeList(new BasicEventList<String>());
        locations = GlazedLists.threadSafeList(new BasicEventList<String>());
        md5 = new MD5State();
        linkState = new LinkState();
        wordBuffer = "";
        savePath = "";
        tempPath = "";
        extraProps = new HashMap<String, Object>();
        
        hrefLinks.addListEventListener(new ListEventListener<String>() {

            @Override
            public void listChanged(ListEvent<String> listChanges) {
                while(listChanges.next()) {
                    propertySupport.fireIndexedPropertyChange(PROP_HREFLINKS, listChanges.getIndex(), listChanges.getOldValue(), listChanges.getNewValue());
                }
            }
        });
        
        srcLinks.addListEventListener(new ListEventListener<String>() {

            @Override
            public void listChanged(ListEvent<String> listChanges) {
                while(listChanges.next()) {
                    propertySupport.fireIndexedPropertyChange(PROP_SRCLINKS, listChanges.getIndex(), listChanges.getOldValue(), listChanges.getNewValue());
                }
            }
        });
        
        words.addListEventListener(new ListEventListener<String>() {

            @Override
            public void listChanged(ListEvent<String> listChanges) {
                while(listChanges.next()) {
                    propertySupport.fireIndexedPropertyChange(PROP_WORDS, listChanges.getIndex(), listChanges.getOldValue(), listChanges.getNewValue());
                }
            }
        });
        
        ((EventList<String>)locations).addListEventListener(new ListEventListener<String>() {

            @Override
            public void listChanged(ListEvent<String> listChanges) {
                while(listChanges.next()) {
                    propertySupport.fireIndexedPropertyChange(PROP_LOCATIONS, listChanges.getIndex(), listChanges.getOldValue(), listChanges.getNewValue());
                }
            }
        });
    }
    
    public UUID getId() {
        return id;
    }

    /**
     * @return the MD5
     */
    public MD5State getMD5() {
        return md5;
    }

    /**
     * @param MD5 the MD5 to set
     */
    public void setMD5(MD5State md5) {
        MD5State oldValue = this.md5;
        this.md5 = md5;
        propertySupport.firePropertyChange(PROP_MD5, oldValue, this.md5);
    }

    /**
     * @return the MD5
     */
    public LinkState getLinkState() {
        return linkState;
    }

    /**
     * @param MD5 the MD5 to setDownload
     */
    public void setLinkState(LinkState linkState) {
        LinkState oldValue = this.linkState;
        this.linkState = linkState;
        propertySupport.firePropertyChange(PROP_LINKSTATE, oldValue, this.linkState);
    }

    /**
     * @return the MD5
     */
    public String getWordBuffer() {
        return wordBuffer;
    }

    /**
     * @param MD5 the MD5 to set
     */
    public void setWordBuffer(String wordBuffer) {
        String oldValue = this.wordBuffer;
        this.wordBuffer = wordBuffer;
        propertySupport.firePropertyChange(PROP_WORDBUFFER, oldValue, this.wordBuffer);
    }

    /**
     * @return the savePath
     */
    public String getSavePath() {
        return savePath;
    }

    /**
     * @param savePath the savePath to set
     */
    public void setSavePath(String savePath) {
        String oldValue = this.savePath;
        this.savePath = savePath;
        propertySupport.firePropertyChange(PROP_SAVEPATH, oldValue, this.savePath);
    }

    /**
     * @return the tempPath
     */
    public String getTempPath() {
        return tempPath;
    }

    /**
     * @param tempPath the tempPath to set
     */
    public void setTempPath(String tempPath) {
        String oldValue = this.tempPath;
        this.tempPath = tempPath;
        propertySupport.firePropertyChange(PROP_TEMPPATH, oldValue, this.tempPath);
    }

    /**
     * @return the hrefLinks
     */
    public List<String> getHrefLinks() {
        return Collections.unmodifiableList(hrefLinks);
    }
    
    public EventListModel<String> getHrefLinksModel() {
        return new EventListModel(hrefLinks);
    }
    
    public void clearHrefLinks() {
        hrefLinks.clear();
    }
    
    public void addHrefLinksListener(ListEventListener<String> listener) {
        hrefLinks.addListEventListener(listener);
    }
    
    public void removeHrefLinksListener(ListEventListener<String> listener) {
        hrefLinks.removeListEventListener(listener);
    }

    public void setHrefLinks(List<String> list) {
        hrefLinks.getReadWriteLock().writeLock().lock();
        hrefLinks.clear();
        hrefLinks.addAll(list);
        hrefLinks.getReadWriteLock().writeLock().unlock();
    }

    /**
     * @param hrefLinks the hrefLinks to set
     */
    public void addHrefLink(String hrefLink) {
        hrefLinks.add(hrefLink);
    }

    public void addHrefLinks(List<String> list) {
        hrefLinks.addAll(list);
    }

    /**
     * @return the srcLinks
     */
    public List<String> getSrcLinks() {
        return Collections.unmodifiableList(srcLinks);
    }
    
    public EventListModel<String> getSrcLinksModel() {
        return new EventListModel<String>(srcLinks);
    }
    
    public void clearSrcLinks() {
        srcLinks.clear();
    }
    
    public void addSrcLinksListener(ListEventListener<String> listener) {
        srcLinks.addListEventListener(listener);
    }
    
    public void removeSrcLinksListener(ListEventListener<String> listener) {
        srcLinks.removeListEventListener(listener);
    }

    public void setSrcLinks(List<String> list) {
        srcLinks.getReadWriteLock().writeLock().lock();
        srcLinks.clear();
        srcLinks.addAll(list);
        srcLinks.getReadWriteLock().writeLock().unlock();
    }

    /**
     * @param srcLinks the srcLinks to set
     */
    public void addSrcLink(String srcLink) {
        srcLinks.add(srcLink);
    }

    public void addSrcLinks(List<String> list) {
        srcLinks.addAll(list);
    }
    
    public List<String> getWords() {
        return Collections.unmodifiableList(words);
    }
    
    public EventListModel<String> getWordsModel() {
        return new EventListModel<String>(words);
    }
    
    public void clearWords() {
        words.clear();
    }
    
    public void addWordsListener(ListEventListener<String> listener) {
        words.addListEventListener(listener);
    }
    
    public void removeWordsListener(ListEventListener<String> listener) {
        words.removeListEventListener(listener);
    }

    public void setWords(List<String> list) {
        words.getReadWriteLock().writeLock().lock();
        words.clear();
        words.addAll(list);
        words.getReadWriteLock().writeLock().unlock();
    }
    
    public void addWord(String word) {
        words.add(word);
    }
    
    public void addWords(List<String> list) {
        words.addAll(list);
    }
    
    @Override
    public List<String> getLocations() {
        return Collections.unmodifiableList(locations);
    }
    
    public EventListModel<String> getLocationsModel() {
        return new EventListModel<String>((EventList<String>)locations);
    }
    
    public void clearLocations() {
        locations.clear();
    }
    
    public void addLocationsListener(ListEventListener<String> listener) {
        ((EventList<String>)locations).addListEventListener(listener);
    }
    
    public void removeLocationsListener(ListEventListener<String> listener) {
        ((EventList<String>)locations).removeListEventListener(listener);
    }

    public void setLocations(List<String> list) {
        ((EventList<String>)locations).getReadWriteLock().writeLock().lock();
        ((EventList<String>)locations).clear();
        ((EventList<String>)locations).addAll(list);
        ((EventList<String>)locations).getReadWriteLock().writeLock().unlock();
    }
    
    public void addLocations(String location) {
        locations.add(location);
    }
    
    public void addLocations(List<String> list) {
        locations.addAll(list);
    }

    public Object getProperty(String key) {

        if (key.equals(Download.PROP_ATTEMPTS)) {
            return Integer.valueOf(this.getAttempts());
        } else if(key.equals(Download.PROP_HOPS)) {
            return Integer.valueOf(this.getHops());
        } else if (key.equals(Download.PROP_CONTENTTYPE)) {
            return this.getContentType();
        } else if (key.equals(Download.PROP_DOWNLOADED)) {
            return Long.valueOf(this.getDownloaded());
        } else if (key.equals(Download.PROP_DOWNLOADTIME)) {
            return Long.valueOf(this.getDownloadTime());
        } else if (key.equals(Download.PROP_FILE)) {
            return this.getFile();
        } else if (key.equals(Download.PROP_PROTOCOLFILENAME)) {
            return this.getProtocolFileName();
        } else if (key.equals(Download.PROP_FILEEXSENTION)) {
            return this.getFileExtension();
        } else if (key.equals(Download.PROP_HOST)) {
            return this.getHost();
        } else if (key.equals(Download.PROP_LOCATIONS)) {
            return this.getLocations();
        } else if (key.equals(Download.PROP_MESSAGE)) {
            return this.getMessage();
        } else if (key.equals(Download.PROP_PATH)) {
            return this.getPath();
        } else if (key.equals(Download.PROP_PROTOCOL)) {
            return this.getProtocol();
        } else if (key.equals(Download.PROP_QUERY)) {
            return this.getQuery();
        } else if (key.equals(Download.PROP_RESPONSECODE)) {
            return Integer.valueOf(this.getResponseCode());
        } else if (key.equals(Download.PROP_RETRYTIME)) {
            return Long.valueOf(this.getRetryTime());
        } else if (key.equals(Download.PROP_SIZE)) {
            return Long.valueOf(this.getSize());
        } else if (key.equals(Download.PROP_STATUS)) {
            return this.getStatus();
        } else if (key.equals(Download.PROP_URL)) {
            return this.getUrl();
        } else if (key.equals(Download.PROP_ID)) {
            return this.getId();
        } else if (key.equals(Download.PROP_LINKSTATE)) {
            return this.getLinkState();
        } else if (key.equals(Download.PROP_MD5)) {
            return this.getMD5();
        } else if (key.equals(Download.PROP_SAVEPATH)) {
            return this.getSavePath();
        } else if (key.equals(Download.PROP_TEMPPATH)) {
            return this.getTempPath();
        } else if(key.equals(Download.PROP_WORDBUFFER)) {
            return this.getWordBuffer();
        } else if(key.equals(Download.PROP_CANSTOP)) {
            return this.isCanStop();
        } else if(key.equals(Download.PROP_CANQUEUE)) {
            return this.isCanQueue();
        } else if(key.equals(Download.PROP_HREFLINKS)) {
            return this.getHrefLinks();
        } else if(key.equals(Download.PROP_SRCLINKS)) {
            return this.getSrcLinks();
        } else if(key.equals(Download.PROP_WORDS)) {
            return this.getWords();
        } else if(key.equals(Download.PROP_BYTESPERSECOND)) {
            return this.getBytesPerSecond();
        } else if(key.equals(Download.PROP_PROGRESS)) {
            return this.getProgress();
        } else if(key.equals(Download.PROP_TIMELEFT)) {
            return this.getTimeLeft();
        }
        
        throw new IllegalArgumentException(key + " is not a property");
    }

    public void setExtraProperty(String key, Object value) {
        if (propertyNames.contains(key)) {
            throw new IllegalArgumentException("use setter for property " + key);
        } else {
            Object oldValue = extraProps.get(key);
            extraProps.put(key, value);
            propertySupport.firePropertyChange(key, oldValue, value);
        }
    }

    public Map<String, Object> getExtraProperties() {
        return Collections.unmodifiableMap(extraProps);
    }

    public void addExtraProperties(Map<String, Object> props) {
        for(String s : props.keySet()) {
            setExtraProperty(s, props.get(s));
        }
    }

    @Override
    public int compareTo(Download o) {
        return id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return id + " " + url;
    }
}
