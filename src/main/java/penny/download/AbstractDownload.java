/*
 * AbstractDownload.java
 *
 * Created on February 23, 2007, 8:11 AM
 * Updated on February 8, 2009, 7:46 PM
 */
package penny.download;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.*;

/**
 * This class represents a AbstractDownload. It is the base class for all other
 * downloads. This class and all classes that extend it should be used to track
 * information on a download only. All other logic such as getting connections
 * and save files should be implemented elsewhere. ex.
 * DownloadConnectionFactory.
 *
 * AbstractDownload can be used to update a table model with property change
 * listeners.
 *
 * It can also be used as a model object to update the download.db which uses
 * DAO objects for different databases.
 *
 * It is important to understand how to update the two times stored in
 * AbstractDownload: retryTime and downloadTime. Before each operation, for
 * instance read() for AbstractDownload, initDownloadTime() must be called, then
 * the operation, then updateDownloadTime(). This should be done in a loop so
 * the time is constantly updated.
 *
 * It is also important to know that setting the status can cause an
 * IllegalStateException. Since this is a runtime exception make sure to look
 * for it.
 *
 * @author John J Mercier
 */
public abstract class AbstractDownload {

    public static final String FILE = "file";
    /**
     * FTP protocol
     */
    public static final String FTP = "ftp";
    /**
     * HTTP protocol
     */
    public static final String HTTP = "http";
    /**
     * HTTPS protocol
     */
    public static final String HTTPS = "https";
    /**
     * Property string for attempts.
     */
    public static final String PROP_ATTEMPTS = "attempts";
    /**
     * Property string for hops.
     */
    public static final String PROP_HOPS = "hops";
    /**
     * Property string for contentType.
     */
    public static final String PROP_CONTENTTYPE = "contentType";
    /**
     * Property string for downloaded.
     */
    public static final String PROP_DOWNLOADED = "downloaded";
    /**
     * Property string for downloadTime.
     */
    public static final String PROP_DOWNLOADTIME = "downloadTime";
    /**
     * Property string for file.
     */
    public static final String PROP_FILE = "file";
    /**
     * Property string for downloadFile
     */
    public static final String PROP_PROTOCOLFILENAME = "protocolFileName";
    /**
     * Property string for fileExtension.
     */
    public static final String PROP_FILEEXSENTION = "fileExtension";
    /**
     * Property string for host.
     */
    public static final String PROP_HOST = "host";
    /**
     * Property string for message.
     */
    public static final String PROP_MESSAGE = "message";
    /**
     * Property string for path.
     */
    public static final String PROP_PATH = "path";
    /**
     * Property string for protocol.
     */
    public static final String PROP_PROTOCOL = "protocol";
    /**
     * Property string for query.
     */
    public static final String PROP_QUERY = "query";
    /**
     * Property string for retryTime.
     */
    public static final String PROP_RETRYTIME = "retryTime";
    /**
     * Property string for size.
     */
    public static final String PROP_SIZE = "size";
    /**
     * Property string for status.
     */
    public static final String PROP_STATUS = "downloadStatus";
    public static final String PROP_CANQUEUE = "canQueue";
    public static final String PROP_CANSTOP = "canStop";
    /**
     * Property string for url.
     */
    public static final String PROP_URL = "url";
    public static final String PROP_PROGRESS = "progress";
    public static final String PROP_BYTESPERSECOND = "rate";
    public static final String PROP_TIMELEFT = "timeLeft";
    /**
     * The URL of this AbstractDownload.
     */
    protected URL url;
    /**
     * The total size of this AbstractDownload.
     */
    protected long size;
    /**
     * How much has been downloaded so far.
     */
    protected long downloaded;
    /**
     * The DownloadStatus of this AbstractDownload.
     */
    protected DownloadStatus status;
    /**
     * The total time spent downloading.
     */
    protected long downloadTime;
    /**
     * The time the download has been recently started
     */
    protected long downloadStartTime;
    /**
     * The time since the retry delay started.
     */
    protected long retryStartTime;
    /**
     * The total time spent waiting to retry.
     */
    protected long retryTime;
    /**
     * The number of attempts for this AbstractDownload.
     */
    protected int attempts;
    /**
     * The number of hops for this AbstractDownload.
     */
    protected int hops;
    /**
     * The content type of this AbstractDownload.
     */
    protected String contentType;
    /**
     * A general message to be displayed when specific events happen.
     */
    protected String message;
    /**
     * The host of the URL for this AbstractDownload.
     */
    protected String host;
    /**
     * The protocol of the URL for this AbstractDownload.
     */
    protected String protocol;
    /**
     * The query of the URL for this AbstractDownload.
     */
    protected String query;
    /**
     * The path of the URL for this AbstractDownload.
     */
    protected String path;
    /**
     * The file of the URL for this AbstractDownload.
     */
    protected String file;
    /**
     * The file name return by protocol (may not be same as url)
     */
    protected String protocolFileName;
    /**
     * The file extention of the URL for this AbstractDownload.
     */
    protected String fileExtension;
    /**
     * The set of URLs the AbstractDownload has been redirected from. In HTTP
     * when a connection is supposed to be redirected the AbstractDownload url
     * will change and locations will store the previous urls.
     */
    protected List<String> locations;
    public static final String PROP_LOCATIONS = "locations";
    /**
     * The HTTP response code received when connecting to the url in this
     * AbstractDownload.
     */
    protected int responseCode;
    public static final String PROP_RESPONSECODE = "responseCode";
    /**
     * propertySupport.
     */
    protected PropertyChangeSupport propertySupport;

