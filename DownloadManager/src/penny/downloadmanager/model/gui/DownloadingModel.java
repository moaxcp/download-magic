/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.gui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import penny.download.DownloadSettings;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 *
 * @author john
 */
public class DownloadingModel implements Serializable {

    private DownloadSettings downloadSettings;

    public static final String PROP_DOWNLOADUNKNOWN = "downloadUnknown";
    private boolean downloadUnknown;

    public static final String PROP_DOWNLOADTYPES = "downloadTypes";
    private EventList<String> downloadTypes;


    public DownloadingModel() {
        downloadSettings = new DownloadSettings();
        downloadUnknown = true;
        downloadTypes = new BasicEventList<String>();
        downloadTypes.add("*");
    }

    public DownloadingModel(DownloadingModel appSettings) {
        this.copy(appSettings);
    }

    public void copy(DownloadingModel downloadingModel) {
        this.downloadSettings.copy(downloadingModel.getDownloadSettings());
        this.setDownloadUnknown(downloadingModel.isDownloadUnknown());
        this.downloadTypes.clear();
        this.downloadTypes.addAll(downloadingModel.getDownloadTypes());
    }

    /**
     * @return the tempNameFormat
     */
    public DownloadSettings getDownloadSettings() {
        return downloadSettings;
    }

    /**
     * @param tempNameFormat the tempNameFormat to set
     */
    public void setDownloadSettings(DownloadSettings downloadSettings) {
        this.downloadSettings = downloadSettings;
    }

    public long getMaxRetryTime() {
        return downloadSettings.getMaxRetryTime()/1000000000;
    }

    public void setMaxRetryTime(long maxRetryTime) {
        downloadSettings.setMaxRetryTime(maxRetryTime);
    }
    
    public EventList<String> getDownloadTypes() {
        return downloadTypes;
    }

    /**
     * @return the downloadUnknown
     */
    public boolean isDownloadUnknown() {
        return downloadUnknown;
    }

    /**
     * @param downloadUnknown the downloadUnknown to set
     */
    public void setDownloadUnknown(boolean downloadUnknown) {
        this.downloadUnknown = downloadUnknown;
    }
}
