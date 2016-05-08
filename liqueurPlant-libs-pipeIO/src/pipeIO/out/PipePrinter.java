package pipeIO.out;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Deprecated
public class PipePrinter implements PipePrinterInterface{
	private FileOutputStream fos;
	private BufferedOutputStream bos;
	private File file;
	private boolean errorReported=false;
	
	public PipePrinter(File file) throws Exception {
		this.file=file;
		fos=new FileOutputStream(file);
		bos=new BufferedOutputStream(fos);
	}
	
	@Override
	public void send(String message){
		//System.out.println("PipePrinter: sending string:"+message);
		try{//////////////
			bos.write((message).getBytes());
			bos.flush();
		}
		catch(IOException e){

			if(!errorReported){
				System.err.println("PipePrinter error. trying to reestablish connection...");
				e.printStackTrace();
				errorReported=true;
			}
			try {
				bos.close();
				bos=new BufferedOutputStream(new FileOutputStream(file));
				//System.out.println("connection reeshtablished!");
			} catch (IOException e1) {
				if(!errorReported){
					System.out.println("reesht. failed");
					e1.printStackTrace();
					errorReported=true;
				}
			}
		}
	}
	
	@Override
	public void stop() {
		try {
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
