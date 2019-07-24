package leadconverter.rulengine;

import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;

import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import leadconverter.create.rule.GetApplicationFreeTrialUserData;
import leadconverter.doctiger.LogByFileWriter;
import leadconverter.mongo.MongoDAO;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/call_rule_engine" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class RuleEngineCall extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = null;
	
	RulEngineMongoDAO rdao=new RulEngineMongoDAO();

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONArray mainarray = new JSONArray();
		JSONObject jsonobject = new JSONObject();
		String listid = null;

		String remoteuser = request.getRemoteUser();

		try {
			Session session = null;

			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			Node content = session.getRootNode().getNode("content");
			if (request.getRequestPathInfo().getExtension().equals("call_rulengine")) {
				//ProcessMain.callRuleEngine();
				//callRuleEngine(response);
				startprocessURLNew(response);
			}else if (request.getRequestPathInfo().getExtension().equals("call_rulengine_new")) {
				out.println("Call Rule Engine New API Called !");
				LogByFileWriter.logger_info("RuleEngineCall : " + "Call Rule Engine New API Called !");
				startprocessURLNew(response);
			}else if (request.getRequestPathInfo().getExtension().equals("call_rulengine_test")) {
				out.println("call_rulengine_test : : : ");
				LogByFileWriter.logger_info("RuleEngineCall : " + "call_rulengine_test : : : ");
			} 
		    }catch (Exception e) {
	
				out.println("Exception ex : : : " + e.getStackTrace());
			}
	}

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
        /*
		try {
		} catch (Exception e) {
			out.println("Exception : : :" + e.getMessage());
		}
		*/

	}
    
	public static void callRuleEngine(SlingHttpServletResponse response) {
		
		int campaignId=0;
		String campaigncreate_by=null;
		StringBuilder builder = new StringBuilder();
		try { 
			   PrintWriter out = response.getWriter();
			   //URL urlCampaign=new URL("http://35.201.178.201:8082/portal/servlet/service/PhpData.phpdata");
			   URL urlCampaign=new URL(ResourceBundle.getBundle("config").getString("allCampaignForRuleEngine"));
			   URLConnection urlConnectionCampaign = urlCampaign.openConnection();
		       BufferedReader bufferedReaderCampaign = new BufferedReader(new InputStreamReader(urlConnectionCampaign.getInputStream()));
		       String lineCampaign;
		       while ((lineCampaign = bufferedReaderCampaign.readLine()) != null)
		       { 
		    	   String email=null;
		    	   builder.append(lineCampaign + "\n");
		       }
		       //System.out.println("builder.toString() : "+builder.toString());
		       JSONObject jsonObject=new JSONObject(builder.toString());
		       JSONArray jsonArray = (JSONArray) jsonObject.get("Data");
		       for(int i=0;i<jsonArray.length();i++) {
		    	    //Executing loop for Last Funnel
		    	    
		    	    
		    	    	
			    	    JSONObject jsonobjectcampen = (JSONObject) jsonArray.get(i);
			                      campaignId=jsonobjectcampen.getInt("Campaign_Id");
			                      campaigncreate_by=jsonobjectcampen.getString("Created_By");
			             StringBuilder builder1 = new StringBuilder();
		                 URL url=new URL(ResourceBundle.getBundle("config").getString("webservice_campaigndatabyid")+campaignId);
			             URLConnection urlConnection = url.openConnection();
				         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				         String line;
				         while ((line = bufferedReader.readLine()) != null)
				         { 
				    	   String email=null;
				    	   builder1.append(line + "\n");
				         }
		                 JSONArray campaign_click_json_array=new JSONArray(builder1.toString());
		                 
		                 if(campaign_click_json_array.length()!=0){
		                	 out.println("Click statistics found for Campaign Id : "+campaignId);
		                	 LogByFileWriter.logger_info("RuleEngineCall : " + "Click statistics found for Campaign Id : "+campaignId);
		                	   //startprocess(campaign_click_json_array,campaigncreate_by,response);
		                	 //if(campaignId==1005){
		                	   startprocessURL(campaign_click_json_array,campaigncreate_by,response);
		                	 //}
		                	   //startprocessTest(campaign_click_json_array,campaigncreate_by,response);
		                	 //List<Campaign> campaignList = createCampagign(campaign_click_json_array,response);
		                 }else{
		                	 out.println("No click statistics found for Campaign Id : "+campaignId);
		                	 LogByFileWriter.logger_info("RuleEngineCall : " + "No click statistics found for Campaign Id : "+campaignId);
		                 }
		    	    }
	                 //break;
	            
	 	    }catch (Exception e) {
	 	      System.out.println(e.getMessage());
	        }
		
      }
	public  static void startprocessURLNew(SlingHttpServletResponse response) {
		PrintWriter out=null;
		try {
				out = response.getWriter();
				System.out.println("Start Process URL New Method Called");
				out.println("Start Process URL New Method Called");
				LogByFileWriter.logger_info("RuleEngineCall : " + "Start Process URL New Method Called");
				          String campaignId=null;
					      String subscriberId=null;
					      
					      RulEngineMongoDAO rdao=new RulEngineMongoDAO();
						  //JSONArray mainJsonArray=new JSONArray(rdao.subscribersViewBasedOnFunnelData("url",campaignId,subscriberId).toString());
						  //JSONArray mainJsonArray=new JSONArray(rdao.subscribersViewBasedOnFunnelData("subscribers",campaignId,subscriberId).toString());
					      LogByFileWriter.logger_info("RuleEngineCall : " + "tempSubscribersViewBasedOnFunnelData Method Called Start");
					      out.println("RuleEngineCall : " + "tempSubscribersViewBasedOnFunnelData Method Called Start");
					      JSONArray mainJsonArray=new JSONArray(rdao.tempSubscribersViewBasedOnFunnelData("temp_subscribers").toString());
					      LogByFileWriter.logger_info("RuleEngineCall : " + "tempSubscribersViewBasedOnFunnelData Method Called End");
					      out.println("RuleEngineCall : " + "tempSubscribersViewBasedOnFunnelData Method Called End");
						  
						  System.out.println("mainJsonArray length : "+mainJsonArray.length());
						         out.println("Subscribers PHPLIST Data MainJson Array length : "+mainJsonArray.length());
						         LogByFileWriter.logger_info("RuleEngineCall : " + "Subscribers PHPLIST Data MainJson Array length : "+mainJsonArray.length());       
						  JSONObject subscriber_campaign_jsonObject=(JSONObject) mainJsonArray.get(0);
						  JSONArray subscriber_campaign_jsonArray=subscriber_campaign_jsonObject.getJSONArray("subscriber_campaign");
						  //System.out.println("subscriber_campaign_jsonArray : "+subscriber_campaign_jsonArray);
						  System.out.println("subscriber_campaign_jsonArray length : "+subscriber_campaign_jsonArray.length());
						         out.println("Subscribers PHPLIST Campaign Data MainJson Array length : "+subscriber_campaign_jsonArray.length());
						         LogByFileWriter.logger_info("RuleEngineCall : " + "Subscribers PHPLIST Campaign Data MainJson Array length : "+subscriber_campaign_jsonArray.length());
						  //JSONArray jsonArray = (JSONArray) jsonObject.get("Data");
						  
						  //campaignclickstatistics_firstclick
						  //campaignclickstatistics_latestclick
						  //campaignclickstatistics_clicked
						  
						    int subscriber_click_count=0;
							String campaignclickstatistics_firstclick="";
							String campaignclickstatistics_latestclick="";
							String campaignclickstatistics_clicked="";
							
							String urlclickstatistics_firstclick="";
							String urlclickstatistics_latestclick="";
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
							String php_ga_recent="NO";
							String campaigncreate_by="";
							
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
					        String hostname = "";
					        String date = "";
					        String ga_recent = "NO";
					        
					        
						  
							for(int d=0;d<subscriber_campaign_jsonArray.length();d++){
								
								JSONObject jsonObject=new JSONObject();
								JSONObject subscriberObject = subscriber_campaign_jsonArray.getJSONObject(d);
								//System.out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId);
								System.out.println("Subscriber Phplist Click Statistics : "+subscriberObject);
								       out.println("Subscriber Phplist Click Statistics : "+subscriberObject);
								       LogByFileWriter.logger_info("RuleEngineCall : " + "Subscriber Phplist Click Statistics : "+subscriberObject);
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
								campaigncreate_by             =(String) subscriberObject.get("Created_By");
								//out.println("Before urlclickstatistics_latestclick");
								urlclickstatistics_latestclick=(String) subscriberObject.get("urlclickstatistics_latestclick");// string Date
								//out.println("After urlclickstatistics_latestclick");
								campaignId=campaign_id;
								subscriberId=subscriber_userid;
								System.out.println("campaignId : "+campaignId+"  subscriberId : "+subscriber_userid);
					             
							    //Getting Subscribers Email Based On Subscriber Id From Phplist
							      //String subscriberget_emailID=GetSubscriberEmailID(subscriberId); 
							      System.out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId+"  subscribe_EmailID : "+subscriber_email);
							      out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId+"  subscribe_EmailID : "+subscriber_email);       
							      LogByFileWriter.logger_info("RuleEngineCall : " + "campaignId : "+campaignId+"  subscriberId : "+subscriberId+"  subscribe_EmailID : "+subscriber_email);
							      //Code to get Application Data
							      //String application_data_arr=GetApplicationFreeTrialUserData.getApplicationData("akhilesh@bizlem.com");
							      String application_data_arr=GetApplicationFreeTrialUserData.getApplicationData(subscriber_email);
								  JSONObject application_data_response_final_json=new JSONObject(application_data_arr);
								  String Free_Trial_Taken=null;
								  String Free_Trial_Product_Name=null;
								  String Free_Trial_Expire_Date=null;
								  String Free_Trial_Joined_Date=null;
								  String Free_Trial_Expire_Flag=null;
								  String Free_Trial_Used_Count=null;
								  if(application_data_response_final_json.length()==0){
									  Free_Trial_Taken="";
									  Free_Trial_Product_Name="";
									  Free_Trial_Expire_Date="";
									  Free_Trial_Joined_Date="";
									  Free_Trial_Expire_Flag="";
									  Free_Trial_Used_Count="";
								  }else{
									  Free_Trial_Taken=application_data_response_final_json.getString("Free_Trial_Taken");
									  Free_Trial_Product_Name=application_data_response_final_json.getString("Free_Trial_Product_Name");
									  Free_Trial_Expire_Date=application_data_response_final_json.getString("Free_Trial_Expire_Date");
									  Free_Trial_Joined_Date=application_data_response_final_json.getString("Free_Trial_Joined_Date");
									  Free_Trial_Expire_Flag=application_data_response_final_json.getString("Free_Trial_Expire_Flag");
									  Free_Trial_Used_Count=application_data_response_final_json.getString("Free_Trial_Used_Count");
								  }
								//Checking For Recent start
								if(!urlclickstatistics_latestclick.equals("null")){
									SimpleDateFormat date_formatter=new SimpleDateFormat("dd MMM yyyy HH:mm");
								    Date date_campare1 = new Date();
								    date_campare1.setDate(date_campare1.getDate()-7);
								    Date date1 = date_formatter.parse(date_formatter.format(date_campare1));
								    Date date2 = date_formatter.parse(urlclickstatistics_latestclick);
								    if (date2.compareTo(date1) > 0) {
								        System.out.println("Date2 is after Date1");
								        php_ga_recent="YES";
								    } else if (date2.compareTo(date1) < 0) {
								        System.out.println("Date2 is before Date1");
								        php_ga_recent="NO";
								    } else if (date2.compareTo(date1) == 0) {
								        System.out.println("Date2 is equal to Date1");
								        php_ga_recent="YES";
								    }
								}
								System.out.println("Is it Recent? : "+php_ga_recent);
							    out.println("Is it Recent? : "+php_ga_recent);
							    LogByFileWriter.logger_info("RuleEngineCall : " + "Is it Recent? : "+php_ga_recent);
							    //Checking For Recent End
							    
							    jsonObject.put("Created_By",campaigncreate_by);
							    //jsonObject.put("Created_By","viki_gmail.com");
							    jsonObject.put("Funnel_Name",funnelNodeName);
								jsonObject.put("SubFunnel_Name",subFunnelNodeName);
								jsonObject.put("Category",subFunnelNodeName);
								jsonObject.put("Campaign_Id",campaign_id);
								jsonObject.put("List_Id",List_Id);
								jsonObject.put("Subscriber_Id",subscriber_userid);//7113
								jsonObject.put("Subscriber_Email",subscriber_email);
								
								jsonObject.put("Recent_Click",php_ga_recent);
								jsonObject.put("Open",viewed);
								jsonObject.put("Click",clicks);
								jsonObject.put("Bounce",bounces);
								
								jsonObject.put("Free_Trial_Taken",Free_Trial_Taken );
								jsonObject.put("Free_Trial_Product_Name",Free_Trial_Product_Name );
								jsonObject.put("Free_Trial_Expire_Date",Free_Trial_Expire_Date );
								jsonObject.put("Free_Trial_Joined_Date",Free_Trial_Joined_Date );
								jsonObject.put("Free_Trial_Expire_Flag",Free_Trial_Expire_Flag );
								jsonObject.put("Free_Trial_Used_Count",Free_Trial_Used_Count );
							    
								MongoDAO mdao=new MongoDAO();
								org.apache.sling.commons.json.JSONArray campaignJsonArr=rdao.findSubscribersBasedOncampaignNameAndDimension2("",campaign_id,Sling_Subject,subscriber_email);
								System.out.println("GA Subscribers CampaignJsonArr Length : "+campaignJsonArr.length());
								out.println("GA Subscribers CampaignJsonArr Length : "+campaignJsonArr.length());
								LogByFileWriter.logger_info("RuleEngineCall : " + "GA Subscribers CampaignJsonArr Length : "+campaignJsonArr.length());
								if(campaignJsonArr.length()!=0){
									for(int i=0;i<campaignJsonArr.length();i++){
										JSONObject json_obj=new JSONObject(campaignJsonArr.get(i).toString());
										  //ga_campaign = json_obj.getString("campaign");
											pagePath = json_obj.getString("pagePath");
											sessionCount = json_obj.getString("sessionCount");
											//dimension2 = json_obj.getString("dimension2");
											//medium = json_obj.getString("medium");
											sessiondurationBucket = json_obj.getString("sessiondurationBucket");
											//pageTitle = json_obj.getString("pageTitle");
											timeOnPage = json_obj.getString("timeOnPage");
											bounces = json_obj.getString("bounces");
											source = json_obj.getString("source");
											medium = json_obj.getString("medium");
											hostname = json_obj.getString("hostname");
											//date = json_obj.getString("date");
											date = json_obj.getString("dateHourMinute");
											//date="201905140031";//"20190514"
										  	String final_date_str=date.substring(0, 4)+"-"+date.substring(4, 6)+"-"+date.substring(6, 8);
										  	String date_str=final_date_str+" "+date.substring(8, 10)+":"+date.substring(10, 12)+":00";
										  	
										  	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
										  	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    
										    //dateFormat108.parse(final_date_str_temp1);
										    System.out.println(" final_date_str_temp1 date : "+dateFormat.parse(date_str));
										  	
										  	Date date_campare1 = new Date();
										  	     date_campare1.setDate(date_campare1.getDate()-7);
										    Date tmp_date1 = sdf.parse(sdf.format(date_campare1));
										    Date tmp_date2 = sdf.parse(final_date_str);
										     
										    if (tmp_date2.compareTo(tmp_date1) > 0) {
										    	 System.out.println("Date2 is after Date1");
										    	 ga_recent="YES";
										    } else if (tmp_date2.compareTo(tmp_date1) < 0) {
										    	 System.out.println("Date2 is before Date1");
										    	 ga_recent="NO";
										    } else if (tmp_date2.compareTo(tmp_date1) == 0) {
										    	 System.out.println("Date2 is equal to Date1");
										    	 ga_recent="YES";
										    }
										    
											if(source.contains("direct")){
												System.out.println("Own Visit");
												jsonObject.put("Own_Visit","YES");
												jsonObject.put("Own_Visit_Recent",ga_recent);
												jsonObject.put("Own_Visit_Time",timeOnPage);
												jsonObject.put("Source","direct");
											}else{
												System.out.println("Campaign Visit");
												jsonObject.put("Own_Visit","NO");
												jsonObject.put("Own_Visit_Recent","NO");
												jsonObject.put("Own_Visit_Time","");
												jsonObject.put("Source","email");
											}
											if(pagePath.length()==1){
												pagePath=hostname;
										    	System.out.println(pagePath);
										    }else{
										       if(pagePath.contains("/?")){
										    	   pagePath=hostname+pagePath.substring(0, pagePath.indexOf("/?"));
										    	   //System.out.println(path1);
										       }else if(pagePath.contains("?")){
										    	   pagePath=hostname+pagePath.substring(0, pagePath.indexOf("?"));
										       }else if(pagePath.contains("/")){
										    	   pagePath=hostname+pagePath;
										       }
										    }
											jsonObject.put(pagePath,timeOnPage);
											jsonObject.put(pagePath+"_recent",ga_recent);
											jsonObject.put(pagePath+"_recent_time",date_str);
											if(i==0){
											//sessiondurationBucket = json_obj.getString("sessiondurationBucket");
											jsonObject.put("Session_Count",sessionCount);
											jsonObject.put("Total_Session_Time",sessiondurationBucket);
											}
											//jsonObject.put("Invoice_Process_Count",invoice_process_count);
									}
								}else{
									        out.println("No GA Data FOUND !");
									        System.out.println("No GA Data FOUND !");
									        LogByFileWriter.logger_info("RuleEngineCall : " + "No GA Data FOUND !");
								}
								System.out.println("Input JsonObject For Rule Engine : "+jsonObject);
								out.println("Input JsonObject For Rule Engine : "+jsonObject);
								LogByFileWriter.logger_info("RuleEngineCall : " + "Input JsonObject For Rule Engine : "+jsonObject);
								String rule_engine_response=urlconnect(ResourceBundle.getBundle("config").getString("rule_engine_url"),jsonObject,response);
								//System.out.println("Rule Engine Response : "+rule_engine_response);
								//out.println("Rule Engine Response : "+rule_engine_response);
								//LogByFileWriter.logger_info("RuleEngineCall : " + "Rule Engine Response : "+rule_engine_response);
								JSONObject ruleEngineResponseJsonObject=new JSONObject(rule_engine_response);
								if(ruleEngineResponseJsonObject.has("Output")){
									String leadStatus=ruleEngineResponseJsonObject.getString("Output");
									System.out.println("Rule Engine Response (Move TO): "+leadStatus);
									out.println("Rule Engine Response (Move TO): "+leadStatus);
									LogByFileWriter.logger_info("RuleEngineCall : " + "Rule Engine Response (Move TO): "+leadStatus);
									
									String url=ResourceBundle.getBundle("config").getString("list_subscriber_move_rulengine");
									sendPostRequestToManageSubscribers(url,ruleEngineResponseJsonObject.toString(),response);
								}else{
									System.out.println("Did not get any OutPut From Rule Engine");
									out.println("Did not get any OutPut From Rule Engine");
									LogByFileWriter.logger_info("RuleEngineCall : " + "Did not get any OutPut From Rule Engine");
								}
								
						    }
				
		    }catch (Exception ex) {
			  out.println("Error : "+ex.getMessage());
		    }
	}
	public  static void startprocessURL(JSONArray array, String campaigncreate_by,SlingHttpServletResponse response) {
		try {
				List<Campaign> campaignList = createCampagign(array,response);
				PrintWriter out = response.getWriter();
				System.out.println("campaignList length : "+campaignList.size());
				out.println("campaignList length : "+campaignList.size());
				LogByFileWriter.logger_info("RuleEngineCall : " + "campaignList length : "+campaignList.size());
				for (Campaign campaign : campaignList) {
					      String campaignId=Integer.toString(campaign.getCampaignId());
					      String subscriberId=campaign.getSubscriber().getSubsriberId();
					      System.out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId);
					             
					    //Getting Subscribers Email Based On Subscriber Id From Phplist
					      String subscriberget_emailID=GetSubscriberEmailID(subscriberId); 
					      System.out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId+"  subscribe_EmailID : "+subscriberget_emailID);
					      out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId+"  subscribe_EmailID : "+subscriberget_emailID);       
					      LogByFileWriter.logger_info("RuleEngineCall : " + "campaignId : "+campaignId+"  subscriberId : "+subscriberId+"  subscribe_EmailID : "+subscriberget_emailID);
					      //Code to get Application Data
					      //String application_data_arr=GetApplicationFreeTrialUserData.getApplicationData("akhilesh@bizlem.com");
					      String application_data_arr=GetApplicationFreeTrialUserData.getApplicationData(subscriberget_emailID);
						  JSONObject application_data_response_final_json=new JSONObject(application_data_arr);
						  String Free_Trial_Taken=null;
						  String Free_Trial_Product_Name=null;
						  String Free_Trial_Expire_Date=null;
						  String Free_Trial_Joined_Date=null;
						  String Free_Trial_Expire_Flag=null;
						  String Free_Trial_Used_Count=null;
						  if(application_data_response_final_json.length()==0){
							  Free_Trial_Taken="";
							  Free_Trial_Product_Name="";
							  Free_Trial_Expire_Date="";
							  Free_Trial_Joined_Date="";
							  Free_Trial_Expire_Flag="";
							  Free_Trial_Used_Count="";
						  }else{
							  Free_Trial_Taken=application_data_response_final_json.getString("Free_Trial_Taken");
							  Free_Trial_Product_Name=application_data_response_final_json.getString("Free_Trial_Product_Name");
							  Free_Trial_Expire_Date=application_data_response_final_json.getString("Free_Trial_Expire_Date");
							  Free_Trial_Joined_Date=application_data_response_final_json.getString("Free_Trial_Joined_Date");
							  Free_Trial_Expire_Flag=application_data_response_final_json.getString("Free_Trial_Expire_Flag");
							  Free_Trial_Used_Count=application_data_response_final_json.getString("Free_Trial_Used_Count");
						  }
					      /*
					      String[] application_data_arr=GetApplicationFreeTrialUserData.getApplicationData("akhilesh0308@bizlem.com").split(":");
						  String freetrial=application_data_arr[0];
						  String invoice_process_count=application_data_arr[1];
						  System.out.println("application_data : "+freetrial+"\n"+"invoice_process_count : "+invoice_process_count);
						  out.println("application_data : "+freetrial+"\n"+"invoice_process_count : "+invoice_process_count);
						  */
					      
					      RulEngineMongoDAO rdao=new RulEngineMongoDAO();
						  //JSONArray mainJsonArray=new JSONArray(rdao.subscribersViewBasedOnFunnelData("url",campaignId,subscriberId).toString());
						  JSONArray mainJsonArray=new JSONArray(rdao.subscribersViewBasedOnFunnelData("subscribers",campaignId,subscriberId).toString());
						  
						  System.out.println("mainJsonArray length : "+mainJsonArray.length());
						         out.println("Subscribers PHPLIST Data MainJson Array length : "+mainJsonArray.length());
						         LogByFileWriter.logger_info("RuleEngineCall : " + "Subscribers PHPLIST Data MainJson Array length : "+mainJsonArray.length());       
						  JSONObject subscriber_campaign_jsonObject=(JSONObject) mainJsonArray.get(0);
						  JSONArray subscriber_campaign_jsonArray=subscriber_campaign_jsonObject.getJSONArray("subscriber_campaign");
						  //System.out.println("subscriber_campaign_jsonArray : "+subscriber_campaign_jsonArray);
						  System.out.println("subscriber_campaign_jsonArray length : "+subscriber_campaign_jsonArray.length());
						         out.println("Subscribers PHPLIST Campaign Data MainJson Array length : "+subscriber_campaign_jsonArray.length());
						         LogByFileWriter.logger_info("RuleEngineCall : " + "Subscribers PHPLIST Campaign Data MainJson Array length : "+subscriber_campaign_jsonArray.length());
						  //JSONArray jsonArray = (JSONArray) jsonObject.get("Data");
						  
						  //campaignclickstatistics_firstclick
						  //campaignclickstatistics_latestclick
						  //campaignclickstatistics_clicked
						  
						    int subscriber_click_count=0;
							String campaignclickstatistics_firstclick="";
							String campaignclickstatistics_latestclick="";
							String campaignclickstatistics_clicked="";
							
							String urlclickstatistics_firstclick="";
							String urlclickstatistics_latestclick="";
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
							String php_ga_recent="NO";
							
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
					        String hostname = "";
					        String date = "";
					        String ga_recent = "NO";
					        
							
						  
							for(int d=0;d<subscriber_campaign_jsonArray.length();d++){
								JSONObject jsonObject=new JSONObject();
								JSONObject subscriberObject = subscriber_campaign_jsonArray.getJSONObject(d);
								//System.out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId);
								System.out.println("Subscriber Phplist Click Statistics : "+subscriberObject);
								       out.println("Subscriber Phplist Click Statistics : "+subscriberObject);
								       LogByFileWriter.logger_info("RuleEngineCall : " + "Subscriber Phplist Click Statistics : "+subscriberObject);
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
								//out.println("Before urlclickstatistics_latestclick");
								urlclickstatistics_latestclick=(String) subscriberObject.get("urlclickstatistics_latestclick");// string Date
								//out.println("After urlclickstatistics_latestclick");
								//Checking For Recent start
								if(!urlclickstatistics_latestclick.equals("null")){
									SimpleDateFormat date_formatter=new SimpleDateFormat("dd MMM yyyy HH:mm");
								    Date date_campare1 = new Date();
								    date_campare1.setDate(date_campare1.getDate()-7);
								    Date date1 = date_formatter.parse(date_formatter.format(date_campare1));
								    Date date2 = date_formatter.parse(urlclickstatistics_latestclick);
								    if (date2.compareTo(date1) > 0) {
								        System.out.println("Date2 is after Date1");
								        php_ga_recent="YES";
								    } else if (date2.compareTo(date1) < 0) {
								        System.out.println("Date2 is before Date1");
								        php_ga_recent="NO";
								    } else if (date2.compareTo(date1) == 0) {
								        System.out.println("Date2 is equal to Date1");
								        php_ga_recent="YES";
								    }
								}
								System.out.println("Is it Recent? : "+php_ga_recent);
							    out.println("Is it Recent? : "+php_ga_recent);
							    LogByFileWriter.logger_info("RuleEngineCall : " + "Is it Recent? : "+php_ga_recent);
							    //Checking For Recent End
							    
							    jsonObject.put("Created_By",campaigncreate_by);
							    jsonObject.put("Funnel_Name",funnelNodeName);
								jsonObject.put("SubFunnel_Name",subFunnelNodeName);
								jsonObject.put("Category",subFunnelNodeName);
								jsonObject.put("Campaign_Id",campaign_id);
								jsonObject.put("List_Id",List_Id);
								jsonObject.put("Subscriber_Id",subscriber_userid);//7113
								jsonObject.put("Subscriber_Email",subscriber_email);
								
								jsonObject.put("Recent_Click",php_ga_recent);
								jsonObject.put("Open",viewed);
								jsonObject.put("Click",clicks);
								jsonObject.put("Bounce",bounces);
								
								jsonObject.put("Free_Trial_Taken",Free_Trial_Taken );
								jsonObject.put("Free_Trial_Product_Name",Free_Trial_Product_Name );
								jsonObject.put("Free_Trial_Expire_Date",Free_Trial_Expire_Date );
								jsonObject.put("Free_Trial_Joined_Date",Free_Trial_Joined_Date );
								jsonObject.put("Free_Trial_Expire_Flag",Free_Trial_Expire_Flag );
								jsonObject.put("Free_Trial_Used_Count",Free_Trial_Used_Count );
							    
								MongoDAO mdao=new MongoDAO();
								org.apache.sling.commons.json.JSONArray campaignJsonArr=rdao.findSubscribersBasedOncampaignNameAndDimension2("",campaign_id,Sling_Subject,subscriber_email);
								System.out.println("GA Subscribers CampaignJsonArr Length : "+campaignJsonArr.length());
								out.println("GA Subscribers CampaignJsonArr Length : "+campaignJsonArr.length());
								LogByFileWriter.logger_info("RuleEngineCall : " + "GA Subscribers CampaignJsonArr Length : "+campaignJsonArr.length());
								if(campaignJsonArr.length()!=0){
									for(int i=0;i<campaignJsonArr.length();i++){
										JSONObject json_obj=new JSONObject(campaignJsonArr.get(i).toString());
										  //ga_campaign = json_obj.getString("campaign");
											pagePath = json_obj.getString("pagePath");
											sessionCount = json_obj.getString("sessionCount");
											//dimension2 = json_obj.getString("dimension2");
											//medium = json_obj.getString("medium");
											sessiondurationBucket = json_obj.getString("sessiondurationBucket");
											//pageTitle = json_obj.getString("pageTitle");
											timeOnPage = json_obj.getString("timeOnPage");
											bounces = json_obj.getString("bounces");
											source = json_obj.getString("source");
											medium = json_obj.getString("medium");
											hostname = json_obj.getString("hostname");
											//date = json_obj.getString("date");
											date = json_obj.getString("dateHourMinute");
											//date="201905140031";//"20190514"
										  	String final_date_str=date.substring(0, 4)+"-"+date.substring(4, 6)+"-"+date.substring(6, 8);
										  	String date_str=final_date_str+" "+date.substring(8, 10)+":"+date.substring(10, 12)+":00";
										  	
										  	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
										  	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    
										    //dateFormat108.parse(final_date_str_temp1);
										    System.out.println(" final_date_str_temp1 date : "+dateFormat.parse(date_str));
										  	
										  	Date date_campare1 = new Date();
										  	     date_campare1.setDate(date_campare1.getDate()-7);
										    Date tmp_date1 = sdf.parse(sdf.format(date_campare1));
										    Date tmp_date2 = sdf.parse(final_date_str);
										     
										    if (tmp_date2.compareTo(tmp_date1) > 0) {
										    	 System.out.println("Date2 is after Date1");
										    	 ga_recent="YES";
										    } else if (tmp_date2.compareTo(tmp_date1) < 0) {
										    	 System.out.println("Date2 is before Date1");
										    	 ga_recent="NO";
										    } else if (tmp_date2.compareTo(tmp_date1) == 0) {
										    	 System.out.println("Date2 is equal to Date1");
										    	 ga_recent="YES";
										    }
										    
											if(source.contains("direct")){
												System.out.println("Own Visit");
												jsonObject.put("Own_Visit","YES");
												jsonObject.put("Own_Visit_Recent",ga_recent);
												jsonObject.put("Own_Visit_Time",timeOnPage);
												jsonObject.put("Source","direct");
											}else{
												System.out.println("Campaign Visit");
												jsonObject.put("Own_Visit","NO");
												jsonObject.put("Own_Visit_Recent","NO");
												jsonObject.put("Own_Visit_Time","");
												jsonObject.put("Source","email");
											}
											if(pagePath.length()==1){
												pagePath=hostname;
										    	System.out.println(pagePath);
										    }else{
										       if(pagePath.contains("/?")){
										    	   pagePath=hostname+pagePath.substring(0, pagePath.indexOf("/?"));
										    	   //System.out.println(path1);
										       }else if(pagePath.contains("?")){
										    	   pagePath=hostname+pagePath.substring(0, pagePath.indexOf("?"));
										       }else if(pagePath.contains("/")){
										    	   pagePath=hostname+pagePath;
										       }
										    }
											jsonObject.put(pagePath,timeOnPage);
											jsonObject.put(pagePath+"_recent",ga_recent);
											jsonObject.put(pagePath+"_recent_time",date_str);
											if(i==0){
											//sessiondurationBucket = json_obj.getString("sessiondurationBucket");
											jsonObject.put("Session_Count",sessionCount);
											jsonObject.put("Total_Session_Time",sessiondurationBucket);
											}
											//jsonObject.put("Invoice_Process_Count",invoice_process_count);
									}
								}else{
									        out.println("No GA Data FOUND !");
									        System.out.println("No GA Data FOUND !");
									        LogByFileWriter.logger_info("RuleEngineCall : " + "No GA Data FOUND !");
								}
								System.out.println("Input JsonObject For Rule Engine : "+jsonObject);
								out.println("Input JsonObject For Rule Engine : "+jsonObject);
								LogByFileWriter.logger_info("RuleEngineCall : " + "Input JsonObject For Rule Engine : "+jsonObject);
								String rule_engine_response=urlconnect(ResourceBundle.getBundle("config").getString("rule_engine_url"),jsonObject,response);
								System.out.println("Rule Engine Response : "+rule_engine_response);
								out.println("Rule Engine Response : "+rule_engine_response);
								LogByFileWriter.logger_info("RuleEngineCall : " + "Rule Engine Response : "+rule_engine_response);
								JSONObject ruleEngineResponseJsonObject=new JSONObject(rule_engine_response);
								if(ruleEngineResponseJsonObject.has("Output")){
									String leadStatus=ruleEngineResponseJsonObject.getString("Output");
									String url=ResourceBundle.getBundle("config").getString("list_subscriber_move_rulengine");
									out.println("list_subscriber_move_rulengine Start");
									sendPostRequestToManageSubscribers(url,ruleEngineResponseJsonObject.toString(),response);
									out.println("list_subscriber_move_rulengine End");
								}else{
									System.out.println("Did not get any OutPut From Rule Engine");
									out.println("Did not get any OutPut From Rule Engine");
									LogByFileWriter.logger_info("RuleEngineCall : " + "Did not get any OutPut From Rule Engine");
								}
								
						    }
				}
		    }catch (Throwable t) {
			  t.printStackTrace();
		    }
	}
	
	public  static void startprocessTest(JSONArray array, String campaigncreate_by,SlingHttpServletResponse response) {
		try {
				List<Campaign> campaignList = createCampagign(array,response);
				//System.out.println("  campaignList   :"+campaignList);
				//callRuleEng calrulengine=new callRuleEng();
				PrintWriter out = response.getWriter();
				
				out.println("campaignList length : "+campaignList.size());
				for (Campaign campaign : campaignList) {
					      //System.out.println("Subscriber id -> " + campaign.getSubscriber().getSubsriberId() + "\n"
							           //  + campaign.getSubscriber().toString());
					      String campaignId=Integer.toString(campaign.getCampaignId());
					      String subscriberId=campaign.getSubscriber().getSubsriberId();
					      
					      System.out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId);
					      out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId);
					      
					      //Code to get Application Data
					      String[] application_data_arr=GetApplicationFreeTrialUserData.getApplicationData("akhilesh0308@bizlem.com").split(":");
						  String freetrial=application_data_arr[0];
						  String invoice_process_count=application_data_arr[1];
						  System.out.println("application_data : "+freetrial+"\n"+"invoice_process_count : "+invoice_process_count);
						  out.println("application_data : "+freetrial+"\n"+"invoice_process_count : "+invoice_process_count);
						    
					      RulEngineMongoDAO rdao=new RulEngineMongoDAO();
                          JSONArray mainJsonArray=new JSONArray(rdao.subscribersViewBasedOnFunnelData("subscribers",campaignId,subscriberId).toString());
						  
						  System.out.println("mainJsonArray length : "+mainJsonArray.length());
						  out.println("mainJsonArray length : "+mainJsonArray.length());
						  //out.println("mainJsonArray : "+mainJsonArray);
						  JSONObject subscriber_campaign_jsonObject=(JSONObject) mainJsonArray.get(0);
						  JSONArray subscriber_campaign_jsonArray=subscriber_campaign_jsonObject.getJSONArray("subscriber_campaign");
						  //System.out.println("subscriber_campaign_jsonArray : "+subscriber_campaign_jsonArray);
						  System.out.println("subscriber_campaign_jsonArray length : "+subscriber_campaign_jsonArray.length());
						  out.println("subscriber_campaign_jsonArray length : "+subscriber_campaign_jsonArray.length());
						  //JSONArray jsonArray = (JSONArray) jsonObject.get("Data");
						  for(int d=0;d<subscriber_campaign_jsonArray.length();d++){
								JSONObject subscriberObject = subscriber_campaign_jsonArray.getJSONObject(d);
								//System.out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId);
								System.out.println("subscriberObject : "+subscriberObject);
								out.println("subscriberObject : "+subscriberObject);
								String subscriber_email       =(String) subscriberObject.get("subscriber_email");
								String campaign_id            =(String) subscriberObject.get("campaign_id");
								String Sling_Subject          =(String) subscriberObject.get("Sling_Subject");
								
								
								MongoDAO mdao=new MongoDAO();
								//org.apache.sling.commons.json.JSONArray campaignJsonArr=rdao.findSubscribersBasedOncampaignNameAndDimension2("","797","hi","harshita.indapurkar@bizlem.com");
								org.apache.sling.commons.json.JSONArray campaignJsonArr=rdao.findSubscribersBasedOncampaignNameAndDimension2("",campaign_id,Sling_Subject,subscriber_email);
								out.println("campaignJsonArr Length : "+campaignJsonArr.length());
								//out.println("campaignJsonArr Length : "+campaignJsonArr);
								if(campaignJsonArr.length()!=0){
									out.println("inside campaignJsonArr.length()!=0");
									for(int i=0;i<campaignJsonArr.length();i++){
										JSONObject json_obj=new JSONObject(campaignJsonArr.get(i).toString());
										System.out.println("pagePath : "+json_obj.getString("pagePath"));
										    out.println("pagePath : "+json_obj.getString("pagePath"));
										    out.println("Rule Engine called for d = "+d);
									}
								}else{
							        out.println("inside campaignJsonArr.length()==0");
							        out.println("inside campaignJsonArr.length()==0");
							        out.println("Rule Engine called for i = "+d);
								}
								
								
								
						  }
				}
		    }catch (Throwable t) {
			  t.printStackTrace();
		    }
	}
	
	
	
	
	
	public  static void startprocess(JSONArray array, String campaigncreate_by,SlingHttpServletResponse response) {
		try {
				List<Campaign> campaignList = createCampagign(array,response);
				//System.out.println("  campaignList   :"+campaignList);
				//callRuleEng calrulengine=new callRuleEng();
				PrintWriter out = response.getWriter();
				
				out.println("campaignList length : "+campaignList.size());
				for (Campaign campaign : campaignList) {
					      //System.out.println("Subscriber id -> " + campaign.getSubscriber().getSubsriberId() + "\n"
							           //  + campaign.getSubscriber().toString());
					      String campaignId=Integer.toString(campaign.getCampaignId());
					      String subscriberId=campaign.getSubscriber().getSubsriberId();
					      
					      System.out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId);
					      out.println("campaignId : "+campaignId+"  subscriberId : "+subscriberId);
					      
					    //Code to get Application Data
					      String[] application_data_arr=GetApplicationFreeTrialUserData.getApplicationData("akhilesh0308@bizlem.com").split(":");
						  String freetrial=application_data_arr[0];
						  String invoice_process_count=application_data_arr[1];
						  System.out.println("application_data : "+freetrial+"\n"+"invoice_process_count : "+invoice_process_count);
						  out.println("application_data : "+freetrial+"\n"+"invoice_process_count : "+invoice_process_count);
					      
					      RulEngineMongoDAO rdao=new RulEngineMongoDAO();
						  //JSONArray mainJsonArray=new JSONArray(rdao.subscribersViewBasedOnFunnelData("url",campaignId,subscriberId).toString());
						  JSONArray mainJsonArray=new JSONArray(rdao.subscribersViewBasedOnFunnelData("subscribers",campaignId,subscriberId).toString());
						  
						  System.out.println("mainJsonArray length : "+mainJsonArray.length());
						  out.println("mainJsonArray length : "+mainJsonArray.length());
						  JSONObject subscriber_campaign_jsonObject=(JSONObject) mainJsonArray.get(0);
						  JSONArray subscriber_campaign_jsonArray=subscriber_campaign_jsonObject.getJSONArray("subscriber_campaign");
						  //System.out.println("subscriber_campaign_jsonArray : "+subscriber_campaign_jsonArray);
						  System.out.println("subscriber_campaign_jsonArray length : "+subscriber_campaign_jsonArray.length());
						  out.println("subscriber_campaign_jsonArray length : "+subscriber_campaign_jsonArray.length());
						  //JSONArray jsonArray = (JSONArray) jsonObject.get("Data");
						  
						  //campaignclickstatistics_firstclick
						  //campaignclickstatistics_latestclick
						  //campaignclickstatistics_clicked
						  
						    int subscriber_click_count=0;
							String campaignclickstatistics_firstclick="";
							String campaignclickstatistics_latestclick="";
							String campaignclickstatistics_clicked="";
							
							String urlclickstatistics_firstclick="";
							String urlclickstatistics_latestclick="";
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
							String recent="NO";
							
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
								out.println("subscriberObject : "+subscriberObject);
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
								out.println("Before urlclickstatistics_latestclick");
								urlclickstatistics_latestclick=(String) subscriberObject.get("urlclickstatistics_latestclick");// string Date
								out.println("After urlclickstatistics_latestclick");
								//Checking For Recent Date Start
								
								if(!urlclickstatistics_latestclick.equals("null")){
									SimpleDateFormat date_formatter=new SimpleDateFormat("dd MMM yyyy HH:mm");
								    Date date_campare1 = new Date();
								    date_campare1.setDate(date_campare1.getDate()-7);
								    Date date1 = date_formatter.parse(date_formatter.format(date_campare1));
								    Date date2 = date_formatter.parse(urlclickstatistics_latestclick);
								    if (date2.compareTo(date1) > 0) {
								        System.out.println("Date2 is after Date1");
								        recent="YES";
								    } else if (date2.compareTo(date1) < 0) {
								        System.out.println("Date2 is before Date1");
								        recent="NO";
								    } else if (date2.compareTo(date1) == 0) {
								        System.out.println("Date2 is equal to Date1");
								        recent="YES";
								    }
								}
								
							    out.println("Is it Recent? : "+recent);
							    //Checking For Recent Date End
							    
								
								MongoDAO mdao=new MongoDAO();
								//org.apache.sling.commons.json.JSONArray campaignJsonArr=rdao.findSubscribersBasedOncampaignNameAndDimension2("","797","hi","harshita.indapurkar@bizlem.com");
								org.apache.sling.commons.json.JSONArray campaignJsonArr=rdao.findSubscribersBasedOncampaignNameAndDimension2("",campaign_id,Sling_Subject,subscriber_email);
								out.println("campaignJsonArr Length : "+campaignJsonArr.length());
								out.println("campaignJsonArr Length : "+campaignJsonArr);
								if(campaignJsonArr.length()!=0){
									out.println("inside campaignJsonArr.length()!=0");
									for(int i=0;i<campaignJsonArr.length();i++){
										JSONObject json_obj=new JSONObject(campaignJsonArr.get(i).toString());
										System.out.println("pagePath : "+json_obj.getString("pagePath"));
										    out.println("pagePath : "+json_obj.getString("pagePath"));
										
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
										    //jsonObject.put("Open",viewed);
											//jsonObject.put("Click",clicks);
											//jsonObject.put("Last_Click","2");
											//jsonObject.put("Bounce","No");
											//jsonObject.put("Session_time","715");
											//jsonObject.put("Last_Session_Time","725");
											//jsonObject.put("Pricing_URl","745");
											//jsonObject.put("Last_Pricing_URl",timeOnPage);
											//jsonObject.put("Own_Visit","0");
											//jsonObject.put("Last_Visit","0");
											//jsonObject.put("Own_Session_time","0");
											//jsonObject.put("Free_Trial","No");
											
											jsonObject.put("funnelName",funnelNodeName);
											jsonObject.put("subFunnelName",subFunnelNodeName);
											jsonObject.put("subscriber_email",subscriber_email);
											jsonObject.put("SubscriberId",subscriber_userid);//7113
											jsonObject.put("ListId",List_Id);
											jsonObject.put("CampaignId",campaign_id);
											//jsonObject.put("Category",subFunnelNodeName);
											jsonObject.put("CreatedBy",campaigncreate_by);
											jsonObject.put("OPTemp","Inform");
											jsonObject.put("Recent",recent);
											
											jsonObject.put("Campaign_Id",campaign_id);
											jsonObject.put("List_Id",List_Id);
											jsonObject.put("Category",subFunnelNodeName);
											jsonObject.put("Open",viewed);
											jsonObject.put("Click",clicks);
											jsonObject.put("Bounce",bounces);
											jsonObject.put("Session_time",sessionCount);
											jsonObject.put("Total_Session_time",sessiondurationBucket);
											jsonObject.put("URl",pagePath);
											jsonObject.put("Time_Spend_On_URl",timeOnPage);
											//jsonObject.put("Free_Trial","YES");
											jsonObject.put("Free_Trial",freetrial);
											jsonObject.put(pagePath,timeOnPage);
											//jsonObject.put("Invoice_Process_Count",invoice_process_count);
											
											
											
											out.println("input jsonObject for rule engine : "+jsonObject);
											//String rulr_engine_response=urlconnect("http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConvert_LACRules/fire",jsonObject);
											//String rulr_engine_response=urlconnect("http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConvertRules_RulesLAC/fire",jsonObject);
											String rulr_engine_response=urlconnect("http://34.74.125.253:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConverterNewRule_RuleUri/fire",jsonObject,response);
											//http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConvertRules_RulesLAC/fire
											//System.out.println(rulr_engine_response);
											out.println("rulr_engine_response : "+rulr_engine_response);
											JSONObject ruleEngineResponseJsonObject=new JSONObject(rulr_engine_response);
											if(ruleEngineResponseJsonObject.has("Output")){
												String leadStatus=ruleEngineResponseJsonObject.getString("Output");
												System.out.println("leadStatus : "+leadStatus);
												System.out.println("leadStatus : "+leadStatus);
												String url=ResourceBundle.getBundle("config").getString("list_subscriber_move_rulengine");
												sendPostRequestToManageSubscribers(url,ruleEngineResponseJsonObject.toString(),response);
											}else{
												System.out.println("I dont found any OutPut From Rule Engine");
											}
									}
								}else{
									        out.println("inside campaignJsonArr.length()==0");
									        ga_campaign = "";
											pagePath = "";
											sessionCount ="0"; 
											dimension2 = "";
											medium = "";
											sessiondurationBucket = "0";
											pageTitle = "";
											timeOnPage = "0";
											bounces = "";
											source = "";
											
											JSONObject jsonObject=new JSONObject();
											//jsonObject.put("Open",viewed);
											//jsonObject.put("Click",clicks);
											//jsonObject.put("Last_Click","2");
											//jsonObject.put("Bounce","No");
											//jsonObject.put("Session_time","715");
											//jsonObject.put("Last_Session_Time","725");
											//jsonObject.put("Pricing_URl","745");
											//jsonObject.put("Last_Pricing_URl",timeOnPage);
											//jsonObject.put("Own_Visit","0");
											//jsonObject.put("Last_Visit","0");
											//jsonObject.put("Own_Session_time","0");
											//jsonObject.put("Free_Trial","No");
																						
											jsonObject.put("funnelName",funnelNodeName);
											jsonObject.put("subFunnelName",subFunnelNodeName);
											jsonObject.put("subscriber_email",subscriber_email);
											jsonObject.put("SubscriberId",subscriber_userid);//7113
											jsonObject.put("ListId",List_Id);
											jsonObject.put("CampaignId",campaign_id);
											//jsonObject.put("Category",subFunnelNodeName);
											jsonObject.put("CreatedBy",campaigncreate_by);
											jsonObject.put("OPTemp","Inform");
											jsonObject.put("Recent",recent);
											
											jsonObject.put("Campaign_Id",campaign_id);
											jsonObject.put("List_Id",List_Id);
											jsonObject.put("Category",subFunnelNodeName);
											jsonObject.put("Open",viewed);
											jsonObject.put("Click",clicks);
											jsonObject.put("Bounce",bounces);
											jsonObject.put("Session_time",sessionCount);
											jsonObject.put("Total_Session_time",sessiondurationBucket);
											jsonObject.put("URl",pagePath);
											jsonObject.put("Time_Spend_On_URl",timeOnPage);
											//jsonObject.put("Free_Trial","YES");
											jsonObject.put("Free_Trial",freetrial);
											jsonObject.put(pagePath,timeOnPage);
											//jsonObject.put("Invoice_Process_Count",invoice_process_count);
											
											out.println("Inside else Step 1");
											out.println("jsonObject : "+jsonObject);
											String rulr_engine_response=urlconnect("http://34.74.125.253:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConverterNewRule_RuleUri/fire",jsonObject,response);
										    System.out.println(rulr_engine_response);
										    out.println("Inside else Step 2");
										    out.println("rulr_engine_response : "+rulr_engine_response);
											JSONObject ruleEngineResponseJsonObject=new JSONObject(rulr_engine_response);
											if(ruleEngineResponseJsonObject.has("Output")){
												String leadStatus=ruleEngineResponseJsonObject.getString("Output");
												out.println("leadStatus : "+leadStatus);
												String url=ResourceBundle.getBundle("config").getString("list_subscriber_move_rulengine");
												sendPostRequestToManageSubscribers(url,ruleEngineResponseJsonObject.toString(),response);
											}else{
												System.out.println("I dont found any OutPut From Rule Engine");
												out.println("I dont found any OutPut From Rule Engine");
											}
											
									
								}
							
						    }
				}
		    }catch (Throwable t) {
			  t.printStackTrace();
		    }
	}
	
	public static List<Campaign> createCampagign(JSONArray array,SlingHttpServletResponse response) {
		
		ArrayList<Campaign> campaignList =null;
		try{
		
		PrintWriter out = response.getWriter();
		campaignList = new ArrayList<Campaign>();
		//System.out.println(" List<Campaign> createCampagign   :"+array);
		
		String subscriberId;
		int mailBounceCount=0;;
		String subscribersEmail="jitendra.sen@bizlem.com"; 
	
		
		for(int i=0;i<array.length();i++) {
			
			
			int  campaignId=array.getJSONObject(0).getInt("id");
			String subject=array.getJSONObject(0).getString("subject");
			
			//System.out.println("campaignId  :"+campaignId +" subject   :"+subject);
			  out.println("campaignId  :"+campaignId +" subject   :"+subject);
			  JSONObject childJSONObject = array.getJSONObject(i);
			   
			   JSONArray subscribersJSON=childJSONObject.getJSONArray("subscribers");
			   for(int d=0;d<subscribersJSON.length();d++){
					Boolean openMail=false;
					Boolean deleverMail =false;
					
				   JSONObject subscriberObject = subscribersJSON.getJSONObject(d);
				   String  mailDelever=subscriberObject.getString("status");
//		              System.out.println("mailDelever   :"+mailDelever);
		              subscriberId=subscriberObject.getString("userid");
		              out.println(d+ " status  :"+mailDelever +" subscriberId   :"+subscriberId);
		              
		              Subscriber subscriber = createSubscriber(subscriberId, deleverMail, openMail, mailBounceCount);
                      campaignList.add(new Campaign(campaignId, "Unknown_1", subject, "Unknown","akhilesh307" , subscriber));
                      
                      /*
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
   		               */
			  }
			   /*
			   for (Campaign campaign : campaignList) {
				      int j=0;
				      //System.out.println("Subscriber id -> " + campaign.getSubscriber().getSubsriberId() + "\n"
						           //  + campaign.getSubscriber().toString());
				      String campId=Integer.toString(campaign.getCampaignId());
				      String subsId=campaign.getSubscriber().getSubsriberId();
				      out.println("campaignList : "+(++j)+ " campId  :"+campId +" subsId   :"+subsId);
			   }
			   */
	    }
		
	  } catch(Exception ex){
			
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
	public static String urlconnect(String urlstr, JSONObject json, SlingHttpServletResponse sling_response) throws IOException, JSONException {
		JSONObject jsonObject = null;
		StringBuffer response = null;

		try {
			PrintWriter out = sling_response.getWriter();
			int responseCode = 0;
			String urlParameters = "";
			out.println("urlconnect : 1");
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
			out.println("urlconnect : 2");
			responseCode = con.getResponseCode();
			out.println("responseCode : "+responseCode);
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
			String urlParameters,SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		//out.println("urlParameters :" + urlParameters);
		// URL url = new URL(callurl + urlParameters.replace("\\", ""));
		URL url = new URL(callurl);
		out.println("Url :" + callurl);
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
		out.println("POST Response Code :: " + responseCode);
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
			//System.out.println(buffer.toString());
			//out.println("Respnse Body : "+buffer.toString());
		} else {
			System.out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}
	
	public static String GetSubscriberEmailID(String subscriberID){
		StringBuilder builder1 = new StringBuilder();
        URL url;
        String subscribergetemail=null;
		try {
			url = new URL(ResourceBundle.getBundle("config").getString("subscribergetbyid")+subscriberID);
			URLConnection urlConnection = url.openConnection();
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	        String line;
	        while ((line = bufferedReader.readLine()) != null)
	        { 
	   	     String email=null;
	   	     builder1.append(line + "\n");
	        }
	        JSONObject subscribergetbyid_json_obj=new JSONObject(builder1.toString().replace("<pre>", ""));
	        JSONObject subscriberget_data_json_obj=(JSONObject) subscribergetbyid_json_obj.get("data");
	        subscribergetemail=subscriberget_data_json_obj.getString("email");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return subscribergetemail;

	}
	
}


