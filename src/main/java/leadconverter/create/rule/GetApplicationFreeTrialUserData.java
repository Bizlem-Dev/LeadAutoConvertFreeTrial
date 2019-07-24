package leadconverter.create.rule;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import leadconverter.doctiger.LogByFileWriter;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class GetApplicationFreeTrialUserData {

	public static void main(String[] args) throws JSONException{
		// TODO Auto-generated method stub
		
		/*
		JSONObject final_rule_inputfields_data=new JSONObject();
        final_rule_inputfields_data.put("username", "akhilesh@bizlem.com");
        System.out.println(final_rule_inputfields_data);
        LogByFileWriter.logger_info("GetApplicationFreeTrialUserData : " + response);
        */
		String application_data_arr=GetApplicationFreeTrialUserData.getApplicationData("akhilesh02@bizlem.com");
		JSONObject application_data_response_final_json=new JSONObject(application_data_arr);
		System.out.println("application_data_response_final_json : "+application_data_response_final_json.length());
		LogByFileWriter.logger_info("GetApplicationFreeTrialUserData : " + "application_data_response_final_json : "+application_data_response_final_json.length());
		/*
		String[] application_data_arr=GetApplicationFreeTrialUserData.getApplicationData("akhilesh@bizlem.com").split(":");
		String freetrial=application_data_arr[0];
		String invoice_process_count=application_data_arr[1];
		System.out.println("application_data : "+freetrial+"\n"+"invoice_process_count : "+invoice_process_count);
		LogByFileWriter.logger_info("GetApplicationFreeTrialUserData : " + "application_data : "+freetrial+"\n"+"invoice_process_count : "+invoice_process_count);
		*/
    }
	public static String getApplicationData(String username){
		String free_trail_status=null;
		String count=null;
		String productType=null;
		String expireday=null;
		String joinedDate=null;
		String freetrial=null;
		String expireFlag=null;
		JSONObject application_data_response_final_json=new JSONObject();
		
		try{
			//MongoApplicationDataDAO madao=new MongoApplicationDataDAO();
			//String trialuser_data_Api=ResourceBundle.getBundle("config").getString("trialuser_data_Api");
			String trial_single_user_data_Api=ResourceBundle.getBundle("config").getString("trial_single_user_data_Api");
			String user_email="akhilesh@bizlem.com";
			
			String rule_engine_response=callRuleEngineApi(trial_single_user_data_Api,username);
			if(rule_engine_response.length()>0){
				JSONObject application_data_response_json=new JSONObject(rule_engine_response);
				count=application_data_response_json.getString("count");
				productType=application_data_response_json.getString("productType");
				expireday=application_data_response_json.getString("expireday");
				joinedDate=application_data_response_json.getString("joinedDate");
				freetrial=application_data_response_json.getString("freetrial");
				expireFlag=application_data_response_json.getString("expireFlag");
				
				application_data_response_final_json.put("Free_Trial_Taken",freetrial );
				application_data_response_final_json.put("Free_Trial_Product_Name",productType );
				application_data_response_final_json.put("Free_Trial_Expire_Date",expireday );
				application_data_response_final_json.put("Free_Trial_Joined_Date",joinedDate );
				application_data_response_final_json.put("Free_Trial_Expire_Flag",freetrial );
				application_data_response_final_json.put("Free_Trial_Used_Count",expireFlag );
				
				if(freetrial.equals("0")){
					free_trail_status="YES";
					//MongoApplicationDataDAO.insertApplicationData(application_data_response_json);
				}
				//System.out.println("freetrial : "+freetrial+"\n"+"invoice process count : "+count);
				//LogByFileWriter.logger_info("GetApplicationFreeTrialUserData : " + "freetrial : "+freetrial+"\n"+"invoice process count : "+count);
			}else{
				free_trail_status="NO";
				count="0";
			}
			//System.out.println("free_trail_status : "+free_trail_status);
			//LogByFileWriter.logger_info("GetApplicationFreeTrialUserData : " + "free_trail_status : "+free_trail_status);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return free_trail_status+":"+count;
		return application_data_response_final_json.toString();
	}
	public static String callRuleEngineApi(String rule_engine_url, String username){
		String response_str= null;
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
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			if(responseCode==200){
				response_str=response.toString();
			}else{
				response_str="";
			}
			//System.out.println("responseCode : "+responseCode+"\n"+"ResponseBody : "+response_str);
			//LogByFileWriter.logger_info("GetApplicationFreeTrialUserData : " + "responseCode : "+responseCode+"\n"+"ResponseBody : "+response_str);
		} catch (Exception e) {
			System.out.println("Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
			LogByFileWriter.logger_info("GetApplicationFreeTrialUserData : " + "Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
		}
		  return response_str.toString();
		//return String.valueOf(responseCode);
	}

}
