/*
 * Download.java
 *
 * Created on February 23, 2007, 8:11 AM
 * Updated on February 8, 2009, 7:46 PM
 */
package penny.download;

import java.beans.*;
import java.net.*;
import java.util.*;

/**
 * This class represents a Download. It is the base class for all other downloads.
 * This class and all classes that extend it should be used to track information
 * on a download only. All other logic such as getting connections and save files
 * should be implementend elseware. ex. DownloadConnectionFactory.
 *
 * Download can be used to update a table model with property change listeners.
 *
 * It can also be used as a model object to update the download.db which uses
 * DAO objects for different databases.
 *
 * It is important to understand how to update the two times stored in Download:
 * retryTime and downloadTime. Before each operation, for instance read() for
 * Download, initDownloadTime() must be called, then the operation, then
 * updateDownloadTime(). This should be done in a loop so the time is constantly
 * updated.
 *
 * It is also important to know that setting the status can cause an
 * IllegalStateException. Since this is a runtime exception make sure to look
 * for it.
 * @author John J Mercier
 */
public class Download implements Comparable<Download> {

    private static final long serialVersionUID = -7323355147711248756L;
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
     * Property string for fileExtention.
     */
    public static final String PROP_FILEEXTENTION = "fileExtention";
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
    /**
     * Property string for url.
     */
    public static final String PROP_URL = "url";

    /**
     * The URL of this Download.
     */
    protected URL url;

    /**
     * The total size of this Download.
     */
    protected long size;

    /**
     * How much has been downloaded so far.
     */
    protected long downloaded;

    /**
     * The DownloadStatus of this Download.
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
     * The number of attempts for this Download.
     */
    protected int attempts;

    /**
     * The content type of this Download.
     */
    protected String contentType;

    /**
     * A general message to be displayed when specific events happen.
     */
    protected String message;
    
    /**
     * The host of the URL for this Download.
     */
    protected String host;

    /**
     * The protocol of the URL for this Download.
     */
    protected String protocol;

    /**
     * The query of the URL for this Download.
     */
    protected String query;

    /**
     * The path of the URL for this Download.
     */
    protected String path;

    /**
     * The file of the URL for this Download.
     */
    protected String file;

    /**
     * The file name return by protocol (may not be same as url)
     */
    protected String protocolFileName;

    /**
     * The file extention of the URL for this Download.
     */
    protected String fileExtention;

    /**
     * The set of URLs the Download has been redirected from. In HTTP when a
     * connection is supposed to be redirected the Download url will change and
     * locations will store the previous urls.
     */
    protected List<URL> locations;
    public static final String PROP_LOCATIONS = "locations";

    /**
     * The HTTP response code recieved when connecting to the url in this
     * Download.
     */
    protected int responseCode;
    public static final String PROP_RESPONSECODE = "responseCode";


    /**
     * propertySupport.
     */
    protected PropertyChangeSupport propertySupport;

    /**
     * Creates an empty Download object. The initial status is QUEUED.
     */
    public Download() {
        propertySupport = new PropertyChangeSupport(this);
        this.setFile("");
        this.setProtocolFileName("");
        this.setFileExtention("");
        this.setHost("");
        this.setPath("");
        this.setProtocol("");
        this.setQuery("");
        this.setSize(-1);
        this.setDownloaded(0);
        this.status = DownloadStatus.QUEUED;
        this.setAttempts(0);
        this.setContentType("");
        this.setDownloadStartTime(0);
        this.setDownloadTime(0);
        this.setMessage("");
        this.setRetryStartTime(0);
        this.setRetryTime(0);
        locations = new ArrayList<URL>();
    }
    
    public Download(URL url) {
        this();
        this.setUrl(url);
        this.setFile(Downloads.getFileName(url.getPath()));
        this.setPath(Downloads.getPath(url.getPath()));
        this.setFileExtention(Downloads.getFileExtention(url.getPath()));
        this.setQuery(url.getQuery());
        if(this.query == null) {
            this.query = "";
        }
        this.setProtocol(url.getProtocol());
        if(this.protocol == null) {
            this.protocol = "";
        }
    }

