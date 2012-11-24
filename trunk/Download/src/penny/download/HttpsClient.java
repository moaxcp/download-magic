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
class HttpsClient extends ProtocolClient {
    
    private HttpClient client;
    
    public HttpsClient(DownloadSettings settings) {
        client = new HttpClient(settings);
    }

    @Override
    void setDownload(AbstractDownload download) {
        client.setDownload(download);
    }

    @Override
    void connect() {
        client.connect();
    }

    @Override
    boolean isDataRestarting() {
        return client.isDataRestarting();
    }

    @Override
    InputStream getContent() {
        return client.getContent();
    }

    @Override
    void close() {
        client.close();
    }

    @Override
    void shutdown() {
        client.shutdown();
    }
}
