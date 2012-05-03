/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.download;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class Downloader {

    private Download download;
    private List<DownloadProcessor> processors;
    private DownloadSettings dSettings;
    private Map<String, ProtocolDownloader> downloaders;

    public Downloader() {
        processors = new ArrayList<DownloadProcessor>();
        download = new Download();
        dSettings = new DownloadSettings();
        downloaders = new HashMap<String, ProtocolDownloader>();
    }

    public Downloader(DownloadSettings ds, List<DownloadProcessor> processors) {
        this();
        this.dSettings = ds;
        this.processors = processors;
    }

    public void addProcessor(DownloadProcessor di) {
        this.getProcessors().add(di);
    }

    public void removeProcessor(DownloadProcessor di) {
        this.getProcessors().remove(di);
    }

    /**
     * @return the download
     */
    public Download getDownload() {
        return download;
    }

    /**
     * @param download the download to set
     */
    public void setDownload(Download download) {
        this.download = download;
    }

    /**
     * @return the dInterfaces
     */
    public List<DownloadProcessor> getProcessors() {
        return processors;
    }

    /**
     * @param processors the dInterfaces to set
     */
    public void setProcessors(List<DownloadProcessor> processors) {
        this.processors = processors;
    }

    /**
     * @return the dSettings
     */
    public DownloadSettings getdSettings() {
        return dSettings;
    }

    /**
     * @param dSettings the dSettings to set
     */
    public void setdSettings(DownloadSettings dSettings) {
        this.dSettings = dSettings;
    }

    void resetProcessors(Download d) {
        d.setDownloaded(0);
        for (DownloadProcessor i : getProcessors()) {
            i.onReset(d);
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINE, Downloader.class.getName(), "resetInterfaces", "reset interfaces for " + d, d);
    }

    void runInput(InputStream in, Download d) throws IOException {
        Logger.getLogger(Downloader.class.getName()).entering(Downloader.class.getName(), "runInput");
        startInputProcessors(d);
        int read = -1;
        byte buffer[] = new byte[getdSettings().getBufferSize()];
        read = in.read(buffer);
        while (read != -1 && d.getStatus() == DownloadStatus.DOWNLOADING) {
            d.updateDownloadTime();
            d.setDownloaded(d.getDownloaded() + read);
            chunckProcessors(d, read, buffer);
            read = in.read(buffer);
        }
        endInputProcessors(d);
        Logger.getLogger(Downloader.class.getName()).exiting(Downloader.class.getName(), "runInput");
    }

    private void initProcessors(Download d) {
        for (DownloadProcessor i : getProcessors()) {
            i.onInit(d);
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINE, Downloader.class.getName(), "resetProcessors", "reset processors for " + d, d);
    }

    private void startInputProcessors(Download d) {
        for (DownloadProcessor i : getProcessors()) {
            i.onStartInput(d);
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINE, Downloader.class.getName(), "startProcessors", "start processors for " + d, d);
    }

    private void endInputProcessors(Download d) {
        for (DownloadProcessor i : getProcessors()) {
            i.onEndInput(d);
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINE, Downloader.class.getName(), "stopProcessors", "stop processors for " + d, d);
    }

    private void completeProcessors(Download d) {
        for (DownloadProcessor i : getProcessors()) {
            i.onCompleted(d);
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINE, Downloader.class.getName(), "completeProcessors", "complete processors for " + d, d);
    }

    private void chunckProcessors(Download d, int read, byte[] buffer) {
        for (DownloadProcessor i : getProcessors()) {
            i.doChunck(d, read, buffer);
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINER, Downloader.class.getName(), "chunkProcessors", "chunck processors for " + d, d);
    }

    private boolean checkProcessors(Download d) {
        boolean r = true;
        for (DownloadProcessor i : getProcessors()) {
            r = i.onCheck(d);
            if (r == false) {
                return r;
            }
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINE, Downloader.class.getName(), "checkProcessors", "check processors for " + d, d);
        return r;
    }

    private void startRetryTimer(Download d) {
        d.setRetryTime(0);
        while (d.getRetryTime() < getdSettings().getMaxRetryTime() && d.getStatus() == DownloadStatus.RETRYING) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            }
            d.updateRetryTime();
        }
    }

    private ProtocolDownloader getDownloader(String protocol) {
        ProtocolDownloader d = downloaders.get(protocol);
        if (d == null) {
            d = ProtocolDownloader.getDownloader(protocol, this);
            if (d != null) {
                downloaders.put(protocol, d);
            } else {
                throw new IllegalArgumentException(protocol + " does not have a ProtocolDownloader");
            }
        }
        return d;
    }

    public void shutdown() {
        for (ProtocolDownloader d : downloaders.values()) {
            d.shutdown();
        }
    }

    public void download() {
        for (int a = 1; a <= getdSettings().getMaxDownloadAttempts(); a++) {
            if (download.getStatus() == DownloadStatus.RETRY) {
                download.setStatus(DownloadStatus.RETRYING);
                download.initRetryTime();
                startRetryTimer(download);
            } else if (download.getStatus() == DownloadStatus.STOPPED) {
                break;
            }

            download.setStatus(DownloadStatus.STARTED);

            download.setAttempts(a);
            download.initDownloadTime();

            initProcessors(download);
            if (!checkProcessors(download)) {
                resetProcessors(download);
                download.setDownloaded(0);
                download.setDownloadTime(0);
            }
            if (download.getStatus() != DownloadStatus.COMPLETE) {
                try {
                    getDownloader(download.getProtocol()).download(download);
                } catch (IllegalArgumentException e) {
                    download.setStatus(DownloadStatus.ERROR, e.toString());
                }
            }
            if (download.getStatus() == DownloadStatus.COMPLETE) {
                completeProcessors(download);
            }

            if (download.getStatus() == DownloadStatus.ERROR && download.getAttempts() < getdSettings().getMaxDownloadAttempts()) {
                download.setStatus(DownloadStatus.RETRY);
            }
            if (download.getStatus() != DownloadStatus.RETRY) {
                break;
            }
        }
    }
}
