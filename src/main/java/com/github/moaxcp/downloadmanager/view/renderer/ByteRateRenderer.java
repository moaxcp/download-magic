/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.view.renderer;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import penny.download.Downloads;

/**
 *
 * @author john
 */
public class ByteRateRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
    
    private DefaultTableCellRenderer renderer;
    
    public ByteRateRenderer() {
        renderer = new DefaultTableCellRenderer();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        renderer.getTableCellRendererComponent(table, Downloads.formatByteSize((Long) value) + " /s", isSelected, hasFocus, row, column);
        return renderer;
    }
}
