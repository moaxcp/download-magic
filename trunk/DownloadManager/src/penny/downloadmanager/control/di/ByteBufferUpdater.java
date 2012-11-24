/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.di;

import java.util.logging.Level;
import java.util.logging.Logger;
import penny.download.AbstractDownload;
import penny.download.DownloadProcessor;
import penny.downloadmanager.model.Model;

/**
 *
 * @author john
 */
public class ByteBufferUpdater implements DownloadProcessor {

    private long bytes;

    @Override
    public void onInit(AbstractDownload d) {
        bytes = 0;
    }

    @Override
    public boolean onCheck(AbstractDownload d) {
        return true;
    }

    @Override
    public void onReset(AbstractDownload d) {
    }

    @Override
    public void onPrepare(AbstractDownload d) {
    }

    @Override
    public void doChunck(AbstractDownload d, int read, byte[] buffer) {
        bytes += read;
    }

    @Override
    public void onFinalize(AbstractDownload d) {
        int rate = (int) (d.getDownloadTime() / 1000000000 == 0 ? 0 : d.getDownloaded() / (d.getDownloadTime() / 1000000000));
        if (rate != 0) {
            Model.getApplicationSettings().getDownloadingModel().getDownloadSettings().setBufferSize(rate / 5);
            Model.getSettingsSaver().save();
            Logger.getLogger(ByteBufferUpdater.class.getName()).log(Level.FINE, "Set buffer size to {0}", Model.getApplicationSettings().getDownloadingModel().getDownloadSettings().getBufferSize());
        }
    }
}
