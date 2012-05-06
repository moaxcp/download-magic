/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.view;

import penny.downloadmanager.view.settings.DownloadSettingsDialog;
import penny.downloadmanager.view.settings.EditFileFormat;
import penny.downloadmanager.view.add.AddTask;
import penny.downloadmanager.view.add.AddDialog;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.view.settings.StartupSettings;

/**
 *
 * @author john
 */
public class View {
    private static AddDialog addDialogView;
    private static AddTask addTask;
    private static DownloadSettingsDialog settingsDialogView;
    private static MainWindow mainWindowView;
    private static EditFileFormat editFileFormat;
    private static StartupSettings startupSettings;

    /**
     * @return the addDialogView
     */
    public static AddDialog getAddDialogView() {
        return addDialogView;
    }

    /**
     * @return the settingsDialogView
     */
    public static DownloadSettingsDialog getSettingsDialogView() {
        return settingsDialogView;
    }

    /**
     * @return the mainWindowView
     */
    public static MainWindow getMainWindowView() {
        return mainWindowView;
    }

    /**
     * @return the editFileFormat
     */
    public static EditFileFormat getEditFileFormat() {
        return editFileFormat;
    }

    public static AddTask getAddTask() {
        return addTask;
    }

    public static StartupSettings getStartupSettings() {
        return startupSettings;
    }

    public static void build() {
        settingsDialogView = new DownloadSettingsDialog(Model.getSettingsDialogModel());
        Model.getSettingsDialogModel().addPropertyChangeListener(settingsDialogView);
        addDialogView = new AddDialog(Model.getAddDialogModel());
        mainWindowView = new MainWindow(Model.getMainWindowModel(), Model.getTaskManagerModel());
        addTask = new AddTask();
        startupSettings = new StartupSettings(Model.getStartupDialogModel());
    }
}
