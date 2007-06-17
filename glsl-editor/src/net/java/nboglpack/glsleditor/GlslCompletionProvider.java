package net.java.nboglpack.glsleditor;

import javax.swing.JEditorPane;
import org.netbeans.api.editor.completion.Completion;
import org.openide.ErrorManager;
import org.netbeans.editor.BaseDocument;
import org.netbeans.spi.editor.completion.CompletionDocumentation;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

import javax.swing.Action;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Iterator;
import net.java.nboglpack.glsleditor.vocabulary.GLSLElementDescriptor;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;


/**
 * Completion provider for the OpenGL Shading Language editor.
 *
 * @author Mathias Henze
 * @author Michael Bien
 */
public class GlslCompletionProvider implements CompletionProvider
{
    private static final ErrorManager LOGGER = ErrorManager.getDefault().getInstance(GlslCompletionProvider.class.getName());

    public GlslCompletionProvider()
    {
    }

    public CompletionTask createTask(int queryType, JTextComponent component)
    {
            String mimetype="text/plain";
            if(component instanceof JEditorPane)
            {
                    mimetype= ((JEditorPane)component).getContentType();
            }
            return new AsyncCompletionTask(new GlslCompletionQuery(mimetype),component);
    }

    public int getAutoQueryTypes(JTextComponent component, String typedText)
    {
            return 0;
    }

    private static class GlslCompletionQuery extends AsyncCompletionQuery
    {
        private GlslVocabularyManager vocabulary;
        public GlslCompletionQuery(String mimetype)
        {
                vocabulary = GlslVocabularyManager.getInstance(mimetype);
        }

        protected void query(CompletionResultSet completionResultSet, Document document, int pos)
        {
                fillResultset(completionResultSet,document,pos);
                completionResultSet.finish();
        }

        private void fillResultset(CompletionResultSet completionResultSet, Document doc, int pos)
        {
                Element paragraph= ((BaseDocument)doc).getParagraphElement(pos);
                String prefix= "";
                try
                {
                        prefix= doc.getText(paragraph.getStartOffset(),pos-paragraph.getStartOffset());
                        // daf�r sorgen, dass wir in den meisten f�llen einen korrekten prefix haben
                        // TODO: besser machen, ist ne hau-ruck-methode
                        // problem: bei leerzeichen in strings werden auch dort funktoren als autocomplete angeboten...
                        prefix= prefix.replaceAll(".*?([\\w-\"]*)$","$1").toLowerCase();
                }
                catch (BadLocationException e)
                {
                        LOGGER.notify(e);
                }
                
                Iterator it= vocabulary.getKeys().iterator();
                while (it.hasNext())  {
                    
                    String name= (String)it.next();
                    if(name.toLowerCase().startsWith(prefix))   {
                        
                        GLSLElementDescriptor[] elements = vocabulary.getDesc(name);
                        if(elements != null) {
                            
                            for (GLSLElementDescriptor element : elements) {

                                completionResultSet.addItem(
                                        new GlslCompletionItem(
                                                name,
                                                element,
                                                prefix,
                                                pos
                                        )
                                );
                            }
                        }
                    }
                }
        }
    }

    private static class GlslCompletionItem implements CompletionItem    {
        
        private String key;
        private GLSLElementDescriptor content;
        private int carretPosition = 0;
        private String prefix;
        
        private String leftText;
        private String rightText;
        
        private String fontColor = "<font color=#b200b2>";
        private String ARGUMENTS_COLOR = "<font color=#b28b00>";
        private String BUILD_IN_VAR_COLOR = "<font color=#008618>";
        private String KEYWORD_COLOR = "<font color=#000099>";
        
        private int priority;

