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
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.db.Download;
import penny.downloadmanager.model.gui.ParsingModel;
import penny.parser.LinkEater;
import penny.parser.LinkExtractor;
import penny.parser.WordEater;
import penny.parser.WordExtractor;

/**
 *
 * @author john
 */
public class WordParser implements LinkEater, WordEater {

    private WordExtractor wordExtractor;
    private ParsingModel parsingModel;
    private Download download;

    public WordParser(Download download) throws URISyntaxException {
        this.download = download;
        parsingModel = Model.getApplicationSettings().getParsingModel();
        wordExtractor = new WordExtractor(this);
        wordExtractor.setWordBuffer(download.getWordBuffer());
    }

    public void parse(int read, byte[] buffer) {
        if (Model.parseWords(download)) {
            wordExtractor.put(buffer, 0, read);
            download.setWordBuffer(wordExtractor.getWordBuffer());
        }
    }

    public void resetParseFromFile(File file) throws URISyntaxException, FileNotFoundException, IOException {
        if (Model.parseWords(download)) {
            wordExtractor = new WordExtractor(this);
            download.setWordBuffer("");
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                int read = in.read(buffer);
                while (read != -1) {
                    parse(read, buffer);
                    read = in.read(buffer);
                }
            } finally {
                in.close();
            }
        }
    }

    public void reset() throws URISyntaxException {
        wordExtractor = new WordExtractor(this);
        download.setWordBuffer("");
    }

    @Override
    public void eatLink(String url, boolean src) {
        if (src) {
            download.addSrcLink(url);
        } else {
            download.addHrefLink(url);
        }
    }

    @Override
    public void eatWord(String word) {
        download.addWord(word);
    }
}
