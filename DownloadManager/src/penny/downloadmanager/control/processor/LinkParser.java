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

    public LinkParser(Download download) throws URISyntaxException {
        this.download = download;
        parsingModel = Model.getApplicationSettings().getParsingModel();
        linkExtractor = new LinkExtractor(download.getUrl().toURI(), this);
        linkExtractor.setLinkState(download.getLinkState());
    }

    public void parse(int read, byte[] buffer) {
        if (Model.parseLinks(download)) {
            linkExtractor.put(buffer, 0, read);
            download.setLinkState(linkExtractor.getLinkState());
        }
    }

    public void resetParseFromFile(File file) throws URISyntaxException, FileNotFoundException, IOException {
        if (Model.parseLinks(download)) {
            linkExtractor = new LinkExtractor(download.getUrl().toURI(), this);
            download.setLinkState(linkExtractor.getLinkState());
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
        download.setWordBuffer("");
        download.setLinkState(linkExtractor.getLinkState());
    }

    @Override
    public void eatLink(String url, boolean src) {
        if (src) {
            download.addSrcLink(url);
        } else {
            download.addHrefLink(url);
        }
    }
}
