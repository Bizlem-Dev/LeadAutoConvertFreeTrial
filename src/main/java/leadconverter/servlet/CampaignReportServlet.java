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
import javax.jcr.query.InvalidQueryException;
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

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;

import leadconverter.mongo.ConnectionHelper;
import leadconverter.mongo.JSON_Reader;
import leadconverter.mongo.MongoDAO;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/CampaignReportApi" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")

public class CampaignReportServlet extends SlingAllMethodsServlet{
	@Reference
	private SlingRepository repo;
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
	}
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		    PrintWriter out = response.getWriter();
		     if (request.getRequestPathInfo().getExtension().equals("ucsAvailableURLs")) {
		    	try {
					request.getRequestDispatcher("/content/static/.ucsAvailableURLs").forward(request, response);
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("ucsUrlClickStatistics")) {
		    	//out.print("CampaignReportServlet extension funnelView !");ucsCampaignClickStatistics.esp
		    	String url=request.getParameter("url");
		    	try {
					request.getRequestDispatcher("/content/static/.ucsUrlClickStatistics").forward(request, response);
					// o.print("working");
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("ucsUrlClickStatisticsData")) {
		    	//out.print("CampaignReportServlet extension funnelView !");
		    	String url=request.getParameter("url");
		    	try {
			    		MongoDAO mdao=new MongoDAO();
			    		if(!url.equals("null")){
			    		   out.print(mdao.ucsUrlClickStatistics("url",url));
			    		}else{
			    		   out.print("Url Should not be Null!");
			    		}
		    		} catch (Exception e) {
					   out.print(e.getMessage());
				    }
		    }else if (request.getRequestPathInfo().getExtension().equals("ucsCampaignClickStatistics")) {
		    	//out.print("CampaignReportServlet extension funnelView !");
		    	String url=request.getParameter("url");
		    	try {
					request.getRequestDispatcher("/content/static/.ucsCampaignClickStatistics").forward(request, response);
					// o.print("working");
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("ucsCampaignClickStatisticsData")) {
		    	//out.print("CampaignReportServlet extension funnelView !");
		    	String url=request.getParameter("url");
		    	String campaign_id=request.getParameter("campaign_id");
		    	try {
			    		MongoDAO mdao=new MongoDAO();
			    		if(!url.equals("null")){
			    		    out.print(mdao.ucsCampaignClickStatistics("url",url,campaign_id));
			    			//out.print("campaign_id :"+campaign_id);
			    			//out.print("url :"+url);
			    		}else{
			    		   out.print("Url Should not be Null!");
			    		}
		    		} catch (Exception e) {
					   out.print(e.getMessage());
				    }
		    }else if (request.getRequestPathInfo().getExtension().equals("ucsUserClickStatisticsCampaignBased")) {
		    	//out.print("CampaignReportServlet extension funnelView !");
		    	String url=request.getParameter("url");
		    	try {
					request.getRequestDispatcher("/content/static/.ucsUserClickStatisticsCampaignBased").forward(request, response);
					// o.print("working");
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("ucsUserClickStatisticsCampaignBasedData")) {
		    	//out.print("CampaignReportServlet extension funnelView !");
		    	String campaign_id=request.getParameter("campaign_id");
		    	try {
			    		MongoDAO mdao=new MongoDAO();
			    		if(!campaign_id.equals("null")){
			    		   out.print(mdao.findCampaignBasedOnCampaignID("funnel",campaign_id));
			    		}else{
			    		   out.print("campaign_id should not be null");
			    		}
		    		} catch (Exception e) {
					   out.print(e.getMessage());
				    }
		    }else if (request.getRequestPathInfo().getExtension().equals("ucsSubscriberClickStatistics")) {
		    	//out.print("CampaignReportServlet extension funnelView !");
		    	String url=request.getParameter("url");
		    	try {
					request.getRequestDispatcher("/content/static/.ucsSubscriberClickStatistics").forward(request, response);
					// o.print("working");
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("ucsSubscriberClickStatisticsData")) {
		    	//out.print("CampaignReportServlet extension funnelView !");
		    	String campaign_id=request.getParameter("campaign_id");
		    	String funnel_name=request.getParameter("funnel_name");
		    	String subfunnel_name=request.getParameter("subfunnel_name");
		    	String userid=request.getParameter("userid");
		    	//out.print("userid : "+userid);
		    	//out.print("campaign_id : "+campaign_id);
		    	//out.print("funnel_path : "+request.getParameter("funnel_path"));
		    	try {
			    		MongoDAO mdao=new MongoDAO();
			    		if(!userid.equals("null")){
			    		   out.print(mdao.findUrlClickStatisticsBasedOnCampaignIDAndUserID("funnel",campaign_id,userid));
			    		}else{
			    		   out.print("User id is not correct");
			    		}
		    		} catch (Exception e) {
					   out.print(e.getMessage());
				    }
		    }else if (request.getRequestPathInfo().getExtension().equals("funnelView")) {
		    	out.print("CampaignReportServlet extension funnelView !");
		    	try {
					request.getRequestDispatcher("/content/static/.funnelView").forward(request, response);
					// o.print("working");
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("campaignView")) {
		    	try {
		    		request.getRequestDispatcher("/content/static/.campaignView").forward(request, response);
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("campaignViewData")) {
		    	//out.print("CampaignReportServlet extension campaignView !");
		    	String campaign_id=request.getParameter("campaign_id");
		    	String funnel_name=request.getParameter("funnel_name");
		    	String subfunnel_name=request.getParameter("subfunnel_name");
		    	try {
			    		MongoDAO mdao=new MongoDAO();
			    		if(!campaign_id.equals("null")){
			    		   out.print(mdao.findCampaignBasedOnCampaignID("funnel",campaign_id));
			    		}else{
			    		   out.print(mdao.findCampaignBasedOnFunnelAndSubFunnelName("funnel",funnel_name,subfunnel_name));
			    		}
		    		} catch (Exception e) {
					   out.print(e.getMessage());
				    }
		    }else if (request.getRequestPathInfo().getExtension().equals("subscribersView")) {
		    	try {
		    		request.getRequestDispatcher("/content/static/.subscribersView").forward(request, response);
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("subscribersViewData")) {
		    	//out.print("CampaignReportServlet extension campaignView !");
		    	String campaign_id=request.getParameter("campaign_id");
		    	String funnel_name=request.getParameter("funnel_name");
		    	String subfunnel_name=request.getParameter("subfunnel_name");
		    	try {
			    		MongoDAO mdao=new MongoDAO();
			    		if(!campaign_id.equals("null")){
			    		   out.print(mdao.findCampaignBasedOnCampaignID("funnel",campaign_id));
			    		}else{
			    		   out.print(mdao.findCampaignBasedOnFunnelAndSubFunnelName("funnel",funnel_name,subfunnel_name));
			    		}
		    		} catch (Exception e) {
					   out.print(e.getMessage());
				    }
		    }else if (request.getRequestPathInfo().getExtension().equals("urlView")) {
		    	//out.print("CampaignReportServlet extension urlView !");
		    	try {
					request.getRequestDispatcher("/content/static/.urlView").forward(request, response);
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("urlViewData")) {
		    	//out.print("CampaignReportServlet extension campaignView !");
		    	String campaign_id=request.getParameter("campaign_id");
		    	String funnel_name=request.getParameter("funnel_name");
		    	String subfunnel_name=request.getParameter("subfunnel_name");
		    	String userid=request.getParameter("userid");
		    	//out.print("userid : "+userid);
		    	//out.print("campaign_id : "+campaign_id);
		    	//out.print("funnel_path : "+request.getParameter("funnel_path"));
		    	try {
			    		MongoDAO mdao=new MongoDAO();
			    		if(!userid.equals("null")){
			    		   out.print(mdao.findUrlClickStatisticsBasedOnCampaignIDAndUserID("funnel",campaign_id,userid));
			    		}else{
			    		   out.print("User id is not correct");
			    		}
		    		} catch (Exception e) {
					   out.print(e.getMessage());
				    }
		    }else if (request.getRequestPathInfo().getExtension().equals("funnelList")) {
		    	//out.print("CampaignReportServlet extension funnelList !");
		    	try {
		    		MongoDAO mdao=new MongoDAO();
		    		out.print(mdao.distinctFunnel().toString());
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("slingFunnelList")) {

		    	//out.print("CampaignReportServlet extension slingSubFunnelList !");
		    	JSONArray responseArr=null;
				try {
					//out.println("CampaignStatistic Extension called");
					Session session = null;
					String user=request.getRemoteUser().replace("@", "_");
				    //out.println("Logged In User  : "+user);
				    session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				    Node content = session.getRootNode().getNode("content/user/"+user+"/Lead_Converter/Email/Funnel");
					NodeIterator iterator = content.getNodes();
					responseArr=new JSONArray();
					while (iterator.hasNext()) {
						responseArr.put(iterator.nextNode().getName());
					}
					out.println(responseArr);
				} catch (Exception e) {
					out.print(e.getMessage());
				}
				
			
		    }else if (request.getRequestPathInfo().getExtension().equals("subFunnelList")) {
		    	//out.print("CampaignReportServlet extension subFunnelList !");
		    	String funnel_id=request.getParameter("funnel_id");
		    	try {
		    		MongoDAO mdao=new MongoDAO();
		    		out.print(mdao.distinctSubFunnel(funnel_id));
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("slingSubFunnelList")) {

		    	//out.print("CampaignReportServlet extension slingSubFunnelList !");
		    	String funnel_id=request.getParameter("funnel_id");
		    	JSONArray responseArr=null;
				
				try {
					//out.println("CampaignStatistic Extension called");
					Session session = null;
					String user=request.getRemoteUser().replace("@", "_");
				    //out.println("Logged In User  : "+user);
				    session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				    Node content = session.getRootNode().getNode("content/user/"+user+"/Lead_Converter/Email/Funnel/"+funnel_id);
					NodeIterator iterator = content.getNodes();
					responseArr=new JSONArray();
					while (iterator.hasNext()) {
						responseArr.put(iterator.nextNode().getName());
					}
					out.println(responseArr);
				} catch (Exception e) {
					out.print(e.getMessage());
				}
				
			
		    }else if (request.getRequestPathInfo().getExtension().equals("campaignList")) {
		    	//out.print("CampaignReportServlet extension campaignList !");
		    	//String funnelName=request.getParameter("funnelName");
		    	String funnel_id=request.getParameter("funnel_id");
		    	try {
		    		MongoDAO mdao=new MongoDAO();
		    		out.print(mdao.findCampaign("funnel",funnel_id));
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("findUniqueUrl")) {
		    	//out.print("CampaignReportServlet extension campaignList !");
		    	//String funnelName=request.getParameter("funnelName");
		    	String funnel_id=request.getParameter("funnel_id");
		    	try {
		    		MongoDAO mdao=new MongoDAO();
		    		out.print(mdao.findUniqueUrl("url",""));
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("subscribersViewBasedOnFunnel")) {
		    	//out.print("CampaignReportServlet extension campaignList !");
		    	//String funnelName=request.getParameter("funnelName");
		    	String url=request.getParameter("url");
		    	try {
					request.getRequestDispatcher("/content/static/.subscribersViewBasedOnFunnel").forward(request, response);
					// o.print("working");
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("subscribersViewBasedOnFunnelData")) {
		    	//out.print("CampaignReportServlet extension campaignList !");
		    	//String funnelName=request.getParameter("funnelName");
		    	String funnel_name=request.getParameter("funnel_name"); //funnelSelect
				String sub_funnel_name=request.getParameter("sub_funnel_name"); //subFunnelSelect
				String start_date=request.getParameter("start_date");
				String end_date=request.getParameter("end_date");
				String search_txt=request.getParameter("search_txt");
		    	String url=request.getParameter("url");
		    	try {
		    		MongoDAO mdao=new MongoDAO();
		    		if(sub_funnel_name.equals("showall")){
		    		    out.print(mdao.subscribersViewBasedOnSubFunnelData("subscribers","2","Inform","24 Oct 2018 06:43","24 Oct 2018 09:43"));
		    		}else{
		    			out.print(mdao.subscribersViewBasedOnFunnelData("subscribers","2","Inform","24 Oct 2018 06:43","24 Oct 2018 09:43"));
		    		}
					// o.print("working");
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("nodeTest")) {
		    	out.print("CampaignReportServlet extension nodeTest !");
		    	Session session = null;
				//String user=request.getRemoteUser().replace("@", "_");
		    	String user="viki_gmail.com";
			    out.println("Logged In User  : "+user);
			    try {
			    	session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			    	Workspace workspace = session.getWorkspace();
			    	workspace.copy("/content/user/"+user+"/Lead_Converter/Email/Funnel/1", "/content/user/"+user+"/Lead_Converter/Email/Funnel/2");
					/*
			    	Node content = session.getRootNode().getNode("content/user/"+user+"/Lead_Converter/Email/Funnel");
					if(!content.hasNode("2")){
						content.addNode("2");
					}
					*/
						
					session.save();
				} catch (Exception e) {
					out.print(e.getMessage());
				}
		    }else if (request.getRequestPathInfo().getExtension().equals("ForDocTest")) {
		    	//out.print("CampaignReportServlet extension funnelView !");
		    	String campaign_id=request.getParameter("campaign_id");
		    	String funnel_name=request.getParameter("funnel_name");
		    	String subfunnel_name=request.getParameter("subfunnel_name");
		    	String userid=request.getParameter("userid");
		    	//out.print("userid : "+userid);
		    	//out.print("campaign_id : "+campaign_id);
		    	//out.print("funnel_path : "+request.getParameter("funnel_path"));
		    	try {
			    		MongoDAO mdao=new MongoDAO();
			    		if(!userid.equals("null")){
			    		   out.print(mdao.findUrlClickStatisticsBasedOnCampaignIDAndUserID("dummy_funnel",campaign_id,userid));
			    		}else{
			    		   out.print("User id is not correct");
			    		}
		    		} catch (Exception e) {
					   out.print(e.getMessage());
				    }
		    }
	}

}
