/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.view.converter;

import penny.downloadmanager.model.DownloadData;
import penny.downloadmanager.model.gui.SavingModel.FileExistsAction;
import penny.recmd5.MD5State;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author john
 */
public class NumberToSizeConverter extends Converter<Long, Integer> {

    private DownloadData d;

    public NumberToSizeConverter() {
        
    }

    public NumberToSizeConverter(DownloadData d) {
        this.d = d;
    }


    @Override
    public Integer convertForward(Long s) {
        return (int) ((s / (float) d.getSize()) * 10000);
    }

    @Override
    public Long convertReverse(Integer t) {
        return (long) t * d.getSize() / 10000;
    }

}
