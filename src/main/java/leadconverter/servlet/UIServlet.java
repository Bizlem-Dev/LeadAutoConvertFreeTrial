package leadconverter.servlet;

import static com.mongodb.client.model.Filters.eq;

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
import org.bson.conversions.Bson;
import org.jsoup.Jsoup;
import org.osgi.service.http.HttpService;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;

import leadconverter.freetrail.FreetrialShoppingCartUpdate;
import leadconverter.freetrail.GetValidityInfoFromEmail;
import leadconverter.mongo.ConnectionHelper;
import leadconverter.mongo.FunnelDetailsMongoDAO;
import leadconverter.mongo.MongoDAO;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/ui" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class UIServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = null;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		// http://development.bizlem.io:8082/portal/servlet/service/ui.shopping45?email=viki@gmail.com&group=G1&SubscriberCount=10
		Session session = null;
		if (request.getRequestPathInfo().getExtension().equals("shopping45")) {
			try {
				try {
					session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				} catch (LoginException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String remoteuser=request.getParameter("email");
				String group=request.getParameter("group");
			String expstatus = new FreetrialShoppingCartUpdate().checkFreeTrialExpirationStatus(remoteuser);
			
				out.println("group serv= "+group +" ::remoteuser"+remoteuser);
				out.println(" ::expstatus= "+expstatus);
				remoteuser=	remoteuser.replace("@", "_");
			Node	shoppingnode = new FreetrialShoppingCartUpdate().getLeadAutoConverterNode(expstatus, remoteuser, group,
						session, response);
			out.println(" ::shoppingnode= "+shoppingnode);
	String userName = request.getParameter("userName").replace("@", "_");
				
				try{
					JSONArray funnel_json_obj=FunnelDetailsMongoDAO.getFunnelList(userName);
					out.println(funnel_json_obj);
				}catch (Exception e) {
					// TODO: handle exception
					out.println("exc"+e);
				}
				
				out.println("GET UI");

				
				out.println("GET LACcartService");
				String jsresp=null;
				String email = request.getParameter("email").replace("@", "_");

				
				try {
					jsresp = new GetValidityInfoFromEmail().getValidityInfo(email, session, response);
					out.println(jsresp);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				JSONObject validjs=new  JSONObject(jsresp);
				if(validjs.has("status")&& validjs.get("status").equals("true")) {
					String leadnode=validjs.getString("Node");
					Node userNode = session.getRootNode().getNode(leadnode);
					
					out.println("userNode ;; "+userNode);
					
				}
				FreetrialShoppingCartUpdate fl = new FreetrialShoppingCartUpdate();
				String exp = fl.checkFreeTrialExpirationStatus(email);
//				String group = request.getParameter("group");
				out.println("GET  fl.checkfreetrial(email= " + exp);
//			        Node validshoppingnode=fl.validationmethod(exp, email, group, session, response);
//			        out.println("GET  valid --------- "+validshoppingnode);
				Node shoppingnode1 = fl.getLeadAutoConverterNode(exp, email, group, session, response);
				out.println("fl node= " + shoppingnode1);
				int subscount = Integer.parseInt(request.getParameter("SubscriberCount"));

				if (shoppingnode1 != null) {
					String respupdate = new FreetrialShoppingCartUpdate().updateSubscriberCounter(email, exp,
							shoppingnode1, session, response, subscount);
					out.println("fl respupdate " + respupdate);
				}

			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}

		/*
		 * if(request.getRequestPathInfo().getExtension().equals("fconfiguration")) {
		 * try {
		 * request.getRequestDispatcher("/content/mainui/.fconfiguration").forward(
		 * request, response); } catch (Exception ex) { out.print(ex.getMessage()); }
		 * }else if(request.getRequestPathInfo().getExtension().equals("funnel")) { try
		 * { request.getRequestDispatcher("/content/mainui/.ffunnel").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("index")) { try {
		 * request.getRequestDispatcher("/content/mainui/.findex").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("indexData")) { try {
		 * request.getRequestDispatcher("/content/mainui/.index").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("list")) { try {
		 * request.getRequestDispatcher("/content/mainui/.flist").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("subscriberslist")) {
		 * try {
		 * request.getRequestDispatcher("/content/mainui/.subscriberslist").forward(
		 * request, response); } catch (Exception ex) { out.print(ex.getMessage()); }
		 * }else
		 * if(request.getRequestPathInfo().getExtension().equals("set-up-campaign")) {
		 * try {
		 * request.getRequestDispatcher("/content/mainui/.fset-up-campaign").forward(
		 * request, response); } catch (Exception ex) { out.print(ex.getMessage()); }
		 * }else if(request.getRequestPathInfo().getExtension().equals("statistic")) {
		 * try {
		 * request.getRequestDispatcher("/content/mainui/.fstatistic").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("statistic2")) { try {
		 * request.getRequestDispatcher("/content/mainui/.fstatistic2").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("statistic3")) { try {
		 * request.getRequestDispatcher("/content/mainui/.fstatistic3").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("statistic4")) { try {
		 * request.getRequestDispatcher("/content/mainui/.fstatistic4").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else{
		 * out.print("Rrquested extension is not an ESP resource"); }
		 */

		if (request.getRequestPathInfo().getExtension().equals("verify")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fverify").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("texts-only-template")) {
			try {
				request.getRequestDispatcher("/content/mainui/.ftexts-only-template").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("text-photos-template")) {
			try {
				request.getRequestDispatcher("/content/mainui/.ftext-photos-template").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("strat-from-scratch")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fstrat-from-scratch").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("smtp-setup")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fsmtp-setup").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("simpleLayout")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fsimpleLayout").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("set-up-campaign")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fset-up-campaign").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("set-up-campaign-complete")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fset-up-campaign-complete").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("select-tamplate")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fselect-tamplate").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("select-tamplate-file")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fselect-tamplate-file").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("news-letter-tamplate")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fnews-letter-tamplate").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("list")) {
			try {
				request.getRequestDispatcher("/content/mainui/.flist").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("index")) {
			try {
			//	request.getRequestDispatcher("/content/mainui/.findex").forward(request, response);
				 request.getRequestDispatcher("/content/static/.Home").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("gallery-template")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fgallery-template").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("funnel")) {
			try {
				request.getRequestDispatcher("/content/mainui/.ffunnel").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("configuration")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fconfiguration").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("googleanalytics")) {
			try {
				request.getRequestDispatcher("/content/mainui/.googleAnalytics").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}

		else if (request.getRequestPathInfo().getExtension().equals("loginuser")) {
			try {
				request.getRequestDispatcher("/content/mainui/.loginuser").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("getloginuser")) {
			try {
				request.getRequestDispatcher("/content/mainui/.getloginuser").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("set-up-rule")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fset-up-rule").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("test-rule-engine")) {
			try {
				request.getRequestDispatcher("/content/mainui/.ftest-rule-engine").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("free-trail-expire")) {
			try {
				request.getRequestDispatcher("/content/mainui/.ffree-trail-expire").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("get_subscriber_status")) {
			// out.println("in callservice");
			String logged_in_user_email = request.getParameter("rm_email");
			// FreeTrialandCart cart = new FreeTrialandCart();
			// String freetrialstatus = cart.checkfreetrial(logged_in_user_email);
			// mailtangynode = cart.getMailtangyNode(freetrialstatus, logged_in_user_email,
			// "", session, response);

			MongoDAO mdao = new MongoDAO();
			long subscribers_count = mdao.getSubscriberCountForLoggedInUserForFreeTrail("subscribers_details",
					logged_in_user_email);
			String free_trail_status = new FreetrialShoppingCartUpdate().checkfreetrial(logged_in_user_email);
			// long subscribers_count=2000;
			// String free_trail_status="0";
			// out.println("logged_in_user_email : "+logged_in_user_email);
			// out.println("subscribers_count : "+subscribers_count);
			// out.println("free_trail_status : "+free_trail_status);
			if (subscribers_count <= 2000 && free_trail_status.equals("0")) {
				System.out.println("Free Train is Active");
				out.println("Free Train is Active");
			} else if (free_trail_status.equals("1")) {
				System.out.println("Free Train Date Expired");
				out.println("Free Train Date Expired");
			} else if (subscribers_count > 2000) {
				System.out.println("Subscriber Count is More");
				out.println("Subscriber Count is More");
			}

		} else if (request.getRequestPathInfo().getExtension().equals("grouplist")) {
			try {
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

				String email = request.getParameter("email").replace("@", "_");
				JSONObject grjsa = new FreetrialShoppingCartUpdate().getLeadAutoConverterGroupList(email, session,
						response);
				out.println(grjsa);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("checkValidUser")) {

			try {
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				String email = request.getParameter("email").replace("@", "_");
				String jsresp;
				try {
					jsresp = new GetValidityInfoFromEmail().getValidityInfo(email, session, response);
					out.println(jsresp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			} catch (LoginException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else if (request.getRequestPathInfo().getExtension().equals("testlac1")) {
			try {
//				request.getRequestDispatcher("/content/static/.LeadAutoConvert").forward(request, response);
				RequestDispatcher dis = request.getRequestDispatcher("/content/static/.Home");
				dis.forward(request, response);
				
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}else if (request.getRequestPathInfo().getExtension().equals("LAC")) {
			try {
				//out.println("get");
//				request.getRequestDispatcher("/content/static/.LeadAutoConvert").forward(request, response);
				RequestDispatcher dis = request.getRequestDispatcher("/content/static/.LACMAIN");
				dis.forward(request, response);
				
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}  
		 else {
			out.print("Rrquested extension is not an ESP resource");
			String remoteuser = request.getRemoteUser();
			out.print("Logged In User Is (remoteuser): " + remoteuser);
		}

		/*
		 * 
		 * if(request.getRequestPathInfo().getExtension().equals("configuration")) { try
		 * { request.getRequestDispatcher("/content/mainui/.configuration").forward(
		 * request, response); } catch (Exception ex) { out.print(ex.getMessage()); }
		 * }else if(request.getRequestPathInfo().getExtension().equals("funnel")) { try
		 * { request.getRequestDispatcher("/content/mainui/.funnel").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("index")) { try {
		 * request.getRequestDispatcher("/content/mainui/.index").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("indexData")) { try {
		 * request.getRequestDispatcher("/content/mainui/.index").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("list")) { try {
		 * request.getRequestDispatcher("/content/mainui/.list").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("subscriberslist")) {
		 * try {
		 * request.getRequestDispatcher("/content/mainui/.subscriberslist").forward(
		 * request, response); } catch (Exception ex) { out.print(ex.getMessage()); }
		 * }else
		 * if(request.getRequestPathInfo().getExtension().equals("set-up-campaign")) {
		 * try {
		 * request.getRequestDispatcher("/content/mainui/.fset-up-campaign").forward(
		 * request, response); } catch (Exception ex) { out.print(ex.getMessage()); }
		 * }else if(request.getRequestPathInfo().getExtension().equals("statistic")) {
		 * try {
		 * request.getRequestDispatcher("/content/mainui/.statistic").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("statistic2")) { try {
		 * request.getRequestDispatcher("/content/mainui/.statistic2").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("statistic3")) { try {
		 * request.getRequestDispatcher("/content/mainui/.statistic3").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else
		 * if(request.getRequestPathInfo().getExtension().equals("statistic4")) { try {
		 * request.getRequestDispatcher("/content/mainui/.statistic4").forward(request,
		 * response); } catch (Exception ex) { out.print(ex.getMessage()); } }else{
		 * out.print("Rrquested extension is not an ESP resource"); }
		 */

	}
}
