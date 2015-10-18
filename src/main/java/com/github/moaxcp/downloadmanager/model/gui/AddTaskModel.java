/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.model.gui;

import java.beans.PropertyChangeListener;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 *
 * @author john
 */
public class AddTaskModel {
    public static enum TaskList {
        DOWNLOAD("Download"),
        LinkToDownload("Link To Download");

        private String name;

        TaskList(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static final String PROP_VISIBLE = "visible";
    public static final String PROP_SELECTEDTASK = "selectedTask";


    private TaskList selectedTask;
    private boolean visible;
    private SwingPropertyChangeSupport propertySupport;

    public AddTaskModel() {
        selectedTask = TaskList.DOWNLOAD;
        visible = false;
        propertySupport = new SwingPropertyChangeSupport(this, true);
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

    /**
     * @return the selectedPanel
     */
    public TaskList getSelectedTask() {
        return selectedTask;
    }

    /**
     * @param selectedPanel the selectedPanel to set
     */
    public void setSelectedTask(TaskList selectedPanel) {
        TaskList oldValue = this.selectedTask;
        this.selectedTask = selectedPanel;
        propertySupport.firePropertyChange(PROP_SELECTEDTASK, oldValue, selectedPanel);
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
