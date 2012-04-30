/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control;

import penny.downloadmanager.model.gui.AddDialogModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author john
 */
public class AddDialogControl implements ActionListener, WindowListener {

    public static final String COM_OK = "OK";
    public static final String COM_CANCEL = "Cancel";
    public static final String COM_BROWSE = "Browse";
    public static final String COM_PARSE = "Parse";
    public static final String COM_WHITESPACE = "Whitespace/comma";
    public static final String COM_SRC = "SRC";
    private AddDialogModel addModel;
    private MainWindowControl mainControl;

    public AddDialogControl(AddDialogModel addModel, MainWindowControl mainControl) {
        this.mainControl = mainControl;
        this.addModel = addModel;
    }

    private void cleanModel() {
        addModel.setAddURL("");
        addModel.setListFileName("");
        addModel.clearUrls();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(COM_OK)) {
            if (addModel.getSelectedPanel().equals(AddDialogModel.PanelList.URL)) {
                try {
                    mainControl.add(new URL(addModel.getAddURL()));
                    addModel.setVisible(false);
                } catch (MalformedURLException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(), "Malformed URL", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(AddDialogControl.class.getName()).logp(Level.INFO, "AddDialogControl", "actionPerformed", ex.toString());
                }
            } else if (addModel.getSelectedPanel().equals(AddDialogModel.PanelList.LIST)) {
                mainControl.add(addModel.getUrls());
                addModel.setVisible(false);
            } else if (addModel.getSelectedPanel().equals(AddDialogModel.PanelList.QUERY)) {
                addModel.setVisible(false);
            } else {
                throw new IllegalStateException();
            }
        } else if (e.getActionCommand().equals(COM_CANCEL)) {
            addModel.setVisible(false);
        } else if (e.getActionCommand().equals(COM_BROWSE)) {
            JFileChooser chooser = new JFileChooser(".");
            int option = chooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                addModel.setListFileName(chooser.getSelectedFile().toString());
            }
        } else if(e.getActionCommand().equals(COM_PARSE)) {
            if(addModel.getListType().equals(COM_WHITESPACE)) {
                try {
                    List<URL> list = UrlGrabbers.parseWhitespaceCommaFile(addModel.getListFileName());
                    addModel.getUrls().clear();
                    for(URL url : list) {
                        addModel.addUrl(url);
                    }
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(), "File Not Found", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(AddDialogControl.class.getName()).logp(Level.SEVERE, "AddDialogControl", "actionPerformed", ex.toString());
                } catch (MalformedURLException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(), "Mailformed URL", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(AddDialogControl.class.getName()).logp(Level.SEVERE, "AddDialogControl", "actionPerformed", ex.toString());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(), "IO Exception", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(AddDialogControl.class.getName()).logp(Level.SEVERE, "AddDialogControl", "actionPerformed", ex.toString());
                }
            } else if(addModel.getListType().equals(COM_SRC)) {
                
            }
        } else if(e.getActionCommand().equals(COM_WHITESPACE)) {
            addModel.setListType(COM_WHITESPACE);
        } else if(e.getActionCommand().equals(COM_SRC)) {
            addModel.setListType(COM_SRC);
        }
    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {
        addModel.setVisible(false);
    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }
}
