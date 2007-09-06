/*
 * IMeshable.java
 *
 * Created on 13. Mai 2006, 15:28
 *
 */

package net.java.nboglpack.visualdesigner.graphics3d;

/**
 * Klassen die dieses Interface implementieren,
 * K�nnen ein Mesh in einen buffer speichern und aus diesem wiederum rendern.
 * @author Samuel Sperling
 */
public interface IMeshable extends IRenderable {
    /**
     * Erzeugt einen Buffer und schreibt alle Daten aus dem �bergebenen Mesh hinein
     * Durch einen Aufruf der Render Funktion wird dieses mesh auf dem OpenGL Device ausgegeben.
     * @param mesh Daten des 3D Objektes welche in den Buffer geschrieben werden sollen.
     * @param drawMode Typ der Fl�che (GL_QUAD, GL_TRIANGLE, ...)
     */
    void createBuffer(Face3D[] mesh, int drawMode);
    /**
     * Erzeugt einen Buffer und schreibt alle Daten aus dem �bergebenen Mesh hinein
     * Durch einen Aufruf der Render Funktion wird dieses mesh auf dem OpenGL Device ausgegeben.
     * @param mesh Daten des 3D Objektes welche in den Buffer geschrieben werden sollen.
     * @param pointsPerFace Anzahl der Verticies pro Fl�che.
     * @param drawMode Typ der Fl�che (GL_QUAD, GL_TRIANGLE, ...)
     */
    void createBuffer(Face3D[] mesh, int pointsPerFace, int drawMode);
}
