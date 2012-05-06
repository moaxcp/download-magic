/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainWindow.java
 *
 * Created on Feb 28, 2011, 8:28:33 AM
 */
package penny.downloadmanager.view;

import penny.downloadmanager.view.renderer.ProgressRenderer;
import penny.downloadmanager.view.renderer.TaskDataRenderer;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import penny.download.DownloadStatus;
import penny.downloadmanager.control.MainWindowControl;
import penny.downloadmanager.model.DownloadData;
import penny.downloadmanager.model.gui.MainWindowModel;
import penny.downloadmanager.model.task.Status;
import penny.downloadmanager.model.task.TaskData;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JProgressBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import penny.downloadmanager.model.TaskManagerModel;

/**
 *
 * @author john
 */
public class MainWindow extends javax.swing.JFrame implements PropertyChangeListener, ListEventListener, ListSelectionListener {

    private EventTableModel<DownloadData> downloadTableModel;
    private EventListModel<TaskData> taskListModel;
    private MainWindowModel mainWindowModel;
    private TaskManagerModel taskModel;

    public MainWindow(MainWindowModel mainWindowModel, TaskManagerModel taskModel) {
        this.mainWindowModel = mainWindowModel;
        this.taskModel = taskModel;
        taskModel.addPropertyChangeListener(this);
        this.mainWindowModel.addPropertyChnageListener(this);
        downloadTableModel = new EventTableModel<DownloadData>(mainWindowModel.getDownloads(), mainWindowModel.getDownloadFormat());
        mainWindowModel.getDownloads().addListEventListener(this);
        taskListModel = new EventListModel<TaskData>(mainWindowModel.getTasks());
        mainWindowModel.getTasks().addListEventListener(this);
        initComponents();
        ProgressRenderer prog = new ProgressRenderer(0, 100);
        prog.setStringPainted(true);
        downloadTable.setDefaultRenderer(JProgressBar.class, prog);
        downloadTable.getSelectionModel().addListSelectionListener(this);
        taskList.getSelectionModel().addListSelectionListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        downloadTable = new javax.swing.JTable();
        addDownloadButton = new javax.swing.JButton();
        removeDownloadButton = new javax.swing.JButton();
        queueDownloadButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        addTaskButton = new javax.swing.JButton();
        removeTaskButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taskList = new javax.swing.JList();
        stopButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exitMenu = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        applicationStartup = new javax.swing.JMenuItem();
        settingsMenu = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        clearDownloadsMenu = new javax.swing.JMenuItem();
        clearCompleteDownloadsMenu = new javax.swing.JMenuItem();
        clearErrorDownloadsMenu = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        systemLookMenu = new javax.swing.JMenuItem();
        metalLookMenu = new javax.swing.JMenuItem();
        downloadProperties = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        testConnectionMenu = new javax.swing.JMenuItem();
        addDownloadMenu = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        randomChangesMenu = new javax.swing.JCheckBoxMenuItem();
        jMenu5 = new javax.swing.JMenu();
        aboutMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Download Magic");

        downloadTable.setModel(downloadTableModel);
        downloadTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        downloadTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(downloadTable);

        addDownloadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/penny/downloadmanager/resources/16x16/actions/list-add.png"))); // NOI18N
        addDownloadButton.setText("Add");

        removeDownloadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/penny/downloadmanager/resources/16x16/actions/list-remove.png"))); // NOI18N
        removeDownloadButton.setText("Remove");
        removeDownloadButton.setEnabled(false);

        queueDownloadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/penny/downloadmanager/resources/16x16/actions/go-bottom.png"))); // NOI18N
        queueDownloadButton.setText("Queue");
        queueDownloadButton.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(addDownloadButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeDownloadButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(queueDownloadButton)
                .addGap(246, 246, 246))
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addDownloadButton)
                    .addComponent(removeDownloadButton)
                    .addComponent(queueDownloadButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Downloads", jPanel2);

        addTaskButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/penny/downloadmanager/resources/16x16/actions/list-add.png"))); // NOI18N
        addTaskButton.setText("Add");

        removeTaskButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/penny/downloadmanager/resources/16x16/actions/list-remove.png"))); // NOI18N
        removeTaskButton.setText("Remove");
        removeTaskButton.setEnabled(false);

        taskList.setModel(taskListModel);
        taskList.setCellRenderer(new TaskDataRenderer(taskList));
        jScrollPane1.setViewportView(taskList);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(addTaskButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeTaskButton)
                .addGap(372, 372, 372))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addTaskButton)
                    .addComponent(removeTaskButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Task Manager", jPanel3);

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/penny/downloadmanager/resources/16x16/actions/media-playback-stop.png"))); // NOI18N
        stopButton.setText("Stop");
        stopButton.setEnabled(false);

        startButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/penny/downloadmanager/resources/16x16/actions/media-playback-start.png"))); // NOI18N
        startButton.setText("Start");

        jMenu1.setText("File");

        exitMenu.setText("Exit");
        jMenu1.add(exitMenu);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        applicationStartup.setText("Application Startup");
        jMenu2.add(applicationStartup);

        settingsMenu.setText("Download Settings");
        jMenu2.add(settingsMenu);

        jMenuItem2.setText("Database Settings");
        jMenu2.add(jMenuItem2);

        clearDownloadsMenu.setText("Clear All");
        jMenu2.add(clearDownloadsMenu);

        clearCompleteDownloadsMenu.setText("Clear Complete");
        jMenu2.add(clearCompleteDownloadsMenu);

        clearErrorDownloadsMenu.setText("Clear Errors");
        jMenu2.add(clearErrorDownloadsMenu);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("View");

        jMenu6.setText("Look");

        systemLookMenu.setText("System");
        jMenu6.add(systemLookMenu);

        metalLookMenu.setText("Metal");
        jMenu6.add(metalLookMenu);

        jMenu3.add(jMenu6);

        downloadProperties.setText("Properties");
        jMenu3.add(downloadProperties);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Tools");

        testConnectionMenu.setText("Test Connection");
        jMenu4.add(testConnectionMenu);

        addDownloadMenu.setText("Add Downloads");
        jMenu4.add(addDownloadMenu);

        jMenu7.setText("Debug");

        randomChangesMenu.setText("Random Changes");
        jMenu7.add(randomChangesMenu);

        jMenu4.add(jMenu7);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Help");

        aboutMenu.setText("About");
        jMenu5.add(aboutMenu);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopButton)
                .addGap(325, 325, 325))
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(stopButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenu;
    private javax.swing.JButton addDownloadButton;
    private javax.swing.JMenuItem addDownloadMenu;
    private javax.swing.JButton addTaskButton;
    private javax.swing.JMenuItem applicationStartup;
    private javax.swing.JMenuItem clearCompleteDownloadsMenu;
    private javax.swing.JMenuItem clearDownloadsMenu;
    private javax.swing.JMenuItem clearErrorDownloadsMenu;
    private javax.swing.JMenuItem downloadProperties;
    private javax.swing.JTable downloadTable;
    private javax.swing.JMenuItem exitMenu;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuItem metalLookMenu;
    private javax.swing.JButton queueDownloadButton;
    private javax.swing.JCheckBoxMenuItem randomChangesMenu;
    private javax.swing.JButton removeDownloadButton;
    private javax.swing.JButton removeTaskButton;
    private javax.swing.JMenuItem settingsMenu;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JMenuItem systemLookMenu;
    private javax.swing.JList taskList;
    private javax.swing.JMenuItem testConnectionMenu;
    // End of variables declaration//GEN-END:variables

    public void registerController(MainWindowControl mainWindowControl) {

        this.addWindowListener(mainWindowControl);

        startButton.addActionListener(mainWindowControl);
        startButton.setActionCommand(MainWindowControl.COM_START);

        stopButton.addActionListener(mainWindowControl);
        stopButton.setActionCommand(MainWindowControl.COM_STOP);

        addDownloadMenu.addActionListener(mainWindowControl);
        addDownloadButton.addActionListener(mainWindowControl);
        addDownloadMenu.setActionCommand(MainWindowControl.COM_ADDDOWNLOAD);
        addDownloadButton.setActionCommand(MainWindowControl.COM_ADDDOWNLOAD);

        removeDownloadButton.addActionListener(mainWindowControl);
        removeDownloadButton.setActionCommand(MainWindowControl.COM_REMOVEDOWNLOAD);

        queueDownloadButton.addActionListener(mainWindowControl);
        queueDownloadButton.setActionCommand(MainWindowControl.COM_QUEUEDOWNLOAD);

        addTaskButton.addActionListener(mainWindowControl);
        addTaskButton.setActionCommand(MainWindowControl.COM_ADDTASK);

        removeTaskButton.addActionListener(mainWindowControl);
        removeTaskButton.setActionCommand(MainWindowControl.COM_REMOVETASK);

        randomChangesMenu.addActionListener(mainWindowControl);
        randomChangesMenu.setActionCommand(MainWindowControl.COM_RANDOM);

        settingsMenu.addActionListener(mainWindowControl);
        settingsMenu.setActionCommand(MainWindowControl.COM_SETTINGS);

        downloadProperties.addActionListener(mainWindowControl);
        downloadProperties.setActionCommand(MainWindowControl.COM_PROPERTIES);

        testConnectionMenu.addActionListener(mainWindowControl);
        testConnectionMenu.setActionCommand(MainWindowControl.COM_ANALYZER);

        systemLookMenu.addActionListener(mainWindowControl);
        systemLookMenu.setActionCommand(MainWindowControl.COM_SYSTEM);

        metalLookMenu.addActionListener(mainWindowControl);
        metalLookMenu.setActionCommand(MainWindowControl.COM_METAL);

        aboutMenu.addActionListener(mainWindowControl);
        aboutMenu.setActionCommand(MainWindowControl.COM_ABOUT);

        exitMenu.addActionListener(mainWindowControl);
        exitMenu.setActionCommand(MainWindowControl.COM_EXIT);
        mainWindowControl.setDownloadTable(downloadTable);
        downloadTable.addMouseListener(mainWindowControl);

        this.clearDownloadsMenu.addActionListener(mainWindowControl);
        this.clearDownloadsMenu.setActionCommand(MainWindowControl.COM_CLEARALLDOWNLOADS);

        this.clearCompleteDownloadsMenu.addActionListener(mainWindowControl);
        this.clearCompleteDownloadsMenu.setActionCommand(MainWindowControl.COM_CLEARCOMPLETEDOWNLOADS);

        this.clearErrorDownloadsMenu.addActionListener(mainWindowControl);
        this.clearErrorDownloadsMenu.setActionCommand(MainWindowControl.COM_CLEARERRORDOWNLOADS);

        applicationStartup.addActionListener(mainWindowControl);
        applicationStartup.setActionCommand(MainWindowControl.COM_APPLICATIONSTARTUP);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(MainWindowModel.PROP_VISIBLE)) {
            this.setVisible((Boolean) evt.getNewValue());
        } else if (evt.getPropertyName().equals(MainWindowModel.PROP_SELDOWNLOAD)) {
            if (evt.getOldValue() != null) {
                ((DownloadData) evt.getOldValue()).removePropertyChangeListener(this);
            }
            if (evt.getNewValue() != null) {
                ((DownloadData) evt.getNewValue()).addPropertyChangeListener(this);
            }
        } else if (evt.getPropertyName().equals(MainWindowModel.PROP_SELTASK)) {
            if (evt.getOldValue() != null) {
                ((TaskData) evt.getOldValue()).removePropertyChangeListener(this);
            }
            if (evt.getNewValue() != null) {
                ((TaskData) evt.getNewValue()).addPropertyChangeListener(this);
            }
        } else if (evt.getPropertyName().equals(DownloadData.PROP_STATUS)) {
            downloadStatus(((DownloadData) evt.getSource()).getStatus());
        } else if (evt.getPropertyName().equals(TaskData.PROP_STATUS)) {
            taskStatus(((TaskData) evt.getSource()).getStatus());
        } else if(evt.getPropertyName().equals(TaskManagerModel.PROP_RUNNING)) {
            if(evt.getNewValue().equals(Boolean.TRUE)) {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            } else if(evt.getNewValue().equals(Boolean.FALSE)) {
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        }
    }

    public void listChanged(ListEvent listChanges) {
        EventList changeList = listChanges.getSourceList();

        while (listChanges.next()) {
            int sourceIndex = listChanges.getIndex();
            int changeType = listChanges.getType();

            switch (changeType) {
                case ListEvent.DELETE:

                    break;
                case ListEvent.INSERT:
                    changeList.getReadWriteLock().readLock().lock();
                    Object obj = changeList.get(sourceIndex);
                    changeList.getReadWriteLock().readLock().unlock();
                    if (obj instanceof DownloadData && mainWindowModel.getDownloadFormat().getColumns((DownloadData) obj)) {
                        downloadTableModel.setTableFormat(mainWindowModel.getDownloadFormat());
                    }
                    break;
                case ListEvent.UPDATE:
                    changeList.getReadWriteLock().readLock().lock();
                    Object obj2 = changeList.get(sourceIndex);
                    changeList.getReadWriteLock().readLock().unlock();
                    if (obj2 instanceof DownloadData && mainWindowModel.getDownloadFormat().getColumns((DownloadData) obj2)) {
                        downloadTableModel.setTableFormat(mainWindowModel.getDownloadFormat());
                    }
                    break;
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {

        if (e.getSource().equals(downloadTable.getSelectionModel()) && !e.getValueIsAdjusting()) {
            int i = downloadTable.getSelectedRow();
            if (i >= 0 && i < mainWindowModel.getDownloads().size()) {
                mainWindowModel.setSelectedDownload(mainWindowModel.getDownloads().get(i));
                downloadStatus(mainWindowModel.getSelectedDownload().getStatus());
            } else {
                removeDownloadButton.setEnabled(false);
                queueDownloadButton.setEnabled(false);
            }
        } else if (e.getSource().equals(taskList.getSelectionModel()) && !e.getValueIsAdjusting()) {
            int i = taskList.getSelectedIndex();
            if (i >= 0) {
                mainWindowModel.setSelectedTask(mainWindowModel.getTasks().get(i));
                taskStatus(mainWindowModel.getSelectedTask().getStatus());
            } else {
                removeTaskButton.setEnabled(false);
            }
        }
    }

    public void downloadStatus(DownloadStatus ds) {
        if (ds == DownloadStatus.QUEUED || ds == DownloadStatus.STOPPED || ds == DownloadStatus.ERROR || ds == DownloadStatus.COMPLETE) {
            removeDownloadButton.setEnabled(true);
            queueDownloadButton.setEnabled(true);
        } else {
            removeDownloadButton.setEnabled(false);
            queueDownloadButton.setEnabled(false);
        }

        if (ds == DownloadStatus.QUEUED) {
            queueDownloadButton.setEnabled(false);
        }
    }

    public void taskStatus(Status ts) {
        if (ts == Status.RUNNING) {
            removeTaskButton.setEnabled(false);
        } else {
            removeTaskButton.setEnabled(true);
        }
    }
}