    /**
     * Sets the url of this Download. Setting the url will also set the file,
     * file extention, host, path, protocol, and query.
     * @param url
     */
    public void setUrl(URL url) {
        URL oldValue = getUrl();
        this.url = url;
        propertySupport.firePropertyChange(PROP_URL, oldValue, getUrl());
        setFile(Downloads.getFileName(url.getPath()));
        if(getFile() != null) {
            setFileExtention(Downloads.getFileExtention(getFile()));
        }
        setHost(url.getHost());
        setPath(Downloads.getPath(url.getPath()));
        setProtocol(url.getProtocol());
        setQuery(url.getQuery());
    }

    /**
     * returns the url of this Download.
     * @return The url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Sets the number of attempts for this Download.
     * @param attempts
     */
    public void setAttempts(int attempts) {
        int oldValue = getAttempts();
        this.attempts = attempts;
        propertySupport.firePropertyChange(PROP_ATTEMPTS, oldValue, getAttempts());
    }

    /**
     * returns the number of attempts for this Download.
     * @return
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * Sets the total size of the Download.
     * @param size
     */
    public void setSize(long size) {
        long oldValue = getSize();
        this.size = size;
        propertySupport.firePropertyChange(PROP_SIZE, oldValue, getSize());
    }

    /**
     * Returns the total size of the Download.
     * @return the size in bytes
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the bytes downloaded for this Download.
     * @param downloaded
     */
    public void setDownloaded(long downloaded) {
        long oldValue = getDownloaded();
        this.downloaded = downloaded;
        propertySupport.firePropertyChange(PROP_DOWNLOADED, oldValue, getDownloaded());
    }

    /**
     * returns the number of bytes downloaded for this Download.
     * @return bytes downloaded
     */
    public long getDownloaded() {
        return downloaded;
    }

    public void queue() {
        setStatus(DownloadStatus.QUEUED);
    }

    public void stop() {
        setStatus(DownloadStatus.STOPPED);
    }

    public void complete() {
        setStatus(DownloadStatus.COMPLETE);
    }

    public void error() {
        setStatus(DownloadStatus.ERROR);
    }

    /**
     * Sets the status and a message for the status.
     * @param status the status of the download
     * @param message the message
     */
    public void setStatus(DownloadStatus status, String message) {
        setStatus(status);
        setMessage(message);
    }

