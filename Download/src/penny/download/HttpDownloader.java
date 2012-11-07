/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/**
 *
 * @author john
 */
class HttpDownloader extends ProtocolDownloader {

    class DownloaderInterceptor implements HttpRequestInterceptor {

        private AbstractDownload download;

        public void setDownload(AbstractDownload d) {
            download = d;
        }

        public void process(
                final HttpRequest request,
                final HttpContext context) throws HttpException, IOException {
            if (!request.containsHeader("Range") && download.getDownloaded() > 0) {
                request.addHeader("Range", "bytes " + download.getDownloaded() + "-");
            }
            if (!request.containsHeader("User-Agent")) {
                request.addHeader("User-Agent", downloader.getdSettings().getHttpUserAgent());
            }
        }
    }
    private Downloader downloader;
    private DefaultHttpClient httpClient;
    private DownloaderInterceptor interceptor;

    HttpDownloader(Downloader downloader) {
        this.downloader = downloader;
        httpClient = new DefaultHttpClient();
        interceptor = new DownloaderInterceptor();
        httpClient.addRequestInterceptor(interceptor);
        HttpParams params = httpClient.getParams();
        params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
        params.setParameter(CoreProtocolPNames.USER_AGENT, downloader.getdSettings().getHttpUserAgent());
        String proxys = downloader.getdSettings().getHttpProxyServer();
        int port = downloader.getdSettings().getHttpProxyPort();
        if (proxys != null && !proxys.equals("")) {
            HttpHost proxy;
            if (port > 0) {
                proxy = new HttpHost(downloader.getdSettings().getHttpProxyServer(), downloader.getdSettings().getHttpProxyPort());
            } else {
                proxy = new HttpHost(downloader.getdSettings().getHttpProxyServer());
            }
            params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        //TODO add params for user agent in httpClient constructor
        //TODO do not follow redirects. add current url to locations and change url to new url
        //TODO add support for HTTP proxy through DEFAULT_PROXY PName and SOCKS proxy

    }

    void shutdown() {
        httpClient.getConnectionManager().shutdown();
    }

    void download(AbstractDownload d) {
        InputStream instream = null;
        HttpGet httpget = null;
        interceptor.setDownload(d);
        try {
            httpget = new HttpGet(d.getUrl().toURI());
            d.setStatus(DownloadStatus.CONNECTING);
            HttpResponse response = httpClient.execute(httpget);
            boolean restart = true;
            for (Header h : response.getAllHeaders()) {
                if (h.getName().equals("Content-Range")) {
                    String value = h.getValue();
                    if (value.contains("bytes") && value.contains(Long.toString(d.getDownloaded()))) {
                        restart = false;
                    }
                }
            }
            if (restart && d.getDownloaded() > 0) {
                downloader.resetProcessors(d);
            }
            HttpEntity entity = response.getEntity();
            d.setResponseCode(response.getStatusLine().getStatusCode());
            for (Header h : response.getAllHeaders()) {
                if (h.getName().equals("Content-Disposition")) {
                    for (HeaderElement he : h.getElements()) {
                        if (he.getName().equals("filename")) {
                            d.setProtocolFileName(he.getValue());
                        }
                    }
                }
            }

            if (entity != null) {
                instream = entity.getContent();
                if (d.getStatus() == DownloadStatus.STOPPED) {
                    return;
                }
                Header contentType = entity.getContentType();
                if (contentType != null) {
                    d.setContentType(contentType.getValue());
                }
                d.setSize(entity.getContentLength());
                d.setStatus(DownloadStatus.CONNECTED);
                d.setStatus(DownloadStatus.DOWNLOADING);
                downloader.runInput(instream, d);
                if (d.getStatus() == DownloadStatus.STOPPED) {
                    return;
                }
            }
            d.setStatus(DownloadStatus.COMPLETE);
            d.setSize(d.getDownloaded());
        } catch (URISyntaxException ex) {
            d.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(HttpDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClientProtocolException ex) {
            d.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(HttpDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            d.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(HttpDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuntimeException ex) {
            httpget.abort();
            d.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(HttpDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(HttpDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
