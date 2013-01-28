package penny.downloadmanager.view.test;

/*
 * ConnectionTest.java
 *
 * Created on May 8, 2008, 7:51 PM
 */
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import penny.download.AbstractDownload;
import penny.download.DownloadSettings;
import penny.download.DownloadStatus;
import penny.download.Downloader;
import penny.downloadmanager.model.db.Download;
import penny.downloadmanager.view.DownloadDataView;

/**
 *
 * @author  John J Mercier
 */
public class ConnectionTest extends javax.swing.JFrame implements PropertyChangeListener, ListDataListener {


    DefaultListModel eventList = new DefaultListModel();
    Download download = null;
    TextUpdater tu;

    /** Creates new form ConnectionTest */
    public ConnectionTest() {
        initComponents();
        tu = new TextUpdater(output);
        eventList.addListDataListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        goButton = new javax.swing.JButton();
        addressBox = new javax.swing.JTextField();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        output = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Downloader Test"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        goButton.setText("Go"); // NOI18N
        goButton.setFocusable(false);
        goButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        goButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButtonActionPerformed(evt);
            }
        });

        addressBox.setText("http://www.facebook.com/");
        addressBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressBoxActionPerformed(evt);
            }
        });

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        output.setEditable(false);
        output.setColumns(20);
        output.setRows(5);
        jScrollPane1.setViewportView(output);

        jSplitPane1.setTopComponent(jScrollPane1);

        jList1.setModel(eventList);
        jList1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(jList1);

        jSplitPane1.setRightComponent(jScrollPane2);

        jButton1.setText("View");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addressBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(goButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(addressBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(goButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goButtonActionPerformed
    if (goButton.getText().equals("Go")) {
        output.setText("");
        eventList.clear();
        Runnable updater = new Runnable() {

            public void run() {
                goButton.setText("Stop");
                download = null;
                try {
                    download = new Download();
                    download.setUrl(new URL(addressBox.getText()));
                    download.addPropertyChangeListener(ConnectionTest.this);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(ConnectionTest.class.getName()).log(Level.SEVERE, null, ex);
                }
                Downloader downloader = new Downloader(new DownloadSettings());
                downloader.setDownload(download);
                downloader.setProcessor(tu);
                downloader.download();
                goButton.setText("Go");
                goButton.setEnabled(true);
            }
        };
        new Thread(updater).start();
    } else {
        download.stop();
        goButton.setEnabled(false);
    }
}//GEN-LAST:event_goButtonActionPerformed

private void addressBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressBoxActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_addressBoxActionPerformed

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    if (download != null && download.getStatus() != DownloadStatus.COMPLETE) {
        download.stop();
    }
}//GEN-LAST:event_formWindowClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        DownloadDataView view = new DownloadDataView(download);
        view.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ConnectionTest().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressBox;
    private javax.swing.JButton goButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea output;
    // End of variables declaration//GEN-END:variables

    public void propertyChange(final PropertyChangeEvent evt) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                if (evt instanceof IndexedPropertyChangeEvent) {
                    
                } else if (evt.getPropertyName().equals(AbstractDownload.PROP_STATUS)) {
                    if (((AbstractDownload) evt.getSource()).getStatus() == DownloadStatus.STOPPED) {
                        goButton.setEnabled(true);
                    }
                    eventList.addElement(evt.getPropertyName() + " = " + evt.getNewValue().toString() + "; " + ((AbstractDownload) evt.getSource()).getMessage());
                } else {
                    eventList.addElement(evt.getPropertyName() + " = " + evt.getNewValue());
                }
            }
        });
    }

    public void intervalAdded(ListDataEvent e) {
        jList1.ensureIndexIsVisible(eventList.getSize() - 1);
    }

    public void intervalRemoved(ListDataEvent e) {
    }

    public void contentsChanged(ListDataEvent e) {
        jList1.setSelectedIndex(eventList.size() - 1);
    }
}
