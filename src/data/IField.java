/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public interface IField {

    /**
     * Getter
     *
     * @return
     */
    Header[] getHeaders();

    /**
     * Get a value as integer
     *
     * @param header
     * @return
     */
    double getNumericValue(Header header);

    /**
     * Get a value
     *
     * @param header
     * @return
     */
    String getValue(Header header);
    
    
    /**
     * Get the class
     * 
     * @return 
     */
    String getCls();
    
}
