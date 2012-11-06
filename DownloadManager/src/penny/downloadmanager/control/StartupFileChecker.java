/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import penny.downloadmanager.model.db.DownloadData;
import penny.downloadmanager.model.Model;
import penny.recmd5.MD5MessageDigest;

/**
 *
 * @author john
 */
public class StartupFileChecker {

    private List<DownloadData> downloads;
    private long current;
    private long total;

    public StartupFileChecker(List<DownloadData> downloads) {
        this.downloads = downloads;
    }

    /**
     * @return the current
     */
    public long getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(long current) {
        this.current = current;
    }

    /**
     * @return the total
     */
    public long getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(long total) {
        this.total = total;
    }

    public void checkFileSizes() {
        setTotal(downloads.size());
        setCurrent(0);
        for (DownloadData download : downloads) {
            if (Model.save(download)) {
                File file = new File(download.getTempPath());
                if (file.length() != download.getDownloaded()) {
                    
                }
            }
            setCurrent(getCurrent() + 1);
        }
    }

    public void checkMD5s() {
        for (DownloadData download : downloads) {
            File file = new File(download.getTempPath());
            if (file.exists() && Model.generateMD5(download)) {
                MD5MessageDigest md5 = new MD5MessageDigest();
                try {
                    InputStream in = new FileInputStream(file);
                    byte[] buffer = new byte[10240];
                    int read = in.read(buffer);
                    while (read != -1) {
                        md5.update(buffer, 0, read);
                        current += read;
                        read = in.read(buffer);
                    }
                    md5.digest();
                    Logger.getLogger(StartupFileChecker.class.getName()).fine("MD5 for " + file.toString() + " is " + md5.getState().toString());
                    if (md5.getState().equals(download.getMD5())) {
                        return;
                    } else {
                        Logger.getLogger(StartupFileChecker.class.getName()).warning("MD5 does not match for file for " + download);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(StartupFileChecker.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(StartupFileChecker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
