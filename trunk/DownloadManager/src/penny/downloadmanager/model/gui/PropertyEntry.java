/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model.gui;

import java.beans.PropertyChangeSupport;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 *
 * @author john
 */
public class PropertyEntry {
    public static final String PROP_KEY = "key";
    private String key;

    public static final String PROP_VALUE = "value";
    private String value;
    
    private PropertyChangeSupport propertySupport;
    
    public PropertyEntry(String key, String value) {
        this.key = key;
        this.value = value;
        propertySupport = new SwingPropertyChangeSupport(this);
    }

    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        String oldValue = this.value;
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
}
