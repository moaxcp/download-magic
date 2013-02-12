/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import penny.download.AbstractDownload;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.db.Download;
import penny.recmd5.MD5MessageDigest;
import penny.recmd5.MD5State;

/**
 *
 * @author john
 */
public class Util {

    public static String insertProperties(Download d, String statement) {
        String str = statement;
        Pattern pat = Pattern.compile("\\$\\{\\w+\\}");
        Matcher mat = pat.matcher(str);
        while (mat.find()) {
            String prop = mat.group();
            prop = prop.replace("${", "");
            prop = prop.replace("}", "");
            str = str.replace("${" + prop + "}", d.getProperty(prop).toString());
        }
        return str;
    }

    public static String getTempFile(Download d) {
        return Model.getApplicationSettings().getSavingModel().getTempFolder() + File.separator + getFileName(d, Model.getApplicationSettings().getSavingModel().getTempNameFormat(), Model.getApplicationSettings().getSavingModel().getDefaultFileName());
    }

    public static String getSaveFile(Download d) {
        return Model.getApplicationSettings().getSavingModel().getSaveFolder() + File.separator + getFileName(d, Model.getApplicationSettings().getSavingModel().getSaveNameFormat(), Model.getApplicationSettings().getSavingModel().getDefaultFileName());
    }

    private static String getFileName(Download d, String saveNameFormat, String defaultFileName) {
        String str = saveNameFormat;
        Pattern pat = Pattern.compile("\\$\\{\\w+\\}");
        Matcher mat = pat.matcher(str);
        while (mat.find()) {
            String prop = mat.group();
            prop = prop.replace("${", "");
            prop = prop.replace("}", "");
            if (prop.equals(AbstractDownload.PROP_FILE) && d.getProperty(prop).equals("")) {
                str = str.replace("${" + prop + "}", defaultFileName);
            } else {
                str = str.replace("${" + prop + "}", d.getProperty(prop).toString());
            }
        }
        return str;
    }

    public static void remove(File f) {
        if (f != null) {
            if (f.isFile()) {
                f.delete();
                Logger.getLogger(Util.class.getName()).finer("deleted file " + f.getName());
                remove(f.getParentFile());
            } else if (f.isDirectory()) {
                if (f.listFiles().length == 0) {
                    f.delete();
                    Logger.getLogger(Util.class.getName()).finer("deleted directory " + f.getName());
                    remove(f.getParentFile());
                }
            }
        }
    }

    public static Map<String, Object> getImageInfo(File file) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        BufferedImage image = ImageIO.read(file);
        if (image != null) {
            map.put(Model.getApplicationSettings().getImageModel().getHeightName(), image.getHeight());
            map.put(Model.getApplicationSettings().getImageModel().getWidthName(), image.getWidth());
        }
        return map;
    }

    public static MD5State getMD5State(File file) {
        InputStream in = null;
        try {
            MD5MessageDigest md5 = new MD5MessageDigest();
            in = new FileInputStream(file);
            byte[] buffer = new byte[10240];
            int read = in.read(buffer);
            while (read != -1) {
                md5.update(buffer, 0, read);
                read = in.read(buffer);
            }
            md5.digest();
            return md5.getState();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
