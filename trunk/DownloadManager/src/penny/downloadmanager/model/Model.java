/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import penny.downloadmanager.model.db.Download;
import ca.odell.glazedlists.ObservableElementList;
import java.io.FileNotFoundException;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import penny.download.AbstractDownload;
import penny.downloadmanager.model.gui.StartupDialogModel;
import penny.recmd5.MD5MessageDigest;
import penny.recmd5.MD5State;

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

    public static void remove(File f) {
        if (f != null) {
            if (f.isFile()) {
                f.delete();
                remove(f.getParentFile());
            } else if (f.isDirectory()) {
                if (f.listFiles().length == 0) {
                    File tempFolder = new File(applicationSettings.getSavingModel().getTempFolder());
                    File saveFolder = new File(applicationSettings.getSavingModel().getSaveFolder());
                    if (!f.equals(tempFolder) && !f.equals(saveFolder)) {
                        f.delete();
                        remove(f.getParentFile());
                    }
                }
            }
        }
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

    public static boolean parseLinks(AbstractDownload d) {
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

    public static boolean parseWords(AbstractDownload d) {
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

    public static boolean save(AbstractDownload d) {
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

    public static MD5State getFileMD5(File file) {
        MD5MessageDigest md5er = new MD5MessageDigest();
        try {
            InputStream in = new FileInputStream(file);
            byte[] buffer = new byte[applicationSettings.getDownloadingModel().getDownloadSettings().getBufferSize()];
            int len = in.read(buffer);
            while (len != -1) {
                md5er.update(buffer, 0, len);
                len = in.read(buffer);
            }

            md5er.digest();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return md5er.getState();
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
                if (d.getStatus() != DownloadStatus.COMPLETE) {
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
                        Logger.getLogger(Model.class.getName()).fine("Checking file size for " + d.getUrl().toString());
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
                        Logger.getLogger(Model.class.getName()).fine("Checking MD5 for " + d.getUrl().toString());
                        if (file.exists()) {
                            d.getMD5().copy(getFileMD5(file));
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
