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

/**
 *
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class StatisticalNaiveBayes extends NaiveBayes {
    
    int[][] counts;
    
    /**
     * Constructs a  Naive Bayes classificator, based on the dataset, with tracking information for a confsuion matrix
     * @param dataset
     */
    public StatisticalNaiveBayes(List<Field> dataset, Header[] header) {        
        super(dataset, header);
        
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
        StatisticalNaiveBayes nbayes = new StatisticalNaiveBayes(learnset, data.getHeaders());
        double rate = nbayes.calcErrorRate(testset);
        System.out.println("Error Rate for this sample: " + rate);        
        
        // Print confusion matrix
        String[] cls = nbayes.getCls();
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
                System.out.print(nbayes.getCounts()[x][y]);
                System.out.print("\t");
            }
            System.out.print("\n");
        }
        
    }
    
}
