/*
 * LinkExtractor.java
 *
 * Created on June 10, 2007, 12:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package penny.parser;

import java.util.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class LinkExtractor {

    private char[] src = new char[]{'*', 's', 'r', 'c', '=', '\"'};
    private char[] href = new char[]{'h', 'r', 'e', 'f', '=', '\"'};
    private char[] queue = new char[6];
    private LinkState linkState;
    private LinkEater linkEater;
    private URI baseURI;

    public URI getBaseURI() {
        return baseURI;
    }

    private void setBaseURI(URI baseURI) {
        this.baseURI = baseURI;
    }

    /** Creates a new instance of LinkExtractor */
    public LinkExtractor(URI baseURI, LinkEater linkEater) {
        this.setBaseURI(baseURI);
        linkState = new LinkState();
        this.linkEater = linkEater;
    }

    public LinkState getLinkState() {
        return linkState;
    }

    public void setLinkState(LinkState state) {
        this.linkState = state;
    }

    private String getFullPath(String s) {
        try {
            return baseURI.resolve(s).toString();
        } catch(IllegalArgumentException ex) {
            Logger.getLogger(LinkExtractor.class.getName()).logp(Level.INFO, "LinkExtractor", "getFullPath", ex.toString() + " " + s);
        }
        return s;
    }

    private boolean match() {
        char[] seq;
        if (queue[3] == 'c') {
            seq = src;
            linkState.setSrcLink(true);
        } else if (queue[3] == 'f') {
            seq = href;
            linkState.setSrcLink(false);
        } else {
            return false;
        }

        for (int i = 0; i < seq.length; i++) {
            if (seq[i] == '*') {
                continue;
            }
            if (seq[i] != queue[i]) {
                return false;
            }
        }

        return true;
    }

    private char getProperChar(char c) {
        char ch = ' ';
        switch (c) {
            case ' ':
                break;
            case '\n':
                break;
            case '\t':
                break;
            case '\'':
                ch = '\"';
            default:
                ch = c;
        }
        return ch;
    }
    
    private String encodeCharacter(char c) {
        String ch = "";
        switch (c) {
            case ' ':
                ch = "%20";
                break;
        }
        return ch;
    }

    public void put(char c) {
        if (linkState.isExtract()) {
            if (c == '\"' || c == '\'') {
                String l = getFullPath(linkState.getLink().toString());
                if(l != null) {
                    linkEater.eatLink(l, linkState.isSrcLink());
                }
                linkState.getLink().setLength(0);
                linkState.setExtract(false);
            } else {
                if (c == ' ') {
                    linkState.getLink().append(encodeCharacter(c));
                } else {
                    linkState.getLink().append(c);
                }
            }
        } else {
            char ch = getProperChar(c);
            if (ch == ' ') {
                return;
            }
            for (int i = 0; i < queue.length - 1; i++) {
                queue[i] = queue[i + 1];
            }
            queue[queue.length - 1] = c;

            if (match()) {
                linkState.setExtract(true);
                //System.out.println("** MATCH = " + new String(queue));
            } else {
                //System.out.println("NO MATCH = " + new String(queue));
            }
        }
    }

    public void put(byte[] b, int off, int len) {
        for (int i = off; i < off + len; i++) {
            put((char) b[i]);
        }
    }
}