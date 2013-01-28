/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import penny.downloadmanager.model.db.Download;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import penny.download.DownloadStatus;
import penny.download.Downloads;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JProgressBar;

/**
 *
 * @author john
 */
public class DownloadTableFormat implements AdvancedTableFormat<Download> {

    private Map<String, Class> columns;
    public static final String PROP_PROGRESS = "progress";
    public static final String PROP_RATE = "rate";
    public static final String PROP_TIMELEFT = "timeLeft";
    private Comparator comparator = new Comparator() {

        @Override
        public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };

    public DownloadTableFormat() {
        columns = new TreeMap<String, Class>();
        columns.put(PROP_PROGRESS, JProgressBar.class);
        columns.put(PROP_RATE, String.class);
        columns.put(PROP_TIMELEFT, String.class);
    }

    public boolean getColumns(Download d) {
        boolean r = false;
        for (String s : Download.propertyNames) {
            if (!columns.containsKey(s)) {
                Object o = d.getProperty(s);
                if (o != null) {
                    columns.put(s, o.getClass());
                    r = true;
                }
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
        if(name.equals(PROP_TIMELEFT)) {
            long rate = (download.getDownloadTime() / 1000000 == 0 ? 0 : download.getDownloaded() / (download.getDownloadTime() / 1000000));
            long time = rate == 0 ? 0 : (download.getSize() - download.getDownloaded()) / rate;
            return Downloads.formatMilliTimeMilli(time);
        }
        if (name.equals(PROP_PROGRESS)) {
            if (download.getSize() >= 0) {
                return (((float) download.getDownloaded()) / (float) download.getSize()) * 100;
            } else {
                return (float) -1;
            }
        }
        if (name.equals(PROP_RATE)) {
            return Downloads.getRate(download);
        }
        if (name.equals(Download.PROP_DOWNLOADED)) {
            return Downloads.formatByteSize(download.getDownloaded());
        }
        if (name.equals(Download.PROP_SIZE)) {
            return Downloads.formatByteSize(download.getSize());
        }
        if (name.equals(Download.PROP_DOWNLOADTIME)) {
            return Downloads.formatMilliTimeMilli(download.getDownloadTime() / 1000000);
        }
        if (name.equals(Download.PROP_RETRYTIME)) {
            return Downloads.formatMilliTimeMilli(download.getRetryTime() / 1000000);
        }
        if(name.equals(Download.PROP_HREFLINKS)) {
            return download.getHrefLinks().size();
        }
        if(name.equals(Download.PROP_SRCLINKS)) {
            return download.getSrcLinks().size();
        }
        if(name.equals(Download.PROP_WORDS)) {
            return download.getWords().size();
        }
        if(name.equals(Download.PROP_LOCATIONS)) {
            return download.getLocations().size();
        }
        return download.getProperty(name);
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
