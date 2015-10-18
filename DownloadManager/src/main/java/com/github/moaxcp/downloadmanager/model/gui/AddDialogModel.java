/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.model.gui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventListModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import javax.swing.event.SwingPropertyChangeSupport;
import com.github.moaxcp.downloadmanager.control.AddDialogControl;

/**
 *
 * @author john
 */
public class AddDialogModel {


    public static enum PanelList {
        URL("URL"),
        LIST("List"),
        QUERY("Query");

        private String name;

        PanelList(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static final String PROP_SELECTEDPANEL = "selectedPanel";
    public static final String PROP_ADDURL = "addURL";
    public static final String PROP_LISTFILENAME = "listFileName";
    public static final String PROP_LISTTYPE = "listType";
    public static final String PROP_SELECTEDCONNECTION = "selectedConnection";
    public static final String PROP_URLS = "urls";
    public static final String PROP_VISIBLE = "visible";

    private PanelList selectedPanel;
    private String addURL;
    private String listFileName;
    private String listType;
    private EventList<URL> urls;

    private boolean visible;

    private SwingPropertyChangeSupport propertySupport;


    public AddDialogModel() {
        selectedPanel = PanelList.URL;
        addURL = "";
        listFileName = "";
        listType = "";
        urls = new BasicEventList<URL>();
        visible = false;
        propertySupport = new SwingPropertyChangeSupport(this, true);
    }

    /**
     * @return the addURL
     */
    public String getAddURL() {
        return addURL;
    }

    /**
     * @param addURL the addURL to set
     */
    public void setAddURL(String addURL) {
        String oldValue = this.addURL;
        this.addURL = addURL;
        propertySupport.firePropertyChange(PROP_ADDURL, oldValue, addURL);
    }

    /**
     * @return the selectedPanel
     */
    public PanelList getSelectedPanel() {
        return selectedPanel;
    }

    /**
     * @param selectedPanel the selectedPanel to set
     */
    public void setSelectedPanel(PanelList selectedPanel) {
        PanelList oldValue = this.selectedPanel;
        this.selectedPanel = selectedPanel;
        propertySupport.firePropertyChange(PROP_SELECTEDPANEL, oldValue, selectedPanel);
    }

    /**
     * @return the listFileName
     */
    public String getListFileName() {
        return listFileName;
    }

    /**
     * @param listFileName the listFileName to set
     */
    public void setListFileName(String listFileName) {
        String oldValue = this.listFileName;
        this.listFileName = listFileName;
        propertySupport.firePropertyChange(PROP_LISTFILENAME, oldValue, listFileName);
    }

    /**
     * @return the listType
     */
    public String getListType() {
        return listType;
    }

    /**
     * @param listType the listType to set
     */
    public void setListType(String listType) {
        String oldValue = this.listType;
        this.listType = listType;
        propertySupport.firePropertyChange(PROP_LISTTYPE, oldValue, listType);
    }

    /**
     * @return the urls
     */
    public URL getUrl(int index) {
        return urls.get(index);
    }

    /**
     * @param urls the urls to set
     */
    public boolean addUrl(URL url) {
        boolean b = urls.add(url);
        return b;
    }

    public EventList<URL> getUrls() {
        return urls;
    }

    public void clearUrls() {
        urls.clear();
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        boolean oldValue = this.visible;
        this.visible = visible;
        propertySupport.firePropertyChange(PROP_VISIBLE, oldValue, visible);
    }

    public void clearDialog() {
        clearUrls();
        setListType(AddDialogControl.COM_WHITESPACE);
        setListFileName("");
        setSelectedPanel(PanelList.URL);
        setAddURL("");
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
