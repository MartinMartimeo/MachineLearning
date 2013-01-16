/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans;

import data.Field;
import data.Header;
import data.IField;
import diverse.CalcHashSet;
import java.util.HashMap;
import java.util.List;

/**
 * Represantion of an KMeans-Cluster.
 * 
 * The Attribute centeroid saves the center location of this cluster
 * with updateCenteroid this can be updated.
 * 
 * cls is the class of the cluster, distincted on updateCenteroid by majority vote on the classes in
 * the cluster.
 * 
 * error_count/... are also updated on updateCenteroid primary out of interest for the result print
 * 
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class Cluster implements IField {
    
    Header[] headers;
    HashMap<Header, Double> centeroid;
    
    
    String cls;
    int error_count;
    double error_rate;
    int field_count;
    
    public Cluster(Field field) {                        
        
        headers = field.getHeaders();
        cls = field.getCls();
        centeroid = new HashMap<Header, Double>();                
        
        for (Header header : headers) {
            centeroid.put(header, field.getNumericValue(header));
        }
        
    }

    @Override
    public Header[] getHeaders() {
        return this.headers;
    }

    @Override
    public double getNumericValue(Header header) {
        return centeroid.get(header);
    }

    @Override
    public String getValue(Header header) {
        throw new UnsupportedOperationException("Not Supported.");
    }

    @Override
    public int hashCode() {
        int hash = 5;
        for (Double d : this.centeroid.values()) {
            hash += d.hashCode();
        }
        return hash;
    }
    
    /**
     * Updates the centeroids based on fields using the average
     * 
     * @param fields 
     * @return Needed the centeroid to be updated?
     * 
     */
    public boolean updateCenteroid(List<Field> fields) {
        
        boolean rtn = false;
        
        // Update the centeroids
        for (Header header : headers) {
            double calc = 0;
            
            for (Field field : fields) {
                calc += field.getNumericValue(header);
            }
            
            calc /= fields.size();
            if (this.centeroid.get(header) != calc) {
                this.centeroid.put(header, calc);
                rtn = true;
            }
            
        }
        
        // Calculate the new class
        CalcHashSet<String> calc = new CalcHashSet<String>();
        for (Field field : fields) {
            calc.add(field.getCls());            
        }
        this.cls = calc.maxValue();
        
        if (fields.isEmpty()) {
            this.error_count = 0;
            this.field_count = 0;
            this.error_rate = 1;
            return rtn;
        }
        this.field_count = fields.size();
        
        // Calculate error rate
        this.error_count = 0;
        for (Field field : fields) {
            if (!field.getCls().equals(this.cls)) {
                this.error_count++;
            }            
        }
        this.error_rate = ((double) this.error_count) / this.field_count;
        
        return rtn;
        
    }

    /**
     * Get the class
     * 
     * Computed by majority vote on all fields
     * 
     * @return 
     */
    @Override
    public String getCls() {
        return this.cls;
    }

    /**
     * Getter
     * 
     * @return 
     */
    double getErrorRate() {
        return this.error_rate;
    }

    /**
     * Getter
     * 
     * @return 
     */
    int getErrorCount() {
        return this.error_count;
    }

    /**
     * Getter
     * 
     * @return 
     */
    int getFieldCount() {
        return this.field_count;
    }
    
}
 