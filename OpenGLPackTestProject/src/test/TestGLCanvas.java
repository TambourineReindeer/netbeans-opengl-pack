/*
 * TestGLCanvas.java
 *
 * Created on 26. Juli 2007, 14:38
 */

package test;

/**
 *
 * @author  mbien
 */
public class TestGLCanvas extends javax.swing.JFrame {
    
    /** Creates new form TestGLCanvas */
    public TestGLCanvas() {
        initComponents();
        
        JOGLGearsDemo demo = new JOGLGearsDemo(gLCanvas1);
        demo.start();
        setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        gLCanvas1 = new javax.media.opengl.GLCanvas();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gLCanvas1, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gLCanvas1, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestGLCanvas();
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.media.opengl.GLCanvas gLCanvas1;
    // End of variables declaration//GEN-END:variables
    
}