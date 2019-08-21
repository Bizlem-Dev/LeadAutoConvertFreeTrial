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
import leadconverter.freetrail.FreetrialShoppingCartUpdate;
import leadconverter.impl.Searching_list_DaoImpl;
import leadconverter.mongo.SubscriberMongoDAO;

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
@Properties({
		@Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/createListAndSubscribers" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts",
				"cat", "latestproducts", "brief", "prodlist", "catalog",
				"viewcart", "productslist", "addcart", "createproduct",
				"checkmodelno", "productEdit" }) })
@SuppressWarnings("serial")
public class UICreateListAndLeadServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = new JSONObject();

	@Override
	protected void doPost(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		PrintWriter out = response.getWriter();
		JSONArray mainarray = new JSONArray();
		JSONObject jsonobject = new JSONObject();
		String listid = null;

		String remoteuser = request.getRemoteUser();

		try {
			Session session = null;

			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			Node content = session.getRootNode().getNode("content");
            if (request.getRequestPathInfo().getExtension().equals("createlistPlusLeadNew")) {
				
				
				try {
					JSONArray myJsonData = new JSONArray(request.getParameter("jsonArr").toString());
			    	JSONObject subscriber_json_obj =null;
			    	JSONObject subscriber_tmp_json_obj =null;
			    	JSONArray subscribers_json_arr= new JSONArray();
		            for (int i=0; i < myJsonData.length(); i++) {
		            	    subscriber_tmp_json_obj =(JSONObject) myJsonData.get(i);
			    			subscriber_json_obj = new JSONObject();
			    			subscriber_json_obj.put("Name",
			    					subscriber_tmp_json_obj.getString("FirstName").replace(" ", ""));
			    			subscriber_json_obj.put("email",
									subscriber_tmp_json_obj.getString("EmailAddress").replace(" ", ""));
			    			subscriber_json_obj.put("confirmed", 1);
			    			subscriber_json_obj.put("htmlemail", 1);
			    			subscriber_json_obj.put("password", 0);
			    			subscriber_json_obj.put("disabled", 0);
			    			subscriber_json_obj.put("foreignkey", "");
			    			subscriber_json_obj.put("subscribepage", 0);
							subscribers_json_arr.put(subscriber_json_obj);
			    	}
		            
		            //out.println("subscribers_json_arr Size :  "+subscribers_json_arr.length());
		            //out.println(subscribers_json_arr);
		            
					String listname = request.getParameter("listname");
					//out.println("ListName : : " + listname);
					//String listurl = content.getNode("ip")
					//		.getProperty("List_Add_Url").getString();
					String listurl = ResourceBundle.getBundle("config").getString("List_Add_Url");

					String listparameter = "?name=" + listname
							+ "&description=This Belongs to " + "&listorder="
							+ 90 + "&active=" + 1;
					String listresponse = this.sendpostdata(listurl,
							listparameter.replace(" ", "%20"), response)
							.replace("<pre>", "");
					//out.println("List Response " + listresponse);
					JSONObject listjson = new JSONObject(listresponse);
					String liststatusresponse = listjson.getString("status");
					// out.println("List Status Response : " +
					// liststatusresponse);
					JSONObject getjsonid = listjson.getJSONObject("data");
					listid = getjsonid.getString("id");
					// out.println("List id : " + listid);
					if (liststatusresponse.equals(("success"))) {
						//out.println("list Success");
						   
						// This Data will go to Php List and Save in Sling
						String urlParameters = "?subscribers="
								+ subscribers_json_arr.toString();
						//String slingurl = content.getNode("ip")
						//		.getProperty("Sling_Url").getString();
						String slingurl = ResourceBundle.getBundle("config").getString("Sling_Url");
						//String phpurl = content.getNode("ip")
						//		.getProperty("Phplist_Url").getString();
						String phpurl = ResourceBundle.getBundle("config").getString("Phplist_Url");
                        String postresponse = this.sendpostdata(phpurl,
								urlParameters.replace(" ", "%20"), response)
								.replace("<pre>", "");
						// out.println("Subscriber_Response : : " +
						// postresponse);
						JSONObject bufferjson = new JSONObject(postresponse);
						String statusresponse = bufferjson.getString("status");
						String integrationresponse = null;
						JSONArray subscriberdata = bufferjson
								.getJSONArray("data");
						for (int subscriberidloop = 0; subscriberidloop < subscriberdata
								.length(); subscriberidloop++) {

							JSONObject data = subscriberdata
									.getJSONObject(subscriberidloop);
							String subscriberid = data.getString("id");
							if (statusresponse.equals(("success"))) {

								//out.println("Integration Success");

								//String integrationurl = content.getNode("ip")
								//		.getProperty("Integration_Url")
								//		.getString();
								String integrationurl = ResourceBundle.getBundle("config").getString("Add_Subscriber_In_List");
								String integrationparameter = "?list_id="
										+ listid + "&subscriber_id="
										+ subscriberid;
								integrationresponse = this.sendpostdata(
										integrationurl,
										integrationparameter
												.replace(" ", "%20"), response)
										.replace("<pre>", "");
							} else {

								out.println("Does Not Add in Phplist");
							}

						}
						// out.println("integrationresponse : "+integrationresponse);
						JSONObject integrationjson = new JSONObject(
								integrationresponse);
						String integrationstatus = integrationjson
								.getString("status");
						JSONObject totaldatajson = new JSONObject();

						totaldatajson.put("Subscribers", postresponse);
						totaldatajson.put("List", integrationjson);

						totaldatajson.put("remote_user",
								request.getRemoteUser());
					//  Raw json-->myJsonData subscriber json-->subscribers_json_arr 
						if (integrationstatus.equals(("success"))) {

							// out.println("Total Data "+totaldatajson);
							// commented by Akhilesh
							// String totaldata = "?totalresult=" +
							// totaldatajson;
							String totaldata = totaldatajson.toString();

							/*
							 * commented by akhilesh String slingresponse =
							 * this.sendpostdata(slingurl,
							 * totaldata.replace(" ", "%20"), response);
							 */
							String slingresponse = this
									.sendpostdataToCreateList(slingurl,
											totaldata.replace(" ", "%20"),
											response);

							//out.println("Sling Response : " + slingresponse);
							JSONObject res_json_obj=new JSONObject();
							           res_json_obj.put("listid", listid);
							out.println(res_json_obj.toString());
							
							// commented by akhilesh
							//response.sendRedirect("http://35.221.160.146:8082/portal//servlet/service/CallingEsp.leadconverternew2?list_id="
								//	+ listid);

						}
						session.save();

					} else {

						out.println("Does Not Add in Phplist");
					}
				} catch (Exception ex) {
					out.println("Exception ex : " + ex.getMessage());
				}

			} else if (request.getRequestPathInfo().getExtension().equals("createlistPlusLead")) {
				
				
				try {
					JSONArray myJsonData = new JSONArray(request.getParameter("jsonArr").toString());
					//out.println("myJsonData : "+myJsonData);
			    	JSONObject subscriber_json_obj =null;
			    	JSONObject subscriber_tmp_json_obj =null;
			    	JSONArray subscribers_json_arr= new JSONArray();
			    	JSONObject subscriber_details =null;
			    	//out.println("createlistPlusLead 1");
		            for (int i=0; i < myJsonData.length(); i++) {
		            	    subscriber_tmp_json_obj =(JSONObject) myJsonData.get(i);
			    			subscriber_json_obj = new JSONObject();
			    			
			    			subscriber_json_obj.put("Name",
			    					subscriber_tmp_json_obj.getString("FirstName").replace(" ", ""));
			    			subscriber_json_obj.put("email",
									subscriber_tmp_json_obj.getString("EmailAddress").replace(" ", ""));
			    			subscriber_json_obj.put("confirmed", 1);
			    			subscriber_json_obj.put("htmlemail", 1);
			    			subscriber_json_obj.put("password", 0);
			    			subscriber_json_obj.put("disabled", 0);
			    			subscriber_json_obj.put("foreignkey", "");
			    			subscriber_json_obj.put("subscribepage", 0);
							subscribers_json_arr.put(subscriber_json_obj);
			    	}
		            //out.println("createlistPlusLead 2");
		            //out.println("subscribers_json_arr Size :  "+subscribers_json_arr.length());
		            //out.println(subscribers_json_arr);
		            
		            String created_by = request.getParameter("remoteuser");
		            
		        	String group="";
					group=request.getParameter("group");
//					String subscount=request.getParameter("SubscriberCount");
					int subscount=Integer.parseInt(request.getParameter("SubscriberCount"));
					Node shoppingnode=null;
					//shopping cart method call
					String expstatus= new FreetrialShoppingCartUpdate().checkFreeTrialExpirationStatus(remoteuser);
				 	
			 		shoppingnode=new FreetrialShoppingCartUpdate().getLeadAutoConverterNode(expstatus, remoteuser.replace("@", "_"), group, session, response);
			 		if(shoppingnode!=null) {
			 		Node groupnode=	shoppingnode.getParent();
			 		Node servicenode=	groupnode.getParent();
			 		if(servicenode.hasProperty("quantity")) {
			 			int quantity=Integer.parseInt(servicenode.getProperty("quantity").getString());
			 			if(quantity>=subscount) {
			 				
//			 			}
//			 		}
			 		String respupdate=	new FreetrialShoppingCartUpdate().updateSubscriberCounter(remoteuser, expstatus, shoppingnode, session, response, subscount);
			 			
			 		
		            
			    	String funnelName = request.getParameter("funnelName");
			    	String fromName = request.getParameter("fromName");
			    	String fromEmailAddress = request.getParameter("fromEmailAddress");
		            
					String listname = request.getParameter("listname");
					
					//out.println("ListName : : " + listname);
					//String listurl = content.getNode("ip")
					//		.getProperty("List_Add_Url").getString();
					String listurl = ResourceBundle.getBundle("config").getString("List_Add_Url");

					String listparameter = "?name=" + listname
							+ "&description=This Belongs to " + "&listorder="
							+ 90 + "&active=" + 1;
					String listresponse = this.sendpostdata(listurl,
							listparameter.replace(" ", "%20"), response)
							.replace("<pre>", "");
					//out.println("List Response " + listresponse);
					JSONObject listjson = new JSONObject(listresponse);
					String liststatusresponse = listjson.getString("status");
					// out.println("List Status Response : " +
					// liststatusresponse);
					JSONObject getjsonid = listjson.getJSONObject("data");
					listid = getjsonid.getString("id");
					// out.println("List id : " + listid);
					//out.println("createlistPlusLead 3");
					if (liststatusresponse.equals(("success"))) {
						//out.println("list Success");
						   
						// This Data will go to Php List and Save in Sling
						String urlParameters = "?subscribers="
								+ subscribers_json_arr.toString();
						//String slingurl = content.getNode("ip")
						//		.getProperty("Sling_Url").getString();
						String slingurl = ResourceBundle.getBundle("config").getString("Sling_Url");
						//String phpurl = content.getNode("ip")
						//		.getProperty("Phplist_Url").getString();
						String phpurl = ResourceBundle.getBundle("config").getString("Phplist_Url");
                        //out.println("createlistPlusLead 3.001");
						String postresponse = this.sendpostdata(phpurl,
								urlParameters.replace(" ", "%20"), response)
								.replace("<pre>", "");
						//out.println("createlistPlusLead 3.002");
						 //out.println("Subscriber_Response : : " + postresponse);
						
						JSONObject bufferjson = new JSONObject(postresponse);
						//out.println("createlistPlusLead 3.0021");
						String statusresponse = bufferjson.getString("status");
						//out.println("createlistPlusLead 3.0022");
						String integrationresponse = null;
						JSONArray subscriberdata = bufferjson
								.getJSONArray("data");
						//out.println("createlistPlusLead 3.003");
						for (int subscriberidloop = 0; subscriberidloop < subscriberdata
								.length(); subscriberidloop++) {

							JSONObject data = subscriberdata
									.getJSONObject(subscriberidloop);
							String subscriberid = data.getString("id");
							//out.println("createlistPlusLead 3.004");
							if (statusresponse.equals(("success"))) {

								//out.println("Integration Success");

								//String integrationurl = content.getNode("ip")
								//		.getProperty("Integration_Url")
								//		.getString();
								String integrationurl = ResourceBundle.getBundle("config").getString("Add_Subscriber_In_List");
								String integrationparameter = "?list_id="
										+ listid + "&subscriber_id="
										+ subscriberid;
								integrationresponse = this.sendpostdata(
										integrationurl,
										integrationparameter
												.replace(" ", "%20"), response)
										.replace("<pre>", "");
							} else {

								out.println("Does Not Add in Phplist");
							}

						}
						// out.println("integrationresponse : "+integrationresponse);
						JSONObject integrationjson = new JSONObject(
								integrationresponse);
						String integrationstatus = integrationjson
								.getString("status");
						JSONObject totaldatajson = new JSONObject();

						totaldatajson.put("Subscribers", postresponse);
						//out.println("createlistPlusLead 4");
						totaldatajson.put("SubscribersWithPropert", request.getParameter("jsonArr").toString());
						totaldatajson.put("List", integrationjson);

						totaldatajson.put("remote_user",
								request.getRemoteUser());
						//Saving Subscribers Details in MongoDB Collections
						subscriber_details=new JSONObject();
						subscriber_details.put("subscribers_details_json_arr", myJsonData);
						subscriber_details.put("subscribers_id_json_arr", subscriberdata);
						subscriber_details.put("created_by", created_by);
						subscriber_details.put("funnelName", funnelName);
						subscriber_details.put("fromName", fromName);
						subscriber_details.put("fromEmailAddress", fromEmailAddress);
						subscriber_details.put("listname", listname);
						subscriber_details.put("listid", listid);
						subscriber_details.put("remote_user", request.getRemoteUser());
						SubscriberMongoDAO.addSubscribersDetails(subscriber_details);
						//out.println("createlistPlusLead 5");
					//  Raw json-->myJsonData subscriber json-->subscribers_json_arr 
						if (integrationstatus.equals(("success"))) {

							// out.println("Total Data "+totaldatajson);
							// commented by Akhilesh
							// String totaldata = "?totalresult=" +
							// totaldatajson;
							String totaldata = totaldatajson.toString();

							/*
							 * commented by akhilesh String slingresponse =
							 * this.sendpostdata(slingurl,
							 * totaldata.replace(" ", "%20"), response);
							 */
							//out.println("createlistPlusLead 6");
							String slingresponse = this
									.sendpostdataToCreateList(slingurl,
											totaldata.replace(" ", "%20"),
											response);
							//out.println("createlistPlusLead 7");

							//out.println("Sling Response : " + slingresponse);
							JSONObject res_json_obj=new JSONObject();
							           res_json_obj.put("listid", listid);
							out.println(res_json_obj.toString());
							
							// commented by akhilesh
							//response.sendRedirect("http://35.221.160.146:8082/portal//servlet/service/CallingEsp.leadconverternew2?list_id="
								//	+ listid);

						}
						session.save();

					} else {

						out.println("Does Not Add in Phplist");
					}
					
			 		}else{
			 			JSONObject res_json_obj=new JSONObject();
				           res_json_obj.put("quantity", quantity);
				           out.println(res_json_obj.toString());
//			 			out.println("You can not upload leads more than "+quantity);
			 		}}
			 		}else{
			 			out.println("User is not Valid");
			 		}
				} catch (Exception ex) {
					out.println("Exception ex : " + ex);
				}

			} else if (request.getRequestPathInfo().getExtension().equals("Single")) {
				String firstname = request.getParameter("fname");
				String lastname = request.getParameter("lname");
				String email = request.getParameter("email");
				jsonobject.put("First_Name", firstname);
				jsonobject.put("Last_Name", lastname);
				jsonobject.put("Email_Id", email);
				mainarray.put(jsonobject);
				String urlParameters = mainarray.toString();

				//String slingurl = content.getNode("ip")
				//		.getProperty("Sling_Url").getString();
				String slingurl = ResourceBundle.getBundle("config").getString("Sling_Url");
				//String phpurl = content.getNode("ip")
				//		.getProperty("Phplist_Url").getString();
				String phpurl = ResourceBundle.getBundle("config").getString("Phplist_Url");
				String postresponse = this.sendpostdata(phpurl, urlParameters,
						response).replace("<pre>", "");
				JSONObject bufferjson = new JSONObject(postresponse);
				String statusresponse = bufferjson.getString("status");

				if (statusresponse.equals(("success"))) {
					String slingresponse = this.sendpostdata(slingurl,
							urlParameters, response);
					out.println("sling_Url" + slingresponse);

				} else {

					out.println("Does Not Add in Phplist");
				}

			}else if (request.getRequestPathInfo().getExtension().equals("mergeList")) {
		
			try {
				
				JSONArray myJsonData = new JSONArray(request.getParameter("jsonArr").toString());
		    	JSONObject subscriber_json_obj =null;
		    	JSONObject subscriber_tmp_json_obj =null;
		    	JSONArray subscribers_json_arr= new JSONArray();
	            for (int i=0; i < myJsonData.length(); i++) {
	            	    subscriber_tmp_json_obj =(JSONObject) myJsonData.get(i);
		    			subscriber_json_obj = new JSONObject();
		    			subscriber_json_obj.put("Name",
		    					subscriber_tmp_json_obj.getString("FirstName").replace(" ", ""));
		    			subscriber_json_obj.put("email",
								subscriber_tmp_json_obj.getString("EmailAddress").replace(" ", ""));
		    			subscriber_json_obj.put("confirmed", 1);
		    			subscriber_json_obj.put("htmlemail", 1);
		    			subscriber_json_obj.put("password", 0);
		    			subscriber_json_obj.put("disabled", 0);
		    			subscriber_json_obj.put("foreignkey", "");
		    			subscriber_json_obj.put("subscribepage", 0);
						subscribers_json_arr.put(subscriber_json_obj);
		    	}
	            //out.println("subscribers_json_arr Size :  "+subscribers_json_arr.length());
	            out.println(subscribers_json_arr);
	            
				String listname = request.getParameter("listname");
				listid=request.getParameter("list_id");
				//out.println("ListName : : " + listname);
				/*
				//String listurl = content.getNode("ip")
				//		.getProperty("List_Add_Url").getString();
				String listurl = ResourceBundle.getBundle("config").getString("List_Add_Url");

				String listparameter = "?name=" + listname
						+ "&description=This Belongs to " + "&listorder="
						+ 90 + "&active=" + 1;
				String listresponse = this.sendpostdata(listurl,
						listparameter.replace(" ", "%20"), response)
						.replace("<pre>", "");
				//out.println("List Response " + listresponse);
				JSONObject listjson = new JSONObject(listresponse);
				String liststatusresponse = listjson.getString("status");
				// out.println("List Status Response : " +
				// liststatusresponse);
				JSONObject getjsonid = listjson.getJSONObject("data");
				listid = getjsonid.getString("id");
				*/
				// out.println("List id : " + listid);
					//out.println("list Success");
					   
					// This Data will go to Php List and Save in Sling
					String urlParameters = "?subscribers="
							+ subscribers_json_arr.toString();
					//String slingurl = content.getNode("ip")
					//		.getProperty("Sling_Url").getString();
					String slingurl = ResourceBundle.getBundle("config").getString("Sling_Url");
					//String phpurl = content.getNode("ip")
					//		.getProperty("Phplist_Url").getString();
					String phpurl = ResourceBundle.getBundle("config").getString("Phplist_Url");
					String postresponse = this.sendpostdata(phpurl,
							urlParameters.replace(" ", "%20"), response)
							.replace("<pre>", "");
					// out.println("Subscriber_Response : : " +
					// postresponse);
					JSONObject bufferjson = new JSONObject(postresponse);
					String statusresponse = bufferjson.getString("status");
					String integrationresponse = null;
					JSONArray subscriberdata = bufferjson
							.getJSONArray("data");
					for (int subscriberidloop = 0; subscriberidloop < subscriberdata
							.length(); subscriberidloop++) {

						JSONObject data = subscriberdata
								.getJSONObject(subscriberidloop);
						String subscriberid = data.getString("id");
						if (statusresponse.equals(("success"))) {

							//out.println("Integration Success");

							//String integrationurl = content.getNode("ip")
							//		.getProperty("Integration_Url")
							//		.getString();
							String integrationurl = ResourceBundle.getBundle("config").getString("Add_Subscriber_In_List");
							String integrationparameter = "?list_id="
									+ listid + "&subscriber_id="
									+ subscriberid;
							integrationresponse = this.sendpostdata(
									integrationurl,
									integrationparameter
											.replace(" ", "%20"), response)
									.replace("<pre>", "");
						} else {

							out.println("Does Not Add in Phplist");
						}

					}
					// out.println("integrationresponse : "+integrationresponse);
					JSONObject integrationjson = new JSONObject(
							integrationresponse);
					String integrationstatus = integrationjson
							.getString("status");
					JSONObject totaldatajson = new JSONObject();

					totaldatajson.put("Subscribers", postresponse);
					totaldatajson.put("List", integrationjson);

					totaldatajson.put("remote_user",
							request.getRemoteUser());

					if (integrationstatus.equals(("success"))) {

						// out.println("Total Data "+totaldatajson);
						// commented by Akhilesh
						// String totaldata = "?totalresult=" +
						// totaldatajson;
						String totaldata = totaldatajson.toString();

						/*
						 * commented by akhilesh String slingresponse =
						 * this.sendpostdata(slingurl,
						 * totaldata.replace(" ", "%20"), response);
						 */
						String slingresponse = this
								.sendpostdataToCreateList(slingurl,
										totaldata.replace(" ", "%20"),
										response);

						//out.println("Sling Response : " + slingresponse);
						JSONObject res_json_obj=new JSONObject();
						           res_json_obj.put("listid", listid);
						out.println(res_json_obj.toString());
						
						// commented by akhilesh
						//response.sendRedirect("http://35.221.160.146:8082/portal//servlet/service/CallingEsp.leadconverternew2?list_id="
							//	+ listid);

					}
					session.save();

				
			} catch (Exception ex) {
				out.println("Exception ex : " + ex.getMessage());
			}

		}

		} catch (Exception e) {

			out.println("Exception ex : : : " + e.getStackTrace());
		}

	}

	public String sendpostdata(String callurl, String urlParameters,
			SlingHttpServletResponse response) throws ServletException,
			IOException {

		PrintWriter out = response.getWriter();
		//out.println("urlParameters :" + urlParameters);
		URL url = new URL(callurl + urlParameters.replace("\\", ""));
		//out.println("Url :" + url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		//
		OutputStream writer = conn.getOutputStream();

		writer.write(urlParameters.getBytes());
		// out.println("Writer Url : "+writer);
		int responseCode = conn.getResponseCode();
		//out.println("POST Response Code :: " + responseCode);
		StringBuffer buffer = new StringBuffer();
		//
		if (responseCode == 200) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			//
			// out.println(buffer.toString());
		} else {
			out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}

	public String sendpostdataToCreateList(String callurl,
			String urlParameters, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		//out.println("urlParameters :" + urlParameters);
		// URL url = new URL(callurl + urlParameters.replace("\\", ""));
		URL url = new URL(callurl);
		//out.println("Url :" + url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		//
		OutputStream writer = conn.getOutputStream();

		writer.write(urlParameters.getBytes());
		// out.println("Writer Url : "+writer);
		int responseCode = conn.getResponseCode();
		//out.println("POST Response Code :: " + responseCode);
		StringBuffer buffer = new StringBuffer();
		//
		if (responseCode == 200) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			//
			// out.println(buffer.toString());
		} else {
			out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}
}
