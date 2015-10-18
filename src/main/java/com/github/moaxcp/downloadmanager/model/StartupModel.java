/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 *
 * @author john
 */
public class StartupModel implements Serializable {

    public static final String PROP_CHECKSIZES = "checkFiles";
    private boolean checkSizes;

    public static final String PROP_CHECKMD5S = "checkMD5s";
    private boolean checkMD5s;

    private transient PropertyChangeSupport propertySupport;

    public StartupModel() {
        checkSizes = true;
        checkMD5s = true;
        propertySupport = new SwingPropertyChangeSupport(this, true);
    }

    public StartupModel(StartupModel model) {
        this.copy(model);
    }

    public void copy(StartupModel model) {
        setCheckSizes(model.isCheckSizes());
        setCheckMD5s(model.isCheckMD5s());
    }

    /**
     * @return the checkMD5s
     */
    public boolean isCheckMD5s() {
        return checkMD5s;
    }

    /**
     * @param checkMD5s the checkMD5s to set
     */
    public void setCheckMD5s(boolean checkMD5s) {
        boolean oldValue = this.checkMD5s;
        this.checkMD5s = checkMD5s;
        propertySupport.firePropertyChange(PROP_CHECKMD5S, oldValue, checkMD5s);
    }

    /**
     * @return the checkSizes
     */
    public boolean isCheckSizes() {
        return checkSizes;
    }

    /**
     * @param checkSizes the checkSizes to set
     */
    public void setCheckSizes(boolean checkSizes) {
        boolean oldValue = this.checkSizes;
        this.checkSizes = checkSizes;
        propertySupport.firePropertyChange(PROP_CHECKSIZES, oldValue, checkSizes);
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
