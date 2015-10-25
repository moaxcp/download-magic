/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.control.task;
import com.github.moaxcp.downloadmanager.model.Model;
import com.github.moaxcp.downloadmanager.model.task.DTaskData;
import com.github.moaxcp.downloadmanager.model.task.TaskData;

/**
 *
 * @author john
 */
public abstract class Task implements Runnable {

    public static Task getTask(TaskData data) {
        if(data instanceof DTaskData) {
            return new DownloadTask((DTaskData) data, Model.getApplicationSettings());
        }

        return null;
    }
}
