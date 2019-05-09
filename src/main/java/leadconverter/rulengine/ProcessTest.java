package leadconverter.rulengine;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;

import leadconverter.mongo.MongoDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a sample file to launch a process.
 */
public class ProcessTest {
	RulEngineMongoDAO rdao=new RulEngineMongoDAO();
	public  void startprocess(JSONArray array, String campaigncreate_by) {
		try {
				List<Campaign> campaignList = createCampagign(array);
				//System.out.println("  campaignList   :"+campaignList);
				//callRuleEng calrulengine=new callRuleEng();
				for (Campaign campaign : campaignList) {
					      //System.out.println("Subscriber id -> " + campaign.getSubscriber().getSubsriberId() + "\n"
							           //  + campaign.getSubscriber().toString());
					      String campaignId=Integer.toString(campaign.getCampaignId());
					      String subscriberId=campaign.getSubscriber().getSubsriberId();
					      
					      System.out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId);
					      
					      
					      RulEngineMongoDAO rdao=new RulEngineMongoDAO();
						  JSONArray mainJsonArray=new JSONArray(rdao.subscribersViewBasedOnFunnelData("url",campaignId,subscriberId).toString());
						  System.out.println("mainJsonArray length : "+mainJsonArray.length());
						  JSONObject subscriber_campaign_jsonObject=(JSONObject) mainJsonArray.get(0);
						  JSONArray subscriber_campaign_jsonArray=subscriber_campaign_jsonObject.getJSONArray("subscriber_campaign");
						  //System.out.println("subscriber_campaign_jsonArray : "+subscriber_campaign_jsonArray);
						  System.out.println("subscriber_campaign_jsonArray length : "+subscriber_campaign_jsonArray.length());
						  //JSONArray jsonArray = (JSONArray) jsonObject.get("Data");
						    int subscriber_click_count=0;
							String campaignclickstatistics_firstclick="";
							String campaignclickstatistics_latestclick="";
							int sent=0;
							String campaign_id="";
					        String Sling_Subject="";
							int clicks=0;
							int viewed=0;
							String open_rate="";
							String click_rate="";
							String subFunnelNodeName="";
							String urlclickstatistics_url="";
							String urlclickstatistics_clicked="";
							String subscriber_viewed="";
							String funnelNodeName="";
							String subscriber_userid="";
							String List_Id="";
							String subscriber_email="";
							
							String ga_campaign = "";
							String pagePath = "";
					        String sessionCount = "";
					        String dimension2 = "";
					        String medium = "";
					        String sessiondurationBucket = "";
					        String pageTitle = "";
					        String timeOnPage = "";
					        String bounces = "";
					        String source = "";
					        
							
						  
							for(int d=0;d<subscriber_campaign_jsonArray.length();d++){
								JSONObject subscriberObject = subscriber_campaign_jsonArray.getJSONObject(d);
								//System.out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId);
								System.out.println("subscriberObject : "+subscriberObject);
								funnelNodeName         =(String) subscriberObject.get("funnelNodeName");
								subFunnelNodeName      =(String) subscriberObject.get("subFunnelNodeName");
								campaign_id            =(String) subscriberObject.get("campaign_id");
								Sling_Subject          =(String) subscriberObject.get("Sling_Subject");
								clicks                 =(int) subscriberObject.getInt("clicks");
								viewed                 =(int) subscriberObject.getInt("viewed");
								open_rate              =(String) subscriberObject.get("open_rate");
								click_rate             =(String) subscriberObject.get("click_rate");
								urlclickstatistics_url =(String) subscriberObject.get("urlclickstatistics_url");
								urlclickstatistics_clicked=(String) subscriberObject.get("urlclickstatistics_clicked");
								subscriber_viewed      =(String) subscriberObject.get("subscriber_viewed");
								subscriber_userid      =(String) subscriberObject.get("subscriber_userid");
								List_Id                =(String) subscriberObject.get("List_Id");
								subscriber_email       =(String) subscriberObject.get("subscriber_email");
								
								MongoDAO mdao=new MongoDAO();
								org.apache.sling.commons.json.JSONArray campaignJsonArr=rdao.findSubscribersBasedOncampaignNameAndDimension2("","797","hi","harshita.indapurkar@bizlem.com");
								//org.apache.sling.commons.json.JSONArray campaignJsonArr=rdao.findSubscribersBasedOncampaignNameAndDimension2("",campaign_id,Sling_Subject,subscriber_email);
								if(campaignJsonArr.length()!=0){
									for(int i=0;i<campaignJsonArr.length();i++){
										JSONObject json_obj=new JSONObject(campaignJsonArr.get(i).toString());
										System.out.println("pagePath : "+json_obj.getString("pagePath"));
										
										    ga_campaign = json_obj.getString("campaign");
											pagePath = json_obj.getString("pagePath");
											sessionCount = json_obj.getString("sessionCount");
											dimension2 = json_obj.getString("dimension2");
											medium = json_obj.getString("medium");
											sessiondurationBucket = json_obj.getString("sessiondurationBucket");
											pageTitle = json_obj.getString("pageTitle");
											timeOnPage = json_obj.getString("timeOnPage");
											bounces = json_obj.getString("bounces");
											source = json_obj.getString("source");
											
											JSONObject jsonObject=new JSONObject();
										    jsonObject.put("Open",viewed);
											jsonObject.put("Click",clicks);
											jsonObject.put("Last_Click","2");
											jsonObject.put("Bounce","No");
											jsonObject.put("Session_time","715");
											jsonObject.put("Last_Session_Time","725");
											jsonObject.put("Pricing_URl","745");
											jsonObject.put("Last_Pricing_URl",timeOnPage);
											jsonObject.put("Own_Visit","0");
											jsonObject.put("Last_Visit","0");
											jsonObject.put("Own_Session_time","0");
											jsonObject.put("Free_Trial","No");
											
											jsonObject.put("funnelName",funnelNodeName);
											jsonObject.put("subFunnelName",subFunnelNodeName);
											jsonObject.put("subscriber_email",subscriber_email);
											jsonObject.put("SubscriberId",subscriber_userid);//7113
											jsonObject.put("ListId",List_Id);
											jsonObject.put("CampaignId",campaign_id);
											jsonObject.put("Category",subFunnelNodeName);
											jsonObject.put("CreatedBy",campaigncreate_by);
											jsonObject.put("OutPutTemp","Inform");
											
											//String rulr_engine_response=urlconnect("http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConvert_LACRules/fire",jsonObject);
											String rulr_engine_response=urlconnect("http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConvertRules_RulesLAC/fire",jsonObject);
											//http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConvertRules_RulesLAC/fire
											//System.out.println(rulr_engine_response);
											JSONObject ruleEngineResponseJsonObject=new JSONObject(rulr_engine_response);
											if(ruleEngineResponseJsonObject.has("Output")){
												String leadStatus=ruleEngineResponseJsonObject.getString("Output");
												System.out.println("leadStatus : "+leadStatus);
												System.out.println("leadStatus : "+leadStatus);
												String url=ResourceBundle.getBundle("config").getString("list_subscriber_move_rulengine");
												sendPostRequestToManageSubscribers(url,ruleEngineResponseJsonObject.toString());
											}else{
												System.out.println("I dont found any OutPut From Rule Engine");
											}
									}
								}else{
									        ga_campaign = "";
											pagePath = "";
											sessionCount =""; 
											dimension2 = "";
											medium = "";
											sessiondurationBucket = "";
											pageTitle = "";
											timeOnPage = "";
											bounces = "";
											source = "";
											
											JSONObject jsonObject=new JSONObject();
										    jsonObject.put("Open",viewed);
											jsonObject.put("Click",clicks);
											jsonObject.put("Last_Click","2");
											jsonObject.put("Bounce",bounces);
											jsonObject.put("Session_time","715");
											jsonObject.put("Last_Session_Time","725");
											jsonObject.put("Pricing_URl","745");
											jsonObject.put("Last_Pricing_URl",timeOnPage);
											jsonObject.put("Own_Visit","0");
											jsonObject.put("Last_Visit","0");
											jsonObject.put("Own_Session_time","0");
											jsonObject.put("Free_Trial","No");
											
											jsonObject.put("funnelName",funnelNodeName);
											jsonObject.put("subFunnelName",subFunnelNodeName);
											jsonObject.put("subscriber_email",subscriber_email);
											jsonObject.put("SubscriberId",subscriber_userid);//7113
											jsonObject.put("ListId",List_Id);
											jsonObject.put("CampaignId",campaign_id);
											jsonObject.put("Category",subFunnelNodeName);
											jsonObject.put("CreatedBy",campaigncreate_by);
											jsonObject.put("OutPutTemp","Inform");
											
											String rulr_engine_response=urlconnect("http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConvert_LACRules/fire",jsonObject);
											//System.out.println(rulr_engine_response);
											JSONObject ruleEngineResponseJsonObject=new JSONObject(rulr_engine_response);
											if(ruleEngineResponseJsonObject.has("Output")){
												String leadStatus=ruleEngineResponseJsonObject.getString("Output");
												System.out.println("leadStatus : "+leadStatus);
												String url=ResourceBundle.getBundle("config").getString("list_subscriber_move_rulengine");
												sendPostRequestToManageSubscribers(url,ruleEngineResponseJsonObject.toString());
											}else{
												System.out.println("I dont found any OutPut From Rule Engine");
											}
											
									
								}
								
								/*
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
								
								
								//String rulr_engine_response=urlconnect("http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConvert_LACRules/fire",jsonObject);
								//System.out.println(rulr_engine_response);
								//JSONObject ruleEngineResponseJsonObject=new JSONObject(rulr_engine_response);
								//String leadStatus=ruleEngineResponseJsonObject.getString("Output");
								//System.out.println("leadStatus : "+leadStatus);
						    }
							
					      /*
					      String subscriberStatus=campaign.getSubscriber().toString();
					      String url="http://35.201.178.201:8082/portal/servlet/service/LeadConverterJbpm.JBPM?Subscriber_Id="+subscriberId+"&Status="+subscriberStatus+"&Created_By="+campaigncreate_by+"&subFunnelNodeName="+subFunnelNodeName+"&funnelNodeName="+funnelNodeName+"&subscriber_email="+subscriber_email;
					      String campaignsubscriberresponse = this.sendpostdata(url);
					      System.out.println("campaignsubscriberresponse     :"+campaignsubscriberresponse); 
					      */ 
					      
					        
				}
		    }catch (Throwable t) {
			  t.printStackTrace();
		    }
	}
	
