/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.model.gui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 *
 * @author john
 */
public class ImageModel implements Serializable {
    public static final String PROP_WIDTHANDHEIGHT = "widthAndHeight";
    private boolean widthAndHeight;

    public static final String PROP_WIDTHNAME = "widthName";
    private String widthName;

    public static final String PROP_HEIGHTNAME = "heightName";
    private String heightName;
    private transient PropertyChangeSupport propertySupport;

    public ImageModel() {
        widthAndHeight = true;
        widthName = "image-width";
        heightName = "image-height";
        propertySupport = new SwingPropertyChangeSupport(this, true);
    }

    public ImageModel(ImageModel model) {
        this.copy(model);
    }

    public void copy(ImageModel model) {
        setWidthAndHeight(model.isWidthAndHeight());
        setWidthName(model.getWidthName());
        setHeightName(model.getHeightName());
    }

    /**
     * @return the widthAndHeight
     */
    public boolean isWidthAndHeight() {
        return widthAndHeight;
    }

    /**
     * @param widthAndHeight the widthAndHeight to set
     */
    public void setWidthAndHeight(boolean widthAndHeight) {
        boolean oldValue = this.widthAndHeight;
        this.widthAndHeight = widthAndHeight;
        propertySupport.firePropertyChange(PROP_WIDTHANDHEIGHT, oldValue, widthAndHeight);
    }

    /**
     * @return the widthName
     */
    public String getWidthName() {
        return widthName;
    }

    /**
     * @param widthName the widthName to set
     */
    public void setWidthName(String widthName) {
        String oldValue = this.widthName;
        this.widthName = widthName;
        propertySupport.firePropertyChange(PROP_WIDTHNAME, oldValue, widthName);
    }

    /**
     * @return the heightName
     */
    public String getHeightName() {
        return heightName;
    }

    /**
     * @param heightName the heightName to set
     */
    public void setHeightName(String heightName) {
        String oldValue = this.heightName;
        this.heightName = heightName;
        propertySupport.firePropertyChange(PROP_HEIGHTNAME, oldValue, heightName);
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
