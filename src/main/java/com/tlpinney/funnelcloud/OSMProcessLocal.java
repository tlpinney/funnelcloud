package com.tlpinney.funnelcloud;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import org.apache.commons.codec.binary.Hex;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;



import org.openstreetmap.osmosis.osmbinary.BinaryParser;
import org.openstreetmap.osmosis.osmbinary.file.BlockInputStream;

import crosby.binary.osmosis.OsmosisBinaryParser;







import org.openstreetmap.osmosis.osmbinary.Fileformat.Blob;
import org.openstreetmap.osmosis.osmbinary.Fileformat.BlobHeader;
import org.openstreetmap.osmosis.osmbinary.Osmformat.PrimitiveGroup;
import org.openstreetmap.osmosis.osmbinary.Osmformat.PrimitiveBlock;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;

import com.tlpinney.funnelcloud.osm.FclSink;


// create a custom sink which can process OSM using osmosis 
// 








public class OSMProcessLocal {

	public static void main(String[] args) throws IOException, DataFormatException {
		// TODO Auto-generated method stub
		//p("hello world");
		
		// open up the sequence file and start processing it with osmosis 
		
		String uri = "district-of-columbia-latest.osm.pbf.seq";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path(uri);
        
        BytesWritable key = new BytesWritable();
        BytesWritable value = new BytesWritable();
        
        
        
        SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);
       
        
        //p(reader.getCompressionCodec());
        //p(reader.getCompressionType());
        //p(reader.getKeyClassName());
        //p(reader.getValueClassName());
        
        
    	FileWriter current_nodes = new FileWriter("current_nodes.tsv");
		FileWriter current_node_tags = new FileWriter("current_node_tags.tsv");
		FileWriter current_ways = new FileWriter("current_ways.tsv");
		FileWriter current_way_tags = new FileWriter("current_way_tags.tsv");
		FileWriter current_way_nodes = new FileWriter("current_way_nodes.tsv");
		FileWriter current_relations = new FileWriter("current_relations.tsv");
		FileWriter current_relation_members = new FileWriter("current_relation_members.tsv");
		FileWriter current_relation_tags = new FileWriter("current_relation_tags.tsv");
		
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("current_nodes", current_nodes);
		hm.put("current_node_tags", current_node_tags);
		hm.put("current_ways", current_ways);
		hm.put("current_way_tags", current_way_tags);
		hm.put("current_way_nodes", current_way_nodes);
		hm.put("current_relations", current_relations);
		hm.put("current_relations", current_relations);
		hm.put("current_relation_members", current_relation_members);
		hm.put("current_relation_tags", current_relation_tags);
        
        while (reader.next(key, value)) {
        
        //p(key);
        //p(value.getLength());
        //p(Hex.encodeHexString(value.getBytes()));
        // not sure why getBytes is messed up
        
        byte[] barr = new byte[value.getLength()];    
        barr = value.copyBytes();
        //p(barr.length);
        
        //p(Hex.encodeHexString(barr));
        
        
        
        
        
        //BlobHeader bh = BlobHeader.parseFrom(key.getBytes());
        //int dsize = bh.getDatasize();
        //byte[] barr = new byte[dsize];
        // read inflate the compressed blob
       
        
       
        
        //p(value.getBytes());
        
        Blob blob = Blob.parseFrom(barr);
         
       	//p(blob.getZlibData());
       	
        Inflater inflater = new Inflater();
        inflater.setInput(blob.getZlibData().toByteArray());
        byte[] blobd = new byte[blob.getRawSize()];
        
        inflater.inflate(blobd);
       	
        // load blobd into a file 
        
        PrimitiveBlock pb = PrimitiveBlock.parseFrom(blobd);
        
        OsmosisBinaryParser obp = new OsmosisBinaryParser();
        
        
        FclSink fcls = new FclSink();
        
        
        // this is bad 
       
        
        	fcls.initialize(hm);
        	obp.setSink(fcls);
        	obp.parse(pb);
        	fcls.complete();
        	fcls.release();
        
        
        }
       
		current_nodes.close();
		current_node_tags.close();
		current_ways.close();
		current_way_tags.close();
		current_way_nodes.close();
		current_relations.close();
		current_relation_members.close();
		current_relation_tags.close();
        
        
        //p(pb.getPrimitivegroupCount());
        //p(pb.)
        
        //for (int i=0; i< pb.getPrimitivegroupCount(); i++) {
        //	PrimitiveGroup pg  = pb.getPrimitivegroup(i);
        //	p(pg.getClass());
        //	p(pg.)
        	
        //}
        
        
        
        // create parquet structure and output data to it 
        // for relations and  ways, add in bounding box to allow for pseudo spatial 
        // queries 
        
        // figure out how geonames maps as certain types
        
        // load the value in osmos 
        
        // create a gazeteer based on geonames format 
        
        
        
        
               // key.getClass(), value.getClass(), CompressionType.NONE);
		
        
        
        // write nodes to an impala parquet file
        
        // node_tags 
        // get all the nodes that have a name 
        // hook into R 
        // look at all the ways that have a name 
        // hook into R 
        // 
        
        
        
        
		
	}
	
    public static void p(Object o) {
        System.out.println(o);
    }
}
