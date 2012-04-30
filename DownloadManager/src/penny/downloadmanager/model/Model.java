/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import ca.odell.glazedlists.ObservableElementList;
import penny.download.DownloadStatus;
import penny.downloadmanager.control.Application;
import penny.downloadmanager.model.db.DAOFactory;
import penny.downloadmanager.model.db.DownloadDAO;
import penny.downloadmanager.model.db.JavaDBDataSource;
import penny.downloadmanager.model.gui.AddDialogModel;
import penny.downloadmanager.model.gui.AddTaskModel;
import penny.downloadmanager.model.gui.SettingsDialogModel;
import penny.downloadmanager.model.gui.MainWindowModel;
import penny.downloadmanager.model.task.DTaskData;
import penny.downloadmanager.model.task.TaskData;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author john
 */
public class Model {

    private static AddDialogModel addDialogModel;
    private static AddTaskModel addTaskModel;
    private static SettingsDialogModel settingsDialogModel;
    private static MainWindowModel mainWindowModel;
    private static TaskManagerModel taskManagerModel;
    private static ApplicationSettingsModel applicationSettings;
    private static ObservableElementList<DownloadData> downloads;
    private static ObservableElementList<TaskData> tasks;
    private static DownloadSaver downloadSaver;
    private static ApplicationSettingsSaver settingsSaver;
    private static TaskSaver taskSaver;

    /**
     * @return the addDialogModel
     */
    public static AddDialogModel getAddDialogModel() {
        return addDialogModel;
    }

    public static AddTaskModel getAddTaskModel() {
        return addTaskModel;
    }

    /**
     * @return the settingsDialogModel
     */
    public static SettingsDialogModel getSettingsDialogModel() {
        return settingsDialogModel;
    }

    /**
     * @return the mainWindowModel
     */
    public static MainWindowModel getMainWindowModel() {
        return mainWindowModel;
    }

    /**
     * @return the applicationSettings
     */
    public static ApplicationSettingsModel getApplicationSettings() {
        return applicationSettings;
    }

    /**
     * @return the downloads
     */
    public static ObservableElementList<DownloadData> getDownloads() {
        return downloads;
    }

    /**
     * @return the tasks
     */
    public static ObservableElementList<TaskData> getTasks() {
        return tasks;
    }

    /**
     * @return the downloadSaver
     */
    public static DownloadSaver getDownloadSaver() {
        return downloadSaver;
    }

    /**
     * @return the taskManagerModel
     */
    public static TaskManagerModel getTaskManagerModel() {
        return taskManagerModel;
    }

    /**
     * @return the settingsSaver
     */
    public static ApplicationSettingsSaver getSettingsSaver() {
        return settingsSaver;
    }

    public static List<String> getDownloadProperties() {
        List<String> list = new ArrayList<String>();
        for (String s : mainWindowModel.getDownloadFormat().getColumns().keySet()) {
            list.add(s);
        }
        return list;
    }

    public static TaskSaver getTaskSaver() {
        return taskSaver;
    }

    public static void build() {
        settingsDialogModel = new SettingsDialogModel();
        applicationSettings = settingsDialogModel.getAppSettings();
        settingsSaver = new ApplicationSettingsSaver("data/settings.dat");
        settingsSaver.setApplicationSettings(applicationSettings);
        settingsSaver.load();
        settingsDialogModel.getAppSettingsCopy().copy(applicationSettings);
        addDialogModel = new AddDialogModel();
        addTaskModel = new AddTaskModel();
        taskManagerModel = new TaskManagerModel();
        mainWindowModel = new MainWindowModel();
        downloads = (ObservableElementList<DownloadData>) mainWindowModel.getDownloads();
        mainWindowModel.setTasks(taskManagerModel.getTasks());
        tasks = (ObservableElementList<TaskData>) taskManagerModel.getTasks();
        mainWindowModel.setTasks(taskManagerModel.getTasks());
        taskSaver = new TaskSaver(tasks);
    }

    public static void loadData() {
        try {
            JavaDBDataSource.getInstance().initDB();
            DownloadDAO dao = DAOFactory.getInstance().getDownloadDAO();
            List<DownloadData> downloads1 = dao.getDownloads();
            for (DownloadData d : downloads1) {
                File tempFile = new File(d.getTempPath());
                File saveFile = new File(d.getSavePath());

                if (d.getStatus() != DownloadStatus.COMPLETE) {
                    d.queue();
                }
            }
            downloads.addAll(downloads1);
            downloadSaver = new DownloadSaver(downloads);
            mainWindowModel.setDownloadSaver(downloadSaver);
            mainWindowModel.getTasks().add(new DTaskData(mainWindowModel.getDownloads()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(),
                    "SQLException",
                    JOptionPane.ERROR_MESSAGE);
            Application.setShutdown(true);
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(),
                    "NullPointerException",
                    JOptionPane.ERROR_MESSAGE);
            Application.setShutdown(true);
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        //TODO on exception display message and signal shutdown.
    }
}
