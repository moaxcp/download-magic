/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import penny.recmd5.MD5State;
import penny.download.Download;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.SwingPropertyChangeSupport;
import penny.parser.LinkState;

/**
 * Facade for AbstractDownload DB info, and AbstractDownload
 * @author john
 */
public class DownloadData extends Download {
    public static final String PROP_EXTRAPROPS = "extraProps";
    private Map<String, Object> extraProps;

    public static final String PROP_MD5 = "MD5";
    private MD5State MD5;

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

    public static final String HREF = "href";
    public static final String SRC = "src";

    private List<String> propertyNames;
    
    private void init() {
        extraProps = new HashMap<String, Object>();
        MD5 = new MD5State();
        linkState = new LinkState();
        wordBuffer = "";
        savePath = "";
        tempPath = "";
        hrefLinks = new BasicEventList<String>();
        srcLinks = new BasicEventList<String>();
        words = new BasicEventList<String>();
        locations = new BasicEventList<URL>();
        propertyNames = new ArrayList<String>();

        locations = new BasicEventList<URL>();
    }

    public DownloadData() {
        super();
        init();
    }

    public DownloadData(URL url) {
        super(url);
        init();
    }

    public DownloadData(URL url, String tempPath, String savePath) {
        super(url);
        init();
        this.savePath = savePath;
        this.tempPath = tempPath;
    }

    public static String insertProperties(DownloadData d, String statement) {
        String str = statement;
        Pattern pat = Pattern.compile("\\$\\{\\w+\\}");
        Matcher mat = pat.matcher(str);
        while (mat.find()) {
            String prop = mat.group();
            prop = prop.replace("${", "");
            prop = prop.replace("}", "");
            str = str.replace("${" + prop + "}", d.getProperty(prop).toString());
        }
        return str;
    }

    public static String getFileName(DownloadData d, String saveNameFormat, String defaultFileName) {
        String str = saveNameFormat;
        Pattern pat = Pattern.compile("\\$\\{\\w+\\}");
        Matcher mat = pat.matcher(str);
        while (mat.find()) {
            String prop = mat.group();
            prop = prop.replace("${", "");
            prop = prop.replace("}", "");
            if (prop.equals(DownloadData.PROP_FILE) && d.getProperty(prop).equals("")) {
                str = str.replace("${" + prop + "}", defaultFileName);
            } else {
                str = str.replace("${" + prop + "}", d.getProperty(prop).toString());
            }
        }
        return str;
    }

