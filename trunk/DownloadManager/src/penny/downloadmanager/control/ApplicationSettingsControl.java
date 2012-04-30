/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.control;

import penny.downloadmanager.model.Model;
import penny.downloadmanager.model.gui.SettingsDialogModel;
import penny.downloadmanager.view.settings.EditFileFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author john
 */
public class ApplicationSettingsControl implements ActionListener, WindowListener {

    public static final String COM_TEMPBROWSE = "tempBrowse";
    public static final String COM_SAVEBROWSE = "saveBrowse";
    public static final String COM_TEMPFORMATEDIT = "tempFormatEdit";
    public static final String COM_SAVEFORMATEDIT = "saveFormatEdit";
    public static final String COM_OK = "ok";
    public static final String COM_CANCEL = "cancel";
    public static final String COM_OKFORMAT = "okFormat";
    public static final String COM_ADDSAVETYPE= "addSaveType";
    public static final String COM_ADDDOWNLOADTYPE = "addDownloadType";
    public static final String COM_ADDLINKTYPE = "addLinkType";
    public static final String COM_ADDWORDTYPE = "addWordType";
    public static final String COM_ADDMD5TYPE = "addMd5Type";
    public static final String COM_REMOVESAVETYPE= "removeSaveType";
    public static final String COM_REMOVEDOWNLOADTYPE = "removeDownloadType";
    public static final String COM_REMOVELINKTYPE = "removeLinkType";
    public static final String COM_REMOVEWORDTYPE = "removeWordType";
    public static final String COM_REMOVEMD5TYPE = "removeMd5Type";

    private SettingsDialogModel model;

    ApplicationSettingsControl(SettingsDialogModel model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(COM_TEMPBROWSE)) {
            JFileChooser chooser = new JFileChooser(".");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = chooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                model.getAppSettingsCopy().getSavingModel().setTempFolder(chooser.getSelectedFile().toString());
            }
        } else if (e.getActionCommand().equals(COM_SAVEBROWSE)) {
            JFileChooser chooser = new JFileChooser(".");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = chooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                model.getAppSettingsCopy().getSavingModel().setSaveFolder(chooser.getSelectedFile().toString());
            }
        } else if(e.getActionCommand().equals(COM_TEMPFORMATEDIT)) {
            model.setNameType(SettingsDialogModel.TYPE_TEMP);
            EditFileFormat edit = new EditFileFormat(model);
            edit.registerController(this);
            edit.setVisible(true);
        } else if(e.getActionCommand().equals(COM_SAVEFORMATEDIT)) {
            model.setNameType(SettingsDialogModel.TYPE_SAVE);
            EditFileFormat edit = new EditFileFormat(model);
            edit.registerController(this);
            edit.setVisible(true);
        } else if(e.getActionCommand().equals(COM_OK)) {
            model.getAppSettings().copy(model.getAppSettingsCopy());
            Model.getSettingsSaver().save();
            model.setVisible(false);
        } else if(e.getActionCommand().equals(COM_CANCEL)) {
            model.setVisible(false);
        } else if(e.getActionCommand().equals(COM_OKFORMAT)) {
            if(model.getNameType().equals(SettingsDialogModel.TYPE_SAVE)) {
                model.getAppSettingsCopy().getSavingModel().setSaveNameFormat(model.getNameFormat());
            } else if(model.getNameType().equals(SettingsDialogModel.TYPE_TEMP)) {
                model.getAppSettingsCopy().getSavingModel().setTempNameFormat(model.getNameFormat());
            }
        } else if(e.getActionCommand().equals(COM_ADDSAVETYPE)) {
            String type = JOptionPane.showInputDialog("Add Type");
            model.getAppSettingsCopy().getSavingModel().getSaveTypes().add(type);
        } else if(e.getActionCommand().equals(COM_ADDDOWNLOADTYPE)) {
            String type = JOptionPane.showInputDialog("Add Type");
            model.getAppSettingsCopy().getDownloadingModel().getDownloadTypes().add(type);
        } else if(e.getActionCommand().equals(COM_ADDLINKTYPE)) {
            String type = JOptionPane.showInputDialog("Add Type");
            model.getAppSettingsCopy().getParsingModel().getParseLinksTypes().add(type);
        } else if(e.getActionCommand().equals(COM_ADDWORDTYPE)) {
            String type = JOptionPane.showInputDialog("Add Type");
            model.getAppSettingsCopy().getParsingModel().getParseWordsTypes().add(type);
        } else if(e.getActionCommand().equals(COM_ADDMD5TYPE)) {
            String type = JOptionPane.showInputDialog("Add Type");
            model.getAppSettingsCopy().getMd5ingModel().getMd5Types().add(type);
        } else if(e.getActionCommand().equals(COM_REMOVESAVETYPE)) {
            model.getAppSettingsCopy().getSavingModel().getSaveTypes().remove(model.getSaveSelected());
        } else if(e.getActionCommand().equals(COM_REMOVEDOWNLOADTYPE)) {
            model.getAppSettingsCopy().getDownloadingModel().getDownloadTypes().remove(model.getDownloadSelected());
        } else if(e.getActionCommand().equals(COM_REMOVELINKTYPE)) {
            model.getAppSettingsCopy().getParsingModel().getParseLinksTypes().remove(model.getLinkSelected());
        } else if(e.getActionCommand().equals(COM_REMOVEWORDTYPE)) {
            model.getAppSettingsCopy().getParsingModel().getParseWordsTypes().remove(model.getWordSelected());
        } else if(e.getActionCommand().equals(COM_REMOVEMD5TYPE)) {
            model.getAppSettingsCopy().getMd5ingModel().getMd5Types().remove(model.getMd5Selected());
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        model.setVisible(false);
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
