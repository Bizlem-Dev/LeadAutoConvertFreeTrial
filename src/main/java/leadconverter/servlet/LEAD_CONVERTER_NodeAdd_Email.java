package leadconverter.servlet;
//Initial Commit 
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/LEAD_CONVERTER_NodeAdd_Email"}),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
public class LEAD_CONVERTER_NodeAdd_Email extends SlingAllMethodsServlet {
	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = new JSONObject();
	String urlParameters = mainjsonobject.toString();
	Date currentTime = new Date();
	

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		if (request.getRequestPathInfo().getExtension().equals("gettingdata")) {

			try {

				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				Node ip = session.getRootNode().getNode("content");
				String campaignname = null;
				String campaignnameinsubscriber = null;
				String subscribername = null;
				String subscriberid = null;
				Node gettypenamenode = null;
				Node getcampaignnamenode = null;
				NodeIterator typenodeitr = null;
				NodeIterator getcampaignnodeitr = null;
				Node getsubscribernamenode = null;
				NodeIterator getsubscribernamenodeitr = null;
				Node getcampaignnameinsubscribernode = null;
				NodeIterator getcampaignnameinsubscribernodeitr = null;
				String campaignid = null;
				Node gettypeinsubscribernode = null;
				NodeIterator gettypeinsubscribernodeitr = null;
				String typenameinsubscriber = null;
				String getcampaignname = null;
				typenodeitr = session.getRootNode().getNode("content").getNode("user")
						.getNode(request.getRemoteUser().replace("@", "_")).getNode("Lead_Converter").getNode("Email")
						.getNode("Campaign").getNodes();// type

				while (typenodeitr.hasNext()) {
					gettypenamenode = typenodeitr.nextNode();
					out.println("GetTypename " + gettypenamenode);
					String typename = gettypenamenode.getName();// typename
					getcampaignnodeitr = session.getRootNode().getNode("content").getNode("user")
							.getNode(request.getRemoteUser().replace("@", "_")).getNode("Lead_Converter")
							.getNode("Email").getNode("Campaign").getNode(typename).getNodes();// type
					while (getcampaignnodeitr.hasNext()) {

						Node name = getcampaignnodeitr.nextNode();
						out.println("Nodes " + name);
						campaignname = name.getName();
						out.println("Campaign Name" + campaignname);
						campaignid = name.getProperty("Campaign_Id").getString();
						out.println("Campaign_Id " + campaignid);

						getsubscribernamenodeitr = session.getRootNode().getNode("content").getNode("LEAD_CONVERTER")
								.getNode("SUBSCRIBER").getNodes();

						while (getsubscribernamenodeitr.hasNext()) {
							getsubscribernamenode = getsubscribernamenodeitr.nextNode();
							subscribername = getsubscribernamenode.getName();// subscribername
							out.println("subscribername " + subscribername);
							subscriberid = getsubscribernamenode.getProperty("SUBSCRIBER_ID").getString();
							out.println("subscribername " + subscriberid);

							gettypeinsubscribernodeitr = session.getRootNode().getNode("content")
									.getNode("LEAD_CONVERTER").getNode("SUBSCRIBER").getNode(subscribername)
									.getNode("Campaign").getNodes();// campaignname

							while (gettypeinsubscribernodeitr.hasNext()) {

								Node campaignnamenode = gettypeinsubscribernodeitr.nextNode();
								getcampaignname = campaignnamenode.getName();
								out.println("campaignnameinsubscriber : " + getcampaignname);

							}

							if (getcampaignname.equals(campaignname)) {
								out.println("If");

								//String subscriber_campaign_url = ip.getNode("ip").getProperty("Campaign_Subscriber")
										//.getString();
								String subscriber_campaign_url = ResourceBundle.getBundle("config").getString("Campaign_Subscriber");
								// http://191.101.165.251/webservice/campaigndatabyuid.php?campid=52&userid=1
								String campaignsubscriberapiurlparameters = "?campid=" + campaignid + "&userid="
										+ subscriberid;

								String campaignsubscriberresponse = this.sendpostdata(subscriber_campaign_url,
										campaignsubscriberapiurlparameters, response).replace("<pre>", "");
								out.println("Campaign_Response: : " + campaignsubscriberresponse);
								
							}
						}
					}
				}

			} catch (Exception ex) {

				out.println("Exception ex " + ex.getMessage());
			}

		}
	}

	@SuppressWarnings("unused")
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		try {

			Session session = null;
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

			String DateToStr = format.format(currentTime);
			
			Node ip = session.getRootNode().getNode("content");
			if (request.getRequestPathInfo().getExtension().equals("NodeAddNew")) {
				out.println("NodeAdd 1   ");
				//String value=request.getParameter("totalresult");
				StringBuilder builder = new StringBuilder();

			    BufferedReader bufferedReaderCampaign = request.getReader();
			    
			    String brokerageline;
			    while ((brokerageline = bufferedReaderCampaign.readLine()) != null) {
			     builder.append(brokerageline + "\n");
			    }
			    out.println("NodeAdd 2   ");
			    out.println("builder   "+builder);
			    String value=builder.toString();
				out.println("totalresult 1   " + value);

				JSONObject object = new JSONObject(value);

				String remot = object.getString("remote_user");

				String subscribers_string = object.getString("Subscribers");
				JSONObject subscribers_json = new JSONObject(subscribers_string);
				JSONObject jsondata = object.getJSONObject("List");
				JSONObject listdata = jsondata.getJSONObject("data");
				String list_id = listdata.getString("id");
				String list_name = listdata.getString("name");
				out.println("list_name: :" + list_name);
				JSONArray subscribers_data = subscribers_json.getJSONArray("data");

				String slingqery = "select [LIST_NAME,LIST_ID,NODE_ID] from [nt:base] where (contains('LIST_NAME','"
						+ list_name + "'))  and ISDESCENDANTNODE('/content/LEAD_CONVERTER/LIST/')";

				Workspace workspace = session.getWorkspace();

				Query query = workspace.getQueryManager().createQuery(slingqery, Query.JCR_SQL2);

				QueryResult queryResult = query.execute();
				NodeIterator iterator = queryResult.getNodes();

				Node list = null;
				Node sub_list = null;
				Node lead_converter = null;
				Node subscriber = null;
				Node email = null;
				Node subscriber_list = null;
				Node newnode = null;
				Node campaignnode = null;
				out.println("countvalue : : : :");

				long count_Value1 = 0;

				Node content = session.getRootNode().getNode("content");

				if (!content.hasNode("LEAD_CONVERTER")) {

					out.println("Lead Node True or False : : :" + !content.hasNode("LEAD_CONVERTER"));
					lead_converter = content.addNode("LEAD_CONVERTER");
					out.println("If LEAD_CONVERTER : : : " + lead_converter);
				} else {

					lead_converter = content.getNode("LEAD_CONVERTER");
				}

				if (!lead_converter.hasNode("LIST")) {

					out.println("List Node True or False : : :" + !lead_converter.hasNode("LIST"));
					list = lead_converter.addNode("LIST");
					list.setProperty("Counter", count_Value1);
					out.println("List id if   ...." + list.toString());
				} else {
					list = lead_converter.getNode("LIST");
					count_Value1 = list.getProperty("Counter").getLong();
					count_Value1 = count_Value1 + 1;
					out.println("count_Value1 after : : : " + count_Value1);
					list.setProperty("Counter", count_Value1);

				}

				if (iterator.hasNext()) {
					sub_list = iterator.nextNode();
					count_Value1 = Long.parseLong(sub_list.getProperty("NODE_ID").getString());
					// out.println(" iterator.next=="+ iterator.nextNode());
				}

				else {
					sub_list = list.addNode(String.valueOf(count_Value1));
					sub_list.setProperty("LIST_ID", list_id);
					sub_list.setProperty("LIST_NAME", list_name);
					sub_list.setProperty("CREATED_BY", remot);
					sub_list.setProperty("NODE_ID", count_Value1);
				}

				for (int i = 0; i < subscribers_data.length(); i++) {

					JSONObject data = subscribers_data.getJSONObject(i);

					String email_id = data.getString("id");
					String email_name = data.getString("email");

					out.println("Subscriber_id  : " + email_id);
					out.println("Subscriber_Name  : " + email_name);

					if (!lead_converter.hasNode("SUBSCRIBER")) {
						subscriber = lead_converter.addNode("SUBSCRIBER");
					} else {
						subscriber = lead_converter.getNode("SUBSCRIBER");
						out.println("subscriber Id else==  " + lead_converter);
					}
					if (!subscriber.hasNode(email_name.replace("@", "_"))) {
						email = subscriber.addNode(email_name.replace("@", "_"));
						email.setProperty("SUBSCRIBER_ID", email_id);
						email.setProperty("SUBSCRIBER_NAME", email_name.replace("@", "_"));
						email.setProperty("CURRENT DATE AND TIME", DateToStr);
						email.setProperty("List_Id",list_id);

						out.println("if EMAIL : :" + subscriber);

					} else {
						email = subscriber.getNode(email_name.replace("@", "_"));
						email.setProperty("SUBSCRIBER_ID", email_id);
						email.setProperty("SUBSCRIBER_NAME", email_name.replace("@", "_"));
						email.setProperty("CURRENT DATE AND TIME", DateToStr);
						email.setProperty("List_Id",list_id);

						out.println("else  EMAIL : :" + subscriber);
					}
					if (!email.hasNode("List")) {
						subscriber_list = email.addNode("List");
					} else {
						subscriber_list = email.getNode("List");

					}

					if (!subscriber_list.hasNode(String.valueOf(count_Value1))) {
						newnode = subscriber_list.addNode(String.valueOf(count_Value1));

					} else {
						newnode = subscriber_list.addNode(String.valueOf(count_Value1));

					}

				}
				session.save();

			}
			else if (request.getRequestPathInfo().getExtension().equals("NodeAdd")) {
				out.println("NodeAdd 1   ");
				//String value=request.getParameter("totalresult");
				StringBuilder builder = new StringBuilder();

			    BufferedReader bufferedReaderCampaign = request.getReader();
			    
			    String brokerageline;
			    while ((brokerageline = bufferedReaderCampaign.readLine()) != null) {
			     builder.append(brokerageline + "\n");
			    }
			    out.println("NodeAdd 2   ");
			    out.println("builder   "+builder);
			    String value=builder.toString();
				out.println("totalresult 1   " + value);

				JSONObject object = new JSONObject(value);

				String remot = object.getString("remote_user");

				String subscribers_string = object.getString("Subscribers");
				
				String SubscribersWithPropert_string = object.getString("SubscribersWithPropert");
				
				JSONObject subscribers_json = new JSONObject(subscribers_string);
				JSONObject jsondata = object.getJSONObject("List");
				JSONObject listdata = jsondata.getJSONObject("data");
				String list_id = listdata.getString("id");
				String list_name = listdata.getString("name");
				out.println("list_name: :" + list_name);
				JSONArray subscribers_data = subscribers_json.getJSONArray("data");
				
				JSONArray SubscribersWithPropert_data = new JSONArray(SubscribersWithPropert_string);
				
				String slingqery = "select [LIST_NAME,LIST_ID,NODE_ID] from [nt:base] where (contains('LIST_NAME','"
						+ list_name + "'))  and ISDESCENDANTNODE('/content/LEAD_CONVERTER/LIST/')";

				Workspace workspace = session.getWorkspace();

				Query query = workspace.getQueryManager().createQuery(slingqery, Query.JCR_SQL2);

				QueryResult queryResult = query.execute();
				NodeIterator iterator = queryResult.getNodes();

				Node list = null;
				Node sub_list = null;
				Node lead_converter = null;
				Node subscriber = null;
				Node email = null;
				Node subscriber_list = null;
				Node newnode = null;
				Node campaignnode = null;
				out.println("countvalue : : : :");

				long count_Value1 = 0;

				Node content = session.getRootNode().getNode("content");

				if (!content.hasNode("LEAD_CONVERTER")) {

					out.println("Lead Node True or False : : :" + !content.hasNode("LEAD_CONVERTER"));
					lead_converter = content.addNode("LEAD_CONVERTER");
					out.println("If LEAD_CONVERTER : : : " + lead_converter);
				} else {

					lead_converter = content.getNode("LEAD_CONVERTER");
				}

				if (!lead_converter.hasNode("LIST")) {

					out.println("List Node True or False : : :" + !lead_converter.hasNode("LIST"));
					list = lead_converter.addNode("LIST");
					list.setProperty("Counter", count_Value1);
					out.println("List id if   ...." + list.toString());
				} else {
					list = lead_converter.getNode("LIST");
					count_Value1 = list.getProperty("Counter").getLong();
					count_Value1 = count_Value1 + 1;
					out.println("count_Value1 after : : : " + count_Value1);
					list.setProperty("Counter", count_Value1);

				}

				if (iterator.hasNext()) {
					sub_list = iterator.nextNode();
					count_Value1 = Long.parseLong(sub_list.getProperty("NODE_ID").getString());
					// out.println(" iterator.next=="+ iterator.nextNode());
				}

				else {
					sub_list = list.addNode(String.valueOf(count_Value1));
					sub_list.setProperty("LIST_ID", list_id);
					sub_list.setProperty("LIST_NAME", list_name);
					sub_list.setProperty("CREATED_BY", remot);
					sub_list.setProperty("NODE_ID", count_Value1);
				}

				for (int i = 0; i < subscribers_data.length(); i++) {

					JSONObject data = subscribers_data.getJSONObject(i);
					JSONObject data_property = SubscribersWithPropert_data.getJSONObject(i);
					

					String email_id = data.getString("id");
					String email_name = data.getString("email");
					
					// EmailAddress FirstName LastName PhoneNumber Address CompanyName CompanyHeadCount Industry Institute Source
                    String EmailAddress=data_property.getString("EmailAddress");
                    String FirstName=data_property.getString("FirstName");
                    String LastName=data_property.getString("LastName");
                    String PhoneNumber=data_property.getString("PhoneNumber");
                    String Address=data_property.getString("Address");
                    String CompanyName=data_property.getString("CompanyName");
                    String CompanyHeadCount=data_property.getString("CompanyHeadCount");
                    String Industry=data_property.getString("Industry");
                    String Institute=data_property.getString("Institute");
                    String Source=data_property.getString("Source");
					out.println("Subscriber_id  : " + email_id);
					out.println("Subscriber_Name  : " + email_name);

					if (!lead_converter.hasNode("SUBSCRIBER")) {
						subscriber = lead_converter.addNode("SUBSCRIBER");
					} else {
						subscriber = lead_converter.getNode("SUBSCRIBER");
						out.println("subscriber Id else==  " + lead_converter);
					}
					if (!subscriber.hasNode(email_name.replace("@", "_"))) {
						email = subscriber.addNode(email_name.replace("@", "_"));
						email.setProperty("SUBSCRIBER_ID", email_id);
						email.setProperty("SUBSCRIBER_NAME", email_name.replace("@", "_"));
						email.setProperty("CURRENT DATE AND TIME", DateToStr);
						email.setProperty("List_Id",list_id);
						// EmailAddress FirstName LastName PhoneNumber Address CompanyName CompanyHeadCount Industry Institute Source
						email.setProperty("EmailAddress",EmailAddress);
						email.setProperty("FirstName",FirstName);
						email.setProperty("LastName",LastName);
						email.setProperty("PhoneNumber",PhoneNumber);
						email.setProperty("Address",Address);
						email.setProperty("CompanyName",CompanyName);
						email.setProperty("CompanyHeadCount",CompanyHeadCount);
						email.setProperty("Industry",Industry);
						email.setProperty("Institute",Institute);
						email.setProperty("Source",Source);
						out.println("if EMAIL : :" + subscriber);

					} else {
						email = subscriber.getNode(email_name.replace("@", "_"));
						email.setProperty("SUBSCRIBER_ID", email_id);
						email.setProperty("SUBSCRIBER_NAME", email_name.replace("@", "_"));
						email.setProperty("CURRENT DATE AND TIME", DateToStr);
						email.setProperty("List_Id",list_id);
						
						email.setProperty("EmailAddress",EmailAddress);
						email.setProperty("FirstName",FirstName);
						email.setProperty("LastName",LastName);
						email.setProperty("PhoneNumber",PhoneNumber);
						email.setProperty("Address",Address);
						email.setProperty("CompanyName",CompanyName);
						email.setProperty("CompanyHeadCount",CompanyHeadCount);
						email.setProperty("Industry",Industry);
						email.setProperty("Institute",Institute);
						email.setProperty("Source",Source);
						out.println("else  EMAIL : :" + subscriber);
					}
					if (!email.hasNode("List")) {
						subscriber_list = email.addNode("List");
					} else {
						subscriber_list = email.getNode("List");

					}

					if (!subscriber_list.hasNode(String.valueOf(count_Value1))) {
						newnode = subscriber_list.addNode(String.valueOf(count_Value1));

					} else {
						newnode = subscriber_list.addNode(String.valueOf(count_Value1));

					}

				}
				session.save();

			}

			else if (request.getRequestPathInfo().getExtension().equals("CampaignNodeAdd")) {
				// http://191.101.165.251/restapi/list-subscriber/listSubscribers.php?list_id=435
				//Node Content = session.getRootNode().getNode("content").getNode("user").getNode(request.getRemoteUser().replace("@", "_")).getNode("Lead_Converter").getNode("Email").getNode("Campaign").getNode(type).getNode(nodes);
				//String bodydata = Content.getProperty("Body").getString();
				out.println("inside servlet");

			try {
				String unsubscriber_link="<p><small><a href='[UNSUBSCRIBEURL]'>unsubscribe me</a></small></p>";
				String list_id = request.getParameter("list_id");

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
				String body = request.getParameter("ckcontent");
				       body=body+unsubscriber_link;
				String footer = request.getParameter("footer");
				       footer=footer+unsubscriber_link;
				String subject = request.getParameter("subject");
				String type = request.getParameter("type");
				String fromfield = request.getRemoteUser();
				String replyto = request.getRemoteUser();
				String embargo = request.getParameter("date");
				String campaignvalue = request.getParameter("campaignvalue");
				
//				String noofdays=request.getParameter("user");
//				String currentdate=request.getParameter("year");
//				
			//	out.println("cu0rrent_date : "+currentdate+"noofdays : "+noofdays );
                String bodyvalue=URLEncoder.encode(body);
				//String campaignaddurl = ip.getNode("ip").getProperty("Campaign_Add_Url").getString();
				String campaignaddurl = ResourceBundle.getBundle("config").getString("Campaign_Add_Url");
				String campaignaddapiurlparameters = "?subject=" + subject + "&fromfield=" + fromfield + "&replyto="
						+ replyto
						+ "&message="+bodyvalue+"&textmessage=hii&footer="+footer+"&status=draft&sendformat=html&template=&embargo="
						+ embargo+"&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
			//	out.println(campaignaddapiurlparameters);
				String campaignresponse = this.sendpostdata(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
						.replace("<pre>", "");
				JSONObject campaignjson = new JSONObject(campaignresponse);
				String campaignstatus = campaignjson.getString("status");
				
				JSONObject data = campaignjson.getJSONObject("data");
				String campaignid = data.getString("id");
				if (campaignstatus.equals(("success"))) {

					if (!content.hasNode(request.getRemoteUser().replace("@", "_"))) {

						// For Adding Campaign in UserNode

						usernode = content.addNode(request.getRemoteUser().replace("@", "_"));
						//
						// out.println(usernode);
					} else {

						usernode = content.getNode(request.getRemoteUser().replace("@", "_"));
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
					/* this is commented by Akhil 
					if (!emailnode.hasNode("Campaign")) {

						campaignnode = emailnode.addNode("Campaign");

					} else {

						campaignnode = emailnode.getNode("Campaign");

					}
					if (!campaignnode.hasNode(type)) {

						typenode = campaignnode.addNode(type);
						typenode.setProperty("Counter", count_Value1);
					//	typenode.setProperty("Current_Date", currentdate);
						typenode.setProperty("Current_Campaign", campaignvalue);
					//	typenode.setProperty("No_of_days", noofdays);
				
					} else {

						typenode = campaignnode.getNode(type);
						count_Value1 = typenode.getProperty("Counter").getLong();
						count_Value1 = count_Value1 + 1;
						typenode.setProperty("Counter", count_Value1);
					//	typenode.setProperty("Current_Date", currentdate);
						typenode.setProperty("Current_Campaign", campaignvalue);
					//	typenode.setProperty("No_of_days", noofdays);

					}
					*/
					if (!emailnode.hasNode("Funnel")) {

						funnelNode = emailnode.addNode("Funnel");
						funnelNode.setProperty("Counter", funnel_count);

					} else {

						funnelNode = emailnode.getNode("Funnel");
						funnel_count=funnelNode.getProperty("Counter").getLong();

					}
					if (!funnelNode.hasNode(Long.toString(funnel_count))) {

						subFunnelNode = funnelNode.addNode(Long.toString(funnel_count));
						subFunnelNode.setProperty("Counter", funnel_count);

					} else {

						subFunnelNode = funnelNode.getNode(Long.toString(funnel_count));
						

					}
					if (!subFunnelNode.hasNode(type)) {

						typenode = subFunnelNode.addNode(type);
						typenode.setProperty("Counter", count_Value1);
					//	typenode.setProperty("Current_Date", currentdate);
						typenode.setProperty("Current_Campaign", campaignvalue);
					//	typenode.setProperty("No_of_days", noofdays);
				
					} else {

						typenode = subFunnelNode.getNode(type);
						count_Value1 = typenode.getProperty("Counter").getLong();
						
						
					//	typenode.setProperty("Current_Date", currentdate);
						typenode.setProperty("Current_Campaign", campaignvalue);
					//	typenode.setProperty("No_of_days", noofdays);

					}
					count_Value1 = count_Value1 + 1;
					typenode.setProperty("Counter", count_Value1);
					addcampaignnode = request.getRemoteUser().replace("@", "_") + "_" + type + "_"
							+ String.valueOf(count_Value1);
					if (!typenode.hasNode(request.getRemoteUser().replace("@", "_") + "_" + type + "_"
							+ String.valueOf(count_Value1))) {

						remoteusernode = typenode.addNode(request.getRemoteUser().replace("@", "_") + "_" + type + "_"
								+ String.valueOf(count_Value1));

						remoteusernode.setProperty("CreatedBy", request.getRemoteUser().replace("@", "_"));
						remoteusernode.setProperty("Subject", subject);
						remoteusernode.setProperty("Body", body);
						remoteusernode.setProperty("Type", type);
						remoteusernode.setProperty("Campaign_Id", campaignid);
						remoteusernode.setProperty("List_Id", list_id);

					} else {

						remoteusernode = typenode.getNode(request.getRemoteUser().replace("@", "_") + "_" + type + "_"
								+ String.valueOf(count_Value1));

						remoteusernode.setProperty("CreatedBy", request.getRemoteUser().replace("@", "_"));
						remoteusernode.setProperty("Subject", subject);
						remoteusernode.setProperty("Body", body);
						remoteusernode.setProperty("Type", type);
						remoteusernode.setProperty("Campaign_Id", campaignid);
						remoteusernode.setProperty("List_Id", list_id);


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
									request.getRemoteUser().replace("@", "_"));
							addcampaigninsubscribernode.setProperty("Subject", subject);
							addcampaigninsubscribernode.setProperty("Body", body);
							addcampaigninsubscribernode.setProperty("Type", type);
							addcampaigninsubscribernode.setProperty("List_Id", list_id);
						}

						else {

							addcampaigninsubscribernode = campaignnode.getNode(addcampaignnode);

							addcampaigninsubscribernode.setProperty("CreatedBy",
									request.getRemoteUser().replace("@", "_"));
							addcampaigninsubscribernode.setProperty("Subject", subject);
							addcampaigninsubscribernode.setProperty("Body", body);
							addcampaigninsubscribernode.setProperty("Type", type);
							addcampaigninsubscribernode.setProperty("List_Id", list_id);

						}

					}
					session.save();
					out.println("uploaded");

				}
			}
				catch(Exception ex) {
					out.println("Message : "+ex.getMessage());
				}
			}
			
			else if (request.getRequestPathInfo().getExtension().equals("campaignuseradd")) {

				Node campaignnode = null;
				Node content = session.getRootNode().getNode("content").getNode("user");
				
				String type = request.getParameter("type");
				String user=request.getParameter("user");
				String year=request.getParameter("year");
				String remoteuser=request.getParameter("remoteuser");
				String distance=request.getParameter("distance");

				 content = session.getRootNode().getNode("content").getNode("user");
				 Node usernode=null;
				 Node leadconverternode=null;
				 Node emailnode=null;
				  campaignnode=null;
				  Node typenode=null;
				  Node remoteusernode=null;
				  long count_Value1=0;
				 if (!content.hasNode(request.getRemoteUser().replace("@", "_"))) {

						// For Adding Campaign in UserNode

						usernode = content.addNode(request.getRemoteUser().replace("@", "_"));
						//
						// out.println(usernode);
					} else {

						usernode = content.getNode(request.getRemoteUser().replace("@", "_"));
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
					//
					
					if (!emailnode.hasNode("Campaign")) {

						campaignnode = emailnode.addNode("Campaign");

					} else {

						campaignnode = emailnode.getNode("Campaign");

					}
					
					if (!campaignnode.hasNode(type)) {

						typenode = campaignnode.addNode(type);
						typenode.setProperty("Distance", distance);
						
						typenode.setProperty("Counter",count_Value1 );
						//typenode.setProperty("Current_Date", currentdate);
					//	typenode.setProperty("Current_Campaign", campaignvalue);
						//typenode.setProperty("No_of_days", noofdays);
				
					} else {

						typenode = campaignnode.getNode(type);
						typenode.setProperty("Distance", distance);
						count_Value1 = typenode.getProperty("Counter").getLong();
					count_Value1 = count_Value1 + 1;
						typenode.setProperty("Counter", count_Value1);
					//	typenode.setProperty("Current_Date", currentdate);
						//typenode.setProperty("Current_Campaign", campaignvalue);
					//	typenode.setProperty("No_of_days", noofdays);

					}
					if (!typenode.hasNode(user)) {

						remoteusernode = typenode.addNode(user.replace("@", "_"));

						remoteusernode.setProperty("CreatedBy", remoteuser);
						//remoteusernode.setProperty("Subject", subject);
						//remoteusernode.setProperty("Body", body);
						remoteusernode.setProperty("Type", type);
						remoteusernode.setProperty("Campaign_Date", year);
						//remoteusernode.setProperty("Campaign_Id", campaignid);
						//remoteusernode.setProperty("List_Id", list_id);

					} else {

						remoteusernode = typenode.getNode(user.replace("@", "_"));

						remoteusernode.setProperty("CreatedBy", remoteuser);
						//remoteusernode.setProperty("Subject", subject);
						//remoteusernode.setProperty("Body", body);
						remoteusernode.setProperty("Type", type);

						remoteusernode.setProperty("Campaign_Date", year);


						//remoteusernode.setProperty("Campaign_Id", campaignid);
						//remoteusernode.setProperty("List_Id", list_id);

					}		
					session.save();
			}
			else if (request.getRequestPathInfo().getExtension().equals("userdata")) {

				String remoteuser = request.getParameter("remoteuser");
				JSONObject obj = null;
				JSONObject mainobj = new JSONObject();

				NodeIterator itr = session.getRootNode().getNode("content").getNode("user")
						.getNode(request.getRemoteUser().replace("@", "_")).getNode("Lead_Converter").getNode("Email")
						.getNode("Campaign").getNodes();
				JSONArray arr = new JSONArray();
				Node subjectdata = null;
				while (itr.hasNext()) {
					subjectdata = itr.nextNode();
					// subarr.put(subjectdata);
					if (subjectdata.hasNodes()) {
						NodeIterator itrType = subjectdata.getNodes();
						Node typedata = null;
						while (itrType.hasNext()) {
							obj = new JSONObject();
							typedata = itrType.nextNode();
							obj.put("Subject", typedata.getProperty("Subject").getString());
							obj.put("type", typedata.getProperty("Type").getString());
							obj.put("Nodes", typedata.getName());
							arr.put(obj);
						}
					}
				}
				mainobj.put("data", arr);
				mainobj.put("remoteuser", remoteuser);
				out.println(mainobj);
			}
					
			else if (request.getRequestPathInfo().getExtension().equals("displaydata")) {

				String type = request.getParameter("type");
				String remoteuser = request.getParameter("remoteuser");
				String nodes = request.getParameter("node");
				Node Content = session.getRootNode().getNode("content").getNode("user").getNode(remoteuser.replace("@", "_")).getNode("Lead_Converter").getNode("Email").getNode("Campaign").getNode(type).getNode(nodes);
				String bodydata = Content.getProperty("Body").getString();
				out.println(bodydata);
			}

			else if (request.getRequestPathInfo().getExtension().equals("SmsNodeAdd")) {

				long count_Value1 = 0;

				Node content = session.getRootNode().getNode("content").getNode("user");
				// out.println("Usernode : "+content);
				Node campaignnode = null;
				Node usernode = null;
				Node typenode = null;
				Node remoteusernode = null;
				Node leadconverternode = null;
				Node smsnode = null;
				String body = request.getParameter("ckcontent");
				String remoteuser = request.getParameter("remoteuser");
				String type = request.getParameter("type");
				if (!content.hasNode(request.getRemoteUser().replace("@", "_"))) {

					usernode = content.addNode(request.getRemoteUser().replace("@", "_"));

				} else {

					usernode = content.getNode(request.getRemoteUser().replace("@", "_"));
				}

				if (!usernode.hasNode("Lead_Converter")) {

					leadconverternode = usernode.addNode("Lead_Converter");

				} else {

					leadconverternode = usernode.getNode("Lead_Converter");

				}

				if (!leadconverternode.hasNode("Sms")) {

					smsnode = leadconverternode.addNode("Sms");

				} else {

					smsnode = leadconverternode.getNode("Sms");

				}
				if (!smsnode.hasNode("Campaign")) {

					campaignnode = smsnode.addNode("Campaign");

				} else {

					campaignnode = smsnode.getNode("Campaign");

				}
				if (!campaignnode.hasNode(type)) {

					typenode = campaignnode.addNode(type);
					//typenode.setProperty("Counter", count_Value1);

				} else {

					typenode = campaignnode.getNode(type);
					count_Value1 = typenode.getProperty("Counter").getLong();
					count_Value1 = count_Value1 + 1;
					typenode.setProperty("Counter", count_Value1);

				}
				if (!typenode.hasNode(
						request.getRemoteUser().replace("@", "_") + "_" + type + "_" + String.valueOf(count_Value1))) {

					remoteusernode = typenode.addNode(request.getRemoteUser().replace("@", "_") + "_" + type + "_"
							+ String.valueOf(count_Value1));
					remoteusernode.setProperty("CreatedBy", request.getRemoteUser());
					remoteusernode.setProperty("Body", body);
					remoteusernode.setProperty("Type", type);

				} else {

					remoteusernode = typenode.getNode(request.getRemoteUser().replace("@", "_") + "_" + type + "_"
							+ String.valueOf(count_Value1));

					remoteusernode.setProperty("CreatedBy", request.getRemoteUser());
					remoteusernode.setProperty("Body", body);
					remoteusernode.setProperty("Type", type);

				}
				// out.println("Remote _User : : "+request.getRemoteUser());
				session.save();
				
			}

			else if (request.getRequestPathInfo().getExtension().equals("smsuserdata")) {

				String remoteuser = request.getParameter("remoteuser");
				JSONObject obj = null;
				JSONObject mainobj = new JSONObject();

				NodeIterator itr = session.getRootNode().getNode("content").getNode("user")
						.getNode(request.getRemoteUser().replace("@", "_")).getNode("Lead_Converter").getNode("Sms")
						.getNode("Campaign").getNodes();

				// int j=0;
				JSONArray arr = new JSONArray();
				Node smsdata = null;
				while (itr.hasNext()) {
					smsdata = itr.nextNode();
					// subarr.put(subjectdata);
					if (smsdata.hasNodes()) {
						NodeIterator itrType = smsdata.getNodes();
						Node typedata = null;
						while (itrType.hasNext()) {
							obj = new JSONObject();
							typedata = itrType.nextNode();
							obj.put("type", typedata.getProperty("Type").getString());
							obj.put("body", typedata.getProperty("Body").getString());

							obj.put("Nodes", typedata.getName());
							arr.put(obj);
						}
					}
				}
				mainobj.put("data", arr);
				mainobj.put("remoteuser", remoteuser);
				out.println(mainobj);
			}

			else if (request.getRequestPathInfo().getExtension().equals("displaysmsdata")) {

				String type = request.getParameter("type");
				// out.println(type);
				String remoteuser = request.getParameter("remoteuser");
				String nodes = request.getParameter("node");

				Node Content = session.getRootNode().getNode("content").getNode("user")
						.getNode(remoteuser.replace("@", "_")).getNode("Lead_Converter").getNode("Sms")
						.getNode("Campaign").getNode(type).getNode(nodes);

				String bodydata = Content.getProperty("Body").getString();
				
				out.println(bodydata);
			} else if (request.getRequestPathInfo().getExtension().equals("StandardTemplate")) {

				long count_Value1 = 0;
				Node content = session.getRootNode().getNode("content");
				// out.println("Usernode : "+content);

				Node campaignnode = null;
				Node usernode = null;
				Node typenode = null;
				Node remoteusernode = null;
				Node leadconverternode = null;
				Node standardtemplatenode = null;
				Node adminnode = null;
				Node emailnode = null;
				String body = request.getParameter("ckcontent");
				String subject = request.getParameter("subject");
				String type = request.getParameter("type");
				if (!content.hasNode("LEAD_CONVERTER")) {

					leadconverternode = content.addNode("LEAD_CONVERTER");
					//
					// out.println(usernode);
				} else {

					leadconverternode = content.getNode("LEAD_CONVERTER");
				}

				if (!leadconverternode.hasNode("Email")) {

					emailnode = leadconverternode.addNode("Email");

				} else {

					emailnode = leadconverternode.getNode("Email");

				}

				if (!emailnode.hasNode("Standard_Template")) {

					standardtemplatenode = emailnode.addNode("Standard_Template");
					standardtemplatenode.setProperty("Counter", count_Value1);
				} else {

					standardtemplatenode = emailnode.getNode("Standard_Template");
					count_Value1 = standardtemplatenode.getProperty("Counter").getLong();
					count_Value1 = count_Value1 + 1;
					standardtemplatenode.setProperty("Counter", count_Value1);
				}

				if (!standardtemplatenode.hasNode("Admin_" + String.valueOf(count_Value1))) {

					adminnode = standardtemplatenode.addNode("Admin_" + String.valueOf(count_Value1));

					adminnode.setProperty("Subject", subject);
					adminnode.setProperty("Body", body);
					adminnode.setProperty("Type", type);

				} else {

					adminnode = standardtemplatenode.getNode("Admin_" + String.valueOf(count_Value1));

					adminnode.setProperty("Subject", subject);
					adminnode.setProperty("Body", body);
					adminnode.setProperty("Type", type);

				}
				session.save();

			}

			else if (request.getRequestPathInfo().getExtension().equals("subjectdata")) {

				JSONObject jsonObject2 = null;
				JSONObject listvalue = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				Node tempRulNode = null;
				NodeIterator iterator = session.getRootNode().getNode("content").getNode("LEAD_CONVERTER")
						.getNode("Email").getNode("Standard_Template").getNodes();
				while (iterator.hasNext()) {
					tempRulNode = iterator.nextNode();
					jsonObject2 = new JSONObject();
					String subject = tempRulNode.getProperty("Subject").getString();
					String type = tempRulNode.getProperty("Type").getString();

					jsonObject2.put("Subject_Name", subject);
					jsonObject2.put("Type", type);

					jsonObject2.put("Nodes", tempRulNode.getName());
					jsonArray.put(jsonObject2);
				}
				listvalue.put("data", jsonArray);
				out.println(listvalue);
			}

			else if (request.getRequestPathInfo().getExtension().equals("displaysubjectdata")) {

				String nodes = request.getParameter("node");

				Node Content = session.getRootNode().getNode("content").getNode("LEAD_CONVERTER").getNode("Email")
						.getNode("Standard_Template").getNode(nodes);
				String bodydata = Content.getProperty("Body").getString();

				out.println(bodydata);

			}

			else if (request.getRequestPathInfo().getExtension().equals("gettingcampaigndetails")) {

				String campaignname = null;
				String campaignnameinsubscriber = null;
				String subscribername = null;
				String subscriberid = null;
				Node gettypenamenode = null;
				Node getcampaignnamenode = null;
				NodeIterator typenodeitr = null;
				NodeIterator getcampaignnodeitr = null;
				Node getsubscribernamenode = null;
				NodeIterator getsubscribernamenodeitr = null;
				Node getcampaignnameinsubscribernode = null;
				NodeIterator getcampaignnameinsubscribernodeitr = null;
				String campaignid = null;
				typenodeitr = session.getRootNode().getNode("content").getNode("user").getNode(request.getRemoteUser())
						.getNode("Lead_Converter").getNode("Campaign").getNodes();// type

				while (typenodeitr.hasNext()) {
					gettypenamenode = typenodeitr.nextNode();
					String typename = gettypenamenode.getName();// typename
					getcampaignnodeitr = session.getRootNode().getNode("content").getNode("user")
							.getNode(request.getRemoteUser()).getNode("Lead_Converter").getNode("Campaign")
							.getNode(typename).getNodes();// type

					while (getcampaignnodeitr.hasNext()) {
						Node name = getcampaignnodeitr.nextNode();
						campaignname = name.getName();
						campaignid = name.getProperty("Campaign_Id").getString();

					}
					getsubscribernamenodeitr = session.getRootNode().getNode("content").getNode("LEAD_CONVERTER")
							.getNode("SUBSCRIBER").getNodes();

					while (getsubscribernamenodeitr.hasNext()) {
						getsubscribernamenode = getsubscribernamenodeitr.nextNode();
						subscribername = getsubscribernamenode.getName();// subscribername
						subscriberid = getsubscribernamenode.getProperty("SUBSCRIBER_ID").getString();

						getcampaignnameinsubscribernodeitr = session.getRootNode().getNode("content")
								.getNode("LEAD_CONVERTER").getNode("SUBSCRIBER").getNode(subscribername)
								.getNode("Campaign").getNodes();// campaignname
						while (getcampaignnameinsubscribernodeitr.hasNext()) {

							getcampaignnameinsubscribernode = getcampaignnameinsubscribernodeitr.nextNode();
							campaignnameinsubscriber = getcampaignnameinsubscribernode.getName();

						}

						if (campaignnameinsubscriber.equals(campaignname)) {

							//String subscriber_campaign_url = ip.getNode("ip").getProperty("Campaign_Subscriber")
							//		.getString();
							String subscriber_campaign_url = ResourceBundle.getBundle("config").getString("Campaign_Subscriber");
							// http://191.101.165.251/webservice/campaigndatabyuid.php?campid=52&userid=1
							String campaignsubscriberapiurlparameters = "?campid=" + campaignid + "&userid="
									+ subscriberid;

							String campaignsubscriberresponse = this
									.sendpostdata(subscriber_campaign_url, campaignsubscriberapiurlparameters, response)
									.replace("<pre>", "");
							out.println("Campaign_Response: :" + campaignsubscriberresponse);
						}
					}
				}

			}

		} catch (Exception e) {
			out.println("Exception : : :" + e.getMessage());
		}
	}

	private void BufferedInputStream(ServletInputStream sisr) {
		// TODO Auto-generated method stub
		
	}

	public String sendpostdata(String callurl, String urlParameters, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		URL url = new URL(callurl + urlParameters);
		out.println("Url :" + url);
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
		if (responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
		} else {
			out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();
	}
}

