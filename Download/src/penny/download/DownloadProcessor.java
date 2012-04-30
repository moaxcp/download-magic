package penny.download;


/**
 *
 * @author john
 */
public interface DownloadProcessor {
    public void onInit(Download d);
    public boolean onCheck(Download d);
    public void onReset(Download d);
    public void onStartInput(Download d);
    public void doChunck(Download d, int read, byte[] buffer);
    public void onEndInput(Download d);
    public void onCompleted(Download d);
}
