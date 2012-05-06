/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.StartupModel;
import penny.downloadmanager.model.gui.StartupDialogModel;

/**
 *
 * @author john
 */
public class StartupSettingsControl implements ActionListener, WindowListener {

    public static final String COM_OK = "ok";
    public static final String COM_CANCEL = "cancel";
    
    private StartupDialogModel startupDialogModel;

    public StartupSettingsControl(StartupDialogModel startupDialogModel) {
        this.startupDialogModel = startupDialogModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(COM_OK)) {
            startupDialogModel.getStartupModel().copy(startupDialogModel.getStartupModelCopy());
            Model.getSettingsSaver().save();
            startupDialogModel.setVisible(false);
        } else if(e.getActionCommand().equals(COM_CANCEL)) {
            startupDialogModel.setVisible(false);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        startupDialogModel.setVisible(false);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
