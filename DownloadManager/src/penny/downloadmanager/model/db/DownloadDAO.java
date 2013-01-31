/*
 * DownloadDOA.java
 *
 * Created on Oct 19, 2007, 9:13:45 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model.db;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author John
 */
public interface DownloadDAO {

    public List<Download> getDownloads();
    
    public Download getDownload(UUID uuid);
    
    public void insertDownload(Download download);

    public void updateDownload(Download download, String property);

    public void updateDownload(Download download);

    public void deleteDownload(UUID uuid);

    public void clearDownloads(List<Download> downloads);
    
    public void clearDownloads();

    public long getDownloadCount();
}