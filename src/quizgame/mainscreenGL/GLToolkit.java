/*
 * GLToolkit.java
 *
 * Created on den 9 juni 2007, 00:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.mainscreenGL;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;

/**
 *
 * @author eklann
import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;

/**
 *
 */
public class GLToolkit {
    
    /** Creates a new instance of GLToolkit */
    public GLToolkit() {
    }
    
    public static int createCube(GL gl, Texture texture) {
        int cubeList = gl.glGenLists(1);
        gl.glNewList(cubeList, gl.GL_COMPILE);
        gl.glBegin(gl.GL_QUADS);
        
        //Front
        gl.glNormal3f(0f, 0f, 1f);
        gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().bottom());
        gl.glVertex3f(-.5f, -.5f, .5f);
        gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().top());
        gl.glVertex3f(-.5f, .5f, .5f);
        gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().top());
        gl.glVertex3f(.5f, .5f, .5f);
        gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().bottom());
        gl.glVertex3f(.5f, -.5f, .5f);
        
        //Back
        gl.glNormal3f(0f, 0f, -1f);
        gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().bottom());
        gl.glVertex3f(-.5f, -.5f, -.5f);
        gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().top());
        gl.glVertex3f(-.5f, .5f, -.5f);
        gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().top());
        gl.glVertex3f(.5f, .5f, -.5f);
        gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().bottom());
        gl.glVertex3f(.5f, -.5f, -.5f);
        
        //Left
        gl.glNormal3f(-1f, 0f, 0f);
        gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().bottom());
        gl.glVertex3f(-.5f, .5f, -.5f);
        gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().top());
        gl.glVertex3f(-.5f, .5f, .5f);
        gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().top());
        gl.glVertex3f(-.5f, -.5f, .5f);
        gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().bottom());
        gl.glVertex3f(-.5f, -.5f, -.5f);
        
        //Right
        gl.glNormal3f(1f, 0f, 0f);
        gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().bottom());
        gl.glVertex3f(.5f, .5f, -.5f);
        gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().top());
        gl.glVertex3f(.5f, .5f, .5f);
        gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().top());
        gl.glVertex3f(.5f, -.5f, .5f);
        gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().bottom());
        gl.glVertex3f(.5f, -.5f, -.5f);
        
        //Top
        gl.glNormal3f(0f, 1f, 0f);
        gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().bottom());
        gl.glVertex3f(.5f, .5f, .5f);
        gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().top());
        gl.glVertex3f(-.5f, .5f, .5f);
        gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().top());
        gl.glVertex3f(-.5f, .5f, -.5f);
        gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().bottom());
        gl.glVertex3f(.5f, .5f, -.5f);
        
        //Bottom
        gl.glNormal3f(0f, -1f, 0f);
        gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().bottom());
        gl.glVertex3f(.5f, -.5f, .5f);
        gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().top());
        gl.glVertex3f(-.5f, -.5f, .5f);
        gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().top());
        gl.glVertex3f(-.5f, -.5f, -.5f);
        gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().bottom());
        gl.glVertex3f(.5f, -.5f, -.5f);
        
        gl.glEnd();
        gl.glEndList();
        
        return cubeList;
    }    
}
