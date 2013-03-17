/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import penny.downloadmanager.model.Model;

/**
 *
 * @author john
 */
public class ColumnPopupMenu extends JPopupMenu implements ActionListener {
    private DownloadTableFormat downloadFormat;
    private Map<String, JCheckBoxMenuItem> menuButtons;
    public ColumnPopupMenu(DownloadTableFormat downloadFormat) {
        menuButtons = new HashMap<String, JCheckBoxMenuItem>();
        this.downloadFormat = downloadFormat;
        for(ColumnStatus c : downloadFormat.getColumns()) {
            JCheckBoxMenuItem menu = new JCheckBoxMenuItem(c.getName());
            menu.setActionCommand(c.getName());
            menu.addActionListener(this);
            menuButtons.put(c.getName(), menu);
            add(menu);
        }
    }
    
    public void initMenu() {
        for(ColumnStatus c : downloadFormat.getColumns()) {
            JCheckBoxMenuItem menu = menuButtons.get(c.getName());
            menu.setSelected(c.isVisible());
        }
    }
    
    public void show(Component invoker, int x, int y) {
        initMenu();
        super.show(invoker, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();
        downloadFormat.setVisible(name, !downloadFormat.isVisible(name));
    }
}
