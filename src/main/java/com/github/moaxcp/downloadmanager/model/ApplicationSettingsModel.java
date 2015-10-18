/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.model;

import com.github.moaxcp.downloadmanager.view.ColumnStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.github.moaxcp.downloadmanager.model.db.Download;
import com.github.moaxcp.downloadmanager.model.gui.DownloadingModel;
import com.github.moaxcp.downloadmanager.model.gui.ImageModel;
import com.github.moaxcp.downloadmanager.model.gui.MD5ingModel;
import com.github.moaxcp.downloadmanager.model.gui.ParsingModel;
import com.github.moaxcp.downloadmanager.model.gui.SavingModel;
import com.github.moaxcp.downloadmanager.view.renderer.ProgressRenderer;

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
    private LookAndFeelModel lookModel;
    private List<ColumnStatus> columns;
    private String sortState;

    public ApplicationSettingsModel() {
        savingModel = new SavingModel();
        downloadingModel = new DownloadingModel();
        parsingModel = new ParsingModel();
        md5ingModel = new MD5ingModel();
        imageModel = new ImageModel();
        startupModel = new StartupModel();
        lookModel = new LookAndFeelModel();
        columns = new ArrayList<ColumnStatus>();
        int i = 0;
        for (String s : Download.propertyNames) {
            ColumnStatus c  = new ColumnStatus(s, true);
            columns.add(c);
            c.setViewOrder(i);
            c.setVisible(true);
            i++;
        }
        sortState = "";
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
        this.lookModel.copy(appSettings.getLookModel());
        for (ColumnStatus c : appSettings.getColumns()) {
            for (ColumnStatus d : columns) {
                if (d.getName().equals(c.getName())) {
                    d.setViewOrder(c.getViewOrder());
                    d.setVisible(c.isVisible());
                    d.setWidth(c.getWidth());
                    break;
                }
            }
        }
        this.sortState = appSettings.getSortState();
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

    public LookAndFeelModel getLookModel() {
        return lookModel;
    }

    public List<ColumnStatus> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnStatus> list) {
        columns = list;
    }
    
    public String getSortState() {
        return sortState;
    }
    
    public void setSortState(String sortState) {
        this.sortState = sortState;
    }
}
