/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import penny.download.AbstractDownload;
import penny.downloadmanager.model.db.Download;

/**
 *
 * @author john
 */
public class DownloadUtil {

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

    public static String getFileName(Download d, String saveNameFormat, String defaultFileName) {
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
    
}
