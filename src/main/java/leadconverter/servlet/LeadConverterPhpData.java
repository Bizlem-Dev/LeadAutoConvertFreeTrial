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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/PhpData" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class LeadConverterPhpData extends SlingAllMethodsServlet {

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
		if (request.getRequestPathInfo().getExtension().equals("campaignForRuleEngine")) {
			try {
				Session session = null;
				session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));
				JSONObject mainjson = new JSONObject();
				JSONArray mainjsonArray = new JSONArray();
				JSONObject datajson = null;
				String campaignCatogory = request.getParameter("camp_catogery");
				String catogoryFilter = request.getParameter("catogery_filter");
				/*
				NodeIterator contentitr = session.getRootNode().getNode("content").getNode("user")
						.getNode("<%=request.getRemoteUser()%>").getNodes();
				while (contentitr.hasNext()) {
					Node nodename = contentitr.nextNode();
					nodename.remove();
				}
				*/
				NodeIterator useritr = session.getRootNode().getNode("content").getNode("user").getNodes();
				while (useritr.hasNext()) {
					datajson = new JSONObject();
					Node usernode = useritr.nextNode();
					//out.println("usernode Name : "+usernode.getName());
					if(!usernode.getName().equals("<%=request.getRemoteUser()%>")){
					if (usernode.hasNode("Lead_Converter")) {
						NodeIterator funnelitr = usernode.getNode("Lead_Converter").getNode("Email")
								.getNode("Funnel").getNodes();
						//out.println("funnelitr getSize : "+funnelitr.getSize());
						//String currentcampaign = currentcampaignname.getProperty("Current_Campaign").getString();
						//NodeIterator leadconverteritr = usernode.getNode("Lead_Converter").getNode("Email")
						//		.getNode("Campaign").getNode("Explore").getNodes();
						
						while (funnelitr.hasNext()) {
							Node funnelNode = funnelitr.nextNode();
							String funnelName = funnelNode.getName();// currentcampaign
							//out.println("funnelName : "+funnelName);
							//if(campaignCatogory=="")
							if(catogoryFilter.equals("single")){
								if(funnelNode.hasNode(campaignCatogory)){
									Node exploreNode=funnelNode.getNode(campaignCatogory);
									if(exploreNode.hasNodes()){
										   NodeIterator campaignsitr = exploreNode.getNodes();
										   while (campaignsitr.hasNext()) {
											   datajson = new JSONObject();
											   Node campaignnode = campaignsitr.nextNode();
											   String campaignName = campaignnode.getName();
											   if(!campaignName.equals("List")){
											   //out.println("campaignName : "+campaignName);
											    String campaignid = campaignnode.getProperty("Campaign_Id").getString();
												String createdby = campaignnode.getProperty("CreatedBy").getString();
												String list_id = campaignnode.getProperty("List_Id").getString();
												//out.println("campaignid : " + campaignid +"/n"+"createdby :" + createdby);
												datajson.put("Campaign_Id", campaignid);
												datajson.put("Created_By", createdby);
												datajson.put("List_Id", list_id);
												datajson.put("Campaign_Category", exploreNode.getName());
												mainjsonArray.put(datajson);
												mainjson.put("Data", mainjsonArray);
												//out.println(mainjson);
											   }
										   }
									 }
							     }
							 }else if(catogoryFilter.equals("all")){
								 if(funnelNode.hasNodes()){
									   NodeIterator campaignNodesitr = funnelNode.getNodes();
									   //out.println("funnelNode Name : "+funnelNode.getName());
									   
									   while (campaignNodesitr.hasNext()) {
										   Node campaignNodeName = campaignNodesitr.nextNode();
										   //out.println("Step 1");
										   //out.println("campaignNodeName : "+campaignNodeName.getName());
										   String campaignHeadingName = campaignNodeName.getName();
										   //out.println("campaignHeadingName : "+campaignHeadingName);
										   if(campaignNodeName.hasNodes()){
											   //out.println("campaignNodeName : "+campaignNodeName.getName());
											   NodeIterator campaignsitr = campaignNodeName.getNodes();
											   while (campaignsitr.hasNext()) {
												   datajson = new JSONObject();
												   Node campaignnode = campaignsitr.nextNode();
												   String campaignName = campaignnode.getName();
												    //out.println("campaignName : "+campaignName);
												    if(!campaignName.equals("List")){
												        String campaignid = campaignnode.getProperty("Campaign_Id").getString();
														String createdby = campaignnode.getProperty("CreatedBy").getString();
														String list_id = campaignnode.getProperty("List_Id").getString();
														//out.println("campaignid : " + campaignid +"/n"+"createdby :" + createdby);
														datajson.put("Campaign_Id", campaignid);
														datajson.put("Created_By", createdby);
														datajson.put("List_Id", list_id);
														datajson.put("Campaign_Category", campaignNodeName.getName());
														mainjsonArray.put(datajson);
														mainjson.put("Data", mainjsonArray);
														//out.println(mainjson);
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
		}else if (request.getRequestPathInfo().getExtension().equals("phpdataexplore")) {
			
			try {
				Session session = null;
				session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));
				JSONObject mainjson = new JSONObject();
				JSONArray mainjsonArray = new JSONArray();
				JSONObject datajson = null;
				/*
				NodeIterator contentitr = session.getRootNode().getNode("content").getNode("user")
						.getNode("<%=request.getRemoteUser()%>").getNodes();
				while (contentitr.hasNext()) {
					Node nodename = contentitr.nextNode();
					nodename.remove();
				}
				*/
				NodeIterator useritr = session.getRootNode().getNode("content").getNode("user").getNodes();
				while (useritr.hasNext()) {
					datajson = new JSONObject();
					Node usernode = useritr.nextNode();
					if(!usernode.getName().equals("<%=request.getRemoteUser()%>")){
						//out.println("Step 1");
						//out.println("usernode.getName() : "+usernode.getName());
					if (usernode.hasNode("Lead_Converter")) {
						//out.println("Step 2");
						if (usernode.getNode("Lead_Converter").hasNode("Email") && usernode.getNode("Lead_Converter").getNode("Email").hasNode("Campaign")) {
							
						
						Node currentcampaignname = usernode.getNode("Lead_Converter").getNode("Email")
								.getNode("Campaign").getNode("Explore");
						//out.println("currentcampaignname : "+currentcampaignname.getName());
						//out.println("Step 2.1");
						String currentcampaign = currentcampaignname.getProperty("Current_Campaign").getString().replace("@", "_");
						//out.println("currentcampaign : "+currentcampaign);
						//out.println("Step 2.2");
						NodeIterator leadconverteritr = usernode.getNode("Lead_Converter").getNode("Email")
								.getNode("Campaign").getNode("Explore").getNodes();
						//out.println("Step 2.3");
						while (leadconverteritr.hasNext()) {
							//out.println("Step 3");
							Node campaignnodes = leadconverteritr.nextNode();
							String campaignname = campaignnodes.getName();// currentcampaign
							//out.println("currentcampaign: "+currentcampaign+  "    campaignname: "+campaignname);
							if (currentcampaign.equals(campaignname)) {
								//out.println("Step 4");
								String campaignid = campaignnodes.getProperty("Campaign_Id").getString();
								String createdby = campaignnodes.getProperty("CreatedBy").getString();
								String list_id = campaignnodes.getProperty("List_Id").getString();
								out.println("campaignid : " + campaignid + "createdby :" + createdby);
								datajson.put("Campaign_Id", campaignid);
								datajson.put("Created_By", createdby);
								datajson.put("List_Id", list_id);
								mainjsonArray.put(datajson);
								mainjson.put("Data", mainjsonArray);
								out.println(mainjson);
							}
						}
					  }
					}
					}
				}
			} catch (Exception ex) {
				out.println("Exception ex :=" + ex.getMessage() + ex.getCause());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("phpdatacoldlead")) {

			try {
				Session session = null;
				session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));
				JSONObject mainjson = new JSONObject();
				JSONArray mainjsonArray = new JSONArray();
				JSONObject datajson = null;
				NodeIterator contentitr = session.getRootNode().getNode("content").getNode("user")
						.getNode("<%=request.getRemoteUser()%>").getNodes();
				while (contentitr.hasNext()) {
					Node nodename = contentitr.nextNode();
					nodename.remove();
				}
				NodeIterator useritr = session.getRootNode().getNode("content").getNode("user").getNodes();
				while (useritr.hasNext()) {
					datajson = new JSONObject();
					Node usernode = useritr.nextNode();
					if (usernode.hasNode("Lead_Converter")) {
						Node currentcampaignname = usernode.getNode("Lead_Converter").getNode("Email")
								.getNode("Campaign").getNode("Inform");
						String currentcampaign = currentcampaignname.getProperty("Current_Campaign").getString();

						NodeIterator leadconverteritr = usernode.getNode("Lead_Converter").getNode("Email")
								.getNode("Campaign").getNode("Inform").getNodes();
						while (leadconverteritr.hasNext()) {
							Node campaignnodes = leadconverteritr.nextNode();
							String campaignname = campaignnodes.getName();// currentcampaign
							if (currentcampaign.equals(campaignname)) {
								String campaignid = campaignnodes.getProperty("Campaign_Id").getString();
								String createdby = campaignnodes.getProperty("CreatedBy").getString();
								String list_id = campaignnodes.getProperty("List_Id").getString();
								out.println("campaignid : " + campaignid + "createdby :" + createdby);
								datajson.put("Campaign_Id", campaignid);
								datajson.put("Created_By", createdby);
								datajson.put("List_Id", list_id);
								mainjsonArray.put(datajson);
								mainjson.put("Data", mainjsonArray);
								out.println(mainjson);
							}
						}
					}
				}
			} catch (Exception ex) {
				out.println(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("phpdatahotlead")) {

			try {
				Session session = null;
				session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));
				JSONObject mainjson = new JSONObject();
				JSONArray mainjsonArray = new JSONArray();
				JSONObject datajson = null;
				NodeIterator contentitr = session.getRootNode().getNode("content").getNode("user")
						.getNode("<%=request.getRemoteUser()%>").getNodes();
				while (contentitr.hasNext()) {
					Node nodename = contentitr.nextNode();
					nodename.remove();
				}
				NodeIterator useritr = session.getRootNode().getNode("content").getNode("user").getNodes();
				while (useritr.hasNext()) {
					datajson = new JSONObject();
					Node usernode = useritr.nextNode();
					if (usernode.hasNode("Lead_Converter")) {
						Node currentcampaignname = usernode.getNode("Lead_Converter").getNode("Email")
								.getNode("Campaign").getNode("Convert");
						String currentcampaign = currentcampaignname.getProperty("Current_Campaign").getString();
						NodeIterator leadconverteritr = usernode.getNode("Lead_Converter").getNode("Email")
								.getNode("Campaign").getNode("Convert").getNodes();
						while (leadconverteritr.hasNext()) {
							Node campaignnodes = leadconverteritr.nextNode();
							String campaignname = campaignnodes.getName();// currentcampaign
							if (currentcampaign.equals(campaignname)) {
								String campaignid = campaignnodes.getProperty("Campaign_Id").getString();
								String createdby = campaignnodes.getProperty("CreatedBy").getString();
								String list_id = campaignnodes.getProperty("List_Id").getString();
								out.println("campaignid : " + campaignid + "createdby :" + createdby);
								datajson.put("Campaign_Id", campaignid);
								datajson.put("Created_By", createdby);
								datajson.put("List_Id", list_id);
								mainjsonArray.put(datajson);
								mainjson.put("Data", mainjsonArray);
								out.println(mainjson);
							}
						}

					}

				}

			} catch (Exception ex) {
				out.println(ex.getMessage());
			}
		}

	}

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

	}
}
