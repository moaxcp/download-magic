/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.control.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import penny.download.DownloadStatus;
import com.github.moaxcp.downloadmanager.model.Model;
import com.github.moaxcp.downloadmanager.model.db.Download;
import com.github.moaxcp.downloadmanager.model.gui.MD5ingModel;
import com.github.moaxcp.recmd5.MD5MessageDigest;
import com.github.moaxcp.recmd5.MD5State;

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

    public void resetMD5FromFile(File file) throws FileNotFoundException, IOException {
        if (Model.generateMD5(download)) {
            download.setMD5(new MD5State());
            md5 = new MD5MessageDigest(download.getMD5());

            InputStream in = null;
            try {
                in = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                int read = in.read(buffer);
                while (read != -1 && download.getStatus() != DownloadStatus.STOPPING) {
                    update(read, buffer);
                    read = in.read(buffer);
                }
            } finally {
                in.close();
            }
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
