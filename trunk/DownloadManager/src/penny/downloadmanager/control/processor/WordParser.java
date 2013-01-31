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
import penny.parser.WordEater;
import penny.parser.WordExtractor;

/**
 *
 * @author john
 */
public class WordParser implements WordEater {

    private WordExtractor wordExtractor;
    private ParsingModel parsingModel;
    private Download download;
    private List<String> wordQueue;

    public WordParser(Download download) throws URISyntaxException {
        this.download = download;
        parsingModel = Model.getApplicationSettings().getParsingModel();
        wordExtractor = new WordExtractor(this);
        wordExtractor.setWordBuffer(download.getWordBuffer());
        wordQueue = new ArrayList<String>();
    }

    public void parse(int read, byte[] buffer) {
        if (Model.parseWords(download)) {
            wordExtractor.put(buffer, 0, read);
            download.setWordBuffer(wordExtractor.getWordBuffer());
            if(wordQueue.size() >= parsingModel.getWordQueueSize()) {
                pushWordsToDownload();
            }
        }
    }

    public void resetParseFromFile(File file) throws URISyntaxException, FileNotFoundException, IOException {
        if (Model.parseWords(download)) {
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
    
    public void complete() {
        pushWordsToDownload();
    }
    
    private void pushWordsToDownload() {
        download.addWords(wordQueue);
        wordQueue.clear();
    }

    public void reset() throws URISyntaxException {
        wordExtractor = new WordExtractor(this);
        download.setWordBuffer("");
        download.clearWords();
        wordQueue.clear();
    }

    @Override
    public void eatWord(String word) {
        wordQueue.add(word);
    }
}
