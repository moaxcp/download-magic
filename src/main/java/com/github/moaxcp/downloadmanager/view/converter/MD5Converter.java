/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.view.converter;

import com.github.moaxcp.recmd5.MD5State;
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
