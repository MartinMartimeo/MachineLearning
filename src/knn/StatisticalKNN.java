package knn;

import cardata.CarFieldComparator;
import data.Data;
import data.Field;
import data.Header;
import distance.DataDistance;
import distance.Manhatten;
import diverse.CalcHashSet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmeans.KMeans;

/**
 * Extends KNN with a tracking for printing a confusion matrix
 * 
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class StatisticalKNN extends KNN {
    
    List<String> cls;
    int[][] counts;
    
    /**
     * Constructs a KNN classificator, based on the dataset, with tracking information for a confsuion matrix
     * @param datasat
     * @param k (how many nearest neighbor are choosen)
     * @param manhatten 
     */
    public StatisticalKNN(List<Field> dataset, int k, DataDistance distance, Header[] header) {        
        super(dataset, k, distance);
        
        this.cls = new ArrayList<String>();
        this.cls.addAll(Arrays.asList(header[header.length-1].getAttributeValues()));
        this.counts = new int[this.cls.size()][this.cls.size()];
    }

    /**
     * Overwritten for tracking the right/wrong count
     * 
     * @param test
     * @return 
     */
    @Override
    public String getCls(Field test) {
        
        String rtn = super.getCls(test);        
        this.counts[this.cls.indexOf(rtn)][this.cls.indexOf(test.getCls())]++;                
        return rtn;
        
    }

    /**
     * Getter
     * @return 
     */
    String[] getCls() {
        return this.cls.toArray(new String[]{});
    }

    /**
     * Getter
     * @return 
     */
    int[][] getCounts() {
        return this.counts;
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
            Logger.getLogger(KMeans.class.getName()).log(Level.SEVERE, null, ex);
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
        
        // Calculate Error Rate
        StatisticalKNN knn = new StatisticalKNN(learnset, 5, new Manhatten(), data.getHeaders());
        double rate = knn.calcErrorRate(testset);
        System.out.println("Error Rate for this sample (using k=5): " + rate);        
        
        // Print confusion matrix
        String[] cls = knn.getCls();
        System.out.println("* Confusion matrix:");
        System.out.print(" ");
        System.out.print("\t");        
        for (String attribute : cls) {
            System.out.print(attribute);
            System.out.print("\t");
        }
        System.out.print("\n");
        for (int x = 0; x < cls.length; x++) {
            System.out.print(cls[x]);
            System.out.print("\t");
            for (int y = 0; y < cls.length; y++) {
                int count = 0;                                            
                System.out.print(knn.getCounts()[x][y]);
                System.out.print("\t");
            }
            System.out.print("\n");
        }
        
    }
    
    
    
    
}
