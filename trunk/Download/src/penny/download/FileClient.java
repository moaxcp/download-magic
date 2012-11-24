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
public class FileClient extends ProtocolClient {
    
    private DownloadSettings settings;

    FileClient(DownloadSettings settings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    void shutdown() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    void setDownload(AbstractDownload download) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    void connect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    boolean isDataRestarting() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    InputStream getContent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
