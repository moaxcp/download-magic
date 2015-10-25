/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.control;

import com.github.moaxcp.downloadmanager.model.gui.AddTaskModel;
import com.github.moaxcp.downloadmanager.model.gui.MainWindowModel;
import com.github.moaxcp.downloadmanager.model.task.DTaskData;
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
                mainModel.getTasks().add(new DTaskData());
            }
            addTaskModel.setVisible(false);
        } else if(e.getActionCommand().equals(COM_CANCEL)) {
            addTaskModel.setVisible(false);
        }
    }
}
