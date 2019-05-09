package leadconverter.rulengine;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import leadconverter.mongo.ConnectionHelper;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.bson.BSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class RulEngineMongoDAO {
	public static void main(String[] args) {
		/*
		RulEngineMongoDAO rdao=new RulEngineMongoDAO();
		rdao.subscribersViewBasedOnFunnelData("subscribers","306","2084");
		*/
		  RulEngineMongoDAO rdao=new RulEngineMongoDAO();
		  JSONArray mainJsonArray;
		    int subscriber_click_count=0;
			String campaignclickstatistics_firstclick=null;
			String campaignclickstatistics_latestclick=null;
			int sent=0;
			String campaign_id=null;
	        String Sling_Subject=null;
			int clicks=0;
			int viewed=0;
			String open_rate=null;
			String click_rate=null;
			String subFunnelNodeName=null;
			String urlclickstatistics_url=null;
			String urlclickstatistics_clicked=null;
			String subscriber_viewed=null;
			String funnelNodeName=null;
		try {
			mainJsonArray = new JSONArray(rdao.subscribersViewBasedOnFunnelData("subscribers","487","2084").toString());// 306 2084  499 6985
			JSONObject subscriber_campaign_jsonObject=(JSONObject) mainJsonArray.get(0);
			  JSONArray subscriber_campaign_jsonArray=subscriber_campaign_jsonObject.getJSONArray("subscriber_campaign");
			  System.out.println("subscriber_campaign_jsonArray : "+subscriber_campaign_jsonArray);
			  //JSONArray jsonArray = (JSONArray) jsonObject.get("Data");
			  
			  for(int d=0;d<subscriber_campaign_jsonArray.length();d++){
					JSONObject subscriberObject = subscriber_campaign_jsonArray.getJSONObject(d);
					System.out.println("subscriberObject : "+subscriberObject);
					funnelNodeName         =(String) subscriberObject.get("funnelNodeName");
					subFunnelNodeName      =(String) subscriberObject.get("subFunnelNodeName");
					campaign_id            =(String) subscriberObject.get("campaign_id");
					Sling_Subject          =(String) subscriberObject.get("Sling_Subject");
					clicks                 =(int) subscriberObject.getInt("clicks");
					viewed                 =(int) subscriberObject.getInt("viewed");
					open_rate              =(String) subscriberObject.get("open_rate");
					click_rate             =(String) subscriberObject.get("click_rate");
					urlclickstatistics_url =(String) subscriberObject.get("urlclickstatistics_url");
					urlclickstatistics_clicked=(String) subscriberObject.get("urlclickstatistics_clicked");
					subscriber_viewed      =(String) subscriberObject.get("subscriber_viewed");
			  }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	}
	public JSONArray findSubscribersBasedOncampaignNameAndDimensionTest(String coll_name,String Campaign_Id,String Campaign_Name,String User_Id) {
		MongoClient mongoClient = null;
		DB db =null;
		DBCollection collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=new JSONArray();;
	    
	    try {
	    	mongoClient = ConnectionHelper.getConn();
	        db = mongoClient.getDB("phplisttest");
	        collection = db.getCollection("google_analytics_data");
            //phplist797
	        DBObject unwind = new BasicDBObject("$unwind", "$addresses");
	        
	        /*
	        DBObject match = new BasicDBObject("$match", new BasicDBObject(
	                "addresses.dimension2", User_Id).append("addresses.campaign", Campaign_Name).append("addresses.source", "phplist"+Campaign_Id));
	        */
	        DBObject match = new BasicDBObject("$match", new BasicDBObject(
	                "addresses.dimension2", User_Id));
	        
	        DBObject project = new BasicDBObject("$project", new BasicDBObject(
	                "_id", 0).append("addresses", 1));
	        //List<DBObject> dblist=
            List<DBObject> pipeline = Arrays.asList(unwind, match, project);
	        AggregationOutput output = collection.aggregate(pipeline);
            Iterable<DBObject> results = output.results();
	        for (DBObject result : results) {
	        	BSONObject addresses=(BSONObject) ((BSONObject) ((BSONObject) result.get("addresses")));
	        	System.out.println(addresses);
	        	//campaignJsonArr=new JSONArray(addresses.toString());
	        	
	        	campaignJsonArr.put(addresses);
	        	//System.out.println(campaignJsonArr);
	    	}
	        System.out.println("findSubscribersBasedOncampaignNameAndDimension2 :"+campaignJsonArr.length());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConn(mongoClient);
		}
        return campaignJsonArr;
    }
	public JSONArray findSubscribersBasedOncampaignNameAndDimension2(String coll_name,String Campaign_Id,String Campaign_Name,String User_Id) {
		MongoClient mongoClient = null;
		DB db =null;
		DBCollection collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=new JSONArray();;
	    
	    try {
	    	mongoClient = ConnectionHelper.getConn();
	        db = mongoClient.getDB("phplisttest");
	        collection = db.getCollection("google_analytics_data");
            //phplist797
	        DBObject unwind = new BasicDBObject("$unwind", "$addresses");
	        
	        DBObject match = new BasicDBObject("$match", new BasicDBObject(
	                "addresses.dimension2", User_Id).append("addresses.campaign", Campaign_Name).append("addresses.source", "phplist"+Campaign_Id));
	        /*
	        DBObject match = new BasicDBObject("$match", new BasicDBObject(
	                "addresses.dimension2", User_Id));
	        */
	        DBObject project = new BasicDBObject("$project", new BasicDBObject(
	                "_id", 0).append("addresses", 1));
	        //List<DBObject> dblist=
            List<DBObject> pipeline = Arrays.asList(unwind, match, project);
	        AggregationOutput output = collection.aggregate(pipeline);
            Iterable<DBObject> results = output.results();
	        for (DBObject result : results) {
	        	BSONObject addresses=(BSONObject) ((BSONObject) ((BSONObject) result.get("addresses")));
	        	System.out.println(addresses);
	        	//campaignJsonArr=new JSONArray(addresses.toString());
	        	
	        	campaignJsonArr.put(addresses);
	        	//System.out.println(campaignJsonArr);
	    	}
	        System.out.println("findSubscribersBasedOncampaignNameAndDimension2 :"+campaignJsonArr.length());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConn(mongoClient);
		}
        return campaignJsonArr;
    }
	
	public JSONArray subscribersViewBasedOnFunnelData(String coll_name,String id,String subscriber_id) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCollection<Document> campaign_collection = null;
	    JSONArray funnelListJsonArr=new JSONArray();
	    JSONArray subfunnelListJsonArr=null;
	    JSONObject subfunnelListJsonObj=null;
	    JSONObject availableUrlJsonObj=null;
	    
	    SimpleDateFormat dateFormatter=new SimpleDateFormat("dd MMM yyyy HH:mm"); 
	    try {
	        mongoClient=ConnectionHelper.getConnection();
	        database=mongoClient.getDatabase("phplisttest");
	        collection=database.getCollection(coll_name);
	        //Bson datefilter =and(gte("sendstart_date", dateFormatter.parse(start_date)),lte("sendstart_date", dateFormatter.parse(end_date)));
            //Bson filter1 =and(eq("funnelNodeName", funnel_name),eq("subFunnelNodeName", sub_funnel_name),datefilter);
            //Bson filter1 =and(eq("funnelNodeName", funnel_name),eq("subFunnelNodeName", sub_funnel_name));
	                Bson filter1 =and(eq("id", id),eq("subscriber_userid", subscriber_id));
	        
	        
					
					int subscriber_click_count=0;
					String campaignclickstatistics_firstclick=null;
					String campaignclickstatistics_latestclick=null;
					String campaignclickstatistics_clicked=null;
					
					int sent=0;
					String campaign_id=null;
			        String Sling_Subject=null;
					int clicks=0;
					int viewed=0;
					String open_rate=null;
					String click_rate=null;
					String subFunnelNodeName=null;
					String urlclickstatistics_url=null;
					String urlclickstatistics_clicked=null;
					String urlclickstatistics_latestclick=null;
					String subscriber_viewed=null;
					String funnelNodeName=null;
					String subscriber_userid=null;
					String List_Id=null;
					String subscriber_email=null;
					
					
					
					
					FindIterable<Document> campaign_click_fi = collection.find(filter1);
					MongoCursor<Document> campaign_clickcursor = campaign_click_fi.iterator();
					subfunnelListJsonArr=new JSONArray();
					subfunnelListJsonObj=new JSONObject();
					//subfunnelListJsonObj.put("subscriber_email", subscriber_email);
					//subfunnelListJsonObj.put("sub_funnel_name", sub_funnel_name);
					try {
						while(campaign_clickcursor.hasNext()) {
							Document doc=campaign_clickcursor.next();
							//System.out.println(doc.getString("urlclickstatistics_latestclick"));
							sent=doc.getInteger("sent");
							campaign_id=doc.getString("id");
							Sling_Subject=doc.getString("Sling_Subject");
							clicks=doc.getInteger("clicks");
							viewed=doc.getInteger("viewed");
							open_rate=doc.getString("open_rate");
							click_rate=doc.getString("click_rate");
							funnelNodeName=doc.getString("funnelNodeName");
							subFunnelNodeName=doc.getString("subFunnelNodeName");
							urlclickstatistics_url=doc.getString("urlclickstatistics_url");
							System.out.println("urlclickstatistics_url  : "+urlclickstatistics_url);
							urlclickstatistics_clicked=doc.getString("urlclickstatistics_clicked");
							System.out.println("urlclickstatistics_clicked  : "+urlclickstatistics_clicked);
							subscriber_viewed=doc.getString("subscriber_viewed");
							subscriber_userid=doc.getString("subscriber_userid");
							List_Id=doc.getString("List_Id");
							subscriber_email=doc.getString("subscriber_email");
							urlclickstatistics_latestclick=doc.getString("urlclickstatistics_latestclick");
							System.out.println("urlclickstatistics_latestclick  : "+urlclickstatistics_latestclick);
							
						    campaignclickstatistics_firstclick=doc.getString("campaignclickstatistics_firstclick");
							campaignclickstatistics_latestclick=doc.getString("campaignclickstatistics_latestclick");
							campaignclickstatistics_clicked=doc.getString("campaignclickstatistics_clicked");
							//++subscriber_click_count;
							availableUrlJsonObj=new JSONObject();
							availableUrlJsonObj.put("funnelNodeName",funnelNodeName);
			                availableUrlJsonObj.put("subFunnelNodeName",subFunnelNodeName);
							availableUrlJsonObj.put("campaign_id",campaign_id);
							availableUrlJsonObj.put("Sling_Subject",Sling_Subject);
							availableUrlJsonObj.put("clicks",clicks);
							availableUrlJsonObj.put("viewed",viewed);
							availableUrlJsonObj.put("open_rate",open_rate);
							availableUrlJsonObj.put("click_rate",click_rate);
							if(urlclickstatistics_url!=null){
							   availableUrlJsonObj.put("urlclickstatistics_url",urlclickstatistics_url);
							}else{
							   availableUrlJsonObj.put("urlclickstatistics_url","null");
							}
							if(urlclickstatistics_clicked!=null){
								availableUrlJsonObj.put("urlclickstatistics_clicked",urlclickstatistics_clicked);
							}else{
								availableUrlJsonObj.put("urlclickstatistics_clicked","null");
						    }
							
							if(!campaignclickstatistics_firstclick.equals("null")){
								availableUrlJsonObj.put("campaignclickstatistics_firstclick",campaignclickstatistics_firstclick);
							}else{
								availableUrlJsonObj.put("campaignclickstatistics_firstclick","null");
						    }
							if(!campaignclickstatistics_latestclick.equals("null")){
								availableUrlJsonObj.put("campaignclickstatistics_latestclick",campaignclickstatistics_latestclick);
							}else{
								availableUrlJsonObj.put("campaignclickstatistics_latestclick","null");
						    }
							if(!campaignclickstatistics_clicked.equals("null")){
								availableUrlJsonObj.put("campaignclickstatistics_clicked",campaignclickstatistics_clicked);
							}else{
								availableUrlJsonObj.put("campaignclickstatistics_clicked","null");
						    }
							
							availableUrlJsonObj.put("subscriber_viewed",subscriber_viewed);
							availableUrlJsonObj.put("subscriber_userid",subscriber_userid);
							availableUrlJsonObj.put("List_Id",List_Id);
							availableUrlJsonObj.put("subscriber_email",subscriber_email);
							if(urlclickstatistics_latestclick!=null){
								availableUrlJsonObj.put("urlclickstatistics_latestclick",urlclickstatistics_latestclick);
							}else{
								availableUrlJsonObj.put("urlclickstatistics_latestclick","null");
						    }
							subfunnelListJsonArr.put(availableUrlJsonObj);
							
						}
						subfunnelListJsonObj.put("subscriber_campaign", subfunnelListJsonArr);
						
					} finally {
						campaign_clickcursor.close();
					}
	                
					funnelListJsonArr.put(subfunnelListJsonObj);
					
			//System.out.println("funnelListJsonArr : "+funnelListJsonArr);
						
	    } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return funnelListJsonArr;
    }
}
