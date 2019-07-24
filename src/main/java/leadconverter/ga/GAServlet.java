package leadconverter.ga;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;




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
import org.bson.conversions.Bson;
import org.jsoup.Jsoup;
import org.osgi.service.http.HttpService;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import java.io.*;
import java.net.*;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import leadconverter.doctiger.LogByFileWriter;
import leadconverter.mongo.ConnectionHelper;
import leadconverter.mongo.MongoDAO;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/ga" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GAServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
		protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		  
		  
		if(request.getRequestPathInfo().getExtension().equals("getCampainsFunnelDetails")) {
	    	Query query;
			Node campaignNode = null;
			Node funnelNode = null;
			Node subFunnelNode = null;
			String campaign_id=null;
			/*
			//Property of Sub Funnel node
			String subFunnelCounter = null;
			String Current_Campaign = null;
			
			//Property of Funnel node
			String funnelCounter =null;
			
			//Property of campaign node
			String CreatedBy=null;
			String Type=null;
			String Campaign_Id=null;
			String Subject=null;
			String Body=null;
			String campaign_status=null;
			String Campaign_Date=null;
			String List_Id=null;
			*/
			JSONObject campaign_details=new JSONObject();
			try {
				campaign_id=request.getParameter("campaign_id");
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				String campaign_sling_query = "select * from [nt:base] where (contains('Campaign_Id','"+campaign_id+"')) and ISDESCENDANTNODE('/content/user')";
			    Workspace workspace = session.getWorkspace();
				query = workspace.getQueryManager().createQuery(campaign_sling_query, Query.JCR_SQL2);
				QueryResult queryResult = query.execute();
				NodeIterator iterator = queryResult.getNodes();
				//content/user/viki_gmail.com/Lead_Converter/Email/Funnel/17-May_funnel1/Explore/viki_gmail.com_Explore_1
				while (iterator.hasNext()) {
					campaignNode = iterator.nextNode();
					subFunnelNode=campaignNode.getParent();
					funnelNode=subFunnelNode.getParent();
					funnelNode.getParent().getParent().getParent().getParent().getName();//getting Node "viki_gmail.com" from Node ""17-May_funnel1
					
					if(campaignNode.hasProperty("Body")){
						campaign_details.put("body", campaignNode.getProperty("Body").getString());
					}else{
						campaign_details.put("body", "null");
					}
					if(campaignNode.hasProperty("Campaign_Id")){
						campaign_details.put("campaign_id", campaignNode.getProperty("Campaign_Id").getString());
					}else{
						campaign_details.put("campaign_id", "null");
					}
					if(campaignNode.hasProperty("CreatedBy")){
						campaign_details.put("created_by", campaignNode.getProperty("CreatedBy").getString());
					}else{
						campaign_details.put("created_by", "null");
					}
					if(campaignNode.hasProperty("List_Id")){
						campaign_details.put("list_id", campaignNode.getProperty("List_Id").getString());
					}else{
						campaign_details.put("list_id", "null");
					}
					if(campaignNode.hasProperty("Subject")){
						campaign_details.put("subject", campaignNode.getProperty("Subject").getString());
					}else{
						campaign_details.put("subject", "null");
					}
					if(campaignNode.hasProperty("Type")){
						campaign_details.put("type", campaignNode.getProperty("Type").getString());
					}else{
						campaign_details.put("type", "null");
					}
					campaign_details.put("campaign_node_name", campaignNode.getName());	
					campaign_details.put("sub_funnel_node_name", subFunnelNode.getName());
					campaign_details.put("funnel_node_name", funnelNode.getName());
					campaign_details.put("campaign_node_path", campaignNode.getPath());
					
					break;
				}
				out.println(campaign_details);
			} catch (Exception e) {
				out.print(e.getMessage());
			}finally{
				
			}
	     }if(request.getRequestPathInfo().getExtension().equals("getCampainsMongoDetails")){
	    	 MongoClient mongoClient = null;
	 	    MongoDatabase database  = null;
	 	    MongoCollection<org.bson.Document> collection = null;
	 	    Document myDoc=null;
	 	     try {
	         	 mongoClient=ConnectionHelper.getConnection();
	              database=mongoClient.getDatabase("phplisttest");
	              collection=database.getCollection("campaign_details");
	              Bson filter = and(eq("Campaign_Id", "1095"),
	                   eq("subscribers.email", "akhilesh@bizlem.com"));
	              MongoCursor<org.bson.Document> iterator = collection.find(filter).iterator();
	              while (iterator.hasNext()) {
	            	  org.bson.Document doc = iterator.next();
	                  out.println("doc : "+doc);
	                  out.println("====================");
	                  out.println("doc : "+doc.toJson());
	              }
	                        
	        } catch (Exception e) {
	        	out.println("Exception : "+e.getMessage());
	            //e.printStackTrace();
	            //throw new RuntimeException(e);
			} finally {
				ConnectionHelper.closeConnection(mongoClient);
			}
	    	 
	     }if(request.getRequestPathInfo().getExtension().equals("getCampainsMongoDetailsNew")){
	    	 MongoClient mongoClient = null;
	 	    MongoDatabase database  = null;
	 	    MongoCollection<org.bson.Document> collection = null;
	 	    Document myDoc=null;
	 	   String campaign_id=request.getParameter("campaign_id");
	 	   String email=request.getParameter("email");
	 	  out.println("campaign_id : "+campaign_id+" email : "+email);
	 	     try {
	         	  mongoClient=ConnectionHelper.getConnection();
	              database=mongoClient.getDatabase("phplisttest");
	              collection=database.getCollection("campaign_details");
	              Bson filter = and(eq("Campaign_Id", campaign_id),
	                   eq("subscribers.email", email));
	              MongoCursor<org.bson.Document> iterator = collection.find(filter).iterator();
	              while (iterator.hasNext()) {
	            	  org.bson.Document doc = iterator.next();
	                  out.println("doc : "+doc.toJson());
	              }
	                        
	        } catch (Exception e) {
	        	out.println("Exception : "+e.getMessage());
	            //e.printStackTrace();
	            //throw new RuntimeException(e);
			} finally {
				ConnectionHelper.closeConnection(mongoClient);
			}
	    	 
	     }else{
			 out.print("Rrquested extension is not an ESP resource By GA");
		 }
		 

			 
	}
}

	

