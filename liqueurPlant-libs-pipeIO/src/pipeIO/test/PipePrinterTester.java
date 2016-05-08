package pipeIO.test;

import java.io.File;

import pipeIO.out.PipePrinter;
import pipeIO.out.PipePrinter2;
import pipeIO.out.PipePrinterInterface;

public class PipePrinterTester {
	private PipePrinterInterface printer;
	
	PipePrinterTester(String[] args){
		
		try{
			File file=new File(args[0]);
			
			switch(args[1]){
				case "1":
					printer= new PipePrinter(file);
					break;
				case "2":
					printer= new PipePrinter2(file);
					break;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		int i=0;
		while(true){
			System.out.println("printing cycle #"+i);
			printer.send("Hello"+Integer.toString(i));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
	}
	
	
	
	
	
	
	
	
	/** args[0] <- pipe name
	 *  args[1] <- pipePrinterType
	*/
	public static void main(String[] args) {
		
		if(args.length!=2){
			System.out.println("check arguments!");
			System.out.println("args[0] <- pipe name");
			System.out.println("args[1] <- pipePrinterType");
		}
		else new PipePrinterTester(args);

	}

}
