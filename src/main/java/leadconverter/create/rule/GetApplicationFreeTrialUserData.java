package leadconverter.create.rule;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class GetApplicationFreeTrialUserData {

	public static void main(String[] args) throws JSONException{
		// TODO Auto-generated method stub
		
		/*
		JSONObject final_rule_inputfields_data=new JSONObject();
        final_rule_inputfields_data.put("username", "akhilesh@bizlem.com");
        System.out.println(final_rule_inputfields_data);
        */
		String[] application_data_arr=GetApplicationFreeTrialUserData.getApplicationData("akhilesh0308@bizlem.com").split(":");
		String freetrial=application_data_arr[0];
		String invoice_process_count=application_data_arr[1];
		System.out.println("application_data : "+freetrial+"\n"+"invoice_process_count : "+invoice_process_count);
    }
	public static String getApplicationData(String username){
		String free_trail_status=null;
		String count=null;
		try{
			//MongoApplicationDataDAO madao=new MongoApplicationDataDAO();
			//String trialuser_data_Api=ResourceBundle.getBundle("config").getString("trialuser_data_Api");
			String trial_single_user_data_Api=ResourceBundle.getBundle("config").getString("trial_single_user_data_Api");
			String user_email="akhilesh@bizlem.com";
			
			String rule_engine_response=callRuleEngineApi(trial_single_user_data_Api,username);
			if(rule_engine_response.length()>0){
				JSONObject application_data_response_json=new JSONObject(rule_engine_response);
				count=application_data_response_json.getString("count");
				String freetrial=application_data_response_json.getString("freetrial");
				if(freetrial.equals("0")){
					free_trail_status="YES";
					MongoApplicationDataDAO.insertApplicationData(application_data_response_json);
				}
				System.out.println("freetrial : "+freetrial+"\n"+"invoice process count : "+count);
			}else{
				free_trail_status="NO";
				count="0";
			}
			System.out.println("free_trail_status : "+free_trail_status);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return free_trail_status+":"+count;
	}
	public static String callRuleEngineApi(String rule_engine_url, String username){
		StringBuffer response = null;
		int responseCode = 0;

		try {
			URL url = new URL(rule_engine_url+username);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			con.setRequestProperty("Content-Type", "application/json");

			con.setDoOutput(true);
			/*
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(rule_heading_fields_json.toString());
			wr.flush();
			wr.close();
			*/

			responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println("responseCode : "+responseCode+"\n"+"ResponseBody : "+response);
		} catch (Exception e) {
			System.out.println("Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
		}
		  return response.toString();
		//return String.valueOf(responseCode);
	}

}
