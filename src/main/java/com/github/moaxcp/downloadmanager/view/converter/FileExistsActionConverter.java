/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.view.converter;

import com.github.moaxcp.downloadmanager.model.gui.SavingModel.FileExistsAction;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author john
 */
public class FileExistsActionConverter extends Converter<FileExistsAction, Boolean> {
    
    private FileExistsAction action;

    public FileExistsActionConverter() {
        
    }

    public FileExistsActionConverter(FileExistsAction action) {
        this.action = action;
    }

    @Override
    public Boolean convertForward(FileExistsAction s) {
        return action.equals(s) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public FileExistsAction convertReverse(Boolean t) {
        return t.equals(Boolean.TRUE) ? action : null;
    }

}
