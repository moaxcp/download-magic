/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control;

import java.io.File;
import java.util.List;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.db.Download;

/**
 *
 * @author john
 */
public class StartupFileChecker {

    private List<Download> downloads;
    private long current;
    private long total;

    public StartupFileChecker(List<Download> downloads) {
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
        for (Download download : downloads) {
            if (Model.save(download)) {
                File file = new File(download.getTempPath());
                if (file.length() != download.getDownloaded()) {
                    
                }
            }
            setCurrent(getCurrent() + 1);
        }
    }
}
