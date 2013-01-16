package kmeans;

import cardata.CarFieldComparator;
import data.Data;
import data.Field;
import data.Header;
import distance.DataDistance;
import distance.Euklidian;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class KMeans {

    Cluster[] cluster;    
    Data data;
    DataDistance distance;
    int k;
    
    /**
     * Constructor, initalisates the clusters
     * 
     * 
     */
    public KMeans(Data data, DataDistance distance) {
               
       this.data = data;
       this.distance = distance;
       
    }
    
    /**
     * Generates k cluster choosen from random fields
     * 
     */
    public void initCluster(int k) {
        
        this.k = k;
        
        // Generate Cluster
        this.cluster = new Cluster[this.k];
        Field[] fields = this.data.getRandomFields(k);        
        for (int i = 0; i < k; i++) {
            this.cluster[i] = new Cluster(fields[i]);
        }
        
    }
    
    /**
     * Distinct for all fields its cluster
     * 
     * @return 
     */
    List<Field>[] getClusterFields() {
        
        List<Field>[] rtn = new ArrayList[this.cluster.length];  

        // Generate the ArrayList
        for (int c = 0; c < this.cluster.length; c++) {
            rtn[c] = new ArrayList<Field>();
        }

        // Decide for all fields the cluster
        for (Field field : this.data.getFields()) {

            // Calculate to which points this cluster belongs
            int field_cluster = 0;
            double min_distance = this.distance.distance(this.cluster[0], field);                    
            for (int c = 1; c < this.cluster.length; c++) {
                if (this.distance.distance(this.cluster[c], field) < min_distance) {
                    field_cluster = c;
                }                
            }

            // Set it 
            rtn[field_cluster].add(field);
        }
        
        return rtn;
    }

    /**
     * Getter
     * 
     * @return 
     */
    private Cluster[] getCluster() {
        return this.cluster;
    }
    
    /**
     * Cluster the data
     */
    public void cluster() {
        
        
        boolean again = false;
        
        // For a second while break condition
        List<Double> hashes = new ArrayList<Double>();
                
        w: do {
            
            List<Field>[] cluster_fields = getClusterFields();

            // Update cluster
            double hash = 0;
            for (int c = 0; c < this.cluster.length; c++) {
                if (cluster[c].updateCenteroid(cluster_fields[c])) {
                    again = true;
                }
                hash += cluster[c].hashCode();
            }
            
            // We are calculation the hash_sum of this result
            // So we know if we are circling in a few results
            // because we have a jumping local minima
            for (Double h : hashes) {
                if (h.doubleValue() == hash) {
                    Logger.getLogger(KMeans.class.getName()).log(Level.WARNING, "Break out of while loop because of circling in local minima");
                    break w;
                }
                    
            }
            hashes.add(hash);
        } while (again);        
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Data data;
        try {
            data = new Data(new File("src/cardata/car.data"));
        } catch (IOException ex) {
            Logger.getLogger(KMeans.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        CarFieldComparator comp = new CarFieldComparator();
        for (Header header : data.getHeaders()) {
            header.provideTotalOder(comp);
        }
        
        KMeans k = new KMeans(data, new Euklidian());
        k.initCluster(4);
        k.cluster();
        
        
        // Print Result
        Cluster[] cluster = k.getCluster();
        int sum_error_count = 0;
        int sum_field_count = 0;
        for (int c = 0; c < cluster.length; c++) {
            System.out.println("*** Cluster #" + c);
            System.out.println("Is clustered as cls: " + cluster[c].getCls());
            System.out.println("Has fields: " + cluster[c].getFieldCount());
            System.out.println("Error Rate (absolute): " + (cluster[c].getErrorRate()*100) + "% (" + cluster[c].getErrorCount() + ")");
            sum_error_count += cluster[c].getErrorCount();    
            sum_field_count += cluster[c].getFieldCount();    
        }
        System.out.println("* " + sum_error_count + " fields are wrong clustered");
        System.out.println("* " + sum_field_count + " fields had been clustered");
        System.out.println("* Total error rate: " + (100.0 * sum_error_count / sum_field_count) + "%");
        
        // Print confusion matrix
        System.out.println("* Confusion matrix:");
        List<Field>[] cfields = k.getClusterFields();
        Header cls = data.getHeaders()[data.getHeaders().length-1];
        System.out.print(" ");
        System.out.print("\t");
        for (String attribute : cls.getAttributes()) {
            System.out.print(attribute);
            System.out.print("\t");
        }
        System.out.print("\n");
        for (String x : cls.getAttributes()) {
            System.out.print(x);
            System.out.print("\t");
            for (String y : cls.getAttributes()) {
                int count = 0;                
                for (int c = 0; c < cluster.length; c++) {
                    if (cluster[c].getCls().equals(y)) {
                        for (Field field : cfields[c]) {
                            if (x.equals(field.getCls())) {
                                count++;
                            }
                        }        
                    }
                }                
                System.out.print(count);
                System.out.print("\t");
            }
            System.out.print("\n");
        }
    }
}
