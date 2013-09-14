package com.tlpinney.funnelcloud;

import java.io.IOException;
import java.util.Arrays;



public class Main {

	public static void main(String[] args) throws IOException {
		
		 
		
		 //args = new String[]{ "beam" };
			
		 // need to implement something like argparse4j
		 // now going with a simple if statement 
		 
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
			 
		 } else {
			 p("Unknown options");
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
	
	public static void version() {
		p("FunnelCloud version 0.1.1");
		System.exit(0);
	}
	
    public static void p(Object o) {
        System.out.println(o);
    }

}
