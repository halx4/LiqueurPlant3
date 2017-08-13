package pipeIO.in;

import org.apache.commons.io.input.TailerListener;

public interface PipeTailerListener extends TailerListener {
	
	public void init(PipeTailer tailer);
	
}