    /**
     * Sets the status of this Download. If an attempt is made to set the
     * Download to the wrong status an IllegalStateException is thrown.
     * @param status
     */
    void setStatus(DownloadStatus status) {
        DownloadStatus oldValue = getStatus();
        switch (status) {
            case QUEUED:
                if(!(oldValue == DownloadStatus.ERROR || oldValue == DownloadStatus.STOPPED || oldValue == DownloadStatus.QUEUED)) {
                    throw new IllegalStateException("Download status must be ERROR or STOPPED before setting to QUEUED. oldValue=" + oldValue);
                }
                break;
            case STARTED:
                if(!(oldValue == DownloadStatus.QUEUED || oldValue == DownloadStatus.RETRYING)) {
                    throw new IllegalStateException("Download status must be QUEUED or RETRYING before setting to STARTED. oldValue=" + oldValue);
                }
                break;
            case CONNECTING:
                if(!(oldValue == DownloadStatus.STARTED || oldValue == DownloadStatus.REDIRECTED)) {
                    throw new IllegalStateException("Download status must be STARTING before setting to CONNECTING. oldValue=" + oldValue);
                }
                break;
            case CONNECTED:
                if(!(oldValue == DownloadStatus.CONNECTING)) {
                    throw new IllegalStateException("Download status must be CONNECTING before setting to CONNECTED. oldValue=" + oldValue);
                }
                break;
            case DOWNLOADING:
                if(!(oldValue == DownloadStatus.CONNECTED)) {
                    throw new IllegalStateException("Download status must be CONNECTED before setting to DOWNLOADING. oldValue=" + oldValue);
                }
                break;
            case STOPPED:
                break;
            case ERROR:
                if(oldValue == DownloadStatus.COMPLETE) {
                    throw new IllegalStateException("Download status cannot be COMPLETE before setting to ERROR. oldValue=" + oldValue);
                }
                break;
            case RETRY:
                if(!(oldValue == DownloadStatus.ERROR || oldValue == DownloadStatus.REDIRECTED)) {
                    throw new IllegalStateException("Download status must be ERROR or REDIRECTED before setting to RETRY. oldValue=" + oldValue);
                }
                break;
            case RETRYING:
                if(!(oldValue == DownloadStatus.RETRY)) {
                    throw new IllegalStateException("Download status must be RETRY before setting to RETRYING. oldValue=" + oldValue);
                }
                break;
            case REDIRECTED:
                if(!(oldValue == DownloadStatus.CONNECTING)) {
                    throw new IllegalStateException("Download status must be CONNECTING before setting to REDIRECTED. oldValue=" + oldValue);
                }
                break;
            case COMPLETE:
                if(!(oldValue == DownloadStatus.DOWNLOADING || oldValue == DownloadStatus.QUEUED)) {
                    throw new IllegalStateException("Download status must be DOWNLOADING before setting to COMPLETE. oldValue=" + oldValue);
                }
                break;
        }
        this.setMessage("");
        this.status = status;
        propertySupport.firePropertyChange(PROP_STATUS, oldValue, getStatus());
    }

    /**
     * Returns the status of this Download.
     * @return
     */
    public DownloadStatus getStatus() {
        return status;
    }

    /**
     * returns the download start time.
     * @return
     */
    protected long getDownloadStartTime() {
        return downloadStartTime;
    }

    /**
     * sets the download start time.
     * @param downloadStartTime
     */
    protected void setDownloadStartTime(long downloadStartTime) {
        this.downloadStartTime = downloadStartTime;
    }

    /**
     * Sets the total time spent downloading this Download.
     * @param downloadTime
     */
    public void setDownloadTime(long downloadTime) {
        long oldValue = this.downloadTime;
        this.downloadTime = downloadTime;
        propertySupport.firePropertyChange(PROP_DOWNLOADTIME, oldValue, downloadTime);
    }

    /**
     * Returns the total time spent downloading this Download.
     * @return
     */
    public long getDownloadTime() {
        return downloadTime;
    }

    /**
     * Sets the current retry start time for this Download.
     * @param retryStartTime
     */
    protected void setRetryStartTime(long retryStartTime) {
        this.retryStartTime = retryStartTime;
    }

    /**
     * returns the current retry start time for this Download.
     * @return
     */
    protected long getRetryStartTime() {
        return retryStartTime;
    }

    /**
     * Returns the retry time for this download.
     * @return
     */
    public long getRetryTime() {
        return retryTime;
    }

    /**
     * Sets the retry time for this download.
     * @param retryTime
     */
    protected void setRetryTime(long retryTime) {
        long oldValue = getRetryTime();
        this.retryTime = retryTime;
        propertySupport.firePropertyChange(PROP_RETRYTIME, oldValue, getRetryTime());
    }

    /**
     * Returns the content type for this Download.
     * @return
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the content type for this Download.
     * @param contentType
     */
    public void setContentType(String contentType) {
        String oldValue = getContentType();
        this.contentType = contentType;
        propertySupport.firePropertyChange(PROP_CONTENTTYPE, oldValue, getContentType());
    }

