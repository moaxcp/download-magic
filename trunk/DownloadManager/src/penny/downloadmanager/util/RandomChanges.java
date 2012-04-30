/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.util;

import ca.odell.glazedlists.EventList;
import penny.downloadmanager.model.DownloadData;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class RandomChanges implements Runnable {

    private boolean run;
    private EventList<DownloadData> downloads;
    private Random random = new Random();

    public RandomChanges(EventList<DownloadData> downloads) {
        this.downloads = downloads;
        run = true;
    }

    public void stop() {
        run = false;
    }

    public void on() {
        run = true;
    }

    public void run() {
        int changed = downloads.size();
        while (run && changed > 0) {
            changed = downloads.size();
            for (DownloadData d : downloads) {
                if (d.getSize() < 0) {
                    d.setSize(random.nextInt(100000) + 50000);
                }
                long downloaded = d.getDownloaded() + random.nextInt(750) + 256;
                if (downloaded >= d.getSize()) {
                    d.setDownloaded(d.getSize());
                    changed--;
                } else {
                    d.setDownloaded(downloaded);
                }
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(RandomChanges.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
