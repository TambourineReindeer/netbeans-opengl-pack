package net.highteq.gamedev.nbm.glsleditor.dataobject;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import com.mbien.glslcompiler.*;
import java.beans.PropertyChangeListener;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.text.CloneableEditorSupport;
import org.openide.text.DataEditorSupport;

public class GlslFragmentShaderObject extends MultiDataObject {
    private ShaderFileObserver observer;
    
    public GlslFragmentShaderObject(FileObject pf, GlslFragmentShaderLoader loader) throws DataObjectExistsException, IOException {
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
        return new GlslFragmentShaderNode(this);
    }

}
