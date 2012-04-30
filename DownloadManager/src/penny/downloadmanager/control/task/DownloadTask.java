/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.task;

import penny.download.DownloadProcessor;
import penny.download.DownloadSettings;
import penny.download.Downloader;
import penny.downloadmanager.control.di.ImageInfo;
import penny.downloadmanager.control.di.MD5Updater;
import penny.downloadmanager.control.di.Parser;
import penny.downloadmanager.control.di.TempFileSaver;
import penny.downloadmanager.model.ApplicationSettingsModel;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.task.Status;
import penny.downloadmanager.model.task.DTaskData;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
public class DownloadTask extends Task {

    private DTaskData data;
    private List<DownloadProcessor> processors;
    private ApplicationSettingsModel settings;

    public DownloadTask(DTaskData data, ApplicationSettingsModel settings) {
        this.data = data;

        processors = new ArrayList<DownloadProcessor>();
        this.settings = settings;
    }

    @Override
    public void run() {
        processors.clear();
        if(settings.getSavingModel().isSave()) {
            processors.add(new TempFileSaver());
        }
        if(settings.getImageModel().isWidthAndHeight()) {
            processors.add(new ImageInfo());
        }
        if(settings.getMd5ingModel().isGenerateMD5()) {
            processors.add(new MD5Updater());
        }
        if(settings.getParsingModel().isParseLinks() || settings.getParsingModel().isParseWords()) {
            processors.add(new Parser());
        }
        
        data.setStatus(Status.RUNNING);
        Downloader downloader = new Downloader(settings.getDownloadingModel().getDownloadSettings(), processors);
        data.setDownload(data.getNextDownload());
        while (data.getDownload() != null && data.getStatus() == Status.RUNNING) {
            downloader.setDownload(data.getDownload());
            downloader.download();
            data.addComplete(data.getDownload());
            data.setDownload(data.getNextDownload());
        }
        data.setDownload(null);
        if (data.getStatus() == Status.RUNNING) {
            data.setStatus(Status.FINISHED);
        }
    }
}
