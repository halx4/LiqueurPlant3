package pipeIO.out;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class PipePrinter2 implements PipePrinterInterface{

	private OutputStreamWriter osr;
	private File file;
	public PipePrinter2(File file) throws Exception {
		this.file=file;

	}
	
	@Override
	public void send(String message){

		try {
			osr = new OutputStreamWriter(new FileOutputStream(file));
			osr.write(message,0,message.length());
			osr.flush();
			osr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	@Override
	public void stop() {
	
	}
	
}
