package leadconverter.create.rule;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import leadconverter.doctiger.LogByFileWriter;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.json.JSONException;


public class CreateRuleEngine {

	public static void main(String[] args) throws IOException {
		 // TODO Auto-generated method stub
		//CreateRuleEngine.createRuleEngine("viki@gmail.com","June27F02");
	}
	public static String createRuleEngine(String logged_in_user,String project_name){
		String rule_engine_response=null;
		String rule_engine_creation_status=null;
		try{
			  String create_project_in_carrot_rule=ResourceBundle.getBundle("config").getString("create_project_in_carrot_rule");
			  String create_SFObect_for_project_in_carrot_rule=ResourceBundle.getBundle("config").getString("create_SFObect_for_project_in_carrot_rule");
			  String create_Fields_for_project_in_carrot_rule=ResourceBundle.getBundle("config").getString("create_Fields_for_project_in_carrot_rule");
			  String validate_Fields_for_project_in_carrot_rule=ResourceBundle.getBundle("config").getString("validate_Fields_for_project_in_carrot_rule");
			  String user_name=ResourceBundle.getBundle("config").getString("add_rule_engine_UserName");
			  
			  String file_path = ResourceBundle.getBundle("config").getString("rule_xls_file_path");
			  StringBuffer response_text = null;
			  String project_description="Lead Auto Converter Project Created by "+logged_in_user+" For Funnel "+project_name;
			  String rule_engine_name=project_name+"_RE";
			  String rule_engine_description="This Rule is based on Google Analytics and PhpList stattistics created for Project "+project_name;
			  
			  
			  Date date = new Date();
			  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			  String created_date= formatter.format(date);
			  System.out.println(created_date);
			  
			  JSONObject  create_carrot_rule_project_json_obj=null;
			  
			  JSONObject  create_SFObect_for_project_json_obj=null;
			  JSONArray   account_json_arr=null;
			  JSONObject  account_json_obj=null;
			  JSONObject  sfdc_selectdata_json_obj=null;
			  
			  JSONObject  create_Fields_for_project_json_obj=null;
			  JSONObject  webservices_json_obj=null;
			  JSONObject  external_data_json_obj=null;
			  JSONArray   url_json_arr=null;
			  JSONObject  url_json_obj=null;
			  JSONArray   output_field_json_arr=null;
			  JSONObject  output_field_json_obj=null;
			  JSONArray   file_data_json_arr=null;
			  JSONObject  file_data_json_obj=null;
			  
			  JSONObject  validate_Fields_for_project_json_obj=null;
			  JSONObject  transform_json_obj=null;
			  JSONArray   raw_data_json_arr=null;
			  JSONArray   transform_json_arr=null;
			  JSONObject  transform_file_data_json_obj=null;
			   
			  //Going to create Project in Carrot Rule
			              create_carrot_rule_project_json_obj=new JSONObject();
						  create_carrot_rule_project_json_obj.put("user_name", user_name);
						  create_carrot_rule_project_json_obj.put("created_date", created_date);
						  create_carrot_rule_project_json_obj.put("project_name", project_name);
						  create_carrot_rule_project_json_obj.put("Project_Description", project_description);
						  create_carrot_rule_project_json_obj.put("Rule_Engine", rule_engine_name);
						  create_carrot_rule_project_json_obj.put("Rule_Engine_Description", rule_engine_description);
			  rule_engine_response=callRuleEngineApi(create_project_in_carrot_rule,create_carrot_rule_project_json_obj);
			  System.out.println("Create project in carrot rule response : "+rule_engine_response);
			  if(rule_engine_response.equals("SUCCESSFULL")){
			  //Going to create SF Object for Project in Carrot Rule  
				  account_json_obj=new JSONObject();
				  account_json_obj.put("field", "id");
				  account_json_obj.put("type", "ID");
				  account_json_arr=new JSONArray();
				  account_json_arr.put(account_json_obj);
				  sfdc_selectdata_json_obj=new JSONObject();
				  sfdc_selectdata_json_obj.put("account", account_json_arr);
				  create_SFObect_for_project_json_obj=new JSONObject();
				  create_SFObect_for_project_json_obj.put("user_name", user_name);
				  create_SFObect_for_project_json_obj.put("project_name", project_name);
				  create_SFObect_for_project_json_obj.put("primary_key", project_name+created_date);
				  create_SFObect_for_project_json_obj.put("SFDC_SelectData", sfdc_selectdata_json_obj);
				  rule_engine_response=callRuleEngineApi(create_SFObect_for_project_in_carrot_rule,create_SFObect_for_project_json_obj);
				  System.out.println("Create SFObect for project in carrot rule response : "+rule_engine_response);
				  if(rule_engine_response.equals("Successfull")){
					//Going to create External Fields For Project in Carrot Rule
					  File file = new File(file_path);
					  BufferedReader br = new BufferedReader(new FileReader(file));
					  String inputLine;
					  response_text = new StringBuffer();
					  while ((inputLine = br.readLine()) != null) {
						  response_text.append(inputLine);
					  }
					  br.close();
				      //System.out.println(response_text);
				      
					  url_json_obj=new JSONObject();
					  url_json_obj.put("url", "");
					  url_json_obj.put("token", "");
					  url_json_obj.put("username", "");
					  url_json_obj.put("password", "");
					  url_json_arr=new JSONArray();
					  url_json_arr.put(url_json_obj);
					  
					  output_field_json_obj=new JSONObject();
					  output_field_json_obj.put("output_name", "");
					  output_field_json_obj.put("output_type", "");
					  output_field_json_obj.put("output_length", "");
					  output_field_json_arr=new JSONArray();
					  output_field_json_arr.put(output_field_json_obj);
					  
					  external_data_json_obj=new JSONObject();
					  external_data_json_obj.put("URL", url_json_arr);
					  external_data_json_obj.put("output_field",output_field_json_arr);
					  
					  webservices_json_obj=new JSONObject();
					  webservices_json_obj.put("EXTERNAL_DATA", external_data_json_obj);
					  
					  file_data_json_obj=new JSONObject();
					  file_data_json_obj.put("filename", "LeadAutoConverterRuleEngineFieldsExcelBase64.xls");
					  file_data_json_obj.put("filedata", response_text);
					  file_data_json_arr=new JSONArray();
					  file_data_json_arr.put(file_data_json_obj);
					  
					  
					  
					  create_Fields_for_project_json_obj=new JSONObject();
					  create_Fields_for_project_json_obj.put("user_name", user_name);
					  create_Fields_for_project_json_obj.put("project_name", project_name);
					  create_Fields_for_project_json_obj.put("primary_key", project_name+created_date);
					  create_Fields_for_project_json_obj.put("WebServices", webservices_json_obj);
					  create_Fields_for_project_json_obj.put("File_Data", file_data_json_arr);
					  
					  rule_engine_response=callRuleEngineApi(create_Fields_for_project_in_carrot_rule,create_Fields_for_project_json_obj);
					  
					//Going to Validate External Fields For Project in Carrot Rule
					  raw_data_json_arr=new JSONArray();
					  transform_json_arr=new JSONArray();
					  transform_json_obj=new JSONObject();
						  
					  transform_file_data_json_obj=new JSONObject();
					  validate_Fields_for_project_json_obj=new JSONObject();
						
						
					  JSONObject externaljson = new JSONObject(rule_engine_response);
					  System.out.println("Create Fields for project in carrot rule response : "+rule_engine_response);
					  JSONObject input_name_json_obj = null;

					  JSONObject messageexternaljson = externaljson.getJSONObject("Message");
					  JSONArray exceldataarray = messageexternaljson.getJSONArray("EXCEL_DATA");
						for (int i = 0; i < exceldataarray.length(); i++) {
							input_name_json_obj = new JSONObject();
							JSONObject fielddata = exceldataarray.getJSONObject(i);
							String fieldvalue = fielddata.getString("field");
							input_name_json_obj.put("input_name", fieldvalue);
							input_name_json_obj.put("input_type", "");
							raw_data_json_arr.put(input_name_json_obj);
						}
						transform_json_arr.put(new JSONObject());
						transform_json_obj.put("Raw_Data", raw_data_json_arr);
						transform_json_obj.put("Transform", transform_json_arr);
						
						validate_Fields_for_project_json_obj.put("user_name", user_name);
						validate_Fields_for_project_json_obj.put("project_name", project_name);
						validate_Fields_for_project_json_obj.put("TRANSFORM", transform_json_obj);
						validate_Fields_for_project_json_obj.put("Transform_File_Data", transform_file_data_json_obj);
						rule_engine_response=callRuleEngineApi(validate_Fields_for_project_in_carrot_rule,validate_Fields_for_project_json_obj);
						System.out.println("Validate Fields for project in carrot rule response : "+rule_engine_response);
						rule_engine_creation_status="True";
				  }else{
					  System.out.println("SFDC data is not set!");
					  rule_engine_creation_status="False";
				  }
			  }else{
				  System.out.println("Rule project is not created!");
				  rule_engine_creation_status="False";
			  }
			  
			  
		      //createRuleEngine(add_rule_engine_URL,final_rule_inputfields_data);
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Error : "+ex.getMessage());
		}
		return rule_engine_creation_status;
	}
	
