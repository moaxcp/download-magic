/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.event.SwingPropertyChangeSupport;
import penny.downloadmanager.model.StartupModel;

/**
 *
 * @author john
 */
public class StartupDialogModel {

    public static final String PROP_VISIBLE = "visible";
    private boolean visible;

    PropertyChangeSupport propertySupport = new SwingPropertyChangeSupport(this, true);

    private StartupModel startupModel;
    private StartupModel startupModelCopy;


    public StartupDialogModel() {
        startupModel = new StartupModel();
        startupModelCopy = new StartupModel();
    }

    public StartupModel getStartupModel() {
        return startupModel;
    }

    public StartupModel getStartupModelCopy() {
        return startupModelCopy;
    }

    public void setStartupModel(StartupModel startupModel) {
        this.startupModel = startupModel;
        startupModelCopy.copy(startupModel);
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
