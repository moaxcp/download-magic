/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.view.renderer;

import penny.downloadmanager.model.task.DTaskData;
import penny.downloadmanager.model.task.LinkToDownloadTaskData;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author john
 */
public class TaskDataRenderer implements ListCellRenderer {

    private DTaskDataTemplate downloadTemplate;
    private LinkToDownloadTaskDataTemplate linkToDownloadTemplate;
    private JLabel label;

    private JList list;

    public TaskDataRenderer(JList list) {
        downloadTemplate = new DTaskDataTemplate();
        linkToDownloadTemplate = new LinkToDownloadTaskDataTemplate();
        label = new JLabel();
        this.list = list;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Color background;
        Color foreground;
        if(isSelected) {
            background = list.getSelectionBackground();
            foreground = list.getSelectionForeground();
        } else {
            background = list.getBackground();
            foreground = list.getForeground();
        }
        if(value instanceof DTaskData) {
            downloadTemplate.setTaskData((DTaskData) value, background, foreground);
            return downloadTemplate;
        } else if(value instanceof LinkToDownloadTaskData) {
            linkToDownloadTemplate.setTaskData((LinkToDownloadTaskData) value, background, foreground);
            return linkToDownloadTemplate;
        } else {
            return label;
        }
    }

}