	public static String createRule(JSONObject rule_final_json_obj){
		String user_name=ResourceBundle.getBundle("config").getString("add_rule_engine_UserName");
		String add_rule_api=ResourceBundle.getBundle("config").getString("add_rule_api");
		String response =null;
		try {
			response =callRuleEngineApi(add_rule_api,rule_final_json_obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	public static String fireRule(String fire_rule_api,JSONObject rule_final_json_obj){
		String response =null;
		try {
			response =callRuleEngineApi(fire_rule_api,rule_final_json_obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	public static String getAllRules(String username,String projectname,String ruleenginename){
			//String username="carrotrule@xyz.com";
			//String projectname="June27F01";
			//String ruleenginename="June27F01_RE";
			String get_all_rules_in_rule_engine = ResourceBundle.getBundle("config").getString("get_all_rules_in_rule_engine");
			String get_all_rules_in_rule_engine_parameter = "?username="+username+"&projectname="+projectname+"&ruleenginename="+ruleenginename;
			String response=null;
			try {
				response = callRuleEngineGetApi(get_all_rules_in_rule_engine,get_all_rules_in_rule_engine_parameter);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
	}
	
	public static String callRuleEngineApi(String rule_engine_url, JSONObject inpu_json_object) throws IOException, JSONException {
		StringBuffer response = null;
		int responseCode = 0;
		try {
			URL url = new URL(rule_engine_url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(inpu_json_object.toString());
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
			//LogByFileWriter.logger_info("CreateRuleEngine : " + "responseCode : "+responseCode+"\n"+"ResponseBody : "+response);
		} catch (Exception e) {
			System.out.println("Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
			//LogByFileWriter.logger_info("CreateRuleEngine : " + "Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
		}
		return response.toString();
		//return String.valueOf(responseCode);
	}
	public static String callRuleEngineGetApi(String rule_engine_url, String rule_engine_url_parameter) throws IOException, JSONException {
		StringBuffer response = null;
		int responseCode = 0;
		try {
			URL url = new URL(rule_engine_url+rule_engine_url_parameter);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			/*
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(inpu_json_object.toString());
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
			//System.out.println("responseCode : "+responseCode+"\n"+"ResponseBody : "+response);
			//LogByFileWriter.logger_info("CreateRuleEngine : " + "responseCode : "+responseCode+"\n"+"ResponseBody : "+response);
		} catch (Exception e) {
			System.out.println("Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
			//LogByFileWriter.logger_info("CreateRuleEngine : " + "Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
		}
		return response.toString();
		//return String.valueOf(responseCode);
	}
}
