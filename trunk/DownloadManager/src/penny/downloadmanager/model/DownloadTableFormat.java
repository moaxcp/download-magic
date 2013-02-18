/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import penny.downloadmanager.model.db.Download;
import penny.downloadmanager.util.Util;

/**
 *
 * @author john
 */
public class DownloadTableFormat implements AdvancedTableFormat<Download> {

    private Map<String, Class> columns;
    private Comparator comparator = new Comparator() {

        @Override
        public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };

    public DownloadTableFormat() {
        columns = new TreeMap<String, Class>();
    }

    public boolean getColumns(Download d) {
        boolean r = false;
        for (String s : Download.propertyNames) {
            if (!columns.containsKey(s)) {
                    columns.put(s, String.class);
                    r = true;
            }
        }
        return r;
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnName(int column) {
        int i = 0;
        for (String d : columns.keySet()) {
            if (i == column) {
                return d;
            }
            i++;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Object getColumnValue(Download download, int column) {
        String name = getColumnName(column);
        return Util.toFormattedString(download, name);
    }

    @Override
    public Class getColumnClass(int column) {
        int i = 0;
        for (String s : columns.keySet()) {
            if (i == column) {
                return columns.get(s);
            }
            i++;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Comparator getColumnComparator(int column) {
        return comparator;
    }

    /**
     * @return the columns
     */
    public Map<String, Class> getColumns() {
        return columns;
    }
}
