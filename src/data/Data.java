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
     * Getter
     * 
     * @return 
     */
    public Header[] getHeaders() {
        return this.headers;
    }
    
    
    
}
