package nbayes;

import cardata.CarFieldComparator;
import data.Data;
import data.Field;
import data.Header;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmeans.KMeans;

/** *
 * Extends Naive Bayes with a main function running 100 times
 * 
 * for this dataset the mean error rate is somewhere about 14,9%
 * one run provided eactly a mean error rate of: 14.896081771720619%
 * 
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class NaiveBayes100Times extends NaiveBayes {
    static final int iterations = 100;
    
    /**
     * Construct a NaiveBayes classificator based on the dataset
     * 
     * @param dataset
     * @param header 
     */
    public NaiveBayes100Times(List<Field> dataset, Header[] header) {
        super(dataset, header);
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
            NaiveBayes nbayes = new NaiveBayes(learnset, data.getHeaders());
            double rate = nbayes.calcErrorRate(testset);
            System.out.println("* Error Rate for this sample (iteration=" + iteration + "): " + rate);   
            meanerrorsum += rate;
        }
        
        // Mean 
        System.out.println("Mean Error Rate: " + (meanerrorsum / iterations * 100) + "%");
        
    }
}
