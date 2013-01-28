package nbayes;

import cardata.CarFieldComparator;
import data.Data;
import data.Field;
import data.Header;
import diverse.CalcHashSet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class NaiveBayes {
    
    
    private final List<Field> dataset;
    
    private double[] probability_cls; // this is for example P(cls) -> [cls]
    private double[][][] probability_attrs; // this is for example P(cls|a) (with a as attribute from h) -> [cls][h][a]
    
    List<String> cls;
    List<Header> header;
    
    /**
     * Construct a NaiveBayes classificator based on the dataset
     * 
     * @param dataset
     * @param header 
     */
    public NaiveBayes(List<Field> dataset, Header[] header) {
        this.dataset = dataset;        
        
        // Init Class Header
        this.cls = new ArrayList<String>();
        this.cls.addAll(Arrays.asList(header[header.length-1].getAttributeValues()));
        this.probability_cls = new double[this.cls.size()];
        this.header = new ArrayList<Header>();
        this.header.addAll(Arrays.asList(header));
        
        CalcHashSet<String> count_cls = new CalcHashSet<String>();
        CalcHashSet<String>[][] count_attrs = new CalcHashSet[this.cls.size()][header.length-1];
        
        // Init Attr Counter
        for (int c = 0; c < this.cls.size(); c++) {
            for (int h = 0; h < header.length-1; h++) {
                count_attrs[c][h] = new CalcHashSet<String>();
            }
        }
        
        // Count
        for (Field field : dataset) {
            count_cls.add(field.getCls());
            for (int h = 0; h < header.length-1; h++) {
                count_attrs[this.cls.indexOf(field.getCls())][h].add(field.getValue(header[h]));
            }
        }
        
        // Probabilities
        this.probability_cls = new double[this.cls.size()];
        this.probability_attrs = new double[this.cls.size()][header.length-1][];        
        for (int c = 0; c < this.cls.size(); c++) {
            this.probability_cls[c] = count_cls.count(this.cls.get(c)) / (double) count_cls.sum();
            for (int h = 0; h < header.length-1; h++) {
                this.probability_attrs[c][h] = new double[header[h].getAttributeValues().length];
                for (int a = 0; a < header[h].getAttributeValues().length; a++) {
                    this.probability_attrs[c][h][a] = count_attrs[c][h].count(header[h].getAttributeValues()[a]) / (double) count_attrs[c][h].sum();
                }                               
            }
        }
        
        
        
        
        
        
        
        
    }
    
    /**
     * Get the posterior for this field
     * 
     * The posterior is calculated as P(cls) * P(attr1|cls) * P(attr2|cls) * ...
     * derived from probability_cls[cls] * probability_attrs[cls][1][attr1] * probability_attrs[cls][2][attr2] * ...
     * 
     * 
     * @param cls
     * @param test
     * @return 
     */
    public double getPosterior(String cls, Field test) {
        
        int c = this.cls.indexOf(cls);
        double p = this.probability_cls[c];
        for (int h = 0; h < this.header.size()-1; h++) {
            for (int a = 0; a < this.header.get(h).getAttributeValues().length; a++) {
                if (this.header.get(h).getAttributeValues()[a].equals(test.getValue(this.header.get(h)))) {
                    p *= this.probability_attrs[c][h][a];
                    break;
                }                
            }
        }
        return p;
        
    }
    
    /**
     * Get the Class from a field
     * 
     * Based on naives bayes the class from a field is the class that has the heighest posterior for
     * this field
     * 
     * @param test
     * @return 
     */
    public String getCls(Field test) {
        
        double max = Double.MIN_VALUE;
        int imax = -1;
        for (int c = 0; c < this.cls.size(); c++) {
            double posterior = this.getPosterior(this.cls.get(c), test);
            if (max < posterior) {
                max = posterior;
                imax = c;
            }
        }
        return this.cls.get(imax);
        
    }
    
    /**
     * Calculates the error Rate for the fields in testset
     * 
     * Therefore each field in testset is classified 
     * and looked for weather its right classificated
     * 
     */
    public double calcErrorRate(List<Field> testset) {
        
        int right = 0;
        for (Field field : testset) {
            if (field.getCls().equals(this.getCls(field))) {
                right++;
            }
        }
        return 1 - ((double) right / testset.size());
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Process Data
        Data data;
        try {
            data = new Data(new File("src/cardata/car.data"));
        } catch (IOException ex) {
            Logger.getLogger(NaiveBayes.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        // Provide an order of the attributes
        CarFieldComparator comp = new CarFieldComparator();
        for (Header header : data.getHeaders()) {
            header.provideTotalOder(comp);
        }
        
        // Get Training and Test Data
        List<Field>[] sets = data.divideFields(0.66);
        List<Field> learnset = sets[0];
        List<Field> testset = sets[1];
        
        // Build Probability
        System.out.println("** Printing Probabilites");
        NaiveBayes nbayes = new NaiveBayes(learnset, data.getHeaders());        
        for (int c = 0; c < nbayes.cls.size(); c++) {
            System.out.println("* " + nbayes.cls.get(c));
            System.out.println("P(" + nbayes.cls.get(c) + ") = " + nbayes.probability_cls[c]);
            for (int h = 0; h < nbayes.header.size()-1; h++) {
                for (int a = 0; a < nbayes.header.get(h).getAttributeValues().length; a++) {
                    System.out.println("P(" + nbayes.header.get(h).getAttributeValues()[a] + "|" + nbayes.cls.get(c) + ") = " + nbayes.probability_attrs[c][h][a]);
                }
            }            
        }
        
        // Calculate Error Rates
        double rate = nbayes.calcErrorRate(testset);
        System.out.println("Error Rate for this sample: " + rate);
        
        
    }
    
}
