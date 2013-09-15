package com.tlpinney.funnelcloud;

import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;



public class Main {

	public static void main(String[] args) throws IOException, DataFormatException {
		 // TODO need to implement something like argparse4j
		 // now going with simple if statements
		 
		 if (Arrays.asList(args).contains("-h")) {
			 usage();
		 } else if (Arrays.asList(args).contains("--help")) {
			 usage();
		 } else if (Arrays.asList(args).contains("--version") ) {
			 version();
		 } else if (Arrays.asList(args).contains("-v") ) {
			 version();
		 } else if (Arrays.asList(args).contains("beam") ) {
			 // check to see if help exists, if so print out the beam usage
			 // if no args exist, print out help
			 if (args.length == 1) {
				 beam_usage();
			 } else if (args.length == 3) {
				 // Call Beam 
				 String [] bargs = { args[1], args[2] };		 
				 Beam.main(bargs);
			 } else {
				 p("Invalid Options");
				 beam_usage();
			 }
		 } else if (Arrays.asList(args).contains("ingest")) {
			 if (args.length == 1) {
				ingest_usage();
			 }
			 // check for subcommands 		 
			 if (Arrays.asList(args).contains("osm")) {
				 if (args.length == 2) {
					 osm_usage();
				 } else if (args.length == 4) {
					 String [] oargs = { args[2], args[3] };
					 OsmReadWeb.main(oargs);
				 } else {
					 p("Invalid Options");
					 osm_usage();
				 }
			 } else if (Arrays.asList(args).contains("wikipedia")) {
				 if (args.length == 2) {
					 wikipedia_usage();
				 } else if (args.length == 5) {
					 // make sure index is included 
					 String [] wargs = {args[2], args[3], args[4] };
					 Wikipedia.main(wargs);
				 } else {
					 p("Invalid Options");
					 wikipedia_usage();
				 }
				 
			 } else if (Arrays.asList(args).contains("geonames")) {
		 		if (args.length == 2) {
		 			geonames_usage();	
		 		} else if (args.length == 4) {
		 			String [] wargs = {args[2], args[3]};
		 			GeoNames.main(wargs);
		 		} else {
		 			p("Invalid Options");
		 			geonames_usage();
		 		}
		 	} else {
				p("Invalid Options");
				ingest_usage(); 
			} 
			 
		 } else {
			 p("Invalid Options");
			 usage();
		 }
	}
	
	public static void usage() {
		p("Usage: fcl [-v] [-h] command [<args>]");
		p("");
		p("    -v, --version                    Print the version");
		p("    -h, --help                       Print the help");
		p("");
		p("Available subcommands, for details fcl command --help");
		p("    beam");
		p("    ingest");
		
		
		
		System.exit(0);
		

	}
	
	public static void beam_usage() {
		p("Usage: fcl beam weburl hdfsdest");
		p("");
		p("    -r, --recursive                  NOT IMPLEMENTED");
		p("    -v, --verbose                    NOT IMPLEMENTED");
		p("");

		
		System.exit(0);
		

	}
	
	public static void ingest_usage() {
		p("Usage: fcl ingest command [<args>]");
		p("");
		p("Available subcommands, for details fcl command --help");
		p("    osm");
		p("    wikipedia");
		
		
		
		System.exit(0);
		

	}
	
	public static void osm_usage() {
		p("Usage: fcl ingest osm weburl hdfsdest");
		p("");
		p("    -v, --verbose                    NOT IMPLEMENTED");
		p("");

		
		System.exit(0);
		

	}
	
	public static void wikipedia_usage() {
		p("Usage: fcl ingest wikipedia enwiki-index.txt weburl hdfsdest");
		p("");
		p("    -v, --verbose                    NOT IMPLEMENTED");
		p("");

		
		System.exit(0);
		

	}
	
	public static void geonames_usage() {
		p("Usage: fcl ingest geonames weburl hdfsdest");
		p("");
		p("    -v, --verbose                    NOT IMPLEMENTED");
		p("");

		
		System.exit(0);
		

	}
	
	
	public static void version() {
		// TODO read from pom.xml
		p("FunnelCloud version 0.1.3");
		System.exit(0);
	}
	
    public static void p(Object o) {
        System.out.println(o);
    }

}
