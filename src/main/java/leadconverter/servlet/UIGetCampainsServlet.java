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
@Properties({
		@Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetCampains" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts",
				"cat", "latestproducts", "brief", "prodlist", "catalog",
				"viewcart", "productslist", "addcart", "createproduct",
				"checkmodelno", "productEdit" }) })
public class UIGetCampainsServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	@Reference
	private SlingRepository repo;
	JSONObject listvalue;
	JSONObject jsonObject = new JSONObject();
	NodeIterator iterator = null;

	protected void doPost(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		PrintWriter out = response.getWriter();

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		NodeIterator funnelNodeIterator = null;
		String remoteuser = request.getParameter("remoteuser");
		if (request.getRequestPathInfo().getExtension().equals("getAllCampaign")){
		try {
			String user = request.getParameter("user");
			//out.println("user "+user); 
			Session session = null;
			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			Node funnelNodes = session.getRootNode().getNode(
					"content/user/" + user + "/Lead_Converter/Email/Funnel");
			//out.println("step 1 ");
			funnelNodeIterator = funnelNodes.getNodes();
			JSONObject campaignjsonObject = null;
			while (funnelNodeIterator.hasNext()) {
				//out.println("step 2 ");
				//campaignjsonObject = new JSONObject();
				Node funnelNode = funnelNodeIterator.nextNode();
				String funnelName = funnelNode.getName();
				//campaignjsonObject.put("funnelName", funnelName);
				if (funnelNode.hasNodes()) {

					if (funnelNode.hasNode("Explore")) {
						Node ExploreNodes=funnelNode.getNode("Explore");
						String subFunnelName = ExploreNodes.getName();
						//campaignjsonObject.put("subFunnelName", subFunnelName);
						NodeIterator ExploreNodesIterator=ExploreNodes.getNodes();
						while (ExploreNodesIterator.hasNext()) {
							campaignjsonObject = new JSONObject();
							campaignjsonObject.put("funnelName", funnelName);
							campaignjsonObject.put("subFunnelName", subFunnelName);
							Node campaignNode = ExploreNodesIterator.nextNode();
							String campaignName=campaignNode.getName();
							campaignjsonObject.put("campaignName", campaignName);
							jsonArray.put(campaignjsonObject);
						}
					}
					if (funnelNode.hasNode("Entice")) {
						Node EnticeNodes=funnelNode.getNode("Entice");
						String subFunnelName = EnticeNodes.getName();
						//campaignjsonObject.put("subFunnelName", subFunnelName);
						NodeIterator EnticeNodesIterator=EnticeNodes.getNodes();
						while (EnticeNodesIterator.hasNext()) {
							campaignjsonObject = new JSONObject();
							campaignjsonObject.put("funnelName", funnelName);
							campaignjsonObject.put("subFunnelName", subFunnelName);
							Node campaignNode = EnticeNodesIterator.nextNode();
							String campaignName=campaignNode.getName();
							campaignjsonObject.put("campaignName", campaignName);
							jsonArray.put(campaignjsonObject);
						}

					}
					if (funnelNode.hasNode("Inform")) {
						Node InformNodes=funnelNode.getNode("Inform");
						String subFunnelName = InformNodes.getName();
						//campaignjsonObject.put("subFunnelName", subFunnelName);
						NodeIterator InformNodesIterator=InformNodes.getNodes();
						while (InformNodesIterator.hasNext()) {
							campaignjsonObject = new JSONObject();
							campaignjsonObject.put("funnelName", funnelName);
							campaignjsonObject.put("subFunnelName", subFunnelName);
							Node campaignNode = InformNodesIterator.nextNode();
							String campaignName=campaignNode.getName();
							campaignjsonObject.put("campaignName", campaignName);
							jsonArray.put(campaignjsonObject);
						}

					}
					if (funnelNode.hasNode("Warm")) {
						Node WarmNodes=funnelNode.getNode("Warm");
						String subFunnelName = WarmNodes.getName();
						//campaignjsonObject.put("subFunnelName", subFunnelName);
						NodeIterator WarmNodesIterator=WarmNodes.getNodes();
						while (WarmNodesIterator.hasNext()) {
							campaignjsonObject = new JSONObject();
							campaignjsonObject.put("funnelName", funnelName);
							campaignjsonObject.put("subFunnelName", subFunnelName);
							Node campaignNode = WarmNodesIterator.nextNode();
							String campaignName=campaignNode.getName();
							campaignjsonObject.put("campaignName", campaignName);
							jsonArray.put(campaignjsonObject);
						}

					}
					if (funnelNode.hasNode("Connect")) {
						Node ConnectNodes=funnelNode.getNode("Connect");
						String subFunnelName = ConnectNodes.getName();
						//campaignjsonObject.put("subFunnelName", subFunnelName);
						NodeIterator ConnectNodesIterator=ConnectNodes.getNodes();
						while (ConnectNodesIterator.hasNext()) {
							campaignjsonObject = new JSONObject();
							campaignjsonObject.put("funnelName", funnelName);
							campaignjsonObject.put("subFunnelName", subFunnelName);
							Node campaignNode = ConnectNodesIterator.nextNode();
							String campaignName=campaignNode.getName();
							campaignjsonObject.put("campaignName", campaignName);
							jsonArray.put(campaignjsonObject);
						}

					}
				} else {

				}
			}
			//jsonObject.put("JsonArray", jsonArray);
			out.println(jsonArray);
		} catch (Exception e) {
			e.getMessage();
			out.println("Error : "+e.getMessage());
		}
	  }else if (request.getRequestPathInfo().getExtension().equals("getFunnelCampaign")){
			try {
				String user = request.getParameter("user");
				String funnel_name = request.getParameter("funnel_name");
				//out.println("user "+user); 
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin"
						.toCharArray()));
				Node funnelNodes = session.getRootNode().getNode(
						"content/user/" + user + "/Lead_Converter/Email/Funnel");
				//out.println("step 1 ");
				/*
				out.println("funnelNodeIterator getSize 1 : "+funnelNodes.getNodes().getSize());
				funnelNodeIterator = funnelNodes.getNodes();
				out.println("funnelNodeIterator getSize 2 : "+funnelNodeIterator.getSize());
				*/
				JSONObject campaignjsonObject = null;
				
				    Node funnelNode = funnelNodes.getNode(funnel_name);
					String funnelName = funnelNode.getName();
					//out.println("step 3 ");
					//campaignjsonObject.put("funnelName", funnelName);
					if (funnelNode.hasNodes()) {
						//out.println("step 4 ");
						if (funnelNode.hasNode("Explore")) {
							//out.println("step 5 ");
							Node ExploreNodes=funnelNode.getNode("Explore");
							String subFunnelName = ExploreNodes.getName();
							//campaignjsonObject.put("subFunnelName", subFunnelName);
							NodeIterator ExploreNodesIterator=ExploreNodes.getNodes();
							while (ExploreNodesIterator.hasNext()) {
								campaignjsonObject = new JSONObject();
								campaignjsonObject.put("funnelName", funnelName);
								campaignjsonObject.put("subFunnelName", subFunnelName);
								Node campaignNode = ExploreNodesIterator.nextNode();
								String campaignName=campaignNode.getName();
								campaignjsonObject.put("campaignName", campaignName);
								jsonArray.put(campaignjsonObject);
							}
						}
						if (funnelNode.hasNode("Entice")) {
							Node EnticeNodes=funnelNode.getNode("Entice");
							String subFunnelName = EnticeNodes.getName();
							//campaignjsonObject.put("subFunnelName", subFunnelName);
							NodeIterator EnticeNodesIterator=EnticeNodes.getNodes();
							while (EnticeNodesIterator.hasNext()) {
								campaignjsonObject = new JSONObject();
								campaignjsonObject.put("funnelName", funnelName);
								campaignjsonObject.put("subFunnelName", subFunnelName);
								Node campaignNode = EnticeNodesIterator.nextNode();
								String campaignName=campaignNode.getName();
								campaignjsonObject.put("campaignName", campaignName);
								jsonArray.put(campaignjsonObject);
							}

						}
						if (funnelNode.hasNode("Inform")) {
							Node InformNodes=funnelNode.getNode("Inform");
							String subFunnelName = InformNodes.getName();
							//campaignjsonObject.put("subFunnelName", subFunnelName);
							NodeIterator InformNodesIterator=InformNodes.getNodes();
							while (InformNodesIterator.hasNext()) {
								campaignjsonObject = new JSONObject();
								campaignjsonObject.put("funnelName", funnelName);
								campaignjsonObject.put("subFunnelName", subFunnelName);
								Node campaignNode = InformNodesIterator.nextNode();
								String campaignName=campaignNode.getName();
								campaignjsonObject.put("campaignName", campaignName);
								jsonArray.put(campaignjsonObject);
							}

						}
						if (funnelNode.hasNode("Warm")) {
							Node WarmNodes=funnelNode.getNode("Warm");
							String subFunnelName = WarmNodes.getName();
							//campaignjsonObject.put("subFunnelName", subFunnelName);
							NodeIterator WarmNodesIterator=WarmNodes.getNodes();
							while (WarmNodesIterator.hasNext()) {
								campaignjsonObject = new JSONObject();
								campaignjsonObject.put("funnelName", funnelName);
								campaignjsonObject.put("subFunnelName", subFunnelName);
								Node campaignNode = WarmNodesIterator.nextNode();
								String campaignName=campaignNode.getName();
								campaignjsonObject.put("campaignName", campaignName);
								jsonArray.put(campaignjsonObject);
							}

						}
						if (funnelNode.hasNode("Connect")) {
							Node ConnectNodes=funnelNode.getNode("Connect");
							String subFunnelName = ConnectNodes.getName();
							//campaignjsonObject.put("subFunnelName", subFunnelName);
							NodeIterator ConnectNodesIterator=ConnectNodes.getNodes();
							while (ConnectNodesIterator.hasNext()) {
								campaignjsonObject = new JSONObject();
								campaignjsonObject.put("funnelName", funnelName);
								campaignjsonObject.put("subFunnelName", subFunnelName);
								Node campaignNode = ConnectNodesIterator.nextNode();
								String campaignName=campaignNode.getName();
								campaignjsonObject.put("campaignName", campaignName);
								jsonArray.put(campaignjsonObject);
							}

						}
					} else {

					}
				
				//jsonObject.put("JsonArray", jsonArray);
				out.println(jsonArray);
			} catch (Exception e) {
				e.getMessage();
				out.println("Error : "+e.getMessage());
			}  
		  
	  }else if (request.getRequestPathInfo().getExtension().equals("getFunnelCampaignBasedOnStatus")){
			try {
				String user = request.getParameter("user");
				//out.println("user "+user); 
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin"
						.toCharArray()));
				/*
				Node funnelNodes = session.getRootNode().getNode(
						"content/user/" + user + "/Lead_Converter/Email/Funnel");
				funnelNodeIterator = funnelNodes.getNodes();
				*/
				/*
				String slingqery="select * from [nt:base] where (contains('CREATED_BY','"+remoteuser+"'))  and ISDESCENDANTNODE('/content/LEAD_CONVERTER/LIST/')";
				Workspace workspace=session.getWorkspace();
				
				Query query=workspace.getQueryManager().createQuery(slingqery, Query.JCR_SQL2);
				
			    QueryResult  queryResult= query.execute();
			
			    funnelNodeIterator=queryResult.getNodes();
			    */
				JSONObject campaignjsonObject = null;
				while (funnelNodeIterator.hasNext()) {
					//out.println("step 2 ");
					//campaignjsonObject = new JSONObject();
					Node funnelNode = funnelNodeIterator.nextNode();
					String funnelName = funnelNode.getName();
					//campaignjsonObject.put("funnelName", funnelName);
					if (funnelNode.hasNodes()) {

						if (funnelNode.hasNode("Explore")) {
							Node ExploreNodes=funnelNode.getNode("Explore");
							String subFunnelName = ExploreNodes.getName();
							//campaignjsonObject.put("subFunnelName", subFunnelName);
							NodeIterator ExploreNodesIterator=ExploreNodes.getNodes();
							while (ExploreNodesIterator.hasNext()) {
								campaignjsonObject = new JSONObject();
								campaignjsonObject.put("funnelName", funnelName);
								campaignjsonObject.put("subFunnelName", subFunnelName);
								Node campaignNode = ExploreNodesIterator.nextNode();
								String campaignName=campaignNode.getName();
								campaignjsonObject.put("campaignName", campaignName);
								jsonArray.put(campaignjsonObject);
							}
						}
						if (funnelNode.hasNode("Entice")) {
							Node EnticeNodes=funnelNode.getNode("Entice");
							String subFunnelName = EnticeNodes.getName();
							//campaignjsonObject.put("subFunnelName", subFunnelName);
							NodeIterator EnticeNodesIterator=EnticeNodes.getNodes();
							while (EnticeNodesIterator.hasNext()) {
								campaignjsonObject = new JSONObject();
								campaignjsonObject.put("funnelName", funnelName);
								campaignjsonObject.put("subFunnelName", subFunnelName);
								Node campaignNode = EnticeNodesIterator.nextNode();
								String campaignName=campaignNode.getName();
								campaignjsonObject.put("campaignName", campaignName);
								jsonArray.put(campaignjsonObject);
							}

						}
						if (funnelNode.hasNode("Inform")) {
							Node InformNodes=funnelNode.getNode("Inform");
							String subFunnelName = InformNodes.getName();
							//campaignjsonObject.put("subFunnelName", subFunnelName);
							NodeIterator InformNodesIterator=InformNodes.getNodes();
							while (InformNodesIterator.hasNext()) {
								campaignjsonObject = new JSONObject();
								campaignjsonObject.put("funnelName", funnelName);
								campaignjsonObject.put("subFunnelName", subFunnelName);
								Node campaignNode = InformNodesIterator.nextNode();
								String campaignName=campaignNode.getName();
								campaignjsonObject.put("campaignName", campaignName);
								jsonArray.put(campaignjsonObject);
							}

						}
						if (funnelNode.hasNode("Warm")) {
							Node WarmNodes=funnelNode.getNode("Warm");
							String subFunnelName = WarmNodes.getName();
							//campaignjsonObject.put("subFunnelName", subFunnelName);
							NodeIterator WarmNodesIterator=WarmNodes.getNodes();
							while (WarmNodesIterator.hasNext()) {
								campaignjsonObject = new JSONObject();
								campaignjsonObject.put("funnelName", funnelName);
								campaignjsonObject.put("subFunnelName", subFunnelName);
								Node campaignNode = WarmNodesIterator.nextNode();
								String campaignName=campaignNode.getName();
								campaignjsonObject.put("campaignName", campaignName);
								jsonArray.put(campaignjsonObject);
							}

						}
						if (funnelNode.hasNode("Connect")) {
							Node ConnectNodes=funnelNode.getNode("Connect");
							String subFunnelName = ConnectNodes.getName();
							//campaignjsonObject.put("subFunnelName", subFunnelName);
							NodeIterator ConnectNodesIterator=ConnectNodes.getNodes();
							while (ConnectNodesIterator.hasNext()) {
								campaignjsonObject = new JSONObject();
								campaignjsonObject.put("funnelName", funnelName);
								campaignjsonObject.put("subFunnelName", subFunnelName);
								Node campaignNode = ConnectNodesIterator.nextNode();
								String campaignName=campaignNode.getName();
								campaignjsonObject.put("campaignName", campaignName);
								jsonArray.put(campaignjsonObject);
							}

						}
					} else {

					}
				}
				//jsonObject.put("JsonArray", jsonArray);
				out.println(jsonArray);
			} catch (Exception e) {
				e.getMessage();
				out.println("Error : "+e.getMessage());
			}  
		  
	  }

	}
}