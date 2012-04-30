/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class ApplicationSettingsSaver {

    private String saveFile;
    private ApplicationSettingsModel appSettings;

    public ApplicationSettingsSaver(String saveFile) {
        this.saveFile = saveFile;
    }

    public void setApplicationSettings(ApplicationSettingsModel appSettings) {
        this.appSettings = appSettings;
    }

    public ApplicationSettingsModel getApplicationSettings() {
        return appSettings;
    }

    public void save() {
        if(appSettings == null) {
            throw new IllegalStateException("appSettings must be set before being saved");
        }
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            File file = new File(saveFile);
            file.getParentFile().mkdirs();
            fout = new FileOutputStream(file);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(appSettings);
            Logger.getLogger(ApplicationSettingsSaver.class.getName()).log(Level.INFO, "appSettings have been saved {0}", appSettings);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ApplicationSettingsSaver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ApplicationSettingsSaver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(oos != null) {
                    oos.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ApplicationSettingsSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if(fout != null) {
                    fout.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ApplicationSettingsSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void load() {
        File file = new File(saveFile);
        if(!file.exists()) {
            save();
        }
        FileInputStream fin = null;
        ObjectInputStream ois = null;
        try {
            fin = new FileInputStream(saveFile);
            ois = new ObjectInputStream(fin);
            appSettings.copy((ApplicationSettingsModel) ois.readObject());
            Logger.getLogger(ApplicationSettingsSaver.class.getName()).log(Level.INFO, "appSettings have been loaded {0}", appSettings);
        }catch (FileNotFoundException ex) {
            Logger.getLogger(ApplicationSettingsSaver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ApplicationSettingsSaver.class.getName()).log(Level.SEVERE, null, ex);
        }  catch(ClassNotFoundException ex) {
            Logger.getLogger(ApplicationSettingsSaver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(fin != null) {
                    fin.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ApplicationSettingsSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if(ois != null) {
                    ois.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ApplicationSettingsSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
