package penny.download;


/**
 *
 * @author john
 */
public interface DownloadProcessor {
    public void onInit(AbstractDownload d);
    public boolean onCheck(AbstractDownload d);
    public void onReset(AbstractDownload d);
    public void onStartInput(AbstractDownload d);
    public void doChunck(AbstractDownload d, int read, byte[] buffer);
    public void onEndInput(AbstractDownload d);
    public void onCompleted(AbstractDownload d);
}
