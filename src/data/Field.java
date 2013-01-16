/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.HashMap;

/**
 *
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class Field extends HashMap<String, String> implements IField {

    private Header[] header;
    private Header cls;
    
    /**
     * Construncts a field
     * 
     * The last header is used as class of this field
     * 
     * @param header
     * @param data 
     */
    Field(Header[] header, String[] data) {
        super();
        
        this.header = new Header[header.length-1];
        
        for (int i = 0; i < header.length; i++) {
            this.put(header[i].getName(), data[i]);            
            header[i].addAttributeValue(data[i]);
            
            if (i == header.length-1) {
                this.cls = header[i];
            } else {
                this.header[i] = header[i];
            }
        }
    }

    /**
     * Getter
     * 
     * @return 
     */
    @Override
    public Header[] getHeaders() {
        return header;
    }

    /**
     * Get a value as integer
     * 
     * If the attributes are integers this returns the value as integer
     * If the attribute has a total order, it returns the index of the total order
     * Otherwise it fails with runtime exception
     * 
     * @param header
     * @return 
     */
    @Override
    public double getNumericValue(Header header) {
        
        String value = getValue(header);
        if (header.isNumeric()) {
            return Integer.valueOf(value);
        } else if (header.isTotalOrder()) {
            String[] attributes = header.getAttributeValues();
            for (int i = 0; i < attributes.length; i++) {
                if (attributes[i].equals(value)) {
                    return i;
                }
            }
            return -1;
        }     
        throw new RuntimeException("Tried to receive value of a non total order header");
        
    }

    /**
     * Get a value
     * 
     * @param header
     * @return 
     */
    @Override
    public String getValue(Header header) {
        return this.get(header.getName());
    }

    /**
     * Get the class
     * 
     * @return 
     */
    @Override
    public String getCls() {
        return this.get(cls.getName());
    }

    /**
     * print
     * 
     * @return 
     */
    @Override
    public String toString() {
        String rtn = "";
        for (Header h : this.header) {
            if (!rtn.isEmpty()) {
                rtn += "|";
            }
            rtn += this.get(h.getName());
        }
        return "<" + rtn + "> : " + getCls();
    }
    
    
    
}
