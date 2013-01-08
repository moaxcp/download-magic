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

    @Override
    void connect() {
        try {
            client.setDefaultTimeout(settings.getFtpConnectTimeout());
            client.setConnectTimeout(settings.getFtpConnectTimeout());
            client.connect(download.getUrl().getHost(), download.getUrl().getPort() < 0 ? 21 : download.getUrl().getPort());
            download.setResponseCode(client.getReplyCode());

            if (!FTPReply.isPositiveCompletion(download.getResponseCode())) {
                client.disconnect();
                Logger.getLogger(FtpClient.class.getName()).logp(Level.SEVERE, FtpClient.class.getName(), "download()", "String " + client.getReplyString() + " Code " + client.getReplyCode());
                download.setStatus(DownloadStatus.ERROR, "failed to connect " + client.getReplyString());
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
            if (!client.login(user, password)) {
                client.logout();
                client.disconnect();
                Logger.getLogger(FtpClient.class.getName()).logp(Level.SEVERE, FtpClient.class.getName(), "download()", "String " + client.getReplyString() + " Code " + client.getReplyCode());
                download.setStatus(DownloadStatus.ERROR, "failed to login username: " + user + ", password: " + password + " " + client.getReplyString());
                return;
            }
            download.setResponseCode(client.getReplyCode());

            if (!FTPReply.isPositiveCompletion(download.getResponseCode())) {
                client.disconnect();
                Logger.getLogger(FtpClient.class.getName()).logp(Level.SEVERE, FtpClient.class.getName(), "download()", "String " + client.getReplyString() + " Code " + client.getReplyCode());
                download.setStatus(DownloadStatus.ERROR, "failed to login username: " + user + ", password: " + password + " " + client.getReplyString());
                return;
            }
            Logger.getLogger(FtpClient.class.getName()).logp(Level.FINE, FtpClient.class.getName(), "download()", "Login successful " + client.getReplyString());
            client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
            client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            FTPFile file = client.mlistFile(download.getUrl().getPath());
            download.setResponseCode(client.getReplyCode());

            if (file != null) {
                download.setSize(file.getSize());
                Logger.getLogger(FtpClient.class.getName()).logp(Level.FINE, FtpClient.class.getName(), "download()", "File size reply " + client.getReplyString());
            }

            client.doCommand("REST", Long.toString(download.getDownloaded()));
            download.setResponseCode(client.getReplyCode());
            Logger.getLogger(FtpClient.class.getName()).logp(Level.FINE, FtpClient.class.getName(), "download()", "FTP Offset is " + client.getRestartOffset() + " " + client.getReplyString());

            if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
                restart = true;
            }
            content = client.retrieveFileStream(download.getUrl().getPath());
            download.setResponseCode(client.getReplyCode());
            if (content == null) {
                download.setStatus(DownloadStatus.ERROR, client.getReplyString());
            }
        } catch (SocketException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(FtpClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(FtpClient.class.getName()).log(Level.SEVERE, null, ex);
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
                if(content != null) {
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
        try {
            if (!client.completePendingCommand()) {
                client.logout();
                client.disconnect();
                Logger.getLogger(FtpClient.class.getName()).severe("client.completePendingCommand() failed");
            }
        } catch (IOException ex) {
            Logger.getLogger(FtpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
