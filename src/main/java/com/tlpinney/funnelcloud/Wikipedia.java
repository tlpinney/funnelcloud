package com.tlpinney.funnelcloud;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;

public class Wikipedia {

	 public static void main( String[] args ) throws IOException
	    {
		 
		 	//args[0] = "/home/vagrant/funnelcloud/enwiki-20130805-pages-articles-multistreaindex.txt";
		 
		 	//String idxfile = "/home/vagrant/funnelcloud/enwiki-20130805-pages-articles-multistream-index.txt";
		 	
		 	// read in the wikipedia index file  
		 	BufferedReader in = new BufferedReader(new FileReader(args[0]));
		 	
		 	// create in an array of byte locations for bzip2 streams
		 	
		 	String line;
		 	Stack<String> aloc = new Stack<String>();
		 	
		 	// read in the first location
		 	String loc = in.readLine().split(":")[0];
		 	//p(loc);
		 	//System.exit(0);
		 	
		 	while ((line = in.readLine()) != null ) {
		 		String sa = line.split(":")[0];
		 		if (!sa.contentEquals(loc)) {
		 			//p(loc);
		 			aloc.push(loc);
		 			loc = sa;
		 		}
		 	}
		 	
		 	in.close();
		 	
		 	//p(aloc);
		 	//System.exit(0);
		 	
		 	p("Processed bz2 index, downloading bz2");
		 	
		 
	        Configuration conf = new Configuration();
	        FileSystem fs = FileSystem.get(conf);
	        String url = args[1];
	        
	                        	        
	        HttpClient httpclient = new HttpClient();
	        GetMethod httpget = new GetMethod(url);
	        
	        httpclient.executeMethod(httpget);
	                
	        BufferedInputStream reader = new BufferedInputStream(
	                   httpget.getResponseBodyAsStream()); 
	               
	         
	         //FSDataOutputStream out = fs.create(outFile);
	        
	         
	         SequenceFile.Writer writer = null;
	         Path path = new Path(args[2]);
	         
	         IntWritable key = new IntWritable();
	         BytesWritable value = new BytesWritable();
	         
	         writer = SequenceFile.createWriter(fs, conf, path, 
	                 key.getClass(), value.getClass(), CompressionType.NONE);
	         
	         // read the first part of the array 
	         
	         long byteloc = Integer.parseInt(aloc.remove(0));
	         Long tmp2 = byteloc;
	         int bytelen = tmp2.intValue(); 
	         long tmp = 0;
	        
	         int count = 0;
	         int bytesRead = 0;
	         byte[] buffer = new byte[bytelen]; 
	       
	        
	         while ((bytesRead = reader.read(buffer)) > -1) {
	        	 
	        	 //p("bytesRead: " + bytesRead);
	        	 //System.exit(0);
	        	 
	        	if (bytesRead < bytelen) {
	        		while (bytesRead < bytelen) {
	        			//p("bytesRead in loop: " + bytesRead);
	        			bytesRead = bytesRead + reader.read(buffer, bytesRead, bytelen-bytesRead);
	            	}
	            }
	        	 
	            BytesWritable bw = new BytesWritable(buffer);           
	            key.set(count);
	            value.set(bw);
	            writer.append(key, value);
	        	         	 
	        
	            if (aloc.size() < 1) {
	            	// we are at the end
	            	break;
	            }
	            
	        	tmp = byteloc;
	        	byteloc = Long.parseLong(aloc.remove(0));
	        	//p("byteloc: " + byteloc);
	        	//p("tmp: " + tmp);
	        	tmp2 = byteloc - tmp;
	        	//p("tmp2: " + tmp2);
	        	bytelen = tmp2.intValue();
	        	 
	        	//p("bytelen: " + bytelen);
	        	 
	        	buffer = new byte[bytelen];
	        	      	 
		        count += 1;    
		       
		        if (count % 1000 == 0 ) {
		        	  p("Processed Blob: " + count);
		          }
		        
		        }
	        
	        
	         
	         
	        reader.close();
	        writer.close();
	    }


	 public static int readFully(byte[] a, BufferedInputStream s, int len) throws IOException {
		 int bytesReadTotal = 0;
		 int bytesRead = 0;
         while (bytesReadTotal < len) {
        	p("bytesReadTotal: " + bytesReadTotal);
         	bytesRead = s.read(a, bytesReadTotal, len - bytesReadTotal);
         	p("bytesRead: " + bytesRead);
         	if (bytesRead == -1 ) {
         		return -1 ;
         	} else {
         		bytesReadTotal = bytesReadTotal + bytesRead;
         	}
         }
         return bytesReadTotal;
         
	 }
     public static void p(Object o) {
         System.out.println(o);
     }

}
	 
