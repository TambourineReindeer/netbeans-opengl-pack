/*
 * VertexShader.java
 *
 * Created on March 28, 2007, 6:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import java.io.IOException;
import java.io.Reader;
import javax.media.opengl.GL;

/**
 *
 * @author Samuel Sperling
 */
public class FragmentShader extends Shader {
    
    public FragmentShader(GL gl, String[] contents) {
        super(gl, gl.GL_FRAGMENT_SHADER_ARB, contents);
    }
    
    /** Creates a new instance of FragmentShader */
    public FragmentShader(GL gl, Reader contents) throws IOException {
        super(gl, gl.GL_FRAGMENT_SHADER_ARB, getContents(contents));
    }
     
    public void finalize() throws Throwable {
        if (program != null)
            program.detatchFragmentShader();
        super.finalize();
    }   
    
}
