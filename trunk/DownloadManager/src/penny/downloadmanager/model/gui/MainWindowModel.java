/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model.gui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.UniqueList;
import penny.download.DownloadStatus;
import penny.downloadmanager.model.DownloadData;
import penny.downloadmanager.model.DownloadSaver;
import penny.downloadmanager.model.DownloadTableFormat;
import penny.downloadmanager.model.db.DAOFactory;
import penny.downloadmanager.model.db.DownloadDAO;
import penny.downloadmanager.model.task.TaskData;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.SwingPropertyChangeSupport;
import penny.downloadmanager.model.Model;

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
    private DownloadData selectedDownload;

    public static final String PROP_SELTASK = "selectedTask";
    private TaskData selectedTask;

    public static final String PROP_RUNNING = "running";
    private boolean running;

    private EventList<DownloadData> downloads;
    private DownloadTableFormat downloadFormat;
    private EventList<TaskData> tasks;
    private DownloadSaver downloadSaver;

    private SwingPropertyChangeSupport propertySupport;

    public MainWindowModel() {
        downloads = new ObservableElementList<DownloadData>(
                GlazedLists.threadSafeList(new UniqueList<DownloadData>(new BasicEventList<DownloadData>())),
                GlazedLists.beanConnector(DownloadData.class));
        DAOFactory.setDB(DAOFactory.JAVADB);

        this.tasks = new ObservableElementList<TaskData>(
                GlazedLists.threadSafeList(new BasicEventList<TaskData>()),
                GlazedLists.beanConnector(TaskData.class));
        visible = false;
        randomChange = false;
        running = false;

        propertySupport = new SwingPropertyChangeSupport(this, true);

        downloadFormat = new DownloadTableFormat();
    }

    public void clearDownloads() {
        downloadSaver.setSaveDelete(false);
        for(DownloadData d : downloads) {
            Model.remove(new File(d.getTempPath()));
        }
        downloads.clear();
        DAOFactory.getInstance().getDownloadDAO().clearDownloads();
        downloadSaver.setSaveDelete(true);
    }

    public void clearCompleteDownloads() {
        downloadSaver.setSaveDelete(false);
        downloads.getReadWriteLock().writeLock().lock();
        List<DownloadData> remove = new ArrayList<DownloadData>();
        for(int i = 0; i < downloads.size(); i++) {
            DownloadData j = downloads.get(i);
            if(j.getStatus() == DownloadStatus.COMPLETE) {
                remove.add(j);
            }
        }
        for(DownloadData d : remove) {
            Model.remove(new File(getSelectedDownload().getTempPath()));
        }
        downloads.removeAll(remove);
        downloads.getReadWriteLock().writeLock().unlock();
        
        DAOFactory.getInstance().getDownloadDAO().clearDownloads(DownloadStatus.COMPLETE);
        downloadSaver.setSaveDelete(true);
    }

    public void clearErrorDownloads() {
        downloadSaver.setSaveDelete(false);
        downloads.getReadWriteLock().writeLock().lock();
        List<DownloadData> remove = new ArrayList<DownloadData>();
        for(int i = 0; i < downloads.size(); i++) {
            DownloadData j = downloads.get(i);
            if(j.getStatus() == DownloadStatus.ERROR) {
                remove.add(j);
            }
        }
        for(DownloadData d : remove) {
            Model.remove(new File(getSelectedDownload().getTempPath()));
        }
        downloads.removeAll(remove);
        downloads.getReadWriteLock().writeLock().unlock();
        DAOFactory.getInstance().getDownloadDAO().clearDownloads(DownloadStatus.ERROR);
        downloadSaver.setSaveDelete(true);
    }

    public DownloadDAO getDownloadDAO() {
        return DAOFactory.getInstance().getDownloadDAO();
    }

    /**
     * @return the downloads
     */
    public EventList<DownloadData> getDownloads() {
        return downloads;
    }

    public void setDownloads(EventList<DownloadData> downloads) {
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
    public DownloadData getSelectedDownload() {
        return selectedDownload;
    }

    /**
     * @param selectedDownload the selectedDownload to set
     */
    public void setSelectedDownload(DownloadData selectedDownload) {
        DownloadData oldValue = this.selectedDownload;
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
     * @return the downloadFormat
     */
    public DownloadTableFormat getDownloadFormat() {
        return downloadFormat;
    }

    /**
     * @param downloadFormat the downloadFormat to set
     */
    public void setDownloadFormat(DownloadTableFormat downloadFormat) {
        this.downloadFormat = downloadFormat;
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
