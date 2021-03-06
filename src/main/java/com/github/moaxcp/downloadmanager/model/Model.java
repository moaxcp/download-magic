/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.model;

import ca.odell.glazedlists.ObservableElementList;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import penny.download.DownloadStatus;
import com.github.moaxcp.downloadmanager.control.Application;
import com.github.moaxcp.downloadmanager.model.db.DAOFactory;
import com.github.moaxcp.downloadmanager.model.db.Download;
import com.github.moaxcp.downloadmanager.model.db.DownloadDAO;
import com.github.moaxcp.downloadmanager.model.db.JavaDBDataSource;
import com.github.moaxcp.downloadmanager.model.gui.AddDialogModel;
import com.github.moaxcp.downloadmanager.model.gui.AddTaskModel;
import com.github.moaxcp.downloadmanager.model.gui.MainWindowModel;
import com.github.moaxcp.downloadmanager.model.gui.SettingsDialogModel;
import com.github.moaxcp.downloadmanager.model.gui.SplashScreenModel;
import com.github.moaxcp.downloadmanager.model.gui.StartupDialogModel;
import com.github.moaxcp.downloadmanager.model.task.DTaskData;
import com.github.moaxcp.downloadmanager.model.task.TaskData;
import com.github.moaxcp.downloadmanager.util.Util;
import com.github.moaxcp.downloadmanager.view.View;

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
    private static StartupDialogModel startupDialogModel;
    private static SplashScreenModel splashScreenModel;

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

    public static SplashScreenModel getSplashScreenModel() {
        return splashScreenModel;
    }

    public static List<String> getAllDownloadProperties() {
        List<String> list = new ArrayList<String>();
        list.addAll(DAOFactory.getInstance().getPropertyDAO().getPropertyNames());
        for (String s : Download.propertyNames) {
            list.add(s);
        }
        return list;
    }

    public static void build() {
        settingsDialogModel = new SettingsDialogModel();
        applicationSettings = settingsDialogModel.getAppSettings();
        settingsSaver = new ApplicationSettingsSaver("data/settings.dat");
        settingsSaver.setApplicationSettings(applicationSettings);
        settingsDialogModel.getAppSettingsCopy().copy(applicationSettings);
        mainWindowModel = new MainWindowModel();
        addDialogModel = new AddDialogModel();
        addTaskModel = new AddTaskModel();
        taskManagerModel = new TaskManagerModel();
        downloads = (ObservableElementList<Download>) mainWindowModel.getDownloads();
        mainWindowModel.setTasks(taskManagerModel.getTasks());
        tasks = (ObservableElementList<TaskData>) taskManagerModel.getTasks();
        startupDialogModel = new StartupDialogModel();
        startupDialogModel.setStartupModel(applicationSettings.getStartupModel());
        splashScreenModel = new SplashScreenModel();
    }

    public static boolean typeMatches(String contentType, List<String> types) {
        if (contentType != null && !contentType.equals("")) {
            for (String s : types) {
                if (s.equals("*")) {
                    return true;
                } else if (contentType.contains(s)) {
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
                if (d.getContentType() == null || d.getContentType().equals("")) {
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
                if (d.getContentType() == null || d.getContentType().equals("")) {
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
        splashScreenModel.setVisible(true);
        try {
            try {
                splashScreenModel.setStage("Loading settings...");
                settingsSaver.load();
                
            } catch (Exception ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            }
            startupDialogModel.setStartupModel(applicationSettings.getStartupModel());
            splashScreenModel.setStage("Initializing DB...");
            JavaDBDataSource.getInstance().initDB();
            DownloadDAO dao = DAOFactory.getInstance().getDownloadDAO();
            splashScreenModel.setStage("Loading Downloads...");
            List<Download> downloads1 = new ArrayList<Download>();
            List<UUID> ids = dao.getIds();
            splashScreenModel.setCurrent(0);
            splashScreenModel.setSize(ids.size());
            for(UUID id : ids) {
                splashScreenModel.setMessage("Loading " + id);
                downloads1.add(dao.getDownload(id));
                splashScreenModel.setCurrent(splashScreenModel.getCurrent() + 1);
            }
            downloads.addAll(downloads1);
            downloadSaver = new DownloadSaver(downloads);
            mainWindowModel.setDownloadSaver(downloadSaver);

            for (Download d : downloads1) {
                if (d.getStatus() != DownloadStatus.COMPLETE && d.getStatus() != DownloadStatus.QUEUED) {
                    d.queue();
                }
            }

            if (Model.getApplicationSettings().getStartupModel().isCheckSizes()) {
                splashScreenModel.setStage("Checking file sizes...");
                splashScreenModel.setCurrent(0);
                splashScreenModel.setSize(downloads1.size());
                for (Download d : downloads1) {
                    splashScreenModel.setMessage("Checking for " + d.getId());
                    if (save(d)) {
                        Logger.getLogger(Model.class.getName()).fine("Checking file size for " + d.getUrl());
                        File file = new File(d.getTempPath());
                        if (file.exists()) {
                            d.setDownloaded(file.length());
                        } else {
                            file = new File(Util.getTempFile(d));
                            if (file.exists()) {
                                d.setTempPath(Util.getTempFile(d));
                                d.setDownloaded(file.length());
                            } else {
                                file = new File(d.getSavePath());
                                if (file.exists()) {
                                    d.setDownloaded(file.length());
                                } else {
                                    file = new File(Util.getSaveFile(d));
                                    if (file.exists()) {
                                        d.setSavePath(Util.getSaveFile(d));
                                        d.setDownloaded(file.length());
                                    } else {
                                        d.setDownloaded(0);
                                    }
                                }
                            }
                        }
                    }
                    splashScreenModel.setCurrent(splashScreenModel.getCurrent() + 1);
                }
            }

            if (Model.getApplicationSettings().getStartupModel().isCheckMD5s()) {
                splashScreenModel.setStage("Checking file MD5s...");
                splashScreenModel.setCurrent(0);
                splashScreenModel.setSize(downloads1.size());
                for (Download d : downloads1) {
                    splashScreenModel.setMessage("Checking for " + d.getId());
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
                    splashScreenModel.setCurrent(splashScreenModel.getCurrent() + 1);
                }
            }
            if (Model.getTasks().size() == 0) {
                Model.getTasks().add(new DTaskData());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(View.getSplashScreen(), ex.toString(),
                    "SQLException",
                    JOptionPane.ERROR_MESSAGE);
            Application.setShutdown(true);
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(View.getSplashScreen(), ex.toString(),
                    "NullPointerException",
                    JOptionPane.ERROR_MESSAGE);
            Application.setShutdown(true);
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(View.getSplashScreen(), ex.toString(),
                    "Exception",
                    JOptionPane.ERROR_MESSAGE);
            Application.setShutdown(true);
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            splashScreenModel.setVisible(false);
        }
    }
}
