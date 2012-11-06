/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.view.test;

import penny.downloadmanager.view.renderer.ProgressRenderer;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.swing.EventTableColumnModel;
import penny.downloadmanager.model.db.DownloadData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author john
 */
public class DownloadTableFormatCM implements AdvancedTableFormat<DownloadData>, ListEventListener<DownloadData> {

    private EventList<TableColumn> columns;
    private EventTableColumnModel<TableColumn> columnModel;
    public static final String PROP_PROGRESS = "progress";
    private List<Class> classes;

    public DownloadTableFormatCM() {
        ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true);
        columns = new BasicEventList<TableColumn>();
        columnModel = new EventTableColumnModel<TableColumn>(columns);
        classes = new ArrayList<Class>();
        TableColumn col = new TableColumn();
        col.setHeaderValue(PROP_PROGRESS);
        col.setCellRenderer(renderer);
        columnModel.addColumn(col);
        classes.add(JProgressBar.class);
    }

    public void getColumns(DownloadData d) {
        for (String s : d.getPropertyNames()) {
            boolean add = true;
            //required lock for read/write transaction
            columns.getReadWriteLock().writeLock().lock();
            for (TableColumn t : columns) {
                if (t.getHeaderValue().equals(s)) {
                    add = false;
                }
            }
            if (add) {
                TableColumn col = new TableColumn();
                col.setHeaderValue(s);
                col.setCellRenderer(new DefaultTableCellRenderer());
                columnModel.addColumn(col);
                classes.add(d.getProperty(s).getClass());
            }
            columns.getReadWriteLock().writeLock().unlock();
        }
    }

    public int getColumnCount() {
        return columns.size();
    }

    public String getColumnName(int column) {
        int i = 0;
        for (TableColumn c : columns) {
            if (i == column) {
                return c.getHeaderValue().toString();
            }
            i++;
        }
        throw new IllegalArgumentException();
    }

    public Object getColumnValue(DownloadData download, int column) {
        String name = getColumnName(column);
        if (name.equals(PROP_PROGRESS)) {
            if (download.getSize() != 0) {
                return (((float) download.getDownloaded()) / (float) download.getSize()) * 100;
            }
            return null;
        }
        return download.getProperty(name);
    }

    public Class getColumnClass(int column) {
        return classes.get(column);
    }

    public Comparator getColumnComparator(final int column) {
        if (getColumnName(column).equals(DownloadData.PROP_URL)) {

            return new Comparator() {

                public int compare(Object o1, Object o2) {
                    return String.CASE_INSENSITIVE_ORDER.compare(o1.toString(),
                            o2.toString());
                }
            };
        } else {
            return null;
        }
    }

    public EventTableColumnModel<TableColumn> getColumnModel() {
        return columnModel;
    }

    public void listChanged(ListEvent<DownloadData> listChanges) {
        EventList<DownloadData> changeList = listChanges.getSourceList();

        while (listChanges.next()) {
            int sourceIndex = listChanges.getIndex();
            int changeType = listChanges.getType();

            switch (changeType) {
                case ListEvent.DELETE:

                    break;
                case ListEvent.INSERT:
                    changeList.getReadWriteLock().readLock().lock();
                    DownloadData obj = changeList.get(sourceIndex);
                    getColumns(obj);
                    changeList.getReadWriteLock().readLock().unlock();

                    break;
                case ListEvent.UPDATE:
                    break;
            }
        }
    }
}
