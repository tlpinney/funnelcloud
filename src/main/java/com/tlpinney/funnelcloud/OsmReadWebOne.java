package com.tlpinney.funnelcloud;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

//import crosby.binary.Fileformat.BlobHeader;
//import crosby.binary.Fileformat.Blob;
//import crosby.binary.Osmformat.HeaderBlock;
import org.openstreetmap.osmosis.osmbinary.Fileformat.BlobHeader;
import org.openstreetmap.osmosis.osmbinary.Fileformat.Blob;
import org.openstreetmap.osmosis.osmbinary.Osmformat.HeaderBlock;

import java.util.zip.DataFormatException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.commons.codec.binary.Hex;



public class OsmReadWebOne {

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
        
       // Blob blob = null;
        
        // read in the rest of the file and spit out locations of where the end and begin
        SequenceFile.Writer writer = null;
        Path path = new Path(args[1]);
        
        BytesWritable key = new BytesWritable();
        BytesWritable value = new BytesWritable();
        
        writer = SequenceFile.createWriter(fs, conf, path, 
                key.getClass(), value.getClass(), CompressionType.NONE);
              
        p("Read in next OSMData blobs");
        
        buf = new byte[4];
        int count = 0;
        int bytesRead;
        
        
        //in.read(buf);
        while ((bytesRead = in.read(buf)) > -1) {   
        
        	int bytesReadTotal = 0;
        	
        	
        	
        	if (bytesRead < 4) {
        		while (bytesRead < 4) {
        			bytesRead = bytesRead + in.read(buf, bytesRead, 4-bytesRead);
        		}
        	}
        	
        	//p(bytesRead);
        	
        	
            bhsize = ByteBuffer.wrap(buf).getInt();
            //p(bhsize);
            blobbuf = new byte[bhsize];
            
            bytesReadTotal = 0;
            while (bytesReadTotal < bhsize) {
            	bytesReadTotal = bytesReadTotal + in.read(blobbuf, bytesReadTotal, bhsize - bytesReadTotal);   
            }
            
            bh = BlobHeader.parseFrom(blobbuf);      
            //p("data type: " + bh.getType());
            //p("data size: " + bh.getDatasize());
         
            int dsize = bh.getDatasize();
            byte[] data = new byte[dsize]; 

            //p("dsize: " + dsize);
            bytesReadTotal = 0;
            while (bytesReadTotal < dsize) {
            	bytesReadTotal = bytesReadTotal + in.read(data, bytesReadTotal, dsize - bytesReadTotal);
            	//p("bytesReadTotal: " + bytesReadTotal);
            };
            
            // process the blob and get from stats from it 
            
            Blob blob = Blob.parseFrom(data);
            
            //p(blob)
            
            // compare stats to what is read from the sequence file 
            //p("raw size: " + blob.getRawSize());
            
            
            // write out pbf in a sequence file (no compression)
        
            BytesWritable bw = new BytesWritable(data); 
            BytesWritable bwkey = new BytesWritable(blobbuf); 
            p("value data length: " + data.length);
            p("value byteswritable length:" + bw.getLength());
            p("key data length: " + blobbuf.length);
            p("key byteswritable length: "  + bwkey.getLength());
            
            
            p(Hex.encodeHexString(blobbuf));
            p("------------------------------------------------");
            p(Hex.encodeHexString(data));
            
            
            key.set(bwkey);
            value.set(bw);
            writer.append(key, value);
            
              
          count += 1;
          
          if (count % 1000 == 0 ) {
        	  p("Processed Blob: " + count);
          }
          
          break;
        }
         
        writer.close();
        in.close();
        
        

    }
    
    public static void p(Object o) {
        System.out.println(o);
    }

}
