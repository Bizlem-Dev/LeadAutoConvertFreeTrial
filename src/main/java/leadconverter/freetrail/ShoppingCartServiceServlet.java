package leadconverter.freetrail;

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

import leadconverter.mongo.MongoDAO;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/LACcartService" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class ShoppingCartServiceServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = null;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Session session = null;
		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("GET LACcartService");
		FreetrialShoppingCartUpdate fl = new FreetrialShoppingCartUpdate();
		String email = request.getParameter("email").replace("@", "_");
		String exp = fl.checkFreeTrialExpirationStatus(email);
		out.println("GET  fl.checkfreetrial(email= " + exp);

//		out.println("fl "+fl.getDocTigerAdvNode( exp, email,  "",  session,
//						 response));

		/*
		 * {"userid":"user2@gmail.com", "productCode":"doctiger", "serviceId":"6445",
		 * "start_date":"2019-07-01", "end_date":"2019-07-30",
		 * "owner_email":"user2@gmail.com", "quantity":"100",
		 * "fullloaddata":[{"user":"nilesh@gmail.com", "roleid":"10","role":"setup",
		 * "group":"g1"},{"user":"viki@gmail.com", "roleid":"9","role":"user",
		 * "group":"G1"}] }
		 */

	}

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		Session session = null;
		Node content = null;
		Node USERNode = null;
		Node servicenode1 = null;
		Node servicenode2 = null;
		Node AdminusernameNode = null;
		Node productcodenode = null;
		Node serviceidnode1 = null;
		Node serviceidnode2 = null;

		Node subusernode = null;
		Node groupnode = null;
		Node usersnode = null;
		Node subusersnode = null;
		StringBuilder builder = null;
		String username = null;
		String serviceid = null;
		String productcode = null;
		String start_date = null;
		String end_date = null;
		String owner_email = null;
		String quantity = null;
		JSONArray fullloaddata = null;

		BufferedReader bufferedReaderCampaign = null;
		JSONObject fulljsonobject = null;
		try {

			out = response.getWriter();
			out.println("Get Shopping Cart");
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			content = session.getRootNode().getNode("content");
			if (!content.hasNode("user")) {
				USERNode = content.addNode("user");
			} else {
				USERNode = content.getNode("user");
			}
			out.println("USERNode " + USERNode);
			if (!content.hasNode("services")) {
				servicenode2 = content.addNode("services");
			} else {
				servicenode2 = content.getNode("services");
			}
			out.println("servicenode2 " + servicenode2);

			// static nodes
			// end------------------------------------------------------------------------
			builder = new StringBuilder();
			bufferedReaderCampaign = request.getReader();
			String line = null;
			while ((line = bufferedReaderCampaign.readLine()) != null) {
				builder.append(line + "\n");
			}
			fulljsonobject = new JSONObject(builder.toString());
			out.println("fulljsonobject " + fulljsonobject);

			// userId --mailId,serviceId --800,
			username = fulljsonobject.getString("userId").replace("@", "_");
			serviceid = fulljsonobject.getString("serviceId");
			productcode = fulljsonobject.getString("productCode");
			start_date = fulljsonobject.getString("start_date");
			end_date = fulljsonobject.getString("end_date");
			owner_email = fulljsonobject.getString("owner_email");
			quantity = fulljsonobject.getString("quantity");

			fullloaddata = fulljsonobject.getJSONArray("fullloaddata");
			out.println("username " + username);
			out.println("serviceid " + serviceid);
			out.println("start_date " + start_date);
			out.println("end_date " + end_date);
			out.println("owner_email " + owner_email);
			out.println("quantity " + quantity);

			out.println("fullloaddata " + fullloaddata);

			if (!USERNode.hasNode(username)) {
				AdminusernameNode = USERNode.addNode(username);
			} else {
				AdminusernameNode = USERNode.getNode(username);
			}
			out.println("AdminusernameNode " + AdminusernameNode);

			if (!AdminusernameNode.hasNode("services")) {
				servicenode1 = AdminusernameNode.addNode("services");
			} else {
				servicenode1 = AdminusernameNode.getNode("services");
			}
			out.println("servicenode1 " + servicenode1);

			if (!servicenode1.hasNode(productcode)) {
				productcodenode = servicenode1.addNode(productcode);
			} else {
				productcodenode = servicenode1.getNode(productcode);
			}
			out.println("productcodenode " + productcodenode);

			if (!productcodenode.hasNode(serviceid)) {
				serviceidnode1 = productcodenode.addNode(serviceid);
				serviceidnode1.setProperty("producttype", productcode);
				serviceidnode1.setProperty("start_date", start_date);
				serviceidnode1.setProperty("end_date", end_date);
				serviceidnode1.setProperty("owner_email", owner_email);
				serviceidnode1.setProperty("quantity", quantity);

			} else {
				serviceidnode1 = productcodenode.getNode(serviceid);
				serviceidnode1.setProperty("producttype", productcode);
				serviceidnode1.setProperty("start_date", start_date);
				serviceidnode1.setProperty("end_date", end_date);
				serviceidnode1.setProperty("owner_email", owner_email);
				serviceidnode1.setProperty("quantity", quantity);
			}
			out.println("serviceidnode1 " + serviceidnode1);

			// ...........................................................................
			// admin 2nd part add serviceid node in servicenode2

			if (!servicenode2.hasNode(serviceid)) {
				serviceidnode2 = servicenode2.addNode(serviceid);
				serviceidnode2.setProperty("producttype", productcode);
				serviceidnode2.setProperty("start_date", start_date);
				serviceidnode2.setProperty("end_date", end_date);
				serviceidnode2.setProperty("owner_email", owner_email);
				serviceidnode2.setProperty("quantity", quantity);

			} else {
				serviceidnode2 = servicenode2.getNode(serviceid);
				serviceidnode2.setProperty("producttype", productcode);
				serviceidnode2.setProperty("start_date", start_date);
				serviceidnode2.setProperty("end_date", end_date);
				serviceidnode2.setProperty("owner_email", owner_email);
				serviceidnode2.setProperty("quantity", quantity);

			}
			out.println("serviceidnode2 " + serviceidnode2);

			// admin node configuration
			// end----------------------------------------------------------------------------------
			// add for loop to save fullload users datas
			for (int i = 0; i < fullloaddata.length(); i++) {
				JSONObject subobj = fullloaddata.getJSONObject(i);
				out.println("subobj " + subobj);
				String subuser = subobj.getString("user").replace("@", "_");
				out.println("subuser " + subuser);
				String role = subobj.getString("role");
				out.println("role " + role);
				String group = subobj.getString("group");
				out.println("group " + group);
				String roleid = subobj.getString("roleid");
				out.println("roleid " + roleid);

				final JSONObject postObj = new JSONObject();
				postObj.put("username", subobj.getString("user"));
				postObj.put("roleid", roleid);
				postObj.put("isactive", 0);

				Runnable myrunnable = new Runnable() {
					public void run() {
						try {
							String name = uploadToServer("http://bizlem.io:8087/apirest/usrmgmt/user/", postObj);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				};
				new Thread(myrunnable).start();

				System.out.println("subuser " + subuser + " role " + role + " group " + group);
				if (!USERNode.hasNode(subuser)) {
					// add user in bizlem call httpurl connection
					String result = sendGet(
							"http://bizlem.io:8082/portal/process/shoppingcart/ShoppingCartUserCheckService?userId="
									+ subobj.getString("user"));
					System.out.println("result " + result);

					subusernode = USERNode.addNode(subuser);
				} else {
					subusernode = USERNode.getNode(subuser);
				}

				out.println("subusernode " + subusernode);
				if (!subusernode.hasNode("services")) {
					servicenode1 = subusernode.addNode("services");
				} else {
					servicenode1 = subusernode.getNode("services");
				}
				out.println("servicenode1 " + servicenode1);

				if (!servicenode1.hasNode(productcode)) {
					productcodenode = servicenode1.addNode(productcode);
				} else {
					productcodenode = servicenode1.getNode(productcode);
				}
				out.println("productcodenode " + productcodenode);

				if (!productcodenode.hasNode(serviceid)) {
					serviceidnode1 = productcodenode.addNode(serviceid);
				} else {
					serviceidnode1 = productcodenode.getNode(serviceid);
				}
				out.println("serviceidnode1 " + serviceidnode1);

				if (!serviceidnode1.hasNode(group)) {
					groupnode = serviceidnode1.addNode(group);
				} else {
					groupnode = serviceidnode1.getNode(group);
				}
				out.println("groupnode " + groupnode);

				// ...........................................................................
				// applicatio 2nd part
				if (!serviceidnode2.hasNode(group)) {
					groupnode = serviceidnode2.addNode(group);
				} else {
					groupnode = serviceidnode2.getNode(group);
				}
				out.println("groupnode " + groupnode);

				if (!groupnode.hasNode("users")) {
					usersnode = groupnode.addNode("users");
				} else {
					usersnode = groupnode.getNode("users");
				}
				if (!usersnode.hasNode(subuser)) {
					subusersnode = usersnode.addNode(subuser);
					subusersnode.setProperty("role", role);
					subusersnode.setProperty("roleid", roleid);

				} else {
					subusersnode = usersnode.getNode(subuser);
					subusersnode.setProperty("role", role);
					subusersnode.setProperty("roleid", roleid);

				}

			}

			// -----------------------------------------------------------------------------------------------------------

			// -------------------------------------------------------------------------------------------------------------

			/// content/services/service001(789)(Property
			/// producttype)/users/gaurav.bhandari_damacgroup.com.partial
			// jcr:root/content/user/gaurav.bhandari_damacgroup.com.partial
			/// serivces/doctiger/789/G1

			// jcr:root/content/user/gaurav.bhandari_damacgroup.com.partial
			// serivces/doctiger/789/G1

			session.save();
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println(e);

			e.printStackTrace();
		}

	}

	public String sendGet(String url) throws Exception {
		StringBuffer userresult = null;
		// String url =
		// "http://35.221.160.146:8180/UserValidation/services/UserValidation/raveUserExistence?userId=doctiger100@gmail.com";
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			userresult = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				userresult.append(inputLine);
			}
			in.close();

			System.out.println(userresult.toString());
			return userresult.toString();
		} catch (Exception e) {
			// e.printStackTrace();
			return e.getMessage();
		}
	}

	private String uploadToServer(String urlstr, JSONObject json) throws IOException, JSONException {
		StringBuilder response = null;
		URL url = null;
		HttpURLConnection con = null;
		DataOutputStream wr = null;
		BufferedReader in = null;
		// .replaceAll("[╔,û#á]", "")
		// BufferedWriter writer = null;
		try {
			// out.println("Json passed is "+json);
			url = new URL(urlstr);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");

			con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			// writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
			// writer.write(json.toString());
			wr.writeBytes(json.toString());
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine = null;
			response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			// System.out.println(response.toString());

		} catch (Exception e) {
			response = null;

		} finally {

			if (null != con) {
				con.disconnect();
				con = null;
			}
			if (null != wr) {
				wr.flush();
				wr.close();
				wr = null;
			}
			/*
			 * if(null != writer){ writer.flush(); writer.close(); writer = null; }
			 */
			if (null != in) {
				in.close();
				in = null;
			}
		}
		return (response == null ? "" : response.toString());

	}
}
