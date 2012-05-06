/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StartupSettings.java
 *
 * Created on Nov 21, 2011, 10:55:21 AM
 */
package penny.downloadmanager.view.settings;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import penny.downloadmanager.control.StartupSettingsControl;
import penny.downloadmanager.model.StartupModel;
import penny.downloadmanager.model.gui.StartupDialogModel;

/**
 *
 * @author john
 */
public class StartupSettings extends javax.swing.JDialog implements PropertyChangeListener {

    /** Creates new form StartupSettings */
    public StartupSettings(StartupDialogModel startupDialogModel) {
        this.startupDialogModel = startupDialogModel;
        System.out.println("view check md5 " + startupDialogModel.getStartupModelCopy().isCheckMD5s());
        System.out.println("view check size " + startupDialogModel.getStartupModelCopy().isCheckSizes());
        startupDialogModel.addPropertyChangeListener(this);
        startupDialogModel.getStartupModelCopy().addPropertyChangeListener(this);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        startupDialogModel = startupDialogModel;
        startupModel = startupDialogModel.getStartupModelCopy();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jCheckBox1.setText("Check file sizes on startup");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, startupModel, org.jdesktop.beansbinding.ELProperty.create("${checkSizes}"), jCheckBox1, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        jCheckBox2.setText("Check file md5 on startup");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, startupModel, org.jdesktop.beansbinding.ELProperty.create("${checkMD5s}"), jCheckBox2, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        jButton1.setText("Cancel");

        jButton2.setText("OK");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(139, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private penny.downloadmanager.model.gui.StartupDialogModel startupDialogModel;
    private penny.downloadmanager.model.StartupModel startupModel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    public void registerController(StartupSettingsControl control) {
        jButton2.addActionListener(control);
        jButton2.setActionCommand(StartupSettingsControl.COM_OK);
        jButton1.addActionListener(control);
        jButton1.setActionCommand(StartupSettingsControl.COM_CANCEL);
        this.addWindowListener(control);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(StartupDialogModel.PROP_VISIBLE)) {
            this.setVisible((Boolean) evt.getNewValue());
            System.out.println("view check md5 " + startupDialogModel.getStartupModelCopy().isCheckMD5s() + " " + startupDialogModel.getStartupModel().isCheckMD5s());
            System.out.println("view check size " + startupDialogModel.getStartupModelCopy().isCheckSizes() + " " + startupDialogModel.getStartupModel().isCheckSizes());
        } else if(evt.getPropertyName().equals(StartupModel.PROP_CHECKMD5S)) {
            System.out.println("view changed check md5 " + startupDialogModel.getStartupModelCopy().isCheckMD5s() + " " + startupDialogModel.getStartupModel().isCheckMD5s());
        } else if(evt.getPropertyName().equals(StartupModel.PROP_CHECKSIZES)) {
            System.out.println("view changed check size " + startupDialogModel.getStartupModelCopy().isCheckSizes() + " " + startupDialogModel.getStartupModel().isCheckSizes());
        }
    }
}
