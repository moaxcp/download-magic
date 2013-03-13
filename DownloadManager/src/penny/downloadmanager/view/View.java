/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.view;

import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import penny.downloadmanager.model.LookAndFeelModel;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.view.add.AddDialog;
import penny.downloadmanager.view.add.AddTask;
import penny.downloadmanager.view.settings.DownloadSettingsDialog;
import penny.downloadmanager.view.settings.EditFileFormat;
import penny.downloadmanager.view.settings.StartupSettings;

/**
 *
 * @author john
 */
public class View {
    private static AddDialog addDialogView;
    private static AddTask addTask;
    private static DownloadSettingsDialog settingsDialogView;
    private static MainWindow mainWindowView;
    private static EditFileFormat editFileFormat;
    private static StartupSettings startupSettings;
    private static SplashScreen splashScreen;

    /**
     * @return the addDialogView
     */
    public static AddDialog getAddDialogView() {
        return addDialogView;
    }

    /**
     * @return the settingsDialogView
     */
    public static DownloadSettingsDialog getSettingsDialogView() {
        return settingsDialogView;
    }

    /**
     * @return the mainWindowView
     */
    public static MainWindow getMainWindowView() {
        return mainWindowView;
    }

    /**
     * @return the editFileFormat
     */
    public static EditFileFormat getEditFileFormat() {
        return editFileFormat;
    }

    public static AddTask getAddTask() {
        return addTask;
    }

    public static StartupSettings getStartupSettings() {
        return startupSettings;
    }
    
    public static SplashScreen getSplashScreen() {
        return splashScreen;
    }
    
    public static void initLookAndFeel() {
        LookAndFeelModel lookModel = Model.getApplicationSettings().getLookModel();
        try {
            UIManager.setLookAndFeel(lookModel.getLookAndFeels().get(lookModel.getLookAndFeel()));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Window window : JFrame.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
        }
    }

    public static void build() {
        LookAndFeelModel lookModel = Model.getApplicationSettings().getLookModel();
        if(lookModel.getLookAndFeel() == null || lookModel.getLookAndFeel().equals("")) {
            for(String name : lookModel.getLookAndFeels().keySet()) {
                if(lookModel.getLookAndFeels().get(name).equals(UIManager.getSystemLookAndFeelClassName())) {
                    lookModel.setLookAndFeel(name);
                    break;
                } else {
                    lookModel.setLookAndFeel(name);
                }
            }
        }
        initLookAndFeel();
        settingsDialogView = new DownloadSettingsDialog(Model.getSettingsDialogModel());
        Model.getSettingsDialogModel().addPropertyChangeListener(settingsDialogView);
        addDialogView = new AddDialog(Model.getAddDialogModel());
        mainWindowView = new MainWindow(Model.getMainWindowModel(), Model.getTaskManagerModel());
        addTask = new AddTask();
        startupSettings = new StartupSettings(Model.getStartupDialogModel());
        splashScreen = new SplashScreen();
        Model.getSplashScreenModel().addPropertyChangeListener(splashScreen);
    }
}
