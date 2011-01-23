/*
 * SortedArrays.java
 *
 * Created on den 2 december 2006
 */

package quizgame.admin;

import java.util.Arrays;

/**
 * Contains various methods for manipulating sorted arrays.
 * @author rheo
 */
public class SortedArrays {
    /** For arrays of objects whose compareTo method is inconsistent with equals. */ 
    public static <T extends Comparable<T>> int binaryIndexOf(T[] array, T key) {
        int index = Arrays.binarySearch(array, key);
        
        if(key.equals(array[index])) {
            return index;
        }
        for(int i = index + 1; i < array.length && array[i - 1].compareTo(array[i]) == 0; i++) {
            if(key.equals(array[i])) {
                return i;
            }
        }
        for(int i = index - 1; i >= 0 && array[i + 1].compareTo(array[i]) == 0; i--) {
            if(key.equals(array[i])) {
                return i;
            }
        }
        
        return -1;
    }
    
    public static <T> T[] add(T[] oldArray, T[] newArray, int index, T element) {
        newArray[index] = element;
        System.arraycopy(oldArray, 0, newArray, 0, index);
        System.arraycopy(oldArray, index, newArray, index + 1, oldArray.length - index);
        return newArray;
    }
    
    public static <T> T[] remove(T[] oldArray, T[] newArray, int index) {
        System.arraycopy(oldArray, 0, newArray, 0, index);
        System.arraycopy(oldArray, index + 1, newArray, index, newArray.length - index);
        return newArray;
    }
    
    public static <T> int move(T[] array, int oldIndex, int newIndex) {
        T element = array[oldIndex];
        
        if(newIndex < 0) {
            newIndex = -newIndex - 1;
        }
        
        if(newIndex > oldIndex) {
            newIndex--;
            System.arraycopy(array, oldIndex + 1, array, oldIndex, newIndex - oldIndex);
        } else if(newIndex < oldIndex) {
            System.arraycopy(array, newIndex, array, newIndex + 1, oldIndex - newIndex);
        }
        
        array[newIndex] = element;
        
        return newIndex;
    }
}
