package cardata;

import data.Field;
import java.util.Comparator;

/**
 *
 * Provides an order for the data fields in car.data
 * 
 * It basicly translates all non numeric strings a number
 * So "low"/"med"/"high" will be translated to 0/2/4
 * 
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class CarFieldComparator implements Comparator<String> {

    @Override
    public int compare(String a, String b) {
       
        int ia = translate(a);
        int ib = translate(b);
        
        return ia - ib;
    }

    private int translate(String s) {
        
        // An Integer?
        if (s.matches("^[0-9]+$")) {
            return Integer.parseInt(s);
        }
        
        // "5more"
        if (s.equals("5more")) {
            return 5;
        }
        
        // "more"
        if (s.equals("more")) {
            return 6;
        }
        
        // "small" / "low" / "unacc"
        if (s.equals("small") || s.equals("low") || s.equals("unacc")) {
            return 0;
        }
        
        // "med" / "acc"
        if (s.equals("med") || s.equals("acc")) {
            return 2;
        }
        
        // "big" / "high" / "good"
        if (s.equals("big") || s.equals("high") || s.equals("good")) {
            return 4;
        }
        
        // "vhigh" / "vgood"
        if (s.equals("vhigh") || s.equals("vgood")) {
            return 6;
        }
        
        throw new UnsupportedOperationException("String is not supported by translate method: " + s);        
    }
    
}
