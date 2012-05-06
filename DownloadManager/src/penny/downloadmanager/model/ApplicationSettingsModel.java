/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import penny.downloadmanager.model.gui.MD5ingModel;
import penny.downloadmanager.model.gui.DownloadingModel;
import penny.downloadmanager.model.gui.ParsingModel;
import penny.downloadmanager.model.gui.SavingModel;
import penny.downloadmanager.model.gui.ImageModel;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author john
 */
public class ApplicationSettingsModel implements Serializable {

    private SavingModel savingModel;
    private DownloadingModel downloadingModel;
    private ParsingModel parsingModel;
    private MD5ingModel md5ingModel;
    private ImageModel imageModel;
    private StartupModel startupModel;

    public ApplicationSettingsModel() {
        savingModel = new SavingModel();
        downloadingModel = new DownloadingModel();
        parsingModel = new ParsingModel();
        md5ingModel = new MD5ingModel();
        imageModel = new ImageModel();
        startupModel = new StartupModel();
    }

    public ApplicationSettingsModel(ApplicationSettingsModel appSettings) {
        this.copy(appSettings);
    }

    public void copy(ApplicationSettingsModel appSettings) {
        this.savingModel.copy(appSettings.getSavingModel());
        this.downloadingModel.copy(appSettings.getDownloadingModel());
        this.parsingModel.copy(appSettings.getParsingModel());
        this.md5ingModel.copy(appSettings.getMd5ingModel());
        this.imageModel.copy(appSettings.getImageModel());
        this.startupModel.copy(appSettings.getStartupModel());
    }

    /**
     * @return the savingModel
     */
    public SavingModel getSavingModel() {
        return savingModel;
    }

    /**
     * @return the downloadSettings
     */
    public DownloadingModel getDownloadingModel() {
        return downloadingModel;
    }

    /**
     * @return the parsingModel
     */
    public ParsingModel getParsingModel() {
        return parsingModel;
    }

    /**
     * @return the md5ingModel
     */
    public MD5ingModel getMd5ingModel() {
        return md5ingModel;
    }

    /**
     * @return the imageModel
     */
    public ImageModel getImageModel() {
        return imageModel;
    }

    /**
     * @return the startupModel
     */
    public StartupModel getStartupModel() {
        return startupModel;
    }
}