	public static List<Campaign> createCampagign(JSONArray array) {
		
		//System.out.println(" List<Campaign> createCampagign   :"+array);
		ArrayList<Campaign> campaignList = new ArrayList<Campaign>();
		String subscriberId;
		int mailBounceCount=0;;
		String subscribersEmail="jitendra.sen@bizlem.com"; 
	
		
		for(int i=0;i<array.length();i++) {
			
			
			int  campaignId=array.getJSONObject(0).getInt("id");
			String subject=array.getJSONObject(0).getString("subject");
			
			//System.out.println("campaignId  :"+campaignId +" subject   :"+subject);
			  JSONObject childJSONObject = array.getJSONObject(i);
			   
			   JSONArray subscribersJSON=childJSONObject.getJSONArray("subscribers");
			   for(int d=0;d<subscribersJSON.length();d++){
					Boolean openMail=false;
					Boolean deleverMail =false;
					
				   JSONObject subscriberObject = subscribersJSON.getJSONObject(d);
				   String  mailDelever=subscriberObject.getString("status");
//		              System.out.println("mailDelever   :"+mailDelever);
		              subscriberId=subscriberObject.getString("userid");
		              if(subscriberObject.has("clicks")){
					          JSONArray clicksJsonArray=subscriberObject.getJSONArray("clicks");
				            
					          for(int k=0;k<clicksJsonArray.length();k++) {
					               JSONObject clicksJosnObject = clicksJsonArray.getJSONObject(k);
					               mailBounceCount=clicksJosnObject.getInt("clicked");
				                   subscribersEmail = clicksJosnObject.getString("email");
					               String  mailopen=subscriberObject.getString("viewed");
					               if(mailopen!=null) {
					            	   openMail=true;
					                  if(mailDelever!=null) {
					                	  deleverMail=true;
	                                      Subscriber subscriber = createSubscriber(subscriberId, deleverMail, openMail,mailBounceCount);
	                                      campaignList.add(new Campaign(campaignId, "Unknown_1", subject, "Unknown", "jitendra307", subscriber));
							          }else {
								          Subscriber subscriber = createSubscriber(subscriberId, deleverMail, openMail,mailBounceCount);
					          		      campaignList.add(new Campaign(campaignId, "Unknown_1", subject, "Unknown", "Jitendra307", subscriber));
					                  }
			                       }
					          }
				        }else {
				              Subscriber subscriber = createSubscriber(subscriberId, deleverMail, openMail, mailBounceCount);
                              campaignList.add(new Campaign(campaignId, "Unknown_1", subject, "Unknown","Jitendra307" , subscriber));
   		                 }
			  }
		
	}
		return campaignList;
	}

