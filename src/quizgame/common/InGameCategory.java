/*
 * InGameCategory.java
 *
 * Created on den 4 december 2006
 */

package quizgame.common;

import java.io.Serializable;

/**
 * @author rheo
 */
public class InGameCategory implements Serializable {
    public InGameQuestion[] questions;
    public String name;
}
