/*
 * ProgressRenderer.java
 *
 * Created on February 23, 2007, 8:51 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.view.renderer;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author john.mercier
 */
public class ProgressRenderer extends JProgressBar implements TableCellRenderer {
    
    private int max;

    public ProgressRenderer(int max) {
        super(0, max);
        this.max = max;
        this.setStringPainted(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int currentValue = (int) ((Double) value).doubleValue() * max;
        double percent = ((Double) value).doubleValue() * 100;
        if (currentValue >= 0) {
            setValue(currentValue);
            this.setString(String.format("%1$.2f%%", percent));
        } else {
            setValue(0);
            this.setString("unknown");
        }
        return this;
    }
}
