/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.view.renderer;

import java.awt.Component;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import penny.download.Downloads;

/**
 *
 * @author john
 */
public class ListRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
    
    private DefaultTableCellRenderer renderer;
    
    public ListRenderer() {
        renderer = new DefaultTableCellRenderer();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        renderer.getTableCellRendererComponent(table, ((List) value).size(), isSelected, hasFocus, row, column);
        return renderer;
    }
    
}
