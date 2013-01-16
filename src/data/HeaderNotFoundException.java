/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.IOException;

/**
 *
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
class HeaderNotFoundException extends IOException {

    public HeaderNotFoundException(IOException ex) {
        super(ex);
    }
    
}