    /**
     * Creates an empty AbstractDownload object. The initial status is QUEUED.
     */
    public AbstractDownload() {
        propertySupport = new PropertyChangeSupport(this);
        file = "";
        protocol = "";
        protocolFileName = "";
        fileExtension = "";
        host = "";
        path = "";
        protocol = "";
        query = "";
        size = -1;
        downloaded = 0;
        status = DownloadStatus.QUEUED;
        attempts = 0;
        hops = -1;
        contentType = "";
        downloadStartTime = 0;
        downloadTime = 0;
        message = "";
        retryStartTime = 0;
        retryTime = 0;
        locations = new ArrayList<String>();
    }

    public AbstractDownload(URL url) {
        this();
        this.setUrl(url);
    }

    /**
     * Sets the url of this AbstractDownload. Setting the url will also set the
     * file, file extention, host, path, protocol, and query.
     *
     * @param url
     */
    public void setUrl(URL url) {
        URL oldValue = getUrl();
        this.url = url;
        propertySupport.firePropertyChange(PROP_URL, oldValue, getUrl());
        setFile(Downloads.getFileName(url.getPath()));
        if (getFile() != null) {
            setFileExtension(Downloads.getFileExtension(getFile()));
        }
        setHost(url.getHost());
        setPath(Downloads.getPath(url.getPath()));

        if (url.getQuery() == null) {
            setQuery("");
        } else {
            setQuery(url.getQuery());
        }

        if (url.getProtocol() == null) {
            setProtocol("");
        } else {
            setProtocol(url.getProtocol());
        }
    }

    /**
     * returns the url of this AbstractDownload.
     *
     * @return The url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Sets the number of attempts for this AbstractDownload.
     *
     * @param attempts
     */
    public void setAttempts(int attempts) {
        int oldValue = getAttempts();
        this.attempts = attempts;
        propertySupport.firePropertyChange(PROP_ATTEMPTS, oldValue, getAttempts());
    }

    /**
     * returns the number of attempts for this AbstractDownload.
     *
     * @return
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * Sets the number of hops for this AbstractDownload.
     *
     * @param hops
     */
    public void setHops(int hops) {
        int oldValue = this.hops;
        this.hops = hops;
        propertySupport.firePropertyChange(PROP_HOPS, oldValue, this.hops);
    }

    /**
     * returns the number of hops for this AbstractDownload.
     *
     * @return
     */
    public int getHops() {
        return hops;
    }

    /**
     * Sets the total size of the AbstractDownload.
     *
     * @param size
     */
    public void setSize(long size) {
        long oldValue = getSize();
        this.size = size;
        propertySupport.firePropertyChange(PROP_SIZE, oldValue, getSize());
    }

    /**
     * Returns the total size of the AbstractDownload.
     *
     * @return the size in bytes
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the bytes downloaded for this AbstractDownload.
     *
     * @param downloaded
     */
    public void setDownloaded(long downloaded) {
        long oldValue = getDownloaded();
        this.downloaded = downloaded;
        propertySupport.firePropertyChange(PROP_DOWNLOADED, oldValue, getDownloaded());
    }

    /**
     * returns the number of bytes downloaded for this AbstractDownload.
     *
     * @return bytes downloaded
     */
    public long getDownloaded() {
        return downloaded;
    }

    public boolean isCanQueue() {
        return status == DownloadStatus.STOPPED || status == DownloadStatus.ERROR || status == DownloadStatus.COMPLETE;
    }

    public void queue() {
        setStatus(DownloadStatus.QUEUED);
    }

