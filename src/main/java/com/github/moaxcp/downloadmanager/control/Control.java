/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.control;

import com.github.moaxcp.downloadmanager.control.task.TaskManager;
import com.github.moaxcp.downloadmanager.model.Model;
import com.github.moaxcp.downloadmanager.view.View;

/**
 *
 * @author john
 */
public class Control {
    private static AddDialogControl addDialogControl;
    private static AddTaskControl addTaskControl;
    private static ApplicationSettingsControl settingsControl;
    private static MainWindowControl mainWindowControl;
    private static TaskManager taskManager;
    private static StartupSettingsControl startupControl;

    /**
     * @return the addDialogControl
     */
    public static AddDialogControl getAddDialogControl() {
        return addDialogControl;
    }

    /**
     * @return the addTaskControl
     */
    public static AddTaskControl getAddTaskControl() {
        return addTaskControl;
    }

    /**
     * @return the settingsControl
     */
    public static ApplicationSettingsControl getSettingsControl() {
        return settingsControl;
    }

    /**
     * @return the mainWindowControl
     */
    public static MainWindowControl getMainWindowControl() {
        return mainWindowControl;
    }

    /**
     * @return the taskManager
     */
    public static TaskManager getTaskManager() {
        return taskManager;
    }

    public static void build() {

        settingsControl = new ApplicationSettingsControl(Model.getSettingsDialogModel());
        View.getSettingsDialogView().registerController(settingsControl);
        View.getSettingsDialogView().addWindowListener(settingsControl);
        taskManager = new TaskManager(Model.getTaskManagerModel());
        mainWindowControl = new MainWindowControl(Model.getMainWindowModel(), Model.getAddDialogModel(), Model.getAddTaskModel(), Model.getSettingsDialogModel(), taskManager);
        addDialogControl = new AddDialogControl(Model.getAddDialogModel(), getMainWindowControl());
        View.getAddDialogView().registerController(addDialogControl);
        View.getAddDialogView().addWindowListener(addDialogControl);
        View.getMainWindowView().registerController(mainWindowControl);
        addTaskControl = new AddTaskControl(Model.getAddTaskModel(), Model.getMainWindowModel());
        startupControl = new StartupSettingsControl(Model.getStartupDialogModel());
        View.getStartupSettings().registerController(startupControl);
    }
}
