package team.javafx.recorder.util;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Paths;

public class PathCreator {

	private PathCreator() {

	}

	public static String getPathAsURL(String path) {
		String urlPath = "<null>";
		try {
			urlPath = Paths.get(path).toUri().toURL().toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return urlPath;
	}
	
	public static void createDirIfNotExist(String directoryPath) {
		File theDir = new File(directoryPath);

		if (!theDir.exists()) {
		    boolean result = false;
		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    	System.out.println("Unable to create dir : " + directoryPath);  
		    }        
		    if(result) {    
		        System.out.println("Dir " + directoryPath + " created");  
		    }
		}
	}

}
