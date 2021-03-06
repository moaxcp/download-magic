/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.util;

import javax.swing.*;
import java.awt.*;
import java.io.*;

import java.lang.reflect.*;
import com.github.moaxcp.downloadmanager.view.ExceptionMessageView;

public class SwingExceptionHandler implements
        Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        if (SwingUtilities.isEventDispatchThread()) {
            showMessage(t, e);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        showMessage(t, e);
                    }
                });
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } catch (InvocationTargetException ite) {
                // not much more we can do here except log the exception

                ite.getCause().printStackTrace();
            }
        }
    }

    private String generateStackTrace(Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        pw.close();
        return writer.toString();
    }

    private void showMessage(Thread t, Throwable e) {
        String stackTrace = generateStackTrace(e);
        // show an error dialog
        ExceptionMessageView message = new ExceptionMessageView(findActiveOrVisibleFrame(), true, new ExceptionMessage(t.toString(), stackTrace));

        message.setVisible(true);
    }

    /**
     * We look for an active frame and attach ourselves to that.
     */
    private Frame findActiveOrVisibleFrame() {
        Frame[] frames = JFrame.getFrames();
        for (Frame frame : frames) {
            if (frame.isActive()) {
                return frame;
            }
        }
        for (Frame frame : frames) {
            if (frame.isVisible()) {
                return frame;
            }
        }
        return null;
    }
}
