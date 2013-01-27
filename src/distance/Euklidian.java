/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distance;

import data.Field;
import data.Header;
import data.IField;
import java.util.Comparator;

/**
 * Calculates the distance between 2 fields using the Euklidian distance measure
 *
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class Euklidian implements DataDistance  {


    @Override
    public double distance(IField a, IField b) {
        Header[] headers = a.getHeaders();
        
        double sum = 0;
        for (Header header : headers) {
            if (header.isTotalOrder()) {
                double ia = a.getNumericValue(header);
                double ib = b.getNumericValue(header);

                sum += (ia - ib) * (ia - ib);
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
        return Math.sqrt(sum);
    }

    @Override
    public int compare(IField a, IField b) {
        return (int) (distance(a, b) * 100); // 2 Nachkommastellen sollten genügen.
    }

    @Override
    public DataDistance createComparator(Field test) {
        return new EuklidianFieldComparator(test);
    }

    private static class EuklidianFieldComparator extends Euklidian {
        private final Field test;

        public EuklidianFieldComparator(Field test) {
            super();
            this.test = test;            
        }

        @Override
        public double distance(IField a, IField b) {
            return super.distance(a, test) - super.distance(b, test);
        }

        @Override
        public int compare(IField a, IField b) {
            return super.compare(a, test) - super.compare(b, test);
        }

        @Override
        public DataDistance createComparator(Field testset) {
            throw new UnsupportedOperationException("Not supported.");
        }
    }
    
}
