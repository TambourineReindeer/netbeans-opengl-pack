/*
 * BufferedMesh.java
 *
 * Created on 11. Mai 2006, 23:58
 */

package net.java.nboglpack.visualdesigner.graphics3d;

import com.sun.opengl.util.BufferUtil;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GLException;

/**
 * Diese Klasse steht f�r informationen eines Mesh welche im Speicher der Grafikharder liegen.
 * Die Klasse bietet M�glichkeiten zur Generierung dieses Buffers und zum Rendern.
 * @author Samuel Sperling
 */
public class BufferedMesh implements IMeshable {
    /**
     * X, Y, Z
     */
    private static final int DIMENSION = 3;
    /**
     * IDs des Buffers im Grafikspeicher {vertex, normal, index}
     */
    private IntBuffer arbIDs;
    /**
     * Anzahl aller Vertices. inkl. Doppelnutznug
     */
    private int bufferLength;
    /**
     * Referenz des OpenGL Devices auf welchem gerender werden soll.
     */
    private GL gl;
    /**
     * Typ der Fl�che (GL_QUAD, GL_TRIANGLE, ...)
     */
    private int drawMode;
    /**
     * True, falls die Hardware den Buffer unterst�tzt
     */
    private boolean hasHardwaresupport = true;
    /**
     * Daten des 3D Objektes welche in den Grafikspeicher geschrieben
     * und durch diesen Buffer repr�sentiert werden soll.
     * Diese Daten werden nur gehalten, falls die Grafikharware keinen Buffer unterst�tzt.
     */
    private Face3D[] mesh;
    
    /**
     * Erzeugt eine Instanz der Klasse BufferedIndexedMesh
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     */
    public BufferedMesh(GL gl) {
        this.gl = gl;
        arbIDs = BufferUtil.newIntBuffer(3);
        arbIDs.put(new int[] {-1, -1, -1});
    }
    
    /**
     * Erzeugt eine Instanz der Klasse BufferedIndexedMesh
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     * @param mesh Daten des 3D Objektes welche in den Grafikspeicher geschrieben
     * und durch diesen Buffer repr�sentiert werden soll.
     * @param DrawMode Typ der Fl�che (GL_QUAD, GL_TRIANGLE, ...)
     * Durch angabe des Typs wird automatisch entschieden, wieviele Punkte f�r eine Fl�che genutzt werden.
     */
    public BufferedMesh(GL gl, Face3D[] mesh, int DrawMode) {
        this.gl = gl;
        arbIDs = BufferUtil.newIntBuffer(3);
        arbIDs.put(new int[] {-1, -1, -1});
        createBuffer(mesh, DrawMode);
    }
    
    /**
     * Erzeugt eine Instanz der Klasse BufferedIndexedMesh
     * @param gl Referenz des OpenGL Devices auf welchem gerender werden soll.
     * @param mesh Daten des 3D Objektes welche in den Grafikspeicher geschrieben
     * und durch diesen Buffer repr�sentiert werden soll.
     * @param pointsPerFace Anzahl der Verticies pro Fl�che.
     * @param DrawMode Typ der Fl�che (GL_QUAD, GL_TRIANGLE, ...)
     */
    public BufferedMesh(GL gl, Face3D[] mesh, int pointsPerFace, int DrawMode) {
        this.gl = gl;
        arbIDs = BufferUtil.newIntBuffer(3);
        arbIDs.put(new int[] {-1, -1, -1});
        createBuffer(mesh, pointsPerFace, DrawMode);
    }