    public boolean isCanStop() {
        return status == DownloadStatus.PREPARING || status == DownloadStatus.RETRYING || status == DownloadStatus.CONNECTING || status == DownloadStatus.DOWNLOADING || status == DownloadStatus.FINALIZING;
    }

    public void stop() {
        setStatus(DownloadStatus.STOPPING);
    }

    void complete() {
        status = DownloadStatus.COMPLETE;
    }

    void error() {
        status = DownloadStatus.ERROR;
    }

    /**
     * Sets the status and a message for the status.
     *
     * @param status the status of the download
     * @param message the message
     */
    public void setStatus(DownloadStatus status, String message) {
        setStatus(status);
        setMessage(message);
    }

    /**
     * Sets the status of this AbstractDownload. If an attempt is made to set
     * the AbstractDownload to the wrong status an IllegalStateException is
     * thrown.
     *
     * @param status
     */
    void setStatus(DownloadStatus status) {
        DownloadStatus oldValue = getStatus();
        switch (status) {
            case QUEUED:
                if (!(oldValue == DownloadStatus.ERROR || oldValue == DownloadStatus.STOPPED || oldValue == DownloadStatus.SKIPPED)) {
                    throw new IllegalStateException("Download status must be ERROR, STOPPED, or SKIPPED before setting to QUEUED. status=" + oldValue);
                }
                break;
            case INITIALIZING:
                if (!(oldValue == DownloadStatus.QUEUED || oldValue == DownloadStatus.RETRYING)) {
                    throw new IllegalStateException("Download status must be QUEUED or RETRYING before setting to INITIALIZING. status=" + oldValue);
                }
                break;
            case CONNECTING:
                if (!(oldValue == DownloadStatus.INITIALIZING || oldValue == DownloadStatus.REDIRECTING)) {
                    throw new IllegalStateException("Download status must be INITIALIZING or REDIRECTING before setting to CONNECTING. status=" + oldValue);
                }
                break;
            case PREPARING:
                if (!(oldValue == DownloadStatus.CONNECTING)) {
                    throw new IllegalStateException("Download status must be CONNECTING before setting to PREPARING. status=" + oldValue);
                }
                break;
            case DOWNLOADING:
                if (!(oldValue == DownloadStatus.PREPARING)) {
                    throw new IllegalStateException("Download status must be PREPARING before setting to DOWNLOADING. status=" + oldValue);
                }
                break;
            case FINALIZING:
                if (!(oldValue == DownloadStatus.DOWNLOADING || oldValue == DownloadStatus.INITIALIZING)) {
                    throw new IllegalStateException("Download status must be DOWNLOADING or INITIALIZING before setting to FINALIZING. status=" + oldValue);
                }
                break;
            case COMPLETE:
                if (!(oldValue == DownloadStatus.FINALIZING)) {
                    throw new IllegalStateException("Download status must be FINALIZING before setting to COMPLETE. status=" + oldValue);
                }
                break;
            case STOPPING:
                if (!(oldValue == DownloadStatus.INITIALIZING || oldValue == DownloadStatus.PREPARING || oldValue == DownloadStatus.CONNECTING || oldValue == DownloadStatus.DOWNLOADING || oldValue == DownloadStatus.FINALIZING || oldValue == DownloadStatus.RETRYING)) {
                    throw new IllegalStateException("Download status must be INITIALIZING, PREPARING, CONNECTING, DOWNLOADING, FINALIZING, or RETRYING before setting to STOPPING. status=" + oldValue);
                }
                break;
            case STOPPED:
                if (!(oldValue == DownloadStatus.STOPPING)) {
                    throw new IllegalStateException("Download status must be STOPPING before setting to STOPPED. status=" + oldValue);
                }
                break;
            case ERROR:
                if (!(oldValue == DownloadStatus.INITIALIZING || oldValue == DownloadStatus.PREPARING || oldValue == DownloadStatus.CONNECTING || oldValue == DownloadStatus.DOWNLOADING || oldValue == DownloadStatus.FINALIZING || oldValue == DownloadStatus.STOPPING)) {
                    throw new IllegalStateException("Download status must be STOPPING, INITIALIZING, CONNECTING, DOWNLOADING, or FINALIZING before setting to ERROR. status=" + oldValue);
                }
                break;
            case RETRYING:
                if (!(oldValue == DownloadStatus.ERROR)) {
                    throw new IllegalStateException("Download status must be ERROR before setting to RETRYING. status=" + oldValue);
                }
                break;
            case REDIRECTING:
                if (!(oldValue == DownloadStatus.CONNECTING)) {
                    throw new IllegalStateException("Download status must be CONNECTING before setting to REDIRECTING. status=" + oldValue);
                }
                break;
            case SKIPPED:
                if (!(oldValue == DownloadStatus.QUEUED)) {
                    throw new IllegalStateException("Download status must be QUEUED before setting to SKIPPED. status=" + oldValue);
                }
                break;
        }
        this.setMessage("");
        this.status = status;
        propertySupport.firePropertyChange(PROP_STATUS, oldValue, getStatus());

        if (isCanStop()) {
            propertySupport.firePropertyChange(PROP_CANSTOP, false, true);
        } else {
            propertySupport.firePropertyChange(PROP_CANSTOP, true, false);
        }
        if (isCanQueue()) {
            propertySupport.firePropertyChange(PROP_CANQUEUE, false, true);
        } else {
            propertySupport.firePropertyChange(PROP_CANQUEUE, true, false);
        }
    }

