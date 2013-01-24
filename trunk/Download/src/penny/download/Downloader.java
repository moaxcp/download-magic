/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.download;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import penny.util.StopWatch;

/**
 *
 * @author john
 */
public class Downloader {

    private AbstractDownload download;
    private DownloadProcessor processor;
    private DownloadSettings dSettings;
    private Map<String, ProtocolClient> clients;

    public Downloader(DownloadSettings ds) {
        this.dSettings = ds;
        clients = new HashMap<>();
    }

    public void setProcessor(DownloadProcessor processor) {
        this.processor = processor;
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

    void runInput(InputStream in, AbstractDownload d) throws IOException {
        Logger.getLogger(Downloader.class.getName()).entering(Downloader.class.getName(), "runInput");
        if (d.getStatus() == DownloadStatus.DOWNLOADING) {
            int read = -1;
            StopWatch bufferWatch = new StopWatch();
            ByteArrayList timeBuffer = new ByteArrayList(128);
            byte buffer[] = new byte[128];
            bufferWatch.start();
            read = in.read(buffer);
            while (read != -1 && d.getStatus() == DownloadStatus.DOWNLOADING) {
                timeBuffer.addElements(timeBuffer.size(), buffer, 0, read);
                bufferWatch.add();
                if (bufferWatch.getTimeMillis() >= getdSettings().getBufferTime()) {
                    d.updateDownloadTime();
                    processor.doChunck(timeBuffer.size(), timeBuffer.toByteArray(buffer));
                    d.setDownloaded(d.getDownloaded() + timeBuffer.size());
                    bufferWatch.restart();
                    timeBuffer.clear();
                }
                read = in.read(buffer);
            }
            if (timeBuffer.size() > 0) {
                d.updateDownloadTime();
                d.setDownloaded(d.getDownloaded() + timeBuffer.size());
                processor.doChunck(timeBuffer.size(), timeBuffer.toByteArray(buffer));
            }
        }
        Logger.getLogger(Downloader.class.getName()).exiting(Downloader.class.getName(), "runInput");
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
                if (download.getStatus() == DownloadStatus.STOPPING) {
                    download.setStatus(DownloadStatus.STOPPED);
                    return;
                }
                continue;
            }

            download.setStatus(DownloadStatus.INITIALIZING);
            download.setAttempts(a);
            download.initDownloadTime();

            processor.onInit(download);

            if (download.getSize() > 0 ? download.getDownloaded() < download.getSize() : true) {
                ProtocolClient client = getClient(download);
                client.setDownload(download);

                for (int h = 0; h < dSettings.getMaxHops(); h++) {
                    try {
                        if (download.getStatus() != DownloadStatus.INITIALIZING && download.getStatus() != DownloadStatus.REDIRECTING) {
                            if (download.getStatus() == DownloadStatus.STOPPING) {
                                download.setStatus(DownloadStatus.STOPPED);
                                return;
                            }
                            break;
                        }
                        download.setStatus(DownloadStatus.CONNECTING);

                        download.setHops(h);
                        client.connect();
                        if (client.isDataRestarting()) {
                            processor.onReset();
                        }

                        if (download.getStatus() == DownloadStatus.REDIRECTING) {
                            continue;
                        } else {
                            if (download.getStatus() != DownloadStatus.CONNECTING) {
                                if (download.getStatus() == DownloadStatus.STOPPING) {
                                    download.setStatus(DownloadStatus.STOPPED);
                                    return;
                                }
                                break;
                            }
                            download.setStatus(DownloadStatus.PREPARING);

                            processor.onPrepare();

                            if (download.getStatus() != DownloadStatus.PREPARING) {
                                if (download.getStatus() == DownloadStatus.STOPPING) {
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
            }
            
            if (download.getStatus() != DownloadStatus.DOWNLOADING && download.getStatus() != DownloadStatus.INITIALIZING) {
                if (download.getStatus() == DownloadStatus.STOPPING) {
                    download.setStatus(DownloadStatus.STOPPED);
                    return;
                }
                continue;
            }
            download.setStatus(DownloadStatus.FINALIZING);

            processor.onFinalize();

            if (download.getStatus() != DownloadStatus.FINALIZING) {
                if (download.getStatus() == DownloadStatus.STOPPING) {
                    download.setStatus(DownloadStatus.STOPPED);
                }
                return;
            } else {
                download.setStatus(DownloadStatus.COMPLETE);
            }
        }
    }
}