    /**
     * Erzeugt einen Buffer und schreibt alle Daten aus dem �bergebenen Mesh in den Speicher
     * der Grafikhardware.
     * Durch einen Aufruf der Render Funktion wird dieses mesh auf dem OpenGL Device ausgegeben.
     * @param mesh Daten des 3D Objektes welche in den Grafikspeicher geschrieben
     * und durch diesen Buffer repr�sentiert werden soll.
     * @param DrawMode Typ der Fl�che (GL_QUAD, GL_TRIANGLE, ...)
     */
    public void createBuffer(Face3D[] mesh, int DrawMode) {
        int pointsPerFace;
        switch(DrawMode) {
            case GL.GL_POINTS:
                pointsPerFace = 1;
                break;
            case GL.GL_LINES:
                pointsPerFace = 2;
                break;
            case GL.GL_TRIANGLES:
                pointsPerFace = 3;
                break;
            case GL.GL_QUADS:
                pointsPerFace = 4;
                break;
            default:
                // autodetect
                pointsPerFace = 5;
        }
        createBuffer(mesh, pointsPerFace, DrawMode);
    }
    /**
     * Erzeugt einen Buffer und schreibt alle Daten aus dem �bergebenen Mesh in den Speicher
     * der Grafikhardware.
     * Durch einen Aufruf der Render Funktion wird dieses mesh auf dem OpenGL Device ausgegeben.
     * @param mesh Daten des 3D Objektes welche in den Grafikspeicher geschrieben
     * und durch diesen Buffer repr�sentiert werden soll.
     * @param pointsPerFace Anzahl der Verticies pro Fl�che.
     * @param DrawMode Typ der Fl�che (GL_QUAD, GL_TRIANGLE, ...)
     */
    public void createBuffer(Face3D[] mesh, int pointsPerFace, int DrawMode) {
        this.drawMode = DrawMode;
        try {
            // Alte Buffer l�schen
            if (arbIDs.get(0) != -1) {
                gl.glDeleteBuffersARB(2, arbIDs);
                arbIDs = BufferUtil.newIntBuffer(3);
                arbIDs.put(new int[] {-1, -1, -1});
            }

            // if (pointsPerFace == 0) count...
            bufferLength = mesh.length * pointsPerFace;

            // Verticies in den Buffer schreiben.
            FloatBuffer vertices = BufferUtil.newFloatBuffer(bufferLength * DIMENSION);
            FloatBuffer normals = BufferUtil.newFloatBuffer(bufferLength * DIMENSION);

            // Daten in die Buffer schreiben
            Vector3D[] vector;
            int z = 0;
            for (int i = 0; i < mesh.length; i++) {
                vector = mesh[i].getCoords();
                for (int v = 0; v < pointsPerFace; v++) {
                    vector[v].buffer(vertices, normals, z);
                    z += DIMENSION;
                }
            }

            // Buffer generieren
            gl.glGenBuffersARB(2, arbIDs);

            // Vertex Buffer binden (aktivieren)
            gl.glBindBufferARB( GL.GL_ARRAY_BUFFER_ARB, arbIDs.get(0) );
            gl.glBufferDataARB( GL.GL_ARRAY_BUFFER_ARB, mesh.length * pointsPerFace * DIMENSION * BufferUtil.SIZEOF_FLOAT, vertices, GL.GL_DYNAMIC_DRAW_ARB);

            // Normal Buffer binden (aktivieren)
            gl.glBindBufferARB( GL.GL_ARRAY_BUFFER_ARB, arbIDs.get(1) );
            gl.glBufferDataARB( GL.GL_ARRAY_BUFFER_ARB, mesh.length * pointsPerFace * DIMENSION * BufferUtil.SIZEOF_FLOAT, normals, GL.GL_DYNAMIC_DRAW_ARB);
        } catch (GLException e) {
            
            /* Falls die Hardware die Buffernutzung nicht unterst�tzt,
             * werden die Mesh-Informationen roh gespeichert
             */
            hasHardwaresupport = false;
            this.mesh = mesh;
        }
    }
    
    /**
     * Rendert den Buffer. Dabei wird das OpenGL Device genutzt welches dem Konstruktor
     * �bergeben wurde.
     */
    public void render() {
        if (this.hasHardwaresupport) {
           
            gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL.GL_NORMAL_ARRAY);

            // Vertex Buffer binden (aktivieren)
            gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, arbIDs.get(0));
            gl.glVertexPointer(DIMENSION, GL.GL_FLOAT, 0, (Buffer) null);

            // Normal Buffer binden (aktivieren)
            gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, arbIDs.get(1));
            gl.glNormalPointer(GL.GL_FLOAT, 0, (Buffer) null);

            // Buffered Vertices malen
            gl.glDrawArrays(this.drawMode, 0, this.bufferLength);

            gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
        } else {
            
            /* Falls die Hardware die Buffernutzung nicht unterst�tzt,
             * werden die Faces einzeln gerendert.
             */
            gl.glBegin(this.drawMode);
            for (int i = 0; i < this.mesh.length; i++) {
                this.mesh[i].render(gl);
            }
            gl.glEnd();
        }
    }

    /**
     * Rendert die Normalen des 3DObjektes aus dem Buffer.
     * Dabei wird das OpenGL Device genutzt welches dem Konstruktor �bergeben wurde.
     */
    public void renderNormals() {
    }
}
