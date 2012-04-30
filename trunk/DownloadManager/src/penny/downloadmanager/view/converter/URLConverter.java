/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.view.converter;

import penny.downloadmanager.model.gui.SavingModel.FileExistsAction;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author john
 */
public class URLConverter extends Converter<URL, String> {

    public URLConverter() {
        
    }

    @Override
    public String convertForward(URL s) {
        return s.toString();
    }

    @Override
    public URL convertReverse(String t) {
        try {
            return new URL(t);
        } catch (MalformedURLException ex) {
            Logger.getLogger(URLConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
