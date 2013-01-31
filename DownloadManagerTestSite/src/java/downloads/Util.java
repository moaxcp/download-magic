/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloads;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
public class Util {
    public static List<String> getAllPaths(File file) {
        List<String> list = new ArrayList<String>();
        if(file.isFile()) {
            throw new IllegalArgumentException("file is a file");
        }
        for(File f : file.listFiles()) {
            if(f.isDirectory()) {
                list.addAll(getAllPaths(f));
            } else {
                list.add(f.getPath());
            }
        }
        return list;
    }
}
