/*
 * GLCapabilityPanel.java
 *
 * Created on 19. Juni 2007, 18:51
 */
package net.java.nboglpack.glcapabilities;

import net.java.nboglpack.glcapabilities.demo.JOGLGearsDemo;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.Beans;
import java.util.Comparator;
import javax.beans.binding.Binding;
import javax.media.opengl.DefaultGLCapabilitiesChooser;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesChooser;
import javax.swing.binding.ParameterKeys;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Michael Bien
 */
public class GLCapabilitiesPanel extends javax.swing.JPanel {
    
 private JOGLGearsDemo demo;
   
    /** Creates new form GLCapabilityPanel */
    public GLCapabilitiesPanel() {
        
        // TODO workaround; NB forgets to set design time flag => we do it
        Beans.setDesignTime(false);
        
        initComponents();
        
        // bind capabilities table to model
        Binding binding = new Binding(capabilitiesModel, "${capabilities}", capabilitiesJPanel.getTable(), "elements");
        binding.putParameter(ParameterKeys.EDITABLE, false);
        
        binding.addChildBinding("${name}", null)     
               .putParameter(ParameterKeys.COLUMN, 0)
               .putParameter(ParameterKeys.COLUMN_CLASS, String.class);
        binding.addChildBinding("${value}", null)     
               .putParameter(ParameterKeys.COLUMN, 1)
               .putParameter(ParameterKeys.COLUMN_CLASS, String.class);

        bindingContext.addBinding(binding);
        binding.bind();
        
        
        // bind extensions table to model
        binding = new Binding(capabilitiesModel, "${extensions}", extentionsJPanel.getTable(), "elements");
        binding.putParameter(ParameterKeys.EDITABLE, false);
        
        bindingContext.addBinding(binding);
        binding.bind();
        
        
        // bind display modes table
        binding = new Binding(capabilitiesModel, "${displayModes}", displayModesJPanel.getTable(), "elements");
        binding.putParameter(ParameterKeys.EDITABLE, false);
        
        binding.addChildBinding("${hardwareAccelerated}", null)
               .putParameter(ParameterKeys.COLUMN, 0)
               .putParameter(ParameterKeys.COLUMN_CLASS, Boolean.class);
        
        binding.addChildBinding("${doubleBuffered}", null)
               .putParameter(ParameterKeys.COLUMN, 1)
               .putParameter(ParameterKeys.COLUMN_CLASS, Boolean.class);
        
        binding.addChildBinding("${stereo}", null)
               .putParameter(ParameterKeys.COLUMN, 2)
               .putParameter(ParameterKeys.COLUMN_CLASS, Boolean.class);
        
        binding.addChildBinding("${sampleBuffers}", null)
               .putParameter(ParameterKeys.COLUMN, 3)
               .putParameter(ParameterKeys.COLUMN_CLASS, Boolean.class);
        
        binding.addChildBinding("${numSamples}", null)
               .putParameter(ParameterKeys.COLUMN, 4)
               .putParameter(ParameterKeys.COLUMN_CLASS, Integer.class);
        
        binding.addChildBinding("${depthBits}", null)
               .putParameter(ParameterKeys.COLUMN, 5)
               .putParameter(ParameterKeys.COLUMN_CLASS, Integer.class);
        
        binding.addChildBinding("${redBits}", null)
               .putParameter(ParameterKeys.COLUMN, 6)
               .putParameter(ParameterKeys.COLUMN_CLASS, Integer.class);
        
        binding.addChildBinding("${greenBits}", null)
               .putParameter(ParameterKeys.COLUMN, 7)
               .putParameter(ParameterKeys.COLUMN_CLASS, Integer.class);
        
        binding.addChildBinding("${blueBits}", null)
               .putParameter(ParameterKeys.COLUMN, 8)
               .putParameter(ParameterKeys.COLUMN_CLASS, Integer.class);
        
        binding.addChildBinding("${alphaBits}", null)
               .putParameter(ParameterKeys.COLUMN, 8)
               .putParameter(ParameterKeys.COLUMN_CLASS, Integer.class);
        
        binding.addChildBinding("${accumRedBits}", null)
               .putParameter(ParameterKeys.COLUMN, 9)
               .putParameter(ParameterKeys.COLUMN_CLASS, Integer.class);
        
        binding.addChildBinding("${accumGreenBits}", null)
               .putParameter(ParameterKeys.COLUMN, 10)
               .putParameter(ParameterKeys.COLUMN_CLASS, Integer.class);
        
        binding.addChildBinding("${accumBlueBits}", null)
               .putParameter(ParameterKeys.COLUMN, 11)
               .putParameter(ParameterKeys.COLUMN_CLASS, Integer.class);
        
        binding.addChildBinding("${accumAlphaBits}", null)
               .putParameter(ParameterKeys.COLUMN, 12)
               .putParameter(ParameterKeys.COLUMN_CLASS, Integer.class);
        
        bindingContext.addBinding(binding);
        binding.bind();
        
        
        demo = new JOGLGearsDemo(gLCanvas);
        
        gLCanvas.addHierarchyListener(new HierarchyListener() {

            public void hierarchyChanged(HierarchyEvent e) {
                if(e.getChangeFlags() == HierarchyEvent.SHOWING_CHANGED) {
                    if(demo.isRunning())
                        demo.stop();
                    else
                        demo.start();
                }
            }
            
        });
         
    }
    
