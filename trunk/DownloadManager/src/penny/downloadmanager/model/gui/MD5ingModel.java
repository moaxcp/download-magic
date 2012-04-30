/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.gui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import java.io.Serializable;

/**
 *
 * @author john
 */
public class MD5ingModel implements Serializable {
    public static final String PROP_GENERATEMD5 = "generateMD5";
    private boolean generateMD5;
    
    public static final String PROP_UPDATEMD5 = "updateMD5";
    private boolean updateMD5;

    public static final String PROP_MD5UNKNOWN = "md5Unknown";
    private boolean md5Unknown;

    public static final String PROP_MD5TYPES = "md5Types";
    private EventList<String> md5Types;

    public MD5ingModel() {
        generateMD5 = true;
        updateMD5 = true;
        md5Unknown = true;
        md5Types = new BasicEventList<String>();
        md5Types.add("*");
    }

    public MD5ingModel(MD5ingModel model) {
        this.copy(model);
    }

    public void copy(MD5ingModel model) {
        this.generateMD5 = model.isGenerateMD5();
        this.updateMD5 = model.isUpdateMD5();
        this.md5Unknown = model.isMd5Unknown();
        md5Types.clear();
        md5Types.addAll(model.getMd5Types());
    }

    /**
     * @return the generateMD5
     */
    public boolean isGenerateMD5() {
        return generateMD5;
    }

    /**
     * @param generateMD5 the generateMD5 to set
     */
    public void setGenerateMD5(boolean generateMD5) {
        this.generateMD5 = generateMD5;
    }

    /**
     * @return the updateMD5
     */
    public boolean isUpdateMD5() {
        return updateMD5;
    }

    /**
     * @param updateMD5 the updateMD5 to set
     */
    public void setUpdateMD5(boolean updateMD5) {
        this.updateMD5 = updateMD5;
    }

    /**
     * @return the md5Unknown
     */
    public boolean isMd5Unknown() {
        return md5Unknown;
    }

    /**
     * @param md5Unknown the md5Unknown to set
     */
    public void setMd5Unknown(boolean md5Unknown) {
        this.md5Unknown = md5Unknown;
    }

    /**
     * @return the md5Types
     */
    public EventList<String> getMd5Types() {
        return md5Types;
    }
}
