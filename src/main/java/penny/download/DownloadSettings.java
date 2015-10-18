package penny.download;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * DownloadSettings contain the proxy settings and all the settings needed to
 * make a DownloadTask function.
 * @author moaxcp
 */
public class DownloadSettings implements Serializable {

    private static final long serialVersionUID = 3283208393421769221L;

    public static String PROP_AUTOFOLLOWREDIRECTS = "autoFollowRedirects";

    private boolean autoFollowRedirects;

    public static String PROP_MAXHOPS = "maxHops";
    private int maxHops;

    public static String PROP_MAXRETRYTIME = "maxRetryTime";
    private long maxRetryTime;

    public static String PROP_MAXDOWNLOADATTEMPTS = "maxDownloadAttempts";
    private int maxDownloadAttempts;


    public static String PROP_BUFFERTIME = "bufferTime";
    private int bufferTime;

    public static String PROP_DOWNLOADPROXYSERVER = "downloadProxyServer";
    private String downloadProxyServer;

    public static String PROP_DOWNLOADPROXYPORT = "downloadProxyPort";
    private int downloadProxyPort;

    public static String PROP_DOWNLOADCONNECTTIMEOUT = "downloadConnectTimeout";
    private int downloadConnectTimeout;

    public static String PROP_DOWNLOADREADTIMEOUT = "downloadReadTimeout";
    private int downloadReadTimeout;

    public static String PROP_FTPPROXYSERVER = "ftpProxyServer";
    private String ftpProxyServer;

    public static String PROP_FTPPROXYPORT = "ftpProxyPort";
    private int ftpProxyPort;

    public static String PROP_FTPCONNECTTIMEOUT = "ftpConnectTimeout";
    private int ftpConnectTimeout;

    public static String PROP_FTPREADTIMEOUT = "ftpReadTimeout";
    private int ftpReadTimeout;

    public static String PROP_HTTPPROXYSERVER = "httpProxyServer";
    private String httpProxyServer;

    public static String PROP_HTTPPROXYPORT = "httpProxyPort";
    private int httpProxyPort;

    public static String PROP_HTTPCONNECTTIMEOUT = "httpConnectTimeout";
    private int httpConnectTimeout;

    public static String PROP_HTTPREADTIMEOUT = "httpReadTimeout";
    private int httpReadTimeout;

    public static String PROP_HTTPUSERAGENT = "httpUserAgent";
    private String httpUserAgent;

    public static String PROP_HTTPSPROXYSERVER = "httpsProxyServer";
    private String httpsProxyServer;

    public static String PROP_HTTPSPROXYPORT = "httpsProxyPort";
    private int httpsProxyPort;

    public static String PROP_HTTPSCONNECTTIMEOUT = "httpsConnectTimeout";
    private int httpsConnectTimeout;

    public static String PROP_HTTPSREADTIMEOUT = "httpsReadTimeout";
    private int httpsReadTimeout;

    public static String PROP_HTTPSUSERAGENT = "httpsUserAgent";
    private String httpsUserAgent;

    public static String PROP_HTTPSTRUSTMANAGERS = "httpsTrustManagers";
    transient private TrustManager[] httpsTrustManagers;

    public static String PROP_SOCKSPROXYSERVER = "socksProxyServer";
    private String socksProxyServer;

    public static String PROP_SOCKSPROXYPORT = "socksProxyPort";
    private int socksProxyPort;

    public static String PROP_SOCKSCONNECTTIMEOUT = "socksConnectTimeout";
    private int socksConnectTimeout;

    public static String PROP_SOCKSREADTIMEOUT = "socksReadTimeout";
    private int socksReadTimeout;

    transient protected PropertyChangeSupport propertySupport;

