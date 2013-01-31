/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import penny.download.DownloadStatus;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.db.Download;
import penny.downloadmanager.model.gui.ParsingModel;
import penny.parser.LinkEater;
import penny.parser.LinkExtractor;

/**
 *
 * @author john
 */
public class LinkParser implements LinkEater {

    private LinkExtractor linkExtractor;
    private ParsingModel parsingModel;
    private Download download;
    private List<String> hrefLinks;
    private List<String> srcLinks;

    public LinkParser(Download download) throws URISyntaxException {
        this.download = download;
        parsingModel = Model.getApplicationSettings().getParsingModel();
        linkExtractor = new LinkExtractor(download.getUrl().toURI(), this);
        linkExtractor.setLinkState(download.getLinkState());
        hrefLinks = new ArrayList<String>();
        srcLinks = new ArrayList<String>();
    }

    public void parse(int read, byte[] buffer) {
        if (Model.parseLinks(download)) {
            linkExtractor.put(buffer, 0, read);
            download.setLinkState(linkExtractor.getLinkState());
            if(hrefLinks.size() > parsingModel.getLinkQueueSize()) {
                pushHrefLinksToDownload();
            }
            if(srcLinks.size() > parsingModel.getLinkQueueSize()) {
                pushSrcLinksToDownload();
            }
        }
    }

    public void resetParseFromFile(File file) throws URISyntaxException, FileNotFoundException, IOException {
        if (Model.parseLinks(download)) {
            reset();
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                int read = in.read(buffer);
                while (read != -1 && download.getStatus() != DownloadStatus.STOPPING) {
                    parse(read, buffer);
                    read = in.read(buffer);
                }
            } finally {
                in.close();
            }
        }
    }

    public void reset() throws URISyntaxException {
        linkExtractor = new LinkExtractor(download.getUrl().toURI(), this);
        download.setLinkState(linkExtractor.getLinkState());
        download.clearHrefLinks();
        download.clearSrcLinks();
    }
    
    private void pushHrefLinksToDownload() {
        download.addHrefLinks(hrefLinks);
        hrefLinks.clear();
    }
    
    private void pushSrcLinksToDownload() {
        download.addSrcLinks(srcLinks);
        srcLinks.clear();
    }
    
    public void complete() {
        pushHrefLinksToDownload();
        pushSrcLinksToDownload();
    }

    @Override
    public void eatLink(String url, boolean src) {
        if (src) {
            srcLinks.add(url);
        } else {
            hrefLinks.add(url);
        }
    }
}
