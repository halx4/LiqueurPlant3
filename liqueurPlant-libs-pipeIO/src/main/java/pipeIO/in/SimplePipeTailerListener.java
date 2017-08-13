package pipeIO.in;

import org.apache.commons.io.input.TailerListenerAdapter;

public class SimplePipeTailerListener extends TailerListenerAdapter implements PipeTailerListener {
	
	@Override
	public void handle(String s){
		System.out.println("NewStringReceived:"+s);
		if(s.equals("exit"))System.exit(0);;
	}

	@Override
	public void init(PipeTailer tailer) {
		// TODO Auto-generated method stub
		
	}
}
