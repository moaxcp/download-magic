/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author john
 */
public class SingleLineFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        return record.getLevel() + ": " + record.getMessage() + "\n";
    }

}
