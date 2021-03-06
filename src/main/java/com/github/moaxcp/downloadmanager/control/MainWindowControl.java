package com.github.moaxcp.downloadmanager.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;
import java.util.*;
import javax.swing.JTable;
import javax.swing.UIManager;
import penny.download.DownloadStatus;
import com.github.moaxcp.downloadmanager.control.task.TaskManager;
import com.github.moaxcp.downloadmanager.model.LookAndFeelModel;
import com.github.moaxcp.downloadmanager.model.Model;
import com.github.moaxcp.downloadmanager.model.db.Download;
import com.github.moaxcp.downloadmanager.model.gui.AddDialogModel;
import com.github.moaxcp.downloadmanager.model.gui.AddTaskModel;
import com.github.moaxcp.downloadmanager.model.gui.MainWindowModel;
import com.github.moaxcp.downloadmanager.model.gui.SettingsDialogModel;
import com.github.moaxcp.downloadmanager.model.task.Status;
import com.github.moaxcp.downloadmanager.model.task.TaskData;
import com.github.moaxcp.downloadmanager.util.RandomChanges;
import com.github.moaxcp.downloadmanager.util.Util;
import com.github.moaxcp.downloadmanager.view.DownloadDataView;
import com.github.moaxcp.downloadmanager.view.View;
import com.github.moaxcp.downloadmanager.view.test.ConnectionTest;

/**
 *
 * @author john
 */
public class MainWindowControl implements ActionListener, WindowListener, MouseListener {

    public static final String COM_EXIT = "exit";
    public static final String COM_PREFERENCES = "preferences";
    public static final String COM_SYSTEM = "system";
    public static final String COM_METAL = "metal";
    public static final String COM_PROPERTIES = "properties";
    public static final String COM_ANALYZER = "analyzer";
    public static final String COM_ADDDOWNLOAD = "addDownload";
    public static final String COM_REMOVEDOWNLOAD = "removeDownload";
    public static final String COM_QUEUEDOWNLOAD = "queueDownload";
    public static final String COM_ABOUT = "about";
    public static final String COM_RANDOM = "randomChange";
    public static final String COM_START = "start";
    public static final String COM_STOP = "stop";
    public static final String COM_ADDTASK = "addTask";
    public static final String COM_REMOVETASK = "removeTask";
    public static final String COM_SETTINGS = "settings";
    public static final String COM_CLEARALLDOWNLOADS = "clearAllDownloads";
    public static final String COM_CLEARCOMPLETEDOWNLOADS = "clearCompleteDownloads";
    public static final String COM_CLEARERRORDOWNLOADS = "clearErrorDownloads";
    public static final String COM_APPLICATIONSTARTUP = "applicationStartup";
    public static final String COM_SORT = "sort";
    
    private MainWindowModel mainModel;
    private SettingsDialogModel settingsModel;
    private AddDialogModel addModel;
    private AddTaskModel addTaskModel;
    private TaskManager taskManager;
    private RandomChanges randomChanges;
    private JTable downloadTable;
    private List<DownloadDataView> downloadViews;

    public MainWindowControl(MainWindowModel mainModel, AddDialogModel addModel, AddTaskModel addTaskModel, SettingsDialogModel settingsModel, TaskManager taskManager) {
        this.settingsModel = settingsModel;
        this.mainModel = mainModel;
        this.addModel = addModel;
        this.addTaskModel = addTaskModel;

        this.taskManager = taskManager;
        randomChanges = new RandomChanges(mainModel.getDownloads());
        downloadViews = new ArrayList<DownloadDataView>();
    }

    public void setDownloadTable(JTable downloadTable) {
        this.downloadTable = downloadTable;
    }

    public void add(URL url) {
        Download d = new Download(UUID.randomUUID());
        d.setUrl(url);
        mainModel.getDownloads().add(d);
    }

    public void add(List<URL> urls) {
        for (URL u : urls) {
            add(u);
        }
    }