    /**
     * Returns the status of this AbstractDownload.
     *
     * @return
     */
    public DownloadStatus getStatus() {
        return status;
    }

    /**
     * returns the download start time.
     *
     * @return
     */
    protected long getDownloadStartTime() {
        return downloadStartTime;
    }

    /**
     * sets the download start time.
     *
     * @param downloadStartTime
     */
    protected void setDownloadStartTime(long downloadStartTime) {
        this.downloadStartTime = downloadStartTime;
    }

    /**
     * Sets the total time spent downloading this AbstractDownload.
     *
     * @param downloadTime
     */
    public void setDownloadTime(long downloadTime) {
        long oldValue = this.downloadTime;
        this.downloadTime = downloadTime;
        propertySupport.firePropertyChange(PROP_DOWNLOADTIME, oldValue, downloadTime);
    }

    /**
     * Returns the total time spent downloading this AbstractDownload.
     *
     * @return
     */
    public long getDownloadTime() {
        return downloadTime;
    }

    /**
     * Sets the current retry start time for this AbstractDownload.
     *
     * @param retryStartTime
     */
    protected void setRetryStartTime(long retryStartTime) {
        this.retryStartTime = retryStartTime;
    }

    /**
     * returns the current retry start time for this AbstractDownload.
     *
     * @return
     */
    protected long getRetryStartTime() {
        return retryStartTime;
    }

    /**
     * Returns the retry time for this download.
     *
     * @return
     */
    public long getRetryTime() {
        return retryTime;
    }

    /**
     * Sets the retry time for this download.
     *
     * @param retryTime
     */
    protected void setRetryTime(long retryTime) {
        long oldValue = getRetryTime();
        this.retryTime = retryTime;
        propertySupport.firePropertyChange(PROP_RETRYTIME, oldValue, getRetryTime());
    }

    /**
     * Returns the content type for this AbstractDownload.
     *
     * @return
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the content type for this AbstractDownload.
     *
     * @param contentType
     */
    public void setContentType(String contentType) {
        String oldValue = getContentType();
        this.contentType = contentType;
        propertySupport.firePropertyChange(PROP_CONTENTTYPE, oldValue, getContentType());
    }

    /**
     * Returns the message for this AbstractDownload.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message for this AbstractDownload.
     *
     * @param message
     */
    public void setMessage(String message) {
        String oldValue = getMessage();
        this.message = message;
        propertySupport.firePropertyChange(PROP_MESSAGE, oldValue, getMessage());
    }

    /**
     * Gets the host for this AbstractDownload.
     *
     * @return String host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host for this AbstractDownload.
     *
     * @param host
     */
    protected void setHost(String host) {
        String oldValue = getHost();
        this.host = host;
        propertySupport.firePropertyChange(PROP_HOST, oldValue, getHost());
    }

    /**
     * Returns the protocol for this AbstractDownload.
     *
     * @return
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the Protocol for this AbstractDownload.
     *
     * @param protocol
     */
    protected void setProtocol(String protocol) {
        String oldValue = getProtocol();
        this.protocol = protocol;
        propertySupport.firePropertyChange(PROP_PROTOCOL, oldValue, getProtocol());
    }

    /**
     * Returns the Query for this AbstractDownload.
     *
     * @return
     */
    public String getQuery() {
        return query;
    }

    /**
     * Sets the query for this AbstractDownload.
     *
     * @param query
     */
    protected void setQuery(String query) {
        String oldValue = getQuery();
        this.query = query;
        propertySupport.firePropertyChange(PROP_QUERY, oldValue, getQuery());
    }

    /**
     * Returns the path for this AbstractDownload. This does not include the
     * file name.
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path for this AbstractDownload.
     *
     * @param path
     */
    protected void setPath(String path) {
        String oldValue = getPath();
        this.path = path;
        propertySupport.firePropertyChange(PROP_PATH, oldValue, getPath());
    }

