/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.model;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import com.github.moaxcp.downloadmanager.model.task.TaskData;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
        try {
            tasks.getReadWriteLock().writeLock().lock();
            for (TaskData t : tasks) {
                t.addPropertyChangeListener(this);
            }
        } finally {
            tasks.getReadWriteLock().writeLock().unlock();
        }
    }

    public void saveList() throws IOException {
        ArrayList<TaskData> saveList = new ArrayList<TaskData>();
        try {
            tasks.getReadWriteLock().readLock().lock();
            for (TaskData t : tasks) {
                saveList.add(t);
            }
        } finally {
            tasks.getReadWriteLock().readLock().unlock();
        }

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data/tasks.dat"));
        //out.writeObject(saveList);
        out.close();
    }
    
    public void loadList() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("data/tasks.dat"));
        //ArrayList<TaskData> list = (ArrayList<TaskData>) in.readObject();
        in.close();
//        tasks.getReadWriteLock().writeLock().lock();
//        for(TaskData t : list) {
//            tasks.add(t);
//        }
//        tasks.getReadWriteLock().writeLock().unlock();
    }

    @Override
    public void listChanged(ListEvent listChanges) {

        EventList changeList = listChanges.getSourceList();

        while (listChanges.next()) {
            try {
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
            } catch (IOException ex) {
                Logger.getLogger(TaskSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            saveList();
        } catch (IOException ex) {
            Logger.getLogger(TaskSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
