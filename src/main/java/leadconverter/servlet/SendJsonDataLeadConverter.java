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
import leadconverter.impl.Searching_list_DaoImpl;

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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/SendJsonDataLeadConverter" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class SendJsonDataLeadConverter extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = new JSONObject();	

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONArray mainarray = new JSONArray();
		JSONObject jsonobject = new JSONObject();
		String listid = null;

		String remoteuser = request.getRemoteUser();

		try {
			Session session = null;

			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			Node content = session.getRootNode().getNode("content");
			if (request.getParameterMap().get("file") != null) {
				RequestParameter[] ap = request.getRequestParameterMap().get("file");
				// out.println("ap.length " + ap.length);

				for (int i = 0; i < ap.length; i++) {
					if (ap[i].getFileName() != null && ap[i].getFileName().trim() != "") {

						String filename = ap[i].getFileName();

						fileType = "";
						if (ap[i] != null &&  ap[i].getSize() != 0) {
							for (int j = 0; j < FILEEXTENSION.length; j++) {
								if (filename.indexOf(FILEEXTENSION[j]) != -1) {
									fileType = FILEEXTENSION[j];
								}
								out.println("4 " + fileType);
							}

							InputStream stream = ap[i].getInputStream();

							if (request.getRequestPathInfo().getExtension().equals("Email")) {
								//
try {
								String listname = request.getParameter("listname");
								out.println("ListName : : " + listname);
								BufferedReader reader = null;
								String line = "";
								String cvsSplitBy = ",";
								reader = new BufferedReader(new InputStreamReader(stream));

								String listurl = content.getNode("ip").getProperty("List_Add_Url").getString();

								String listparameter = "?name=" + listname + "&description=This Belongs to "
										+ "&listorder=" + 90 + "&active=" + 1;
								String listresponse = this
										.sendpostdata(listurl, listparameter.replace(" ", "%20"), response)
										.replace("<pre>", "");
								out.println("List Response " + listresponse);
								JSONObject listjson = new JSONObject(listresponse);
								String liststatusresponse = listjson.getString("status");
								// out.println("List Status Response : " + liststatusresponse);
								JSONObject getjsonid = listjson.getJSONObject("data");
								listid = getjsonid.getString("id");
								// out.println("List id : " + listid);
								if (liststatusresponse.equals(("success"))) {
									out.println("list Success");
									while ((line = reader.readLine()) != null) {
										String[] data = line.split(cvsSplitBy);
										jsonobject = new JSONObject();
										//out.println("Email id : " + data[0]);
										out.println("Name : "+data[1]);
										jsonobject.put("Name", data[1].toString().replace(" ", ""));
										jsonobject.put("email", data[0].toString().replace(" ", ""));
										jsonobject.put("confirmed", 1);
										jsonobject.put("htmlemail", 1);
										jsonobject.put("password", 0);
										jsonobject.put("disabled", 0);
										jsonobject.put("foreignkey", "");
										jsonobject.put("subscribepage", 0);
										mainarray.put(jsonobject);
									}

									// This Data will go to Php List and Save in Sling
									String urlParameters = "?subscribers=" + mainarray.toString();
									String slingurl = content.getNode("ip").getProperty("Sling_Url").getString();
									String phpurl = content.getNode("ip").getProperty("Phplist_Url").getString();

									String postresponse = this
											.sendpostdata(phpurl, urlParameters.replace(" ", "%20"), response)
											.replace("<pre>", "");
									// out.println("Subscriber_Response : : " + postresponse);
									JSONObject bufferjson = new JSONObject(postresponse);
									String statusresponse = bufferjson.getString("status");
									String integrationresponse = null;
									JSONArray subscriberdata = bufferjson.getJSONArray("data");
									for (int subscriberidloop = 0; subscriberidloop < subscriberdata
											.length(); subscriberidloop++) {

										JSONObject data = subscriberdata.getJSONObject(subscriberidloop);
										String subscriberid = data.getString("id");
										if (statusresponse.equals(("success"))) {

											out.println("Integration Success");

											String integrationurl = content.getNode("ip").getProperty("Integration_Url")
													.getString();
											String integrationparameter = "?list_id=" + listid + "&subscriber_id="
													+ subscriberid;
											integrationresponse = this
													.sendpostdata(integrationurl,
															integrationparameter.replace(" ", "%20"), response)
													.replace("<pre>", "");
										}
										else {

											out.println("Does Not Add in Phplist");
										}

									}
									// out.println("integrationresponse : "+integrationresponse);
									JSONObject integrationjson = new JSONObject(integrationresponse);
									String integrationstatus = integrationjson.getString("status");
									JSONObject totaldatajson = new JSONObject();

									totaldatajson.put("Subscribers", postresponse);
									totaldatajson.put("List", integrationjson);

									totaldatajson.put("remote_user", request.getRemoteUser());

									if (integrationstatus.equals(("success"))) {

										// out.println("Total Data "+totaldatajson);
										String totaldata = "?totalresult=" + totaldatajson;

										String slingresponse = this.sendpostdata(slingurl,
												totaldata.replace(" ", "%20"), response);

										out.println("Sling Response : " + slingresponse);
										response.sendRedirect(
												"http://35.221.160.146:8082/portal//servlet/service/CallingEsp.leadconverternew2?list_id="
														+ listid);

									}
									session.save();

								} else {

									out.println("Does Not Add in Phplist");
								}
							}
								catch(Exception ex) {
									out.println("Exception ex : "+ex.getMessage());
								}
								
							} 
							else if (request.getRequestPathInfo().getExtension().equals("searchuser")) {

								String list_Name = request.getParameter("listn");
								String list_Id = request.getParameter("listi");
								out.println("remoteuser   " + remoteuser);
								out.println("list_Id   " + list_Id);
								out.println("list_Name  " + list_Name);
								BufferedReader reader = null;
								String line = "";
								String cvsSplitBy = ",";
								reader = new BufferedReader(new InputStreamReader(stream));
								out.println("reader " + reader);
								while ((line = reader.readLine()) != null) {
									String[] data = line.split(cvsSplitBy);

									jsonobject = new JSONObject();
									// out.println("Email id : " + data[0]);
									jsonobject.put("Name", data[1].toString());
									jsonobject.put("email", data[0].toString());
									jsonobject.put("confirmed", 1);
									jsonobject.put("htmlemail", 1);
									jsonobject.put("password", 0);
									jsonobject.put("disabled", 0);
									jsonobject.put("foreignkey", "");
									jsonobject.put("subscribepage", 0);
									jsonobject.put("Remote_User", request.getRemoteUser());
									mainarray.put(jsonobject);

								}

								String urlParameters = "?subscribers=" + mainarray.toString();

								String slingurl = content.getNode("ip").getProperty("Sling_Url").getString();
								String phpurl = content.getNode("ip").getProperty("Phplist_Url").getString();

								String postresponse = this
										.sendpostdata(phpurl, urlParameters.replace(" ", "%20"), response)
										.replace("<pre>", "");
								// out.println("Subscriber_Response : : " + postresponse);
								JSONObject bufferjson = new JSONObject(postresponse);
								String statusresponse = bufferjson.getString("status");
								String integrationresponse = null;
								JSONArray subscriberdata = bufferjson.getJSONArray("data");
								for (int subscriberidloop = 0; subscriberidloop < subscriberdata
										.length(); subscriberidloop++) {
									JSONObject data = subscriberdata.getJSONObject(subscriberidloop);
									String subscriberid = data.getString("id");
									if (statusresponse.equals(("success"))) {

										out.println("Integration Success");
										String integrationurl = content.getNode("ip").getProperty("Integration_Url")
												.getString();
										String integrationparameter = "?list_id=" + list_Id + "&subscriber_id="
												+ subscriberid;
										integrationresponse = this.sendpostdata(integrationurl,
												integrationparameter.replace(" ", "%20"), response)
												.replace("<pre>", "");
									} else {

										out.println("Does Not Add in Phplist");
									}

								}
								out.println("integrationresponse : " + integrationresponse);
								JSONObject integrationjson = new JSONObject(integrationresponse);
								String integrationstatus = integrationjson.getString("status");
								// JSONObject dataintegration=integrationjson.getJSONArray("data");
								// {"status":"error","type":"Error","data":{"code":"23000","message":"SQLSTATE[23000]:
								// Integrity constraint violation: 1062 Duplicate entry '2021-431' for key
								// 'PRIMARY'"}}
								// String codeintegration=dataintegration.getString("code");
								JSONObject totaldatajson = new JSONObject();

								totaldatajson.put("Subscribers", postresponse);
								totaldatajson.put("List", integrationjson);

								totaldatajson.put("remote_user", request.getRemoteUser());

								if (integrationstatus.equals(("success"))) {

									out.println("Total Data " + totaldatajson);
									String totaldata = "?totalresult=" + totaldatajson;

									String slingresponse = this.sendpostdata(slingurl, totaldata.replace(" ", "%20"),
											response);

									out.println("Sling Response : " + slingresponse);
									response.sendRedirect(
											"http://35.221.160.146:8082/portal//servlet/service/CallingEsp.leadconverternew2?list_id="
													+ list_Id);
									session.save();

								}

								else if (integrationstatus.equals("error")) {
									JSONObject dataintegration = integrationjson.getJSONObject("data");
									String code = dataintegration.getString("code");
									if (code.equals("23000")) {
										response.sendRedirect(
												"http://35.221.160.146:8082/portal//servlet/service/CallingEsp.Error");
									}
								} else {

									out.println("Does Not Add in PhpList");
								}

							}
							else if (request.getRequestPathInfo().getExtension().equals("Phone")) {
								BufferedReader reader = null;
								String line = "";
								String cvsSplitBy = ",";
								String firstnamesms = request.getParameter("firstnamesms");
								String lastnamesms = request.getParameter("lastnamesms");
								String mobilenumbersms = request.getParameter("mobilenumbersms");
								reader = new BufferedReader(new InputStreamReader(stream));
								while ((line = reader.readLine()) != null) {
									String[] data = line.split(cvsSplitBy);
									jsonobject = new JSONObject();
									// out.println("Email id : " + data[0]);
									jsonobject.put("Name", data[0].toString());
									jsonobject.put("Phone_Number", data[1].toString());
									jsonobject.put("First_Name", firstnamesms);
									jsonobject.put("Last_Name", lastnamesms);
									jsonobject.put("Mobile_Number", mobilenumbersms);
									mainarray.put(jsonobject);
								}
								mainjsonobject.put("SmsDetails", mainarray);
								String urlParameters = "?smsdetails=" + mainjsonobject;
								String slingurl = content.getNode("ip").getProperty("Sling_Sms_Url").getString();
								String slingresponse = this.sendpostdata(slingurl, urlParameters, response);

								out.println("Sling_Response :: : " + slingresponse);
								// session.save();
							}
													
							
							else if (request.getRequestPathInfo().getExtension().equals("Single")) {

								String firstname = request.getParameter("fname");
								String lastname = request.getParameter("lname");
								String email = request.getParameter("email");
								jsonobject.put("First_Name", firstname);
								jsonobject.put("Last_Name", lastname);
								jsonobject.put("Email_Id", email);
								mainarray.put(jsonobject);
								String urlParameters = mainarray.toString();

								String slingurl = content.getNode("ip").getProperty("Sling_Url").getString();
								String phpurl = content.getNode("ip").getProperty("Phplist_Url").getString();
								String postresponse = this.sendpostdata(phpurl, urlParameters, response)
										.replace("<pre>", "");
								JSONObject bufferjson = new JSONObject(postresponse);
								String statusresponse = bufferjson.getString("status");

								if (statusresponse.equals(("success"))) {
									String slingresponse = this.sendpostdata(slingurl, urlParameters, response);
									out.println("sling_Url" + slingresponse);

								} else {

									out.println("Does Not Add in Phplist");
								}

							}

						}
					}
				}

			}
		} catch (Exception e) {

			out.println("Exception ex : : : " + e.getMessage());
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

		//
		OutputStream writer = conn.getOutputStream();

		writer.write(urlParameters.getBytes());
		// out.println("Writer Url : "+writer);
		int responseCode = conn.getResponseCode();
		out.println("POST Response Code :: " + responseCode);
		StringBuffer buffer = new StringBuffer();
		//
		if (responseCode == 200) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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

	// public String callPostService(String urlStr, String[] paramName, String[]
	// paramValue,
	// SlingHttpServletResponse response) {
	//
	// StringBuilder response1 = new StringBuilder();
	// URL url;
	// try {
	// response.getWriter().println(paramName[0]);
	// response.getWriter().println(paramValue[0]);
	// url = new URL(urlStr);
	// // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setRequestMethod("POST");
	// conn.setDoOutput(true);
	// conn.setDoInput(true);
	// conn.setUseCaches(false);
	// conn.setAllowUserInteraction(false);
	// // conn.setRequestProperty("Content-Type",
	// // "application/x-www-form-urlencoded");
	//
	// conn.setRequestProperty("Content-Type", "application/json");
	// conn.setRequestProperty("Accept", "application/json");
	//
	// // conn.setRequestProperty("Authorization",
	// // "Bearer " + access_token);
	//
	// // Create the form content
	// OutputStream out = conn.getOutputStream();
	// Writer writer = new OutputStreamWriter(out, "UTF-8");
	// for (int i = 0; i < paramName.length; i++) {
	// writer.write(paramName[i]);
	// writer.write("=");
	// writer.write(URLEncoder.encode(paramValue[i], "UTF-8"));
	// writer.write("&");
	// }
	// writer.close();
	// out.close();
	// if (conn.getResponseCode() != 200) {
	// throw new IOException(conn.getResponseMessage());
	// }
	// // Buffer the result into a string
	// BufferedReader rd = new BufferedReader(new
	// InputStreamReader(conn.getInputStream()));
	// response1 = new StringBuilder();
	// String line;
	// while ((line = rd.readLine()) != null) {
	// response1.append(line);
	// }
	// rd.close();
	//
	// conn.disconnect();
	// } catch (Exception e) {
	// response1.append(e.getMessage());
	//
	// }
	// return response1.toString();
	// }

}
