/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import ca.odell.glazedlists.ObservableElementList;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import penny.download.DownloadStatus;
import penny.downloadmanager.control.Application;
import penny.downloadmanager.model.db.DAOFactory;
import penny.downloadmanager.model.db.Download;
import penny.downloadmanager.model.db.DownloadDAO;
import penny.downloadmanager.model.db.JavaDBDataSource;
import penny.downloadmanager.model.gui.AddDialogModel;
import penny.downloadmanager.model.gui.AddTaskModel;
import penny.downloadmanager.model.gui.MainWindowModel;
import penny.downloadmanager.model.gui.SettingsDialogModel;
import penny.downloadmanager.model.gui.StartupDialogModel;
import penny.downloadmanager.model.task.DTaskData;
import penny.downloadmanager.model.task.TaskData;
import penny.downloadmanager.util.Util;

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
    private static ObservableElementList<Download> downloads;
    private static ObservableElementList<TaskData> tasks;
    private static DownloadSaver downloadSaver;
    private static ApplicationSettingsSaver settingsSaver;
    private static TaskSaver taskSaver;
    private static StartupDialogModel startupDialogModel;

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
    public static ObservableElementList<Download> getDownloads() {
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

    public static StartupDialogModel getStartupDialogModel() {
        return startupDialogModel;
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
        settingsDialogModel.getAppSettingsCopy().copy(applicationSettings);
        addDialogModel = new AddDialogModel();
        addTaskModel = new AddTaskModel();
        taskManagerModel = new TaskManagerModel();
        mainWindowModel = new MainWindowModel();
        downloads = (ObservableElementList<Download>) mainWindowModel.getDownloads();
        mainWindowModel.setTasks(taskManagerModel.getTasks());
        tasks = (ObservableElementList<TaskData>) taskManagerModel.getTasks();
        taskSaver = new TaskSaver(tasks);
        startupDialogModel = new StartupDialogModel();
        startupDialogModel.setStartupModel(applicationSettings.getStartupModel());
    }

    public static boolean typeMatches(String contentType, List<String> types) {
        if (contentType != null && !contentType.equals("")) {
            for (String s : types) {
                if (s.equals("*")) {
                    return true;
                } else if (s.contains(contentType)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean generateMD5(Download d) {
        boolean r = false;
        if (applicationSettings.getMd5ingModel().isGenerateMD5()) {
            if (applicationSettings.getMd5ingModel().isMd5Unknown()) {
                if (d.getContentType() == null || d.getContentType().equals("")) {
                    r = true;
                }
            }
            if (Model.typeMatches(d.getContentType(), applicationSettings.getMd5ingModel().getMd5Types())) {
                r = true;
            }
        }
        return r;
    }

    public static boolean parseLinks(Download d) {
        boolean r = false;
        if (applicationSettings.getParsingModel().isParseLinks()) {
            if (applicationSettings.getParsingModel().isParseUnknownLinks()) {
                if (d.getContentType() == null || (d.getContentType() != null && d.getContentType().equals(""))) {
                    r = true;
                }
            }
            if (Model.typeMatches(d.getContentType(), applicationSettings.getParsingModel().getParseLinksTypes())) {
                r = true;
            }
        }
        return r;
    }

    public static boolean parseWords(Download d) {
        boolean r = false;
        if (applicationSettings.getParsingModel().isParseWords()) {
            if (applicationSettings.getParsingModel().isParseUnknownWords()) {
                if (d.getContentType() == null || (d.getContentType() != null && d.getContentType().equals(""))) {
                    r = true;
                }
            }
            if (Model.typeMatches(d.getContentType(), applicationSettings.getParsingModel().getParseWordsTypes())) {
                r = true;
            }
        }
        return r;
    }

    public static boolean save(Download d) {
        boolean r = false;
        if (applicationSettings.getSavingModel().isSave()) {
            if (applicationSettings.getSavingModel().isSaveUnknown()) {
                if (d.getContentType() == null || d.getContentType().equals("")) {
                    r = true;
                }
            }
            if (Model.typeMatches(d.getContentType(), applicationSettings.getSavingModel().getSaveTypes())) {
                r = true;
            }
        }
        return r;
    }

    public static void loadData() {
        try {
            try {
                settingsSaver.load();
            } catch (Exception ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            }
            startupDialogModel.setStartupModel(applicationSettings.getStartupModel());
            JavaDBDataSource.getInstance().initDB();
            DownloadDAO dao = DAOFactory.getInstance().getDownloadDAO();
            List<Download> downloads1 = dao.getDownloads();
            downloads.addAll(downloads1);
            downloadSaver = new DownloadSaver(downloads);
            mainWindowModel.setDownloadSaver(downloadSaver);

            for (Download d : downloads1) {
                if (d.getStatus() != DownloadStatus.COMPLETE && d.getStatus() != DownloadStatus.QUEUED) {
                    d.queue();
                }
            }

            if (Model.getApplicationSettings().getStartupModel().isCheckSizes()) {
                for (Download d : downloads1) {
                    if (save(d)) {
                        File file = new File(d.getTempPath());
                        if (!file.exists()) {
                            file = new File(d.getSavePath());
                        }
                        Logger.getLogger(Model.class.getName()).fine("Checking file size for " + d.getUrl());
                        if (file.exists()) {
                            d.setDownloaded(file.length());
                        } else {
                            d.setDownloaded(0);
                        }
                    }
                }
            }

            if (Model.getApplicationSettings().getStartupModel().isCheckMD5s()) {
                for (Download d : downloads1) {
                    if (save(d)) {
                        File file = new File(d.getTempPath());
                        if (!file.exists()) {
                            file = new File(d.getSavePath());
                        }
                        Logger.getLogger(Model.class.getName()).fine("Checking MD5 for " + d.getUrl());
                        if (file.exists()) {
                            d.getMD5().copy(Util.getMD5State(file));
                        }
                    }
                }
            }

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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(),
                    "Exception",
                    JOptionPane.ERROR_MESSAGE);
            Application.setShutdown(true);
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
