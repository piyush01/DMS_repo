package org.dspace.content;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class GetPropertiesValues {

	String result = "";
	//InputStream inputStream ;
	 FileInputStream inputStream;
	//public static final String xmlFilePath = "C:\\DMS\\dspace-5.3-src-release\\dspace-api\\src\\main\\resources\\Messages.properties";
	 //public static final String xmlFilePath = "C:\\DMS\\dspace-5.3-src-release\\dspace\\config\\dspace.cfg";
public  String getPropValues(String xmlFilePath ) throws IOException {
		
		try {
			Properties prop = new Properties();
			//String propFileName = "config.properties";
 
			//inputStream = getClass().getClassLoader().getResourceAsStream(xmlFilePath);
		inputStream=new FileInputStream(xmlFilePath);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + xmlFilePath + "' not found in the classpath");
			}
 
			Date time = new Date(System.currentTimeMillis());
 
			// get the property value and print it out
			/*String user = prop.getProperty("metadata.dc.title");
			String company1 = prop.getProperty("metadata.dc.title.alternative");
			String company2 = prop.getProperty("jsp.adminhelp");
			String company3 = prop.getProperty("jsp.administer");*/
			String webui_itemdisplay_default = prop.getProperty("webui.itemdisplay.default");
 
			result = webui_itemdisplay_default;
			//System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}

public  String getMessagePropValues(String xmlFilePath,String element ) throws IOException {
	
	try {
		Properties prop = new Properties();
		//String propFileName = "config.properties";

		//inputStream = getClass().getClassLoader().getResourceAsStream(xmlFilePath);
	inputStream=new FileInputStream(xmlFilePath);
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + xmlFilePath + "' not found in the classpath");
		}

		Date time = new Date(System.currentTimeMillis());

		// get the property value and print it out
		/*String user = prop.getProperty("metadata.dc.title");
		String company1 = prop.getProperty("metadata.dc.title.alternative");
		String company2 = prop.getProperty("jsp.adminhelp");
		String company3 = prop.getProperty("jsp.administer");*/
		String key = prop.getProperty("metadata.dc."+element);

		result = key;
		//System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);
	} catch (Exception e) {
		System.out.println("Exception: " + e);
	} finally {
		inputStream.close();
	}
	return result;
}

public  String getsearchFilter(String xmlFilePath,String element ) throws IOException {
	
	try {
		Properties prop = new Properties();
		//String propFileName = "config.properties";

		//inputStream = getClass().getClassLoader().getResourceAsStream(xmlFilePath);
	inputStream=new FileInputStream(xmlFilePath);
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + xmlFilePath + "' not found in the classpath");
		}

		Date time = new Date(System.currentTimeMillis());

		// get the property value and print it out
		/*String user = prop.getProperty("metadata.dc.title");
		String company1 = prop.getProperty("metadata.dc.title.alternative");
		String company2 = prop.getProperty("jsp.adminhelp");
		String company3 = prop.getProperty("jsp.administer");*/
		String key = prop.getProperty("jsp.search.filter."+element);

		result = key;
		//System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);
	} catch (Exception e) {
		System.out.println("Exception: " + e);
	} finally {
		inputStream.close();
	}
	return result;
}

public  String getsearchAdvance(String xmlFilePath,String element ) throws IOException {
	
	try {
		Properties prop = new Properties();
		//String propFileName = "config.properties";

		//inputStream = getClass().getClassLoader().getResourceAsStream(xmlFilePath);
	inputStream=new FileInputStream(xmlFilePath);
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + xmlFilePath + "' not found in the classpath");
		}

		Date time = new Date(System.currentTimeMillis());

		// get the property value and print it out
		/*String user = prop.getProperty("metadata.dc.title");
		String company1 = prop.getProperty("metadata.dc.title.alternative");
		String company2 = prop.getProperty("jsp.adminhelp");
		String company3 = prop.getProperty("jsp.administer");*/
		String key = prop.getProperty("jsp.search.advanced.type."+element);

		result = key;
		//System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);
	} catch (Exception e) {
		System.out.println("Exception: " + e);
	} finally {
		inputStream.close();
	}
	return result;
}

}
