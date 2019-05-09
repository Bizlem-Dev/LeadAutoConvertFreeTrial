package leadconverter.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.json.JSONException;

import leadconverter.impl.Searching_list_DaoImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/callrulengine" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })

public class CallRuleEngine extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	@Reference
	private SlingRepository repo;
	JSONObject  listvalue;
	JSONArray jsonArray=new JSONArray();
	JSONObject jsonObject=new JSONObject();
	NodeIterator iterator=null;
	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			   throws ServletException, IOException{
			    PrintWriter out =response.getWriter();

				JSONArray jsonArray=new JSONArray();
				JSONObject jsonObject=new JSONObject();
				try {
					jsonObject.put("Open","2"); //
					jsonObject.put("Click","2"); // urlclickstatistics_clicked phplist data url collection
					jsonObject.put("Last_Click","2"); //latestclick
					jsonObject.put("Bounce","No");
					jsonObject.put("Session_time","715");
					jsonObject.put("Last_Session_Time","725");
					jsonObject.put("Pricing_URl","745");
					jsonObject.put("Last_Pricing_URl","750");
					jsonObject.put("Own_Visit","0");
					jsonObject.put("Last_Visit","0");
					jsonObject.put("Own_Session_time","0");
					jsonObject.put("Free_Trial","Yes");
					String rulr_engine_response=urlconnect("http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConvert_LACRules/fire",jsonObject);
					System.out.println(rulr_engine_response);
					out.println(rulr_engine_response);
					String leadStatus=null;
					JSONObject ruleEngineResponseJsonObject=new JSONObject(rulr_engine_response);
					if(ruleEngineResponseJsonObject.has("Output")){
					    leadStatus=ruleEngineResponseJsonObject.getString("Output");
					}else{
						leadStatus="NoStatus";
					}
					System.out.println("leadStatus : "+leadStatus);
					out.println("leadStatus : "+leadStatus);
				} catch (org.apache.sling.commons.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
	}
	
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			   throws ServletException, IOException{
				PrintWriter out =response.getWriter();
		
				JSONArray jsonArray=new JSONArray();
				JSONObject jsonObject=new JSONObject();
				try {
					jsonObject.put("Open","2");
					jsonObject.put("Click","2");
					jsonObject.put("Last_Click","2");
					jsonObject.put("Bounce","No");
					jsonObject.put("Session_time","715");
					jsonObject.put("Last_Session_Time","725");
					jsonObject.put("Pricing_URl","745");
					jsonObject.put("Last_Pricing_URl","750");
					jsonObject.put("Own_Visit","0");
					jsonObject.put("Last_Visit","0");
					jsonObject.put("Own_Session_time","0");
					jsonObject.put("Free_Trial","Yes");
					String rulr_engine_response=urlconnect("http://35.186.166.22:8082/drools/callrules/carrotrule@xyz.com_LeadAutoConvert_LACRules/fire",jsonObject);
					System.out.println(rulr_engine_response);
					out.println(rulr_engine_response);
					String leadStatus=null;
					JSONObject ruleEngineResponseJsonObject=new JSONObject(rulr_engine_response);
					if(ruleEngineResponseJsonObject.has("Output")){
					    leadStatus=ruleEngineResponseJsonObject.getString("Output");
					}else{
						leadStatus="NoStatus";
					}
					System.out.println("leadStatus : "+leadStatus);
					out.println("leadStatus : "+leadStatus);
				} catch (org.apache.sling.commons.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	private static String urlconnect(String urlstr, JSONObject json) throws IOException, JSONException {
		JSONObject jsonObject = null;
		StringBuffer response = null;

		try {

			int responseCode = 0;
			String urlParameters = "";

			URL url = new URL(urlstr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");

			con.setRequestProperty("Content-Type", "application/json");

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			// wr.writeBytes(json.toString());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
			writer.write(json.toString());
			writer.close();
			wr.flush();
			wr.close();

			responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toString();

	}


}