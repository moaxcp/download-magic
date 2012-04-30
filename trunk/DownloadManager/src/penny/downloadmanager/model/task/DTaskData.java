/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model.task;

import ca.odell.glazedlists.EventList;
import penny.download.DownloadStatus;
import penny.downloadmanager.model.DownloadData;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
public class DTaskData extends TaskData {

    public static final String PROP_DOWNLOAD = "download";
    private DownloadData download;


    private EventList<DownloadData> downloads;
    
    public static final String PROP_COMPLETE = "complete";
    private List<DownloadData> complete;

    public DTaskData(EventList<DownloadData> downloads) {
        this.downloads = downloads;
        this.name = "Download Task";
        this.status = Status.QUEUED;
        complete = new ArrayList<DownloadData>();
        for(DownloadData d : downloads) {
            if(d.getStatus() != DownloadStatus.QUEUED) {
                addComplete(d);
            }
        }
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        if(download != null && status == Status.STOPPED) {
            downloads.getReadWriteLock().writeLock().lock();
            download.stop();
            downloads.getReadWriteLock().writeLock().unlock();
        }
        Status oldValue = getStatus();
        this.status = status;
        propertySupport.firePropertyChange(PROP_STATUS, oldValue, status);
    }

    public DownloadData getNextDownload() {
        downloads.getReadWriteLock().writeLock().lock();
        for (DownloadData i : downloads) {
            if (i.getStatus().equals(DownloadStatus.QUEUED)) {
                downloads.getReadWriteLock().writeLock().unlock();
                return i;
            }
        }
        downloads.getReadWriteLock().writeLock().unlock();
        return null;
    }

    /**
     * @return the download
     */
    public DownloadData getDownload() {
        return download;
    }

    /**
     * @param download the download to set
     */
    public void setDownload(DownloadData download) {
        DownloadData oldValue = this.download;
        this.download = download;
        propertySupport.firePropertyChange(PROP_DOWNLOAD, oldValue, download);
    }
    
    public int getProgress(DownloadData download) {
        return download.getSize() > 0 ? ((int) (download.getDownloaded() / (float) download.getSize() * 10000)) : 0;
    }

    /**
     * @return the downloads
     */
    public EventList<DownloadData> getDownloads() {
        return downloads;
    }

    public void addComplete(DownloadData data) {
        int oldValue = complete.size();
        complete.add(data);
        propertySupport.firePropertyChange(PROP_COMPLETE, oldValue, complete.size());
    }

    public int getComplete() {
        return complete.size();
    }
}
