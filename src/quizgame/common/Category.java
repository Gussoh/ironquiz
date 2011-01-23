/*
 * Category.java
 *
 * Created on den 27 november 2006
 */

package quizgame.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 * @author rheo
 */
public class Category implements Comparable<Category>, Serializable {
    public String name;
    public Question[] questions;
    
    public String toString() {
        return name;
    }
    
    public int compareTo(Category category) {
        return name.compareToIgnoreCase(category.name);
    }
}
