/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.download;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author john
 */
public class Downloads {

    /**
     * number of nanoseconds in a millisecond
     */
    protected static final long millisecond = 1000000;
    /**
     * number of nanoseconds in a second
     */
    protected static final long second = 1000 * millisecond;
    /**
     * number of nanoseconds in a minute
     */
    protected static final long minute = 60 * second;
    /**
     * number of nanoseconds in a hour
     */
    protected static final long hour = 60 * minute;
    /**
     * number of nanoseconds in a day
     */
    protected static final long day = 24 * hour;
    
    public static String formatProgress(double prog) {
        return String.format("%1$.2f", prog * 100);
    }

    /**
     * Returns the time left for a AbstractDownload.
     * @param d
     * @return the time left
     */
    public static long getTimeLeft(AbstractDownload d) {
        if (d.getDownloadTime() == 0) {
            return 0;
        }
        return (d.getSize() - d.getDownloaded()) / (d.getDownloaded() / d.getDownloadTime());
    }

    /**
     * Returns the rate for a AbstractDownload.
     * @param d The download to calculate the rate for.
     * @return the rate in seconds
     */
    public static String getRate(AbstractDownload d) {
        return formatByteSize((long)(d.getDownloadTime() == 0 ? 0 : (d.getDownloaded() / (double)d.getDownloadTime()) * (double)1000000000)) + "/s";
    }

    /**
     * Formats a long containing a byte value and returns a String containing
     * either the KB, MB, GB, TB value
     *
     * @param sz - the number of bytes
     * @return String formatByteSize - A formatted size
     */
    public static String formatByteSize(long sz) {
        String post = "B";
        if(sz < 10000) {
            return String.format("%1$d %2$s", sz, post);
        }
        double size = sz;
        if ((long)size / 1024 > 0) {
            size = size / 1024;
            post = "KB";
        }
        if ((long)size / 1024 > 0) {
            size = size / 1024;
            post = "MB";
        }
        if ((long)size / 1024 > 0) {
            size = size / 1024;
            post = "GB";
        }
        if ((long)size / 1024 > 0) {
            size = size / 1024;
            post = "TB";
        }
        int digits = Long.toString((long) size).length();
        return String.format("%1$." + (4 - digits) + "f %2$s", size, post);
    }

