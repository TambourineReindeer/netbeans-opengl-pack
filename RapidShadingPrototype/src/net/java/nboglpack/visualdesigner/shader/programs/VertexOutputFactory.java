/*
 * VertexOutputFactory.java
 *
 * Created on April 30, 2007, 11:56 AM
 */

package net.java.nboglpack.visualdesigner.shader.programs;

import java.awt.Color;
import java.util.HashMap;
import javax.swing.JPanel;
import net.java.nboglpack.visualdesigner.GlobalVariable;
import net.java.nboglpack.visualdesigner.shader.exporter.ExportingExeption;
import net.java.nboglpack.visualdesigner.shader.exporter.IExportable;
import net.java.nboglpack.visualdesigner.shader.exporter.IShaderCodeExportVisitor;
import net.java.nboglpack.visualdesigner.shader.exporter.ValueAssignment;
import net.java.nboglpack.visualdesigner.shader.variables.DataType;
import net.java.nboglpack.visualdesigner.ShaderNode;
import net.java.nboglpack.visualdesigner.shader.exporter.UnsupportedShaderExporterException;
import net.java.nboglpack.visualdesigner.ShaderProgramOutVariable;

/**
 * Creates node to simulate the hardware-interface.
 *
 * @author Samuel Sperling
 */
public class VertexOutputFactory implements IShaderProgramFactory {
    
    /** Creates a new instance of VertexOutputFactory */
    public VertexOutputFactory() {
    }

    public String[] getVariants() {
        return null;
    }

    public IShaderProgram createShaderProgram() {
        return new VertexOutputShader();
    }

    public IShaderProgram createShaderProgram(String variant) {
        return new VertexOutputShader();
    }

    public String getName() {
        return "Hadware Interface for Vertex Ouput";
    }

    public Color getNodeBackgroundColor() {
        return HardwareInterfaces.nodeBackgroundColor;
    }
    
}

/**
 *
 * @author Samuel Sperling
 */
class VertexOutputShader extends ShaderNode implements IShaderProgram {
    
    private HashMap<String, GlobalVariable> globalVariables;
    
    public VertexOutputShader() {
        super("Hadware Interface for Vertex Ouput");
        super.setShaderProgram(this);
        setBackground(HardwareInterfaces.nodeBackgroundColor);
        setUpVariables();
    }
    protected void initComponents() {
        super.initComponents();
        this.contextMenu.remove(mnuDelete);
    }

    private void setUpVariables() {
        
        globalVariables = HardwareInterfaces.setupVars(
                this,
                new String[] {"Vertex", "Normal", "Color", "SecondaryColor", "FogCoord"},
                new String[] {"Vertex", "Normal", "Color", "Secondary Color", "Fog Coord"},
                new int[] {DataType.DATA_TYPE_VEC4, DataType.DATA_TYPE_VEC3, DataType.DATA_TYPE_VEC4, DataType.DATA_TYPE_VEC4, DataType.DATA_TYPE_FLOAT},
                GlobalVariable.GLOBAL_HARDWARE_VERTEX_IN,
                false,
                false
                );
    }
    
    public void exportShaderCode(IShaderCodeExportVisitor shaderCodeExporter)
        throws UnsupportedShaderExporterException {
    }

    public ShaderNode getShaderNode() {
        return this;
    }

    public JPanel getProperiesPanel() {
        return null;
    }

    public ValueAssignment exportShaderCode(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor)
            throws ExportingExeption {
        GlobalVariable globalVar = globalVariables.get("gl_" + outputVariable.getName());
        if (globalVar == null)
            throw new ExportingExeption("Output variable '" + outputVariable.getName() + "' is not connected to Vertex2FragmentTransfer");
        
        return ValueAssignment.createVariableRedirectionAssignment(globalVar);
    }

    public IExportable[] getSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        GlobalVariable globalVar = globalVariables.get("gl_" + outputVariable.getName());
        if (globalVar == null)
            throw new ExportingExeption("Output variable '" + outputVariable.getName() + "' is not connected to Vertex2FragmentTransfer");
        
        return new IExportable[] {globalVar};
    }

    public boolean requiresSources(ShaderProgramOutVariable outputVariable, IShaderCodeExportVisitor exportVisitor) throws ExportingExeption {
        return true;
    }
    public Class getFactoryClass() {
        return VertexOutputFactory.class;
    }
    
    public String getVariant() {
        return null;
    }
}
