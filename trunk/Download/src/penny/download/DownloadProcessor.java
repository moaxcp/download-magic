package penny.download;


/**
 *
 * @author john
 */
public interface DownloadProcessor {
    //initializing
    public void onInit(AbstractDownload d);
    public boolean onCheck(AbstractDownload d);
    public void onReset(AbstractDownload d);
    //preparing
    public void onPrepare(AbstractDownload d);
    //Downloading
    public void doChunck(AbstractDownload d, int read, byte[] buffer);
    //finalizing
    public void onFinalize(AbstractDownload d);
}
