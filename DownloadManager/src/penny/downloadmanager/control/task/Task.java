/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.control.task;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.task.DTaskData;
import penny.downloadmanager.model.task.LinkToDownloadTaskData;
import penny.downloadmanager.model.task.Status;
import penny.downloadmanager.model.task.TaskData;

/**
 *
 * @author john
 */
public abstract class Task implements Runnable {

    public static Task getTask(TaskData data) {
        if(data instanceof DTaskData) {
            return new DownloadTask((DTaskData) data, Model.getApplicationSettings());
        } else if(data instanceof LinkToDownloadTaskData) {
            return new LinkToDownloadTask((LinkToDownloadTaskData) data);
        }

        return null;
    }
}
