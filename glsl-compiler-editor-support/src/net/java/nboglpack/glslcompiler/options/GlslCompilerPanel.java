package net.java.nboglpack.glslcompiler.options;

import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import org.openide.util.NbPreferences;

/**
 * Created on 15. March 2007, 16:10
 * @author Michael Bien
 */
final class GlslCompilerPanel extends javax.swing.JPanel {
    
    private final GlslCompilerOptionsPanelController controller;
    
    GlslCompilerPanel(GlslCompilerOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel optionsPanel = new javax.swing.JPanel();
        javax.swing.JLabel patternLabel = new javax.swing.JLabel();
        patternTextField = new javax.swing.JTextField();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        glVersionLabel = new javax.swing.JLabel();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        vendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        joglVersionLabel = new javax.swing.JLabel();

        setBackground(java.awt.Color.white);

        optionsPanel.setBackground(getBackground());
        optionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("compiler error log parser configuration"));

        org.openide.awt.Mnemonics.setLocalizedText(patternLabel, "error log pattern:");

        org.jdesktop.layout.GroupLayout optionsPanelLayout = new org.jdesktop.layout.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
            optionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(patternLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(patternTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
        );
        optionsPanelLayout.setVerticalGroup(
            optionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(optionsPanelLayout.createSequentialGroup()
                .add(optionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(patternLabel)
                    .add(patternTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel1.setBackground(getBackground());
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("OpenGL Runtime Information"));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "OpenGL Version:");

        org.openide.awt.Mnemonics.setLocalizedText(glVersionLabel, "jLabel2");

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, "GPU Vendor:");

        org.openide.awt.Mnemonics.setLocalizedText(vendorLabel, "jLabel4");

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, "JOGL Version:");

        org.openide.awt.Mnemonics.setLocalizedText(joglVersionLabel, "jLabel6");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jLabel3)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(joglVersionLabel)
                    .add(vendorLabel)
                    .add(glVersionLabel))
                .addContainerGap(179, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(glVersionLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(vendorLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(joglVersionLabel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, optionsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(optionsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    void load() {
        Preferences preferences = NbPreferences.forModule(GlslCompilerPanel.class);
        patternTextField.setText(preferences.get("GlslCompilerLogPattern", ""));
        glVersionLabel.setText(preferences.get("GLVersion", ""));
        vendorLabel.setText(preferences.get("GLVendor", ""));
        joglVersionLabel.setText(preferences.get("JOGLVersion", ""));
    }
    
    void store() {
        NbPreferences.forModule(GlslCompilerPanel.class).put("GlslCompilerLogPattern", patternTextField.getText());
    }
    
    boolean valid() {
        
        String pattern = patternTextField.getText();
        if(pattern == null || pattern.trim().equals(""))
            return false;
        
        try{
            Pattern.compile(pattern);
            return true;
        }catch(Exception ex){
            return false; 
        }
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel glVersionLabel;
    private javax.swing.JLabel joglVersionLabel;
    private javax.swing.JTextField patternTextField;
    private javax.swing.JLabel vendorLabel;
    // End of variables declaration//GEN-END:variables
    
}
