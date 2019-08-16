package leadconverter.mongo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONObject;
import org.json.JSONException;

//import leadconverter.servlet.LEAD_CONVERTER_NodeAdd_Email;

public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Test1 clob=new Test1();
		//String campaignaddurl = ResourceBundle.getBundle("config").getString("Campaign_Add_Url_New");
		String campaignaddurl = ResourceBundle.getBundle("config").getString("phplist_api_campaignUpdate");
		//campaignUpdate
		//String 
		String campaignaddapiurlparameters = "id=580&subject=Subject from http Post";
		//String campaignaddapiurlparameters = "subject=From code new api&fromfield=Akhilesh&replyto=akhiles"+
		//"&message=Akhiles masage&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo=&rsstemplate=&owner=1&htmlformatted=";
		/*
		String campaignaddapiurlparameters1 = "?id="+id+"&subject=" + subject + "&fromfield=" + fromfield + "&replyto="
				+ replyto
				+ "&message="+body+"&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo="
				+ embargo+"&rsstemplate=&owner=1&htmlformatted=";
		
		String campaignaddapiurlparameters2 = "?id="+id+"&subject=" + subject + "&fromfield=" + fromfield + "&replyto="
				+ replyto
				+ "&message="+body+"&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo="
				+ embargo+"&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
		*/
		try {
			//String campaignlisturl = ResourceBundle.getBundle("config").getString("listCampaignAdd");
			//String campaignlisturl = ResourceBundle.getBundle("config").getString("processqueue");
			//String campaignparameter = "?listid=" + "680" + "&campid=" + "650";
			//String campaignparameter = "?campid=" + "650";
			/*
			String campaignaddurl2 = ResourceBundle.getBundle("config").getString("embargoupdateurl");
			String campaignaddapiurlparameters2 = "id="+"679"+"&embargo=" + "2019-02-25 12:30:00 PM";
			String campaignaddurl3 = ResourceBundle.getBundle("config").getString("campaignStatusUpdate");
			String campaignaddapiurlparameters3 = "?id=" + "679" + "&status=" + "submitted";
			
			String campaignlistresponse = sendpostdata(campaignaddurl3, campaignaddapiurlparameters3.replace(" ", "%20"))
					.replace("<pre>", "");
			System.out.println("campaignlistresponse :" + campaignlistresponse);
			*/
			
			JSONObject jsonObject=new JSONObject();
			    jsonObject.put("Open","2");
				jsonObject.put("Click","2");
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
				jsonObject.put("Name","Akhilesh");
				jsonObject.put("SurName","");
				String rulr_engine_response=urlconnect("http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConvert_LACRules/fire",jsonObject);
				System.out.println(rulr_engine_response);
				JSONObject ruleEngineResponseJsonObject=new JSONObject(rulr_engine_response);
				String leadStatus=ruleEngineResponseJsonObject.getString("Output");
				System.out.println("leadStatus : "+leadStatus);
				
			
			
			
			//String campaignresponse = clob.sendpostdata(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""))
				//	.replace("<pre>", "");
			//System.out.println("campaignresponse :" + campaignresponse);
			//sendHttpPostData(campaignaddurl,campaignaddapiurlparameters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

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
	public static String sendHttpPostData(String campaignaddurl,String campaignaddapiurlparameters) throws Exception {
        URL url = new URL(campaignaddurl);
        /*
        Map<String,Object> params = new LinkedHashMap<String,Object>();
        params.put("name", "Freddie the Fish");
        params.put("email", "fishie@seamail.example.com");
        params.put("reply_to_thread", 10394);
        params.put("message", "Shark attacks in Botany Bay have gotten out of control. We need more defensive dolphins to protect the schools here, but Mayor Porpoise is too busy stuffing his snout with lobsters. He's so shellfish.");

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        */
        //System.out.print("postData.toString() " +postData.toString());
        
        
        byte[] postDataBytes = campaignaddapiurlparameters.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        //for (int c; (c = in.read()) >= 0;)
        //  System.out.print((char)c);
       
        StringBuffer buffer = new StringBuffer();
        for (int c; (c = in.read()) >= 0;)
        	buffer.append((char)c);
        System.out.println("response : "+buffer.toString());
        return buffer.toString();
       
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
