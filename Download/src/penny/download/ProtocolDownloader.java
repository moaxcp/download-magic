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
    abstract void download(Download d);
    abstract void shutdown();

    static ProtocolDownloader getDownloader(String protocol, Downloader d) {
        ProtocolDownloader downloader = null;
        if(protocol.toLowerCase().equals(Download.HTTP)) {
            downloader = new HttpDownloader(d);
        } else if(protocol.toLowerCase().equals(Download.HTTPS)) {
            downloader = new HttpDownloader(d);
        } else if(protocol.toLowerCase().equals(Download.FTP)) {
            downloader = new FtpDownloader(d);
        } else if(protocol.toLowerCase().equals(Download.FILE)) {
            downloader = new FileDownloader(d);
        }
        return downloader;
    }
}