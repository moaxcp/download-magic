/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control;

import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.db.JavaDBDataSource;
import penny.downloadmanager.util.EventRateCounter;
import penny.downloadmanager.util.SwingExceptionHandler;
import penny.downloadmanager.view.View;

/**
 *
 * @author john
 */
public class Application {

    private static boolean shutdown = false;

    public static boolean getShutdown() {
        return shutdown;
    }

    public static void setShutdown(boolean shutdown) {
        Application.shutdown = shutdown;
    }

    public static void startMainWindow() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                Model.getMainWindowModel().setVisible(true);
            }
        });
    }
    
    public static Thread getThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setUncaughtExceptionHandler(new SwingExceptionHandler());
        return t;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        Thread.currentThread().setUncaughtExceptionHandler(new SwingExceptionHandler());
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventRateCounter());
        
        //System.setProperty("socksProxyHost", "localhost");
        //System.setProperty("socksProxyPort", "9050");
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {
                    Model.getTaskSaver().saveList();
                    System.out.println("saved tasks");
                } catch (Exception ex) {
                    System.out.println("could not save tasks");
                    ex.printStackTrace();
                }

                try {
                    Model.getDownloadSaver().saveAllDownloads();
                    System.out.println("saved downloads");
                } catch (Exception ex) {
                    System.out.println("could not save downloads");
                    ex.printStackTrace();
                }
                
                try {
                    JavaDBDataSource.getInstance().shutdownDB();
                    System.out.println("shutdownDB()");
                } catch (Exception ex) {
                    System.out.println("shutdownDB() exception is normal");
                    System.out.println(ex);
                }
            }
        });

        Model.build();
        View.build();
        Control.build();

        Model.loadData();
        View.initLookAndFeel();

        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    Thread.currentThread().setUncaughtExceptionHandler(new SwingExceptionHandler());
                }
            });

        } catch (InterruptedException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!shutdown) {
            startMainWindow();
        } else {
            System.exit(0);
        }
    }
}
