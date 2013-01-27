package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A data abstraction level parsing a csv file
 * 
 * @author Severin Orth <severin.orth@st.ovgu.de>
 */
public class Data {
 
    List<Field> fields = new ArrayList<Field>();
    private Header[] headers;
    
    /**
     * Reads our data set from data_file
     * 
     * @param data_file 
     */
    public Data(File data_file) throws FileNotFoundException, HeaderNotFoundException, IOException {
        
        // Read Content
        BufferedReader data_reader = new BufferedReader(new FileReader(data_file));
        
        // Header
        String[] data_header;        
        try {
            String data_header_line = data_reader.readLine();
            data_header = data_header_line.split(",");            
        } catch (IOException ex) {
            throw new HeaderNotFoundException(ex);
        }        
        this.headers = new Header[data_header.length];
        for (int i = 0; i < data_header.length; i++) {
            headers[i] = new Header(data_header[i]);
        }
        
        // Data
        String data_line;
        while ((data_line = data_reader.readLine()) != null) {
            Field field = new Field(headers, data_line.split(","));
            fields.add(field);    
        }
    }
    
    /**
     * Divde into ps.length+1 subsets with distribution given by ps
     * 
     * The last subset will always contain all remaining fields
     * 
     * For example the call divideFields(0.33) will return 2 subsets, one with 
     * 33% of all fields, the other with all remaining nodes
     * 
     * if the sum of ps is >= 1 the last subsets will be 0-length
     * 
     */
    public List<Field>[] divideFields(double... ps) {
        
        List<Field>[] rtn = new ArrayList[ps.length+1];
        List<Field> all = new ArrayList(); all.addAll(this.fields); // Create a modifable clone
        Random r = new Random();
        for (int pi = 0; pi < ps.length; pi++) {
            rtn[pi] = new ArrayList<Field>();
            for (int i = 0; i < this.fields.size() * ps[pi]; i++) {
                int index = r.nextInt(all.size());
                Field field = all.remove(index);
                rtn[pi].add(field);
            }
        }
        rtn[ps.length] = new ArrayList<Field>(); rtn[ps.length].addAll(all);                
        return rtn;        
    }

    /**
     * Get k random unique fields from dataset
     * 
     * @param k
     * @return 
     */
    public Field[] getRandomFields(int k) {
        
        Field[] rtn = new Field[k];
        Random r = new Random();
        
        w: while (k > 0) {
            
            // Get a random field
            int index = r.nextInt(this.fields.size());
            Field field = this.fields.get(index);
            
            // Check on existant
            for (int i = k; i < rtn.length; i++) {
                if (rtn[i] == field) {
                    continue w;
                }
            }
            
            // Set it
            rtn[--k] = field;                
        }
        
        return rtn;
        
    }

    /**
     * Get all fields
     * 
     * @return 
     */
    public List<Field> getFields() {
        return this.fields;
    }

    /**
     * Get size
     * 
     * @return 
     */
    public int size() {
        return this.fields.size();
    }

    /**
     * Getter
     * 
     * @return 
     */
    public Header[] getHeaders() {
        return this.headers;
    }
    
    
    
}
