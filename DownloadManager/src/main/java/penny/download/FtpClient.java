/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author john
 */
class FtpClient extends ProtocolClient {

    private FTPClient client;
    private InputStream content;
    private DownloadSettings settings;
    private AbstractDownload download;
    private boolean restart;

    FtpClient(DownloadSettings settings) {
        this.settings = settings;
        client = new FTPClient();
    }

    @Override
    void setDownload(AbstractDownload download) {
        this.download = download;
        content = null;
    }

    private boolean statusCheck() throws IOException {
        download.setResponseCode(client.getReplyCode());
        if (!FTPReply.isPositiveCompletion(download.getResponseCode())) {
            client.disconnect();
            Logger.getLogger(FtpClient.class.getName()).logp(Level.SEVERE, FtpClient.class.getName(), "statusCheck()", "String " + client.getReplyString() + " Code " + client.getReplyCode());
            download.setStatus(DownloadStatus.ERROR, "command failed " + client.getReplyString());
            return false;
        } else if (download.getStatus() == DownloadStatus.STOPPING) {
            return false;
        }
        return true;
    }

    @Override
    void connect() {
        try {
            client.setDefaultTimeout(settings.getFtpConnectTimeout());
            client.setConnectTimeout(settings.getFtpConnectTimeout());
            client.connect(download.getUrl().getHost(), download.getUrl().getPort() < 0 ? 21 : download.getUrl().getPort());

            if (!statusCheck()) {
                return;
            }

            client.enterLocalPassiveMode();

            if (!statusCheck()) {
                return;
            }

            client.setSoTimeout(settings.getFtpReadTimeout());
            String user = "anonymous";
            String password = "";
            if (download.getUrl().getUserInfo() != null) {
                String[] s = download.getUrl().getUserInfo().split(":");
                switch (s.length) {
                    case 2:
                        password = s[1];
                    case 1:
                        user = s[0];
                }
            }

            client.login(user, password);

            if (!statusCheck()) {
                return;
            }

            Logger.getLogger(FtpClient.class.getName()).logp(Level.FINE, FtpClient.class.getName(), "connect()", "Login successful " + client.getReplyString());

            FTPFile file = client.mlistFile(download.getUrl().getPath());
            if (!statusCheck()) {
                return;
            }

            if (file != null && download.getSize() <= 0) {
                download.setSize(file.getSize());
                Logger.getLogger(FtpClient.class.getName()).logp(Level.FINE, FtpClient.class.getName(), "connect()", "File size reply " + client.getReplyString());
            }

            if (download.getDownloaded() > 0) {
                client.doCommand("REST", Long.toString(download.getDownloaded()));
                download.setResponseCode(client.getReplyCode());
                Logger.getLogger(FtpClient.class.getName()).logp(Level.FINE, FtpClient.class.getName(), "connect()", "FTP Offset: " + " " + client.getReplyString());
                if (client.getReplyCode() != 350) {
                    restart = true;
                    Logger.getLogger(FtpClient.class.getName()).logp(Level.FINE, FtpClient.class.getName(), "connect()", "Restarting download from 0");
                }
            }

            client.setFileType(FTP.BINARY_FILE_TYPE);

            if (!statusCheck()) {
                return;
            }
            Logger.getLogger(FtpClient.class.getName()).logp(Level.FINE, FtpClient.class.getName(), "connect()", "Binary Mode response: " + client.getReplyString());

            content = client.retrieveFileStream(download.getUrl().getPath());
        } catch (Exception ex) {
            if (download.getStatus() != DownloadStatus.STOPPING) {
                download.setStatus(DownloadStatus.ERROR, ex.toString());
                Logger.getLogger(FtpClient.class.getName()).log(Level.SEVERE, null, ex);
            } else {
                download.setMessage(ex.toString());
            }
        }
    }

    @Override
    boolean isDataRestarting() {
        return restart;
    }

    @Override
    InputStream getContent() {
        return content;
    }

    @Override
    void close() {
        if (client.isConnected()) {
            try {
                if (content != null) {
                    content.close();
                }
                client.logout();
                client.disconnect();
            } catch (IOException ex) {
                Logger.getLogger(FtpClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    void shutdown() {
        if (client.isConnected()) {
            try {
                if (content != null) {
                    content.close();
                }
                client.logout();
                client.disconnect();
            } catch (IOException ex) {
                Logger.getLogger(FtpClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
