/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.swing.UIManager;

/**
 *
 * @author john
 */
public class LookAndFeelModel implements Serializable {
    
    private transient Map<String, String> lookAndFeels;
    public static final String PROP_LOOKANDFEEL = "lookAndFeel";
    private String lookAndFeel;
    
    private transient PropertyChangeSupport propertySupport;
    
    public LookAndFeelModel() {
        lookAndFeels = new HashMap<String, String>();
        propertySupport = new PropertyChangeSupport(this);
        for(UIManager.LookAndFeelInfo i : UIManager.getInstalledLookAndFeels()) {
            lookAndFeels.put(i.getName(), i.getClassName());
        }
    }
    
    public void copy(LookAndFeelModel model) {
        lookAndFeels = new HashMap<String, String>();
        for(UIManager.LookAndFeelInfo i : UIManager.getInstalledLookAndFeels()) {
            lookAndFeels.put(i.getName(), i.getClassName());
        }
        if(lookAndFeels.keySet().contains(model.getLookAndFeel())) {
            setLookAndFeel(model.getLookAndFeel());
        }
    }
    
    public Map<String, String> getLookAndFeels() {
        return lookAndFeels;
    }
    
    public String getLookAndFeel() {
        return lookAndFeel;
    }
    
    public void setLookAndFeels(Map<String, String> lookAndFeels) {
        this.lookAndFeels = lookAndFeels;
    }
    
    public void setLookAndFeel(String lookAndFeel) {
        if(!lookAndFeels.keySet().contains(lookAndFeel)) {
            throw new IllegalArgumentException(lookAndFeel + " is not avaliable.");
        }
        String oldValue = this.lookAndFeel;
        this.lookAndFeel = lookAndFeel;
        propertySupport.firePropertyChange(PROP_LOOKANDFEEL, oldValue, this.lookAndFeel);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String prop, PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(prop, listener);
    }

    public void removePropertyChangeListener(String prop, PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(prop, listener);
    }
}
