/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import penny.downloadmanager.model.db.DAOFactory;
import penny.downloadmanager.model.db.DownloadDAO;
import penny.downloadmanager.model.task.TaskData;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class TaskSaver implements ListEventListener<TaskData>, PropertyChangeListener {

    private ObservableElementList<TaskData> tasks;

    public TaskSaver(ObservableElementList<TaskData> tasks) {
        this.tasks = tasks;
        this.tasks.addListEventListener(this);
        for (TaskData t : tasks) {
            t.addPropertyChangeListener(this);
        }
    }

    public void saveList() {
        ArrayList<TaskData> saveList = new ArrayList<TaskData>();
        for (TaskData t : tasks) {
            saveList.add(t);
        }

        
    }

    @Override
    public void listChanged(ListEvent listChanges) {

        EventList changeList = listChanges.getSourceList();

        while (listChanges.next()) {
            int sourceIndex = listChanges.getIndex();
            int changeType = listChanges.getType();

            switch (changeType) {
                case ListEvent.DELETE:
                    changeList.getReadWriteLock().readLock().lock();
                    TaskData t1 = (TaskData) listChanges.getOldValue();
                    changeList.getReadWriteLock().readLock().unlock();
                    t1.removePropertyChangeListener(this);
                    saveList();
                    break;
                case ListEvent.INSERT:
                    changeList.getReadWriteLock().readLock().lock();
                    TaskData t2 = (TaskData) changeList.get(sourceIndex);
                    changeList.getReadWriteLock().readLock().unlock();
                    t2.addPropertyChangeListener(this);
                    saveList();
                    break;
                case ListEvent.UPDATE:

                    break;
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        saveList();
    }
}
