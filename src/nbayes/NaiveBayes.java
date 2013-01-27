/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class NaiveBayes {
    
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
    }
    
}
