/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.model.gui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.swing.event.SwingPropertyChangeSupport;

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
    
    public static final String PROP_WORDQUEUESIZE = "wordQueueSize";
    private int wordQueueSize;
    
    public static final String PROP_LINKQUEUESIZE = "linkQueueSize";
    private int linkQueueSize;
    
    private transient PropertyChangeSupport propertySupport;

    public ParsingModel() {
        parseLinks = true;
        parseUnknownLinks = false;
        parseLinksTypes = new BasicEventList<String>();
        parseLinksTypes.add("html");
        parseWords = true;
        parseUnknownWords = false;
        parseWordsTypes = new BasicEventList<String>();
        parseWordsTypes.add("text");
        wordQueueSize = 500;
        linkQueueSize = 10;
        propertySupport = new SwingPropertyChangeSupport(this, true);
    }

    public ParsingModel(ParsingModel parsingModel) {
        this.copy(parsingModel);
    }

    public void copy(ParsingModel parsingModel) {
        setParseLinks(parsingModel.isParseLinks());
        setParseUnknownLinks(parsingModel.isParseUnknownLinks());
        parseLinksTypes.clear();
        parseLinksTypes.addAll(parsingModel.getParseLinksTypes());
        setParseWords(parsingModel.isParseWords());
        setParseUnknownWords(parsingModel.isParseUnknownWords());
        parseWordsTypes.clear();
        parseWordsTypes.addAll(parsingModel.getParseWordsTypes());
        setWordQueueSize(parsingModel.getWordQueueSize());
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
        boolean oldValue = this.parseLinks;
        this.parseLinks = parseLinks;
        propertySupport.firePropertyChange(PROP_PARSELINKS, oldValue, parseLinks);
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
        boolean oldValue = this.parseUnknownLinks;
        this.parseUnknownLinks = parseUnknownLinks;
        propertySupport.firePropertyChange(PROP_PARSEUNKNOWNLINKS, oldValue, parseUnknownLinks);
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
        boolean oldValue = this.parseWords;
        this.parseWords = parseWords;
        propertySupport.firePropertyChange(PROP_PARSEWORDS, oldValue, parseWords);
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
        boolean oldValue = this.parseUnknownWords;
        this.parseUnknownWords = parseUnknownWords;
        propertySupport.firePropertyChange(PROP_PARSEUNKNOWNWORDS, oldValue, parseUnknownWords);
    }

    /**
     * @return the parseWordsTypes
     */
    public EventList<String> getParseWordsTypes() {
        return parseWordsTypes;
    }
    
    public int getWordQueueSize() {
        return wordQueueSize;
    }
    
    public void setWordQueueSize(int wordQueueSize) {
        int oldValue = this.wordQueueSize;
        this.wordQueueSize = wordQueueSize;
        propertySupport.firePropertyChange(PROP_WORDQUEUESIZE, oldValue, wordQueueSize);
    }
    
    public int getLinkQueueSize() {
        return linkQueueSize;
    }
    
    public void setLinkQueueSize(int linkQueueSize) {
        int oldValue = this.linkQueueSize;
        this.linkQueueSize = linkQueueSize;
        propertySupport.firePropertyChange(PROP_LINKQUEUESIZE, oldValue, linkQueueSize);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(property, listener);
    }
}
