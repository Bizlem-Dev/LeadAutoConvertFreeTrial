package leadconverter.rulengine;

import java.io.*;
import java.util.*;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
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

import java.net.*;
import java.text.SimpleDateFormat;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/subscribermanger" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class SubscriberMangerBasedOnRuleEngineResponse extends SlingAllMethodsServlet {

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
		JSONArray mainarray = new JSONArray();
		JSONObject jsonobject = new JSONObject();
		String listid = null;

		String remoteuser = request.getRemoteUser();

		try {
			Session session = null;

			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			Node content = session.getRootNode().getNode("content");
		if (request.getRequestPathInfo().getExtension().equals("subscriber_handler")) {
			//String slingurl = content.getNode("ip")
			//		.getProperty("Sling_Url").getString();
			String slingurl = ResourceBundle.getBundle("config").getString("Sling_Url");
			JSONObject totaldatajson = new JSONObject();
			String totaldata = totaldatajson.toString();
			String slingresponse = this
					.sendpostdataToCreateList(slingurl,
							totaldata.replace(" ", "%20"),
							response);
		}else if (request.getRequestPathInfo().getExtension().equals("call_Rule_Engine_Main")) {
			//String slingurl = content.getNode("ip").getProperty("Sling_Url").getString();
			String slingurl = ResourceBundle.getBundle("config").getString("Sling_Url");
			ProcessMain.callRuleEngine();
		}
		
		} catch (Exception e) {

			out.println("Exception ex : : : " + e.getStackTrace());
		}
	}

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		try {

			Session session = null;
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			
			Node content = session.getRootNode().getNode("content");
			Node content_ip = session.getRootNode().getNode("content");
			//Node content_ip = session.getRootNode().getNode("content");
		
		if (request.getRequestPathInfo().getExtension().equals("list_subscriber_move_rulengine")) {
			StringBuilder builder = new StringBuilder();
		    BufferedReader bufferedReaderCampaign = request.getReader();
		    
		    String brokerageline;
		    while ((brokerageline = bufferedReaderCampaign.readLine()) != null) {
		     builder.append(brokerageline + "\n");
		    }
		    out.println("Inside list_subscriber_move_rulengine");
			JSONObject json_object = new JSONObject(builder.toString());
			
			String funnelName=json_object.getString("funnelName");
			String subFunnelName=json_object.getString("subFunnelName");
			String subscriber_email=json_object.getString("subscriber_email");
			String SubscriberId=json_object.getString("SubscriberId");
			String ListId=json_object.getString("ListId");
			String CampaignId=json_object.getString("CampaignId");
			String Category=json_object.getString("Category");
			String CreatedBy=json_object.getString("CreatedBy");
			String OutPutTemp_1=json_object.getString("OPTemp");
			String OutPutTemp=json_object.getString("Output");
			
			
			out.println("funnelName : "+funnelName);
			out.println("subscriber_email : "+subscriber_email);
			out.println("SubscriberId : "+SubscriberId);
			out.println("ListId : "+ListId);
			
			Node ListNode=null;
			Node ActiveListNode=null;
			Node DraftListNode=null;
			String ActiveListId=null;
			String DraftListId=null;
			String Counter=null;
			int campaign_counter_int=0;
			
			
			//String subscriberaddurl = content.getNode("ip").getProperty("Integration_Url").getString();
			String subscriberaddurl = ResourceBundle.getBundle("config").getString("Integration_Url");
			//String url = content.getNode("ip").getProperty("Delete_SubscriberIn_List").getString();
			String url = ResourceBundle.getBundle("config").getString("Delete_SubscriberIn_List");
			
			Node userNode = session.getRootNode().getNode("content").getNode("user").getNode(CreatedBy);
			Node subFunneNode = userNode.getNode("Lead_Converter").getNode("Email")
					.getNode("Funnel").getNode(funnelName).getNode(OutPutTemp);
			
			if(subFunneNode.hasProperty("Counter")){
				Counter=subFunneNode.getProperty("Counter").getString();
				out.println("Counter : "+Counter);
				campaign_counter_int = Integer.parseInt(Counter);
				out.println("campaign_counter_int : "+campaign_counter_int);
				if(campaign_counter_int==4){
					out.println(" Four equal to Four");
				}
			}
			
			out.println("subFunneNode : "+subFunneNode.getName());
			ListNode=subFunneNode.getNode("List");
			out.println("ListNode : "+ListNode.getName());
			ActiveListNode=ListNode.getNode("ActiveList");
			DraftListNode=ListNode.getNode("DraftList");
			String listid=null;
			if(!DraftListNode.hasNodes()){
				String listname ="Draft_List_"+OutPutTemp;
				//String listurl = content_ip.getNode("ip")
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
				DraftListNode.addNode(listid);
				DraftListId=listid;
			}else{
				NodeIterator DraftListNodeTtr=DraftListNode.getNodes();
				while(DraftListNodeTtr.hasNext()) {
					Node draftNode = DraftListNodeTtr.nextNode();
					DraftListId=draftNode.getName();
					out.println("DraftListId : "+DraftListId);
				}
			}
			
			String currentCampign="";
			String Campaign_Date="";
			
			if(ActiveListNode.hasProperty("currentCampign")){
				currentCampign=ActiveListNode.getProperty("currentCampign").getString();
			}
			if(ActiveListNode.hasProperty("Campaign_Date")){
				Campaign_Date=ActiveListNode.getProperty("Campaign_Date").getString();
			}
			
			out.println("ActiveListNode : "+ActiveListNode.getName());
			NodeIterator ActiveListNodeTtr=ActiveListNode.getNodes();
			while(ActiveListNodeTtr.hasNext()) {
				Node activeNode = ActiveListNodeTtr.nextNode();
				ActiveListId=activeNode.getName();
				out.println("ActiveListId : "+ActiveListId);
			}
			out.println("ActiveListId : "+ActiveListId);
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    //Date date1 = sdf.parse("2009-12-31");
		    //Date date2 = sdf.parse("2010-01-31");
			//campaign_counter_int
			if(!currentCampign.equals("")){
				String campaign_index_str=currentCampign.substring(currentCampign.length()-1, currentCampign.length());
				int campaign_index_int = Integer.parseInt(campaign_index_str);			
				
				if(campaign_index_int==1){
					if(!Campaign_Date.equals("")){
						
					    Date date_campare1 = new Date();
					    Date date1 = sdf1.parse(sdf1.format(date_campare1));
					    Date date2 = sdf1.parse(Campaign_Date);
					    
					    out.println("date1 : " + sdf1.format(date1));
					    out.println("date2 : " + sdf1.format(date2));
			
					    if (date1.compareTo(date2) > 0) {
					        out.println("Date1 is after Date2");
					        out.println("Put the subscriber in draft list");
					     // Put the subscriber in draft list
					        String addsubscriberinlistparameters = "?list_id=" + ActiveListId +"&subscriber_id="+ SubscriberId;
					        
							//String addsubscriberinlistparameters = "?list_id=" + DraftListId +"&subscriber_id="+ SubscriberId;
							String responsedata =this.sendpostdata(subscriberaddurl,addsubscriberinlistparameters.replace(" ","%20"),response).replace("<pre>", "");
					    } else if (date1.compareTo(date2) < 0) {
					        out.println("Date1 is before Date2");
					        //write logic here
					        // Put the subscriber in current List
					        out.println("Put the subscriber in current List");
					        String addsubscriberinlistparameters = "?list_id=" + ActiveListId +"&subscriber_id="+ SubscriberId;
							String responsedata =this.sendpostdata(subscriberaddurl,addsubscriberinlistparameters.replace(" ","%20"),response).replace("<pre>", "");
					    } else if (date1.compareTo(date2) == 0) {
					        out.println("Date1 is equal to Date2");
					     // Put the subscriber in draft list
					        out.println("Put the subscriber in draft list");
					        String addsubscriberinlistparameters = "?list_id=" + ActiveListId +"&subscriber_id="+ SubscriberId;
					        
							//String addsubscriberinlistparameters = "?list_id=" + DraftListId +"&subscriber_id="+ SubscriberId;
							String responsedata =this.sendpostdata(subscriberaddurl,addsubscriberinlistparameters.replace(" ","%20"),response).replace("<pre>", "");
					    } else {
					        out.println("How to get here?");
					    }
					}else{
						out.println("Add to Draft");
						//DraftListId
						
						String addsubscriberinlistparameters = "?list_id=" + DraftListId +"&subscriber_id="+ SubscriberId;
						String responsedata =this.sendpostdata(subscriberaddurl,addsubscriberinlistparameters.replace(" ","%20"),response).replace("<pre>", "");
					}
				}else if (campaign_index_int==campaign_counter_int){
					out.println("Add to Draft");
					//DraftListId
					String addsubscriberinlistparameters = "?list_id=" + DraftListId +"&subscriber_id="+ SubscriberId;
					String responsedata =this.sendpostdata(subscriberaddurl,addsubscriberinlistparameters.replace(" ","%20"),response).replace("<pre>", "");
				
					
					
				}else if(campaign_index_int>1 && campaign_index_int<campaign_counter_int){
					out.println("Add to Draft");
					//DraftListId
					String addsubscriberinlistparameters = "?list_id=" + DraftListId +"&subscriber_id="+ SubscriberId;
					String responsedata =this.sendpostdata(subscriberaddurl,addsubscriberinlistparameters.replace(" ","%20"),response).replace("<pre>", "");
				
					
				}
		    }
			
			//String url = content.getNode("ip").getProperty("Delete_SubscriberIn_List").getString();
            String deletesubscriberinlistparameters = "?list_id=" + ListId +"&subscriber_id="+SubscriberId;
			String apiresponse =this.sendpostdata(url,deletesubscriberinlistparameters.replace(" ", "%20"),response).replace("<pre>", "");
			
			//String subscriberaddurl = content.getNode("ip").getProperty("Integration_Url").getString();
			//String addsubscriberinlistparameters = "?list_id=" + ActiveListId +"&subscriber_id="+ SubscriberId;
			//String responsedata =this.sendpostdata(subscriberaddurl,addsubscriberinlistparameters.replace(" ","%20"),response).replace("<pre>", "");

			/*
			NodeIterator funnelitr = userNode.getNode("Lead_Converter").getNode("Email")
							.getNode("Funnel").getNode(funnelName).getNodes();
		    while(funnelitr.hasNext()) {
				 Node funnelNode = funnelitr.nextNode();
				 out.println("funnelNode.getName() : "+funnelNode.getName());
						     if(funnelNode.hasNodes()){
								   NodeIterator campaignNodesitr = funnelNode.getNodes();
							 }
			}
		   */
			session.save();
			/*
			String slingqery = "select [LIST_NAME,LIST_ID,NODE_ID] from [nt:base] where (contains('LIST_NAME','"
					+ list_name + "'))  and ISDESCENDANTNODE('/content/LEAD_CONVERTER/LIST/')";

			Workspace workspace = session.getWorkspace();

			Query query = workspace.getQueryManager().createQuery(slingqery, Query.JCR_SQL2);

			QueryResult queryResult = query.execute();
			NodeIterator iterator = queryResult.getNodes();

			long count_Value1 = 0;

			session.save();
			*/

			
		}
		
		} catch (Exception e) {
			out.println("Exception : : :" + e.getMessage());
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
		OutputStream writer = conn.getOutputStream();
		writer.write(urlParameters.getBytes());
		int responseCode = conn.getResponseCode();
		StringBuffer buffer = new StringBuffer();
		if (responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
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


