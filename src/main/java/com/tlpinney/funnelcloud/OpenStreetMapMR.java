package com.tlpinney.funnelcloud;

import org.apache.commons.codec.binary.Hex;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileAsBinaryInputFormat;
import org.openstreetmap.osmosis.osmbinary.Fileformat.Blob;
import org.openstreetmap.osmosis.osmbinary.Osmformat.PrimitiveBlock;

import com.tlpinney.funnelcloud.osm.FclSink;
import com.tlpinney.funnelcloud.osm.FclSink2;

import crosby.binary.osmosis.OsmosisBinaryParser;

import java.io.IOException;
import java.util.Iterator;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;


public class OpenStreetMapMR{

  public static class OSMMapper extends MapReduceBase
      implements Mapper<BytesWritable, BytesWritable, Text, Text> {

//    private final static Text key = new LongWritable();
    // private final static LongWritable count = new LongWritable();

    public void map(BytesWritable header, BytesWritable blobval,
        OutputCollector<Text, Text> output, Reporter reporter)
        throws IOException {
      
    	
    	 byte[] barr = new byte[blobval.getLength()];    
         barr = blobval.copyBytes();
         
         
         //System.out.println(Hex.encodeHexString(barr));
         
         Blob blob = Blob.parseFrom(barr);
          
         //p(blob.getZlibData());
        	
         Inflater inflater = new Inflater();
         inflater.setInput(blob.getZlibData().toByteArray());
         byte[] blobd = new byte[blob.getRawSize()];
         
         try {
			inflater.inflate(blobd);
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
        	
         // load blobd into a file 
         
         System.out.println("---------------------- Was able to inflate");
         PrimitiveBlock pb = PrimitiveBlock.parseFrom(blobd);
         
         OsmosisBinaryParser obp = new OsmosisBinaryParser();
         
         
         FclSink2 fcls = new FclSink2();
         
         fcls.initialize(null);
         obp.setSink(fcls);
         obp.parse(pb);
         // get the value that was create 
         
         fcls.complete();
         fcls.release();
         
         for (String k: fcls.keyvals.keySet()) {
        	String v = fcls.keyvals.get(k);
        	output.collect(new Text(k), new Text(v));
        	System.out.print(k);
         }
      // process the blob

      // count the keys and 
      // output the key count 
     
    	//  output.collect(key, count);
      }

    }
  



  public static class OSMReducer extends MapReduceBase
      implements Reducer<Text, Text, Text, Text> {

    public void reduce(Text key, Iterator<Text> values,
        OutputCollector<Text, Text> output, Reporter reporter)
        throws IOException {

    	// add up any extra keys then add up the key count
    	
    	while (values.hasNext()) {
        	output.collect(key, values.next());	
    	}
    	
    	
    }
  }


  /**
   * The actual main() method for our program; this is the
   * "driver" for the MapReduce job.
   */
  public static void main(String[] args) {
    JobClient jc = new JobClient();
    JobConf conf = new JobConf(OpenStreetMapMR.class);

    conf.setJobName("OpenStreetMapMR");

    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Text.class);

    conf.setInputFormat(SequenceFileAsBinaryInputFormat.class);
    
    FileInputFormat.addInputPath(conf, new Path(args[1]));
    FileOutputFormat.setOutputPath(conf, new Path(args[2]));

    conf.setMapperClass(OSMMapper.class);
    conf.setReducerClass(OSMReducer.class);
    conf.set("mapred.job.tracker", "local");

    jc.setConf(conf);

    try {
      JobClient.runJob(conf);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}