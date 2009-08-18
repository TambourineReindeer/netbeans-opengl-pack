/*
 * Created on 26. August 2006, 15:26
 */

package com.mbien.engine.glsl;

import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLWorker;
import com.mbien.engine.util.GLWorkerImpl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;

/**
 * @author Michael Bien
 */
public class GLSLShader {
    
 public final TYPE type;
 
 private final String source;
 final String[] shaderNames;
 
 private int handle;
 private String compilerMsg;
 
 private final static EnumMap<TYPE, Boolean> SUPPORTED_SHADER = new EnumMap<TYPE, Boolean>(TYPE.class);

 private boolean throwExceptionOnCompilerWarning = false;

 private CodeFragment[] fragments;
 
    public GLSLShader(String... filePath) {
        this(toFiles(filePath));
    }
    
    public GLSLShader(File... files) {
        
        this.type = TYPE.parse(files[0].getName());
        
        if(type==null) 
            throw new IllegalArgumentException("Wrong file ending; only .[gl(sl)]frag, .[gl(sl)]vert and .[gl(sl)]geom endings are allowed");
        
        shaderNames = new String[files.length];
        source = readSourceFile(files);
    }

    public GLSLShader(TYPE type, File... files) {
        
        if(type == null)
            throw new IllegalArgumentException("null as shader type not allowed");
        
        this.type = type;
        shaderNames = new String[files.length];
        source = readSourceFile(files);
    }

    public GLSLShader(TYPE type, String source, String name) {
        
        if(type == null)
            throw new IllegalArgumentException("null as shader type not allowed");

        this.type = type;
        shaderNames = new String[]{ name };
        this.source = source;

    }
    
    public GLSLShader(TYPE type, String[] sources, String[] names) {
        
        if(type == null)
            throw new IllegalArgumentException("null as shader type not allowed");
        
        this.type = type;
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sources.length; i++) {
            sb.append(sources[i]);
        }
        
        shaderNames = names;
        source = sb.toString();
    }

    public GLSLShader(TYPE type, String filepath, ShaderSourceLoader provider) {

        if(type == null)
            throw new IllegalArgumentException("null as shader type not allowed");

        fragments = provider.loadWithDependencies(filepath);

        this.type = type;
        this.shaderNames = new String[fragments.length];

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fragments.length; i++) {
            shaderNames[i] = fragments[i].name;
            sb.append(fragments[i].source).append("\n");
        }

        this.source = sb.toString();
    }

    public GLSLShader(TYPE type, CodeFragment mainFragment, String path, ShaderSourceLoader provider) {

        if(type == null)
            throw new IllegalArgumentException("null as shader type not allowed");

        fragments = provider.loadWithDependencies(mainFragment, path);

        this.type = type;
        this.shaderNames = new String[fragments.length];

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fragments.length; i++) {
            shaderNames[i] = fragments[i].name;
            sb.append(fragments[i].source).append("\n");
        }

        this.source = sb.toString();
    }

