package leadconverter.manage.campign;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jxl.*;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.jsoup.Jsoup;
import org.osgi.service.http.HttpService;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/campaignactivator" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class CampaignActivator extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repos;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = null;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		if (request.getRequestPathInfo().getExtension().equals("activate")) {
			try {
				Session session = null;
				session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));
				JSONObject mainjson = new JSONObject();
				JSONArray mainjsonArray = new JSONArray();
				JSONObject datajson = null;
				String campaignCatogory = request.getParameter("camp_catogery");
				String days = request.getParameter("days");
				
				NodeIterator useritr = session.getRootNode().getNode("content").getNode("user").getNodes();
				while (useritr.hasNext()) {
					datajson = new JSONObject();
					Node usernode = useritr.nextNode();
					if(!usernode.getName().equals("<%=request.getRemoteUser()%>")){
					if (usernode.hasNode("Lead_Converter")) {
						NodeIterator funnelitr = usernode.getNode("Lead_Converter").getNode("Email")
								.getNode("Funnel").getNodes();
						while (funnelitr.hasNext()) {
							Node funnelNode = funnelitr.nextNode();
							String funnelName = funnelNode.getName();// currentcampaign
								if(funnelNode.hasNode(campaignCatogory)){
									Node subFunnelNode=funnelNode.getNode(campaignCatogory);
									String Current_Date="";
									String Current_Campaign="";
									String current_campaign_status=null;
									if(subFunnelNode.hasProperty("Current_Date")){
										Current_Date=subFunnelNode.getProperty("Current_Date").getString();
									}
									if(subFunnelNode.hasProperty("Current_Campaign")){
										Current_Campaign=subFunnelNode.getProperty("Current_Campaign").getString();
										//Checking Current Campaign Status
										if(subFunnelNode.hasNode(Current_Campaign)){
											if(subFunnelNode.getNode(Current_Campaign).hasProperty("campaign_status")){
												current_campaign_status=subFunnelNode.getNode(Current_Campaign).getProperty("campaign_status").getString();
											}
										}
									}
									
									if(subFunnelNode.hasNodes()){
										   NodeIterator campaignsitr = subFunnelNode.getNodes();
										   while (campaignsitr.hasNext()) {
											    datajson = new JSONObject();
											    Node campaignnode = campaignsitr.nextNode();
											    String campaignName = campaignnode.getName();
											   //out.println("campaignName : "+campaignName);
											   
											    String Campaign_Date=null;
											    String campaign_status=null;
											    
											    
											    String Campaign_Id = campaignnode.getProperty("Campaign_Id").getString();
												String List_Id = campaignnode.getProperty("List_Id").getString();
												String createdby = campaignnode.getProperty("CreatedBy").getString();
												
											    if(campaignnode.hasProperty("campaign_status")){
											    	campaign_status=campaignnode.getProperty("campaign_status").getString();
											    }
											    if(campaignnode.hasProperty("Campaign_Date")){
											    	Campaign_Date=campaignnode.getProperty("Campaign_Date").getString();
											    	
											    	
											    	
											    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
													Date today=new Date();
													today.setDate(today.getDate()+Integer.parseInt(days));
												    Date date1 = sdf1.parse(sdf1.format(today));
												    Date date2 = sdf1.parse(Campaign_Date);
												    //out.println("Current_Date : " + Current_Date+"   Current_Campaign : " + Current_Campaign);
												    out.println("Current_Date : " + Current_Date+"   campaignName : " + campaignName);
												    out.println("date1 : " + sdf1.format(date1));
												    out.println("date2 : " + sdf1.format(date2));
												    
												    if (date1.compareTo(date2) > 0) {
												        out.println("Date1 is after Date2");
												    } else if (date1.compareTo(date2) < 0) {
												        out.println("Date1 is before Date2");
												    } else if (date1.compareTo(date2) == 0) {
												    	out.println("Date1 is equal to Date2");
												        out.println("campaignName : "+campaignName);
												        out.println("current_campaign_status : "+current_campaign_status);
												    	if(current_campaign_status.equals("sent")){
												    		subFunnelNode.setProperty("Current_Campaign",campaignName);
												    	}else{
												    		out.println("Current Campaign Status is Not equal to Sent");
												    	}
												        
												    } else {
												        out.println("How to get here?");
												    }
											    }
											    //mainjsonArray.put(datajson);
												//mainjson.put("Data", mainjsonArray);
												//out.println(mainjson);
											   
										   }
									 }
							     }
						}
					  }
					}
				
				}
				out.println(mainjson);
				session.save();
			} catch (Exception ex) {
				//out.println("Exception ex :=" + ex.getMessage() + ex.getCause());
				
				try {
					JSONObject errordatajson = new JSONObject();
					errordatajson.put("Error", "Null");
					errordatajson.put("Exception", ex.getMessage().toString());
					errordatajson.put("Cause", ex.getCause().toString());
					out.println(errordatajson);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					out.println("Exception ex :=" + ex.getMessage() + ex.getCause());
				}
				
			}
		} else if (request.getRequestPathInfo().getExtension().equals("activate_new")) {
			try {
				Session session = null;
				session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));
				JSONObject mainjson = new JSONObject();
				JSONArray mainjsonArray = new JSONArray();
				JSONObject datajson = null;
				String campaignCatogory = request.getParameter("camp_catogery");
				String catogoryFilter = request.getParameter("catogery_filter");
				String days = request.getParameter("days");
				
				NodeIterator useritr = session.getRootNode().getNode("content").getNode("user").getNodes();
				while (useritr.hasNext()) {
					datajson = new JSONObject();
					Node usernode = useritr.nextNode();
					if(!usernode.getName().equals("<%=request.getRemoteUser()%>")){
					if (usernode.hasNode("Lead_Converter")) {
						NodeIterator funnelitr = usernode.getNode("Lead_Converter").getNode("Email")
								.getNode("Funnel").getNodes();
						while (funnelitr.hasNext()) {
							Node funnelNode = funnelitr.nextNode();
							String funnelName = funnelNode.getName();// currentcampaign
							if(catogoryFilter.equals("single")){
								if(funnelNode.hasNode(campaignCatogory)){
									Node subFunnelNode=funnelNode.getNode(campaignCatogory);
									out.println("subFunnelNode Name : "+subFunnelNode.getName());
									if(subFunnelNode.hasNode("List")){
										Node ListNode=subFunnelNode.getNode("List");
										if(ListNode.hasNode("ActiveList")){
											Node ActiveListNode=ListNode.getNode("ActiveList");
											
											if(ActiveListNode.hasNodes()){
												   NodeIterator ActiveListNodeItr = ActiveListNode.getNodes();
												   while (ActiveListNodeItr.hasNext()) {
													    Node ActiveListChildNode = ActiveListNodeItr.nextNode();
													    if(ActiveListChildNode.hasNode("Campaign")){
													    	Node ActiveListChildCampaignNode = ActiveListChildNode.getNode("Campaign");
													    	NodeIterator ActiveListChildCampaignNodeItr = ActiveListChildCampaignNode.getNodes();
															   while (ActiveListChildCampaignNodeItr.hasNext()) {
																    Node ActiveListChildCampaignChildNode = ActiveListChildCampaignNodeItr.nextNode();
																    out.println("ActiveListChildCampaignChildNode Name : "+ActiveListChildCampaignChildNode.getName());
															   }
													    }
												   }
											}
										}
									}
								}
							}else if(catogoryFilter.equals("all")){
								 if(funnelNode.hasNodes()){
									   NodeIterator campaignNodesitr = funnelNode.getNodes();
									   while (campaignNodesitr.hasNext()) {
										   Node subFunnelNode = campaignNodesitr.nextNode();
										   String subFunnelNodeName = subFunnelNode.getName();
										   out.println("subFunnelNodeName : "+subFunnelNodeName);
										   if(subFunnelNode.hasNode("List")){
												Node ListNode=subFunnelNode.getNode("List");
												if(ListNode.hasNode("ActiveList")){
													Node ActiveListNode=ListNode.getNode("ActiveList");
													
													if(ActiveListNode.hasNodes()){
														   NodeIterator ActiveListNodeItr = ActiveListNode.getNodes();
														   while (ActiveListNodeItr.hasNext()) {
															    Node ActiveListChildNode = ActiveListNodeItr.nextNode();
															    if(ActiveListChildNode.hasNode("Campaign")){
															    	Node ActiveListChildCampaignNode = ActiveListChildNode.getNode("Campaign");
															    	NodeIterator ActiveListChildCampaignNodeItr = ActiveListChildCampaignNode.getNodes();
																	   while (ActiveListChildCampaignNodeItr.hasNext()) {
																		    Node ActiveListChildCampaignChildNode = ActiveListChildCampaignNodeItr.nextNode();
																		    String Current_Date="";
																			String Current_Campaign="";
																			String current_campaign_status=null;
																			
																			String Campaign_Date=null;
																		    String campaign_status=null;
																		    String Campaign_Id = null;
																			String List_Id = null;
																			String Active_List_Id = null;
																			if(ActiveListChildCampaignChildNode.hasProperty("Campaign_Date")){
																			    Campaign_Date=ActiveListChildCampaignChildNode.getProperty("Campaign_Date").getString();
																			}
																			if(ActiveListChildCampaignChildNode.hasProperty("campaign_status")){
																				campaign_status=ActiveListChildCampaignChildNode.getProperty("campaign_status").getString();
																			}
																			if(ActiveListChildCampaignChildNode.hasProperty("Campaign_Id")){
																				Campaign_Id=ActiveListChildCampaignChildNode.getProperty("Campaign_Id").getString();
																			}
																			if(ActiveListChildCampaignChildNode.hasProperty("List_Id")){
																				List_Id=ActiveListChildCampaignChildNode.getProperty("List_Id").getString();
																			}
																			Active_List_Id=ActiveListChildNode.getName();
																			out.println("ActiveListChildCampaignChildNode Name : "+ActiveListChildCampaignChildNode.getName());
																			out.println("Active_List_Id Name : "+Active_List_Id);
																			
																			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
																			Date today=new Date();
																			today.setDate(today.getDate()+Integer.parseInt(days));
																		    Date date1 = sdf1.parse(sdf1.format(today));
																		    Date date2 = sdf1.parse(Campaign_Date);
																		    //out.println("Current_Date : " + Current_Date+"   Current_Campaign : " + Current_Campaign);
																		    //out.println("Current_Date : " + Current_Date);
																		    //out.println("Campaign_Date : " + Campaign_Date);
																		    out.println("Current_Date : " + sdf1.format(date1)+"   Campaign_Date : " + sdf1.format(date2));
																		    if (date1.compareTo(date2) > 0) {
																		        out.println("Date1 is after Date2");
																		    } else if (date1.compareTo(date2) < 0) {
																		        out.println("Date1 is before Date2");
																		    } else if (date1.compareTo(date2) == 0) {
																		    	out.println("Date1 is equal to Date2");
																		    	
																		    	ActiveListChildNode.setProperty("currentCampign", ActiveListChildCampaignChildNode.getName());
																		    	ActiveListChildNode.setProperty("Campaign_Date", Campaign_Date);
																		    	
																		    	ActiveListNode.setProperty("currentCampign", ActiveListChildCampaignChildNode.getName());
																		    	ActiveListNode.setProperty("Campaign_Date", Campaign_Date);
																		    	
																		        out.println("campaignName : "+ActiveListChildCampaignChildNode.getName());
																		        out.println("current_campaign_status : "+campaign_status);
																		    	if(!campaign_status.equals("sent")){
																		    		out.println("===========================Active==============================");
																		    		out.println("Active Campaign : "+ActiveListChildCampaignChildNode.getName());
																		    		out.println("Active Campaign_Id : "+Campaign_Id);
																		    		out.println("Active Active_List_Id : "+Active_List_Id);
																		    		out.println("===========================Active==============================");
																		    		
																		    		// This method call will delete List From Campaign
																		    		String campaign_list_delete_response=this.campaignListDelete(List_Id,Campaign_Id,response);
																		    		out.println("campaign_list_delete_response : "+campaign_list_delete_response);
																		    		
																		    		// This method call will Add List to Campaign
																		    		String campaign_list_add_response=this.campaignListAdd(Active_List_Id,Campaign_Id,response);
																		    		out.println("campaign_list_add_response : "+campaign_list_add_response);
																		    		
																		    		// This method call will Update Embargo(Campaign send date & time) of Campaign
																		    		String id = Campaign_Id;
																					String embargo = Campaign_Date;
																					
																					
																					//Checking Campaign status for setting campaign status
																					String campaigngetbyid_url=ResourceBundle.getBundle("config").getString("campaigngetbyid");
																					String campaigngetbyid_url_parameter="?id="+id;
																					String campaigngetbyid_url_response=sendpostdata(campaigngetbyid_url,campaigngetbyid_url_parameter,response);
																					JSONObject response_data_json_obj=(JSONObject) new JSONObject(campaigngetbyid_url_response.replace("<pre>", "")).get("data");
																					  String status=response_data_json_obj.getString("status");
																					  
																					  if(status.equals("sent") || status.equals("submitted")){
																						  System.out.println("No need to Anything : "+status);
																						  if(status.equals("sent")){
																							  String status_response=this.campaignStatusUpdate(id,"submitted",response);
																							  out.println("status_response reque: "+status_response);
																						  }
																					  }else{
																						  System.out.println("Do your stuff here "+status);
																						    String embargoupdateurl = ResourceBundle.getBundle("config").getString("embargoupdateurl");
																							String embargoupdateurlparameters = "id="+id+"&embargo=" + embargo;
																							//out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
																							String campaignlistresponse = this.sendHttpPostData(embargoupdateurl, embargoupdateurlparameters.replace(" ", "%20"), response)
																									.replace("<pre>", "");
																							out.println("campaignresponse : "+campaignlistresponse);
																							// This method call will Activate The Campaign for sending
																							String status_response=this.campaignStatusUpdate(id,"submitted",response);
																							out.println("status_response : "+status_response);
																				      }
																		    		
																					// This method call will Process the Queue(May be need Thread)
																					/*
																					String campaignlisturl = ResourceBundle.getBundle("config").getString("processqueue");
																					String campaignparameter = "?campid=100";
																					String campaignlistresponse1 = this.sendpostdata(campaignlisturl, campaignparameter.replace(" ", "%20"),response)
																							.replace("<pre>", "");
																					out.println("Process Queue Response :" + campaignlistresponse);
																					*/
																					
																		    	}else{
																		    		out.println("Current Campaign Status is Not equal to Sent");
																		    	}
																		    	
																		        
																		    } else {
																		        out.println("How to get here?");
																		    }
																		    
																	   }
															    }
														   }
													}
												}
											}
									   }
									}
						     }else{
						    	 mainjson.put("Warn", "Please Provide Proper Filter And Category");
								 //out.println(mainjson); 
								 break;
						     }
						}
					  }
					}
				
				}
				out.println(mainjson);
				session.save();
			} catch (Exception ex) {
				//out.println("Exception ex :=" + ex.getMessage() + ex.getCause());
				
				try {
					JSONObject errordatajson = new JSONObject();
					errordatajson.put("Error", "Null");
					errordatajson.put("Exception", ex.getMessage().toString());
					errordatajson.put("Cause", ex.getCause().toString());
					out.println(errordatajson);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					out.println("Exception ex :=" + ex.getMessage() + ex.getCause());
				}
				
			}
		}
	}

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

	}
	
	public String campaignStatusUpdate(String id,String status,SlingHttpServletResponse response) throws ServletException,
	IOException {
		String campaignaddurl = ResourceBundle.getBundle("config").getString("campaignStatusUpdate");
		String campaignaddapiurlparameters = "?id=" + id + "&status=" + status;
		//out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
		String campaignresponse = this.sendpostdata(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
				.replace("<pre>", "");
		return campaignresponse;
	}
	//listCampaignAdd
	public String campaignListAdd(String listid,String campid,SlingHttpServletResponse response) throws ServletException,
	IOException {
		String campaignaddurl = ResourceBundle.getBundle("config").getString("listCampaignAdd");
		String campaignaddapiurlparameters = "?listid=" + listid + "&campid=" + campid;
		//out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
		String campaignresponse = this.sendpostdata(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
				.replace("<pre>", "");
		return campaignresponse;
	}
	//listCampaignDelete
		public String campaignListDelete(String listid,String campid,SlingHttpServletResponse response) throws ServletException,
		IOException {
			String campaignaddurl = ResourceBundle.getBundle("config").getString("listCampaignDelete");
			String campaignaddapiurlparameters = "?listid=" + listid + "&campid=" + campid;
			//out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
			String campaignresponse = this.sendpostdata(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
					.replace("<pre>", "");
			return campaignresponse;
		}
	
	public String sendHttpPostData(String campaignaddurl,String campaignaddapiurlparameters,SlingHttpServletResponse response) throws ServletException,
	IOException {
        
		PrintWriter out = response.getWriter();
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
	
	public String sendpostdata(String callurl, String urlParameters,
			SlingHttpServletResponse response) throws ServletException,
			IOException {

		PrintWriter out = response.getWriter();
		//out.println("inside sendpostdata urlParameters :" + urlParameters);
		URL url = new URL(callurl + urlParameters.replace("\\", ""));
		//out.println("inside sendpostdata Url :" + url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		//
		OutputStream writer = conn.getOutputStream();

		writer.write(urlParameters.getBytes());
		// out.println("Writer Url : "+writer);
		int responseCode = conn.getResponseCode();
		//out.println("POST Response Code :: " + responseCode);
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
			// out.println(buffer.toString());
		} else {
			out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}
}

