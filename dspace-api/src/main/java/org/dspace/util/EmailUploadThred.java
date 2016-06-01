package org.dspace.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class EmailUploadThred extends Thread{
	 /** log4j category */
    private static Logger log = Logger.getLogger(EmailUploadThred.class);

	 /** log4j category */
	InputStream is;
	  String type;

	  public EmailUploadThred(InputStream is, String type)
	  {
	    this.is = is;
	    this.type = type;
	  }

	  public void run()
	  {
	    try {
	      InputStreamReader isr = new InputStreamReader(is);
	      BufferedReader br = new BufferedReader(isr);
	      String line=null;
	      while ( (line = br.readLine()) != null) {
	          log.info(type + ">" + line);
	      }
	    } catch (IOException ioe) {
	      ioe.printStackTrace();
	    }
	  }

}
