/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.view.converter;

import com.github.moaxcp.downloadmanager.model.db.Download;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author john
 */
public class NumberToSizeConverter extends Converter<Long, Integer> {

    private Download d;

    public NumberToSizeConverter() {
        
    }

    public NumberToSizeConverter(Download d) {
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
