/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.view;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.github.moaxcp.downloadmanager.model.Model;
import com.github.moaxcp.downloadmanager.model.db.Download;
import com.github.moaxcp.downloadmanager.view.renderer.ByteRateRenderer;
import com.github.moaxcp.downloadmanager.view.renderer.ByteRenderer;
import com.github.moaxcp.downloadmanager.view.renderer.ListRenderer;
import com.github.moaxcp.downloadmanager.view.renderer.ProgressRenderer;
import com.github.moaxcp.downloadmanager.view.renderer.TimeRenderer;

/**
 *
 * @author john
 */
public class DownloadTableFormat implements AdvancedTableFormat<Download>, TableColumnModelListener, PropertyChangeListener, MouseListener {

    private List<ColumnStatus> columns;
    private EventTableModel<Download> tableModel;
    private TableColumnModel columnModel;
    private Map<String, Comparator> comparators;
    private boolean resizingColumn;
    private boolean ignoreAdd;

    private class DataComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            if (o1 instanceof String && o2 instanceof String) {
                return ((String) o1).compareTo((String) o2);
            } else if (o1 instanceof Boolean && o2 instanceof Boolean) {
                return ((Boolean) o1).compareTo((Boolean) o2);
            } else if (o1 instanceof Integer && o2 instanceof Integer) {
                return ((Integer) o1).compareTo((Integer) o2);
            } else if (o1 instanceof Float && o2 instanceof Float) {
                return ((Float) o1).compareTo((Float) o2);
            } else if (o1 instanceof Double && o2 instanceof Double) {
                return ((Double) o1).compareTo((Double) o2);
            } else if (o1 instanceof Long && o2 instanceof Long) {
                return ((Long) o1).compareTo((Long) o2);
            } else {
                return o1.toString().compareTo(o2.toString());
            }
        }
    }

    public DownloadTableFormat(List<ColumnStatus> columns) {
        comparators = new HashMap<String, Comparator>();
        this.columns = columns;
        resizingColumn = false;

        for (ColumnStatus c : columns) {
            c.addPropertyChangeListener(this);
            comparators.put(c.getName(), new DataComparator());
        }
        
    }

    public void initColumns() {
        ignoreAdd = true;
        for(int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn t = columnModel.getColumn(i);
            ColumnStatus c = getColumnStatus(getColumnName(t.getModelIndex()));
            if(c.getWidth() != t.getWidth()) {
                t.setPreferredWidth(c.getWidth());
            }
        }
        
        List<TableColumn> tableColumns = new ArrayList<TableColumn>();

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn c = columnModel.getColumn(i);
            tableColumns.add(c);
        }

        for (TableColumn c : tableColumns) {
            columnModel.removeColumn(c);
        }

        TreeMap<Integer, ColumnStatus> orderMap = new TreeMap<Integer, ColumnStatus>();

        for (ColumnStatus c : columns) {
            if (c.isVisible()) {
                orderMap.put(c.getViewOrder(), c);
            }
        }

        while (orderMap.firstEntry() != null) {
            int key = orderMap.firstKey();
            ColumnStatus cs = orderMap.get(key);
            for (TableColumn tc : tableColumns) {
                if (tc.getIdentifier().toString().equals(cs.getName())) {
                    columnModel.addColumn(tc);
                    break;
                }
            }
            orderMap.remove(key);
        }
        ignoreAdd = false;
    }
    
    public void initWidthModel() {
        for(int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn t = columnModel.getColumn(i);
            ColumnStatus c = getColumnStatus(getColumnName(t.getModelIndex()));
            if(c.getWidth() != t.getWidth()) {
                c.setWidth(t.getWidth());
            }
        }
    }

    public List<ColumnStatus> getColumns() {
        return columns;
    }

    public void setTableModel(EventTableModel<Download> tableModel) {
        this.tableModel = tableModel;
    }

    public void setColumnModel(TableColumnModel columnModel) {
        this.columnModel = columnModel;
        columnModel.addColumnModelListener(this);
    }

    private ColumnStatus getColumnStatus(String name) {
        for (ColumnStatus c : columns) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Column " + name + " does not exist");
    }

    public boolean isVisible(String name) {
        return getColumnStatus(name).isVisible();
    }

    public void setVisible(String name, boolean vis) {
        getColumnStatus(name).setVisible(vis);
        tableModel.setTableFormat(this);
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn c = columnModel.getColumn(i);
            getColumnStatus(getColumnName(c.getModelIndex())).setViewOrder(i);
        }
    }

    public void setViewOrder(String name, int viewOrder) {
        getColumnStatus(name).setViewOrder(viewOrder);
    }

    @Override
    public int getColumnCount() {
        int i = 0;
        for (ColumnStatus c : columns) {
            if (c.isVisible()) {
                i++;
            }
        }
        return i;
    }

    @Override
    public String getColumnName(int column) {
        int i = 0;
        for (ColumnStatus c : columns) {
            if (c.isVisible()) {
                if (i == column) {
                    return c.getName();
                }
                i++;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Object getColumnValue(Download download, int column) {
        return download.getProperty(getColumnName(column));
    }

    private Class getColumnClass(String name) {
        if (name.equals(Download.PROP_PROGRESS)) {
            return ProgressRenderer.class;
        } else if (name.equals(Download.PROP_BYTESPERSECOND)) {
            return ByteRateRenderer.class;
        } else if (name.equals(Download.PROP_DOWNLOADED) || name.equals(Download.PROP_SIZE)) {
            return ByteRenderer.class;
        } else if (name.equals(Download.PROP_LOCATIONS) || name.equals(Download.PROP_HREFLINKS) || name.equals(Download.PROP_SRCLINKS) || name.equals(Download.PROP_WORDS)) {
            return ListRenderer.class;
        } else if (name.equals(Download.PROP_DOWNLOADTIME) || name.equals(Download.PROP_RETRYTIME) || name.equals(Download.PROP_TIMELEFT)) {
            return TimeRenderer.class;
        } else {
            return String.class;
        }
    }

    @Override
    public Class getColumnClass(int column) {
        int i = 0;
        for (ColumnStatus c : columns) {
            if (c.isVisible()) {
                if (i == column) {
                    return getColumnClass(c.getName());
                }
                i++;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Comparator getColumnComparator(int column) {
        return comparators.get(getColumnName(column));
    }

    @Override
    public void columnAdded(TableColumnModelEvent e) {
        if(!ignoreAdd) {
            initWidthModel();
        }
    }

    @Override
    public void columnRemoved(TableColumnModelEvent e) {
    }

    @Override
    public void columnMoved(TableColumnModelEvent e) {
        if (e.getFromIndex() != e.getToIndex()) {
            String to = getColumnName(columnModel.getColumn(e.getToIndex()).getModelIndex());
            String from = getColumnName(columnModel.getColumn(e.getFromIndex()).getModelIndex());
            setViewOrder(from, e.getFromIndex());
            setViewOrder(to, e.getToIndex());
        }
    }

    @Override
    public void columnMarginChanged(ChangeEvent e) {
        resizingColumn = true;
    }

    @Override
    public void columnSelectionChanged(ListSelectionEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(resizingColumn) {
            resizingColumn = false;
            initWidthModel();
            Model.getSettingsSaver().save();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ColumnStatus.PROP_VISIBLE)) {
            tableModel.setTableFormat(this);
            Model.getSettingsSaver().save();
        } else if(evt.getPropertyName().equals(ColumnStatus.PROP_VIEWORDER)) {
            Model.getSettingsSaver().save();
        }
    }
}
