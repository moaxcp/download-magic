/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.view.converter;

import penny.download.Downloads;
import penny.downloadmanager.model.gui.SavingModel.FileExistsAction;
import penny.recmd5.MD5State;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author john
 */
public class BytesToStringConverter extends Converter<Long, String> {

    @Override
    public String convertForward(Long s) {
        return Downloads.formatByteSize(s);
    }

    @Override
    public Long convertReverse(String t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
