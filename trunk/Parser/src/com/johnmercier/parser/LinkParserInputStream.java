/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnmercier.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.StringCharacterIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class LinkParserInputStream extends InputStream {

    private int[] src = new int[]{'*', 's', 'r', 'c', '='};
    private int[] href = new int[]{'h', 'r', 'e', 'f', '='};
    private int[] queue = new int[5];
    private boolean extract;
    private boolean match;
    private boolean matchSRC;
    private StringCharacterIterator replaceLink;
    private int startChar;
    private int endChar;
    private InputStream in;
    private LinkEater linkEater;
    private LinkReplacer linkReplacer;
    private URL baseURL;

    public LinkParserInputStream(InputStream in, URI baseURI, LinkEater linkEater) {
        extract = false;
        match = false;
        matchSRC = false;
        endChar = -1;
        startChar = -1;
        this.in = in;

        this.linkEater = linkEater;
        this.linkReplacer = null;

        this.replaceLink = new StringCharacterIterator("");
        replaceLink.next();
        if (replaceLink.getIndex() > replaceLink.getEndIndex()) {
            System.out.println("hello");
        }
    }

    public LinkParserInputStream(InputStream in, URL baseURL, LinkEater linkEater, LinkReplacer linkReplacer) {
        extract = false;
        match = false;
        endChar = -1;
        this.in = in;
        this.baseURL = baseURL;
        this.linkEater = linkEater;
        this.linkReplacer = linkReplacer;

        this.replaceLink = new StringCharacterIterator("");
        replaceLink.next();
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
            if(url.getPath().equals("")) {
                url = new URL(url.getProtocol(), url.getHost(), "/");
            }
            try {
                return new URI(url.toString()).resolve(URLEncoder.encode(path, "UTF8")).toURL();
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(LinkParserInputStream.class.getName()).log(Level.SEVERE, null, ex);
                return new URI(url.toString()).resolve(path).toURL();
            }
        }
    }

    private boolean match() {
        int[] seq;
        if (queue[3] == 'c') {
            seq = src;
            match = matchFull(seq);
            if(match) {
                matchSRC = true;
            } else {
                matchSRC = false;
            }
        } else if (queue[3] == 'f') {
            seq = href;
            match = matchFull(seq);
        } else {
            match = false;
            matchSRC = false;
        }

        return match;
    }

    private boolean matchFull(int[] seq) {
        match = true;
        for (int i = 0; i < seq.length; i++) {
            if (seq[i] == '*') {
                continue;
            }
            if (seq[i] != queue[i]) {
                match = false;
                break;
            }
        }
        return match;
    }

    private void addToQueue(int c) {
        c = getStreamToQueue(c);
        if (c != -1) {
            for (int i = 0; i < queue.length - 1; i++) {
                queue[i] = queue[i + 1];
            }
            queue[queue.length - 1] = c;
        }
    }

    private int getStreamToQueue(int c) {
        if (c == ' ') {
            c = -1;
        } else if (c == '\t') {
            c = -1;
        } else if (c == '\n') {
            c = -1;
        }
        return c;
    }

    private void initReplaceLink(String link) {
        if (replaceLink == null) {
            replaceLink = new StringCharacterIterator(link);
        } else {
            replaceLink.setText(link);
        }
    }

    public int read() throws IOException {
        int returnCharacter = -1;
        if (extract) {
            if (startChar == -1) {
                startChar = getIn().read();
            }
            if (startChar == '\"' || startChar == '\'' || startChar == ' ' || startChar == -1) {
                returnCharacter = startChar;
                startChar = -1;
            }
            if (startChar != -1) {
                StringBuffer link = new StringBuffer();
                link.append((char) startChar);
                while (extract) {
                    returnCharacter = getIn().read();
                    if (returnCharacter == '\"' || returnCharacter == '\'' || returnCharacter == ' ' || returnCharacter == '>' || returnCharacter == -1) {
                        endChar = returnCharacter;
                        String l = null;
                        try {
                            l = LinkParserInputStream.createURL(baseURL, link.toString()).toString();
                        } catch (URISyntaxException ex) {
                            Logger.getLogger(LinkParserInputStream.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(LinkParserInputStream.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (l != null) {
                            linkEater.eatLink(l, matchSRC);
                        }
                        extract = false;
                        queue = new int[6];
                    } else {
                        link.append((char) returnCharacter);
                    }
                }
                String l = null;
                try {
                    l = LinkParserInputStream.createURL(baseURL, link.toString()).toString();
                } catch (URISyntaxException ex) {
                    Logger.getLogger(LinkParserInputStream.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(LinkParserInputStream.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (l != null) {
                    if (linkReplacer != null) {
                        initReplaceLink(linkReplacer.getReplaceLink(l, matchSRC));
                    } else {
                        initReplaceLink(link.toString());
                    }
                } else {
                    if (linkReplacer != null) {
                        initReplaceLink(linkReplacer.getReplaceLink(link.toString(), matchSRC));
                    } else {
                        initReplaceLink(link.toString());
                    }
                }
                returnCharacter = replaceLink.first();
            }
        } else {
            startChar = -1;
            char replaceChar = replaceLink.next();
            if (replaceChar == StringCharacterIterator.DONE) {
                if (endChar != -1) {
                    returnCharacter = endChar;
                    endChar = -1;
                } else {
                    returnCharacter = getIn().read();
                    addToQueue(returnCharacter);
                    extract = match();
                }
            } else {
                returnCharacter = replaceChar;
            }
        }
        return returnCharacter;
    }

    /**
     * @return the in
     */
    public InputStream getIn() {
        return in;
    }
}
