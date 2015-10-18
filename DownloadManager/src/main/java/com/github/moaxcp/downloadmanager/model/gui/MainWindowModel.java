/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.model.gui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.SortedList;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.event.SwingPropertyChangeSupport;
import penny.download.DownloadStatus;
import com.github.moaxcp.downloadmanager.model.DownloadSaver;
import com.github.moaxcp.downloadmanager.view.DownloadTableFormat;
import com.github.moaxcp.downloadmanager.model.db.DAOFactory;
import com.github.moaxcp.downloadmanager.model.db.Download;
import com.github.moaxcp.downloadmanager.model.db.DownloadDAO;
import com.github.moaxcp.downloadmanager.model.task.TaskData;
import com.github.moaxcp.downloadmanager.util.Util;

/**
 *
 * @author john
 */
public class MainWindowModel {

    public static final String PROP_VISIBLE = "visible";
    private boolean visible;
    
    public static final String PROP_RANDOM = "randomChange";
    private boolean randomChange;

    public static final String PROP_SELDOWNLOAD = "selectedDownload";
    private Download selectedDownload;

    public static final String PROP_SELTASK = "selectedTask";
    private TaskData selectedTask;

    public static final String PROP_RUNNING = "running";
    private boolean running;

    private EventList<Download> downloads;
    private EventList<TaskData> tasks;
    private DownloadSaver downloadSaver;

    private SwingPropertyChangeSupport propertySupport;

    public MainWindowModel() {
        Comparator<Download> comp = new Comparator<Download>() {

            @Override
            public int compare(Download o1, Download o2) {
                return o1.getUrl().getPath().toString().compareTo(o2.getUrl().getPath().toString());
            }
        };
        downloads = new ObservableElementList<Download>(
                GlazedLists.threadSafeList(new SortedList(new BasicEventList<Download>(), comp)),
                GlazedLists.beanConnector(Download.class));
        DAOFactory.setDB(DAOFactory.JAVADB);

        this.tasks = new ObservableElementList<TaskData>(
                GlazedLists.threadSafeList(new BasicEventList<TaskData>()),
                GlazedLists.beanConnector(TaskData.class));
        visible = false;
        randomChange = false;
        running = false;

        propertySupport = new SwingPropertyChangeSupport(this, true);
    }

    public void clearDownloads() {
        downloadSaver.setSaveDelete(false);
        for(Download d : downloads) {
            Util.remove(new File(d.getTempPath()));
        }
        downloads.clear();
        DAOFactory.getInstance().getDownloadDAO().clearDownloads();
        downloadSaver.setSaveDelete(true);
    }

    public void clearCompleteDownloads() {
        downloadSaver.setSaveDelete(false);
        downloads.getReadWriteLock().writeLock().lock();
        List<Download> remove = new ArrayList<Download>();
        for(int i = 0; i < downloads.size(); i++) {
            Download j = downloads.get(i);
            if(j.getStatus() == DownloadStatus.COMPLETE) {
                remove.add(j);
            }
        }
        for(Download d : remove) {
            Util.remove(new File(d.getTempPath()));
        }
        downloads.removeAll(remove);
        downloads.getReadWriteLock().writeLock().unlock();
        
        DAOFactory.getInstance().getDownloadDAO().clearDownloads(remove);
        downloadSaver.setSaveDelete(true);
    }

    public void clearErrorDownloads() {
        downloadSaver.setSaveDelete(false);
        downloads.getReadWriteLock().writeLock().lock();
        List<Download> remove = new ArrayList<Download>();
        for(int i = 0; i < downloads.size(); i++) {
            Download j = downloads.get(i);
            if(j.getStatus() == DownloadStatus.ERROR) {
                remove.add(j);
            }
        }
        for(Download d : remove) {
            Util.remove(new File(d.getTempPath()));
        }
        downloads.removeAll(remove);
        downloads.getReadWriteLock().writeLock().unlock();
        DAOFactory.getInstance().getDownloadDAO().clearDownloads(remove);
        downloadSaver.setSaveDelete(true);
    }

    public DownloadDAO getDownloadDAO() {
        return DAOFactory.getInstance().getDownloadDAO();
    }

    /**
     * @return the downloads
     */
    public EventList<Download> getDownloads() {
        return downloads;
    }

    public void setDownloads(EventList<Download> downloads) {
        this.downloads = downloads;
    }

    /**
     * @return the tasks
     */
    public EventList<TaskData> getTasks() {
        return tasks;
    }

    public void setTasks(EventList<TaskData> tasks) {
        this.tasks = tasks;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        boolean oldValue = this.visible;
        this.visible = visible;
        propertySupport.firePropertyChange(PROP_VISIBLE, oldValue, visible);
    }

    /**
     * @return the randomChange
     */
    public boolean isRandomChange() {
        return randomChange;
    }

    /**
     * @param randomChange the randomChange to set
     */
    public void setRandomChange(boolean randomChange) {
        boolean oldValue = this.randomChange;
        this.randomChange = randomChange;
        propertySupport.firePropertyChange(PROP_RANDOM, oldValue, randomChange);
    }

    /**
     * @return the selectedDownload
     */
    public Download getSelectedDownload() {
        return selectedDownload;
    }

    /**
     * @param selectedDownload the selectedDownload to set
     */
    public void setSelectedDownload(Download selectedDownload) {
        Download oldValue = this.selectedDownload;
        this.selectedDownload = selectedDownload;
        propertySupport.firePropertyChange(PROP_SELDOWNLOAD, oldValue, selectedDownload);
    }

    /**
     * @return the selectedTask
     */
    public TaskData getSelectedTask() {
        return selectedTask;
    }

    /**
     * @param selectedTask the selectedTask to set
     */
    public void setSelectedTask(TaskData selectedTask) {
        TaskData oldValue = this.selectedTask;
        this.selectedTask = selectedTask;
        propertySupport.firePropertyChange(PROP_SELTASK, oldValue, selectedTask);
    }

    /**
     * @param downloadSaver the downloadSaver to set
     */
    public void setDownloadSaver(DownloadSaver downloadSaver) {
        this.downloadSaver = downloadSaver;
    }

    public void addPropertyChnageListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}