    /**
     * Creates a default and reasonable DownloadSettings object.
     */
    public DownloadSettings() {
        autoFollowRedirects = true;
        maxHops = 3;
        this.maxRetryTime = 5000000000l;
        this.maxDownloadAttempts = 3;
        this.bufferTime = 1000 / 3;
        downloadProxyServer = null;
        downloadProxyPort = -1;
        downloadConnectTimeout = 10000;
        downloadReadTimeout = 10000;
        ftpProxyServer = null;
        ftpProxyPort = -1;
        ftpConnectTimeout = 10000;
        ftpReadTimeout = 10000;
        httpProxyServer = null;
        httpProxyPort = -1;
        httpConnectTimeout = 10000;
        httpReadTimeout = 10000;
        httpUserAgent = "project-penny Download Manager (http://code.google.com/p/project-penny/)";
        httpsProxyServer = null;
        httpsProxyPort = -1;
        httpsConnectTimeout = 10000;
        httpsReadTimeout = 10000;
        httpsUserAgent = httpUserAgent;
        httpsTrustManagers = new TrustManager[]{
                        new X509TrustManager() {

                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }

                            public void checkClientTrusted(
                                    java.security.cert.X509Certificate[] certs, String authType) {
                            }

                            public void checkServerTrusted(
                                    java.security.cert.X509Certificate[] certs, String authType) {
                            }
                        }
                    };
        setPropertySupport(new PropertyChangeSupport(this));
    }

    public synchronized void copy(DownloadSettings dSettings) {
        this.setAutoFollowRedirects(dSettings.isAutoFollowRedirects());
        this.setBufferTime(dSettings.getBufferTime());
        this.setDownloadConnectTimeout(dSettings.getDownloadConnectTimeout());
        this.setDownloadProxyPort(dSettings.getDownloadProxyPort());
        this.setDownloadProxyServer(dSettings.getDownloadProxyServer());
        this.setDownloadReadTimeout(dSettings.getDownloadReadTimeout());
        this.setFtpConnectTimeout(dSettings.getFtpConnectTimeout());
        this.setFtpProxyPort(dSettings.getFtpProxyPort());
        this.setFtpProxyServer(dSettings.getFtpProxyServer());
        this.setFtpReadTimeout(dSettings.getFtpReadTimeout());
        this.setHttpUserAgent(dSettings.getHttpUserAgent());
        this.setHttpConnectTimeout(dSettings.getHttpConnectTimeout());
        this.setHttpProxyPort(dSettings.getHttpProxyPort());
        this.setHttpProxyServer(dSettings.getHttpProxyServer());
        this.setHttpReadTimeout(dSettings.getHttpReadTimeout());
        this.setHttpsUserAgent(dSettings.getHttpsUserAgent());
        this.setHttpsConnectTimeout(dSettings.getHttpsConnectTimeout());
        this.setHttpsReadTimeout(dSettings.getHttpsReadTimeout());
        this.setHttpsProxyServer(dSettings.getHttpsProxyServer());
        this.setHttpsProxyPort(dSettings.getHttpProxyPort());
        this.setHttpsTrustManagers(dSettings.getHttpsTrustManagers());
        this.setMaxDownloadAttempts(dSettings.getMaxDownloadAttempts());
        this.setMaxHops(dSettings.getMaxHops());
        this.setMaxRetryTime(dSettings.getMaxRetryTime());
    }

    public synchronized void setPropertySupport(PropertyChangeSupport propertySupport) {
        this.propertySupport = propertySupport;
    }

    /**
     * @return the maxRetryTime
     */
    public synchronized long getMaxRetryTime() {
        return maxRetryTime;
    }

    /**
     * @param maxRetryTime the maxRetryTime to set
     */
    public synchronized void setMaxRetryTime(long maxRetryTime) {
        long oldValue = this.maxRetryTime;
        this.maxRetryTime = maxRetryTime;
        propertySupport.firePropertyChange(PROP_MAXRETRYTIME, oldValue, maxRetryTime);
    }

    /**
     * @return the maxDownloadAttempts
     */
    public synchronized int getMaxDownloadAttempts() {
        return maxDownloadAttempts;
    }

    /**
     * @param maxDownloadAttempts the maxDownloadAttempts to set
     */
    public synchronized void setMaxDownloadAttempts(int maxDownloadAttempts) {
        int oldValue = this.maxDownloadAttempts;
        this.maxDownloadAttempts = maxDownloadAttempts;
        propertySupport.firePropertyChange(PROP_MAXDOWNLOADATTEMPTS, oldValue, maxDownloadAttempts);
    }

    /**
     * @return the bufferSize
     */
    public synchronized int getBufferTime() {
        return bufferTime;
    }

    /**
     * @param bufferSize the bufferSize to set
     */
    public synchronized void setBufferTime(int bufferTime) {
        int oldValue = this.bufferTime;
        this.bufferTime = bufferTime;
        propertySupport.firePropertyChange(DownloadSettings.PROP_BUFFERTIME, oldValue, bufferTime);
    }

    /**
     *
     * @return
     */
    public synchronized String getDownloadProxyServer() {
        return downloadProxyServer;
    }

    /**
     *
     * @param downloadProxyServer
     */
    public synchronized void setDownloadProxyServer(String downloadProxyServer) {
        String oldValue = this.downloadProxyServer;
        this.downloadProxyServer = downloadProxyServer;
        propertySupport.firePropertyChange(DownloadSettings.PROP_DOWNLOADPROXYSERVER, oldValue, downloadProxyServer);
    }

    /**
     *
     * @return
     */
    public synchronized int getDownloadProxyPort() {
        return downloadProxyPort;
    }

    /**
     *
     * @param downloadProxyPort
     */
    public synchronized void setDownloadProxyPort(int downloadProxyPort) {
        int oldValue = this.downloadProxyPort;
        this.downloadProxyPort = downloadProxyPort;
        propertySupport.firePropertyChange(DownloadSettings.PROP_DOWNLOADPROXYPORT, oldValue, downloadProxyPort);
    }

    /**
     *
     * @return
     */
    public synchronized int getDownloadConnectTimeout() {
        return downloadConnectTimeout;
    }

    /**
     *
     * @param downloadConnectTimeout
     */
    public synchronized void setDownloadConnectTimeout(int downloadConnectTimeout) {
        int oldValue = this.downloadConnectTimeout;
        this.downloadConnectTimeout = downloadConnectTimeout;
        propertySupport.firePropertyChange(DownloadSettings.PROP_DOWNLOADCONNECTTIMEOUT, oldValue, downloadConnectTimeout);
    }

    /**
     *
     * @return
     */
    public synchronized int getDownloadReadTimeout() {
        return downloadReadTimeout;
    }

    /**
     *
     * @param downloadReadTimeout
     */
    public synchronized void setDownloadReadTimeout(int downloadReadTimeout) {
        int oldValue = this.downloadReadTimeout;
        this.downloadReadTimeout = downloadReadTimeout;
        propertySupport.firePropertyChange(DownloadSettings.PROP_DOWNLOADREADTIMEOUT, oldValue, downloadReadTimeout);
    }

    /**
     *
     * @return
     */
    public synchronized String getFtpProxyServer() {
        return ftpProxyServer;
    }

    /**
     *
     * @param ftpProxyServer
     */
    public synchronized void setFtpProxyServer(String ftpProxyServer) {
        String oldValue = this.ftpProxyServer;
        this.ftpProxyServer = ftpProxyServer;
        propertySupport.firePropertyChange(DownloadSettings.PROP_FTPPROXYSERVER, oldValue, ftpProxyServer);
    }

    /**
     *
     * @return
     */
    public synchronized int getFtpProxyPort() {
        return ftpProxyPort;
    }

    /**
     *
     * @param ftpProxyPort
     */
    public synchronized void setFtpProxyPort(int ftpProxyPort) {
        int oldValue = this.ftpProxyPort;
        this.ftpProxyPort = ftpProxyPort;
        propertySupport.firePropertyChange(DownloadSettings.PROP_FTPPROXYPORT, oldValue, ftpProxyPort);
    }

    /**
     *
     * @return
     */
    public synchronized int getFtpConnectTimeout() {
        return ftpConnectTimeout;
    }

    /**
     *
     * @param ftpConnectTimeout
     */
    public synchronized void setFtpConnectTimeout(int ftpConnectTimeout) {
        int oldValue = this.ftpConnectTimeout;
        this.ftpConnectTimeout = ftpConnectTimeout;
        propertySupport.firePropertyChange(DownloadSettings.PROP_FTPCONNECTTIMEOUT, oldValue, ftpConnectTimeout);
    }

    /**
     *
     * @return
     */
    public synchronized int getFtpReadTimeout() {
        return ftpReadTimeout;
    }

    /**
     *
     * @param ftpReadTimeout
     */
    public synchronized void setFtpReadTimeout(int ftpReadTimeout) {
        int oldValue = this.ftpReadTimeout;
        this.ftpReadTimeout = ftpReadTimeout;
        propertySupport.firePropertyChange(DownloadSettings.PROP_FTPREADTIMEOUT, oldValue, ftpReadTimeout);
    }

    /**
     *
     * @return
     */
    public synchronized String getHttpProxyServer() {
        return httpProxyServer;
    }

    /**
     *
     * @param httpProxyServer
     */
    public synchronized void setHttpProxyServer(String httpProxyServer) {
        String oldValue = this.httpProxyServer;
        this.httpProxyServer = httpProxyServer;
        propertySupport.firePropertyChange(DownloadSettings.PROP_HTTPPROXYSERVER, oldValue, httpProxyServer);
    }

    /**
     *
     * @return
     */
    public synchronized int getHttpProxyPort() {
        return httpProxyPort;
    }

    /**
     *
     * @param httpProxyPort
     */
    public synchronized void setHttpProxyPort(int httpProxyPort) {
        int oldValue = this.httpProxyPort;
        this.httpProxyPort = httpProxyPort;
        propertySupport.firePropertyChange(DownloadSettings.PROP_HTTPPROXYPORT, oldValue, httpProxyPort);
    }

    /**
     *
     * @return
     */
    public synchronized int getHttpConnectTimeout() {
        return httpConnectTimeout;
    }

    /**
     *
     * @param httpConnectTimeout
     */
    public synchronized void setHttpConnectTimeout(int httpConnectTimeout) {
        int oldValue = this.httpConnectTimeout;
        this.httpConnectTimeout = httpConnectTimeout;
        propertySupport.firePropertyChange(DownloadSettings.PROP_HTTPCONNECTTIMEOUT, oldValue, httpConnectTimeout);
    }

    /**
     *
     * @return
     */
    public synchronized int getHttpReadTimeout() {
        return httpReadTimeout;
    }

    /**
     *
     * @param httpReadTimeout
     */
    public synchronized void setHttpReadTimeout(int httpReadTimeout) {
        int oldValue = this.httpReadTimeout;
        this.httpReadTimeout = httpReadTimeout;
        propertySupport.firePropertyChange(DownloadSettings.PROP_HTTPREADTIMEOUT, oldValue, httpReadTimeout);
    }

    /**
     *
     * @return
     */
    public synchronized String getHttpUserAgent() {
        return httpUserAgent;
    }

    /**
     *
     * @param httpUserAgent
     */
    public synchronized void setHttpUserAgent(String httpUserAgent) {
        String oldValue = this.httpUserAgent;
        this.httpUserAgent = httpUserAgent;
        propertySupport.firePropertyChange(DownloadSettings.PROP_HTTPUSERAGENT, oldValue, httpUserAgent);
    }

    /**
     *
     * @return
     */
    public synchronized String getHttpsProxyServer() {
        return httpsProxyServer;
    }

    /**
     *
     * @param httpsProxyServer
     */
    public synchronized void setHttpsProxyServer(String httpsProxyServer) {
        String oldValue = this.httpsProxyServer;
        this.httpsProxyServer = httpsProxyServer;
        propertySupport.firePropertyChange(DownloadSettings.PROP_HTTPSPROXYSERVER, oldValue, httpsProxyServer);
    }

    /**
     *
     * @return
     */
    public synchronized int getHttpsProxyPort() {
        return httpsProxyPort;
    }

    /**
     * Sets the httpsProxyPort. This is used when making connections.
     * @param httpsProxyPort
     */
    public synchronized void setHttpsProxyPort(int httpsProxyPort) {
        int oldValue = this.httpsProxyPort;
        this.httpsProxyPort = httpsProxyPort;
        propertySupport.firePropertyChange(DownloadSettings.PROP_HTTPSPROXYPORT, oldValue, httpsProxyPort);
    }

    /**
     * Returns the httpsConnectionTimeout. This is used when making connections.
     * @return
     */
    public synchronized int getHttpsConnectTimeout() {
        return httpsConnectTimeout;
    }

    /**
     * Sets the httpConnectionTimeout. This is used when making connections.
     * @param httpsConnectTimeout
     */
    public synchronized void setHttpsConnectTimeout(int httpsConnectTimeout) {
        int oldValue = this.httpsConnectTimeout;
        this.httpsConnectTimeout = httpsConnectTimeout;
        propertySupport.firePropertyChange(DownloadSettings.PROP_HTTPSCONNECTTIMEOUT, oldValue, httpsConnectTimeout);
    }

    /**
     * returns the httpsReadTimeout.
     * @return
     */
    public synchronized int getHttpsReadTimeout() {
        return httpsReadTimeout;
    }

    /**
     * Sets the httpsReadTimeout.
     * @param httpsReadTimeout
     */
    public synchronized void setHttpsReadTimeout(int httpsReadTimeout) {
        int oldValue = this.httpsReadTimeout;
        this.httpsReadTimeout = httpsReadTimeout;
        propertySupport.firePropertyChange(DownloadSettings.PROP_HTTPSREADTIMEOUT, oldValue, httpsReadTimeout);
    }

    /**
     *
     * @return
     */
    public synchronized String getSocksProxyServer() {
        return socksProxyServer;
    }

    /**
     *
     * @param httpsProxyServer
     */
    public synchronized void setSocksProxyServer(String socksProxyServer) {
        String oldValue = this.socksProxyServer;
        this.socksProxyServer = socksProxyServer;
        propertySupport.firePropertyChange(DownloadSettings.PROP_SOCKSPROXYSERVER, oldValue, socksProxyServer);
    }

    /**
     *
     * @return
     */
    public synchronized int getSocksProxyPort() {
        return socksProxyPort;
    }

    /**
     * Sets the httpsProxyPort. This is used when making connections.
     * @param httpsProxyPort
     */
    public synchronized void setSocksProxyPort(int socksProxyPort) {
        int oldValue = this.socksProxyPort;
        this.socksProxyPort = socksProxyPort;
        propertySupport.firePropertyChange(DownloadSettings.PROP_SOCKSPROXYPORT, oldValue, socksProxyPort);
    }

    /**
     * Returns the httpsConnectionTimeout. This is used when making connections.
     * @return
     */
    public synchronized int getSocksConnectTimeout() {
        return socksConnectTimeout;
    }

    /**
     * Sets the httpConnectionTimeout. This is used when making connections.
     * @param httpsConnectTimeout
     */
    public synchronized void setSocksConnectTimeout(int socksConnectTimeout) {
        int oldValue = this.socksConnectTimeout;
        this.socksConnectTimeout = socksConnectTimeout;
        propertySupport.firePropertyChange(DownloadSettings.PROP_HTTPSCONNECTTIMEOUT, oldValue, socksConnectTimeout);
    }

    /**
     * returns the httpsReadTimeout.
     * @return
     */
    public synchronized int getSocksReadTimeout() {
        return socksReadTimeout;
    }

    /**
     * Sets the httpsReadTimeout.
     * @param httpsReadTimeout
     */
    public synchronized void setSocksReadTimeout(int socksReadTimeout) {
        int oldValue = this.socksReadTimeout;
        this.socksReadTimeout = socksReadTimeout;
        propertySupport.firePropertyChange(DownloadSettings.PROP_SOCKSREADTIMEOUT, oldValue, socksReadTimeout);
    }

    /**
     * Returns the httpsUserAgent.
     * @return
     */
    public synchronized String getHttpsUserAgent() {
        return httpsUserAgent;
    }

    /**
     * Sets the httpsUserAgent, which is used when making a request.
     * @param httpsUserAgent
     */
    public synchronized void setHttpsUserAgent(String httpsUserAgent) {
        String oldValue = this.httpsUserAgent;
        this.httpsUserAgent = httpsUserAgent;
        propertySupport.firePropertyChange(DownloadSettings.PROP_HTTPSUSERAGENT, oldValue, httpsUserAgent);
    }

    /**
     * returns the httpsTrustManagers.
     * @return
     */
    public synchronized TrustManager[] getHttpsTrustManagers() {
        return httpsTrustManagers;
    }

    /**
     * Sets the httpsTrustManagers. This is set in the ConnectionFactory.
     * @param httpsTrustManagers
     */
    public synchronized void setHttpsTrustManagers(TrustManager[] httpsTrustManagers) {
        TrustManager[] oldValue = this.httpsTrustManagers;
        this.httpsTrustManagers = httpsTrustManagers;
        propertySupport.firePropertyChange(DownloadSettings.PROP_HTTPSTRUSTMANAGERS, oldValue, httpsTrustManagers);
    }

    /**
     * returns if redirects should be followed.
     * @return
     */
    public synchronized boolean isAutoFollowRedirects() {
        return autoFollowRedirects;
    }

    /**
     * Sets wether redirects should be followed.
     * @param autoFollowRedirects
     */
    public synchronized void setAutoFollowRedirects(boolean autoFollowRedirects) {
        boolean oldValue = this.autoFollowRedirects;
        this.autoFollowRedirects = autoFollowRedirects;
        propertySupport.firePropertyChange(DownloadSettings.PROP_AUTOFOLLOWREDIRECTS, oldValue, autoFollowRedirects);
    }

    /**
     * Gets the maxHops, which is how many times a redirect will be followed.
     * @return
     */
    public synchronized int getMaxHops() {
        return maxHops;
    }

    /**
     * Sets the maxHops, which is how many times a redirect will be followed.
     * @param maxHops
     */
    public synchronized void setMaxHops(int maxHops) {
        int oldValue = this.maxHops;
        this.maxHops = maxHops;
        propertySupport.firePropertyChange(DownloadSettings.PROP_MAXHOPS, oldValue, maxHops);
    }

    /**
     * adds a PropertyChangeListener
     * @param listener
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * removes a PropertyChangeListener
     * @param listener
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}