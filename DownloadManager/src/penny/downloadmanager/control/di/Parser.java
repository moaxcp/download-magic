/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.di;

import penny.download.Download;
import penny.download.DownloadProcessor;
import penny.downloadmanager.model.ApplicationSettingsModel;
import penny.downloadmanager.model.DownloadData;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.gui.ParsingModel;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import penny.parser.LinkEater;
import penny.parser.LinkExtractor;
import penny.parser.WordEater;
import penny.parser.WordExtractor;

/**
 *
 * @author john
 */
public class Parser implements DownloadProcessor, LinkEater, WordEater {

    private LinkExtractor linkExtractor;
    private WordExtractor wordExtractor;
    private DownloadData curDownload;
    private ParsingModel parsingModel;

    public Parser() {
        System.out.println();
        parsingModel = Model.getApplicationSettings().getParsingModel();
        wordExtractor = new WordExtractor(this);
    }

    private boolean parseLinks(Download d) {
        boolean r = false;
        if (parsingModel.isParseLinks()) {
            if (parsingModel.isParseUnknownLinks()) {
                if(d.getContentType() == null || (d.getContentType() != null && d.getContentType().equals(""))) {
                    r= true;
                }
            }
            if(ApplicationSettingsModel.typeMatches(d.getContentType(), parsingModel.getParseLinksTypes())) {
                r = true;
            }
        }
        return r;
    }

    private boolean parseWords(Download d) {
        boolean r = false;
        if (parsingModel.isParseWords()) {
            if (parsingModel.isParseUnknownWords()) {
                if(d.getContentType() == null || (d.getContentType() != null && d.getContentType().equals(""))) {
                    r= true;
                }
            }
            if(ApplicationSettingsModel.typeMatches(d.getContentType(), parsingModel.getParseWordsTypes())) {
                r = true;
            }
        }
        return r;
    }

    @Override
    public void onStartInput(Download d) {
        curDownload = (DownloadData) d;
        try {
            linkExtractor = new LinkExtractor(curDownload.getUrl().toURI(), this);
            linkExtractor.setLinkState(curDownload.getLinkState());
            wordExtractor.setWordBuffer(curDownload.getWordBuffer());
        } catch (URISyntaxException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onEndInput(Download d) {
    }

    @Override
    public void onCompleted(Download d) {
    }

    @Override
    public boolean onCheck(Download d) {
        return true;
    }

    @Override
    public void doChunck(Download d, int read, byte[] buffer) {
        if(parseLinks(d)) {
            linkExtractor.put(buffer, 0, read);
            curDownload.setLinkState(linkExtractor.getLinkState());
        }
        if(parseWords(d)) {
            wordExtractor.put(buffer, 0, read);
            curDownload.setWordBuffer(wordExtractor.getWordBuffer());
        }
    }

    @Override
    public void onReset(Download d) {
    }

    @Override
    public void eatLink(String url, boolean src) {
        if (src) {
            curDownload.addSrcLink(url);
        } else {
            curDownload.addHrefLink(url);
        }
    }

    @Override
    public void onInit(Download d) {
    }

    @Override
    public void eatWord(String word) {
        curDownload.addWord(word);
    }
}
