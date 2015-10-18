/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.view.converter;

import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author john
 */
public class NanoToSecondConverter extends Converter<Long, Long> {


    @Override
    public Long convertForward(Long s) {
        return s / 1000000000l;
    }

    @Override
    public Long convertReverse(Long t) {
        return t * 1000000000l;
    }

}
