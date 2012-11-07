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
class FtpDownloader extends ProtocolDownloader {

    private FTPClient client;
    private Downloader downloader;

    FtpDownloader(Downloader downloader) {
        this.downloader = downloader;
        client = new FTPClient();
    }

    void shutdown() {
        try {
            client.logout();
            client.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(FtpDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void download(AbstractDownload d) {
        InputStream instream = null;

        try {
            d.setStatus(DownloadStatus.CONNECTING);
            client.setBufferSize(downloader.getdSettings().getBufferSize());
            client.setDefaultTimeout(downloader.getdSettings().getFtpConnectTimeout());
            client.setConnectTimeout(downloader.getdSettings().getFtpConnectTimeout());
            client.connect(d.getUrl().getHost(), d.getUrl().getPort() < 0 ? 21 : d.getUrl().getPort());
            client.setSoTimeout(downloader.getdSettings().getFtpReadTimeout());
            String user = "anonymous";
            String password = "";
            if (d.getUrl().getUserInfo() != null) {
                String[] s = d.getUrl().getUserInfo().split(":");
                switch (s.length) {
                    case 2:
                        password = s[1];
                    case 1:
                        user = s[0];
                }
            }
            client.login(user, password);

            if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
                client.disconnect();
                Logger.getLogger(FtpDownloader.class.getName()).logp(Level.SEVERE, FtpDownloader.class.getName(), "download()", "String " + client.getReplyString() + " Code " + client.getReplyCode());
                d.setStatus(DownloadStatus.ERROR, client.getReplyString());
                return;
            }
            Logger.getLogger(FtpDownloader.class.getName()).logp(Level.FINE, FtpDownloader.class.getName(), "download()", "Login successful " + client.getReplyString());
            client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
            client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            //TODO get contentType
            FTPFile file = client.mlistFile(d.getUrl().getPath());
            if(file != null) {
                d.setSize(file.getSize());
                Logger.getLogger(FtpDownloader.class.getName()).logp(Level.FINE, FtpDownloader.class.getName(), "download()", "File size reply " + client.getReplyString());
            }

            client.setRestartOffset(d.getDownloaded());
            Logger.getLogger(FtpDownloader.class.getName()).logp(Level.FINE, FtpDownloader.class.getName(), "download()", "FTP Offset is " + client.getRestartOffset() + " " + client.getReplyString());

            if(client.getRestartOffset() != d.getDownloaded()) {
                downloader.resetProcessors(d);
            }

            instream = client.retrieveFileStream(d.getUrl().getPath());
            Logger.getLogger(FtpDownloader.class.getName()).logp(Level.FINE, FtpDownloader.class.getName(), "download()", "Input Stream is reply " + client.getReplyString());
            if (d.getStatus() == DownloadStatus.STOPPED) {
                return;
            }
            if (instream == null) {
                Logger.getLogger(FtpDownloader.class.getName()).logp(Level.SEVERE, FtpDownloader.class.getName(), "download()", "Input Stream is reply " + client.getReplyString());
                d.setStatus(DownloadStatus.ERROR, client.getReplyString());
                return;
            }
            d.setStatus(DownloadStatus.CONNECTED);
            d.setStatus(DownloadStatus.DOWNLOADING);
            downloader.runInput(instream, d);
            if (d.getStatus() == DownloadStatus.STOPPED) {
                return;
            }
            d.setStatus(DownloadStatus.COMPLETE);
            d.setSize(d.getDownloaded());

        } catch (SocketException ex) {
            d.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(FtpDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            d.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(FtpDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuntimeException ex) {
            d.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(FtpDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(FtpDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
