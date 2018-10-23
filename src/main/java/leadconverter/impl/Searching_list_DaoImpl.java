package leadconverter.impl;

	import java.io.PrintWriter;

	import javax.jcr.Node;
	import javax.jcr.NodeIterator;
	import javax.jcr.Session;
	import javax.jcr.Workspace;
	import javax.jcr.query.Query;
	import javax.jcr.query.QueryResult;

	import org.apache.sling.api.SlingHttpServletResponse;
	import org.apache.sling.commons.json.JSONArray;
	import org.apache.sling.commons.json.JSONObject;


	public class Searching_list_DaoImpl {

		JSONArray jsonArray=new JSONArray();
		JSONObject jsonObject=new JSONObject();
		NodeIterator iterator=null;
		public JSONObject getListSerch(String remoteuser, Session session, SlingHttpServletResponse response)   
		 {

//			String createuery="select [List_Name] from [nt:base] where (contains('List_Name','"+listname+"'))  and ISDESCENDANTNODE('/content/Lead_Converter/List/')";
	try{
			PrintWriter out =response.getWriter();
			Node tempRulNode=null;
			
			String slingqery="select [LIST_NAME] from [nt:base] where (contains('CREATED_BY','"+remoteuser+"'))  and ISDESCENDANTNODE('/content/LEAD_CONVERTER/LIST/')";
			Workspace workspace=session.getWorkspace();
			
			Query query=workspace.getQueryManager().createQuery(slingqery, Query.JCR_SQL2);
			
		QueryResult  queryResult= query.execute();
		
		NodeIterator iterator=queryResult.getNodes();
		JSONObject jsonObject2=null;
			
			while (iterator.hasNext()) {
				tempRulNode =   iterator.nextNode();
				
				jsonObject2=new JSONObject();
				String list_id=tempRulNode.getProperty("LIST_ID").getString();
				jsonObject2.put("List_id", list_id);
				String list_name=tempRulNode.getProperty("LIST_NAME").getString();
			   jsonObject2.put("List_Name", list_name);
			   jsonObject2.put("Query", slingqery);
			   jsonArray.put(jsonObject2);
			  
			}
	return jsonObject.put("JsonArray", jsonArray);
	}catch(Exception e){
		e.getMessage();
		
	}
	return jsonObject;
		}
	}