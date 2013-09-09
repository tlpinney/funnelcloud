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



/**
 * funnelcloud
 *
 */
public class App 
{
    
    public static void main( String[] args ) throws IOException
    {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        String url = args[0];
        Path outFile = new Path(args[1]);
        
                        
        //BufferedReader in = new BufferedReader(new FileReader(inFile));
        //BufferedWriter outf = new BufferedWriter(new FileWriter("foobar"));
        //BufferedOutputStream outf2 = new BufferedOutputStream(new FileOutputStream("foobar2"));
        
        HttpClient httpclient = new HttpClient();
        GetMethod httpget = new GetMethod(url);
        
        httpclient.executeMethod(httpget);
                
        BufferedInputStream reader = new BufferedInputStream(
                   httpget.getResponseBodyAsStream()); 
               
         //BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFile));
         //BufferedInputStream in = new BufferedInputStream(reader);
         //reader.read
         
         FSDataOutputStream out = fs.create(outFile);
        
         int bytesRead;
         byte[] buffer = new byte[4096];
       
         while ((bytesRead = reader.read(buffer)) > -1) {
            out.write(buffer, 0, bytesRead);
            //System.out.println(bytesRead);
        }
        //outf2.write(buffer, 0, 4096);
        //System.out.println(bytesRead);
             
        //in.close();
        reader.close();
        out.close();
        
    }
    
   
                
    
    
}
