/*
 * Entry.java
 *
 * Created on November 27, 2006, 5:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.common;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author gsohtell
 */
public class Entry<K, V> implements Map.Entry<K, V>, Serializable {
    private Object key;
    private Object value;
    
    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() {
        return (K)key;
    }
    
    public V getValue() {
        return (V)value;
    }
    
    public V setValue(V value) {
        this.value = value;
        return (V)value;
    }

    public String toString() {
        return key + " -> " + value;
    }
}
