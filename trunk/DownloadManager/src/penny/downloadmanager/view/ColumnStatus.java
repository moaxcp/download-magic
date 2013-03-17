/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 *
 * @author john
 */
public class ColumnStatus implements Serializable {
    public static final String PROP_NAME = "name";
    private String name;
    public static final String PROP_VISIBLE = "visible";
    private boolean visible;
    public static final String PROP_VIEWORDER = "viewOrder";
    private int viewOrder;
    public static final String PROP_WIDTH = "width";
    private int width;
    
    private transient PropertyChangeSupport propertySupport;

    public ColumnStatus(String name, boolean visible) {
        this.name = name;
        this.visible = visible;
        this.width = 75;
        propertySupport = new PropertyChangeSupport(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldValue = this.name;
        this.name = name;
        propertySupport.firePropertyChange(PROP_NAME, oldValue, this.name);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        boolean oldValue = this.visible;
        this.visible = visible;
        propertySupport.firePropertyChange(PROP_VISIBLE, oldValue, this.visible);
    }

    public int getViewOrder() {
        return viewOrder;
    }

    public void setViewOrder(int order) {
        int oldValue = this.viewOrder;
        this.viewOrder = order;
        propertySupport.firePropertyChange(PROP_VIEWORDER, oldValue, this.viewOrder);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        int oldValue = this.width;
        this.width = width;
        propertySupport.firePropertyChange(PROP_WIDTH, oldValue, this.width);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}