    /**
     * Gets the file name for this AbstractDownload. This does not include the
     * path.
     *
     * @return
     */
    public String getFile() {
        return file;
    }

    /**
     * Sets the file name for this AbstractDownload. This does not include the
     * path.
     *
     * @param file
     */
    protected void setFile(String file) {
        String oldValue = getFile();
        this.file = file;
        propertySupport.firePropertyChange(PROP_FILE, oldValue, getFile());
    }

    /**
     * Gets the protocol file name for this AbstractDownload. This does not
     * include the path.
     *
     * @return
     */
    public String getProtocolFileName() {
        return protocolFileName;
    }

    /**
     * Sets the protocol file name for this AbstractDownload. This does not
     * include the path.
     *
     * @param file
     */
    protected void setProtocolFileName(String file) {
        String oldValue = getProtocolFileName();
        this.protocolFileName = file;
        propertySupport.firePropertyChange(PROP_FILE, oldValue, getProtocolFileName());
    }

    /**
     * Returns the file extention for this AbstractDownload.
     *
     * @return
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * Sets the file Extention for this AbstractDownload.
     *
     * @param fileExtension
     */
    protected void setFileExtension(String fileExtension) {
        String oldValue = getFileExtension();
        this.fileExtension = fileExtension;
        propertySupport.firePropertyChange(PROP_FILEEXSENTION, oldValue, getFileExtension());
    }

    /**
     * Returns the locations where this AbstractDownload has been redirected
     * from.
     *
     * @return
     */
    public List<String> getLocations() {
        return locations;
    }

    /**
     * Add a location to the list of locations this download has been redirected
     * from
     *
     * @param location
     */
    public void addLocation(String location) {
        int index = locations.size();
        locations.add(location);
        propertySupport.fireIndexedPropertyChange(PROP_LOCATIONS, index, null, location);
    }

    /**
     * Returns the response code received when connecting to this
     * AbstractDownload.
     *
     * @return
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the response code received when connecting to this AbstractDownload.
     *
     * @param responseCode
     */
    public void setResponseCode(int responseCode) {
        int oldValue = this.responseCode;
        this.responseCode = responseCode;
        propertySupport.firePropertyChange(PROP_RESPONSECODE, oldValue, getResponseCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractDownload && ((AbstractDownload) obj).getUrl().equals(this.getUrl())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.url != null ? this.url.hashCode() : 0);
        return hash;
    }

    /**
     * initializes the download time. Use this before a read() operation.
     */
    public void initDownloadTime() {
        this.setDownloadStartTime(System.nanoTime());
    }

    /**
     * updates the download time. Use this after a read() operation.
     */
    public void updateDownloadTime() {
        long oldValue = getDownloadTime();
        this.setDownloadTime((System.nanoTime() - this.getDownloadStartTime()) + this.getDownloadTime());
        this.setDownloadStartTime(System.nanoTime());
        propertySupport.firePropertyChange(PROP_DOWNLOADTIME, oldValue, getDownloadTime());
    }

    /**
     * initializes the retry time. Use this before a sleep() operation.
     */
    public void initRetryTime() {
        this.setRetryStartTime(System.nanoTime());
        this.setRetryTime(0);
    }

    /**
     * updates the Retry Time. Use this after a sleep() operation.
     */
    public void updateRetryTime() {
        long oldValue = getRetryTime();
        this.setRetryTime((System.nanoTime() - this.getRetryStartTime()) + this.getRetryTime());
        this.setRetryStartTime(System.nanoTime());
        propertySupport.firePropertyChange(PROP_RETRYTIME, oldValue, getRetryTime());
    }

    public long getTimeLeft() {
        double rate = (getDownloadTime() == 0 ? 0 : getDownloaded() / (double) getDownloadTime());
        return (long) (rate == 0 ? 0 : (getSize() - getDownloaded()) / rate);
    }

    public double getProgress() {
        if (getSize() >= 0) {
            return ((double) getDownloaded()) / (double) getSize();
        } else {
            return 0;
        }
    }

    public long getBytesPerSecond() {
        return (long)(getDownloadTime() == 0 ? 0 : (getDownloaded() / (double)getDownloadTime()) * (double)1000000000);
    }

    @Override
    public String toString() {
        return url.toString();
    }

    /**
     * adds a PropertyChangeListener to the download
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * removes a PropertyChangeListener from this download
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListeners() {
        PropertyChangeListener[] listeners = propertySupport.getPropertyChangeListeners();
        for (PropertyChangeListener l : listeners) {
            removePropertyChangeListener(l);
        }
    }
}
