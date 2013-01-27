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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmeans.KMeans;

/**
 *
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class KNN {
    private DataDistance distance;
    private int k;
    private Field[] dataset = new Field[0];

    /**
     * Constructs a KNN classificator, based on the dataset
     * @param datasat
     * @param k (how many nearest neighbor are choosen)
     * @param manhatten 
     */
    public KNN(List<Field> dataset, int k, DataDistance distance) {        
        this.dataset = dataset.toArray(this.dataset);
        this.k = k;
        this.distance = distance;
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
     * Get the class based on the KNN algorithm for test
     * 
     * It will sort the array by a Comparator with the distance to test
     * and then do a majority vote on the first k elements.
     * 
     * @param test
     * @return 
     */
    public String getCls(Field test) {
        
        Comparator c = this.distance.createComparator(test);
        Arrays.sort(this.dataset, c);
        CalcHashSet<String> s = new CalcHashSet<String>();
        for (int i = 0; i < this.k; i++) {
            s.add(this.dataset[i].getCls());
        }
        return s.maxValue();
        
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
        
        // Calculate Error Rates
        for (int i = 1; i <= 5; i++) {
            KNN knn = new KNN(learnset, i, new Manhatten());
            double rate = knn.calcErrorRate(testset);
            System.out.println("Error Rate for this sample (using k=" + i + "): " + rate);
        }
        
    }
    
}
