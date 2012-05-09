/*
 * DownloadDOA.java
 *
 * Created on Oct 19, 2007, 9:13:45 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model.db;
import penny.download.DownloadStatus;
import penny.downloadmanager.model.DownloadData;
import java.util.List;

/**
 *
 * @author John
 */
public interface DownloadDAO {

    public List<DownloadData> getDownloads();

    public void insertDownload(DownloadData download);

    public void updateDownload(DownloadData download, String property);

    public void updateDownload(DownloadData download);

    public void saveLink(String url, String link, String type);

    public void saveLink(String url, String link, String type, int count);

    public void saveWord(String url, String word);

    public void saveProperty(String url, String name, Object property);

    public void deleteDownload(String url);

    public void deleteLinks(String url);

    public void deleteWords(String url);

    public void deleteProperties(String url);

    public void deleteProperty(String url, String name);

    public void clearDownloads(List<DownloadData> downloads);
    
    public void clearDownloads();

    public long getDownloadCount();

    public long getUrlCount();

    public long getPropertyCount();

    public long getWordCount();
}