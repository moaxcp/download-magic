/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.download;

/**
 *
 * @author john
 */
abstract class ProtocolDownloader {
    abstract void download(AbstractDownload d);
    abstract void shutdown();

    static ProtocolDownloader getDownloader(String protocol, Downloader d) {
        ProtocolDownloader downloader = null;
        if(protocol.toLowerCase().equals(AbstractDownload.HTTP)) {
            downloader = new HttpDownloader(d);
        } else if(protocol.toLowerCase().equals(AbstractDownload.HTTPS)) {
            downloader = new HttpDownloader(d);
        } else if(protocol.toLowerCase().equals(AbstractDownload.FTP)) {
            downloader = new FtpDownloader(d);
        } else if(protocol.toLowerCase().equals(AbstractDownload.FILE)) {
            downloader = new FileDownloader(d);
        }
        return downloader;
    }
}