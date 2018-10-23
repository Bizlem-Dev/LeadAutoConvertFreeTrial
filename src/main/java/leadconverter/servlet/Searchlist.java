package leadconverter.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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

import leadconverter.impl.Searching_list_DaoImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/Searchlist" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })

public class Searchlist extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	@Reference
	private SlingRepository repo;
	JSONObject  listvalue;
	JSONArray jsonArray=new JSONArray();
	JSONObject jsonObject=new JSONObject();
	NodeIterator iterator=null;
	
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			   throws ServletException, IOException{
			  PrintWriter out =response.getWriter();

				JSONArray jsonArray=new JSONArray();
				JSONObject jsonObject=new JSONObject();
				NodeIterator iterator=null;
		if (request.getRequestPathInfo().getExtension().equals("listsearch")) {
				
					
				String remoteuser=request.getParameter("remoteuser");
				
				
				//out.println(remoteuser);
				
					try {
		    					Session session = null;
		    					session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		    					Node tempRulNode=null;
		    					
		    					String slingqery="select [LIST_NAME] from [nt:base] where (contains('CREATED_BY','"+remoteuser+"'))  and ISDESCENDANTNODE('/content/LEAD_CONVERTER/LIST/')";
		    					Workspace workspace=session.getWorkspace();
		    					
		    					Query query=workspace.getQueryManager().createQuery(slingqery, Query.JCR_SQL2);
		    					
		    				QueryResult  queryResult= query.execute();
		    				
		    				 iterator=queryResult.getNodes();
		    				JSONObject jsonObject2=null;
		    					
		    					while (iterator.hasNext()) {
		    						tempRulNode =   iterator.nextNode();
		    						
		    						jsonObject2=new JSONObject();
		    						String list_id=tempRulNode.getProperty("LIST_ID").getString();
		    						jsonObject2.put("List_id", list_id);
		    						String list_name=tempRulNode.getProperty("LIST_NAME").getString();
		    					   jsonObject2.put("List_Name", list_name);
		    					   jsonArray.put(jsonObject2);
		    					}
		    					jsonObject.put("JsonArray", jsonArray);
		    					out.println(jsonObject);
		    			}catch(Exception e){
		    				e.getMessage();
		    				
		    			}

					
	}
		
		
	
	
	else if (request.getRequestPathInfo().getExtension().equals("getlist_data")) {
		try {
		Session session = null;
		session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	Node emailname=null;
	String selectedlistname=request.getParameter("selectedlistid");
	NodeIterator itr = session.getRootNode().getNode("content").getNode("LEAD_CONVERTER").getNode("SUBSCRIBER").getNodes();
	JSONObject jsonObject2=null;

	while(itr.hasNext()) {
		emailname=itr.nextNode();//emailname
		String list_id=emailname.getProperty("List_Id").getString();
		
		if(selectedlistname.equals(list_id)) {
			jsonObject2=new JSONObject();

			String emailnodename=emailname.getName();
			 jsonObject2.put("Email_Name", emailnodename);
			   jsonArray.put(jsonObject2);
		}
	}
	jsonObject.put("Email_Array", jsonArray);
	out.println(jsonObject);

	
		}
		catch(Exception ex) {
			
			out.println("Exception ex "+ex.getMessage());
			
		}
	}
	}


}