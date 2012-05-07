/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import penny.recmd5.MD5State;
import penny.download.Download;
import java.net.URL;
import penny.parser.LinkState;

/**
 * Facade for AbstractDownload DB info, and AbstractDownload
 * @author john
 */
public class DownloadData extends Download {

    public static final String PROP_MD5 = "MD5";
    private MD5State newMD5;

    public static final String PROP_LINKSTATE = "linkState";
    private LinkState newLinkState;

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
    
    private void init() {
        newMD5 = new MD5State();
        newLinkState = new LinkState();
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
        setExtraProperty(PROP_SAVEPATH, savePath);
        setExtraProperty(PROP_TEMPPATH, tempPath);
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
        if(MD5 == null) {
            throw new IllegalArgumentException("MD5 cannot be null");
        }
        MD5State oldValue = getMD5();
        newMD5.copy(MD5);
        setExtraProperty(PROP_MD5, newMD5);
        newMD5 = oldValue;
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
        if(linkState == null) {
            throw new IllegalArgumentException("linkState cannot be null");
        }
        LinkState oldValue = getLinkState();
        newLinkState.copy(linkState);
        setExtraProperty(PROP_LINKSTATE, newLinkState);
        newLinkState = oldValue;
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
