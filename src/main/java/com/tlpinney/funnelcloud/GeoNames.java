package com.tlpinney.funnelcloud;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.SequenceFileAsBinaryInputFormat;
import java.util.zip.ZipInputStream;


/**
 * funnelcloud
 *
 */
public class GeoNames 
{
    
    public static void main( String[] args ) throws IOException
    {
    	
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        String url = args[0];
        Path outFile = new Path(args[1]);
        
                             
        HttpClient httpclient = new HttpClient();
        GetMethod httpget = new GetMethod(url);
        
        httpclient.executeMethod(httpget);
                
        BufferedInputStream reader = new BufferedInputStream(
                   httpget.getResponseBodyAsStream()); 
               
        
        // open the hdfs file for writing
        FSDataOutputStream out = fs.create(outFile);
        
        
        int bytesRead;
        byte[] buffer = new byte[4096];
        //ZipInputStream zis = new ZipInputStream();
        
       
        while ((bytesRead = reader.read(buffer)) > -1) {
            out.write(buffer, 0, bytesRead);
            
            //System.out.println(bytesRead);
        }

        reader.close();
        out.close();
        
    }
    
   
                
    
    
}
