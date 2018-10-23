package leadconverter.servlet;

import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import java.lang.reflect.Array;
import java.util.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.*;
import java.rmi.Remote;

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
import javax.servlet.ServletException;
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
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/LeadConverterJbpm" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class LeadConverterJbpm extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repos;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		if (request.getRequestPathInfo().getExtension().equals("DeleteNode")) {

			PrintWriter out = response.getWriter();
			out.println("GET");
			try {
				Session session = null;
				session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));
				// Node content = session.getRootNode().getNode("content");
				Node contentitr = session.getRootNode().getNode("content").getNode("product");
				contentitr.update("Product_New");
				out.println(contentitr);
				session.save();
			} catch (Exception ex) {

				out.println("Exception ex" + ex.getMessage());
			}
		}
		else if (request.getRequestPathInfo().getExtension().equals("JBPM_Fresh")) {
PrintWriter out=response.getWriter();
			try {
				Session session = null;
				session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));
				Node content = session.getRootNode().getNode("content");
				String subscriberid = request.getParameter("Subscriber_Id");
				String statusjbpm = request.getParameter("Status");
				out.println("Status JBPM : " + statusjbpm);
				Node subscribernamenode = null;
				Node campainginsubscribernode = null;
				String Created_By = request.getParameter("Created_By");
				String list_id_JBPM = request.getParameter("List_Id");// From JBPM
				NodeIterator subscriberemailitr = session.getRootNode().getNode("content").getNode("LEAD_CONVERTER")
						.getNode("SUBSCRIBER").getNodes();
				while(subscriberemailitr.hasNext()) {
					subscribernamenode = subscriberemailitr.nextNode();// email
					String subscribername = subscribernamenode.getName();
					out.println(subscribername);
					String statussling = subscribernamenode.getProperty("Status").getString();// Status
					NodeIterator campainginsubscriberitr = session.getRootNode().getNode("content")
							.getNode("LEAD_CONVERTER").getNode("SUBSCRIBER").getNode(subscribername).getNode("Campaign")
							.getNodes();
					while (campainginsubscriberitr.hasNext()) {
						campainginsubscribernode = campainginsubscriberitr.nextNode();
						String campaignname = campainginsubscribernode.getName();//// Explore0;
						out.println("campaignname : "+campaignname);
						NodeIterator mainnode = session.getRootNode().getNode("content").getNode("user")
								.getNode(Created_By).getNode("Lead_Converter").getNode("Email").getNode("Campaign")
								.getNodes();
						// out.println(mainnode);
						//campainginsubscribernode.remove();
						Node typenode = null;
						String typename = null;
						String Current_Campaign = null;
						Node currentcampaignnode = null;
						NodeIterator typenodeitr = null;
						String currentcampaingname = null;
						while (mainnode.hasNext()) {
							typenode = mainnode.nextNode();// Explore//Inform
							typename = typenode.getName();
							out.println("Typename : : " + typename);
							Current_Campaign = typenode.getProperty("Current_Campaign").getString().replace("@", "_");
							out.println("Current_Campaign : "+Current_Campaign);
							if (typename.equals("Explore")) {
								typenodeitr = session.getRootNode().getNode("content").getNode("user")
										.getNode(Created_By).getNode("Lead_Converter").getNode("Email")
										.getNode("Campaign").getNode("Explore").getNodes();
out.println("Explore");
							}
							if (typename.equals("Inform")) {
								typenodeitr = session.getRootNode().getNode("content").getNode("user")
										.getNode(Created_By).getNode("Lead_Converter").getNode("Email")
										.getNode("Campaign").getNode("Inform").getNodes();
								out.println("Inform ");

							}
							while (typenodeitr.hasNext()) {
								if (statussling.equals(statusjbpm)) {
             out.println("Status Equals");
									currentcampaignnode = typenodeitr.nextNode();
									currentcampaingname = currentcampaignnode.getName(); 
									out.println("currentcampaingname :"+currentcampaingname);// Explore_0
									
									if (Current_Campaign.equals(currentcampaingname)) {
										out.println("If Current Campaign");
										typenode.setProperty("Current_Campaign", currentcampaingname);
									} else {
										out.println("Else Current Campaign");
										typenode.setProperty("Current_Campaign", currentcampaingname);
										out.println("subscriber name : "+subscribername);
										Node currentcampaingchangenode= session.getRootNode().getNode("content").getNode("LEAD_CONVERTER").getNode("SUBSCRIBER").getNode(subscribername).getNode("Campaign");
												currentcampaingchangenode.addNode(currentcampaingname);
									}
								}
							}
						}
						session.save();
					}
				}
			} catch (Exception ex) {

				out.println("Exception ex: : " + ex.getMessage());
			}

		}

	}


	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		if (request.getRequestPathInfo().getExtension().equals("JBPM")) {

			try {
				Session session = null;
				session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));

				Node content = session.getRootNode().getNode("content");
				Node leadconverternode = null;
				Node subscribernode = null;
				Node subscriberidnode = null;
				Node campaignnode = null;
				Node usernode = null;
				String subscriberemailname = null;
				String subscriberidsling = null;
				String currentcampaignnextname = null;
				String subscriberid = request.getParameter("Subscriber_Id");
				String statusjbpm = request.getParameter("Status");
				out.println("Status JBPM : " + statusjbpm);

				String Created_By = request.getParameter("Created_By");
				String list_id_JBPM = request.getParameter("List_Id");// From JBPM
				NodeIterator subscriberemailitr = session.getRootNode().getNode("content").getNode("LEAD_CONVERTER")
						.getNode("SUBSCRIBER").getNodes();
				while (subscriberemailitr.hasNext()) {
					out.println("Iterator");
					Node subscriberemail = subscriberemailitr.nextNode();
					subscriberemailname = subscriberemail.getName();
					subscriberidsling = subscriberemail.getProperty("SUBSCRIBER_ID").getString();
					out.println("subscriberemailname : " + subscriberemailname);
					out.println("subscriberidsling : " + subscriberidsling);
					if (!content.hasNode("LEAD_CONVERTER")) {
						out.println("LC_if");
						leadconverternode = content.addNode("LEAD_CONVERTER");

					} else {
						out.println("LC_if");

						leadconverternode = content.getNode("LEAD_CONVERTER");
					}

					if (!leadconverternode.hasNode("SUBSCRIBER")) {
						// out.println("Sub_if");

						subscribernode = leadconverternode.addNode("SUBSCRIBER");

					} else {
						// out.println("Sub_else");

						subscribernode = leadconverternode.getNode("SUBSCRIBER");

					}
					out.println(subscribernode);

					if (subscribernode.hasNode(subscriberemailname)) {
						out.println("subnode_if");
						subscriberidnode = subscribernode.getNode(subscriberemailname);
						String statussling = subscriberidnode.getProperty("Status").getString();// Status
						out.println("statussling  :" + statussling);
						if (statussling.equals(statusjbpm)) {
							out.println("Equals Status in both");
							NodeIterator mainnode = session.getRootNode().getNode("content").getNode("user")
									.getNode(Created_By).getNode("Lead_Converter").getNode("Email").getNode("Campaign").getNodes();
							Node typenode = null;
							String typename = null;
							String Current_Campaign = null;
							while (mainnode.hasNext()) {
								typenode = mainnode.nextNode();// Explore//Inform
								typename = typenode.getName();
								out.println("Typename : : " + typename);
								Current_Campaign = typenode.getProperty("Current_Campaign").getString().replace("@","_");
								out.println("Current_Campaign : : " + Current_Campaign);// Explore_0;

								NodeIterator typenodeitr = session.getRootNode().getNode("content").getNode("user")
										.getNode(Created_By).getNode("Lead_Converter").getNode("Email")
										.getNode("Campaign").getNode("Explore").getNodes();
								while (typenodeitr.hasNext()) {

									Node campaignnextnode = typenodeitr.nextNode();// Explore//Inform//explore_0;
									out.println("campaignnextnode" + campaignnextnode);

									currentcampaignnextname = campaignnextnode.getName();
									out.println("currentcampaignnextname : : " + currentcampaignnextname);
									if (Current_Campaign.equals(currentcampaignnextname)) {
										out.println("If Current Campaign equals");
										typenode.setProperty("Current_Campaign", currentcampaignnextname);

									} else {
										out.println("Else Current Campaign");
										typenode.setProperty("Current_Campaign", currentcampaignnextname);
									}
									session.save();

								}
							}
						}
					}
					if (subscriberid.equals(subscriberidsling)) {

						out.println("Equals Subscriber_Ids");
						if (!subscribernode.hasNode(subscriberemailname.replace("@", "_"))) {

							subscriberidnode = subscribernode.addNode(subscriberemailname.replace("@", "_"));
							subscriberidnode.setProperty("Status", statusjbpm);

						} else {

							subscriberidnode = subscribernode.getNode(subscriberemailname.replace("@", "_"));
							subscriberidnode.setProperty("Status", statusjbpm);

						}
						if (!subscriberidnode.hasNode("Campaign")) {

							campaignnode = subscriberidnode.addNode("Campaign");

						} else {

							campaignnode = subscriberidnode.getNode("Campaign");
						}
						String slinglist = null;
						if (statusjbpm.equals("Cold_lead")) {

							Node colddata = content.getNode("user").getNode(Created_By).getNode("Lead_Converter")
									.getNode("Email").getNode("Campaign").getNode("Entice")
									.getNode(Created_By + "_" + "Entice_0");
							slinglist = colddata.getProperty("List_Id").getString();
						}
						if (statusjbpm.equals("Unknown")) {

							Node unknowndata = content.getNode("user").getNode(Created_By).getNode("Lead_Converter")
									.getNode("Email").getNode("Campaign").getNode("Explore")
									.getNode(Created_By + "_" + "Explore_0");
							slinglist = unknowndata.getProperty("List_Id").getString();
						}

						NodeIterator campaignnodeitr = campaignnode.getNodes();

						while (campaignnodeitr.hasNext()) {
							out.println(list_id_JBPM + slinglist);
							usernode = campaignnodeitr.nextNode();
							String url = content.getNode("ip").getProperty("Delete_SubscriberIn_List").getString();

							// String deletesubscriberinlistparameters = "?list_id=" + list_id_JBPM +
							// "&subscriber_id="
							// + subscriberidsling;
							// String apiresponse =
							// this.sendpostdata(url,deletesubscriberinlistparameters.replace(" ", "%20"),
							// response)
							// .replace("<pre>", "");
							// 1//91.101.165.251/restapi/list-subscriber/listSubscriberDelete.php?list_id=oldlist&subscriber_id=subscriberidsling
							usernode.remove();
							// out.println("API RESPONSE : :"+list_id_JBPM+slinglist+apiresponse);

						}
						if (!campaignnode.hasNode(currentcampaignnextname)) {

							usernode = campaignnode.addNode(currentcampaignnextname);
							usernode.setProperty("List_Id", slinglist);
							usernode.setProperty("Subscriber_Id", subscriberidsling);

							String subscriberaddurl = content.getNode("ip").getProperty("Integration_Url").getString();
							// String addsubscriberinlistparameters = "?list_id=" + slinglist +
							// "&subscriber_id="
							// + subscriberidsling;
							// String responsedata =
							// this.sendpostdata(subscriberaddurl,addsubscriberinlistparameters.replace(" ",
							// "%20"), response)
							// .replace("<pre>", "");

							// http://191.101.165.251/restapi/list-subscriber/listSubscriberAdd.php?list_id=2&subscriber_id=22

						}

					} else {
						out.println("Else");
					}
					session.save();
				}
			}

			catch (Exception e) {
				out.println("Exception : : :" + e.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("JBPM_Fresh_Get")) {

			try {
				Session session = null;
				session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));
				Node content = session.getRootNode().getNode("content");
				String subscriberid = request.getParameter("Subscriber_Id");
				String statusjbpm = request.getParameter("Status");
				out.println("Status JBPM : " + statusjbpm);
				Node subscribernamenode = null;
				Node campainginsubscribernode = null;
				String Created_By = request.getParameter("Created_By");
				String list_id_JBPM = request.getParameter("List_Id");// From JBPM
				NodeIterator subscriberemailitr = session.getRootNode().getNode("content").getNode("LEAD_CONVERTER")
						.getNode("SUBSCRIBER").getNodes();
				while (subscriberemailitr.hasNext()) {
					subscribernamenode = subscriberemailitr.nextNode();// email
					String subscribername = subscribernamenode.getName();
					out.println(subscribername);
					String statussling = subscribernamenode.getProperty("Status").getString();// Status

					NodeIterator campainginsubscriberitr = session.getRootNode().getNode("content")
							.getNode("LEAD_CONVERTER").getNode("SUBSCRIBER").getNode(subscribername).getNode("Campaign")
							.getNodes();

					while (campainginsubscriberitr.hasNext()) {

						campainginsubscribernode = campainginsubscriberitr.nextNode();
						String campaignname = campainginsubscribernode.getName();//// Explore0;
						out.println("campaignname : "+campaignname);
						NodeIterator mainnode = session.getRootNode().getNode("content").getNode("user")
								.getNode(Created_By).getNode("Lead_Converter").getNode("Email").getNode("Campaign")
								.getNodes();
						// out.println(mainnode);
						//campainginsubscribernode.remove();
						Node typenode = null;
						String typename = null;
						String Current_Campaign = null;
						Node currentcampaignnode = null;
						NodeIterator typenodeitr = null;
						String currentcampaingname = null;
						while (mainnode.hasNext()) {
							typenode = mainnode.nextNode();// Explore//Inform
							typename = typenode.getName();
							out.println("Typename : : " + typename);
							Current_Campaign = typenode.getProperty("Current_Campaign").getString().replace("@", "_");
							out.println("Current_Campaign : "+Current_Campaign);
							if (typename.equals("Explore")) {
								typenodeitr = session.getRootNode().getNode("content").getNode("user")
										.getNode(Created_By).getNode("Lead_Converter").getNode("Email")
										.getNode("Campaign").getNode("Explore").getNodes();
out.println("Explore ");
							}
							if (typename.equals("Inform")) {
								typenodeitr = session.getRootNode().getNode("content").getNode("user")
										.getNode(Created_By).getNode("Lead_Converter").getNode("Email")
										.getNode("Campaign").getNode("Inform").getNodes();
								out.println("Inform ");

							}
							while (typenodeitr.hasNext()) {
								if (statussling.equals(statusjbpm)) {
out.println("Status Equals");
									currentcampaignnode = typenodeitr.nextNode();
									currentcampaingname = currentcampaignnode.getName(); 
									out.println("currentcampaingname :"+currentcampaingname);// Explore_0
									
									if (Current_Campaign.equals(currentcampaingname)) {
										out.println("If Current Campaign");
										typenode.setProperty("Current_Campaign", currentcampaingname);
									} else {
										out.println("Else Current Campaign");
										typenode.setProperty("Current_Campaign", currentcampaingname);
										out.println("subscriber name : "+subscribername);
										Node currentcampaingchangenode= session.getRootNode().getNode("content").getNode("LEAD_CONVERTER").getNode("SUBSCRIBER").getNode(subscribername).getNode("Campaign");

												currentcampaingchangenode.addNode(currentcampaingname);
									}
								}
							}
						}
						session.save();

					}
				}

			} catch (Exception ex) {

				out.println("Exception ex: : " + ex.getMessage());
			}

		}

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
		OutputStream writer = conn.getOutputStream();
		writer.write(urlParameters.getBytes());
		int responseCode = conn.getResponseCode();
		StringBuffer buffer = new StringBuffer();
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
