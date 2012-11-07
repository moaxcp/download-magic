/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DownloadTemplate.java
 *
 * Created on Apr 18, 2012, 1:25:59 PM
 */
package penny.downloadmanager.view.renderer;

import penny.downloadmanager.model.db.DownloadData;
import java.awt.Color;

/**
 *
 * @author john
 */
public class DownloadTemplate extends javax.swing.JPanel {

    /** Creates new form DownloadTemplate */
    public DownloadTemplate() {
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

        jLabel4 = new javax.swing.JLabel();
        fileName = new javax.swing.JLabel();
        url = new javax.swing.JLabel();
        progress = new javax.swing.JProgressBar();

        jLabel4.setText("jLabel4");

        fileName.setText("File Name");

        url.setText("Url");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fileName, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
            .addComponent(url, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
            .addComponent(progress, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(fileName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(url)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fileName;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JProgressBar progress;
    private javax.swing.JLabel url;
    // End of variables declaration//GEN-END:variables

    void setDownloadData(DownloadData data, Color background, Color forground) {
        if (data != null) {
            fileName.setText(data.getFile());
            this.progress.setMaximum(10000);
            int prog = data.getSize() > 0 ? ((int) (data.getDownloaded() / (float) data.getSize() * 10000)) : 0;
            this.progress.setValue(prog);
            url.setText(data.getUrl().toString());
        }
        this.setBackground(background);
        this.setForeground(forground);
    }
}