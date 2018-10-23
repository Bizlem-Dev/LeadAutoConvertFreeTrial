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
		if (request.getRequestPathInfo().getExtension().equals("phpdataexplore")) {
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
								.getNode("Campaign").getNode("Explore");
						String currentcampaign = currentcampaignname.getProperty("Current_Campaign").getString();
						NodeIterator leadconverteritr = usernode.getNode("Lead_Converter").getNode("Email")
								.getNode("Campaign").getNode("Explore").getNodes();
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