    /**
     * Returns a path without file name. Takes in a path for a file.
     * @param path The path to the file.
     * @return The path.
     */
    public static String getPath(String path) {
        if (path.endsWith("\\") || path.endsWith("/")) {
            return path;
        }
        String[] tk = path.split("[\\\\/]");

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < tk.length - 1; ++i) {
            if (!tk[i].equals("")) {
                buf.append(File.separatorChar + tk[i]);
            }
        }
        return buf.toString();
    }

    /**
     * Returns the filename from a String that contains a full or relative path to a file.
     * @param path The path to the file.
     * @return The file name.
     */
    public static String getFileName(String path) {
        if (path.endsWith("\\") || path.endsWith("/")) {
            return "";
        }
        String[] tk = path.split("[\\\\/]");
        if (tk.length >= 1) {
            return tk[tk.length - 1];
        }
        return "";
    }

    /**
     * Returns the file extention of a file.
     * @param file
     * @return the file extention or null if there is no extention
     */
    public static String getFileExtension(String file) {
        if (file == null || !file.contains(".")) {
            return "";
        }
        if(file.endsWith(".")) {
            return ".";
        }
        int i = file.lastIndexOf('.');
        return i == -1 ? "" : file.substring(i);
    }

    /**
     * Used to create a url from a path parsed from a webpage and the url of
     * the webpage
     * @param url
     * @param path
     * @return the url to the path
     */
    public static URL createURL(URL url, String path) throws URISyntaxException, MalformedURLException {
        try {
            return new URL(path);
        } catch (MalformedURLException mue) {
            if (url.getPath().equals("")) {
                url = new URL(url.getProtocol(), url.getHost(), "/");
            }
            try {
                return new URI(url.toString()).resolve(URLEncoder.encode(path, "UTF8")).toURL();
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Downloads.class.getName()).log(Level.SEVERE, null, ex);
                return new URI(url.toString()).resolve(path).toURL();
            }
        }
    }

    /**
     * Formats a time in milliseconds to a 00:00:00:00 unit format.
     * @param time The time in milliseconds.
     * @return The formatted string.
     */
    public static String formatNanoTime(long nanoseconds) {
        long days = (nanoseconds / day);
        long hours = (nanoseconds / hour) % 24;
        long minutes = (nanoseconds / minute) % 60;
        long seconds = (nanoseconds / second) % 60;
        long milli = (nanoseconds / millisecond) % 1000;
        long nano = nanoseconds % 1000000;
        String sdays = (days < 10 ? "0" + days : ("" + days)) + ":";
        String shours = (hours < 10 ? "0" + hours : ("" + hours)) + ":";
        String sminutes = (minutes < 10 ? "0" + minutes : "" + minutes) + ":";
        String sseconds = (seconds < 10 ? "0" + seconds : "" + seconds) + ":";
        String smilli = (milli < 100 ? milli < 10 ? "00" + milli : "0" + milli : "" + milli);
        String snano = nano < 100000 ? nano < 10000 ? nano < 1000 ? nano < 100 ? nano < 10 ? "00000" + nano : "0000" + nano : "000" + nano : "00" + nano : "0" + nano : "" + nano;
        String postfix = days >= 1 ? "day" : hours >= 1 ? "hr" : minutes >= 1 ? "mn" : seconds >= 1 ? "s" : milli >= 1 ? "ms" : nano >= 1 ? "ns" : "";
        return (days == 0 ? "" : sdays) + (days == 0 && hours == 0 ? "" : shours) + (days == 0 && hours == 0 && minutes == 0 ? "" : sminutes) + (days == 0 && hours == 0 && minutes == 0 && seconds == 0 ? "" : sseconds) + (days == 0 && hours == 0 && minutes == 0 && seconds == 0 && milli == 0 ? "" : smilli) + (milli == 0 ? snano : "") + " " + postfix;
    }
    
    public static void setStatus(AbstractDownload d, DownloadStatus s) {
        if(s == DownloadStatus.COMPLETE) {
            d.complete();
        } else if(s == DownloadStatus.ERROR) {
            d.error();
        } else if(s == DownloadStatus.STOPPED) {
            d.status = DownloadStatus.STOPPED;
        } else {
            throw new IllegalArgumentException("DownloadStatus invalid " + s);
        }
    }
    
    public static String toFormattedString(AbstractDownload download, String property) {
        if(property.equals(AbstractDownload.PROP_ATTEMPTS)) {
            return String.format("%1$d", download.getAttempts());
        }
        if(property.equals(AbstractDownload.PROP_CANQUEUE)) {
            return String.valueOf(download.isCanQueue());
        }
        if(property.equals(AbstractDownload.PROP_CANSTOP)) {
            return String.valueOf(download.isCanStop());
        }
        if(property.equals(AbstractDownload.PROP_CONTENTTYPE)) {
            return download.getContentType();
        }
        if(property.equals(AbstractDownload.PROP_DOWNLOADED)) {
            return Downloads.formatByteSize(download.getDownloaded());
        }
        if(property.equals(AbstractDownload.PROP_DOWNLOADTIME)) {
            return Downloads.formatNanoTime(download.getDownloadTime());
        }
        if(property.equals(AbstractDownload.PROP_FILE)) {
            return download.getFile();
        }
        if(property.equals(AbstractDownload.PROP_FILEEXSENTION)) {
            return download.getFileExtension();
        }
        if(property.equals(AbstractDownload.PROP_HOPS)) {
            return String.valueOf(download.getHops());
        }
        if(property.equals(AbstractDownload.PROP_HOST)) {
            return download.getHost();
        }
        if(property.equals(AbstractDownload.PROP_LOCATIONS)) {
            return String.format("%1$d", download.getLocations().size());
        }
        if(property.equals(AbstractDownload.PROP_MESSAGE)) {
            return download.getMessage();
        }
        if(property.equals(AbstractDownload.PROP_PATH)) {
            return download.getPath();
        }
        if(property.equals(AbstractDownload.PROP_PROGRESS)) {
            return Downloads.formatProgress(download.getProgress());
        }
        if(property.equals(AbstractDownload.PROP_PROTOCOL)) {
            return download.getProtocol();
        }
        if(property.equals(AbstractDownload.PROP_PROTOCOLFILENAME)) {
            return download.getProtocolFileName();
        }
        if(property.equals(AbstractDownload.PROP_QUERY)) {
            return download.getQuery();
        }
        if(property.equals(AbstractDownload.PROP_BYTESPERSECOND)) {
            return String.format("%1$s/s", Downloads.formatByteSize(download.getBytesPerSecond()));
        }
        if(property.equals(AbstractDownload.PROP_RESPONSECODE)) {
            return String.valueOf(download.getResponseCode());
        }
        if(property.equals(AbstractDownload.PROP_RETRYTIME)) {
            return Downloads.formatNanoTime(download.getRetryTime());
        }
        if(property.equals(AbstractDownload.PROP_SIZE)) {
            return Downloads.formatByteSize(download.getSize());
        }
        if(property.equals(AbstractDownload.PROP_STATUS)) {
            return download.getStatus().toString();
        }
        if(property.equals(AbstractDownload.PROP_TIMELEFT)) {
            return Downloads.formatNanoTime(download.getTimeLeft());
        }
        if(property.equals(AbstractDownload.PROP_URL)) {
            return download.getUrl().toString();
        }
        return null;
    }
}
