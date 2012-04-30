/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.lang.String;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author john
 */
public class UrlGrabbers {

    public static List<URL> parseWhitespaceCommaFile(String filename) throws FileNotFoundException, MalformedURLException, IOException {
        List<URL> list = new ArrayList<URL>();
        LineNumberReader lr = new LineNumberReader(new BufferedReader(new FileReader(new File(filename))));
        String line = lr.readLine();
        while (line != null) {
            List<String> s = Arrays.asList(line.split("[\\s,]+"));
            for (String i : s) {
                try {
                    list.add(new URL(i));
                } catch(MalformedURLException ex) {
                    
                }
            }
            line = lr.readLine();
        }
        lr.close();
        return list;
    }
}
