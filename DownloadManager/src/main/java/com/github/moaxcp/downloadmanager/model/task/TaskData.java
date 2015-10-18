/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.model.task;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 *
 * @author john
 */
public abstract class TaskData implements Serializable {
    
    public static final String PROP_NAME = "name";
    protected String name;
    public static final String PROP_STATUS = "taskStatus";
    protected Status status;

    protected PropertyChangeSupport propertySupport;

    public TaskData() {
        propertySupport = new PropertyChangeSupport(this);
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        String oldValue = getName();
        this.name = name;
        propertySupport.firePropertyChange(PROP_NAME, oldValue, name);
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        Status oldValue = getStatus();
        this.status = status;
        propertySupport.firePropertyChange(PROP_STATUS, oldValue, status);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}