        public GlslCompletionItem(String key, GLSLElementDescriptor content, String prefix, int carretPosition)    {
            
            this.key = key;
            this.content = content;
            this.prefix = prefix;
            this.carretPosition = carretPosition;
            
            leftText = createLeftText();
            rightText = content.type;
            
            // low prority is first in completion list
            switch(content.category) {
                case TYPE:
                    priority = 10;
                    break;
                case JUMP:
                    priority = 9;
                    break;
                case SELECTION:
                    priority = 8;
                    break;
                case ITERATION:
                    priority = 7;
                    break;
                case KEYWORD:
                    priority = 6;
                    break;
                case QUALIFIER:
                    priority = 5;
                    break;
                case BUILD_IN_FUNC:
                    priority = 4;
                    break;
                case BUILD_IN_VAR:
                    priority = 3;
                    break;
            }
        }
        
        private String createLeftText() {
            
            StringBuilder text = new StringBuilder();
            
            switch(content.category) {
                case TYPE:
                case JUMP:
                case SELECTION:
                case ITERATION:
                case KEYWORD:
                case QUALIFIER:
                    text.append(KEYWORD_COLOR); 
                    break;
                case BUILD_IN_VAR:
                    text.append(BUILD_IN_VAR_COLOR);
                    break;
            }
            text.append("<b>");
            text.append(key);
            text.append("</b></font>");
            
            if(content.arguments != null){
                text.append(ARGUMENTS_COLOR);
                text.append(content.arguments);
                text.append("</font>");
            }
            return text.toString();
        }

        public String getKey()
        {
                return key;
        }

        public GLSLElementDescriptor getContent()
        {
                return content;
        }

        public int getCarretPosition()
        {
                return carretPosition;
        }

        public String getPrefix()
        {
                return prefix;
        }

        public void defaultAction(JTextComponent component)
        {
                Completion.get().hideAll();
                try
                {
                        component.getDocument().insertString(carretPosition,key.substring(prefix.length()),null);
                }
                catch (BadLocationException e)
                {
                        LOGGER.notify(e);
                }
        }

        public void processKeyEvent(KeyEvent evt)   {
            // TODO: if "." then Completion.get().showCompletion()
        }

        public int getPreferredWidth(Graphics g, Font defaultFont)     {
            return CompletionUtilities.getPreferredWidth(leftText, rightText, g, defaultFont);
        }

        public void render(Graphics g, Font defaultFont, Color defaultColor, Color backgroundColor, int width, int height, boolean selected)   {
//                itemLabel.setFont(defaultFont);
//                itemLabel.setForeground(defaultColor);
//                itemLabel.setBackground(backgroundColor);
//                itemLabel.setBounds(0,0,width,height);
//                if(selected)
//                {
//                        itemLabel.setBackground(UIManager.getDefaults().getColor("MenuItem.selectionBackground"));
//                        itemLabel.setForeground(UIManager.getDefaults().getColor("MenuItem.selectionForeground"));
//                }
//                itemLabel.paint(g);
            CompletionUtilities.renderHtml(null, leftText, rightText, g, defaultFont, defaultColor, width, height, selected);
        }

        public CompletionTask createDocumentationTask()
        {

            if(content.doc == null)
                return null;

            return new AsyncCompletionTask(
                new AsyncCompletionQuery()
                {
                        private DocItem item= new DocItem(content);
                        protected void query(CompletionResultSet completionResultSet, Document document, int i)
                        {
                                completionResultSet.setDocumentation(item);
                                completionResultSet.finish();
                        }
                }
            );
        }

        public CompletionTask createToolTipTask()
        {
                return null;
        }

        public boolean instantSubstitution(JTextComponent component)
        {
                defaultAction(component);
                return true;
        }

        public int getSortPriority()
        {
                return priority;
        }

        public CharSequence getSortText()
        {
                return key;
        }

        public CharSequence getInsertPrefix()
        {
                return prefix;
        }
    }

    public static class DocItem implements CompletionDocumentation
    {
        private GLSLElementDescriptor content;

        public DocItem(GLSLElementDescriptor content)
        {
                this.content = content;
        }

        public String getText()
        {
                return content.doc;
        }

        public URL getURL()
        {
                return null;
        }

        public CompletionDocumentation resolveLink(String link)
        {
                return null;
        }

        public Action getGotoSourceAction()
        {
                return null;
        }
    }
	
}