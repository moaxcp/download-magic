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

/**
 *
 * @author john
 */
public class TempFileSaver implements DownloadProcessor {

    private OutputStream out;
    private SavingModel savingModel;
    private boolean overwriteTemp;
    private File save;
    private File temp;

    public TempFileSaver() {
        this.savingModel = Model.getApplicationSettings().getSavingModel();
        overwriteTemp = false;
    }

    private boolean save(Download d) {
        boolean r = false;
        if (savingModel.isSave()) {
            if (savingModel.isSaveUnknown()) {
                if (d.getContentType() == null || d.getContentType().equals("")) {
                    r = true;
                }
            }
            if (ApplicationSettingsModel.typeMatches(d.getContentType(), savingModel.getSaveTypes())) {
                r = true;
            }
        }
        return r;
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
        overwriteTemp = false;

        if (save.exists()) {
            Model.remove(temp);
            switch (Model.getApplicationSettings().getSavingModel().getSaveExistsAction()) {
                case OVERWRITE:
                    Model.remove(save);
                    i.setDownloaded(0);
                    break;
                case COMPLETE:
                    i.setDownloaded(save.length());
                    i.setStatus(DownloadStatus.STOPPED, "");
                    i.setStatus(DownloadStatus.QUEUED, "");
                    i.setStatus(DownloadStatus.COMPLETE, "Save file exists. Download complete.");
                    break;
            }
        } else {
            if (!temp.exists()) {
                i.setTempPath(savingModel.getTempFolder() + "/" + DownloadData.getFileName(i, savingModel.getTempNameFormat(), savingModel.getDefaultFileName()));
            } else {
                switch (savingModel.getTempExistsAction()) {
                    case OVERWRITE:
                        Model.remove(temp);
                        break;
                    case COMPLETE:
                        break;
                }
                i.setDownloaded(temp.length());
            }
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
                if(i.getDownloaded() > 0) {
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
        if (save(d)) {
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

            switch (savingModel.getTempExistsAction()) {
                case OVERWRITE:
                    try {
                        out = new FileOutputStream(i.getTempPath(), !overwriteTemp);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(TempFileSaver.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case COMPLETE:
                    try {
                        out = new FileOutputStream(i.getTempPath(), true);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(TempFileSaver.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
            }
        }
    }

    @Override
    public void doChunck(Download d, int read, byte[] buffer) {
        try {
            if (save(d)) {
                out.write(buffer, 0, read);
            }
        } catch (IOException ex) {
            d.setStatus(DownloadStatus.ERROR, ex.toString());
            Logger.getLogger(TempFileSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onReset(Download d) {
        overwriteTemp = true;
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

        if (save(d)) {
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
