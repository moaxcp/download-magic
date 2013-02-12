/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.task;

import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.db.Download;
import penny.downloadmanager.model.task.LinkToDownloadTaskData;
import penny.downloadmanager.model.task.Status;

/**
 *
 * @author john
 */
public class LinkToDownloadTask extends Task {

    private LinkToDownloadTaskData data;

    public LinkToDownloadTask(LinkToDownloadTaskData data) {
        this.data = data;
    }

    @Override
    public void run() {
        data.setStatus(Status.RUNNING);
        for (Download d : Model.getDownloads()) {
            if (data.getStatus() == Status.STOPPED) {
                return;
            }
            for (String link : d.getHrefLinks()) {
                if (data.getStatus() == Status.STOPPED) {
                    return;
                }
                data.addLink(link);
            }
            for (String link : d.getSrcLinks()) {
                if (data.getStatus() == Status.STOPPED) {
                    return;
                }
                data.addLink(link);
            }
        }
        while (data.moveOneLink() && data.getStatus() == Status.RUNNING);
        if(data.getStatus() == Status.RUNNING) {
            data.setStatus(Status.FINISHED);
        }
    }
}
