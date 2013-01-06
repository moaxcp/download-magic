/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import penny.download.AbstractDownload;
import penny.download.DownloadProcessor;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.db.Download;
import penny.downloadmanager.model.gui.MD5ingModel;
import penny.downloadmanager.util.Util;
import penny.recmd5.MD5MessageDigest;
import penny.recmd5.MD5State;

/**
 *
 * @author john
 */
public class MD5er {

    private MessageDigest md5;
    private MD5ingModel md5Model;
    private Download download;

    public MD5er(Download download) {
        this.download = download;
        md5Model = Model.getApplicationSettings().getMd5ingModel();
        md5 = new MD5MessageDigest(download.getMD5());
    }

    public void resetMD5FromFile() throws FileNotFoundException, IOException {
        if (Model.generateMD5(download)) {
            download.setMD5(new MD5State());
            md5 = new MD5MessageDigest(download.getMD5());
            File temp = new File(Util.getTempFile(download));
            File save = new File(Util.getSaveFile(download));
            File file = null;
            if (temp.exists()) {
                file = temp;
            } else if (save.exists()) {
                file = save;
            }

            InputStream in = null;
            in = new FileInputStream(file);
            byte[] buffer = new byte[10240];
            int read = in.read(buffer);
            while (read != -1) {
                update(read, buffer);
                read = in.read(buffer);
            }
            in.close();
        }
    }

    public void update(int read, byte[] buffer) {
        if (Model.generateMD5(download)) {
            md5.update(buffer, 0, read);
            if (md5Model.isUpdateMD5()) {
                md5.digest();
                download.setMD5(download.getMD5());
            }
        }
    }

    public void reset() {
        download.setMD5(new MD5State());
        md5 = new MD5MessageDigest(download.getMD5());
    }

    public void complete() {
        md5.digest();
        download.setMD5(download.getMD5());
    }
}
