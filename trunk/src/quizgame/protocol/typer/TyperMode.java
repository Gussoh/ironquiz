/*
 * TyperMode.java
 *
 * Created on den 9 mars 2007, 23:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.typer;

import java.awt.BorderLayout;
import javax.swing.SwingUtilities;
import quizgame.typer.Typer;

/**
 *
 * @author Dukken
 */
public abstract class TyperMode implements TyperPacket {
    
    public abstract void run(final Typer t);
    
    public void resetFrame(final Typer t) {
        t.getMainPanel().removeAll();
        t.getMainPanel().setLayout(new BorderLayout());
        t.getJFrame().validate();
    }
}