	public static Subscriber createSubscriber(String subscriberId, boolean mailDelivered, boolean mailOpen, int mailBounceCount) {
		// new Subscriber(subsriberId, mailBounce, mailDelivered, mailOpen,
		// mailBounceCount, mailHotLink1ClickCount, mailHotLink2ClickCount,
		// mailHotLink3ClickCount, mailHotLink4ClickCount, mailHotLink5ClickCount,
		// mailNormalLink1ClickCount, mailNormalLink2ClickCount,
		// mailNormalLink3ClickCount, mailNormalLink4ClickCount,
		// mailNormalLink5ClickCount, mailUnsubscribe, clickedOnHotLinks,
		// clickedOnNormalLinks, clickedTwiceonAnyNormalLink)

    	return new Subscriber(subscriberId, false, mailDelivered, mailOpen, mailBounceCount, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, false,
				false, false, false);
//		return new Subscriber(subscriberId, false, true, true, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, false,
//				false, false, false);
	}
	public String sendpostdata(String callurl)
			   throws IOException {

		

			  URL url = new URL(callurl);
			  System.out.println("Url :" + url);
			  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			  conn.setDoOutput(true);
			  conn.setUseCaches(false);
			  conn.setRequestMethod("POST");

			  // 
			  OutputStream writer = conn.getOutputStream();

			  writer.write(callurl.getBytes());
			  int responseCode = conn.getResponseCode();
			  StringBuffer buffer = new StringBuffer();
			  //
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
	public String urlconnect(String urlstr, JSONObject json) throws IOException, JSONException {
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

			//System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toString();

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

}