/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.gui;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 *
 * @author john
 */
public class PropertyEntry implements PropertyChangeListener {

    public static final String PROP_KEY = "key";
    private String key;
    public static final String PROP_VALUE = "value";
    private Object value;
    private PropertyChangeSupport propertySupport;

    public PropertyEntry(String key, Object value) {
        this.key = key;
        this.value = value;
        propertySupport = new PropertyChangeSupport(this);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        Object oldValue = this.value;
        this.value = value;
        propertySupport.firePropertyChange(PROP_VALUE, oldValue, value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        String oldValue = this.key;
        this.key = key;
        propertySupport.firePropertyChange(PROP_KEY, oldValue, key);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(key)) {
            if (evt instanceof IndexedPropertyChangeEvent) {
                setValue(((IndexedPropertyChangeEvent) evt).getIndex());
            } else {
                setValue(evt.getNewValue());
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}
