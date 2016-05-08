package pipeIO.test;
import java.io.File;

import pipeIO.in.PipeTailer;
import pipeIO.in.SimplePipeTailerListener;

public class PipeTailerExample {

	public static void main(String[] args) {
		System.out.println("MainClass");
		File file=null;
		SimplePipeTailerListener listener=new SimplePipeTailerListener();
		
		if(args.length==0){
			System.out.println("check arguments!");
			System.exit(0);
		}
		else{
			file=new File(args[0]);
		}
		
		if(!file.exists()){
			System.err.println("file does not exist");
			//System.exit(1);
		}
		
		PipeTailer.create(file,listener,Integer.parseInt(args[1]));
		
		
		System.out.println("END");
		
		
	}

	
	
}
