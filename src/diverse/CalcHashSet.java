package diverse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

/**
 * This is a HashSet that counts how often T has been added
 * and provides a method to get the T that had been added most
 * 
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class CalcHashSet<T> extends HashSet<T> {

    HashMap<T, Integer> counts = new HashMap<T, Integer>();
    
    public CalcHashSet() {
        super();
    }

    @Override
    public boolean add(T e) {
        
        if (super.add(e)) {
            this.counts.put(e, 0);
            return true;
        } else {
            this.counts.put(e, this.counts.get(e) + 1);
            return false;
        }
        
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean remove(Object o) {
        
        int i = count((T) o);
        if (i <= 1) {
            super.remove(o);
            this.counts.remove((T) o);
        } else {
            this.counts.put((T) o, i-1);
        }
        
        return i > 0;        
    }

    /**
     * Remove all counts of o
     * 
     * @param o
     * @return 
     */
    public boolean removeAll(T o) {
        
        this.counts.remove(o);
        return super.remove(o);
        
    }
    
    /**
     * How often T exist
     * @param o
     * @return 
     */
    public int count(T o) {        
        Integer i = this.counts.get(o);
        if (i == null) {
            return 0;
        }
        return i.intValue();
    }

    /**
     * Return the element with the greates count
     * 
     * @return 
     */
    public T maxValue() {
        
        int max = Integer.MIN_VALUE;
        T rtn = null;
        for (Entry<T, Integer> entry : this.counts.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                rtn = entry.getKey();
            }
        }
        return rtn;       
        
    }
    
    
    
    
    
    
    
    
}
