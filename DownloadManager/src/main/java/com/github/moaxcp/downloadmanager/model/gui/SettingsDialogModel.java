/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.model.gui;

import com.github.moaxcp.downloadmanager.model.ApplicationSettingsModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author john
 */
public class SettingsDialogModel {
    public static final String PROP_SETTINGS = "settings";
    private ApplicationSettingsModel appSettings;
    private ApplicationSettingsModel appSettingsCopy;

    public static final String PROP_NAMEFORMAT = "nameFormat";
    private String nameFormat;

    public static final String PROP_NAMETYPE = "nameType";
    private String nameType;

    public static final String TYPE_SAVE = "save";
    public static final String TYPE_TEMP = "temp";

    public static final String PROP_VISIBLE = "visible";
    private boolean visible;

    private String saveSelected;
    private String downloadSelected;
    private String linkSelected;
    private String wordSelected;
    private String md5Selected;

    private PropertyChangeSupport propertySupport;

    public SettingsDialogModel() {
        appSettings = new ApplicationSettingsModel();
        appSettingsCopy = new ApplicationSettingsModel();
        visible = false;
        propertySupport = new PropertyChangeSupport(this);
    }

    /**
     * @return the appSettings
     */
    public ApplicationSettingsModel getAppSettings() {
        return appSettings;
    }

    /**
     * @param appSettings the appSettings to set
     */
    public void setAppSettings(ApplicationSettingsModel appSettings) {
        ApplicationSettingsModel oldValue = this.getAppSettings();
        this.appSettings = appSettings;
        propertySupport.firePropertyChange(PROP_SETTINGS, oldValue, appSettings);
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        boolean oldValue = this.visible;
        if(visible) {
            this.initAppSettingsCopy();
        }
        this.visible = visible;
        propertySupport.firePropertyChange(PROP_VISIBLE, oldValue, visible);
    }

    public void initAppSettingsCopy() {
        appSettingsCopy.copy(getAppSettings());
    }

    public ApplicationSettingsModel getAppSettingsCopy() {
        return appSettingsCopy;
    }

    /**
     * @return the nameFormat
     */
    public String getNameFormat() {
        return nameFormat;
    }

    /**
     * @param nameFormat the nameFormat to set
     */
    public void setNameFormat(String nameFormat) {
        String oldValue = this.nameFormat;
        this.nameFormat = nameFormat;
        propertySupport.firePropertyChange(PROP_NAMEFORMAT, oldValue, nameFormat);
    }

    /**
     * @return the nameType
     */
    public String getNameType() {
        return nameType;
    }

    /**
     * @param nameType the nameType to set
     */
    public void setNameType(String nameType) {
        String oldValue = this.nameType;
        this.nameType = nameType;
        propertySupport.firePropertyChange(PROP_NAMETYPE, oldValue, nameType);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    /**
     * @return the saveSelected
     */
    public String getSaveSelected() {
        return saveSelected;
    }

    /**
     * @param saveSelected the saveSelected to set
     */
    public void setSaveSelected(String saveSelected) {
        this.saveSelected = saveSelected;
    }

    /**
     * @return the downloadSelected
     */
    public String getDownloadSelected() {
        return downloadSelected;
    }

    /**
     * @param downloadSelected the downloadSelected to set
     */
    public void setDownloadSelected(String downloadSelected) {
        this.downloadSelected = downloadSelected;
    }

    /**
     * @return the linkSelected
     */
    public String getLinkSelected() {
        return linkSelected;
    }

    /**
     * @param linkSelected the linkSelected to set
     */
    public void setLinkSelected(String linkSelected) {
        this.linkSelected = linkSelected;
    }

    /**
     * @return the wordSelected
     */
    public String getWordSelected() {
        return wordSelected;
    }

    /**
     * @param wordSelected the wordSelected to set
     */
    public void setWordSelected(String wordSelected) {
        this.wordSelected = wordSelected;
    }

    /**
     * @return the md5Selected
     */
    public String getMd5Selected() {
        return md5Selected;
    }

    /**
     * @param md5Selected the md5Selected to set
     */
    public void setMd5Selected(String md5Selected) {
        this.md5Selected = md5Selected;
    }
}
