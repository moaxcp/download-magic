/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import penny.download.AbstractDownload;
import penny.download.DownloadProcessor;
import penny.download.DownloadSettings;
import penny.download.Downloader;

/**
 *
 * @author john
 */
public class DownloaderTest implements DownloadProcessor, PropertyChangeListener {
    
    List<String> http;
    List<String> ftp;
    List<String> file;
    List<String> paths;
    Map<String, String> md5s;
    
    public DownloaderTest() {

        paths = new ArrayList<String>();
        paths.add("/DownloadManagerTestSite/downloads/DSC00761.JPG");
        paths.add("/DownloadManagerTestSite/downloads/DSC00786.JPG");
        paths.add("/DownloadManagerTestSite/downloads/DSC00785.JPG");
        paths.add("/DownloadManagerTestSite/downloads/DSC00760.JPG");
        paths.add("/DownloadManagerTestSite/downloads/AutoCompleteSupportTableColumn.wmv");
        paths.add("/DownloadManagerTestSite/downloads/tango-icon-theme-0.8.90.tar.gz");
        paths.add("/DownloadManagerTestSite/downloads/DSC00726.JPG");
        paths.add("/DownloadManagerTestSite/downloads/google-chrome-stable_current_amd64.deb");
        paths.add("/DownloadManagerTestSite/downloads/Non-Zero%20Possibility.m4a");
        paths.add("/DownloadManagerTestSite/downloads/jhepwork-2.9.zip");
        paths.add("/DownloadManagerTestSite/downloads/DSC00725.JPG");
        paths.add("/DownloadManagerTestSite/downloads/saga_gui.cfg");
        paths.add("/DownloadManagerTestSite/downloads/DSC00727.JPG");
        paths.add("/DownloadManagerTestSite/downloads/DSC00762.JPG");
        paths.add("/DownloadManagerTestSite/downloads/DSC00759.JPG");
        paths.add("/DownloadManagerTestSite/downloads/DSC00758.JPG");
        paths.add("/DownloadManagerTestSite/downloads/binding.pdf");

        ftp = new ArrayList<String>();
        http = new ArrayList<String>();
        for(String s : paths) {
            http.add("http://localhost:8081" + s);
            ftp.add("ftp://localhost" + s);
            file.add("file:///home/john/NetBeansProjects" + s);
        }

    }

    public void go() throws MalformedURLException {
        DownloadSettings ds = new DownloadSettings();
        Downloader downloader = new Downloader(ds);
        downloader.addProcessor(this);
        for(String url : http) {
            class Download extends AbstractDownload {
                Download(URL url) {
                    setUrl(url);
                }
            }
            AbstractDownload d = new Download(new URL(url));
            d.addPropertyChangeListener(this);
            downloader.setDownload(d);
            downloader.download();
            d.removePropertyChangeListener(this);
        }
    }

    public static void main(String... args) throws MalformedURLException {
        DownloaderTest test = new DownloaderTest();
        test.go();
    }

    public void onInit(AbstractDownload d) {
        System.out.println("init " + d);
    }

    public boolean onCheck(AbstractDownload d) {
        System.out.println("onCheck " + d);
        return true;
    }

    public void onReset(AbstractDownload d) {
        System.out.println("onReset " + d);
    }

    public void onStartInput(AbstractDownload d) {
        System.out.println("onStarted " + d);
    }

    public void doChunck(AbstractDownload d, int read, byte[] buffer) {
        //System.out.println("doChunck " + d);
    }

    public void onEndInput(AbstractDownload d) {
        System.out.println("onStopped " + d);
    }

    public void onCompleted(AbstractDownload d) {
        System.out.println("onCompleted " + d);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(AbstractDownload.PROP_STATUS)) {
            AbstractDownload d = (AbstractDownload) evt.getSource();
            System.out.println(d.getStatus() + " " + d.getUrl());
        } else if(evt.getPropertyName().equals(AbstractDownload.PROP_MESSAGE)) {
            AbstractDownload d = (AbstractDownload) evt.getSource();
            System.out.println("Message: " + d.getMessage());
        }
    }

}
