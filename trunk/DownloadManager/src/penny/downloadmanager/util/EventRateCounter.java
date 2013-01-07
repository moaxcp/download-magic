/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.util;

import penny.util.StopWatch;
import java.awt.AWTEvent;
import java.awt.EventQueue;

/**
 *
 * @author john
 */
public class EventRateCounter extends EventQueue {
    private long count;
    private StopWatch watch;
    
    public EventRateCounter() {
        watch = new StopWatch();
        watch.start();
    }
    
    @Override
    protected void dispatchEvent(AWTEvent event) {
        watch.add();
        if(watch.getTimeMillis() > 1000) {
            float rate = count / ((float) watch.getTimeMillis() / (float)1000);
            System.out.println("swing event rate: " + rate + "/s");
            watch.restart();
            count = 0;
        }
        count++;
        super.dispatchEvent(event);
    }
}
