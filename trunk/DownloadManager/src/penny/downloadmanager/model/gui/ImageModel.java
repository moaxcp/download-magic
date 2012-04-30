/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.gui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 *
 * @author john
 */
public class ImageModel implements Serializable {
    public static final String PROP_WIDTHANDHEIGHT = "widthAndHeight";
    private boolean widthAndHeight;

    public static final String PROP_WIDTHNAME = "widthName";
    private String widthName;

    public static final String PROP_HEIGHTNAME = "heightName";
    private String heightName;

    public ImageModel() {
        widthAndHeight = false;
        widthName = "image-width";
        heightName = "image-height";
    }

    public ImageModel(ImageModel model) {
        this.copy(model);
    }

    public void copy(ImageModel model) {
        this.widthAndHeight = model.isWidthAndHeight();
    }

    /**
     * @return the widthAndHeight
     */
    public boolean isWidthAndHeight() {
        return widthAndHeight;
    }

    /**
     * @param widthAndHeight the widthAndHeight to set
     */
    public void setWidthAndHeight(boolean widthAndHeight) {
        this.widthAndHeight = widthAndHeight;
    }

    /**
     * @return the widthName
     */
    public String getWidthName() {
        return widthName;
    }

    /**
     * @param widthName the widthName to set
     */
    public void setWidthName(String widthName) {
        this.widthName = widthName;
    }

    /**
     * @return the heightName
     */
    public String getHeightName() {
        return heightName;
    }

    /**
     * @param heightName the heightName to set
     */
    public void setHeightName(String heightName) {
        this.heightName = heightName;
    }
}
