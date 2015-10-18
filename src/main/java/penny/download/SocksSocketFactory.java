/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.download;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 *
 * @author john
 */
public class SocksSocketFactory implements SchemeSocketFactory {

    String proxyHost;
    int proxyPort;

    public SocksSocketFactory(String socksHost, int socksPort) {
        proxyHost = socksHost;
        proxyPort = socksPort;
    }

    public boolean isSecure(Socket sock) throws IllegalArgumentException {
        if (sock.isClosed()) {
            throw new IllegalArgumentException("Socket is closed.");
        }
        return false;
    }

    public Socket createSocket(HttpParams params) throws IOException {
        InetSocketAddress socksaddr = new InetSocketAddress(proxyHost, proxyPort);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
        Socket sock = new Socket(proxy);
        return sock;
    }

    public Socket connectSocket(Socket sock, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        sock.bind(localAddress);
        try {
            sock.connect(remoteAddress, HttpConnectionParams.getConnectionTimeout(params));

        } catch (SocketTimeoutException ex) {
            throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
        }
        return sock;
    }
}
