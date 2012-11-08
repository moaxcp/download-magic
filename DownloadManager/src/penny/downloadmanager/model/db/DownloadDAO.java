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

    public void saveLink(UUID uuid, String link, String type);

    public void saveLink(UUID uuid, String link, String type, int count);

    public void saveWord(UUID uuid, String word);

    public void saveProperty(UUID uuid, String name, Object property);

    public void deleteDownload(UUID uuid);

    public void deleteLinks(UUID uuid);

    public void deleteWords(UUID uuid);

    public void deleteProperties(UUID uuid);

    public void deleteProperty(UUID uuid, String name);

    public void clearDownloads(List<Download> downloads);
    
    public void clearDownloads();

    public long getDownloadCount();

    public long getUrlCount();

    public long getPropertyCount();

    public long getWordCount();
}