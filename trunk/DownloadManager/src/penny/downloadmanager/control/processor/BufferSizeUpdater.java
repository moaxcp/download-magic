/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.processor;

import java.util.logging.Level;
import java.util.logging.Logger;
import penny.download.AbstractDownload;
import penny.download.DownloadProcessor;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.db.Download;

/**
 *
 * @author john
 */
public class BufferSizeUpdater {

    private Download download;
    
    public BufferSizeUpdater(Download download) {
        this.download = download;
    }
    
    public void update(int read) {
    }
    
    public void complete() {
        int rate = (int) (download.getDownloadTime() / 1000000000 == 0 ? 0 : download.getDownloaded() / (download.getDownloadTime() / 1000000000));
        if (rate != 0) {
            Model.getApplicationSettings().getDownloadingModel().getDownloadSettings().setBufferSize(rate / 5);
            Model.getSettingsSaver().save();
            Logger.getLogger(BufferSizeUpdater.class.getName()).log(Level.FINE, "Set buffer size to {0}", Model.getApplicationSettings().getDownloadingModel().getDownloadSettings().getBufferSize());
        }
    }
}
