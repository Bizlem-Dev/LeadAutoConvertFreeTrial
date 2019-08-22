package leadconverter.mailcheker.spamassertion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import leadconverter.doctiger.LogByFileWriter;

import org.apache.sling.commons.json.JSONObject;

public class MailChecker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        String email_details=emailValidation("tejal.jabade10@gmail.com");
        System.out.println(email_details);
        //Exist : akhileshyadav0308@gmail.com
        //Does not Exist : akhileshyadav0308asd@gmail.com
		//emailValidationByApiLayer("akhileshyadav0308@gmail.com");
	}
	public static String emailValidationByApiLayer(String userid) {
		String email_status ="NA";
		String apilayer_email_checker_api = ResourceBundle.getBundle("config").getString("apilayer_email_checker_api") + userid+"&smtp=1&format=0";
		try {
			URL url = new URL(apilayer_email_checker_api);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String email_checker_response = reader.readLine();
			JSONObject email_checker_response_json=new JSONObject(email_checker_response);
			System.out.println(email_checker_response_json);
            //JSONObject obj = new JSONObject(text);
			//expireFlag = obj.getInt("expireFlag");
			//System.out.println(Integer.toString(expireFlag));
			conn.disconnect();
		} catch (Exception ex) {
			System.out.println("Error : "+ex);
		}
		return email_status;
	}
	
	public static String emailValidation(String userid) {
		//LogByFileWriter.logger_info("MailChecker : emailValidation() " + userid);
		String email_status ="NA";
		String email_details =null;
		URL url = null;
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String email_checker_api=null;
		InputStream in = null;
		try {
			email_checker_api = ResourceBundle.getBundle("config").getString("email_checker_api") + userid;
		     url = new URL(email_checker_api);
			//System.out.println("Step 1");
			conn = (HttpURLConnection) url.openConnection();
			//System.out.println("Step 2");
			conn.setRequestMethod("GET");
			conn.connect();
			//System.out.println("Step 3");
		    in = conn.getInputStream();
			//System.out.println("Step 4");
			reader = new BufferedReader(new InputStreamReader(in));
			email_details = reader.readLine();
			//System.out.println("Step 5");
			/*
			System.out.println(text);
            if(text.contains("Does not Exist")){
            	email_status="Invalid";
            	System.out.println("Invalid");
			}else{
				email_status="Valid";
				System.out.println("Valid");
			}
			*/
			//JSONObject obj = new JSONObject(text);
			//System.out.println("obj : "+obj);
			//expireFlag = obj.getInt("expireFlag");
			//System.out.println(Integer.toString(expireFlag));
			
		} catch (Exception ex) {
			System.out.println("Error : "+ex);
		}
		finally {
		
			conn.disconnect();
			
			try {
				if(null !=in) {
					
				in.close();
				in=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(null !=reader) {
					reader.close();
					reader=null;
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conn =null;
		}
		return email_details;
	}

}
