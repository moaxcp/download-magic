/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.FileDataSource;
import penny.download.AbstractDownload;
import penny.download.DownloadProcessor;
import penny.download.DownloadStatus;
import penny.download.Downloads;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.db.Download;
import penny.downloadmanager.model.gui.SavingModel;
import penny.downloadmanager.util.Util;

/**
 *
 * @author john
 */
public class FileSaver {

    private OutputStream out;
    private SavingModel savingModel;
    private File save;
    private File temp;
    private Download download;
    
    public File getSave() {
        return save;
    }

    public FileSaver(Download download) {
        this.download = download;
        this.savingModel = Model.getApplicationSettings().getSavingModel();
        setPaths();

        if (save.exists()) {
            switch (Model.getApplicationSettings().getSavingModel().getSaveExistsAction()) {
                case OVERWRITE:
                    Util.remove(save);
                    Logger.getLogger(Processor.class.getName()).fine("Overwriting saveFile " + save);
                    break;
                case COMPLETE:
                    Util.remove(temp);
                    download.setDownloaded(save.length());
                    break;
            }
        }

        if (temp.exists()) {
            Logger.getLogger(Processor.class.getName()).fine("temp file exists and is " + Downloads.formatByteSize(temp.length()));
            switch (savingModel.getTempExistsAction()) {
                case OVERWRITE:
                    Logger.getLogger(Processor.class.getName()).fine("overwrite tempFile " + temp);
                    Util.remove(temp);
                    break;
                case COMPLETE:
                    download.setDownloaded(temp.length());
                    Logger.getLogger(Processor.class.getName()).fine("continuing with tempFile " + temp);
                    break;
            }
        } else if (!save.exists()) {
            Logger.getLogger(Processor.class.getName()).fine("temp file does not exist");
            download.setDownloaded(0);
        }
    }

    private void setPaths() {
        download.setTempPath(Util.getTempFile(download));
        download.setSavePath(Util.getSaveFile(download));
        temp = new File(download.getTempPath());
        save = new File(download.getSavePath());
    }

    private void closeFile() throws IOException {
        if (out != null) {
            out.close();
        }
    }
    
    public void reset() {
        Util.remove(save);
        Util.remove(temp);
    }

    public void prepare() throws FileNotFoundException {
        if (Model.save(download)) {
            File file = new File(download.getTempPath());
            String realPath = Util.getTempFile(download);
            File newFile = new File(realPath);

            if (file.exists() && !file.equals(newFile)) {
                newFile.getParentFile().mkdirs();
                file.renameTo(newFile);
                download.setTempPath(realPath);
            } else {
                newFile.getParentFile().mkdirs();
                download.setTempPath(Util.getTempFile(download));
            }

            out = new FileOutputStream(download.getTempPath(), true);
        }
    }

    public void save(int read, byte[] buffer) throws IOException {
        if (Model.save(download)) {
            out.write(buffer, 0, read);
        }
    }

    public void complete() throws IOException {
        closeFile();

        File file = new File(download.getTempPath());
        if (file.exists()) {
            if (download.getContentType() == null || download.getContentType().equals("")) {
                FileDataSource dataSource = new FileDataSource(file);
                download.setContentType(dataSource.getContentType());
            }
        } else {
            file = new File(download.getSavePath());
            if (file.exists() && (download.getContentType() == null || download.getContentType().equals(""))) {
                FileDataSource dataSource = new FileDataSource(file);
                download.setContentType(dataSource.getContentType());
            }
        }

        download.setSavePath(Util.getSaveFile(download));
        temp = new File(download.getTempPath());
        save = new File(download.getSavePath());

        if (Model.save(download)) {
            save.getParentFile().mkdirs();
            temp.renameTo(save);
            Util.remove(temp.getParentFile());
            if (temp.exists()) {
                download.setStatus(DownloadStatus.ERROR, "Temp file sill exists");
                Logger.getLogger(FileSaver.class.getName()).log(Level.SEVERE, null, "Temp file sill exists");
            }
        } else {
            Util.remove(temp);
            Util.remove(save);
        }
    }
}
