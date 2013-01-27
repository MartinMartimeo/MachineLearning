/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knn;

import cardata.CarFieldComparator;
import data.Data;
import data.Field;
import data.Header;
import distance.DataDistance;
import distance.Manhatten;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmeans.KMeans;

/**
 *
 * Extends KNN with a main function running 100 times with a k of 5
 * 
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class KNN100Times extends KNN {

    static final int k = 5;
    static final int iterations = 100;
    
    /**
     * Constructs a KNN classificator, based on the dataset
     * @param datasat
     * @param k (how many nearest neighbor are choosen)
     * @param manhatten 
     */
    public KNN100Times(List<Field> dataset, DataDistance distance) {        
        super(dataset, k, distance);
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
        
        // Run it
        double meanerrorsum = 0;
        for (int iteration = 1; iteration <= iterations; iteration++) {
        
            // Get Training and Test Data
            List<Field>[] sets = data.divideFields(0.66);
            List<Field> learnset = sets[0];
            List<Field> testset = sets[1];

            // Calculate Error Rate
            KNN knn = new KNN100Times(learnset, new Manhatten());
            double rate = knn.calcErrorRate(testset);
            System.out.println("* Error Rate for this sample (iteration=" + iteration + "): " + rate);   
            meanerrorsum += rate;
        }
        
        // Mean 
        System.out.println("Mean Error Rate: " + (meanerrorsum / iterations * 100) + "%");
        
    }
    
    
    
}
