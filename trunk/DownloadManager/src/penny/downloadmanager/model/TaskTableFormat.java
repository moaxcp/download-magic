/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import penny.download.Downloads;
import penny.downloadmanager.control.task.Task;
import penny.downloadmanager.model.task.TaskData;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JProgressBar;

/**
 *
 * @author john
 */
public class TaskTableFormat implements AdvancedTableFormat<TaskData> {

    private Map<String, Class> columns;
    private Comparator comparator = new Comparator() {

        public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };

    public TaskTableFormat() {
        columns = new TreeMap<String, Class>();
    }

    public int getColumnCount() {
        return columns.size();
    }

    public String getColumnName(int column) {
        if(column == 0) {
            return "Name";
        } else if(column == 1) {
            return "Status";
        }
        return null;
    }

    public Object getColumnValue(TaskData task, int column) {
        if(column == 0) {
            return task.getName();
        } else if(column == 1) {
            return task.getStatus().toString();
        }
        return null;
    }

    public Class getColumnClass(int column) {
        return String.class;
    }

    public Comparator getColumnComparator(int column) {
        return comparator;
    }
}
