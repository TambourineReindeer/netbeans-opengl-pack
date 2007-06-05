/*
 * GLSLElementDescriptor.java
 * 
 * Created on 03.06.2007, 23:03:57
 * 
 */
package net.highteq.gamedev.nbm.glsleditor.vocabulary;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Michael Bien
 */
public class GLSLElementDescriptor {
 
 @XmlType
 @XmlEnum(String.class)
 public enum Category { BUILD_IN_FUNC, BUILD_IN_VAR, JUMP, ITERATION, SELECTION, QUALIFIER, TYPE, KEYWORD }
 
  @XmlElement
  public final Category category;
  
  @XmlElement(required = true)
  public final String tooltip;
  
  @XmlElement(required = true)
  public final String doc;
  
  @XmlElement(required = true)
  public final String arguments;
  
  @XmlElement(required = true)
  public final String type;

    public GLSLElementDescriptor() {
        category    = Category.KEYWORD;
        tooltip     = null;
        doc         = null;
        arguments   = null;
        type        = null;
    }
    
}
