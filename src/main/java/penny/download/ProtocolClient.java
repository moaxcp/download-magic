/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.download;

import java.io.InputStream;

/**
 *
 * @author john
 */
abstract class ProtocolClient {
    abstract void setDownload(AbstractDownload download);
    abstract void connect();
    abstract boolean isDataRestarting();
    abstract InputStream getContent();
    abstract void close();
    abstract void shutdown();

    static ProtocolClient getClient(String protocol, DownloadSettings settings) {
        ProtocolClient client = null;
        if(protocol.toLowerCase().equals(AbstractDownload.HTTP)) {
            client = new HttpClient(settings);
        } else if(protocol.toLowerCase().equals(AbstractDownload.HTTPS)) {
            client = new HttpClient(settings);
        } else if(protocol.toLowerCase().equals(AbstractDownload.FTP)) {
            client = new FtpClient(settings);
        } else if(protocol.toLowerCase().equals(AbstractDownload.FILE)) {
            client = new FileClient(settings);
        }
        return client;
    }
}