    public List<String> getPropertyNames() {
        for(String k : extraProps.keySet()) {
            if(!propertyNames.contains(k)) {
                propertyNames.add(k);
            }
        }
        
        if(!propertyNames.contains(Download.PROP_ATTEMPTS)) {
            propertyNames.add(Download.PROP_ATTEMPTS);
        }

        if(!propertyNames.contains(Download.PROP_CONTENTTYPE)) {
            propertyNames.add(Download.PROP_CONTENTTYPE);
        }

        if(!propertyNames.contains(Download.PROP_DOWNLOADED)) {
            propertyNames.add(Download.PROP_DOWNLOADED);
        }

        if(!propertyNames.contains(Download.PROP_DOWNLOADTIME)) {
            propertyNames.add(Download.PROP_DOWNLOADTIME);
        }

        if(!propertyNames.contains(Download.PROP_FILE)) {
            propertyNames.add(Download.PROP_FILE);
        }

        if(!propertyNames.contains(Download.PROP_PROTOCOLFILENAME)) {
            propertyNames.add(Download.PROP_PROTOCOLFILENAME);
        }

        if(!propertyNames.contains(Download.PROP_FILEEXTENTION)) {
            propertyNames.add(Download.PROP_FILEEXTENTION);
        }

        if(!propertyNames.contains(Download.PROP_HOST)) {
            propertyNames.add(Download.PROP_HOST);
        }

        if(!propertyNames.contains(Download.PROP_LOCATIONS)) {
            propertyNames.add(Download.PROP_LOCATIONS);
        }

        if(!propertyNames.contains(Download.PROP_MESSAGE)) {
            propertyNames.add(Download.PROP_MESSAGE);
        }

        if(!propertyNames.contains(Download.PROP_PATH)) {
            propertyNames.add(Download.PROP_PATH);
        }

        if(!propertyNames.contains(Download.PROP_PROTOCOL)) {
            propertyNames.add(Download.PROP_PROTOCOL);
        }

        if(!propertyNames.contains(Download.PROP_QUERY)) {
            propertyNames.add(Download.PROP_QUERY);
        }

        if(!propertyNames.contains(Download.PROP_RESPONSECODE)) {
            propertyNames.add(Download.PROP_RESPONSECODE);
        }

        if(!propertyNames.contains(Download.PROP_RETRYTIME)) {
            propertyNames.add(Download.PROP_RETRYTIME);
        }
        if(!propertyNames.contains(Download.PROP_SIZE)) {
            propertyNames.add(Download.PROP_SIZE);
        }
        if(!propertyNames.contains(Download.PROP_STATUS)) {
            propertyNames.add(Download.PROP_STATUS);
        }
        if(!propertyNames.contains(Download.PROP_URL)) {
            propertyNames.add(Download.PROP_URL);
        }
        if(!propertyNames.contains(DownloadData.PROP_HREFLINKS)) {
            propertyNames.add(DownloadData.PROP_HREFLINKS);
        }
        if(!propertyNames.contains(DownloadData.PROP_MD5)) {
            propertyNames.add(DownloadData.PROP_MD5);
        }
        if(!propertyNames.contains(DownloadData.PROP_LINKSTATE)) {
            propertyNames.add(DownloadData.PROP_LINKSTATE);
        }
        if(!propertyNames.contains(DownloadData.PROP_WORDBUFFER)) {
            propertyNames.add(DownloadData.PROP_WORDBUFFER);
        }
        if(!propertyNames.contains(DownloadData.PROP_SAVEPATH)) {
            propertyNames.add(DownloadData.PROP_SAVEPATH);
        }
        if(!propertyNames.contains(DownloadData.PROP_SRCLINKS)) {
            propertyNames.add(DownloadData.PROP_SRCLINKS);
        }
        if(!propertyNames.contains(DownloadData.PROP_WORDS)) {
            propertyNames.add(DownloadData.PROP_WORDS);
        }
        if(!propertyNames.contains(DownloadData.PROP_TEMPPATH)) {
            propertyNames.add(DownloadData.PROP_TEMPPATH);
        }

        return propertyNames;
    }

    public Object getProperty(String name) {
        for(String s : extraProps.keySet()) {
            if(s.equals(name)) {
                return extraProps.get(s);
            }
        }

        if(name.equals(Download.PROP_ATTEMPTS)) {
            return Integer.valueOf(this.getAttempts());
        } else if(name.equals(Download.PROP_CONTENTTYPE)) {
            return this.getContentType();
        } else if(name.equals(Download.PROP_DOWNLOADED)) {
            return Long.valueOf(this.getDownloaded());
        } else if(name.equals(Download.PROP_DOWNLOADTIME)) {
            return Long.valueOf(this.getDownloadTime());
        } else if(name.equals(Download.PROP_FILE)) {
            return this.getFile();
        } else if(name.equals(Download.PROP_PROTOCOLFILENAME)) {
            return this.getProtocolFileName();
        } else if(name.equals(Download.PROP_FILEEXTENTION)) {
            return this.getFileExtention();
        } else if(name.equals(Download.PROP_HOST)) {
            return this.getHost();
        } else if(name.equals(Download.PROP_LOCATIONS)) {
            return this.getLocations();
        } else if(name.equals(Download.PROP_MESSAGE)) {
            return this.getMessage();
        } else if(name.equals(Download.PROP_PATH)) {
            return this.getPath();
        } else if(name.equals(Download.PROP_PROTOCOL)) {
            return this.getProtocol();
        } else if(name.equals(Download.PROP_QUERY)) {
            return this.getQuery();
        } else if(name.equals(Download.PROP_RESPONSECODE)) {
            return Integer.valueOf(this.getResponseCode());
        } else if(name.equals(Download.PROP_RETRYTIME)) {
            return Long.valueOf(this.getRetryTime());
        } else if(name.equals(Download.PROP_SIZE)) {
            return Long.valueOf(this.getSize());
        } else if(name.equals(Download.PROP_STATUS)) {
            return this.getStatus();
        } else if(name.equals(Download.PROP_URL)) {
            return this.getUrl();
        } else if(name.equals(DownloadData.PROP_HREFLINKS)) {
            return this.getHrefLinks();
        } else if(name.equals(DownloadData.PROP_MD5)) {
            return this.getMD5();
        } else if(name.equals(DownloadData.PROP_LINKSTATE)) {
            return this.getLinkState();
        } else if(name.equals(DownloadData.PROP_WORDBUFFER)) {
            return this.getWordBuffer();
        } else if(name.equals(DownloadData.PROP_SAVEPATH)) {
            return this.getSavePath();
        } else if(name.equals(DownloadData.PROP_TEMPPATH)) {
            return this.getTempPath();
        } else if(name.equals(DownloadData.PROP_SRCLINKS)) {
            return this.getSrcLinks();
        } else if(name.equals(DownloadData.PROP_WORDS)) {
            return this.getWords();
        }
        return "";
    }

