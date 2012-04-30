/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.view.test;

import penny.download.Download;
import penny.download.DownloadProcessor;
import javax.swing.JList;
import javax.swing.JTextArea;

/**
 *
 * @author john
 */
public class TextUpdater implements DownloadProcessor {

    JTextArea text;
    StringBuffer urlContent = new StringBuffer();

    public TextUpdater(JTextArea text) {
        this.text = text;
    }

    public void onStartInput(Download d) {
    }

    public void onEndInput(Download d) {
    }

    public void doChunck(Download d, int read, byte[] buffer) {
        for (int i = 0; i < read; i++) {
            urlContent.append((char)buffer[i]);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                text.setText(urlContent.toString());
            }
        });
    }

    public void onReset(Download d) {

    }

    public void onCompleted(Download d) {
        
    }

    public void setDownloaded(Download d) {
        
    }

    public boolean onCheck(Download d) {
        return true;
    }

    @Override
    public void onInit(Download d) {
        
    }
}
