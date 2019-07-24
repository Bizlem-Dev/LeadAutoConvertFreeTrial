package leadconverter.create.rule;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import leadconverter.doctiger.LogByFileWriter;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.json.JSONException;


public class CreateRuleEngineBackUp {

	public static void main(String[] args) throws IOException {
		 // TODO Auto-generated method stub
		try{
			  String add_rule_engine_URL=ResourceBundle.getBundle("config").getString("add_rule_engine_URL");
			  String user_name=ResourceBundle.getBundle("config").getString("add_rule_engine_UserName");
			  String project_name="Bizlem_Integral";
			  String ruleengine_name="Bizlem_Integral_rule";
			  
			  
			  String fileName = ResourceBundle.getBundle("config").getString("add_rule_engine_headings_fileName");
			  
			  ClassLoader classLoader = ClassLoader.getSystemClassLoader();
			  File file = new File(classLoader.getResource(fileName).getFile());
			  //File is found
			  System.out.println("File Found : " + file.exists());
			  LogByFileWriter.logger_info("CreateRuleEngine : File Found : " + file.exists());
			  //Read File Content
			  BufferedReader br = new BufferedReader(new FileReader(file));
			  StringBuffer response = null;
			  String inputLine;
			  response = new StringBuffer();
			  while ((inputLine = br.readLine()) != null) {
					response.append(inputLine);
			  }
			  br.close();
			  System.out.println(response);
			  LogByFileWriter.logger_info("CreateRuleEngine : " + response);
			  /*
			  JSONParser parser = new JSONParser();
			  //Object object = parser.parse(new FileReader("E:\\bizlem\\LeadAutoConverter\\sample.txt"));
		      Object object = parser.parse(response.toString());
		      //convert Object to JSONObject
		      JSONArray rule_inputfields_data = (JSONArray)object;
		      JSONObject final_rule_inputfields_data=new JSONObject();
		                 final_rule_inputfields_data.put("user_name", user_name);
		                 final_rule_inputfields_data.put("project_name", project_name);
		                 final_rule_inputfields_data.put("ruleengine_name", ruleengine_name);
		                 final_rule_inputfields_data.put("data", rule_inputfields_data);
		      */
		      //System.out.println(final_rule_inputfields_data);
		      //createRuleEngine(add_rule_engine_URL,final_rule_inputfields_data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static String createRuleEngine(String ruleengine_name){
		String rule_engine_response=null;
		try{
			  String add_rule_engine_URL=ResourceBundle.getBundle("config").getString("add_rule_engine_URL");
			  String user_name=ResourceBundle.getBundle("config").getString("add_rule_engine_UserName");
			  String fileName = ResourceBundle.getBundle("config").getString("add_rule_engine_headings_fileName");
			  String project_name=ResourceBundle.getBundle("config").getString("add_rule_engine_projectName");
			  StringBuffer response_text = null;
			  try{
				  File file = new File("/home/ubuntu/leadAutoConvertRuleJson/"+fileName);
				  BufferedReader br = new BufferedReader(new FileReader(file));
				  String inputLine;
				  response_text = new StringBuffer();
				  while ((inputLine = br.readLine()) != null) {
					  response_text.append(inputLine);
				  }
				  br.close();
            }catch(Exception ex){
          	  System.out.println("Inside Catch : "+ex.getMessage());
          	  LogByFileWriter.logger_info("CreateRuleEngine : " + "Inside Catch : "+ex.getMessage());
            }
			  JSONArray rule_inputfields_data = new JSONArray(response_text.toString());
			  JSONObject final_rule_inputfields_data=new JSONObject();
		                 final_rule_inputfields_data.put("user_name", user_name);
		                 final_rule_inputfields_data.put("project_name", project_name);
		                 final_rule_inputfields_data.put("ruleengine_name", ruleengine_name);
		                 final_rule_inputfields_data.put("data", rule_inputfields_data);
		      System.out.println(final_rule_inputfields_data);
		      LogByFileWriter.logger_info("CreateRuleEngine : " + final_rule_inputfields_data);
		      rule_engine_response=callRuleEngineApi(add_rule_engine_URL,final_rule_inputfields_data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rule_engine_response;
	}
	public static String callRuleEngineApi(String rule_engine_url, JSONObject rule_heading_fields_json) throws IOException, JSONException {
		StringBuffer response = null;
		int responseCode = 0;

		try {
			URL url = new URL(rule_engine_url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");

			con.setRequestProperty("Content-Type", "application/json");

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(rule_heading_fields_json.toString());
			wr.flush();
			wr.close();

			responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println("responseCode : "+responseCode+"\n"+"ResponseBody : "+response);
			LogByFileWriter.logger_info("CreateRuleEngine : " + "responseCode : "+responseCode+"\n"+"ResponseBody : "+response);
		} catch (Exception e) {
			System.out.println("Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
			LogByFileWriter.logger_info("CreateRuleEngine : " + "Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
		}
		//return response.toString();
		return String.valueOf(responseCode);

	}

}
