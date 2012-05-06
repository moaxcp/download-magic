/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.di;

import penny.download.Download;
import penny.download.DownloadStatus;
import penny.download.DownloadProcessor;
import penny.downloadmanager.model.ApplicationSettingsModel;
import penny.downloadmanager.model.DownloadData;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.gui.SavingModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.FileDataSource;
import penny.download.Downloads;

/**
 *
 * @author john
 */
public class TempFileSaver implements DownloadProcessor {

    private OutputStream out;
    private SavingModel savingModel;
    private File save;
    private File temp;

    public TempFileSaver() {
        this.savingModel = Model.getApplicationSettings().getSavingModel();
    }

    private void closeFile(Download d) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException ex) {
            d.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(TempFileSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onInit(Download d) {
        DownloadData i = (DownloadData) d;
        save = new File(savingModel.getSaveFolder() + "/" + DownloadData.getFileName(i, savingModel.getSaveNameFormat(), savingModel.getDefaultFileName()));
        temp = new File(savingModel.getTempFolder() + "/" + DownloadData.getFileName(i, savingModel.getTempNameFormat(), savingModel.getDefaultFileName()));

        if (save.exists()) {
            i.setDownloaded(save.length());
            switch (Model.getApplicationSettings().getSavingModel().getSaveExistsAction()) {
                case OVERWRITE:
                    Model.remove(save);
                    Logger.getLogger(TempFileSaver.class.getName()).fine("Overwriting saveFile " + save);
                    break;
                case COMPLETE:
                    Model.remove(temp);
                    i.setStatus(DownloadStatus.STOPPED, "");
                    i.setStatus(DownloadStatus.QUEUED, "");
                    i.setStatus(DownloadStatus.COMPLETE, "Save file exists. Download complete.");
                    Logger.getLogger(TempFileSaver.class.getName()).fine("Save file exists. Download complete.");
                    break;
            }
        }

        if (temp.exists()) {
            Logger.getLogger(TempFileSaver.class.getName()).fine("temp file exists and is " + Downloads.formatByteSize(temp.length()));
            i.setDownloaded(temp.length());
            switch (savingModel.getTempExistsAction()) {
                case OVERWRITE:
                    Logger.getLogger(TempFileSaver.class.getName()).fine("overwrite tempFile " + temp);
                    Model.remove(temp);
                    break;
                case COMPLETE:
                    Logger.getLogger(TempFileSaver.class.getName()).fine("continuing with tempFile " + temp);
                    break;
            }
        } else if(!save.exists()) {
            Logger.getLogger(TempFileSaver.class.getName()).fine("temp file does not exist");
            i.setDownloaded(0);
            i.setTempPath(savingModel.getTempFolder() + "/" + DownloadData.getFileName(i, savingModel.getTempNameFormat(), savingModel.getDefaultFileName()));
        }
    }

    @Override
    public boolean onCheck(Download d) {
        boolean r = false;
        DownloadData i = (DownloadData) d;

        if (save.exists()) {
            r = i.getDownloaded() == save.length() && i.getStatus() == DownloadStatus.COMPLETE;
        } else {
            if (temp.exists()) {
                switch (savingModel.getTempExistsAction()) {
                    case OVERWRITE:
                        r = false;
                        break;
                    case COMPLETE:
                        r = true;
                        break;
                }
                if (temp.length() != d.getDownloaded()) {
                    r = false;
                }

            } else {
                if (i.getDownloaded() > 0) {
                    r = false;
                } else {
                    r = true;
                }
            }
        }
        return r;
    }

    @Override
    public void onStartInput(Download d) {
        if (Model.save(d)) {
            DownloadData i = (DownloadData) d;
            File file = new File(i.getTempPath());
            String realPath = savingModel.getTempFolder() + "/" + DownloadData.getFileName(i, savingModel.getTempNameFormat(), savingModel.getDefaultFileName());
            File newFile = new File(realPath);

            if (file.exists() && !file.equals(newFile)) {
                newFile.getParentFile().mkdirs();
                file.renameTo(newFile);
                i.setTempPath(savingModel.getTempFolder() + "/" + DownloadData.getFileName(i, savingModel.getTempNameFormat(), savingModel.getDefaultFileName()));
            } else {
                newFile.getParentFile().mkdirs();
                i.setTempPath(savingModel.getTempFolder() + "/" + DownloadData.getFileName(i, savingModel.getTempNameFormat(), savingModel.getDefaultFileName()));
            }

            try {
                out = new FileOutputStream(i.getTempPath(), true);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TempFileSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void doChunck(Download d, int read, byte[] buffer) {
        try {
            if (Model.save(d)) {
                out.write(buffer, 0, read);
            }
        } catch (IOException ex) {
            d.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(TempFileSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onReset(Download d) {
        Model.remove(save);
        Model.remove(temp);
    }

    @Override
    public void onEndInput(Download d) {
        closeFile(d);
    }

    @Override
    public void onCompleted(Download d) {

        DownloadData i = (DownloadData) d;
        File file = new File(i.getTempPath());
        if (file.exists()) {
            if (i.getContentType() == null || i.getContentType().equals("")) {
                FileDataSource dataSource = new FileDataSource(file);
                i.setContentType(dataSource.getContentType());
            }
        } else {
            file = new File(i.getSavePath());
            if (file.exists() && (i.getContentType() == null || i.getContentType().equals(""))) {
                FileDataSource dataSource = new FileDataSource(file);
                i.setContentType(dataSource.getContentType());
            }
        }

        i.setSavePath(savingModel.getSaveFolder() + "/" + DownloadData.getFileName(i, savingModel.getSaveNameFormat(), savingModel.getDefaultFileName()));
        temp = new File(i.getTempPath());
        save = new File(i.getSavePath());

        if (Model.save(d)) {
            closeFile(d);

            save.getParentFile().mkdirs();
            temp.renameTo(save);
            Model.remove(temp);
        } else {
            Model.remove(temp);
            Model.remove(save);
        }
    }
}