    public void openDownloadView(Download download) {
        DownloadDataView view = null;

        for (DownloadDataView v : downloadViews) {
            if (v.getDownload().equals(mainModel.getSelectedDownload())) {
                view = v;
                break;
            }
        }

        if (view == null) {
            view = new DownloadDataView(mainModel.getSelectedDownload());
            downloadViews.add(view);
        }

        view.setVisible(true);
        view.addWindowListener(this);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals(COM_RANDOM)) {
            randomChange();
        } else if (e.getActionCommand().equals(COM_ANALYZER)) {
            analyzer();
        } else if (e.getActionCommand().equals(COM_ADDDOWNLOAD)) {
            addModel.setVisible(true);
        } else if (e.getActionCommand().equals(COM_START)) {
            startTasks();
        } else if (e.getActionCommand().equals(COM_STOP)) {
            stopTasks();
        } else if (e.getActionCommand().equals(COM_SETTINGS)) {
            settingsModel.setVisible(true);
        } else if (e.getActionCommand().equals(COM_PROPERTIES) && mainModel.getSelectedDownload() != null) {

            openDownloadView(mainModel.getSelectedDownload());

        } else if (e.getActionCommand().equals(COM_REMOVEDOWNLOAD) && mainModel.getSelectedDownload() != null) {
            Util.remove(new File(mainModel.getSelectedDownload().getTempPath()));
            mainModel.getDownloads().remove(mainModel.getSelectedDownload());
        } else if (e.getActionCommand().equals(COM_QUEUEDOWNLOAD) && mainModel.getSelectedDownload() != null) {
            mainModel.getSelectedDownload().queue();
        } else if (e.getActionCommand().equals(COM_EXIT)) {
            exit();
        } else if (e.getActionCommand().equals(COM_ADDTASK)) {
            addTaskModel.setVisible(true);
        } else if (e.getActionCommand().equals(COM_REMOVETASK) && mainModel.getSelectedTask() != null) {
            mainModel.getTasks().remove(mainModel.getSelectedTask());
        } else if (e.getActionCommand().equals(COM_ABOUT)) {
            System.out.println(e.getActionCommand());
        } else if (e.getActionCommand().equals(COM_CLEARALLDOWNLOADS)) {
            mainModel.clearDownloads();
        } else if (e.getActionCommand().equals(COM_CLEARCOMPLETEDOWNLOADS)) {
            mainModel.clearCompleteDownloads();
        } else if (e.getActionCommand().equals(COM_CLEARERRORDOWNLOADS)) {
            mainModel.clearErrorDownloads();
        } else if (e.getActionCommand().equals(COM_APPLICATIONSTARTUP)) {
            Model.getStartupDialogModel().setVisible(true);
        } else if(e.getActionCommand().equals(COM_SORT)) {
            Model.getApplicationSettings().setSortState(View.getMainWindowView().getSorter().toString());
            Model.getSettingsSaver().save();
        } else {
            LookAndFeelModel lookModel = Model.getApplicationSettings().getLookModel();
            if (lookModel.getLookAndFeels().containsKey(e.getActionCommand())) {
                lookModel.setLookAndFeel(e.getActionCommand());
                View.initLookAndFeel();
                Model.getSettingsSaver().save();
            }
        }
    }

    private void analyzer() {
        ConnectionTest analyzer = new ConnectionTest();
        analyzer.setVisible(true);
    }

    private void randomChange() {
        if (mainModel.isRandomChange()) {
            randomChanges.on();
            Thread t = Application.getThread(randomChanges);
            t.start();
        } else {
            randomChanges.stop();
        }
    }

    private void startTasks() {
        mainModel.getDownloads().getReadWriteLock().writeLock().lock();
        for (Download i : mainModel.getDownloads()) {
            if (!(i.getStatus().equals(DownloadStatus.COMPLETE) || i.getStatus().equals(DownloadStatus.QUEUED))) {
                i.queue();
            }
        }
        mainModel.getDownloads().getReadWriteLock().writeLock().unlock();
        mainModel.getTasks().getReadWriteLock().writeLock().lock();
        for (TaskData t : mainModel.getTasks()) {
            t.setStatus(Status.QUEUED);
        }
        mainModel.getTasks().getReadWriteLock().writeLock().unlock();
        taskManager.start();
        taskManager.getModel().setRunning(true);
    }

    private void stopTasks() {
        mainModel.getTasks().getReadWriteLock().writeLock().lock();
        for (TaskData t : mainModel.getTasks()) {
            t.setStatus(Status.STOPPED);
        }
        mainModel.getTasks().getReadWriteLock().writeLock().unlock();
    }

    private void exit() {
        System.exit(0);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (e.getWindow().equals(View.getMainWindowView())) {
            exit();
        } else if (e.getWindow() instanceof DownloadDataView) {
            DownloadDataView view = (DownloadDataView) e.getWindow();
            view.removeWindowListener(this);
            view.getDownload().removePropertyChangeListener(view);
            downloadViews.remove(view);

        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(downloadTable) && e.getClickCount() >= 2) {
            openDownloadView(mainModel.getSelectedDownload());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
