/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.control.task;

import penny.download.Downloader;
import com.github.moaxcp.downloadmanager.control.processor.Processor;
import com.github.moaxcp.downloadmanager.model.ApplicationSettingsModel;
import com.github.moaxcp.downloadmanager.model.task.DTaskData;
import com.github.moaxcp.downloadmanager.model.task.Status;

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
        data.init();
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
