/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.gui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import java.io.Serializable;

/**
 *
 * @author john
 */
public class ParsingModel implements Serializable {
    public static final String PROP_PARSELINKS = "parseLinks";
    private boolean parseLinks;
    public static final String PROP_PARSEUNKNOWNLINKS = "parseUnknownLinks";
    private boolean parseUnknownLinks;
    public static final String PROP_PARSELINKSTYPES = "parseLinksTypes";
    private EventList<String> parseLinksTypes;

    public static final String PROP_PARSEWORDS = "parseWords";
    private boolean parseWords;
    public static final String PROP_PARSEUNKNOWNWORDS = "parseUnknownWords";
    private boolean parseUnknownWords;
    public static final String PROP_PARSEWORDSTYPES = "parseWordsTypes";
    private EventList<String> parseWordsTypes;

    public ParsingModel() {
        parseLinks = true;
        parseUnknownLinks = true;
        parseLinksTypes = new BasicEventList<String>();
        parseLinksTypes.add("text");
        parseWords = true;
        parseUnknownWords = false;
        parseWordsTypes = new BasicEventList<String>();
        parseWordsTypes.add("text");
    }

    public ParsingModel(ParsingModel parsingModel) {
        this.copy(parsingModel);
    }

    public void copy(ParsingModel parsingModel) {
        parseLinks = parsingModel.isParseLinks();
        parseUnknownLinks = parsingModel.isParseUnknownLinks();
        parseLinksTypes.clear();
        parseLinksTypes.addAll(parsingModel.getParseLinksTypes());
        parseWords = parsingModel.isParseWords();
        parseUnknownWords = parsingModel.isParseUnknownWords();
        parseWordsTypes.clear();
        parseWordsTypes.addAll(parsingModel.getParseWordsTypes());
    }

    /**
     * @return the parseLinks
     */
    public boolean isParseLinks() {
        return parseLinks;
    }

    /**
     * @param parseLinks the parseLinks to set
     */
    public void setParseLinks(boolean parseLinks) {
        this.parseLinks = parseLinks;
    }

    /**
     * @return the parseUnknownLinks
     */
    public boolean isParseUnknownLinks() {
        return parseUnknownLinks;
    }

    /**
     * @param parseUnknownLinks the parseUnknownLinks to set
     */
    public void setParseUnknownLinks(boolean parseUnknownLinks) {
        this.parseUnknownLinks = parseUnknownLinks;
    }

    /**
     * @return the parseLinksTypes
     */
    public EventList<String> getParseLinksTypes() {
        return parseLinksTypes;
    }

    /**
     * @return the parseWords
     */
    public boolean isParseWords() {
        return parseWords;
    }

    /**
     * @param parseWords the parseWords to set
     */
    public void setParseWords(boolean parseWords) {
        this.parseWords = parseWords;
    }

    /**
     * @return the parseUnknownWords
     */
    public boolean isParseUnknownWords() {
        return parseUnknownWords;
    }

    /**
     * @param parseUnknownWords the parseUnknownWords to set
     */
    public void setParseUnknownWords(boolean parseUnknownWords) {
        this.parseUnknownWords = parseUnknownWords;
    }

    /**
     * @return the parseWordsTypes
     */
    public EventList<String> getParseWordsTypes() {
        return parseWordsTypes;
    }
}
