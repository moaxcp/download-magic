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
     * number of miliseconds in a second
     */
    protected static final long second = 1000;
    /**
     * number of miliseconds in a minute
     */
    protected static final long minute = 60 * second;
    /**
     * number of miliseconds in a hour
     */
    protected static final long hour = 60 * minute;
    /**
     * number of miliseconds in a day
     */
    protected static final long day = 24 * hour;

    /**
     * Gets the percentage of download completed for a AbstractDownload.
     * @param d
     * @return percentage complete
     */
    public static float getProgress(AbstractDownload d) {
        if(d.getSize() > 0) {
            return ((float) d.getDownloaded() / d.getSize()) * 100;
        } else {
            return 0;
        }
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
        return formatByteSize((d.getDownloadTime() / 1000000000 == 0 ? 0 : d.getDownloaded() / (d.getDownloadTime() / 1000000000))) + "/s";
    }

    /**
     * Formats a long containing a byte value and returns a String containing
     * either the KB, MB, GB, TB value
     *
     * @param sz - the number of bytes
     * @return String formatByteSize - A formatted size
     */
    public static String formatByteSize(long sz) {
        float size = sz;
        String post = "B";
        if (sz / 1024 > 0) {
            size = size / 1024;
            sz = (long) size;
            post = "KB";
        }
        if (sz / 1024 > 0) {
            size = size / 1024;
            sz = (long) size;
            post = "MB";
        }
        if (sz / 1024 > 0) {
            size = size / 1024;
            sz = (long) size;
            post = "GB";
        }
        if (sz / 1024 > 0) {
            size = size / 1024;
            //sz = (long)size;
            post = "TB";
        }

        return String.format("%1$.2f", size) + post;
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
    public static String getFileExtention(String file) {
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
    public static String formatMilliTime(long timeMilli) {
        long days = (timeMilli / (1000 * 60 * 60 * 24));
        long hours = (timeMilli / (1000 * 60 * 60)) % 24;
        long minutes = (timeMilli / (1000 * 60)) % 60;
        long seconds = (timeMilli / 1000) % 60;
        String sdays = (days == 0 ? "00:" : ((days < 10 ? "0" + days : days) + ":"));
        String shours = (hours == 0 || hours >= 24 ? "00:" : (hours < 10 ? "0" + hours : hours) + ":");
        String sminutes = (minutes == 0 || minutes >= 60 ? "00:" : (minutes < 10 ? "0" + minutes : minutes) + ":");
        String sseconds = seconds < 10 ? "0" + seconds : seconds + "";
        String postfix = days >= 1 ? "day" : hours >= 1 ? "hour" : minutes >= 1 ? "min" : seconds >= 1 ? "sec" : "";
        return (days == 0 ? "" : sdays) + (days == 0 && hours == 0 ? "" : shours) + (days == 0 && hours == 0 && minutes == 0 ? "" : sminutes) + sseconds + " " + postfix;
    }

    /**
     * Formats a time in milliseconds to a 00:00:00:00:000 unit format.
     * @param time The time in milliseconds.
     * @return The formatted string.
     */
    public static String formatMilliTimeMilli(long timeMilli) {
        long days = (timeMilli / (1000 * 60 * 60 * 24));
        long hours = (timeMilli / (1000 * 60 * 60)) % 24;
        long minutes = (timeMilli / (1000 * 60)) % 60;
        long seconds = (timeMilli / 1000) % 60;
        long milli = timeMilli % 1000;
        String sdays = (days == 0 ? "00:" : ((days < 10 ? "0" + days : days) + ":"));
        String shours = (hours == 0 || hours >= 24 ? "00:" : (hours < 10 ? "0" + hours : hours) + ":");
        String sminutes = (minutes == 0 || minutes >= 60 ? "00:" : (minutes < 10 ? "0" + minutes : minutes) + ":");
        String sseconds = (seconds == 0 || seconds >= 60 ? "00:" : (seconds < 10 ? "0" + seconds : seconds) + ":");
        String smilli = milli < 100 ? milli < 10 ? "00" + milli : "0" + milli : "" + milli;
        String postfix = days >= 1 ? "day" : hours >= 1 ? "hour" : minutes >= 1 ? "min" : seconds >= 1 ? "sec" : milli >= 1 ? "milli" : "";
        return (days == 0 ? "" : sdays) + (days == 0 && hours == 0 ? "" : shours) + (days == 0 && hours == 0 && minutes == 0 ? "" : sminutes) + (days == 0 && hours == 0 && minutes == 0 && seconds == 0 ? "" : sseconds) + smilli + " " + postfix;
    }

    /**
     * Formats a time in nanoseconds to a 00:00:00:00 unit format.
     * @param time The time in nanoseconds.
     * @return The formatted string.
     */
    public static String formatNanoTime(long timeNano) {
        long days = (timeNano / (1000 * 60 * 60 * 24));
        long hours = (timeNano / (1000 * 60 * 60)) % 24;
        long minutes = (timeNano / (1000 * 60)) % 60;
        long seconds = (timeNano / 1000) % 60;
        String sdays = (days == 0 ? "00:" : ((days < 10 ? "0" + days : days) + ":"));
        String shours = (hours == 0 || hours >= 24 ? "00:" : (hours < 10 ? "0" + hours : hours) + ":");
        String sminutes = (minutes == 0 || minutes >= 60 ? "00:" : (minutes < 10 ? "0" + minutes : minutes) + ":");
        String sseconds = seconds < 10 ? "0" + seconds : seconds + "";
        String postfix = days >= 1 ? "day" : hours >= 1 ? "hour" : minutes >= 1 ? "min" : seconds >= 1 ? "sec" : "";
        return (days == 0 ? "" : sdays) + (days == 0 && hours == 0 ? "" : shours) + (days == 0 && hours == 0 && minutes == 0 ? "" : sminutes) + sseconds + " " + postfix;
    }

    /**
     * Formats a time in nanoseconds to a 00:00:00:00:000 unit format.
     * @param time The time in nanoseconds.
     * @return The formatted string.
     */
    public static String formatNanoTimeMilli(long timeNano) {
        long days = (timeNano / (1000 * 60 * 60 * 24));
        long hours = (timeNano / (1000 * 60 * 60)) % 24;
        long minutes = (timeNano / (1000 * 60)) % 60;
        long seconds = (timeNano / 1000) % 60;
        long milli = timeNano % 1000;
        String sdays = (days == 0 ? "00:" : ((days < 10 ? "0" + days : days) + ":"));
        String shours = (hours == 0 || hours >= 24 ? "00:" : (hours < 10 ? "0" + hours : hours) + ":");
        String sminutes = (minutes == 0 || minutes >= 60 ? "00:" : (minutes < 10 ? "0" + minutes : minutes) + ":");
        String sseconds = (seconds == 0 || seconds >= 60 ? "00:" : (seconds < 10 ? "0" + seconds : seconds) + ":");
        String smilli = milli < 100 ? milli < 10 ? "00" + milli : "0" + milli : "" + milli;
        String postfix = days >= 1 ? "day" : hours >= 1 ? "hour" : minutes >= 1 ? "min" : seconds >= 1 ? "sec" : milli >= 1 ? "milli" : "";
        return (days == 0 ? "" : sdays) + (days == 0 && hours == 0 ? "" : shours) + (days == 0 && hours == 0 && minutes == 0 ? "" : sminutes) + (days == 0 && hours == 0 && minutes == 0 && seconds == 0 ? "" : sseconds) + smilli + " " + postfix;
    }

    /**
     * Formats a time in nanoseconds to a 00:00:00:00:000000000 unit format.
     * @param time The time in nanoseconds.
     * @return The formatted string.
     */
    public static String formatNanoTimeNano(long timeNano) {
        long days = (timeNano / (1000 * 60 * 60 * 24));
        long hours = (timeNano / (1000 * 60 * 60)) % 24;
        long minutes = (timeNano / (1000 * 60)) % 60;
        long seconds = (timeNano / 1000) % 60;
        long milli = timeNano % 1000;
        String sdays = (days == 0 ? "00:" : ((days < 10 ? "0" + days : days) + ":"));
        String shours = (hours == 0 || hours >= 24 ? "00:" : (hours < 10 ? "0" + hours : hours) + ":");
        String sminutes = (minutes == 0 || minutes >= 60 ? "00:" : (minutes < 10 ? "0" + minutes : minutes) + ":");
        String sseconds = (seconds == 0 || seconds >= 60 ? "00:" : (seconds < 10 ? "0" + seconds : seconds) + ":");
        String smilli = milli < 100 ? milli < 10 ? "00" + milli : "0" + milli : "" + milli;
        String postfix = days >= 1 ? "day" : hours >= 1 ? "hour" : minutes >= 1 ? "min" : seconds >= 1 ? "sec" : milli >= 1 ? "milli" : "";
        return (days == 0 ? "" : sdays) + (days == 0 && hours == 0 ? "" : shours) + (days == 0 && hours == 0 && minutes == 0 ? "" : sminutes) + (days == 0 && hours == 0 && minutes == 0 && seconds == 0 ? "" : sseconds) + smilli + " " + postfix;
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
}
