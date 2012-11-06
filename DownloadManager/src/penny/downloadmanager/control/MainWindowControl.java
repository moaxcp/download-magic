package penny.downloadmanager.control;

import penny.download.DownloadStatus;
import penny.downloadmanager.control.task.TaskManager;
import penny.downloadmanager.model.gui.SettingsDialogModel;
import penny.downloadmanager.model.db.DownloadData;
import penny.downloadmanager.model.gui.AddDialogModel;
import penny.downloadmanager.model.gui.AddTaskModel;
import penny.downloadmanager.model.gui.MainWindowModel;
import penny.downloadmanager.model.task.Status;
import penny.downloadmanager.model.task.TaskData;
import penny.downloadmanager.util.RandomChanges;
import penny.downloadmanager.view.DownloadDataView;
import penny.downloadmanager.view.test.ConnectionTest;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.view.View;

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
        DownloadData d = new DownloadData(url);
        mainModel.getDownloads().add(d);
    }

    public void add(List<URL> urls) {
        for (URL u : urls) {
            add(u);
        }
    }

    public void openDownloadView(DownloadData download) {
            DownloadDataView view = null;

            for(DownloadDataView v : downloadViews) {
                if(v.getDownload().equals(mainModel.getSelectedDownload())) {
                    view = v;
                    break;
                }
            }

            if(view == null) {
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
            Model.remove(new File(mainModel.getSelectedDownload().getTempPath()));
            mainModel.getDownloads().remove(mainModel.getSelectedDownload());
        } else if (e.getActionCommand().equals(COM_QUEUEDOWNLOAD) && mainModel.getSelectedDownload() != null) {
            mainModel.getSelectedDownload().queue();
        } else if (e.getActionCommand().equals(COM_EXIT)) {
            exit();
        } else if (e.getActionCommand().equals(COM_SYSTEM)) {
            System.out.println(e.getActionCommand());
        } else if (e.getActionCommand().equals(COM_METAL)) {
            System.out.println(e.getActionCommand());
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
        } else if(e.getActionCommand().equals(COM_APPLICATIONSTARTUP)) {
            Model.getStartupDialogModel().setVisible(true);
        }
    }

    private void analyzer() {
        ConnectionTest analyzer = new ConnectionTest();
        analyzer.setVisible(true);
    }

    private void randomChange() {
        if (mainModel.isRandomChange()) {
            randomChanges.on();
            Thread t = new Thread(randomChanges);
            t.start();
        } else {
            randomChanges.stop();
        }
    }

    private void startTasks() {
        mainModel.getDownloads().getReadWriteLock().writeLock().lock();
        for (DownloadData i : mainModel.getDownloads()) {
            if (!i.getStatus().equals(DownloadStatus.COMPLETE)) {
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
        if(e.getWindow().equals(View.getMainWindowView())) {
            exit();
        } else if(e.getWindow() instanceof DownloadDataView) {
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
