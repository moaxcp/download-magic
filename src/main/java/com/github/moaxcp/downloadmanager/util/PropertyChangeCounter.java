/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.util;

import penny.download.util.StopWatch;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author john
 */
public class PropertyChangeCounter implements PropertyChangeListener {
    private long count;
    private StopWatch watch;
    private String name;
    
    public PropertyChangeCounter(String name) {
        this.name = name;
        watch = new StopWatch();
        watch.start();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        watch.add();
        if(watch.getTimeMillis() > 1000) {
            float rate = count / ((float) watch.getTimeMillis() / (float)1000);
            //System.out.println(Thread.currentThread().getName() + ": " + name + " event rate: " + rate + "/s");
            watch.restart();
            count = 0;
        }
        count++;
    }
}
