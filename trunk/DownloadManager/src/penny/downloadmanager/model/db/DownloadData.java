/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model.db;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import java.util.Objects;
import penny.recmd5.MD5State;
import penny.download.Download;
import java.net.URL;
import java.util.UUID;
import penny.parser.LinkState;

/**
 * Facade for AbstractDownload DB info, and AbstractDownload
 * @author john
 */
public class DownloadData extends Download {
    
    public static final String PROP_ID = "ID";
    
    private UUID id;

    public static final String PROP_MD5 = "MD5";

    public static final String PROP_LINKSTATE = "linkState";

    public static final String PROP_WORDBUFFER = "wordBuffer";

    public static final String PROP_SAVEPATH = "savePath";

    public static final String PROP_TEMPPATH = "tempPath";

    public static final String PROP_HREFLINKS = "hrefLinks";
    private EventList<String> hrefLinks;

    public static final String PROP_SRCLINKS = "srcLinks";
    private EventList<String> srcLinks;

    public static final String PROP_WORDS = "words";
    private EventList<String> words;

    public static final String HREF = "href";
    public static final String SRC = "src";
    
    public void init(URL url) {
        super.init(url);
        hrefLinks = new BasicEventList<String>();
        srcLinks = new BasicEventList<String>();
        words = new BasicEventList<String>();
        locations = new BasicEventList<URL>();

        setExtraProperty(PROP_MD5, new MD5State());
        setExtraProperty(PROP_LINKSTATE, new LinkState());
        setExtraProperty(PROP_WORDBUFFER, "");
        setExtraProperty(PROP_SAVEPATH, "");
        setExtraProperty(PROP_TEMPPATH, "");
    }

    DownloadData(URL url, UUID id) {
        super(url);
        init(url);
        this.id = id;
    }
    
    public UUID getId() {
        return id;
    }

    /**
     * @return the MD5
     */
    public MD5State getMD5() {
        return (MD5State) getProperty(PROP_MD5);
    }

    /**
     * @param MD5 the MD5 to set
     */
    public void setMD5(MD5State MD5) {
        setExtraProperty(PROP_MD5, MD5);
    }

    /**
     * @return the MD5
     */
    public LinkState getLinkState() {
        return (LinkState) getProperty(PROP_LINKSTATE);
    }

    /**
     * @param MD5 the MD5 to set
     */
    public void setLinkState(LinkState linkState) {
        setExtraProperty(PROP_LINKSTATE, linkState);
    }

    /**
     * @return the MD5
     */
    public String getWordBuffer() {
        return (String) getProperty(PROP_WORDBUFFER);
    }

    /**
     * @param MD5 the MD5 to set
     */
    public void setWordBuffer(String wordBuffer) {
        setExtraProperty(PROP_WORDBUFFER, wordBuffer);
    }

    /**
     * @return the savePath
     */
    public String getSavePath() {
        return (String) getProperty(PROP_SAVEPATH);
    }

    /**
     * @param savePath the savePath to set
     */
    public void setSavePath(String savePath) {
        setExtraProperty(PROP_SAVEPATH, savePath);
    }

    /**
     * @return the tempPath
     */
    public String getTempPath() {
        return (String) getProperty(PROP_TEMPPATH);
    }

    /**
     * @param tempPath the tempPath to set
     */
    public void setTempPath(String tempPath) {
        setExtraProperty(PROP_TEMPPATH, tempPath);
    }

    /**
     * @return the hrefLinks
     */
    public EventList<String> getHrefLinks() {
        return hrefLinks;
    }

    public void setHrefLinks(EventList<String> list) {
        hrefLinks.clear();
        for(String s : list) {
            addHrefLink(s);
        }
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
        srcLinks.clear();
        for(String s : list) {
            addSrcLink(s);
        }
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
        words.clear();
        for(String s : list) {
            addWord(s);
        }
    }

    /**
     * @param srcLinks the srcLinks to set
     */
    public void addWord(String word) {
        int i = words.size();
        words.add(word);
        propertySupport.fireIndexedPropertyChange(PROP_WORDS, i, null, word);
    }
}
