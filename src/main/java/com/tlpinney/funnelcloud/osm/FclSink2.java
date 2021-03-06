package com.tlpinney.funnelcloud.osm;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.EntityType;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.RelationMember;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;


public class FclSink2 implements Sink{

	public HashMap<String,String> keyvals = new HashMap<String,String>();
	
	
	
	
	public void initialize(Map<String, Object> map){
		// TODO Auto-generated method stub
		
		//p("Initialization");
		
		// set up tsv files for writing 
		// write to local file system 
	
	}

	public void complete() {
		// TODO Auto-generated method stub
		
		//p("complete");
		
		
	}

	public void release() {
		// TODO Auto-generated method stub
		//p("release");
	}

	public void process(EntityContainer ec) {
		// TODO Auto-generated method stub
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(tz);
		
		//p("process entity: " + ec);
		Entity e = ec.getEntity();
		
		//EntityContainerFactory<T> containerFactory;
		
		//nextValue = containerFactory.createContainer(entity);
		
		
		//p(e.getType());
		
		if (e.getType() == EntityType.Node) {
			// process nodes 		
			//p(e.getClass());
			//p(e.getUser());
			
			Node n = (Node) ec.getEntity();
			
			
			//p(n.getLatitude());
			//p(n.getLongitude());
			
			
			
			//Map<String, Object> mt = e.getMetaTags();
			
			//for (String name : mt.keySet()) {
			//	p("metatag: " + name + " " + mt.get(name));
			//}
			
		
	//		Node n = new Node(e);
			
			OsmUser ou = e.getUser();
		
			//p(ou.getName());
			//p(e.getTimestamp());
			Collection<Tag> ct = e.getTags();
		
			long id = n.getId();
			double latitude = n.getLatitude();
			double longitude = n.getLongitude();
			long changeset_id = n.getChangesetId();
			long millis = n.getTimestamp().getTime();
			//String tstamp2 = n.getTimestamp().getTime()
			String tstamp = df.format(n.getTimestamp().getTime());
			
			
			boolean visible = true;
			long tile = 0;
			// ignore tile for now, will need to generate it 
			// also add more tile levels 
			long version = n.getVersion();
			
			// output to tsv 
			String row = id + "\t" + latitude + "\t" + longitude + 
				"\t" + changeset_id + "\t" + visible + "\t"   + tstamp +
				"\t" + tile + "\t" + version + "\n";
			
		
			keyvals.put(String.format("current_nodes:%11d", id), row);  
		
			
			
			for (Tag t : ct ) {
				//p(t);
				String k = t.getKey();
				String v = t.getValue();
				
				row = id + "\t" + k + "\t" + v + "\n"; 
				
				//System.out.print(row);
			
				keyvals.put(String.format("current_node_tags:%11d", id), row);
		
				
				
			}
		
		
			
			
			// insert data into parquet tables
			
			// output to tsv file 
			// this will be easy to port to mapreduce
			// next output would be some type of block compressed format
			// that is compatible with hive with a custom Serde 
			// this will allow easy importation into parquet 
			// the next step will be direct to parquet format 
			// port over osmosis-apidb to impala to allow for reads 
			// inserts for building an api db 
			
	
			
		} else if (e.getType() == EntityType.Way) {
			
			Way w = (Way) ec.getEntity();
			
			long id = w.getId();
			long changeset_id = w.getChangesetId();
			String tstamp = df.format(w.getTimestamp().getTime());
			boolean visible = true; 
			long version = w.getVersion();
			
			String row = id + "\t" + changeset_id + "\t" + tstamp
					+ "\t" + visible + "\t" + version + "\n";
			
			
				
			keyvals.put(String.format("current_ways:%11d", id), row);
	
			
			Collection<Tag> ct = w.getTags();
			
			
			for (Tag t : ct ) {
				//p(t);
				String k = t.getKey();
				String v = t.getValue();
				
				row = id + "\t" + k + "\t" + v + "\n"; 
				
				//System.out.print(row);
			
					
				keyvals.put(String.format("current_way_tags:%11d", id), row);

			}
			
			List<WayNode> wnl = w.getWayNodes();
			
			long sequence_id = 0;
			for (WayNode wn : wnl) {
				long node_id = wn.getNodeId();
				row = id + "\t" + node_id + "\t" + sequence_id + "\n";
				sequence_id += 1;
				
			
					
			keyvals.put(String.format("current_way_nodes:%11d", id), row);

				
			}
	
		} else if (e.getType() == EntityType.Relation) {
			
			Relation r = (Relation) ec.getEntity();
			
			long id = r.getId();
			long changeset_id = r.getChangesetId();
			String tstamp = df.format(r.getTimestamp().getTime());
			boolean visible = true;
			long version = r.getVersion();
			
			String row = id + "\t" + changeset_id + "\t" + tstamp + "\t"
					+ visible + "\t" + version + "\n";
			

			
			keyvals.put(String.format("current_relations:%11d", id), row);

			
			List<RelationMember> lrm = r.getMembers();
			
			int sequence_id = 0;
			for (RelationMember rm : lrm) {
				int member_type = rm.getMemberType().ordinal();
				long member_id = rm.getMemberId();
				String member_role = rm.getMemberRole();
				
				row = id + "\t" + member_type + "\t" + member_id + "\t"
						+ member_role + "\t" + sequence_id + "\n";
				sequence_id += 1;
				
					keyvals.put(String.format("current_relation_members:%11d", id), row);
	
				
				
			}
			
			
			Collection<Tag> ct = r.getTags();
			
			for (Tag t : ct ) {
				//p(t);
				String k = t.getKey();
				String v = t.getValue();
				
				row = id + "\t" + k + "\t" + v + "\n"; 
				
				//System.out.print(row);
			
					keyvals.put(String.format("current_relation_tags:%11d", id), row);
			
			}
			
			
			
			
		} else  {
			this.complete();
			p("Unsupported type: " + e.getType() );
			System.exit(0);
		}
		
		
	
	
	}
	
	public static void p(Object o) {
	   System.out.println(o);
	}
	

}