    public void updateFromModel() {
        
        bindingContext.unbind();
        bindingContext.bind();
        
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>)extentionsJPanel.getTable().getRowSorter();
        sorter.setModel(extentionsJPanel.getTable().getModel());
        
        sorter = (TableRowSorter)displayModesJPanel.getTable().getRowSorter();
        sorter.setModel(displayModesJPanel.getTable().getModel());
        
        sorter = (TableRowSorter)capabilitiesJPanel.getTable().getRowSorter();
        sorter.setModel(capabilitiesJPanel.getTable().getModel());

        
        sorter.setComparator(1, new Comparator<String>() {
            
            public int compare(String str1, String str2) {
                
                int i1;
                int i2;
                
                if(str1.contains(","))
                    i1 = Integer.parseInt(str1.substring(0, str1.indexOf(",")));
                else
                    i1 = Integer.parseInt(str1);
                
                if(str2.contains(","))
                    i2 = Integer.parseInt(str2.substring(0, str2.indexOf(",")));
                else
                    i2 = Integer.parseInt(str2);
  
                return i1-i2;
            }
            
        });
        capabilitiesJPanel.getTable().getColumnModel().getColumn(1).setMaxWidth(80);
        
    }
    
    
    private GLCanvas createGLDemoCanvas() {
        
        GLCapabilities caps = new GLCapabilities();
        caps.setNumSamples(4);
        caps.setSampleBuffers(true);
        
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GLCapabilitiesChooser chooser = new DefaultGLCapabilitiesChooser(){

            @Override
            public int chooseCapabilities(GLCapabilities desired, GLCapabilities[] available, int arg2) {
                int max = 0;
                for (GLCapabilities elem : available) {
                    if(elem == null)
                        continue;
                    capabilitiesModel.getDisplayModes().add(elem);
                    if(elem.getHardwareAccelerated() && max < elem.getNumSamples())
                        max = elem.getNumSamples();
                }
                capabilitiesModel.setMaxSampleBuffers(max+"x");

                return super.chooseCapabilities(desired, available, arg2);
            }
        };
        
        
        return new GLCanvas(caps, chooser, null, device);
    }
            
    
    public GLCapabilitiesModel getModel() {
        return capabilitiesModel;
    }
    void setModel(GLCapabilitiesModel model) {
        this.capabilitiesModel = model;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingContext = new javax.beans.binding.BindingContext();

        capabilitiesModel = new net.java.nboglpack.glcapabilities.GLCapabilitiesModel();
        javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
        javax.swing.JPanel overviewPanel = new javax.swing.JPanel();
        javax.swing.JPanel basicCapsPanel = new javax.swing.JPanel();
        glField = new javax.swing.JTextField();
        glslField = new javax.swing.JTextField();
        joglField = new javax.swing.JTextField();
        rendererField = new javax.swing.JTextField();
        vendorField = new javax.swing.JTextField();
        javax.swing.JLabel gl = new javax.swing.JLabel();
        javax.swing.JLabel glsl = new javax.swing.JLabel();
        javax.swing.JLabel jogl = new javax.swing.JLabel();
        javax.swing.JLabel renderer = new javax.swing.JLabel();
        javax.swing.JLabel vendor = new javax.swing.JLabel();
        gLCanvas = createGLDemoCanvas();
        javax.swing.JPanel overviewCapsPanel = new javax.swing.JPanel();
        viewportField = new javax.swing.JTextField();
        textureSizeField = new javax.swing.JTextField();
        textureUnitsField = new javax.swing.JTextField();
        textureUnitsVSField = new javax.swing.JTextField();
        lightsField = new javax.swing.JTextField();
        anisotropicFilteringField = new javax.swing.JTextField();
        fsaaField = new javax.swing.JTextField();
        textureUnitsFSField = new javax.swing.JTextField();
        textureUnitsGSField = new javax.swing.JTextField();
        renderBuffersField = new javax.swing.JTextField();
        javax.swing.JLabel viewportSizeLabel = new javax.swing.JLabel();
        javax.swing.JLabel textureSizeLabel = new javax.swing.JLabel();
        javax.swing.JLabel textureUnitsLabel = new javax.swing.JLabel();
        javax.swing.JLabel textureUnitsVSLabel = new javax.swing.JLabel();
        javax.swing.JLabel dynamicLightsLabel = new javax.swing.JLabel();
        javax.swing.JLabel anisotropicFilteringLabel = new javax.swing.JLabel();
        javax.swing.JLabel fsaaSamplesLabel = new javax.swing.JLabel();
        javax.swing.JLabel textureUnitsFSLabel = new javax.swing.JLabel();
        javax.swing.JLabel textureUnitsGSLabel = new javax.swing.JLabel();
        javax.swing.JLabel renderBuffersLabel = new javax.swing.JLabel();
        capabilitiesJPanel = new net.java.nboglpack.glcapabilities.FilteredTable();
        extentionsJPanel = new net.java.nboglpack.glcapabilities.FilteredTable();
        displayModesJPanel = new net.java.nboglpack.glcapabilities.DisplayModesPanel();

        tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        basicCapsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.basicCapsPanel.border.title"))); // NOI18N

        glField.setEditable(false);
        glField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${glVersion}", glField, "text");

        glslField.setEditable(false);
        glslField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${glslVersion}", glslField, "text");

        joglField.setEditable(false);
        joglField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${implVersion}", joglField, "text");

        rendererField.setEditable(false);
        rendererField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${renderer}", rendererField, "text");

        vendorField.setEditable(false);
        vendorField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${vendor}", vendorField, "text");

        gl.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.gl.text")); // NOI18N

        glsl.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.glsl.text")); // NOI18N

        jogl.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.jogl.text")); // NOI18N

        renderer.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.renderer.text")); // NOI18N

        vendor.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.vendor.text")); // NOI18N

        javax.swing.GroupLayout basicCapsPanelLayout = new javax.swing.GroupLayout(basicCapsPanel);
        basicCapsPanel.setLayout(basicCapsPanelLayout);
        basicCapsPanelLayout.setHorizontalGroup(
            basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicCapsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jogl)
                    .addComponent(glsl)
                    .addComponent(gl)
                    .addComponent(vendor)
                    .addComponent(renderer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(basicCapsPanelLayout.createSequentialGroup()
                        .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(glField, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                            .addComponent(glslField, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                            .addComponent(joglField, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gLCanvas, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(rendererField, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                    .addComponent(vendorField, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
                .addContainerGap())
        );
        basicCapsPanelLayout.setVerticalGroup(
            basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicCapsPanelLayout.createSequentialGroup()
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gLCanvas, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, basicCapsPanelLayout.createSequentialGroup()
                        .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(gl)
                            .addComponent(glField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(glsl)
                            .addComponent(glslField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jogl)
                            .addComponent(joglField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(renderer)
                    .addComponent(rendererField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vendor)
                    .addComponent(vendorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        overviewCapsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.overviewCapsPanel.border.title"))); // NOI18N

        viewportField.setEditable(false);
        viewportField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${maxViewPortSize}", viewportField, "text");

        textureSizeField.setEditable(false);
        textureSizeField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${maxTextureSize}", textureSizeField, "text");

        textureUnitsField.setEditable(false);
        textureUnitsField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${maxTextureUnits}", textureUnitsField, "text");

        textureUnitsVSField.setEditable(false);
        textureUnitsVSField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${maxVertexTextureImageUnits}", textureUnitsVSField, "text");

        lightsField.setEditable(false);
        lightsField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${maxLights}", lightsField, "text");

        anisotropicFilteringField.setEditable(false);
        anisotropicFilteringField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${maxAnisotropy}", anisotropicFilteringField, "text");

        fsaaField.setEditable(false);
        fsaaField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${maxSampleBuffers}", fsaaField, "text");

        textureUnitsFSField.setEditable(false);
        textureUnitsFSField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${maxTextureImageUnits}", textureUnitsFSField, "text");

        textureUnitsGSField.setEditable(false);
        textureUnitsGSField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${maxGeometryTextureImageUnits}", textureUnitsGSField, "text");

        renderBuffersField.setEditable(false);
        renderBuffersField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bindingContext.addBinding(capabilitiesModel, "${maxDrawBuffers}", renderBuffersField, "text");

        viewportSizeLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.viewportSizeLabel.text")); // NOI18N

        textureSizeLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureSizeLabel.text")); // NOI18N

        textureUnitsLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsLabel.text")); // NOI18N

        textureUnitsVSLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsVSLabel.text")); // NOI18N

        dynamicLightsLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.dynamicLightsLabel.text")); // NOI18N

        anisotropicFilteringLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.anisotropicFilteringLabel.text")); // NOI18N

        fsaaSamplesLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.fsaaSamplesLabel.text")); // NOI18N

        textureUnitsFSLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsFSLabel.text")); // NOI18N

        textureUnitsGSLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.textureUnitsGSLabel.text")); // NOI18N

        renderBuffersLabel.setText(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.renderBuffersLabel.text")); // NOI18N

        javax.swing.GroupLayout overviewCapsPanelLayout = new javax.swing.GroupLayout(overviewCapsPanel);
        overviewCapsPanel.setLayout(overviewCapsPanelLayout);
        overviewCapsPanelLayout.setHorizontalGroup(
            overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overviewCapsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textureSizeLabel)
                    .addComponent(viewportSizeLabel)
                    .addComponent(textureUnitsLabel)
                    .addComponent(textureUnitsVSLabel)
                    .addComponent(dynamicLightsLabel))
                .addGap(5, 5, 5)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lightsField, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(viewportField, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(textureSizeField, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(textureUnitsField, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(textureUnitsVSField, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(anisotropicFilteringLabel)
                    .addComponent(fsaaSamplesLabel)
                    .addComponent(textureUnitsFSLabel)
                    .addComponent(textureUnitsGSLabel)
                    .addComponent(renderBuffersLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(anisotropicFilteringField, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(fsaaField, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(textureUnitsFSField, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(textureUnitsGSField, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(renderBuffersField, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addContainerGap())
        );
        overviewCapsPanelLayout.setVerticalGroup(
            overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overviewCapsPanelLayout.createSequentialGroup()
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(viewportSizeLabel)
                        .addComponent(viewportField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(anisotropicFilteringLabel))
                    .addComponent(anisotropicFilteringField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textureSizeLabel)
                        .addComponent(textureSizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fsaaSamplesLabel))
                    .addComponent(fsaaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textureUnitsLabel)
                        .addComponent(textureUnitsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(textureUnitsFSLabel))
                    .addComponent(textureUnitsFSField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textureUnitsVSLabel)
                    .addComponent(textureUnitsGSField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textureUnitsVSField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textureUnitsGSLabel))
                .addGap(18, 18, 18)
                .addGroup(overviewCapsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dynamicLightsLabel)
                    .addComponent(renderBuffersField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lightsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(renderBuffersLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout overviewPanelLayout = new javax.swing.GroupLayout(overviewPanel);
        overviewPanel.setLayout(overviewPanelLayout);
        overviewPanelLayout.setHorizontalGroup(
            overviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, overviewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(overviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(overviewCapsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(basicCapsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        overviewPanelLayout.setVerticalGroup(
            overviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overviewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(basicCapsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(overviewCapsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.overviewPanel.TabConstraints.tabTitle"), overviewPanel); // NOI18N
        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.capabilitiesJPanel.TabConstraints.tabTitle"), capabilitiesJPanel); // NOI18N
        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.extentionsJPanel.TabConstraints.tabTitle"), extentionsJPanel); // NOI18N
        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(GLCapabilitiesPanel.class, "GLCapabilitiesPanel.displayModesJPanel.TabConstraints.tabTitle"), displayModesJPanel); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingContext.bind();
    }// </editor-fold>//GEN-END:initComponents
  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField anisotropicFilteringField;
    private net.java.nboglpack.glcapabilities.FilteredTable capabilitiesJPanel;
    private net.java.nboglpack.glcapabilities.GLCapabilitiesModel capabilitiesModel;
    private net.java.nboglpack.glcapabilities.DisplayModesPanel displayModesJPanel;
    private net.java.nboglpack.glcapabilities.FilteredTable extentionsJPanel;
    private javax.swing.JTextField fsaaField;
    private javax.media.opengl.GLCanvas gLCanvas;
    private javax.swing.JTextField glField;
    private javax.swing.JTextField glslField;
    private javax.swing.JTextField joglField;
    private javax.swing.JTextField lightsField;
    private javax.swing.JTextField renderBuffersField;
    private javax.swing.JTextField rendererField;
    private javax.swing.JTextField textureSizeField;
    private javax.swing.JTextField textureUnitsFSField;
    private javax.swing.JTextField textureUnitsField;
    private javax.swing.JTextField textureUnitsGSField;
    private javax.swing.JTextField textureUnitsVSField;
    private javax.swing.JTextField vendorField;
    private javax.swing.JTextField viewportField;
    private javax.beans.binding.BindingContext bindingContext;
    // End of variables declaration//GEN-END:variables
    
}
