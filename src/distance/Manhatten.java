/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distance;

import data.Field;
import data.Header;
import data.IField;

/**
 *
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class Manhatten implements DataDistance {

    @Override
    public double distance(IField a, IField b) {
        Header[] headers = a.getHeaders();
        
        double sum = 0;
        for (Header header : headers) {
            if (header.isTotalOrder()) {
                double ia = a.getNumericValue(header);
                double ib = b.getNumericValue(header);

                sum += Math.abs(ia - ib);
            } else { // Just equal or not equal
                String sa = a.getValue(header);
                String sb = b.getValue(header);
                
                if (sa.equals(sb)) {
                    sum += 1;
                } else {
                    sum += 0;
                }                
            }
        }
        return sum;
    }

    @Override
    public int compare(IField a, IField b) {
        return (int) (distance(a, b));
    }    
}