/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.task;

import penny.download.Downloader;
import penny.downloadmanager.control.processor.Processor;
import penny.downloadmanager.model.ApplicationSettingsModel;
import penny.downloadmanager.model.task.DTaskData;
import penny.downloadmanager.model.task.Status;

/**
 *
 * @author john
 */
public class DownloadTask extends Task {

    private DTaskData data;
    private Processor processor;
    private ApplicationSettingsModel settings;

    public DownloadTask(DTaskData data, ApplicationSettingsModel settings) {
        this.data = data;

        processor = new Processor();
        this.settings = settings;
    }

    @Override
    public void run() {
        
        data.setStatus(Status.RUNNING);
        Downloader downloader = new Downloader(settings.getDownloadingModel().getDownloadSettings());
        downloader.setProcessor(processor);
        data.setDownload(data.getNextDownload());
        while (data.getDownload() != null && data.getStatus() == Status.RUNNING) {
            downloader.setDownload(data.getDownload());
            downloader.download();
            data.addComplete(data.getDownload());
            data.setDownload(data.getNextDownload());
        }
        downloader.shutdown();
        data.setDownload(null);
        if (data.getStatus() == Status.RUNNING) {
            data.setStatus(Status.FINISHED);
        }
    }
}
