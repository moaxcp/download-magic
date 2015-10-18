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
public class MD5ingModel implements Serializable {
    public static final String PROP_GENERATEMD5 = "generateMD5";
    private boolean generateMD5;
    
    public static final String PROP_UPDATEMD5 = "updateMD5";
    private boolean updateMD5;

    public static final String PROP_MD5UNKNOWN = "md5Unknown";
    private boolean md5Unknown;

    public static final String PROP_MD5TYPES = "md5Types";
    private EventList<String> md5Types;
    private transient PropertyChangeSupport propertySupport;

    public MD5ingModel() {
        generateMD5 = true;
        updateMD5 = true;
        md5Unknown = true;
        md5Types = new BasicEventList<String>();
        md5Types.add("*");
        propertySupport = new SwingPropertyChangeSupport(this, true);
    }

    public MD5ingModel(MD5ingModel model) {
        this.copy(model);
    }

    public void copy(MD5ingModel model) {
        setGenerateMD5(model.isGenerateMD5());
        setUpdateMD5(model.isUpdateMD5());
        setMd5Unknown(model.isMd5Unknown());
        md5Types.clear();
        md5Types.addAll(model.getMd5Types());
    }

    /**
     * @return the generateMD5
     */
    public boolean isGenerateMD5() {
        return generateMD5;
    }

    /**
     * @param generateMD5 the generateMD5 to set
     */
    public void setGenerateMD5(boolean generateMD5) {
        boolean oldValue = this.generateMD5;
        this.generateMD5 = generateMD5;
        propertySupport.firePropertyChange(PROP_GENERATEMD5, oldValue, generateMD5);
    }

    /**
     * @return the updateMD5
     */
    public boolean isUpdateMD5() {
        return updateMD5;
    }

    /**
     * @param updateMD5 the updateMD5 to set
     */
    public void setUpdateMD5(boolean updateMD5) {
        boolean oldValue = this.updateMD5;
        this.updateMD5 = updateMD5;
        propertySupport.firePropertyChange(PROP_UPDATEMD5, oldValue, updateMD5);
    }

    /**
     * @return the md5Unknown
     */
    public boolean isMd5Unknown() {
        return md5Unknown;
    }

    /**
     * @param md5Unknown the md5Unknown to set
     */
    public void setMd5Unknown(boolean md5Unknown) {
        boolean oldValue = this.md5Unknown;
        this.md5Unknown = md5Unknown;
        propertySupport.firePropertyChange(PROP_MD5UNKNOWN, oldValue, md5Unknown);
    }

    /**
     * @return the md5Types
     */
    public EventList<String> getMd5Types() {
        return md5Types;
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
