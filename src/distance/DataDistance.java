/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package distance;

import data.Field;
import data.IField;
import java.util.Comparator;

/**
 *
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public interface DataDistance extends Comparator<IField> {
    
    public double distance(IField a, IField b);

    public DataDistance createComparator(Field testset);
    
}
