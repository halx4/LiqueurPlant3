package liqueurPlant.client.siloDriverCodesys;

import java.io.File;
import pipeIO.out.PipePrinter2;
import pipeIO.out.PipePrinterInterface;

public class OutPipeFiller implements Runnable{
	
    private static final int DEFAULT_DELAY_MILLIS = 1000;
	
	private File file;
	private PipePrinterInterface pipePrinter;
	private volatile boolean run=true;
    private final long delayInterval;
    private PipeFillerSource source;
	
    
	public OutPipeFiller(File outPipe,PipeFillerSource source) {
		this(outPipe, source, DEFAULT_DELAY_MILLIS);
	}
    
	
	public OutPipeFiller(File outPipe,PipeFillerSource source,long delayInterval) {
		this.file=outPipe;
		this.delayInterval=delayInterval;
		this.source=source;
	}


	public static OutPipeFiller create(File outPipe,PipeFillerSource source,long delayInterval){
		OutPipeFiller filler=new OutPipeFiller(outPipe,source,delayInterval);
		Thread thread=new Thread(filler);
		thread.setDaemon(false);
		thread.start();
		return filler;
	}


	@Override
	public void run() {
		try {
			pipePrinter=new PipePrinter2(file);
		
		
			while(run){
				
				pipePrinter.send(source.getStringToSend());
				Thread.sleep(delayInterval);
				
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
				pipePrinter.stop();

		}
	}
	
	
    public void stop() {
        this.run = false;
    }

	
}
