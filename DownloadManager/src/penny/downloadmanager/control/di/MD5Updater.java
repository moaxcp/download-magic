/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.di;

import penny.recmd5.MD5MessageDigest;
import penny.recmd5.MD5State;
import penny.download.Download;
import penny.download.DownloadProcessor;
import penny.downloadmanager.model.ApplicationSettingsModel;
import penny.downloadmanager.model.DownloadData;
import penny.downloadmanager.model.gui.MD5ingModel;
import penny.downloadmanager.model.Model;
import java.security.MessageDigest;

/**
 *
 * @author john
 */
public class MD5Updater implements DownloadProcessor {

    MessageDigest md5;
    MD5ingModel md5Model;

    public MD5Updater() {
        md5Model = Model.getApplicationSettings().getMd5ingModel();
    }

    private void init(Download d) {
        DownloadData i = (DownloadData) d;
        md5 = new MD5MessageDigest(i.getMD5());
    }

    @Override
    public void onStartInput(Download d) {
        if (md5Model.isGenerateMD5()) {
            init(d);
        }
    }

    @Override
    public void onEndInput(Download d) {
    }

    @Override
    public void onCompleted(Download d) {
        DownloadData i = (DownloadData) d;
        if (Model.generateMD5(i)) {
            md5.digest();
            i.setMD5(i.getMD5());
        }
    }

    @Override
    public boolean onCheck(Download d) {
        return true;
    }

    @Override
    public void doChunck(Download d, int read, byte[] buffer) {
        DownloadData i = (DownloadData) d;
        if (Model.generateMD5(i)) {
            md5.update(buffer, 0, read);
            if (md5Model.isUpdateMD5()) {
                md5.digest();
                i.setMD5(i.getMD5());
            }
        }
    }

    @Override
    public void onReset(Download d) {
        DownloadData i = (DownloadData) d;
        if (Model.generateMD5(i)) {
            i.setMD5(new MD5State());
            init(d);
        }
    }

    @Override
    public void onInit(Download d) {
        DownloadData i = (DownloadData) d;
            init(d);
        if (d.getDownloaded() <= 0 && Model.generateMD5(i)) {
            i.setMD5(new MD5State());
        }
    }
}
