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
import javax.jcr.PathNotFoundException;
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

import leadconverter.create.rule.CreateRuleEngine;
import leadconverter.mongo.MongoDAO;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/uidata" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class UIDataProcessServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };
 
	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject=null;
    
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		if(request.getRequestPathInfo().getExtension().equals("configurationData")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.configuration").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("funnelData")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.funnel").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("indexData")) {
		     try {
					JSONObject dataJsonObj=new JSONObject(request.getParameter("data").toString());
					//out.print(dataJsonObj);
					//out.print("dataJsonObj : "+dataJsonObj);
					Session session = null;
					session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
					Node ip = session.getRootNode().getNode("content");
					String remoteuser=null;
					String funnelName=null;
					String fromName=null;
					String fromEmailAddress=null;
					String trackOpens=null;
					String trackClicks=null;
					String trackPlainTextClicks=null;
					String googleAnalyticssLinkTracking=null;
					String autoTweetAfterSending=null;
					String autoPost2SocialMedia=null;
					
					String campaignname = null;
					Node getcampaignnamenode = null;
					Node gettypeinsubscribernode = null;
					NodeIterator gettypeinsubscribernodeitr = null;
					String typenameinsubscriber = null;
					NodeIterator typenodeitr = null;
					
					remoteuser=dataJsonObj.getString("remoteuser");
					remoteuser=remoteuser.replace("@", "_");
					funnelName=dataJsonObj.getString("funnelName");
					fromName=dataJsonObj.getString("fromName");
					fromEmailAddress=dataJsonObj.getString("fromEmailAddress");
					trackOpens=dataJsonObj.getString("trackOpens");
					trackClicks=dataJsonObj.getString("trackClicks");
					trackPlainTextClicks=dataJsonObj.getString("trackPlainTextClicks");
					googleAnalyticssLinkTracking=dataJsonObj.getString("googleAnalyticssLinkTracking");
					autoTweetAfterSending=dataJsonObj.getString("autoTweetAfterSending");
					autoPost2SocialMedia=dataJsonObj.getString("autoPost2SocialMedia");
					
					Node funnelNode=null;
					Node funnelNameNode = null;
					if(session.getRootNode().hasNode("content/user/"+remoteuser+"/Lead_Converter/Email/Funnel")){
						funnelNode = session.getRootNode().getNode("content/user/"+remoteuser+"/Lead_Converter/Email/Funnel");
					}else{
						funnelNode = createFunnelNode(session,remoteuser);
					}
					
					
					if (!funnelNode.hasNode(funnelName)) {
						funnelNameNode = funnelNode.addNode(funnelName);
						funnelNameNode.setProperty("funnelName", funnelName);
						funnelNameNode.setProperty("fromName", fromName);
						funnelNameNode.setProperty("fromEmailAddress", fromEmailAddress);
						funnelNameNode.setProperty("trackOpens", trackOpens);
						funnelNameNode.setProperty("trackClicks", trackClicks);
						funnelNameNode.setProperty("trackPlainTextClicks", trackPlainTextClicks);
						funnelNameNode.setProperty("googleAnalyticssLinkTracking", googleAnalyticssLinkTracking);
						funnelNameNode.setProperty("autoTweetAfterSending", autoTweetAfterSending);
						funnelNameNode.setProperty("autoPost2SocialMedia", autoPost2SocialMedia);
						//out.print("Funnel : "+funnelName +" has Created Now");
						out.print("Funnel Created");
						//String rule_engine_response=CreateRuleEngine.createRuleEngine(funnelName.replace(" ", "_"));
						String rule_engine_response=CreateRuleEngine.createRuleEngine(remoteuser,funnelName.replace(" ", "_"));
						
						out.print("rule_engine_response : "+rule_engine_response);
					}else{
						funnelNameNode = funnelNode.getNode(funnelName);
						//out.print("Funnel : "+funnelName +" Exists");
						out.print("Funnel Exists");
						
					}
					
					
					/*
					String slingqery = "select * from [nt:base] where funnelName = '"+funnelName+"' "
							+ "and ISDESCENDANTNODE('/content/user/"+remoteuser+"/Lead_Converter/Email/Funnel')";
		            Workspace workspace = session.getWorkspace();
					
		            Query query = workspace.getQueryManager().createQuery(slingqery, Query.JCR_SQL2);

					QueryResult queryResult = query.execute();
					NodeIterator iterator = queryResult.getNodes();
					
					if(iterator.hasNext()){
						out.print("Funnel : "+funnelName +" Exists");
					}else{
						out.print("Funnel : "+funnelName +" Not Exists Create One");
					}
					*/
					session.save();
					
				} catch (Exception ex) {

					out.println("Exception ex " + ex.getMessage());
				}
		 }else if(request.getRequestPathInfo().getExtension().equals("createLead")) {
		     try {
				   out.println(request.getParameter("jsonArr"));
		    	   JSONArray myJsonData = new JSONArray(request.getParameter("jsonArr"));
                   for (int i=0; i < myJsonData.length(); ++i) {
		    			//out.println(myJsonData.get(i));
		    	   }
		    	 
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("listData")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.list").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("subscriberslistData")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.subscriberslist").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("set-up-campaignData")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.set-up-campaign").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("statisticData")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.statistic").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("statistic2Data")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.statistic2").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("statistic3Data")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.statistic3").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("statistic4Data")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.statistic4").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else{
			 out.print("Rrquested extension is not an ESP resource");
		 }
	}
	public static Node createFunnelNode(Session session,String remoteuser){
		Node contentNode = null;
		Node userNode = null;
		Node loginUserNode = null;
		Node LeadConverterNode = null;
		Node emailNode = null;
		Node funnelNode = null;
		
		try {
			contentNode=session.getRootNode().getNode("content");
			if (!contentNode.hasNode("user")) {
				userNode = contentNode.addNode("user");
			}else{
				userNode = contentNode.getNode("user");
			}
			if (!userNode.hasNode(remoteuser)) {
				loginUserNode = userNode.addNode(remoteuser);
			}else{
				loginUserNode = userNode.getNode(remoteuser);
			}
			if (!loginUserNode.hasNode("Lead_Converter")) {
				LeadConverterNode = loginUserNode.addNode("Lead_Converter");
			}else{
				LeadConverterNode = loginUserNode.getNode("Lead_Converter");
			}
			if (!LeadConverterNode.hasNode("Email")) {
				emailNode = LeadConverterNode.addNode("Email");
			}else{
				emailNode = LeadConverterNode.getNode("Email");
			}
			if (!emailNode.hasNode("Funnel")) {
				funnelNode = emailNode.addNode("Funnel");
			}else{
				funnelNode = emailNode.getNode("Funnel");
			}
			if (!emailNode.hasNode("Funnel")) {
				funnelNode = emailNode.addNode("Funnel");
			}else{
				funnelNode = emailNode.getNode("Funnel");
			}

		} catch (PathNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return funnelNode;
	}
}

	
