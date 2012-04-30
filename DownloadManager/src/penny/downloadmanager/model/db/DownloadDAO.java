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

    public boolean insertDownload(DownloadData download);

    public boolean updateDownload(DownloadData download, String property);

    public boolean updateDownload(DownloadData download);

    public boolean saveLink(String url, String link, String type);

    public boolean saveWord(String url, String word);

    public boolean saveProperty(String url, String name, Object property);

    public boolean deleteDownload(DownloadData download);

    public boolean clearDownloads(DownloadStatus status);
    
    public long clearDownloads();

    public long getDownloadCount();

    public long getUrlCount();

    public long getPropertyCount();

    public long getWordCount();
}