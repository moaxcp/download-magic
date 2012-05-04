/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.gui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 *
 * @author john
 */
public class SavingModel implements Serializable {

    public enum FileExistsAction implements Serializable {OVERWRITE, COMPLETE }

    public static final String PROP_SAVE= "save";
    private boolean save;
    public static final String PROP_TEMPFOLDER = "tempFolder";
    private String tempFolder;
    public static final String PROP_SAVEFOLDER = "saveFolder";
    private String saveFolder;
    public static final String PROP_DEFAULTFILENAME = "defaultFileName";
    private String defaultFileName;
    public static final String PROP_SAVENAMEFORMAT = "saveNameFormat";
    private String saveNameFormat;
    public static final String PROP_TEMPNAMEFORMAT = "tempNameFormat";
    private String tempNameFormat;
    public static final String PROP_SAVEUNKNOWN = "saveUnknown";
    private boolean saveUnknown;
    public static final String PROP_TEMPEXISTSACTION = "tempExistsAction";
    private FileExistsAction tempExistsAction;
    public static final String PROP_SAVEEXISTSACTION = "saveExistsAction";
    private FileExistsAction saveExistsAction;
    public static final String PROP_SAVETYPES = "saveTypes";
    private EventList<String> saveTypes;

    private transient PropertyChangeSupport propertySupport;

    public SavingModel() {
        save = true;
        tempFolder = "tempDownloads";
        saveFolder = "saveDownloads";
        defaultFileName = "index.html";
        tempNameFormat = "${host}" + "${path}" + File.separatorChar + "${file}";
        saveNameFormat = "${host}" + "${path}" + File.separatorChar + "${file}";
        saveUnknown = true;
        tempExistsAction = FileExistsAction.COMPLETE;
        saveExistsAction = FileExistsAction.COMPLETE;
        saveTypes = new BasicEventList<String>();
        saveTypes.add("*");
        propertySupport = new SwingPropertyChangeSupport(this, true);
    }

    public SavingModel(SavingModel appSettings) {
        this.copy(appSettings);
    }

    public void copy(SavingModel appSettings) {
        this.setSave(appSettings.isSave());
        this.setDefaultFileName(appSettings.defaultFileName);
        this.setSaveFolder(appSettings.saveFolder);
        this.setSaveNameFormat(appSettings.saveNameFormat);
        this.setTempFolder(appSettings.tempFolder);
        this.setTempNameFormat(appSettings.tempNameFormat);
        this.setSaveUnknown(appSettings.isSaveUnknown());
        this.setTempExistsAction(appSettings.getTempExistsAction());
        this.setSaveExistsAction(appSettings.getSaveExistsAction());
        saveTypes.clear();
        saveTypes.addAll(appSettings.getSaveTypes());
    }

    /**
     * @return the save
     */
    public boolean isSave() {
        return save;
    }

    /**
     * @param save the save to set
     */
    public void setSave(boolean save) {
        this.save = save;
    }

    /**
     * @return the tempFolder
     */
    public synchronized String getTempFolder() {
        return tempFolder;
    }

    /**
     * @param tempFolder the tempFolder to set
     */
    public synchronized void setTempFolder(String tempFolder) {
        String oldValue = this.tempFolder;
        this.tempFolder = tempFolder;
        propertySupport.firePropertyChange(PROP_TEMPFOLDER, oldValue, tempFolder);
    }

    /**
     * @return the saveFolder
     */
    public synchronized String getSaveFolder() {
        return saveFolder;
    }

    /**
     * @param saveFolder the saveFolder to set
     */
    public synchronized void setSaveFolder(String saveFolder) {
        String oldValue = this.saveFolder;
        this.saveFolder = saveFolder;
        propertySupport.firePropertyChange(PROP_SAVEFOLDER, oldValue, saveFolder);
    }

    /**
     * @return the defaultFileName
     */
    public synchronized String getDefaultFileName() {
        return defaultFileName;
    }

    /**
     * @param defaultFileName the defaultFileName to set
     */
    public synchronized void setDefaultFileName(String defaultFileName) {
        String oldValue = this.defaultFileName;
        this.defaultFileName = defaultFileName;
        propertySupport.firePropertyChange(PROP_DEFAULTFILENAME, oldValue, defaultFileName);
    }

    /**
     * @return the fileNameFormat
     */
    public synchronized String getSaveNameFormat() {
        return saveNameFormat;
    }

    /**
     * @param fileNameFormat the fileNameFormat to set
     */
    public synchronized void setSaveNameFormat(String saveNameFormat) {
        String oldValue = this.saveNameFormat;
        this.saveNameFormat = saveNameFormat;
        propertySupport.firePropertyChange(PROP_SAVENAMEFORMAT, oldValue, saveNameFormat);
    }

    /**
     * @return the tempNameFormat
     */
    public String getTempNameFormat() {
        return tempNameFormat;
    }

    /**
     * @param tempNameFormat the tempNameFormat to set
     */
    public void setTempNameFormat(String tempNameFormat) {
        String oldValue = this.tempNameFormat;
        this.tempNameFormat = tempNameFormat;
        propertySupport.firePropertyChange(PROP_TEMPNAMEFORMAT, oldValue, tempNameFormat);
    }

    /**
     * @return the saveUnknown
     */
    public boolean isSaveUnknown() {
        return saveUnknown;
    }

    /**
     * @param saveUnknown the saveUnknown to set
     */
    public void setSaveUnknown(boolean saveUnknown) {
        boolean oldValue = this.saveUnknown;
        this.saveUnknown = saveUnknown;
        propertySupport.firePropertyChange(PROP_SAVEUNKNOWN, oldValue, saveUnknown);
    }

    /**
     * @return the saveTypes
     */
    public EventList<String> getSaveTypes() {
        return saveTypes;
    }

    /**
     * @param saveTypes the saveTypes to set
     */
    public void setSaveTypes(EventList<String> saveTypes) {
        EventList<String> oldValue = this.saveTypes;
        this.saveTypes = saveTypes;
        propertySupport.firePropertyChange(PROP_SAVETYPES, oldValue, saveTypes);
    }

    /**
     * @return the tempExistsAction
     */
    public FileExistsAction getTempExistsAction() {
        return tempExistsAction;
    }

    /**
     * @param tempExistsAction the tempExistsAction to set
     */
    public void setTempExistsAction(FileExistsAction tempExistsAction) {
        System.out.println("set temp exists " + tempExistsAction);
        FileExistsAction oldValue = this.tempExistsAction;
        this.tempExistsAction = tempExistsAction;
        propertySupport.firePropertyChange(PROP_TEMPEXISTSACTION, oldValue, tempExistsAction);
    }

    /**
     * @return the saveExistsAction
     */
    public FileExistsAction getSaveExistsAction() {
        return saveExistsAction;
    }

    /**
     * @param saveExistsAction the saveExistsAction to set
     */
    public void setSaveExistsAction(FileExistsAction saveExistsAction) {
        FileExistsAction oldValue = this.saveExistsAction;
        this.saveExistsAction = saveExistsAction;
        propertySupport.firePropertyChange(PROP_SAVEEXISTSACTION, oldValue, saveExistsAction);
    }
}
