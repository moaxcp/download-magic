/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.view.test;

import javax.swing.JTextArea;
import penny.download.AbstractDownload;
import penny.download.DownloadProcessor;

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

    @Override
    public void doChunck(int read, byte[] buffer) {
        for (int i = 0; i < read; i++) {
            urlContent.append((char)buffer[i]);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                text.setText(urlContent.toString());
            }
        });
    }

    @Override
    public void onInit(AbstractDownload d) {
        
    }

    @Override
    public void onReset() {
        
    }

    @Override
    public void onPrepare() {
        
    }

    @Override
    public void onFinalize() {
        
    }
}
