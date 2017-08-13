package pipeIO.in;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.apache.commons.io.IOUtils;


public class PipeTailer implements Runnable {

    private static final int DEFAULT_DELAY_MILLIS = 1000;

    /**
     * The file which will be tailed.
     */
    private final File file;

    /**
     * The amount of time to wait for the file to be updated.
     */
    private final long delayMillis;



    /**
     * The listener to notify of events when tailing.
     */
    private final PipeTailerListener listener;


    
    /**
     * The tailer will run as long as this value is true.
     */
    private volatile boolean run = true;

    /**
     * Creates a Tailer for the given file, starting from the beginning, with the default delay of 1.0s.
     * @param file The file to follow.
     * @param listener the TailerListener2 to use.
     */
    public PipeTailer(File file, PipeTailerListener listener) {
        this(file, listener, DEFAULT_DELAY_MILLIS);
    }

    /**
     * Creates a Tailer for the given file, starting from the beginning.
     * @param file the file to follow.
     * @param listener the TailerListener2 to use.
     * @param delayMillis the delay between checks of the file for new content in milliseconds.
     */
    public PipeTailer(File file, PipeTailerListener listener, long delayMillis) {
        this.file = file;
        this.delayMillis = delayMillis;
        this.listener = listener;
        listener.init(this);
    }

    
    /**
     * Creates and starts a Tailer for the given file, starting at the beginning of the file
     * 
     * @param file the file to follow.
     * @param listener the TailerListener2 to use.
     * @param delayMillis the delay between checks of the file for new content in milliseconds.
     * @param isDaemon true declares the thread as a Daemon. Default = false.
     * @return The new tailer
     */
    public static PipeTailer create(File file, PipeTailerListener listener, long delayMillis,boolean isDaemon) {
        PipeTailer tailer = new PipeTailer(file, listener, delayMillis);
        Thread thread = new Thread(tailer);
        thread.setDaemon(isDaemon);
        thread.start();
        return tailer;
    }


    /**
     * Creates and starts a Tailer for the given file, starting at the beginning of the file
     * 
     * @param file the file to follow.
     * @param listener the TailerListener2 to use.
     * @param delayMillis the delay between checks of the file for new content in milliseconds.
     * @return The new tailer
     */
    public static PipeTailer create(File file, PipeTailerListener listener, long delayMillis) {
        return create(file, listener, delayMillis,false);

    }

    /**
     * Creates and starts a Tailer for the given file, starting at the beginning of the file
     * with the default delay of 1.0s
     * 
     * @param file the file to follow.
     * @param listener the TailerListener2 to use.
     * @return The new tailer
     */
    public static PipeTailer create(File file, PipeTailerListener listener) {
        return create(file, listener, DEFAULT_DELAY_MILLIS,false);
    }

    /**
     * Return the file.
     *
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * Return the delay in milliseconds.
     *
     * @return the delay in milliseconds.
     */
    public long getDelay() {
        return delayMillis;
    }

    /**
     * Follows changes in the file, calling the TailerListener2's handle method for each new line.
     */
    public void run() {
    	BufferedReader reader=null;
		String line;

        try {

           
                
        	reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            while (run) {
				while ((line = reader.readLine()) != null) {
					//System.out.println("PipeTailer Line received! calling handler.");
					listener.handle(line);
					
				}
            	
            	
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                }

            }

        } catch (Exception e) {
        	System.err.println("Pipe Tailer error");
        	e.printStackTrace();
           // listener.handle(e);
            

        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Allows the tailer to complete its current loop and return.
     */
    public void stop() {
        this.run = false;
    }

 

}
