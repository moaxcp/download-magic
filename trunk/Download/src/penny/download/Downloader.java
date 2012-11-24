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

    private AbstractDownload download;
    private List<DownloadProcessor> processors;
    private DownloadSettings dSettings;
    private Map<String, ProtocolClient> clients;

    public Downloader(DownloadSettings ds) {
        this.dSettings = ds;
        clients = new HashMap<String, ProtocolClient>();
        processors = new ArrayList<DownloadProcessor>();
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
    public AbstractDownload getDownload() {
        return download;
    }

    /**
     * @param download the download to set
     */
    public void setDownload(AbstractDownload download) {
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

    void resetProcessors(AbstractDownload d) {
        d.setDownloaded(0);
        for (DownloadProcessor i : getProcessors()) {
            if (d.getStatus() != DownloadStatus.INITIALIZING) {
                break;
            }
            i.onReset(d);
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINE, Downloader.class.getName(), "resetInterfaces", "reset interfaces for " + d, d);
    }

    void runInput(InputStream in, AbstractDownload d) throws IOException {
        Logger.getLogger(Downloader.class.getName()).entering(Downloader.class.getName(), "runInput");
        if (d.getStatus() == DownloadStatus.DOWNLOADING) {
            int read = -1;
            byte buffer[] = new byte[getdSettings().getBufferSize()];
            read = in.read(buffer);
            while (read != -1 && d.getStatus() == DownloadStatus.DOWNLOADING) {
                d.updateDownloadTime();
                d.setDownloaded(d.getDownloaded() + read);
                chunckProcessors(d, read, buffer);
                read = in.read(buffer);
            }
        }
        Logger.getLogger(Downloader.class.getName()).exiting(Downloader.class.getName(), "runInput");
    }

    private void initProcessors(AbstractDownload d) {
        for (DownloadProcessor i : getProcessors()) {
            if (d.getStatus() != DownloadStatus.INITIALIZING) {
                break;
            }
            i.onInit(d);
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINE, Downloader.class.getName(), "resetProcessors", "reset processors for " + d, d);
    }

    private void prepareInputProcessors(AbstractDownload d) {
        for (DownloadProcessor i : getProcessors()) {
            if (d.getStatus() != DownloadStatus.PREPARING) {
                break;
            }
            i.onPrepare(d);
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINE, Downloader.class.getName(), "startProcessors", "start processors for " + d, d);
    }

    private void finalizeProcessors(AbstractDownload d) {
        for (DownloadProcessor i : getProcessors()) {
            if (d.getStatus() != DownloadStatus.FINALIZING) {
                break;
            }
            i.onFinalize(d);
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINE, Downloader.class.getName(), "completeProcessors", "complete processors for " + d, d);
    }

    private void chunckProcessors(AbstractDownload d, int read, byte[] buffer) {
        for (DownloadProcessor i : getProcessors()) {
            if (d.getStatus() != DownloadStatus.DOWNLOADING) {
                break;
            }
            i.doChunck(d, read, buffer);
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINER, Downloader.class.getName(), "chunkProcessors", "chunck processors for " + d, d);
    }

    private boolean checkProcessors(AbstractDownload d) {
        boolean r = true;
        for (DownloadProcessor i : getProcessors()) {
            if (d.getStatus() != DownloadStatus.INITIALIZING) {
                break;
            }
            r = i.onCheck(d);
            if (r == false) {
                return r;
            }
        }
        Logger.getLogger(Downloader.class.getName()).logp(Level.FINE, Downloader.class.getName(), "checkProcessors", "check processors for " + d, d);
        return r;
    }

    private void startRetryTimer(AbstractDownload d) {
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

    private ProtocolClient getClient(AbstractDownload download) {
        ProtocolClient client = clients.get(download.getProtocol());
        if (client == null) {
            client = ProtocolClient.getClient(download.getProtocol(), dSettings);
            if (client != null) {
                clients.put(download.getProtocol(), client);
            } else {
                throw new IllegalArgumentException(download.getProtocol() + " does not have a ProtocolDownloader");
            }
        }
        return client;
    }

    public void shutdown() {
        for (ProtocolClient d : clients.values()) {
            d.shutdown();
        }
    }

    public void download() {
        for (int a = 1; a <= getdSettings().getMaxDownloadAttempts(); a++) {
            if (download.getStatus() == DownloadStatus.ERROR) {
                download.setStatus(DownloadStatus.RETRYING);
            }

            if (download.getStatus() == DownloadStatus.RETRYING) {
                download.initRetryTime();
                startRetryTimer(download);
            }

            if (download.getStatus() != DownloadStatus.QUEUED && download.getStatus() != DownloadStatus.RETRYING) {
                if (download.getStatus() == DownloadStatus.STOPPED) {
                    download.setStatus(DownloadStatus.STOPPED);
                    return;
                }
                continue;
            }

            download.setStatus(DownloadStatus.INITIALIZING);

            download.setAttempts(a);
            download.initDownloadTime();

            initProcessors(download);

            if (!checkProcessors(download)) {
                resetProcessors(download);
                download.setDownloaded(0);
                download.setDownloadTime(0);
            }

            ProtocolClient client = getClient(download);
            client.setDownload(download);

            for (int h = 0; h < dSettings.getMaxHops(); h++) {
                try {
                    if (download.getStatus() != DownloadStatus.INITIALIZING && download.getStatus() != DownloadStatus.REDIRECTING) {
                        if (download.getStatus() == DownloadStatus.STOPPED) {
                            download.setStatus(DownloadStatus.STOPPED);
                            return;
                        }
                        break;
                    }
                    download.setStatus(DownloadStatus.CONNECTING);

                    download.setHops(h);
                    client.connect();
                    if (client.isDataRestarting()) {
                        resetProcessors(download);
                    }

                    if (download.getStatus() == DownloadStatus.REDIRECTING) {
                        continue;
                    } else {
                        if (download.getStatus() != DownloadStatus.CONNECTING) {
                            if (download.getStatus() == DownloadStatus.STOPPED) {
                                download.setStatus(DownloadStatus.STOPPED);
                                return;
                            }
                            break;
                        }
                        download.setStatus(DownloadStatus.PREPARING);

                        prepareInputProcessors(download);

                        if (download.getStatus() != DownloadStatus.PREPARING) {
                            if (download.getStatus() == DownloadStatus.STOPPED) {
                                download.setStatus(DownloadStatus.STOPPED);
                                return;
                            }
                            break;
                        }
                        download.setStatus(DownloadStatus.DOWNLOADING);

                        InputStream in = client.getContent();
                        runInput(in, download);

                        break;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
                    download.setStatus(DownloadStatus.ERROR, ex.toString());
                } finally {
                    client.close();
                }
            }

            if (download.getStatus() != DownloadStatus.DOWNLOADING) {
                if (download.getStatus() == DownloadStatus.STOPPED) {
                    download.setStatus(DownloadStatus.STOPPED);
                    return;
                }
                continue;
            }
            download.setStatus(DownloadStatus.FINALIZING);

            finalizeProcessors(download);

            if (download.getStatus() != DownloadStatus.FINALIZING) {
                if (download.getStatus() == DownloadStatus.STOPPED) {
                    download.setStatus(DownloadStatus.STOPPED);
                }
                return;
            } else {
                download.setStatus(DownloadStatus.COMPLETE);
            }
        }
    }
}
