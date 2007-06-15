package net.java.nboglpack.glsleditor.dataobject;

import net.java.nboglpack.glslcompiler.ShaderFileObserver;
import net.java.nboglpack.glslcompiler.ShaderFileObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.text.Document;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.text.CloneableEditorSupport;
import org.openide.text.DataEditorSupport;

public class GlslVertexShaderObject extends MultiDataObject {
    private ShaderFileObserver observer;

    public GlslVertexShaderObject(FileObject pf, GlslVertexShaderLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        CookieSet cookies = getCookieSet();
        observer= new ShaderFileObserver(this);
        final CloneableEditorSupport support= DataEditorSupport.create(this, getPrimaryEntry(), cookies);
        support.addPropertyChangeListener(
            new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent event) {
                    if("document".equals(event.getPropertyName())){
                        if(event.getNewValue()!=null)
                        {
                            support.getDocument().addDocumentListener(observer);
                            observer.runCompileTask();
                        }
                        else if(event.getOldValue()!=null)
                        {
                            // cylab: I think this is never called.
                            // But I don't know if unregistering the observer makes any difference...
                            ((Document)event.getOldValue()).removeDocumentListener(observer);
                        }
                    }
                }
            }
        );
        cookies.add((Node.Cookie) support);
    }
    
    protected Node createNodeDelegate() {
        return new GlslVertexShaderNode(this);
    }
}