    public void setExtraProps(String key, Object val) {
        Object oldValue = extraProps.get(key);
        extraProps.put(key, val);
        propertySupport.firePropertyChange(key, oldValue, val);
        propertySupport.firePropertyChange(PROP_EXTRAPROPS, extraProps, extraProps);
    }

    public void setExtraProps(Map<String, Object> props) {
        Map<String, Object> oldValue = extraProps;
        extraProps = props;
        propertySupport.firePropertyChange(PROP_EXTRAPROPS, oldValue, props);
    }

    public Map<String, Object> getExtraProps() {
        return Collections.unmodifiableMap(extraProps);
    }

    public Object getExtraProps(String key) {
        return extraProps.get(key);
    }

    /**
     * @return the MD5
     */
    public MD5State getMD5() {
        return MD5;
    }

    /**
     * @param MD5 the MD5 to set
     */
    public void setMD5(MD5State MD5) {
        MD5State oldValue = this.MD5;
        this.MD5 = MD5;
        propertySupport.firePropertyChange(PROP_MD5, oldValue, MD5);
    }

    /**
     * @return the MD5
     */
    public LinkState getLinkState() {
        return linkState;
    }

    /**
     * @param MD5 the MD5 to set
     */
    public void setLinkState(LinkState linkState) {
        LinkState oldValue = this.linkState;
        this.linkState = linkState;
        propertySupport.firePropertyChange(PROP_LINKSTATE, oldValue, linkState);
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
        propertySupport.firePropertyChange(PROP_WORDBUFFER, oldValue, wordBuffer);
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
        propertySupport.firePropertyChange(PROP_SAVEPATH, oldValue, savePath);
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
        propertySupport.firePropertyChange(PROP_TEMPPATH, oldValue, tempPath);
    }

    /**
     * @return the hrefLinks
     */
    public EventList<String> getHrefLinks() {
        return hrefLinks;
    }

    public void setHrefLinks(EventList<String> list) {
        this.hrefLinks = list;
    }

    /**
     * @param hrefLinks the hrefLinks to set
     */
    public void addHrefLink(String hrefLink) {
        int i = hrefLinks.size();
        hrefLinks.add(hrefLink);
        propertySupport.fireIndexedPropertyChange(PROP_HREFLINKS, i, null, hrefLink);
    }

    /**
     * @return the srcLinks
     */
    public EventList<String> getSrcLinks() {
        return srcLinks;
    }

    public void setSrcLinks(EventList<String> list) {
        this.srcLinks = list;
    }

    /**
     * @param srcLinks the srcLinks to set
     */
    public void addSrcLink(String srcLink) {
        int i = srcLinks.size();
        srcLinks.add(srcLink);
        propertySupport.fireIndexedPropertyChange(PROP_SRCLINKS, i, null, srcLink);
    }

    /**
     * @return the srcLinks
     */
    public EventList<String> getWords() {
        return words;
    }

    public void setWords(EventList<String> list) {
        this.words = list;
    }

    /**
     * @param srcLinks the srcLinks to set
     */
    public void addWord(String word) {
        int i = words.size();
        words.add(word);
        propertySupport.fireIndexedPropertyChange(PROP_WORDS, i, null, word);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListeners() {
        PropertyChangeListener[] listeners = propertySupport.getPropertyChangeListeners();
        for(PropertyChangeListener l : listeners) {
            removePropertyChangeListener(l);
        }
    }
}
