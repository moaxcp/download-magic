/*
 * ProgressRenderer.java
 *
 * Created on February 23, 2007, 8:51 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package penny.downloadmanager.view.renderer;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author john.mercier
 */
public class ProgressRenderer extends JProgressBar implements TableCellRenderer {

    public ProgressRenderer(int min, int max) {
        super(min, max);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int perc = (int) ((Float) value).floatValue();
        if (perc >= 0) {
            setValue((int) ((Float) value).floatValue());
            this.setString(String.format("%1$.2f%2$c", ((Float) value).floatValue(), '%'));
        } else {
            setValue(0);
            this.setString("unknown");
        }
        return this;
    }
}
