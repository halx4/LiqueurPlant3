package liqueurPlant.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.sun.javafx.fxml.PropertyNotFoundException;

public class PropertiesHandler {
	private Properties properties = new Properties();

	private String propertiesFilename;

	 public PropertiesHandler(String filename) throws IOException  {
		this.propertiesFilename = filename;
		loadProperties();
	}

	// ----------------------------------
	public void loadProperties() throws IOException {

		File propFile = new File(propertiesFilename);
		FileInputStream iFile = null;

		System.out.println("properties file exists. trying to load...");
		iFile = new FileInputStream(propFile);
		properties.load(iFile);

		try {
			if (iFile != null)
				iFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		properties.list(System.out);
	}

	// ------------------------------
	public String getProperty(String key) throws PropertyNotFoundException {
		String returnValue = properties.getProperty(key);
		if (returnValue == null)
			throw new PropertyNotFoundException(key);
		else
			return returnValue;
	}

}
