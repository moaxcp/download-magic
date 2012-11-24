/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.view.test;

import penny.download.AbstractDownload;
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

    public void onPrepare(AbstractDownload d) {
    }

    public void onEndInput(AbstractDownload d) {
    }

    public void doChunck(AbstractDownload d, int read, byte[] buffer) {
        for (int i = 0; i < read; i++) {
            urlContent.append((char)buffer[i]);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                text.setText(urlContent.toString());
            }
        });
    }

    public void onReset(AbstractDownload d) {

    }

    public void onFinalize(AbstractDownload d) {
        
    }

    public void setDownloaded(AbstractDownload d) {
        
    }

    public boolean onCheck(AbstractDownload d) {
        return true;
    }

    @Override
    public void onInit(AbstractDownload d) {
        
    }
}
