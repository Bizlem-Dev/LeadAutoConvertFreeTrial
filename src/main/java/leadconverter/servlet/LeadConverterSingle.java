package leadconverter.servlet;

import java.io.IOException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.*;

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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/LeadConverterSingle" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class LeadConverterSingle extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repos;

	// @Reference
	// private SchedulerService product;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		if (request.getRequestPathInfo().getExtension().equals("scheduler")) {

		 try {
			 Session session = null;
			 session = repos.login(new SimpleCredentials("admin", "admin"
			 .toCharArray()));
			//
			// Node content = session.getRootNode().getNode("content");
			// Node usernode=null;
			// Node emailnode=null;
			// Node campaignnode=null;
			// Node campaignemailnode=null;
			// out.println("Content Node : : : "+content);
			// if(!content.hasNode("ArrayofEmails")) {
			// usernode = content.addNode("ArrayofEmails");
			// out.println("Array Node : : : "+usernode);
			// } else{
			// usernode=content.getNode("ArrayofEmails");
			// out.println("Usernode : : :"+usernode);
			//
			// }
			// if(!content.hasNode("CampaignIdNode")) {
			// usernode = content.addNode("CampaignIdNode");
			// out.println("CampaignIdNode : : : "+usernode);
			// } else{
			// usernode=content.getNode("CampaignIdNode");
			// out.println("Usernode : : :"+usernode);
			//
			// }
			// String emailid=request.getParameter("emailist");
			// String campaignid=request.getParameter("campaignid");
			// out.println("Campaign Id : : :"+campaignid);
			// out.println("Email id :"+emailid);
			// String[] split=emailid.split(",");
			// String[] splitcampaignid=campaignid.split(",");
			// for(int i=0;i<split.length;i++)
			// {
			// out.println("Length : "+split.length);
			//
			//
			// if(!(usernode.hasNode(split[i].replace("@", "_")))) {
			// out.println("True or False : : : "+!(usernode.hasNode(split[i].replace("@",
			// "_"))));
			// emailnode=usernode.addNode(split[i].replace("@", "_"));
			// campaignnode=emailnode.addNode(campaignid);
			// emailnode.setProperty("Email_Id", split[i].replace("@", "_"));
			// out.println("Email after Spliting: : : "+ split[i].replace("@", "_"));
			// //usernode.setProperty("Email", split[i]);
			// session.save();
			// }
			//
			// }
			// for(int j=0;j<splitcampaignid.length;j++)
			// {
			// out.println("Length : "+splitcampaignid.length);
			//
			//
			// if(!(usernode.hasNode(split[j].replace("@", "_")))) {
			// out.println("True or False : : : "+!(usernode.hasNode(split[j].replace("@",
			// "_"))));
			// campaignnode=usernode.addNode(campaignid);
			// campaignemailnode=campaignnode.addNode(campaignid);
			// campaignemailnode.setProperty("Email_Id", split[j].replace("@", "_"));
			// out.println("Email after Spliting: : : "+ split[j].replace("@", "_"));
			// //usernode.setProperty("Email", split[i]);
			// session.save();
			// }
			//
			// }
			// // session.save();
			// }
			//
			// catch (Exception e) {
			// out.println("Exception : : :"+e.getMessage());
			// }
			//Node content = session.getRootNode().getNode("content");
				String statussling ="Unknown";
String statusjbpm="Unknown";
if(statussling.equals(statusjbpm)) {

			NodeIterator mainnode=session.getRootNode().getNode("content").getNode("user").getNode("viki_gmail.com").getNode("Lead_Converter").getNode("Email").getNode("Campaign").getNodes();
			//out.println(mainnode);
			Node typenode=null;
			String typename=null;
			String Current_Campaign =null;
			while(mainnode.hasNext()) {
				
				 typenode=mainnode.nextNode();//Explore//Inform
				 typename=typenode.getName();
				 out.println("Typename : : "+typename);
				 Current_Campaign=typenode.getProperty("Current_Campaign").getString();
				 out.println("Current_Campaign : : "+Current_Campaign);//Explore_0;

				NodeIterator typenodeitr=session.getRootNode().getNode("content").getNode("user").getNode("viki_gmail.com").getNode("Lead_Converter").getNode("Email").getNode("Campaign").getNode(typename).getNodes();
			while(typenodeitr.hasNext()) {
			
				Node campaignnextnode=typenodeitr.nextNode();//Explore//Inform//explore_0;
				out.println("campaignnextnode"+campaignnextnode);
			
		String currentcampaignnextname=campaignnextnode.getName();
		 out.println("currentcampaignnextname : : "+currentcampaignnextname);
		 if(Current_Campaign.equals(currentcampaignnextname)) {
			 out.println("If");
				typenode.setProperty("Current_Campaign", currentcampaignnextname);

		 }
		 else {
			 out.println("Else");
		typenode.setProperty("Current_Campaign", currentcampaignnextname);
		 }
		session.save();
		
			}
			}
}
else {
	
	out.println("Else");
}
		 }
		 catch(Exception ex) {
			 
			 out.println("Message : "+ex.getMessage());
			 
		 }
		 
			
			
			
			
			
			
			
			
			
			
			
		} else if (request.getRequestPathInfo().getExtension().equals("siva")) {

			PrintWriter o = response.getWriter();
			try {
				request.getRequestDispatcher("/content/static/.siva").forward(request, response);
				// o.print("working");
			} catch (Exception e) {
				o.print(e.getMessage());
			}

			// response.getOutputStream().println(request.getParameter("companyName"));

		}
		// Date c=new Date();
		// SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		// String str2 = sd.format(c);
		// out.println("Date : "+str2);
		//
		// String querystr="select [Created_Date] from [nt:base] where
		// (contains('Created_Date','*"+str2+"*')) and
		// ISDESCENDANTNODE('/content/fileup/')";
		//
		//
		// Workspace workspace = session.getWorkspace();
		// Query query = workspace.getQueryManager().createQuery(
		// querystr, Query.JCR_SQL2);
		// QueryResult result = query.execute();
		// NodeIterator iterator = result.getNodes();
		// int cout=0;
		// while (iterator.hasNext()) {
		// out.println("inside while");
		// out.println(cout);
		// fileup = iterator.nextNode();
		// fileup.getProperty("filepath");
		// out.println("Fileup :"+fileup);
		// mainarray.put(fileup); }
		// }catch (Exception e) {
		// e.printStackTrace();
		// }
		//

		// out.println("mainarray :: "+mainarray);

	}

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		if (request.getRequestPathInfo().getExtension().equals("email")) {

			try {
				Session session = null;
				session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));

				Node content = session.getRootNode().getNode("content");
				Node emailnode = null;
				Node campaignidnode = null;
				Node campaignnode = null;
				Node campaignemailnode = null;
				Node campnodeid = null;
				Node customernode = null;
				Node customeremailnode = null;
				Node subscribernode = null;
				int i = 0;
				// int j = 0;
				int counter=0;
				out.println("Content Node : : : " + content);
				if (!content.hasNode("CustomerSingleNode")) {
					customernode = content.addNode("CustomerSingleNode");
					out.println("Array Node : : : " + customernode);
				} else {
					customernode = content.getNode("CustomerSingleNode");
					out.println("Usernode : : :" + customernode);
				}

				if (!content.hasNode("SubscriberSingleNode")) {
					subscribernode = content.addNode("SubscriberSingleNode");
					out.println("Array Node : : : " + subscribernode);
				} else {
					subscribernode = content.getNode("SubscriberSingleNode");
					out.println("Usernode : : :" + subscribernode);
				}
				if (!content.hasNode("CampaignIdSingleNode")) {
					campaignnode = content.addNode("CampaignIdSingleNode");
					// out.println("CampaignIdNode : : : " + arrayofemails);
				} else {
					campaignnode = content.getNode("CampaignIdSingleNode"); // out.println("Usernode : : :" +
																			// arrayofemails);
				}
				String emailid = request.getParameter("emailist");
				String campaignid = request.getParameter("campaignid");
				String customerid = request.getParameter("customerid");
				String status = request.getParameter("status");
				out.println("Campaign Id : : :" + campaignid);
				out.println("Email id :" + emailid);
				out.println("Customer Email : " + customerid);
				String[] split = emailid.split(",");
				String[] statussplit = status.split(",");

				// String[] customeridsplit=customerid.split(",");
				// out.println("Customer length " +customeridsplit.length);
				if (!customernode.hasNode(customerid.replace("@", "_"))) {
//customernode.setProperty("Count", counter++);

					out.println("Customer Id : " + !customernode.hasNode(customerid.replace("@", "_")));
					customeremailnode = customernode.addNode(customerid.replace("@", "_"));

					campaignidnode = customeremailnode.addNode("Campaign Id");
					campaignidnode.addNode(campaignid);
				}
				if (!campaignnode.hasNode(campaignid)) {
					campnodeid = campaignnode.addNode(campaignid);
					out.println("CampNode id In If" + campnodeid);
					// if(!(campnodeid.hasNode(split[i].replace("@", "_")))) {
					// out.println("If Campaign Node Email : : :"+emailincampaignode);
					// }
					for (i = 0; i < split.length; i++) {
						out.println("If");
						out.println("Split_Length : " + split.length);

						if (!(subscribernode.hasNode(split[i].replace("@", "_")))) {
							// out.println("Email Node True or False : : : "
							// + !(subscribernode.hasNode(split[i].replace("@", "_"))));
							emailnode = subscribernode.addNode(split[i].replace("@", "_"));
							emailnode.setProperty("Campaign_Id", campaignid);
							campaignemailnode = emailnode.addNode("CampaignId");
							campaignidnode = campaignemailnode.addNode(campaignid);
							campaignidnode.setProperty("Status", statussplit[i]);
						}
						if (!(campnodeid.hasNode(customerid.replace("@", "_")))) {
							// "+!campnodeid.hasNode(split[i].replace("@", "_")));
							campnodeid.addNode(customerid.replace("@", "_"));
						}
					
 						session.save();
					}
				} else {
					out.println("Else");

					for (i = 0; i < split.length; i++) {
						campnodeid = session.getRootNode().getNode("content").getNode("CampaignIdSingleNode")
								.getNode(campaignid);
						// out.println("Else CampNode Id : : : " + campnodeid);
						// out.println("Campnode id in Else: : : True or False : : : "
						// + campnodeid.hasNode(split[i].replace("@", "_")));

						if (!(campnodeid.hasNode(customerid.replace("@", "_")))) {
							out.println("Should Not ADD"); // out.println("Campnode id in Else: : : True or False : : :
															// "+!campnodeid.hasNode(split[i].replace("@", "_")));
							campnodeid.addNode(customerid.replace("@", "_"));// email
							// emailincampaignode.setProperty("Campaign_Id", campaignid);
						}
						// out.println("Split_Length : " + split.length);
						if (!(subscribernode.hasNode(split[i].replace("@", "_")))) {
							/// out.println("Email Node True or False : : : "
							// + !(subscribernode.hasNode(split[i].replace("@", "_"))));
							emailnode = subscribernode.addNode(split[i].replace("@", "_"));
							emailnode.setProperty("Campaign_Id", campaignid);
							campaignemailnode = emailnode.addNode("CampaignId");
							campaignidnode = campaignemailnode.addNode(campaignid);
							campaignidnode.setProperty("Status", statussplit);
							// out.println("Email after Spliting: : : " + split[i].replace("@", "_"));
						}

						// out.println("Email in Campaign : : : " + split[i]);

						session.save();
					}

				}
			}

			catch (Exception e) {
				out.println("Exception : : :" + e.getMessage());
			}
		}
	}
}
