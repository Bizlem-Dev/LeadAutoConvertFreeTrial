package leadconverter.rulengine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import javax.servlet.ServletException;

import leadconverter.mongo.Test1;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONObject;
import org.json.JSONException;

public class callRuleEng {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			  JSONObject jsonObject=new JSONObject();
			    /*
			    jsonObject.put("Open",2);
				jsonObject.put("Click",2);
				jsonObject.put("Last_Click","2");
				jsonObject.put("Bounce","No");
				jsonObject.put("Session_time","715");
				jsonObject.put("Last_Session_Time","725");
				jsonObject.put("Pricing_URl","745");
				jsonObject.put("Last_Pricing_URl","750");
				jsonObject.put("Own_Visit","0");
				jsonObject.put("Last_Visit","0");
				jsonObject.put("Own_Session_time","0");
				jsonObject.put("Free_Trial","No");
				
				jsonObject.put("funnelName","funnel27003");
				jsonObject.put("subFunnelName","Explore");
				jsonObject.put("subscriber_email","Sreerupa.Sengupta@3i-infotech.com");
				jsonObject.put("SubscriberId","7109");//7113
				jsonObject.put("ListId","754");
				jsonObject.put("CampaignId","788");
				jsonObject.put("Category","Unknown");
				jsonObject.put("CreatedBy","viki_gmail.com");
				jsonObject.put("OutPutTemp","Inform");
				*/
				jsonObject.put("Campaign_Id","856");
				jsonObject.put("List_Id","856");
				jsonObject.put("Category","Explore");
				jsonObject.put("Open",1);
				jsonObject.put("Click",15);
				jsonObject.put("Bounce","0");
				jsonObject.put("Session_time","40");
				jsonObject.put("Total_Session_time","90");
				jsonObject.put("URl","/aboutUs.html");
				jsonObject.put("Time_Spend_On_URl","70.0");
				jsonObject.put("Free_Trial","YES");
				
				jsonObject.put("funnelName","funnelNodeName");
				jsonObject.put("subFunnelName","subFunnelNodeName");
				jsonObject.put("subscriber_email","subscriber_email");
				jsonObject.put("SubscriberId","subscriber_userid");//7113
				jsonObject.put("ListId","List_Id");
				jsonObject.put("CampaignId","campaign_id");
				//jsonObject.put("Category",subFunnelNodeName);
				jsonObject.put("CreatedBy","viki_gmail.com");
				jsonObject.put("OPTemp","Inform");
				//OPTemp  jsonObject.put("OutPutTemp","Inform");
				
				JSONObject rule_response_obj=new JSONObject();
				rule_response_obj.put("CreatedBy","viki_gmail.com");
				rule_response_obj.put("Category","Explore");
				rule_response_obj.put("Total_Session_time","130");
				rule_response_obj.put("OPTemp","Inform");
				rule_response_obj.put("Free_Trial","YES");

				rule_response_obj.put("funnelName","11April Shiipping");
				rule_response_obj.put("Click","10");
				rule_response_obj.put("subscriber_email","radhika.joshi@bizlem.com");
				rule_response_obj.put("List_Id","784");
				rule_response_obj.put("URl","/industries.html");
				rule_response_obj.put("Open","2");
				rule_response_obj.put("subFunnelName","Explore");

				rule_response_obj.put("Session_time","16");
				rule_response_obj.put("ListId","784");
				rule_response_obj.put("CampaignId","856");
				rule_response_obj.put("SubscriberId","7119");
				rule_response_obj.put("Time_Spend_On_URl","31.0");
				rule_response_obj.put("Campaign_Id","856");
				rule_response_obj.put("Bounce","0");
				rule_response_obj.put("Output","Connect");
				
				String rulr_engine_response=rule_response_obj.toString();
				//funnel27003
				//Explore 
				//
				//http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConverterNewRule_RuleUri/fire
				//String rulr_engine_response=urlconnect("http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConverterNewRule_RuleUri/fire",jsonObject);
				//String rulr_engine_response=urlconnect("http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConvert_LACRules/fire",jsonObject);
				//System.out.println(rulr_engine_response);
				JSONObject ruleEngineResponseJsonObject=new JSONObject(rulr_engine_response);
				String leadStatus=ruleEngineResponseJsonObject.getString("Output");
				System.out.println("leadStatus : "+leadStatus);
				
				String url=ResourceBundle.getBundle("config").getString("list_subscriber_move_rulengine");
				//sendPostRequestToManageSubscribers(url,ruleEngineResponseJsonObject.toString());
				RulEngineMongoDAO mdao=new RulEngineMongoDAO();
				//System.out.println(mdao.findSubscribersBasedOncampaignNameAndDimensionTest("", "888", "ExpCamp1", "akhilesh@bizlem.com"));
				System.out.println(mdao.findSubscribersBasedOncampaignNameAndDimension2("", "888", "ExpCamp1", "akhilesh@bizlem.com"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static String sendPostRequestToManageSubscribers(String callurl,
			String urlParameters)
			throws ServletException, IOException {

		//out.println("urlParameters :" + urlParameters);
		// URL url = new URL(callurl + urlParameters.replace("\\", ""));
		URL url = new URL(callurl);
		//out.println("Url :" + url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		//
		OutputStream writer = conn.getOutputStream();

		writer.write(urlParameters.getBytes());
		// out.println("Writer Url : "+writer);
		int responseCode = conn.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);
		StringBuffer buffer = new StringBuffer();
		//
		if (responseCode == 200) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			//
			System.out.println(buffer.toString());
		} else {
			System.out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}
	private static String urlconnect(String urlstr, JSONObject json) throws IOException, JSONException {
		JSONObject jsonObject = null;
		StringBuffer response = null;

		try {

			int responseCode = 0;
			String urlParameters = "";

			URL url = new URL(urlstr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");

			con.setRequestProperty("Content-Type", "application/json");

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			// wr.writeBytes(json.toString());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
			writer.write(json.toString());
			writer.close();
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

			System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toString();

	}
}
