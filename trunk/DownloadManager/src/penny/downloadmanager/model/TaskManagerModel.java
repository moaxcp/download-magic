/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model;

import penny.downloadmanager.control.task.Task;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;
import penny.downloadmanager.model.task.TaskData;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author john
 */
public class TaskManagerModel {

    private EventList<TaskData> tasks;

    public static final String PROP_RUNNING = "running";
    private boolean running;

    private PropertyChangeSupport propertySupport;

    public TaskManagerModel() {
        tasks = new ObservableElementList<TaskData>(
                GlazedLists.threadSafeList(new BasicEventList<TaskData>()),
                GlazedLists.beanConnector(TaskData.class));
        running = false;
        propertySupport = new PropertyChangeSupport(this);
    }

    /**
     * @return the running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @param running the running to set
     */
    public void setRunning(boolean running) {
        boolean oldValue = this.running;
        this.running = running;
        propertySupport.firePropertyChange(PROP_RUNNING, oldValue, running);
    }

    public EventList<TaskData> getTasks() {
        return tasks;
    }

    public void addPropertyChangeSupport(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeSupport(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeSupport(String prop, PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(prop, listener);
    }

    public void removePropertyChangeSupport(String prop, PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(prop, listener);
    }
}
