package com.tlpinney.funnelcloud;

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

import java.io.IOException;
import java.util.Iterator;


public class OpenStreetMapMR{

  public static class OSMMapper extends MapReduceBase
      implements Mapper<IntWritable, BytesWritable, Text, LongWritable> {

    private final static Text key = new Text();
    private final static LongWritable count = new LongWritable();

    public void map(IntWritable index, BytesWritable blob,
        OutputCollector<Text, LongWritable> output, Reporter reporter)
        throws IOException {
      
      // process the blob

      // count the keys and 
      // output the key count 
     
    	//  output.collect(key, count);
      }

    }
  



  public static class OSMReducer extends MapReduceBase
      implements Reducer<Text, LongWritable, Text, LongWritable> {

    public void reduce(Text key, Iterator<LongWritable> values,
        OutputCollector<Text, LongWritable> output, Reporter reporter)
        throws IOException {

    	// add up any extra keys then add up the key count
    	System.out.println("Hello");
    	
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

    FileInputFormat.addInputPath(conf, new Path("input"));
    FileOutputFormat.setOutputPath(conf, new Path("output"));

    conf.setMapperClass(OSMMapper.class);
    conf.setReducerClass(OSMReducer.class);

    jc.setConf(conf);

    try {
      JobClient.runJob(conf);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}