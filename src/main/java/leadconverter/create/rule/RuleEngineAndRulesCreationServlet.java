package leadconverter.create.rule;

import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;













import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;

import leadconverter.doctiger.LogByFileWriter;
import leadconverter.mongo.RuleEngineMongoDAO;

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




@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/rule_engine_and_rles_creation_servlet" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class RuleEngineAndRulesCreationServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = null;
	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException{
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
			if (request.getRequestPathInfo().getExtension().equals("create_funnel_rulengine")) {
				try{
					  String project_name = request.getParameter("project_name");
					  String ruleengine_name = request.getParameter("ruleengine_name");
					  String add_rule_engine_URL=ResourceBundle.getBundle("config").getString("add_rule_engine_URL");
					  String user_name=ResourceBundle.getBundle("config").getString("add_rule_engine_UserName");
					  String fileName = ResourceBundle.getBundle("config").getString("add_rule_engine_headings_fileName");
					  StringBuffer response_text = null;
					  try{
						  File file = new File(ResourceBundle.getBundle("config").getString("rule_engine_inputfields_file_path"));
						  BufferedReader br = new BufferedReader(new FileReader(file));
						  String inputLine;
						  response_text = new StringBuffer();
						  while ((inputLine = br.readLine()) != null) {
							  response_text.append(inputLine);
						  }
						  br.close();
                      }catch(Exception ex){
                    	  out.println("Inside Catch : "+ex.getMessage());
                    	  LogByFileWriter.logger_info("RuleEngineAndRulesCreationServlet : " + "Inside Catch : "+ex.getMessage());
                      }
					  JSONArray rule_inputfields_data = new JSONArray(response_text.toString());
					  JSONObject final_rule_inputfields_data=new JSONObject();
				                 final_rule_inputfields_data.put("user_name", user_name);
				                 final_rule_inputfields_data.put("project_name", project_name);
				                 final_rule_inputfields_data.put("ruleengine_name", ruleengine_name);
				                 final_rule_inputfields_data.put("data", rule_inputfields_data);
				      //out.println(final_rule_inputfields_data);
				      //LogByFileWriter.logger_info("RuleEngineAndRulesCreationServlet :  final_rule_inputfields_data " + final_rule_inputfields_data);
				      
				                 //String rule_engine_response=CreateRuleEngine.createRuleEngine(ruleengine_name.replace(" ", "_"));
					  //out.print("rule_engine_response : "+rule_engine_response);
					  //LogByFileWriter.logger_info("RuleEngineAndRulesCreationServlet : " + "rule_engine_response : "+rule_engine_response);
				                 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (request.getRequestPathInfo().getExtension().equals("create_funnel_rules")) {
				out.println("call_rulengine_test : : : ");
				LogByFileWriter.logger_info("RuleEngineAndRulesCreationServlet : " + "call_rulengine_test : : : ");
			}else if (request.getRequestPathInfo().getExtension().equals("get_fields")) {
				out.println(RuleEngineMongoDAO.findFieldsFromRuleFieldsDetails());
				LogByFileWriter.logger_info("RuleEngineAndRulesCreationServlet : " + "call_rulengine_test : : : ");
			}else if (request.getRequestPathInfo().getExtension().equals("get_category")) {
				out.println(RuleEngineMongoDAO.findCategoryFromRuleFieldsDetails());
				LogByFileWriter.logger_info("RuleEngineAndRulesCreationServlet : " + "call_rulengine_test : : : ");
			}else if (request.getRequestPathInfo().getExtension().equals("add_fields")) {
				out.println(RuleEngineMongoDAO.addRuleFieldsDetails());
				LogByFileWriter.logger_info("RuleEngineAndRulesCreationServlet : " + "call_rulengine_test : : : ");
			}
		    }catch (Exception e) {
	
				out.println("Exception ex : : : " + e.getStackTrace());
				LogByFileWriter.logger_info("RuleEngineAndRulesCreationServlet : " + "Exception ex : : : " + e.getStackTrace());
			}
	}

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		if (request.getRequestPathInfo().getExtension().equals("create_rule")) {
			try{
				
				JSONObject rule_final_json_obj = new JSONObject(request.getParameter("rule_final_json_obj").toString());
				String str=CreateRuleEngine.createRule(rule_final_json_obj);
				out.println(str);
			} catch (Exception ex) {
				out.println("Exception ex : " + ex.getMessage());
			}
        }else if (request.getRequestPathInfo().getExtension().equals("get_all_rule")) {
			try{
				String username=request.getParameter("username").toString();//"carrotrule@xyz.com";
				String projectname=request.getParameter("projectname").toString();//"June27F01";
				String ruleenginename=request.getParameter("ruleenginename").toString();//"June27F01_RE";
		        String all_rules_response=CreateRuleEngine.getAllRules(username,projectname,ruleenginename);
		        out.println(all_rules_response);
			} catch (Exception ex) {
				out.println("Exception ex : " + ex.getMessage());
			}
        }else if (request.getRequestPathInfo().getExtension().equals("fire_rule_engine")) {
            try{
				
				JSONObject rule_final_json_obj = new JSONObject(request.getParameter("rule_final_json_obj").toString());
				String rule_engine_url=request.getParameter("rule_engine_url").toString();
				String str=CreateRuleEngine.fireRule(rule_engine_url,rule_final_json_obj);
				out.println(str);
			} catch (Exception ex) {
				out.println("Exception ex : " + ex.getMessage());
			}
        }
	}

}




