 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
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
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/**
 *
 * @author john
 */
class HttpClient extends ProtocolClient {

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
                request.addHeader("User-Agent", settings.getHttpUserAgent());
            }
        }
    }
    private DownloadSettings settings;
    private AbstractDownload download;
    private DefaultHttpClient httpClient;
    private DownloaderInterceptor interceptor;
    private InputStream content;
    private boolean restart;

    HttpClient(DownloadSettings settings) {
        this.settings = settings;
        httpClient = new DefaultHttpClient();
        interceptor = new DownloaderInterceptor();
        httpClient.addRequestInterceptor(interceptor);
        HttpParams params = httpClient.getParams();
        params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
        //params.setParameter(CoreProtocolPNames.USER_AGENT, settings.getHttpUserAgent());
        String proxys = settings.getHttpProxyServer();
        int port = settings.getHttpProxyPort();
        if (proxys != null && !proxys.equals("")) {
            HttpHost proxy;
            if (port > 0) {
                proxy = new HttpHost(settings.getHttpProxyServer(), settings.getHttpProxyPort());
            } else {
                proxy = new HttpHost(settings.getHttpProxyServer());
            }
            params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        //TODO add support for SOCKS

    }

    @Override
    void setDownload(AbstractDownload download) {
        interceptor.setDownload(download);
        this.download = download;
        content = null;
        restart = false;
    }

    @Override
    void connect() {
        try {
            HttpGet httpget = new HttpGet(download.getUrl().toURI());
            HttpResponse response = httpClient.execute(httpget);
            download.setResponseCode(response.getStatusLine().getStatusCode());

            if (response.getStatusLine().getStatusCode() / 100 == 3) {
                Header[] headers = response.getHeaders("Location");
                if (headers.length > 0 && headers[0] != null) {
                    download.addLocation(download.getUrl());
                    download.setUrl(new URL(headers[0].getValue()));
                    download.setStatus(DownloadStatus.REDIRECTING);
                    httpget.abort();
                } else {
                    download.setStatus(DownloadStatus.ERROR, "Could not find location for redirection");
                }
                return;
            }
            
            if(download.getResponseCode() / 100 != 2) {
                download.setStatus(DownloadStatus.ERROR, "response code is " + download.getResponseCode() + " could not download");
                httpget.abort();
                return;
            }

            boolean byteExists = false;
            for (Header h : response.getAllHeaders()) {
                if (h.getName().equals("Content-Type") && h.getValue() != null) {
                    download.setContentType(h.getValue());
                }
                if (h.getName().equals("Content-Length") && h.getValue() != null) {
                    download.setSize(Long.valueOf(h.getValue()));
                }
                if (h.getName().equals("Content-Range")) {
                    String value = h.getValue();
                    if (value.contains("bytes") && value.contains(Long.toString(download.getDownloaded()))) {
                        byteExists = true;
                    }
                }
                if (h.getName().equals("Content-Disposition")) {
                    for (HeaderElement he : h.getElements()) {
                        if (he.getName().equals("filename")) {
                            download.setProtocolFileName(he.getValue());
                        }
                    }
                }
            }

            //should restart
            if (!byteExists && download.getDownloaded() > 0) {
                restart = true;
            }
            
            content = response.getEntity().getContent();
            
        } catch (ClientProtocolException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    boolean isDataRestarting() {
        return restart;
    }

    @Override
    InputStream getContent() {
        return content;
    }

    @Override
    void close() {
        try {
            if(content != null) {
                content.close();
            }
        } catch (IOException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void shutdown() {
        httpClient.getConnectionManager().shutdown();
    }
}
