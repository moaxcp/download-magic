/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.control;

import penny.downloadmanager.model.gui.AddTaskModel;
import penny.downloadmanager.model.gui.MainWindowModel;
import penny.downloadmanager.model.task.DTaskData;
import penny.downloadmanager.model.task.LinkToDownloadTaskData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author john
 */
public class AddTaskControl implements ActionListener {

    public static final String COM_OK = "OK";
    public static final String COM_CANCEL = "Cancel";

    private AddTaskModel addTaskModel;
    private MainWindowModel mainModel;

    public AddTaskControl(AddTaskModel addTaskModel, MainWindowModel mainModel) {
        this.addTaskModel = addTaskModel;
        this.mainModel = mainModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(COM_OK)) {
            if(addTaskModel.getSelectedTask().equals(AddTaskModel.TaskList.DOWNLOAD)) {
                mainModel.getTasks().add(new DTaskData(mainModel.getDownloads()));
            } else if(addTaskModel.getSelectedTask().equals(AddTaskModel.TaskList.LinkToDownload)) {
                mainModel.getTasks().add(new LinkToDownloadTaskData(mainModel.getDownloads()));
            }
            addTaskModel.setVisible(false);
        } else if(e.getActionCommand().equals(COM_CANCEL)) {
            addTaskModel.setVisible(false);
        }
    }
}
