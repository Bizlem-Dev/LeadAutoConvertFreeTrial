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
	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			   throws ServletException, IOException{
			  PrintWriter out =response.getWriter();

				JSONArray jsonArray=new JSONArray();
				JSONObject jsonObject=new JSONObject();
				NodeIterator iterator=null;
				if (request.getRequestPathInfo().getExtension()
						.equals("getListSubscribers1")) {
					try {
						Session session = null;
						session = repo.login(new SimpleCredentials("admin", "admin"
								.toCharArray()));
						Node listNode = null;
						String nodeId=null;
						String selectedListID = request
								.getParameter("selectedlistid");
						//out.println("selectedlistname : "+selectedlistname);
						NodeIterator list_itr = session.getRootNode().getNode("content")
								.getNode("LEAD_CONVERTER").getNode("LIST")
								.getNodes();
						while (list_itr.hasNext()) {
							listNode=list_itr.nextNode();
							if (listNode.getProperty("LIST_ID")
									.getString().equals(selectedListID)) {
								//out.println();
								nodeId=listNode.getProperty("NODE_ID").getString();
							}
						}
						Node subscriberNode = null;
						NodeIterator subscribers_itr = session.getRootNode().getNode("content")
								.getNode("LEAD_CONVERTER").getNode("SUBSCRIBER")
								.getNodes();
						NodeIterator subscribers_list_itr=null;
						Node subscribers_list_node=null;
						String emailnodename=null;
						
						JSONObject sub_json_obj = null;
						//out.println("List Id to be serached : "+nodeId);
						while (subscribers_itr.hasNext()) {
							subscriberNode = subscribers_itr.nextNode();
							//out.println("subscriberNode : "+subscriberNode.getName());
							subscribers_list_itr =subscriberNode.getNode("List").getNodes();
							while (subscribers_list_itr.hasNext()) {
								subscribers_list_node=subscribers_list_itr.nextNode();
								//out.println("subscribers_list_node : "+subscribers_list_node.getName());
								//out.println("subscriberNode : "+subscriberNode.getName());
								//out.println("subscribers_list_node.getName().toString()).equals(nodeId) : "+(subscribers_list_node.getName().toString()).equals(nodeId));
								if((subscribers_list_node.getName().toString()).equals(nodeId)){
									//out.println("subscriberNode : "+subscriberNode.getName());
									sub_json_obj = new JSONObject();
									emailnodename = subscriberNode.getName();
									sub_json_obj.put("Email_Name", emailnodename.replace("_", "@"));
									sub_json_obj.put("Name", emailnodename.substring(0, emailnodename.indexOf("_")));
									
									if(subscriberNode.hasProperty("SUBSCRIBER_ID")){
										
										sub_json_obj.put("SUBSCRIBER_ID", subscriberNode.getProperty("SUBSCRIBER_ID").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("SUBSCRIBER_ID", "NA");
									}
									if(subscriberNode.hasProperty("SUBSCRIBER_NAME")){
										
										sub_json_obj.put("SUBSCRIBER_NAME", subscriberNode.getProperty("SUBSCRIBER_NAME").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("SUBSCRIBER_NAME", "NA");
									}
									if(subscriberNode.hasProperty("CURRENT DATE AND TIME")){
										
										sub_json_obj.put("CURRENT_DATE_AND_TIME", subscriberNode.getProperty("CURRENT DATE AND TIME").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("CURRENT_DATE_AND_TIME","NA");
									}
									if(subscriberNode.hasProperty("List_Id")){
										
										sub_json_obj.put("List_Id", subscriberNode.getProperty("List_Id").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("List_Id", "NA");
									}
									
									if(subscriberNode.hasProperty("EmailAddress")){
										
										sub_json_obj.put("EmailAddress", subscriberNode.getProperty("EmailAddress").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("EmailAddress", "NA");
									}
									if(subscriberNode.hasProperty("FirstName")){
										
										sub_json_obj.put("FirstName", subscriberNode.getProperty("FirstName").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("FirstName", "NA");
									}
									if(subscriberNode.hasProperty("LastName")){
										
										sub_json_obj.put("LastName", subscriberNode.getProperty("LastName").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("LastName", "NA");
									}
									if(subscriberNode.hasProperty("PhoneNumber")){
										
										sub_json_obj.put("PhoneNumber", subscriberNode.getProperty("PhoneNumber").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("PhoneNumber", "NA");
									}
									if(subscriberNode.hasProperty("Address")){
										
										sub_json_obj.put("Address", subscriberNode.getProperty("Address").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("Address", "NA");
									}
									
									
									if(subscriberNode.hasProperty("CompanyName")){
										
										sub_json_obj.put("CompanyName", subscriberNode.getProperty("CompanyName").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("CompanyName", "NA");
									}
									if(subscriberNode.hasProperty("CompanyHeadCount")){
										
										sub_json_obj.put("CompanyHeadCount", subscriberNode.getProperty("CompanyHeadCount").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("CompanyHeadCount", "NA");
									}
									if(subscriberNode.hasProperty("Industry")){
										
										sub_json_obj.put("Industry", subscriberNode.getProperty("Industry").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("Industry", "NA");
									}
									if(subscriberNode.hasProperty("Institute")){
										
										sub_json_obj.put("Institute", subscriberNode.getProperty("Institute").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("Institute", "NA");
									}
									if(subscriberNode.hasProperty("Source")){
										
										sub_json_obj.put("Source", subscriberNode.getProperty("Source").getString().toString().replace("%20", " "));
									}else{
										sub_json_obj.put("Source", "NA");
									}
									
									jsonArray.put(sub_json_obj);
									break;
								}
								
							}
						}
						JSONObject mainJson = new JSONObject();
						mainJson.put("data", jsonArray);
						out.println(mainJson);

					} catch (Exception ex) {

						out.println("Exception ex " + ex.getMessage());

					}
				}

				
	}
	
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
		    					   jsonObject2.put("List_Name", list_name.replace("%20", " "));
		    					   jsonArray.put(jsonObject2);
		    					}
		    					jsonObject.put("JsonArray", jsonArray);
		    					out.println(jsonObject);
		    			}catch(Exception e){
		    				e.getMessage();
		    				
		    			}

					
	}else if (request.getRequestPathInfo().getExtension()
				.equals("getlist_data")) {
			try {
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin"
						.toCharArray()));
				Node emailname = null;
				String selectedlistname = request
						.getParameter("selectedlistid");
				out.println("selectedlistname : "+selectedlistname);
				NodeIterator itr = session.getRootNode().getNode("content")
						.getNode("LEAD_CONVERTER").getNode("SUBSCRIBER")
						.getNodes();
				JSONObject jsonObject2 = null;

				while (itr.hasNext()) {
					emailname = itr.nextNode();// emailname
					String list_id = emailname.getProperty("List_Id")
							.getString();
					out.println("emailname step 1: "+emailname);
					out.println("selectedlistname.equals(list_id) : "+selectedlistname.equals(list_id));
					if (selectedlistname.equals(list_id)) {
						out.println("inside if 1");
						jsonObject2 = new JSONObject();
                      
						String emailnodename = emailname.getName();
						jsonObject2.put("Email_Name", emailnodename);
						jsonArray.put(jsonObject2);
						out.println("inside if 2");
						
					}
					out.println("emailname step 2: "+emailname);
				}
				jsonObject.put("Email_Array", jsonArray);
				out.println(jsonObject);

			} catch (Exception ex) {

				out.println("Exception ex " + ex.getMessage());

			}
		}else if (request.getRequestPathInfo().getExtension()
				.equals("getlist_data_new")) {
			try {
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin"
						.toCharArray()));
				Node listNode = null;
				String nodeId=null;
				String selectedListID = request
						.getParameter("selectedlistid");
				//out.println("selectedlistname : "+selectedlistname);
				NodeIterator list_itr = session.getRootNode().getNode("content")
						.getNode("LEAD_CONVERTER").getNode("LIST")
						.getNodes();
				while (list_itr.hasNext()) {
					listNode=list_itr.nextNode();
					if (listNode.getProperty("LIST_ID")
							.getString().equals(selectedListID)) {
						//out.println();
						nodeId=listNode.getProperty("NODE_ID").getString();
					}
				}
				Node subscriberNode = null;
				NodeIterator subscribers_itr = session.getRootNode().getNode("content")
						.getNode("LEAD_CONVERTER").getNode("SUBSCRIBER")
						.getNodes();
				NodeIterator subscribers_list_itr=null;
				Node subscribers_list_node=null;
				String emailnodename=null;
				
				JSONObject sub_json_obj = null;
				//out.println("List Id to be serached : "+nodeId);
				while (subscribers_itr.hasNext()) {
					subscriberNode = subscribers_itr.nextNode();
					//out.println("subscriberNode : "+subscriberNode.getName());
					subscribers_list_itr =subscriberNode.getNode("List").getNodes();
					while (subscribers_list_itr.hasNext()) {
						subscribers_list_node=subscribers_list_itr.nextNode();
						//out.println("subscribers_list_node : "+subscribers_list_node.getName());
						//out.println("subscriberNode : "+subscriberNode.getName());
						//out.println("subscribers_list_node.getName().toString()).equals(nodeId) : "+(subscribers_list_node.getName().toString()).equals(nodeId));
						if((subscribers_list_node.getName().toString()).equals(nodeId)){
							//out.println("subscriberNode : "+subscriberNode.getName());
							sub_json_obj = new JSONObject();
							emailnodename = subscriberNode.getName();
							sub_json_obj.put("Email_Name", emailnodename.replace("_", "@"));
							sub_json_obj.put("Name", emailnodename.substring(0, emailnodename.indexOf("_")));
							jsonArray.put(sub_json_obj);
							break;
						}
						
					}
				}
				out.println(jsonArray);

			} catch (Exception ex) {

				out.println("Exception ex " + ex.getMessage());

			}
		}else if (request.getRequestPathInfo().getExtension()
				.equals("getListSubscribers")) {
			try {
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin"
						.toCharArray()));
				Node listNode = null;
				String nodeId=null;
				String selectedListID = request
						.getParameter("selectedlistid");
				//out.println("selectedlistname : "+selectedlistname);
				NodeIterator list_itr = session.getRootNode().getNode("content")
						.getNode("LEAD_CONVERTER").getNode("LIST")
						.getNodes();
				while (list_itr.hasNext()) {
					listNode=list_itr.nextNode();
					if (listNode.getProperty("LIST_ID")
							.getString().equals(selectedListID)) {
						//out.println();
						nodeId=listNode.getProperty("NODE_ID").getString();
					}
				}
				Node subscriberNode = null;
				NodeIterator subscribers_itr = session.getRootNode().getNode("content")
						.getNode("LEAD_CONVERTER").getNode("SUBSCRIBER")
						.getNodes();
				NodeIterator subscribers_list_itr=null;
				Node subscribers_list_node=null;
				String emailnodename=null;
				
				JSONObject sub_json_obj = null;
				//out.println("List Id to be serached : "+nodeId);
				while (subscribers_itr.hasNext()) {
					subscriberNode = subscribers_itr.nextNode();
					//out.println("subscriberNode : "+subscriberNode.getName());
					subscribers_list_itr =subscriberNode.getNode("List").getNodes();
					while (subscribers_list_itr.hasNext()) {
						subscribers_list_node=subscribers_list_itr.nextNode();
						//out.println("subscribers_list_node : "+subscribers_list_node.getName());
						//out.println("subscriberNode : "+subscriberNode.getName());
						//out.println("subscribers_list_node.getName().toString()).equals(nodeId) : "+(subscribers_list_node.getName().toString()).equals(nodeId));
						if((subscribers_list_node.getName().toString()).equals(nodeId)){
							//out.println("subscriberNode : "+subscriberNode.getName());
							sub_json_obj = new JSONObject();
							emailnodename = subscriberNode.getName();
							sub_json_obj.put("Email_Name", emailnodename.replace("_", "@"));
							sub_json_obj.put("Name", emailnodename.substring(0, emailnodename.indexOf("_")));
							
							if(subscriberNode.hasProperty("SUBSCRIBER_ID")){
								
								sub_json_obj.put("SUBSCRIBER_ID", subscriberNode.getProperty("SUBSCRIBER_ID").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("SUBSCRIBER_ID", "NA");
							}
							if(subscriberNode.hasProperty("SUBSCRIBER_NAME")){
								
								sub_json_obj.put("SUBSCRIBER_NAME", subscriberNode.getProperty("SUBSCRIBER_NAME").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("SUBSCRIBER_NAME", "NA");
							}
							if(subscriberNode.hasProperty("CURRENT DATE AND TIME")){
								
								sub_json_obj.put("CURRENT_DATE_AND_TIME", subscriberNode.getProperty("CURRENT DATE AND TIME").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("CURRENT_DATE_AND_TIME","NA");
							}
							if(subscriberNode.hasProperty("List_Id")){
								
								sub_json_obj.put("List_Id", subscriberNode.getProperty("List_Id").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("List_Id", "NA");
							}
							
							if(subscriberNode.hasProperty("EmailAddress")){
								
								sub_json_obj.put("EmailAddress", subscriberNode.getProperty("EmailAddress").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("EmailAddress", "NA");
							}
							if(subscriberNode.hasProperty("FirstName")){
								
								sub_json_obj.put("FirstName", subscriberNode.getProperty("FirstName").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("FirstName", "NA");
							}
							if(subscriberNode.hasProperty("LastName")){
								
								sub_json_obj.put("LastName", subscriberNode.getProperty("LastName").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("LastName", "NA");
							}
							if(subscriberNode.hasProperty("PhoneNumber")){
								
								sub_json_obj.put("PhoneNumber", subscriberNode.getProperty("PhoneNumber").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("PhoneNumber", "NA");
							}
							if(subscriberNode.hasProperty("Address")){
								
								sub_json_obj.put("Address", subscriberNode.getProperty("Address").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("Address", "NA");
							}
							
							
							if(subscriberNode.hasProperty("CompanyName")){
								
								sub_json_obj.put("CompanyName", subscriberNode.getProperty("CompanyName").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("CompanyName", "NA");
							}
							if(subscriberNode.hasProperty("CompanyHeadCount")){
								
								sub_json_obj.put("CompanyHeadCount", subscriberNode.getProperty("CompanyHeadCount").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("CompanyHeadCount", "NA");
							}
							if(subscriberNode.hasProperty("Industry")){
								
								sub_json_obj.put("Industry", subscriberNode.getProperty("Industry").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("Industry", "NA");
							}
							if(subscriberNode.hasProperty("Institute")){
								
								sub_json_obj.put("Institute", subscriberNode.getProperty("Institute").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("Institute", "NA");
							}
							if(subscriberNode.hasProperty("Source")){
								
								sub_json_obj.put("Source", subscriberNode.getProperty("Source").getString().toString().replace("%20", " "));
							}else{
								sub_json_obj.put("Source", "NA");
							}
							
							jsonArray.put(sub_json_obj);
							break;
						}
						
					}
				}
				out.println(jsonArray);

			} catch (Exception ex) {

				out.println("Exception ex " + ex.getMessage());

			}
		}
	}


}