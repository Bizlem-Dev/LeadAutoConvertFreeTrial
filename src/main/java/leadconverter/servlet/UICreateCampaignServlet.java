package leadconverter.servlet;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jxl.*;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import leadconverter.impl.Searching_list_DaoImpl;

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
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.jsoup.Jsoup;
import org.osgi.service.http.HttpService;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
		@Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/createCampaign" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts",
				"cat", "latestproducts", "brief", "prodlist", "catalog",
				"viewcart", "productslist", "addcart", "createproduct",
				"checkmodelno", "productEdit" }) })
@SuppressWarnings("serial")
public class UICreateCampaignServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = new JSONObject();
	String unnsubscriber_link="<p><small><a href='[UNSUBSCRIBEURL]'>unsubscribe me</a></small></p>";

	@SuppressWarnings("deprecation")
	@Override
	protected void doPost(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		PrintWriter out = response.getWriter();
		JSONArray mainarray = new JSONArray();
		JSONObject jsonobject = new JSONObject();
		String listid = null;

		String remoteuser = request.getRemoteUser();

		try {
			Session session = null;

			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			//Node content = session.getRootNode().getNode("content");
			Node ip = session.getRootNode().getNode("content");

			if (request.getRequestPathInfo().getExtension().equals("CampaignNodeAdd")) {
				//out.println("CampaignNodeAdd Called");
				try {
					//String list_id = request.getParameter("list_id");

					long count_Value1 = 0;
					long funnel_count = 1;//this variable created by Akhil
					long sub_funnel_count = 1;//this variable created by Akhil
					Node campaignnode = null;
					Node funnelNode = null;//this variable created by Akhil
					Node subFunnelNode = null;//this variable created by Akhil
					Node content = session.getRootNode().getNode("content").getNode("user");
					Node content_ip = session.getRootNode().getNode("content");
					Node usernode = null;
					Node typenode = null;
					Node remoteusernode = null;
					Node leadconverternode = null;
					Node emailnode = null;
					String addcampaignnode = null;
					Node addcampaigninsubscribernode = null;
					
					String footer = request.getParameter("footer");
					       //footer=footer+unnsubscriber_link;
					String send_a_webpage_url = request.getParameter("send_a_webpage_url");
				    String compose_message_txt = request.getParameter("compose_message_txt");
				    String body = request.getParameter("ckcontent");
				           body=body+unnsubscriber_link;
				    String subject = request.getParameter("subject");
					String campaignName = request.getParameter("campaignName");
					String fromName = request.getParameter("fromName");
					String fromEmailAddress = request.getParameter("fromEmailAddress");
					String funnelName = request.getParameter("funnelName");
					String type = request.getParameter("SubFunnelName");
					String DistanceBtnCampaign = request.getParameter("DistanceBtnCampaign");
					String list_id = request.getParameter("listid");
					
					String fromfield="Akhilesh Yadav UI"; //request.getRemoteUser();
					String replyto = request.getRemoteUser();
					//String embargo ="2019-01-07 06:43:02 PM"; //request.getParameter("date"); 2019-01-07 06:43:02 PM
					String embargo ="";
					String campaignvalue = request.getParameter("campaignvalue");
					
					//out.println("body : "+body);
					String bodyvalue=URLEncoder.encode(body);
					//out.println("bodyvalue : "+bodyvalue);
					/*
					//String campaignaddurl = ip.getNode("ip").getProperty("Campaign_Add_Url").getString();
					String campaignaddurl = ResourceBundle.getBundle("config").getString("Campaign_Add_Url");
					//String campaignaddurl = ResourceBundle.getBundle("config").getString("Campaign_Add_Url_New");
					String campaignaddapiurlparameters = "?subject=" + subject + "&fromfield=" + fromfield + "&replyto="
							+ replyto
							+ "&message="+body+"&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo="
							+ embargo+"&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
					//out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
					String campaignresponse = this.sendpostdata(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
							.replace("<pre>", "");
					//out.println("campaignresponse : "+campaignresponse);
					*/
					String campaignaddurl = ResourceBundle.getBundle("config").getString("Campaign_Add_Url_New");
					String campaignaddapiurlparameters = "subject=" + subject + "&fromfield=" + fromfield + "&replyto="
							+ replyto
							+ "&message="+body+"&textmessage=hii&footer="+footer+"&status=draft&sendformat=html&template=&embargo="
							+ embargo+"&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
					//out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
					String campaignresponse = this.sendHttpPostData(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
							.replace("<pre>", "");
					
					
					JSONObject campaignjson = new JSONObject(campaignresponse);
					String campaignstatus = campaignjson.getString("status");
					JSONObject data = campaignjson.getJSONObject("data");
					String campaignid = data.getString("id");
					String campaign_status = data.getString("status");
					//status
					
					JSONObject res_json_obj=new JSONObject();
			                   res_json_obj.put("campaignid", campaignid);
			                   //res_json_obj.put("body", body);
			                   //res_json_obj.put("bodyvalue", bodyvalue);
			                   
			        out.println(res_json_obj.toString());
			        
			        String logged_in_user="viki_gmail.com";
					if (campaignstatus.equals(("success"))) {

						if (!content.hasNode(logged_in_user)) {
							usernode = content.addNode(logged_in_user);
				     	} else {
							usernode = content.getNode(logged_in_user);
						}
						if (!usernode.hasNode("Lead_Converter")) {
							leadconverternode = usernode.addNode("Lead_Converter");
						} else {
						    leadconverternode = usernode.getNode("Lead_Converter");
	   					}
						if (!leadconverternode.hasNode("Email")) {
							emailnode = leadconverternode.addNode("Email");
						} else {
							emailnode = leadconverternode.getNode("Email");
						}
						if (!emailnode.hasNode("Funnel")) {
							funnelNode = emailnode.addNode("Funnel");
						} else {
							funnelNode = emailnode.getNode("Funnel");
						}
						
						Node UserFunnelNode = null;//this variable created by Akhil
						Node UserSubFunnelNode = null;//this variable created by Akhil
						Node ListNode = null;//this variable created by Akhil
						Node ListCampaignNode = null;//this variable created by Akhil
						Node DraftListNode = null;
						Node ActiveListNode = null;
						Node ActiveListChildNode = null;
						if (!funnelNode.hasNode(funnelName)) {
							UserFunnelNode = funnelNode.addNode(funnelName);
						} else {
							UserFunnelNode = funnelNode.getNode(funnelName);
					    }
						int date_distance=0;
						if (!UserFunnelNode.hasNode(type)) {
							//out.println("if UserFunnelNode");
							campaignvalue=logged_in_user + "_" + type + "_"+ String.valueOf(count_Value1+1);
							UserSubFunnelNode = UserFunnelNode.addNode(type);
							//Setting Subfunnel property
							UserSubFunnelNode.setProperty("Counter", count_Value1);
						    UserSubFunnelNode.setProperty("Current_Campaign", campaignvalue);
							UserSubFunnelNode.setProperty("Distance", DistanceBtnCampaign);
                           	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//2019-03-15 01:00:00
							//System.out.println("Today's Date : "+dateFormat.format(new Date()));
							UserSubFunnelNode.setProperty("Current_Date", dateFormat.format(new Date()));
						    //UserSubFunnelNode.setProperty("No_of_days", noofdays);
							if(UserSubFunnelNode.hasNode("List")){
								//out.println("if List");
								
								ListNode=UserSubFunnelNode.getNode("List");
								if(ListNode.hasNode("DraftList")){
									DraftListNode=ListNode.getNode("DraftList");
									
								}else{
									DraftListNode=ListNode.addNode("DraftList");
								}
								if(ListNode.hasNode("ActiveList")){
									ActiveListNode=ListNode.getNode("ActiveList");
									if(type.equals("Explore")){
										ActiveListChildNode=ActiveListNode.addNode(list_id);
										if(ActiveListChildNode.hasNode("Campaign")){
											ListCampaignNode=ActiveListChildNode.getNode("Campaign");
											
										}else{
											ListCampaignNode=ActiveListChildNode.addNode("Campaign");
										}
									}else{
										
										String listname ="List_"+campaignvalue;
										//String listurl = content_ip.getNode("ip")
										//		.getProperty("List_Add_Url").getString();
										String listurl = ResourceBundle.getBundle("config").getString("List_Add_Url");

										String listparameter = "?name=" + listname
												+ "&description=This Belongs to " + "&listorder="
												+ 90 + "&active=" + 1;
										String listresponse = this.sendpostdata(listurl,
												listparameter.replace(" ", "%20"), response)
												.replace("<pre>", "");
										//out.println("List Response " + listresponse);
										JSONObject listjson = new JSONObject(listresponse);
										String liststatusresponse = listjson.getString("status");
										// out.println("List Status Response : " +
										// liststatusresponse);
										JSONObject getjsonid = listjson.getJSONObject("data");
										listid = getjsonid.getString("id");
										//out.println("Created Enitce list id : "+listid);
										ActiveListChildNode=ActiveListNode.addNode(listid);
										if(ActiveListChildNode.hasNode("Campaign")){
											ListCampaignNode=ActiveListChildNode.getNode("Campaign");
											
										}else{
											ListCampaignNode=ActiveListChildNode.addNode("Campaign");
										}
										
									}
								}else{
									ActiveListNode=ListNode.addNode("ActiveList");
									if(type.equals("Explore")){
										ActiveListChildNode=ActiveListNode.addNode(list_id);
										if(ActiveListChildNode.hasNode("Campaign")){
											ListCampaignNode=ActiveListChildNode.getNode("Campaign");
											
										}else{
											ListCampaignNode=ActiveListChildNode.addNode("Campaign");
										}
                                    }else{
                                    	String listname ="List_"+campaignvalue;
										//String listurl = content_ip.getNode("ip")
										//		.getProperty("List_Add_Url").getString();
										String listurl = ResourceBundle.getBundle("config").getString("List_Add_Url");

										String listparameter = "?name=" + listname
												+ "&description=This Belongs to " + "&listorder="
												+ 90 + "&active=" + 1;
										String listresponse = this.sendpostdata(listurl,
												listparameter.replace(" ", "%20"), response)
												.replace("<pre>", "");
										//out.println("List Response " + listresponse);
										JSONObject listjson = new JSONObject(listresponse);
										String liststatusresponse = listjson.getString("status");
										// out.println("List Status Response : " +
										// liststatusresponse);
										JSONObject getjsonid = listjson.getJSONObject("data");
										listid = getjsonid.getString("id");
										
										ActiveListChildNode=ActiveListNode.addNode(listid);
										if(ActiveListChildNode.hasNode("Campaign")){
											ListCampaignNode=ActiveListChildNode.getNode("Campaign");
											
										}else{
											ListCampaignNode=ActiveListChildNode.addNode("Campaign");
										}
										
									}
									
								}
							}else{
								//out.println("else List");
								ListNode=UserSubFunnelNode.addNode("List");
								if(ListNode.hasNode("DraftList")){
									DraftListNode=ListNode.getNode("DraftList");
									
								}else{
									DraftListNode=ListNode.addNode("DraftList");
								}
								if(ListNode.hasNode("ActiveList")){
									//out.println("if ActiveList");
									ActiveListNode=ListNode.getNode("ActiveList");
									if(type.equals("Explore")){
										//out.println("if Explore");
										ActiveListChildNode=ActiveListNode.addNode(list_id);
										if(ActiveListChildNode.hasNode("Campaign")){
											ListCampaignNode=ActiveListChildNode.getNode("Campaign");
											
										}else{
											ListCampaignNode=ActiveListChildNode.addNode("Campaign");
										}
									}else{
										//out.println("else Explore");
										String listname ="List_"+campaignvalue;
										//String listurl = content_ip.getNode("ip")
												//.getProperty("List_Add_Url").getString();
										String listurl = ResourceBundle.getBundle("config").getString("List_Add_Url");

										String listparameter = "?name=" + listname
												+ "&description=This Belongs to " + "&listorder="
												+ 90 + "&active=" + 1;
										String listresponse = this.sendpostdata(listurl,
												listparameter.replace(" ", "%20"), response)
												.replace("<pre>", "");
										//out.println("List Response " + listresponse);
										JSONObject listjson = new JSONObject(listresponse);
										String liststatusresponse = listjson.getString("status");
										// out.println("List Status Response : " +
										// liststatusresponse);
										JSONObject getjsonid = listjson.getJSONObject("data");
										listid = getjsonid.getString("id");
										
										ActiveListChildNode=ActiveListNode.addNode(listid);
										if(ActiveListChildNode.hasNode("Campaign")){
											ListCampaignNode=ActiveListChildNode.getNode("Campaign");
											
										}else{
											ListCampaignNode=ActiveListChildNode.addNode("Campaign");
										}
										
									}
								}else{
									//out.println("if ActiveList");
									ActiveListNode=ListNode.addNode("ActiveList");
									if(type.equals("Explore")){
										ActiveListChildNode=ActiveListNode.addNode(list_id);
										if(ActiveListChildNode.hasNode("Campaign")){
											ListCampaignNode=ActiveListChildNode.getNode("Campaign");
											
										}else{
											ListCampaignNode=ActiveListChildNode.addNode("Campaign");
										}
                                    }else{
                                    	String listname ="List_"+campaignvalue;
										//String listurl = content_ip.getNode("ip")
											//	.getProperty("List_Add_Url").getString();
										String listurl = ResourceBundle.getBundle("config").getString("List_Add_Url");

										String listparameter = "?name=" + listname
												+ "&description=This Belongs to " + "&listorder="
												+ 90 + "&active=" + 1;
										String listresponse = this.sendpostdata(listurl,
												listparameter.replace(" ", "%20"), response)
												.replace("<pre>", "");
										//out.println("List Response " + listresponse);
										JSONObject listjson = new JSONObject(listresponse);
										String liststatusresponse = listjson.getString("status");
										// out.println("List Status Response : " +
										// liststatusresponse);
										JSONObject getjsonid = listjson.getJSONObject("data");
										listid = getjsonid.getString("id");
										
										ActiveListChildNode=ActiveListNode.addNode(listid);
										if(ActiveListChildNode.hasNode("Campaign")){
											ListCampaignNode=ActiveListChildNode.getNode("Campaign");
											
										}else{
											ListCampaignNode=ActiveListChildNode.addNode("Campaign");
										}
										
									}
									
								}
							}
							date_distance=0;
							//out.println("step 1");
						} else {
							//out.println("else UserFunnelNode");
							UserSubFunnelNode = UserFunnelNode.getNode(type);
							
							count_Value1 = UserSubFunnelNode.getProperty("Counter").getLong();
						    UserSubFunnelNode.setProperty("Distance", DistanceBtnCampaign);
						    if(UserSubFunnelNode.hasProperty("Distance")){
						    	String distance=UserSubFunnelNode.getProperty("Distance").getString().replace(" Week", "");
						    	date_distance=Integer.parseInt(distance);
						    }else{
						    	date_distance=0;
						    }
						    
						    ListNode=UserSubFunnelNode.getNode("List");
						    
						    if(ListNode.hasNode("ActiveList")){
								ActiveListNode=ListNode.getNode("ActiveList");
								
								if(ActiveListNode.getNodes().hasNext()){
									//ActiveListChildNode=ActiveListNode.getNode(list_id);
									ActiveListChildNode=ActiveListNode.getNodes().nextNode();
									//out.println("ActiveListChildNode Name : "+ActiveListChildNode.getName());
								}
								
									
									if(ActiveListChildNode.hasNode("Campaign")){
										ListCampaignNode=ActiveListChildNode.getNode("Campaign");
										
									}else{
										ListCampaignNode=ActiveListChildNode.addNode("Campaign");
									}
								}
						    //out.println("step 2");
						}
						//out.println("step 3");
						String Campaign_Date=null;
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = new Date();
						if(date_distance==0){
							int date_difference=((int) count_Value1)*date_distance*7;
					        date.setDate(date.getDate()+date_difference);
					        Campaign_Date= dateFormat.format(date);
					    }else{
					    	int date_difference=((int) count_Value1)*date_distance*7;
					        date.setDate(date.getDate()+date_difference);
					        Campaign_Date= dateFormat.format(date);
					    }
						count_Value1 = count_Value1 + 1;
						UserSubFunnelNode.setProperty("Counter", count_Value1);
						addcampaignnode = logged_in_user + "_" + type + "_"
								+ String.valueOf(count_Value1);
						
						Node ListCampaignChildNode =ListCampaignNode.addNode(addcampaignnode);
						     ListCampaignChildNode.setProperty("Campaign_Id", campaignid);
						     ListCampaignChildNode.setProperty("List_Id", list_id);
						     ListCampaignChildNode.setProperty("campaign_status", campaign_status);
						     ListCampaignChildNode.setProperty("Campaign_Date", Campaign_Date);
						     //out.println("step 4");
						if (!UserSubFunnelNode.hasNode(logged_in_user + "_" + type + "_"
								+ String.valueOf(count_Value1))) {

							remoteusernode = UserSubFunnelNode.addNode(logged_in_user + "_" + type + "_"
									+ String.valueOf(count_Value1));

							remoteusernode.setProperty("CreatedBy", logged_in_user);
							remoteusernode.setProperty("Subject", subject);
							remoteusernode.setProperty("Body", body);
							remoteusernode.setProperty("Type", type);
							remoteusernode.setProperty("Campaign_Id", campaignid);
							remoteusernode.setProperty("List_Id", list_id);
							remoteusernode.setProperty("campaign_status", campaign_status);
							
							remoteusernode.setProperty("Campaign_Date", Campaign_Date);
						} else {
							remoteusernode = UserSubFunnelNode.getNode(logged_in_user + "_" + type + "_"
									+ String.valueOf(count_Value1));
                            
							remoteusernode.setProperty("CreatedBy", logged_in_user);
							remoteusernode.setProperty("Subject", subject);
							remoteusernode.setProperty("Body", body);
							remoteusernode.setProperty("Type", type);
							remoteusernode.setProperty("Campaign_Id", campaignid);
							remoteusernode.setProperty("List_Id", list_id);
							remoteusernode.setProperty("campaign_status", campaign_status);
							remoteusernode.setProperty("Campaign_Date", Campaign_Date);
						}

						// For Adding Campaign in SubscriberNode

						// url
						// http://localhost/restapi/campaign-list/listCampaignAdd.php?listid=2&campid=92
						
						//String campaignlisturl = ip.getNode("ip").getProperty("Campaign_List_Url").getString();
						String campaignlisturl = ResourceBundle.getBundle("config").getString("Campaign_List_Url");
						String campaignparameter = "?listid=" + list_id + "&campid=" + campaignid;
						String campaignlistresponse = this
								.sendpostdata(campaignlisturl, campaignparameter.replace(" ", "%20"), response)
								.replace("<pre>", "");
						// out.println("Campaign_List_Response : " + campaignlistresponse);
						//String subscriberdataurl = ip.getNode("ip").getProperty("Subscriber_Data_Url").getString();
						String subscriberdataurl = ResourceBundle.getBundle("config").getString("Subscriber_Data_Url");
						String subscriberdataparameters = "?list_id=" + list_id;
						String subscriberdataresponse = this
								.sendpostdata(subscriberdataurl, subscriberdataparameters, response).replace("<pre>", "");
						JSONObject subscriberdatajson = new JSONObject(subscriberdataresponse);
						JSONArray subscriberdata = subscriberdatajson.getJSONArray("data");
						for (int subscriberdataloop = 0; subscriberdataloop < subscriberdata
								.length(); subscriberdataloop++) {

							// Node For Adding Campaign Reference with subscribers
							JSONObject data_subscriber = subscriberdata.getJSONObject(subscriberdataloop);
							String subscriberemail = data_subscriber.getString("email");
							Node subscribernode = session.getRootNode().getNode("content").getNode("LEAD_CONVERTER")
									.getNode("SUBSCRIBER");

							Node campaigninemailnode = null;
							Node subscriberemailnode = null;
							Node campaigninsubscribernode = null;

							if (!subscribernode.hasNode(subscriberemail.replace("@", "_"))) {
								subscriberemailnode = subscribernode.addNode(subscriberemail.replace("@", "_"));
							} else {

								subscriberemailnode = subscribernode.getNode(subscriberemail.replace("@", "_"));

							}

							if (!subscriberemailnode.hasNode("Campaign")) {

								campaignnode = subscriberemailnode.addNode("Campaign");

							} else {

								campaignnode = subscriberemailnode.getNode("Campaign");

							}
							if (!campaignnode.hasNode(addcampaignnode)) {

								addcampaigninsubscribernode = campaignnode.addNode(addcampaignnode);
								addcampaigninsubscribernode.setProperty("CreatedBy",
										logged_in_user);
								addcampaigninsubscribernode.setProperty("Subject", subject);
								addcampaigninsubscribernode.setProperty("Body", body);
								addcampaigninsubscribernode.setProperty("Type", type);
								addcampaigninsubscribernode.setProperty("List_Id", list_id);
							}

							else {

								addcampaigninsubscribernode = campaignnode.getNode(addcampaignnode);

								addcampaigninsubscribernode.setProperty("CreatedBy",
										logged_in_user);
								addcampaigninsubscribernode.setProperty("Subject", subject);
								addcampaigninsubscribernode.setProperty("Body", body);
								addcampaigninsubscribernode.setProperty("Type", type);
								addcampaigninsubscribernode.setProperty("List_Id", list_id);

							}

						}
						
						session.save();
						//out.println("uploaded");

					}
					
					//request.getRequestDispatcher("/content/mainui/.findex").forward(request, response);
				}
				catch(Exception ex) {
					out.println("Message : "+ex.getMessage());
				}
				
			} else if (request.getRequestPathInfo().getExtension().equals("updateCampaign")) {
				//out.println("updateCampaign");
				//String id="569";
				String id = request.getParameter("id");
				String body = request.getParameter("ckcontent");
				String subject = request.getParameter("subject");
				//String type = request.getParameter("type");
				String fromfield="Akhilesh Yadav UI"; //request.getRemoteUser();
				String replyto = request.getRemoteUser();
				//String embargo ="2019-01-07 06:43:02 PM"; //request.getParameter("date"); 2019-01-07 06:43:02 PM
				String embargo =request.getParameter("embargo");
				String campaignvalue = request.getParameter("campaignvalue");
				
				String campaignName = request.getParameter("campaignName");
				String fromName = request.getParameter("fromName");
				String fromEmailAddress = request.getParameter("fromEmailAddress");
				String funnelName = request.getParameter("funnelName");
				String type = request.getParameter("SubFunnelName");
				String DistanceBtnCampaign = request.getParameter("DistanceBtnCampaign");
				String list_id = request.getParameter("listid");
				
				//String bodyvalue=URLEncoder.encode(body);
				//String campaignaddurl = ip.getNode("ip").getProperty("Campaign_Update_Url").getString();
				//String campaignaddurl = ResourceBundle.getBundle("config").getString("phplist_api_campaignUpdate");
				String campaignaddurl = ResourceBundle.getBundle("config").getString("embargoupdateurl");
				String campaignaddapiurlparameters = "id="+id+"&subject=" + subject;
				/*
				String campaignaddapiurlparameters = "?id="+id+"&subject=" + subject + "&fromfield=" + fromfield + "&replyto="
						+ replyto
						+ "&message="+body+"&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo="
						+ embargo+"&rsstemplate=&owner=1&htmlformatted=";
				
				String campaignaddapiurlparameters1 = "?id="+id+"&subject=" + subject + "&fromfield=" + fromfield + "&replyto="
						+ replyto
						+ "&message="+body+"&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo="
						+ embargo+"&rsstemplate=&owner=1&htmlformatted=";
				
				String campaignaddapiurlparameters2 = "?id="+id+"&subject=" + subject + "&fromfield=" + fromfield + "&replyto="
						+ replyto
						+ "&message="+body+"&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo="
						+ embargo+"&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
				
				String campaignresponse = this.sendpostdata(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
						.replace("<pre>", "");
				*/
				String campaignresponse = this.sendHttpPostData(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20")
						.replace("\r", "").replace("\n", ""), response);
				
				out.println("campaignresponse : "+campaignresponse);
				
				/*
				JSONObject campaignjson = new JSONObject(campaignresponse);
				String campaignstatus = campaignjson.getString("status");
				JSONObject data = campaignjson.getJSONObject("data");
				String campaignid = data.getString("id");
				
				JSONObject res_json_obj=new JSONObject();
		                   res_json_obj.put("campaignid", campaignid);
		                   //res_json_obj.put("body", body);
		                   //res_json_obj.put("bodyvalue", bodyvalue);
		                   
		        out.println(res_json_obj.toString());
                */
			}else if (request.getRequestPathInfo().getExtension().equals("updateEmbargo")) {

				try {
					JSONObject mainjson = new JSONObject();
					String campaignCatogory = request.getParameter("camp_catogery");
					
					String campaignid = request.getParameter("campaignid");
					String embargo = request.getParameter("embargo");
					
					String FName = request.getParameter("funnelName");
					//String SubFunnelName = request.getParameter("SubFunnelName");
					String subscriber_listid = request.getParameter("listid");
					
					//String id="569";
					String campaignaddurl = ResourceBundle.getBundle("config").getString("embargoupdateurl");
					String campaignaddapiurlparameters = "id="+campaignid+"&embargo=" + embargo;
					 out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
					String campaignlistresponse = this.sendHttpPostData(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20"), response)
							.replace("<pre>", "");
					/*
					out.println("campaignresponse : "+campaignlistresponse);
					String status_response=this.campaignStatusUpdate(campaignid,"submitted",response);
					out.println("status_response : "+status_response);
					*/
					
					NodeIterator useritr = session.getRootNode().getNode("content").getNode("user").getNodes();
					while (useritr.hasNext()) {
						Node usernode = useritr.nextNode();
						out.println("updateCampaignDate : Step 1 usernode Name "+usernode.getName());
						if(!usernode.getName().equals("<%=request.getRemoteUser()%>")){
						if (usernode.hasNode("Lead_Converter")) {
							    if(usernode.getNode("Lead_Converter").getNode("Email")
										.getNode("Funnel").hasNode(FName)){
							    out.println("updateCampaignDate : Step 1");
							    Node funnelNode = usernode.getNode("Lead_Converter").getNode("Email")
										.getNode("Funnel").getNode(FName);
							    out.println("updateCampaignDate : Step 1.1");
								String funnelName = funnelNode.getName();// currentcampaign
								out.println("updateCampaignDate : Step 1.1 funnelName : "+funnelName);
								  if(funnelNode.hasNode(campaignCatogory)){
									    out.println("updateCampaignDate : Step 2");
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
																	    if(ActiveListChildCampaignChildNode.hasProperty("Campaign_Date")){
																	    	ActiveListChildCampaignChildNode.setProperty("Campaign_Date", embargo);
																	    }
																   }
														    }
													   }
												}
											}
										}
										NodeIterator subFunnelNodeItr = subFunnelNode.getNodes();
										while (subFunnelNodeItr.hasNext()) {
										    Node ActiveListChildNode = subFunnelNodeItr.nextNode();
										    if(ActiveListChildNode.hasProperty("Campaign_Id")){
										    	String Temp_Campaign_Id=ActiveListChildNode.getProperty("Campaign_Id").getString();
										    	if(Temp_Campaign_Id.equals(campaignid)){
										    	  out.println("ActiveListChildCampaignChildNode Name : "+ActiveListChildNode.getName());
										    	  ActiveListChildNode.setProperty("Campaign_Date", embargo);
										    	}
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
					} catch (Exception e) {
						// TODO Auto-generated catch block
						out.println("Exception ex :=" + ex.getMessage() + ex.getCause());
					}
					
				}
				
			}else if (request.getRequestPathInfo().getExtension().equals("addCampaign")) {
				String body = request.getParameter("ckcontent");
				String subject = request.getParameter("subject");
				//String type = request.getParameter("type");
				String fromfield="Akhilesh Yadav UI"; //request.getRemoteUser();
				String replyto = request.getRemoteUser();
				//String embargo ="2019-01-07 06:43:02 PM"; //request.getParameter("date"); 2019-01-07 06:43:02 PM
				String embargo ="";
				String campaignvalue = request.getParameter("campaignvalue");
				
				String campaignName = request.getParameter("campaignName");
				String fromName = request.getParameter("fromName");
				String fromEmailAddress = request.getParameter("fromEmailAddress");
				String funnelName = request.getParameter("funnelName");
				String type = request.getParameter("SubFunnelName");
				String DistanceBtnCampaign = request.getParameter("DistanceBtnCampaign");
				String list_id = request.getParameter("listid");
				
				//out.println("body : "+body);
				String bodyvalue=URLEncoder.encode(body);
				//out.println("bodyvalue : "+bodyvalue);
				//String campaignaddurl = ip.getNode("ip").getProperty("Campaign_Add_Url").getString();
				String campaignaddurl = ResourceBundle.getBundle("config").getString("Campaign_Add_Url_New");
				String campaignaddapiurlparameters = "subject=" + subject + "&fromfield=" + fromfield + "&replyto="
						+ replyto
						+ "&message="+body+"&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo="
						+ embargo+"&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
				//out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
				String campaignresponse = this.sendHttpPostData(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
						.replace("<pre>", "");
				out.println("campaignresponse : "+campaignresponse);
			}else if (request.getRequestPathInfo().getExtension().equals("placeCampaignInQueueforSending1")) {
				
				String id = request.getParameter("id");
				String status = request.getParameter("status");
				String campaignaddurl = ResourceBundle.getBundle("config").getString("campaignStatusUpdate");
				String campaignaddapiurlparameters = "id=" + id + "&status=" + status;
				String campaignresponse = this.sendHttpPostData(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
						.replace("<pre>", "");
				out.println("campaignresponse : "+campaignresponse);
			}else if (request.getRequestPathInfo().getExtension().equals("placeCampaignInQueueforSending")) {
				//out.println("placeCampaignInQueueforSending2 step 1 ");
				String id = request.getParameter("id");
				String status = request.getParameter("status");
				//out.println("placeCampaignInQueueforSending2 step 1  id : "+id+"  status : "+status);
				String campaignaddurl = ResourceBundle.getBundle("config").getString("campaignStatusUpdate");
				String campaignaddapiurlparameters = "?id=" + id + "&status=" + status;
				//out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
				String campaignresponse = this.sendpostdata(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
						.replace("<pre>", "");
				//out.println("placeCampaignInQueueforSending2 step 3 : ");
				out.println("campaignresponse : "+campaignresponse);
			}else if (request.getRequestPathInfo().getExtension().equals("processQueue")) {
				
				String campid =request.getParameter("campid");
				String campaignlisturl = ResourceBundle.getBundle("config").getString("processqueue");
				//String campaignparameter = "?listid=" + "680" + "&campid=" + "650";
				String campaignparameter = "?campid=" +campid;
				String campaignlistresponse = this.sendpostdata(campaignlisturl, campaignparameter.replace(" ", "%20"),response)
						.replace("<pre>", "");
				out.println("Process Queue Response :" + campaignlistresponse);
			}

		} catch (Exception e) {

			out.println("Exception ex : : : " + e.getStackTrace());
		}

	}
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		PrintWriter out = response.getWriter();
		JSONArray mainarray = new JSONArray();
		JSONObject jsonobject = new JSONObject();
		String listid = null;

		String remoteuser = request.getRemoteUser();
		
		if (request.getRequestPathInfo().getExtension().equals("processQueue")) {
			
			String campaignlisturl = ResourceBundle.getBundle("config").getString("processqueue");
			//String campaignparameter = "?listid=" + "680" + "&campid=" + "650";
			String campaignparameter = "?campid=680"; //unuse parameter
			String campaignlistresponse = this.sendpostdata(campaignlisturl, campaignparameter.replace(" ", "%20"),response)
					.replace("<pre>", "");
			out.println("Process Queue Response :" + campaignlistresponse);
		}else if (request.getRequestPathInfo().getExtension().equals("updateCampaignDate")) {
			try {
				
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				out.println("updateCampaignDate : Step 0");
				JSONObject mainjson = new JSONObject();
				String campaignCatogory = request.getParameter("camp_catogery");
				String campaignid = request.getParameter("campaignid");
				String embargo = request.getParameter("embargo");
				out.println("updateCampaignDate : Step 0.1");
				String FName = request.getParameter("funnelName");
				//String SubFunnelName = request.getParameter("SubFunnelName");
				String subscriber_listid = request.getParameter("listid");
				out.println("updateCampaignDate : Step 0.2 FName FName : "+FName);
				
				
				NodeIterator useritr = session.getRootNode().getNode("content").getNode("user").getNodes();
				while (useritr.hasNext()) {
					Node usernode = useritr.nextNode();
					out.println("updateCampaignDate : Step 1 usernode Name "+usernode.getName());
					if(!usernode.getName().equals("<%=request.getRemoteUser()%>")){
					if (usernode.hasNode("Lead_Converter")) {
						    if(usernode.getNode("Lead_Converter").getNode("Email")
									.getNode("Funnel").hasNode(FName)){
						    out.println("updateCampaignDate : Step 1");
						    Node funnelNode = usernode.getNode("Lead_Converter").getNode("Email")
									.getNode("Funnel").getNode(FName);
						    out.println("updateCampaignDate : Step 1.1");
							String funnelName = funnelNode.getName();// currentcampaign
							out.println("updateCampaignDate : Step 1.1 funnelName : "+funnelName);
							  if(funnelNode.hasNode(campaignCatogory)){
								    out.println("updateCampaignDate : Step 2");
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
																    if(ActiveListChildCampaignChildNode.hasProperty("Campaign_Date")){
																    	ActiveListChildCampaignChildNode.setProperty("Campaign_Date", embargo);
																    }
															   }
													    }
												   }
											}
										}
									}
									NodeIterator subFunnelNodeItr = subFunnelNode.getNodes();
									while (subFunnelNodeItr.hasNext()) {
									    Node ActiveListChildNode = subFunnelNodeItr.nextNode();
									    if(ActiveListChildNode.hasProperty("Campaign_Id")){
									    	String Temp_Campaign_Id=ActiveListChildNode.getProperty("Campaign_Id").getString();
									    	if(Temp_Campaign_Id.equals(campaignid)){
									    	  out.println("ActiveListChildCampaignChildNode Name : "+ActiveListChildNode.getName());
									    	  ActiveListChildNode.setProperty("Campaign_Date", embargo);
									    	}
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
				} catch (Exception e) {
					// TODO Auto-generated catch block
					out.println("Exception ex :=" + ex.getMessage() + ex.getCause());
				}
				
			}
		}
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

	public String sendpostdataToCreateList(String callurl,
			String urlParameters, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
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
