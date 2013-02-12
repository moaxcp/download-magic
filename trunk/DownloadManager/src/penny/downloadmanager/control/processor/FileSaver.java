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
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public FileSaver(Download download) {
        this.download = download;
        this.savingModel = Model.getApplicationSettings().getSavingModel();
        initFiles();
        download.setDownloaded(temp.length());
    }

    public File getSaveFile() {
        return save;
    }

    public File getTempFile() {
        return temp;
    }

    private File setupCorrectFile(String filePath, String newFilePath) {
        File file = new File(filePath);
        File newFile = new File(newFilePath);

        if (file.exists() && !file.equals(newFile)) {
            newFile.getParentFile().mkdirs();
            File delete = new File(file.getPath());
            file.renameTo(newFile);
            Util.remove(delete.getParentFile());
        } else if(file.getParentFile() != null && file.getParentFile().exists() && !file.equals(newFile)) {
            Util.remove(file.getParentFile());
            newFile.getParentFile().mkdirs();
        } else {
            newFile.getParentFile().mkdirs();
        }
        
        return newFile;
    }
    
    private void initFiles() {
        temp = setupCorrectFile(download.getTempPath(), Util.getTempFile(download));
        download.setTempPath(Util.getTempFile(download));
        save = setupCorrectFile(download.getSavePath(), Util.getSaveFile(download));
        download.setSavePath(Util.getSaveFile(download));

        if (save.exists()) {
            switch (Model.getApplicationSettings().getSavingModel().getSaveExistsAction()) {
                case OVERWRITE:
                    Util.remove(save);
                    Logger.getLogger(Processor.class.getName()).log(Level.FINE, "Overwriting saveFile {0}", save);
                    break;
                case COMPLETE:
                    //move save to temp and resume download from there.
                    temp.delete();
                    File file = new File(save.getPath());
                    file.renameTo(temp);
                    Util.remove(save.getParentFile());
                    Logger.getLogger(Processor.class.getName()).log(Level.FINE, "Continuing with save file {0}", save);
                    break;
            }
        } else if (temp.exists()) {
            Logger.getLogger(Processor.class.getName()).log(Level.FINE, "temp file exists and is {0}", Downloads.formatByteSize(temp.length()));
            switch (savingModel.getTempExistsAction()) {
                case OVERWRITE:
                    Logger.getLogger(Processor.class.getName()).log(Level.FINE, "overwrite tempFile {0}", temp);
                    Util.remove(temp);
                    break;
                case COMPLETE:
                    Logger.getLogger(Processor.class.getName()).log(Level.FINE, "continuing with tempFile {0}", temp);
                    break;
            }
        }
        
        if(save.getParentFile().exists()) {
            Util.remove(save.getParentFile());
        }
    }

    private void closeFile() throws IOException {
        if (out != null) {
            out.close();
        }
    }

    public void reset() {
        try {
            closeFile();
        } catch (IOException ex) {
            Logger.getLogger(FileSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
        Util.remove(save);
        Util.remove(temp);
        Logger.getLogger(Processor.class.getName()).log(Level.FINE, "Download has been restarted. Removed files.");
    }

    public void prepare() throws FileNotFoundException {
        temp = setupCorrectFile(download.getTempPath(), Util.getTempFile(download));
        download.setTempPath(Util.getTempFile(download));
        save = setupCorrectFile(download.getSavePath(), Util.getSaveFile(download));
        download.setSavePath(Util.getSaveFile(download));
        if(save.getParentFile().exists()) {
            Util.remove(save.getParentFile());
        }
        out = new FileOutputStream(temp, true);
    }

    public void save(int read, byte[] buffer) throws IOException {
        out.write(buffer, 0, read);
    }

    public void checkFileType() {
        temp = setupCorrectFile(download.getTempPath(), Util.getTempFile(download));
        download.setTempPath(Util.getTempFile(download));
        if (temp.exists()) {
            if (download.getContentType() == null || download.getContentType().equals("")) {
                try {
                    download.setContentType(Files.probeContentType(temp.toPath()));
                } catch (IOException ex) {
                    Logger.getLogger(FileSaver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public void complete() throws IOException {
        closeFile();
        
        temp = setupCorrectFile(download.getTempPath(), Util.getTempFile(download));
        download.setTempPath(Util.getTempFile(download));
        save = setupCorrectFile(download.getSavePath(), Util.getSaveFile(download));
        download.setSavePath(Util.getSaveFile(download));
        
        if (Model.save(download)) {
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
