/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

/**
 *
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class Header {
    
    private boolean is_numeric = true;
    private boolean is_totalorder = true;
    
    private HashSet<String> attributes = new HashSet<String>(); 
    private String[] attributes_order;
    private String name;

    Header(String header) {
        name = header;
    }        
    
    /**
     * Getter
     * @return 
     */
    public String getName() {
        return name;
    }
    
    /**
     * Add an Attribute
     * 
     * @param attribute 
     */
    void addAttribute(String attribute) {
        if (attributes.add(attribute)) {
            attributes_order = null; // Forget order if there was a new element
        }
        
        if (!attribute.matches("^[0-9]+$")) {
            is_numeric = false;
            is_totalorder = false;
        }
    }
    
    /**
     * Return the Set of Attributes as Array
     * 
     * @return 
     */
    public String[] getAttributes() {
        if (attributes_order != null) {
            return attributes_order;
        } else {
            attributes_order = attributes.toArray(new String[]{});
            return attributes_order;
        }
    }

    /**
     * Getter
     * 
     * @return 
     */
    public boolean isTotalOrder() {
        return this.is_totalorder;
    }
    
    /**
     * Sorts the Attributes of this header consiring Comparator
     * 
     * @param comp 
     */
    public void provideTotalOder(Comparator<String> comp) {
        
        getAttributes();
        Arrays.sort(attributes_order, comp);
        
        this.is_totalorder = true;
    }

    /**
     * Getter
     * 
     * @return 
     */
    public boolean isNumeric() {
        return this.is_numeric;
    }
}
