/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.view.converter;

import penny.downloadmanager.model.gui.SavingModel.FileExistsAction;
import penny.recmd5.MD5State;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author john
 */
public class MD5Converter extends Converter<MD5State, String> {
    

    public MD5Converter() {
        
    }

    @Override
    public String convertForward(MD5State s) {
        return s.toString();
    }

    @Override
    public MD5State convertReverse(String t) {
        return null;
    }

}
