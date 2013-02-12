/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model.task;

import ca.odell.glazedlists.EventList;
import penny.download.DownloadStatus;
import penny.downloadmanager.model.db.Download;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import penny.downloadmanager.model.Model;

/**
 *
 * @author john
 */
public class DTaskData extends TaskData {

    public static final String PROP_DOWNLOAD = "download";
    private transient Download download;
    
    public static final String PROP_COMPLETE = "complete";
    private transient int complete;

    public DTaskData() {
        this.name = "Download Task";
        this.status = Status.QUEUED;
        complete = 0;
        for(Download d : Model.getDownloads()) {
            if(d.getStatus() != DownloadStatus.QUEUED) {
                complete++;
            }
        }
    }
    
    public void init() {
        this.status = Status.QUEUED;
        int oldValue = complete;
        complete = 0;
        for(Download d : Model.getDownloads()) {
            if(d.getStatus() != DownloadStatus.QUEUED) {
                complete++;
            }
        }
        propertySupport.firePropertyChange(PROP_COMPLETE, oldValue, complete);
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        if(download != null && status == Status.STOPPED) {
            Model.getDownloads().getReadWriteLock().writeLock().lock();
            download.stop();
            Model.getDownloads().getReadWriteLock().writeLock().unlock();
        }
        Status oldValue = getStatus();
        this.status = status;
        propertySupport.firePropertyChange(PROP_STATUS, oldValue, status);
    }

    public Download getNextDownload() {
        Model.getDownloads().getReadWriteLock().writeLock().lock();
        for (Download i : Model.getDownloads()) {
            if (i.getStatus().equals(DownloadStatus.QUEUED)) {
                Model.getDownloads().getReadWriteLock().writeLock().unlock();
                return i;
            }
        }
        Model.getDownloads().getReadWriteLock().writeLock().unlock();
        return null;
    }

    /**
     * @return the download
     */
    public Download getDownload() {
        return download;
    }

    /**
     * @param download the download to set
     */
    public void setDownload(Download download) {
        Download oldValue = this.download;
        this.download = download;
        propertySupport.firePropertyChange(PROP_DOWNLOAD, oldValue, download);
    }
    
    public int getProgress(Download download) {
        return download.getSize() > 0 ? ((int) (download.getDownloaded() / (float) download.getSize() * 10000)) : 0;
    }

    public void addComplete(Download data) {
        int oldValue = complete;
        complete++;
        propertySupport.firePropertyChange(PROP_COMPLETE, oldValue, complete);
    }

    public int getComplete() {
        return complete;
    }
}
