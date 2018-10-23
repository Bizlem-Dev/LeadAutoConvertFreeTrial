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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/LeadConverterDouble" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class LeadConverterDouble extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repos;

	// @Reference
	// private SchedulerService product;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		if (request.getRequestPathInfo().getExtension().equals("scheduler")) {

			// try {
			// Session session = null;
			// session = repos.login(new SimpleCredentials("admin", "admin"
			// .toCharArray()));
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
				Node emailincampaignode = null;
				Node subscribernode = null;
				int i = 0;
				out.println("Content Node : : : " + content);
				if (!content.hasNode("CustomerDoubleNode")) {
					customernode = content.addNode("CustomerDoubleNode");
					out.println("CustomerDoubleNode If : : : " + customernode);
				} else {
					customernode = content.getNode("CustomerDoubleNode");
					out.println("CustomerDoubleNode Else : : :" + customernode);
				}

				if (!content.hasNode("SubscriberDoubleNode")) {
					subscribernode = content.addNode("SubscriberDoubleNode");
					out.println(" SubscriberDoubleNode If : : : " + subscribernode);
				} else {
					subscribernode = content.getNode("SubscriberDoubleNode");
					out.println("SubscriberDoubleNode Else : : :" + subscribernode);
				}
				if (!content.hasNode("CampaignIdDoubleNode")) {
					campaignnode = content.addNode("CampaignIdDoubleNode");
					// out.println("CampaignIdNode : : : " + arrayofemails);
				} else {
					campaignnode = content.getNode("CampaignIdDoubleNode"); // out.println("Usernode : : :" +
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

					out.println("Customer Id : " + !customernode.hasNode(customerid.replace("@", "_")));
					customeremailnode = customernode.addNode(customerid.replace("@", "_"));
				}
				if (!campaignnode.hasNode(campaignid)) {
					campnodeid = campaignnode.addNode(campaignid);
					out.println("CampNode id In If" + campnodeid);

					for (i = 0; i < split.length; i++) {
						out.println("If");
						out.println("Split_Length : " + split.length);

						if (!(subscribernode.hasNode(split[i].replace("@", "_")))) {
							emailnode = subscribernode.addNode(split[i].replace("@", "_"));
							emailnode.setProperty("Campaign_Id", campaignid);
							campaignemailnode = emailnode.addNode("CampaignId");
							campaignidnode = campaignemailnode.addNode(campaignid);
							campaignidnode.setProperty("Status", statussplit[i]);

						}
						if (!(campnodeid.hasNode(customerid.replace("@", "_")))) {
							campnodeid.addNode(customerid.replace("@", "_"));
						}
						session.save();
					}
				} else {
					out.println("Else");

					for (i = 0; i < split.length; i++) {
						campnodeid = session.getRootNode().getNode("content").getNode("CampaignIdDoubleNode")
								.getNode(campaignid);
						out.println("Else CampNode Id : : : " + campnodeid);
						out.println("Campnode id in Else: : : True or False : : : "
								+ campnodeid.hasNode(split[i].replace("@", "_")));
						if (!(campnodeid.hasNode(customerid.replace("@", "_")))) {
							out.println("Should Not ADD");
							campnodeid.addNode(customerid.replace("@", "_"));// email
						}
						out.println("Split_Length : " + split.length);
						if (!(subscribernode.hasNode(split[i].replace("@", "_")))) {
							out.println("Email Node True or False :  : : "
									+ !(subscribernode.hasNode(split[i].replace("@", "_"))));
							emailnode = subscribernode.addNode(split[i].replace("@", "_"));
							emailnode.setProperty("Campaign_Id", campaignid);
							campaignemailnode = emailnode.addNode("CampaignId");
							campaignidnode = campaignemailnode.addNode(campaignid);
							campaignidnode.setProperty("Status", statussplit);
							out.println("Email after Spliting: : : " + split[i].replace("@", "_"));
						}

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
