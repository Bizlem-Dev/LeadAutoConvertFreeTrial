package leadconverter.rulengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import javax.servlet.ServletException;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;



public class Test {

	public static void main(String[] args) throws ServletException, IOException, JSONException {
		// TODO Auto-generated method stub
		  /*
		  RulEngineMongoDAO rdao=new RulEngineMongoDAO();
		  //JSONArray mainJsonArray=new JSONArray(rdao.subscribersViewBasedOnFunnelData("url",campaignId,subscriberId).toString());
		  JSONArray mainJsonArray=new JSONArray(rdao.subscribersViewBasedOnFunnelData("subscribers","499","2089").toString());//6985 2089
		  
		  System.out.println("mainJsonArray length : "+mainJsonArray.length());
		  JSONObject subscriber_campaign_jsonObject=(JSONObject) mainJsonArray.get(0);
		  JSONArray subscriber_campaign_jsonArray=subscriber_campaign_jsonObject.getJSONArray("subscriber_campaign");
		  System.out.println("subscriber_campaign_jsonArray : "+subscriber_campaign_jsonArray);
		  System.out.println("subscriber_campaign_jsonArray length : "+subscriber_campaign_jsonArray.length());
		  */
		  String id="945";
		  String campaigngetbyid_url=ResourceBundle.getBundle("config").getString("campaigngetbyid");
		  String campaigngetbyid_url_parameter="?id="+id;
		  String response=sendpostdata(campaigngetbyid_url,campaigngetbyid_url_parameter);
		  JSONObject response_data_json_obj=(JSONObject) new JSONObject(response.replace("<pre>", "")).get("data");
		  String status=response_data_json_obj.getString("status");
		  
		  if(status.equals("sent") || status.equals("submitted")){
			  System.out.println("No need to Anything : "+status);
		  }else{
			  System.out.println("Do your stuff here "+status);
	      }
		  //ResourceBundle.

	}
	
	public static String sendpostdata(String callurl, String urlParameters)
			throws ServletException, IOException {

				URL url = new URL(callurl + urlParameters);
		System.out.println("Url :" + url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		// 
		OutputStream writer = conn.getOutputStream();

		writer.write(urlParameters.getBytes());
		int responseCode = conn.getResponseCode();
		StringBuffer buffer = new StringBuffer();
		//
		System.out.println("responseCode :" + responseCode);
		if (responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
		} else {
			System.out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();
	}

}
