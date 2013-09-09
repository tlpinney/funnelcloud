package com.tlpinney.funnelcloud;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import crosby.binary.Fileformat.BlobHeader;
import crosby.binary.Fileformat.Blob;
import crosby.binary.Osmformat.HeaderBlock;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
//import org.openstreetmap.osmosis.



import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.commons.codec.binary.Hex;



public class OsmReadWeb {

	public static void main(String[] args) throws IOException, DataFormatException {
		Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
    	String url = args[0];
        //Path outFile = new Path(args[1]);
        HttpClient httpclient = new HttpClient();
        GetMethod httpget = new GetMethod(url); 
        httpclient.executeMethod(httpget);
                 
        BufferedInputStream in = new BufferedInputStream(
                    httpget.getResponseBodyAsStream()); 
                 
        byte [] buf = new byte[4];    
        in.read(buf);
        int bhsize = ByteBuffer.wrap(buf).getInt();
        byte [] blobbuf = new byte[bhsize];
        in.read(blobbuf);
        BlobHeader bh = BlobHeader.parseFrom(blobbuf);
             
        int bsize = bh.getDatasize();   
        buf = new byte[bsize];
        in.read(buf);
        
        Blob blob = null;
        
        // read in the rest of the file and spit out locations of where the end and begin
        SequenceFile.Writer writer = null;
        Path path = new Path(args[1]);
        
        IntWritable key = new IntWritable();
        BytesWritable value = new BytesWritable();
        
        writer = SequenceFile.createWriter(fs, conf, path, 
                key.getClass(), value.getClass(), CompressionType.NONE);
              
        p("Read in next OSMData block");
        
        buf = new byte[4];
        int count = 0;
        int bytesRead;
        
        
        //in.read(buf);
        while ((bytesRead = in.read(buf)) > -1) {   
        
        	if (bytesRead < 4) {
        		// does repeated reads until it gets all the data?
        		bytesRead = in.read(buf, bytesRead, 4-bytesRead);
        	}
        	
        	p(bytesRead);
        	
        	
            bhsize = ByteBuffer.wrap(buf).getInt();
            p(bhsize);
            blobbuf = new byte[bhsize];
            in.read(blobbuf);        
            bh = BlobHeader.parseFrom(blobbuf);      
            p("data type: " + bh.getType());
            p("data size: " + bh.getDatasize());
         
            int dsize = bh.getDatasize();
            byte[] data = new byte[dsize]; 

            int bytesReadTotal = 0;
            //bytesRead = 0;
            while (bytesReadTotal < dsize) {
            	//p(bytesReadTotal);
            	bytesReadTotal = bytesReadTotal + in.read(data, bytesReadTotal, dsize - bytesReadTotal);
            };
            
            //p("bytesReadTotal: " + bytesReadTotal);
            
            
            //blob = Blob.parseFrom(data);
            // read blob into a bytearray and write and ignore it 
            //p("serialized size: " + blob.getSerializedSize());
            
            
            // write out pbf in a sequence file (no compression)
            BytesWritable bw = new BytesWritable(data);           
            key.set(count);
            value.set(bw);
            writer.append(key, value);
            
            //p(in.available());
            //p(Hex.encodeHexString(data));
   

            
          count += 1;
        }
         
        writer.close();
        in.close();
        
        

    }
    
    public static void p(Object o) {
        System.out.println(o);
    }

}
