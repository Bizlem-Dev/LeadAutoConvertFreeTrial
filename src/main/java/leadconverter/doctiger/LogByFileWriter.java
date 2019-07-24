package leadconverter.doctiger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class LogByFileWriter {
	public static void logger_info(String data) {
		String path=ResourceBundle.getBundle("config").getString("log.file.path");
		OutputStream os = null;
	    SimpleDateFormat sdf156 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  	data=sdf156.format(new Date())+" INFO : "+data+"\n";
	    
	    try {
	        os = new FileOutputStream(new File(path), true);
	        os.write(data.getBytes(), 0, data.length());
	        //os.write(b, off, len);
	        
	    } catch (IOException e) {
	        System.out.println("LogByFileWriter : "+e.getMessage());
	    }finally{
	        try {
	            os.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("LogByFileWriter : "+e.getMessage());
	        }
	    }
	}
}
