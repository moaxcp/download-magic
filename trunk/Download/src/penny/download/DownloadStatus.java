
package penny.download;

/**
 * Used to indicate the status of a Download.
 * @author John J Mercier
 */
public enum DownloadStatus {
    /**
     * Download is Queued.
     */
    QUEUED("Queued"),

    /**
     * Download is Starting
     */
    STARTED("Started"),

    /**
     * Download is being connected.
     */
    CONNECTING("Connecting"),

    /**
     * Download has connected.
     */
    CONNECTED("Connected"),

    /**
     * Download is being downloaded.
     */
    DOWNLOADING("Downloading"),

    /**
     * Download will retry.
     */
    RETRY("Retry"),

    /**
     * Download is retrying.
     */
    RETRYING("Retrying"),

    /**
     * Download has been redirected
     */
    REDIRECTED("Redirected"),

    /**
     * download has been stopped.
     */
    STOPPED("Stopped"),
    /**
     * Download has encountered an error.
     */
    ERROR("Error"),

    /**
     * Download is complete.
     */
    COMPLETE("Complete");

    
    private String str;
    private DownloadStatus(String str) {
        this.str = str;
    }
    
    /**
     * Returns the String representation of this DownloadStatus.
     * @return the DownloadStatus.
     */
    @Override
    public String toString() {
        return str;
    }

    public static DownloadStatus getStatus(String s) {
        for(DownloadStatus d : DownloadStatus.values()) {
            if(d.toString().equals(s)) {
                return d;
            }
        }
        return null;
    }
}
