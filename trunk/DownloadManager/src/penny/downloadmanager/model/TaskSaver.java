/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import penny.downloadmanager.model.task.TaskData;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author john
 */
public class TaskSaver implements ListEventListener<TaskData>, PropertyChangeListener {

    private ObservableElementList<TaskData> tasks;

    public TaskSaver(ObservableElementList<TaskData> tasks) {
        this.tasks = tasks;
        this.tasks.addListEventListener(this);
        try {
            tasks.getReadWriteLock().writeLock().lock();
            for (TaskData t : tasks) {
                t.addPropertyChangeListener(this);
            }
        } finally {
            tasks.getReadWriteLock().writeLock().unlock();
        }
    }

    public void saveList() {
        ArrayList<TaskData> saveList = new ArrayList<TaskData>();
        try {
            tasks.getReadWriteLock().readLock().lock();
            for (TaskData t : tasks) {
                saveList.add(t);
            }
        } finally {
            tasks.getReadWriteLock().readLock().unlock();
        }

        ObjectOutputStream out;
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
