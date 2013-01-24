/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import penny.download.AbstractDownload;
import penny.download.DownloadProcessor;
import penny.download.DownloadStatus;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.db.Download;
import penny.downloadmanager.util.Util;

/**
 *
 * @author john
 */
public class Processor implements DownloadProcessor {

    private Download download;
    private LinkParser linkParser;
    private WordParser wordParser;
    private MD5er md5er;
    private FileSaver saver;
    private boolean previouslyMd5ed;
    private boolean wordsParsed;
    private boolean linksParsed;

    public Processor() {
    }

    @Override
    public void onInit(AbstractDownload d) {
        try {
            this.download = (Download) d;
            linkParser = new LinkParser(download);
            wordParser = new WordParser(download);
            md5er = new MD5er(download);
            previouslyMd5ed = Model.generateMD5(download);
            linksParsed = Model.parseLinks(download);
            wordsParsed = Model.parseWords(download);
            saver = new FileSaver(download);
        } catch (URISyntaxException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onReset() {
        try {
            linkParser.reset();
            wordParser.reset();
            md5er.reset();
            saver.reset();
        } catch (URISyntaxException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void onPrepare() {
        try {
            saver.prepare();
            if (!previouslyMd5ed) {
                download.setMessage("Md5ing file");
                md5er.resetMD5FromFile(saver.getTempFile());
                download.setMessage("finished Md5ing file");
                previouslyMd5ed = Model.generateMD5(download);
            }
            if (!linksParsed) {
                download.setMessage("parsing links");
                linkParser.resetParseFromFile(saver.getTempFile());
                download.setMessage("finished parsing links");
                linksParsed = Model.parseLinks(download);
            }
            if (!wordsParsed) {
                download.setMessage("parsing words");
                wordParser.resetParseFromFile(saver.getTempFile());
                download.setMessage("finished parsing words");
                wordsParsed = Model.parseWords(download);
            }
        } catch (URISyntaxException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void doChunck(int read, byte[] buffer) {
        try {
            linkParser.parse(read, buffer);
            wordParser.parse(read, buffer);
            md5er.update(read, buffer);
            saver.save(read, buffer);
        } catch (IOException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onFinalize() {
        try {
            saver.checkFileType();
            if (!previouslyMd5ed) {
                download.setMessage("Md5ing file");
                md5er.resetMD5FromFile(saver.getTempFile());
                download.setMessage("finished Md5ing file");
            }
            if (!linksParsed) {
                download.setMessage("parsing links");
                linkParser.resetParseFromFile(saver.getTempFile());
                download.setMessage("finished parsing links");
                linksParsed = Model.parseLinks(download);
            }
            if (!wordsParsed) {
                download.setMessage("parsing words");
                wordParser.resetParseFromFile(saver.getTempFile());
                download.setMessage("finished parsing words");
                wordsParsed = Model.parseWords(download);
            }
            if (download.getContentType().contains("image")) {
                download.setMessage("getting image info");
                Map<String, Object> imageInfo = Util.getImageInfo(saver.getTempFile());
                download.setMessage("finished getting image info");
                download.addExtraProperties(imageInfo);
            }
            md5er.complete();
            saver.complete();
        } catch (URISyntaxException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            download.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
