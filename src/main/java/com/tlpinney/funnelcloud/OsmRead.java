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



import org.apache.commons.codec.binary.Hex;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.SequenceFile.CompressionType;



public class OsmRead {

    public static void main(String[] args) throws IOException, DataFormatException {
        //p("Read in OSM");
        
        //BlobHeader.Builder bh = BlobHeader.newBuilder();
        Blob.Builder b = Blob.newBuilder();
    
        // open the osm file
        String pbf = "/vagrant/contrib/district-of-columbia-latest.osm.pbf";
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(pbf));
        //FileInputStream fis = new FileInputStream(pbf);
        
        // read in the first 4 bytes to get the size of the blobheader 
        // (network byte order)
        
        //FileChannel fic = fis.getChannel();
        //long pbf_size = fic.size();
        //p("pbf size: " + pbf_size);
        
        //ByteBuffer buf = new ByteBuffer.all
        
        byte [] buf = new byte[4];
        
        //Integer bsize = new Integer();
        in.read(buf);
        
        int bhsize = ByteBuffer.wrap(buf).getInt();
        
        byte [] blobbuf = new byte[bhsize];
        in.read(blobbuf);
        
        BlobHeader bh = BlobHeader.parseFrom(blobbuf);
        
        //p(bh.getType());
        //p(bh.getIndexdata());
        //p(bh.getDatasize());
        
        int bsize = bh.getDatasize();
        
        buf = new byte[bsize];
        
        in.read(buf);
        
        // read in the blob
        
        Blob blob = Blob.parseFrom(buf);
        
        //p(blob);
        //p(blob.hasRawSize());
        //p(blob.getRawSize());
        //p(blob.getSerializedSize());
        
        
        
        //p(blob.getZlibData());
        
        
        
        Inflater inflater = new Inflater();
        inflater.setInput(blob.getZlibData().toByteArray());
        byte[] blobd = new byte[blob.getRawSize()];
        
        inflater.inflate(blobd);
        
        //HeaderBlock hb = HeaderBlock.parseFrom(blobd);
        //p(hb.getBbox());
        //p(hb.getSource());
        //p(hb);
        //p(hb.getOsmosisReplicationTimestamp());
        //p(hb.getWritingprogram());
        
        // read in the rest of the file and spit out locations of where the end and begin
        SequenceFile.Writer writer = null;
        
        String uri = "osm2.seq";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path(uri);
        
        IntWritable key = new IntWritable();
        BytesWritable value = new BytesWritable();
        
        writer = SequenceFile.createWriter(fs, conf, path, 
                key.getClass(), value.getClass(), CompressionType.NONE);
        
        
        
        p("Read in next OSMData block");
        
        buf = new byte[4];
        int count = 0;
        while (in.read(buf) != -1 ) {   
        
            //in.read(buf);
            bhsize = ByteBuffer.wrap(buf).getInt();
        
            //p(bhsize);
        
            blobbuf = new byte[bhsize];
            in.read(blobbuf);
        
            bh = BlobHeader.parseFrom(blobbuf);
        
            //p(bh.getType());
            //p(bh.getDatasize());
            
            byte[] data = new byte[bh.getDatasize()];
            
            in.read(data);
            
            blob = Blob.parseFrom(data);
        
            // read blob into a bytearray and write and ignore it 
            
            
            // write out pbf in a sequence file (no compression)
            BytesWritable bw = new BytesWritable(data);           
            key.set(count);
            value.set(bw);
            writer.append(key, value);
            
            
            p(Hex.encodeHexString(data));
            
            System.exit(0);
            
            
            // key being a simple index, value being the blob 
          count += 1;
        }
         
        writer.close();
        
        
        

    }
    
    public static void p(Object o) {
        System.out.println(o);
    }

}
