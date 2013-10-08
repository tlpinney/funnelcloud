package com.tlpinney.funnelcloud;

import org.apache.commons.codec.binary.Hex;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileAsBinaryInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.openstreetmap.osmosis.osmbinary.Fileformat.Blob;
import org.openstreetmap.osmosis.osmbinary.Fileformat.BlobHeader;
import org.openstreetmap.osmosis.osmbinary.Osmformat.PrimitiveBlock;

import com.tlpinney.funnelcloud.osm.FclSink;
import com.tlpinney.funnelcloud.osm.FclSink2;

import crosby.binary.osmosis.OsmosisBinaryParser;

import org.apache.hadoop.conf.Configured;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;


public class OSMMRTest extends Configured implements Tool {

  public static class OSMMapper <K, V> extends Mapper<BytesWritable, BytesWritable, Text, Text>{


	
    public void map(BytesWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
      
    	
      	byte[] barr = value.copyBytes();
    	byte[] barr2 = Arrays.copyOfRange(barr, 4, barr.length);
    	
         Blob blob = Blob.parseFrom(barr2);
          
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
         
         // System.out.println("---------------------- Was able to inflate");
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
        	

        	
        	context.write(new Text(k), new Text(v));
        	//System.out.print(k);
         }
  
      }

    }
  



  public static class OSMReducer <K, V> extends Reducer<Text, Text, NullWritable, Text>{ 
	  
	  private MultipleOutputs<NullWritable, Text> mos;
	
	  public void setup(Context context) {	
		  mos = new MultipleOutputs<NullWritable, Text>(context);
	  }
	  protected void cleanup(Context context)
			    throws IOException, InterruptedException {
			 
			   mos.close();
	   }
	  
    public void reduce(Text key, Iterable<Text> values,
            Context context) throws IOException, InterruptedException {
          
    	// add up any extra keys then add up the key count
    	
    	for (Text t : values) {
      	
    	    if (key.toString().startsWith("current_nodes:")) {
    	       mos.write("currentnodestsv", NullWritable.get(), t);	
    	    } else if (key.toString().startsWith("current_node_tags:")) {
    	       mos.write("currentnodetagstsv", NullWritable.get(), t );
    	    } else if (key.toString().startsWith("current_ways:")) {
    	       mos.write("currentwaystsv", NullWritable.get(), t);
    	    } else if (key.toString().startsWith("current_way_tags:")) {
    	       mos.write("currentwaytagstsv", NullWritable.get(), t);
    	    } else if (key.toString().startsWith("current_way_nodes:")) {
    	       mos.write("currentwaynodestsv", NullWritable.get(), t);
    	    } else if (key.toString().startsWith("current_relations:")) {
    	       mos.write( "currentrelationstsv", NullWritable.get(), t);
    	    } else if (key.toString().startsWith("current_relation_members:")) {
    	       mos.write("currentrelationmemberstsv", NullWritable.get(), t);
    	    } else if (key.toString().startsWith("current_relation_tags:")) {
    	       mos.write("currentrelationtagstsv", NullWritable.get(), t);
    	    }
    	        	
    	        	
        	//context.write(key, t);	
    	}
    	
    	
    }
  }


  /**
   * The actual main() method for our program; this is the
   * "driver" for the MapReduce job.
 * @throws IOException 
 * @throws ClassNotFoundException 
 * @throws InterruptedException 
   */
  
  public int run(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
    
	Configuration conf = getConf();
		
    Job job = new Job(conf, "OSMMRTest");
    job.setJarByClass(getClass());
    
    job.setMapperClass(OSMMapper.class);
    job.setReducerClass(OSMReducer.class);

	job.setMapOutputKeyClass(Text.class);
	job.setMapOutputValueClass(Text.class);
    job.setOutputKeyClass(NullWritable.class);
    job.setOutputValueClass(Text.class);
    
    
	job.setInputFormatClass(SequenceFileAsBinaryInputFormat.class);
	SequenceFileInputFormat.setInputPaths(job, new Path(args[1]));
	
	//job.setNumReduceTasks(8);
	
	
	MultipleOutputs.addNamedOutput(job, "currentnodestsv", TextOutputFormat.class,
			NullWritable.class, Text.class);
	MultipleOutputs.addNamedOutput(job, "currentnodetagstsv", TextOutputFormat.class,
			NullWritable.class, Text.class);
	MultipleOutputs.addNamedOutput(job, "currentwaystsv", TextOutputFormat.class,
			NullWritable.class, Text.class);
	MultipleOutputs.addNamedOutput(job, "currentwaytagstsv", TextOutputFormat.class,
			NullWritable.class, Text.class);
	MultipleOutputs.addNamedOutput(job, "currentwaynodestsv", TextOutputFormat.class,
			NullWritable.class, Text.class);
	MultipleOutputs.addNamedOutput(job, "currentrelationstsv", TextOutputFormat.class,
			NullWritable.class, Text.class);
	MultipleOutputs.addNamedOutput(job, "currentrelationmemberstsv", TextOutputFormat.class,
			NullWritable.class, Text.class);
	MultipleOutputs.addNamedOutput(job, "currentrelationtagstsv", TextOutputFormat.class,
			NullWritable.class, Text.class);
	
	
	
	
	//tell Hadoop not to create empty part-r-xxxxxx file(s)
	LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);
	
	
    FileOutputFormat.setOutputPath(job, new Path(args[2]));
    
	//job.setOutputFormatClass(SequenceFileOutputFormat.class);
	//SequenceFileOutputFormat.setOutputPath(job, new Path(outDir));

 
    //conf.set("mapred.job.tracker", "local");

    // set up multiple outputs for each type of tsv file 
    // MultipleOutputs
    
   job.submit();
   
	int retVal = job.waitForCompletion(true)?0:1;
	return retVal;
	
  }
  
  

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new OSMMRTest(), args);
	}

}


  