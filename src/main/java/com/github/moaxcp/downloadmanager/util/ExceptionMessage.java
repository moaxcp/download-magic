/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.util;

/**
 *
 * @author john
 */
public class ExceptionMessage {
    
    private String threadName = "N/A";
    private String stackTrace = "N/A";
    
    public ExceptionMessage() {
        
    }
    
    public ExceptionMessage(String threadName, String stackTrace) {
        this.threadName = threadName;
        this.stackTrace = stackTrace;
    }
    
    public String getThreadName() {
        return threadName;
    }
    
    public String getStackTrace() {
        return stackTrace;
    }
    
}