//    public GLSLShader(File file, boolean importDependencies) {
//
//        if(type == null)
//            throw new IllegalArgumentException("null as shader type not allowed");
//
//        this.type = type;
//        shaderNames = new String[]{ name };
//        this.source = source;
//    }
    
    private static File[] toFiles(String... names)  {
        File[] files = new File[names.length];
        
        for(int i = 0; i < files.length; i++) 
            files[i] = new File(names[i]);
        
        return files;
    }

    private final String readSourceFile(File... files) {
        
        StringBuilder sb = new StringBuilder();
                
        for(int i = 0; i < files.length; i ++) {
            
            FileReader reader = null;
            
            int length = 0;
            File sourceFile = files[i];
            char[] buffer = new char[(int)sourceFile.length()];
            shaderNames[i] = sourceFile.getName();
            try {
                reader = new FileReader(sourceFile);
                length = reader.read(buffer);
            } catch (FileNotFoundException ex) {
                getLog().severe("shader source not found\n"+ex.getMessage());
            } catch (IOException ex) {
                getLog().severe("exception while reading shader source\n"+ex.getMessage());
            }finally {
                if(reader != null) {
                    try{
                        reader.close();
                    }catch(IOException e){
                        getLog().severe("can't close opened stream\n"+e.getMessage());                 
                    }
                }
            }
            
            sb.append(buffer, 0, length);
        }             

        getLog().fine("shader source read done");
        
        return sb.toString();
    }
    
    public void initShader(GL2GL3 gl) throws GLSLCompileException {
        handle = gl.glCreateShader(type.GL_TYPE);
        gl.glShaderSource(handle, 1, new String[] {source}, new int[] {source.length()}, 0);
        gl.glCompileShader(handle);
        checkShader(gl);
    }
    
    public void deleteShader(GL2GL3 gl) {
        gl.glDeleteShader(handle);
    }
    
    private void checkShader(GL2GL3 gl) throws GLSLCompileException {
        
        boolean error = false;
        
        // check compile state
        int[] buffer = new int[1];
        gl.glGetShaderiv(handle, GL2GL3.GL_COMPILE_STATUS, buffer, 0);
        if(buffer[0] == GL.GL_FALSE) {
//            getLog().warning("error compiling shader:\n"+getName());
            error = true;
        }
        
        // log info log
        gl.glGetShaderiv(handle, GL2GL3.GL_INFO_LOG_LENGTH, buffer, 0);
        
        if(buffer[0] > 0) {
            byte[] log = new byte[buffer[0]];
            gl.glGetShaderInfoLog(handle, buffer[0], buffer, 0, log, 0);
            
            compilerMsg = new String(log, 0, log.length-1);
            
            if(error || throwExceptionOnCompilerWarning && compilerMsg.contains("WARNING")) {
                throw new GLSLCompileException(this, compilerMsg.split("\n"));
            }
        }
    }
    
    public static Logger getLog() {
        return Logger.getLogger(GLSLShader.class.getPackage().getName());
    }
    
    public int getID() {
        return handle;
    }
    
    public void setThrowExceptionOnCompilerWarning(boolean b) {
        this.throwExceptionOnCompilerWarning = b;
    }
    
    public String getName() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < shaderNames.length; i++) {
            sb.append(shaderNames[i]);
            if(i+1 < shaderNames.length)
                sb.append(", ");
        }
        return sb.toString();
    }

    public String getCompilerMsg() {
        return compilerMsg;
    }

    public CodeFragment[] getFragments() {
        return fragments;
    }

    private static boolean isShaderSupported(TYPE type) {
        
        if(SUPPORTED_SHADER.get(type) == null) {
            
           GLWorker worker = GLWorkerImpl.getDefault();

           worker.work(new GLRunnable() {
              public void run(GLContext context) {
                  GL gl = context.getGL();
                  SUPPORTED_SHADER.put(TYPE.VERTEX, gl.isExtensionAvailable(TYPE.VERTEX.EXT_STRING));
                  SUPPORTED_SHADER.put(TYPE.FRAGMENT, gl.isExtensionAvailable(TYPE.FRAGMENT.EXT_STRING));
                  SUPPORTED_SHADER.put(TYPE.GEOMETRY, gl.isExtensionAvailable(TYPE.GEOMETRY.EXT_STRING));
              }
           });
           
        }
        
        return SUPPORTED_SHADER.get(type);
    }

    public enum TYPE {
        
        VERTEX(GL2GL3.GL_VERTEX_SHADER, "GL_ARB_vertex_shader"),
        FRAGMENT(GL2GL3.GL_FRAGMENT_SHADER, "GL_ARB_fragment_shader"),
        GEOMETRY(GL2GL3.GL_GEOMETRY_SHADER, "GL_EXT_geometry_shader4");
                
        public final int GL_TYPE;
        public final String EXT_STRING;
        
        private TYPE(int gltype, String extention) {
            GL_TYPE = gltype;
            EXT_STRING = extention;
        }
        
        public static TYPE parse(String ext) {
            String _ext="."+ext;
            if(_ext.endsWith(".frag") || _ext.endsWith(".glfrag") || _ext.endsWith(".glslfrag") || _ext.endsWith(".gl-frag") || _ext.endsWith(".glsl-frag"))
                return TYPE.FRAGMENT;
            if(_ext.endsWith(".vert") || _ext.endsWith(".glvert") || _ext.endsWith(".glslvert") || _ext.endsWith(".gl-vert") || _ext.endsWith(".glsl-vert"))
                return TYPE.VERTEX;
            if(_ext.endsWith(".geom") || _ext.endsWith(".glgeom") || _ext.endsWith(".glslgeom") || _ext.endsWith(".gl-geom") || _ext.endsWith(".glsl-geom"))
                return TYPE.GEOMETRY;
            return null;
        }
        
        public static TYPE fromMime(String string) {
            if(string.equalsIgnoreCase("text/x-glsl-fragment-shader"))
                return TYPE.FRAGMENT;
            else if(string.equalsIgnoreCase("text/x-glsl-vertex-shader"))
                return TYPE.VERTEX;
            else if(string.equalsIgnoreCase("text/x-glsl-geometry-shader"))
                return TYPE.GEOMETRY;
            return null;
        }

        public boolean isSupported() {
            return isShaderSupported(this);
        }
    }
}
