/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.task;

import penny.downloadmanager.control.Application;
import penny.downloadmanager.model.TaskManagerModel;
import penny.downloadmanager.model.task.TaskData;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class TaskManager {

    private class TaskRunnable implements Runnable {

        public void run() {
            model.setRunning(true);
            start:
            for (int i = 0; i < model.getTasks().size() && model.isRunning(); i++) {
                TaskData t = model.getTasks().get(i);
                switch (t.getStatus()) {
                    case QUEUED:
                        try {
                            Task.getTask(t).run();
                        } catch (Exception e) {
                            Logger.getLogger(TaskRunnable.class.getName()).log(Level.SEVERE, null, e);
                            model.setRunning(false);
                        }
                        break;
                    case RUNNING:
                        break start;
                    case FINISHED:
                        break start;
                    case STOPPED:
                        break start;
                    case ERROR:
                        break start;
                }
            }
            model.setRunning(false);
        }
    }
    private Thread thread;
    private TaskRunnable runnable;
    TaskManagerModel model;

    public TaskManager(TaskManagerModel model) {
        this.model = model;
        runnable = new TaskRunnable();
    }

    public boolean start() {
        if (!model.isRunning()) {
            thread = Application.getThread(runnable);
            thread.setName("TaskManager");
            thread.start();
        }
        return !model.isRunning();
    }

    public void stop() {
        model.setRunning(false);
    }

    public TaskManagerModel getModel() {
        return model;
    }
}