    /**
     * Returns the message for this Download.
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message for this Download.
     * @param message
     */
    public void setMessage(String message) {
        String oldValue = getMessage();
        this.message = message;
        propertySupport.firePropertyChange(PROP_MESSAGE, oldValue, getMessage());
    }

    /**
     * Gets the host for this Download.
     * @return String host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host for this Download.
     * @param host
     */
    protected void setHost(String host) {
        String oldValue = getHost();
        this.host = host;
        propertySupport.firePropertyChange(PROP_HOST, oldValue, getHost());
    }

    /**
     * Returns the protocol for this Download.
     * @return
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the Protocol for this Download.
     * @param protocol
     */
    protected void setProtocol(String protocol) {
        String oldValue = getProtocol();
        this.protocol = protocol;
        propertySupport.firePropertyChange(PROP_PROTOCOL, oldValue, getProtocol());
    }

    /**
     * Returns the Query for this Download.
     * @return
     */
    public String getQuery() {
        return query;
    }

    /**
     * Sets the query for this Download.
     * @param query
     */
    protected void setQuery(String query) {
        String oldValue = getQuery();
        this.query = query;
        propertySupport.firePropertyChange(PROP_QUERY, oldValue, getQuery());
    }

    /**
     * Returns the path for this Download. This does not include the file name.
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path for this Download.
     * @param path
     */
    protected void setPath(String path) {
        String oldValue = getPath();
        this.path = path;
        propertySupport.firePropertyChange(PROP_PATH, oldValue, getPath());
    }

    /**
     * Gets the file name for this Download. This does not include the path.
     * @return
     */
    public String getFile() {
        return file;
    }

    /**
     * Sets the file name for this Download. This does not include the path.
     * @param file
     */
    protected void setFile(String file) {
        String oldValue = getFile();
        this.file = file;
        propertySupport.firePropertyChange(PROP_FILE, oldValue, getFile());
    }

    /**
     * Gets the protocol file name for this Download. This does not include the path.
     * @return
     */
    public String getProtocolFileName() {
        return protocolFileName;
    }

    /**
     * Sets the protocol file name for this Download. This does not include the path.
     * @param file
     */
    protected void setProtocolFileName(String file) {
        String oldValue = getProtocolFileName();
        this.protocolFileName = file;
        propertySupport.firePropertyChange(PROP_FILE, oldValue, getProtocolFileName());
    }

    /**
     * Returns the file extention for this Download.
     * @return
     */
    public String getFileExtention() {
        return fileExtention;
    }

    /**
     * Sets the file Extention for this Download.
     * @param fileExtention
     */
    protected void setFileExtention(String fileExtention) {
        String oldValue = getFileExtention();
        this.fileExtention = fileExtention;
        propertySupport.firePropertyChange(PROP_FILEEXTENTION, oldValue, getFileExtention());
    }

    /**
     * Returns the locations where this Download has been redirected from.
     * @return
     */
    public List<URL> getLocations() {
        return locations;
    }

    /**
     * Add a location to the list of locations this download has been redirected
     * from
     * @param location
     */
    public void addLocation(URL location) {
        int index = locations.size();
        locations.add(location);
        propertySupport.fireIndexedPropertyChange(PROP_LOCATIONS, index, null, location);
    }

    /**
     * Returns the response code recieved when connecting to this Download.
     * @return
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the response code recieved when connecting to this Download.
     * @param responseCode
     */
    public void setResponseCode(int responseCode) {
        int oldValue = this.responseCode;
        this.responseCode = responseCode;
        propertySupport.firePropertyChange(PROP_RESPONSECODE, oldValue, getResponseCode());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Download && ((Download)obj).getUrl().equals(this.getUrl())) {
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
        this.setDownloadTime(0);
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

    /**
     * adds a PropertyChangeListener to the download
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * removes a PropertyChangeListener from this download
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return url.toString();
    }

    public int compareTo(Download o) {
        return getUrl().toString().compareTo(o.getUrl().toString());
    }
}