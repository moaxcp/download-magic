
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
    
    INITIALIZING("Initializing"),

    /**
     * Download is Preparing
     */
    PREPARING("Preparing"),

    /**
     * Download is being connected.
     */
    CONNECTING("Connecting"),

    /**
     * Download is being downloaded.
     */
    DOWNLOADING("Downloading"),
    
    FINALIZING("Finalizing"),

    /**
     * Download is complete.
     */
    COMPLETE("Complete"),

    /**
     * Download has been Redirecting
     */
    REDIRECTING("Redirecting"),

    /**
     * Download is retrying.
     */
    RETRYING("Retrying"),
    
    STOPPING("Stopping"),

    /**
     * download has been stopped.
     */
    STOPPED("Stopped"),
    /**
     * Download has encountered an error.
     */
    ERROR("Error"),
    SKIPPED("Skipped");

    
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
