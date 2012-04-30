/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DownloadPanel.java
 *
 * Created on Jun 9, 2011, 6:37:34 PM
 */

package penny.downloadmanager.view.test;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventTableModel;
import penny.downloadmanager.model.DownloadData;
import java.awt.LayoutManager;

/**
 *
 * @author john
 */
public class DownloadPanel extends javax.swing.JPanel {
    private EventList<DownloadData> list;
    private DownloadTableFormatCM format;
    private EventTableModel<DownloadData> downloadModel;


    public DownloadPanel() {

    }


    /** Creates new form DownloadPanel */
    public DownloadPanel(EventList<DownloadData> list, DownloadTableFormatCM format) {
        this.list = list;
        this.format = format;
        downloadModel = new EventTableModel<DownloadData>(list, format);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        downloadTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        downloadTable.setModel(downloadModel);
        downloadTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(downloadTable);

        jScrollPane2.setViewportView(jScrollPane1);

        jLabel1.setText("hello");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(167, 167, 167)
                .addComponent(jLabel1)
                .addContainerGap(214, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(37, 37, 37)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable downloadTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    /**
     * @param list the list to set
     */
    public void setList(EventList<DownloadData> list) {
        this.list = list;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(DownloadTableFormatCM format) {
        this.format = format;
    }

}
