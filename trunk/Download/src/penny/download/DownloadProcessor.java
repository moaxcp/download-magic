package penny.download;


/**
 *
 * @author john
 */
public interface DownloadProcessor {
    //initializing
    public void onInit(AbstractDownload d);
    public void onReset();
    //preparing
    public void onPrepare();
    //Downloading
    public void doChunck(int read, byte[] buffer);
    //finalizing
    public void onFinalize();
}
