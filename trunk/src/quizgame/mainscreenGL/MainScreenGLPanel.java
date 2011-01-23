/*
 * MainScreenGLPanel.java
 *
 * Created on den 8 juni 2007, 23:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.mainscreenGL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLJPanel;
import javax.media.opengl.glu.GLU;
import quizgame.common.InGameCategory;

/**
 *
 * @author eklann
 */
public class MainScreenGLPanel extends GLJPanel implements GLEventListener {
        
    private Texture categoryTexture;
    
    private List<InGameCategory> categoryList;
    private List<Integer> categoryCubeList;
    private boolean categoriesUpdated = false;
    
    int standardCube;
    
    /** Creates a new instance of MainScreenGLPanel */
    public MainScreenGLPanel(Dimension dimension) {
        categoryList = new ArrayList<InGameCategory>();
        categoryCubeList = new ArrayList<Integer>();
        
        setPreferredSize(dimension);

        addGLEventListener(this);
        requestFocus();
    }

    public void init(GLAutoDrawable gLAutoDrawable) {
        //koppat from gltest --->
        GL gl = gLAutoDrawable.getGL();
        gl.glShadeModel(gl.GL_SMOOTH);
        
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        gl.glClearDepth(1.0f);
        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glDepthFunc(gl.GL_LEQUAL);
        gl.glHint(gl.GL_POLYGON_SMOOTH_HINT, gl.GL_NICEST);
        
        gl.glHint(gl.GL_PERSPECTIVE_CORRECTION_HINT, gl.GL_NICEST);
        gl.glEnable(gl.GL_LIGHTING);
        float ambient[] = {.5f, .5f, .5f, 1f};
        float diffuse[] = {1f, 1f, 1f, 1f};
        float position[] = {0f, 0f, 0f, 1f};
        
        gl.glLightfv(gl.GL_LIGHT1, gl.GL_AMBIENT, ambient, 0);
        gl.glLightfv(gl.GL_LIGHT1, gl.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(gl.GL_LIGHT1, gl.GL_POSITION, position, 0);
        gl.glEnable(gl.GL_LIGHT1);
        
        // <--- till hit

        try {
            categoryTexture = TextureIO.newTexture(new File("img/category.jpg"), true);
        } catch (GLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }        
        
        //Skapa globala listor
        standardCube = GLToolkit.createCube(gl, categoryTexture);
    }

    public void display(GLAutoDrawable gLAutoDrawable) {
        //initiera skit
        
        GL gl = gLAutoDrawable.getGL();

        /*
        if (categoriesUpdated) {
            categoryCubeList.clear();
            for (InGameCategory c : categoryList) {
                categoryCubeList.add(GLToolkit)
            }
            categoriesUpdated = false;
        }
        */
        
	gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);			// Clear The Screen And The Depth Buffer
	gl.glLoadIdentity();							// Reset The Current Modelview Matrix        
        
        gl.glEnable(gl.GL_TEXTURE_2D);

        categoryTexture.enable();
        
        gl.glTranslatef(0f, 0f, -8f);

        //gl.glCallList(standardCube);
        
        for (int i = 0; i < categoryList.size(); i++) {
            gl.glCallList(standardCube);
            gl.glTranslatef(1.2f, 0f, 0f);
            System.out.println("Ritade " + categoryList.get(i).name);
        }
    }

    public void reshape(GLAutoDrawable gLAutoDrawable, int i, int i0, int i1, int i2) {
        //koppat from gltest
    
        GL gl = gLAutoDrawable.getGL();
        gl.glMatrixMode(gl.GL_PROJECTION);
        gl.glLoadIdentity();
        
        GLU glu = new GLU();
        glu.gluPerspective(45.0f, (float)(4 / 3), 0.1f, 100.0f);
        
        gl.glMatrixMode(gl.GL_MODELVIEW);
        gl.glLoadIdentity();
        
    }

    public void displayChanged(GLAutoDrawable gLAutoDrawable, boolean b, boolean b0) {
        //bry??
    }

    void setCategories(InGameCategory[] categories) {        
        categoryList.clear();
        categoryCubeList.clear();
        
        for (int i = 0; i < categories.length; i++) {
            categoryList.add(categories[i]);
        }
        
        categoriesUpdated = true;
    }
    
}
