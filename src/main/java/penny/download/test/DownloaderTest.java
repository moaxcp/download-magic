/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.download.test;

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
import penny.download.Downloads;

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
    long percent;
    
    AbstractDownload d;

    public DownloaderTest() {

        paths = new ArrayList<String>();
        paths.add("/DownloadManagerTestSite/downloads/AutoCompleteSupportTableColumn.wmv");
        paths.add("/DownloadManagerTestSite/downloads/jhepwork-2.9.zip");
        paths.add("/DownloadManagerTestSite/downloads/flower.html");
        paths.add("/DownloadManagerTestSite/downloads/index.html");
        paths.add("/DownloadManagerTestSite/downloads/tango-icon-theme-0.8.90.tar.gz");
        paths.add("/DownloadManagerTestSite/downloads/Non-Zero%20Possibility.m4a");
        paths.add("/DownloadManagerTestSite/downloads/DSC00725.JPG");
        paths.add("/DownloadManagerTestSite/downloads/javascript.js");
        paths.add("/DownloadManagerTestSite/downloads/saga_gui.cfg");
        paths.add("/DownloadManagerTestSite/downloads/google-chrome-stable_current_amd64.deb");
        paths.add("/DownloadManagerTestSite/downloads/binding.pdf");

        ftp = new ArrayList<String>();
        http = new ArrayList<String>();
        file = new ArrayList<String>();
        for (String s : paths) {
            http.add("http://localhost:8084" + s);
            ftp.add("ftp://anonymous@localhost" + s);
            file.add("file:///home/john/NetBeansProjects" + s);
        }

    }

    public void go() throws MalformedURLException {
        DownloadSettings ds = new DownloadSettings();
        Downloader downloader = new Downloader(ds);
        downloader.setProcessor(this);
        for (String url : http) {
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
        for (String url : ftp) {
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
        this.d = d;
        System.out.println("init " + d);
        percent = 0;
    }

    public boolean onCheck() {
        System.out.println("onCheck " + d.getStatus());
        return true;
    }

    public void onReset() {
        System.out.println("onReset " + d.getStatus());
    }

    public void onPrepare() {
        System.out.println("onPrepare " + d.getStatus() + " size " + d.getSize());
    }

    public void doChunck(int read, byte[] buffer) {
        //System.out.println("doChunck " + d.getStatus() + " " + Downloads.getProgress(d) + "%");
    }

    public void onFinalize() {
        System.out.println("onFinalize " + d.getStatus());
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(AbstractDownload.PROP_STATUS)) {
            AbstractDownload d = (AbstractDownload) evt.getSource();
            System.out.println(d.getStatus() + " " + d.getUrl());
        } else if (evt.getPropertyName().equals(AbstractDownload.PROP_MESSAGE)) {
            AbstractDownload d = (AbstractDownload) evt.getSource();
            System.out.println("Message: " + d.getMessage());
        }
    }
}
