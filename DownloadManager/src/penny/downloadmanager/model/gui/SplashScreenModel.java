/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author john
 */
public class SplashScreenModel {
    public static final String PROP_STAGE = "stage";
    private String stage;
    public static final String PROP_MESSAGE = "message";
    private String message;
    public static final String PROP_CURRENT = "current";
    private int current;
    public static final String PROP_SIZE = "size";
    private int size;
    public static final String PROP_VISIBLE = "visible";
    private boolean visible;
    
    private PropertyChangeSupport propertySupport;
    
    public SplashScreenModel() {
        stage = "";
        message = "";
        current = 0;
        size = 0;
        visible = false;
        propertySupport = new PropertyChangeSupport(this);
    }

    /**
     * @return the stage
     */
    public String getStage() {
        return stage;
    }

    /**
     * @param stage the stage to set
     */
    public void setStage(String stage) {
        setMessage("");
        String oldValue = this.stage;
        this.stage = stage;
        propertySupport.firePropertyChange(PROP_STAGE, oldValue, this.stage);
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        String oldValue = this.message;
        this.message = message;
        propertySupport.firePropertyChange(PROP_MESSAGE, oldValue, this.message);
    }

    /**
     * @return the current
     */
    public int getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(int current) {
        int oldValue = this.current;
        this.current = current;
        propertySupport.firePropertyChange(PROP_CURRENT, oldValue, this.current);
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        int oldValue = this.size;
        this.size = size;
        propertySupport.firePropertyChange(PROP_SIZE, oldValue, this.size);
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
        propertySupport.firePropertyChange(PROP_VISIBLE, oldValue, this.visible);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}
