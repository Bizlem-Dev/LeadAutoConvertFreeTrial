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
					Node usernode = null;
					Node typenode = null;
					Node remoteusernode = null;
					Node leadconverternode = null;
					Node emailnode = null;
					String addcampaignnode = null;
					Node addcampaigninsubscribernode = null;
					
					String footer = request.getParameter("footer");
					String send_a_webpage_url = request.getParameter("send_a_webpage_url");
				    String compose_message_txt = request.getParameter("compose_message_txt");
				    String body = request.getParameter("ckcontent");
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
					String campaignaddurl = ip.getNode("ip").getProperty("Campaign_Add_Url").getString();
					//String campaignaddurl = "http://35.221.31.185/phplist_api/campaign_api.php?cmd=campaignAdd";
					String campaignaddapiurlparameters = "?subject=" + subject + "&fromfield=" + fromfield + "&replyto="
							+ replyto
							+ "&message="+body+"&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo="
							+ embargo+"&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
					//out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
					String campaignresponse = this.sendpostdata(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
							.replace("<pre>", "");
					//out.println("campaignresponse : "+campaignresponse);
					*/
					String campaignaddurl = "http://35.221.31.185/phplist_api/campaign_api.php?cmd=campaignAdd";
					String campaignaddapiurlparameters = "subject=" + subject + "&fromfield=" + fromfield + "&replyto="
							+ replyto
							+ "&message="+body+"&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo="
							+ embargo+"&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
					//out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
					String campaignresponse = this.sendHttpPostData(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
							.replace("<pre>", "");
					
					
					JSONObject campaignjson = new JSONObject(campaignresponse);
					String campaignstatus = campaignjson.getString("status");
					JSONObject data = campaignjson.getJSONObject("data");
					String campaignid = data.getString("id");
					
					JSONObject res_json_obj=new JSONObject();
			                   res_json_obj.put("campaignid", campaignid);
			                   res_json_obj.put("body", body);
			                   res_json_obj.put("bodyvalue", bodyvalue);
			                   
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
						if (!funnelNode.hasNode(funnelName)) {
							UserFunnelNode = funnelNode.addNode(funnelName);
						} else {
							UserFunnelNode = funnelNode.getNode(funnelName);
					    }
						if (!UserFunnelNode.hasNode(type)) {

							UserSubFunnelNode = UserFunnelNode.addNode(type);
							UserSubFunnelNode.setProperty("Counter", count_Value1);
						//	typenode.setProperty("Current_Date", currentdate);
							UserSubFunnelNode.setProperty("Current_Campaign", campaignvalue);
						//	typenode.setProperty("No_of_days", noofdays);
					
						} else {

							UserSubFunnelNode = UserFunnelNode.getNode(type);
							count_Value1 = UserSubFunnelNode.getProperty("Counter").getLong();
							
							
						//	typenode.setProperty("Current_Date", currentdate);
							UserSubFunnelNode.setProperty("Current_Campaign", campaignvalue);
						//	typenode.setProperty("No_of_days", noofdays);

						}
						count_Value1 = count_Value1 + 1;
						UserSubFunnelNode.setProperty("Counter", count_Value1);
						addcampaignnode = logged_in_user + "_" + type + "_"
								+ String.valueOf(count_Value1);
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

						} else {

							remoteusernode = UserSubFunnelNode.getNode(logged_in_user + "_" + type + "_"
									+ String.valueOf(count_Value1));
                            
							remoteusernode.setProperty("CreatedBy", logged_in_user);
							remoteusernode.setProperty("Subject", subject);
							remoteusernode.setProperty("Body", body);
							remoteusernode.setProperty("Type", type);
							remoteusernode.setProperty("Campaign_Id", campaignid);
							remoteusernode.setProperty("List_Id", list_id);
                            

						}

						// For Adding Campaign in SubscriberNode

						// url
						// http://localhost/restapi/campaign-list/listCampaignAdd.php?listid=2&campid=92
						
						String campaignlisturl = ip.getNode("ip").getProperty("Campaign_List_Url").getString();
						String campaignparameter = "?listid=" + list_id + "&campid=" + campaignid;
						String campaignlistresponse = this
								.sendpostdata(campaignlisturl, campaignparameter.replace(" ", "%20"), response)
								.replace("<pre>", "");
						// out.println("Campaign_List_Response : " + campaignlistresponse);
						String subscriberdataurl = ip.getNode("ip").getProperty("Subscriber_Data_Url").getString();
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
				//String campaignaddurl = "http://35.221.31.185/phplist_api/campaign_api.php?cmd=campaignUpdate";
				String campaignaddurl = "http://35.221.31.185/phplist_api/campaign_api_wurl.php?cmd=campaignUpdate";
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
				//out.println("updateCampaign");
				//String id="569";
				String id = request.getParameter("id");
				String embargo = request.getParameter("embargo");
				String campaignaddurl = "http://35.221.31.185/phplist_api/campaign_api_wurl.php?cmd=campaignUpdate";
				//http://35.221.31.185/phplist_api/campaign_api_wurl.php?cmd=campaignUpdate
				 String campaignaddapiurlparameters = "id="+id+"&embargo=" + embargo;
				 out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
				//String campaignaddapiurlparameters = "id="+id+"&embargo=" + embargo;
				//String campaignaddapiurlparameters = "id="+"679"+"&embargo=" + "2019-02-25 12:30:00 AM";
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
				//String campaignresponse = this.sendHttpPostData(campaignaddurl, campaignaddapiurlparameters, response);
				 
				 
				 
				String campaignlistresponse = this.sendHttpPostData(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20"), response)
						.replace("<pre>", "");
				
				out.println("campaignresponse : "+campaignlistresponse);
				
				/*
				String campaignaddurl1 = "http://35.221.31.185/restapi/campaign/campaignStatusUpdate.php";
				String campaignaddapiurlparameters1 = "id="+id+"&status=submitted";
				//draft sent suspended submitted
				String campaignlistresponse1 = this.sendHttpPostData(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20"), response)
						.replace("<pre>", "");
				
				out.println("campaignresponse1 : "+campaignlistresponse1);
				*/
				String status_response=this.campaignStatusUpdate(id,"submitted",response);
				out.println("status_response : "+status_response);
				
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
				String campaignaddurl = "http://35.221.31.185/phplist_api/campaign_api.php?cmd=campaignAdd";
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
				String campaignaddurl = "http://35.221.31.185/restapi/campaign/campaignStatusUpdate.php";
				String campaignaddapiurlparameters = "id=" + id + "&status=" + status;
				String campaignresponse = this.sendHttpPostData(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
						.replace("<pre>", "");
				out.println("campaignresponse : "+campaignresponse);
			}else if (request.getRequestPathInfo().getExtension().equals("placeCampaignInQueueforSending")) {
				//out.println("placeCampaignInQueueforSending2 step 1 ");
				String id = request.getParameter("id");
				String status = request.getParameter("status");
				//out.println("placeCampaignInQueueforSending2 step 1  id : "+id+"  status : "+status);
				String campaignaddurl = "http://35.221.31.185/restapi/campaign/campaignStatusUpdate.php";
				String campaignaddapiurlparameters = "?id=" + id + "&status=" + status;
				//out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
				String campaignresponse = this.sendpostdata(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
						.replace("<pre>", "");
				//out.println("placeCampaignInQueueforSending2 step 3 : ");
				out.println("campaignresponse : "+campaignresponse);
			}else if (request.getRequestPathInfo().getExtension().equals("processQueue")) {
				
				String campid =request.getParameter("campid");
				String campaignlisturl = "http://35.221.31.185/phplistpq/processqueue.php";
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
	public String campaignStatusUpdate(String id,String status,SlingHttpServletResponse response) throws ServletException,
	IOException {
		String campaignaddurl = "http://35.221.31.185/restapi/campaign/campaignStatusUpdate.php";
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
