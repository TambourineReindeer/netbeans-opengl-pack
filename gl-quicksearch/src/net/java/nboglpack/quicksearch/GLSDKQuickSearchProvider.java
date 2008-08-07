/*
 * Created on 5. August 2008, 01:14
 */

package net.java.nboglpack.quicksearch;

/**
 * OpenGL SDK specification quicksearch provider for http://www.opengl.org/sdk/docs/man/xhtml/.
 * @author Michael Bien
 */
public class GLSDKQuickSearchProvider extends WebPageQuickSearchProvider {
    
    private final static String base = "http://www.opengl.org/sdk/docs/man/xhtml/";

    public GLSDKQuickSearchProvider() {
        super(base + "index.html");
    }
    
    @Override
    String filter(String href, String name) {
        //ignore inter page links
        if(href.charAt(0) == '#')
            return null;
        return base+href;
    }

}
