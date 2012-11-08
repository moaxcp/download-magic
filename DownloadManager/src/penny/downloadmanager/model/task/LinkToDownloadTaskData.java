/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.task;

import penny.downloadmanager.model.db.Download;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class LinkToDownloadTaskData extends TaskData {

    public static final String PROP_SIZE = "size";
    private int size;
    public static final String PROP_PROGRESS = "progress";
    private int progress;
    private LinkedList<String> links;
    private List<Download> downloads;

    public LinkToDownloadTaskData(List<Download> downloads) {
        name = "Link To Download Task";
        this.status = Status.QUEUED;
        links = new LinkedList<String>();
        this.downloads = downloads;
    }

    public int getSize() {
        return size;
    }

    public int getProgress() {
        return progress;
    }

    public List<Download> getDownloads() {
        return downloads;
    }

    public void addLink(String link) {
        int oldValue = links.size();
        links.add(link);
        propertySupport.firePropertyChange(PROP_SIZE, oldValue, links.size());
    }

    public boolean moveOneLink() {
        try {
            Download download = new Download(UUID.randomUUID());
            download.setUrl(new URL(links.pop()));
            downloads.add(download);
            int oldValue = progress;
            progress++;
            propertySupport.firePropertyChange(PROP_PROGRESS, oldValue, progress);
        } catch (MalformedURLException ex) {
            size--;
            Logger.getLogger(LinkToDownloadTaskData.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (progress < size) {
            return true;
        } else {
            return false;
        }
    }
}