package leadconverter.mongo;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import java.util.Set;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.bson.BSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
//import org.json.simple.JSONArray;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.util.JSON;
import static com.mongodb.client.model.Filters.*;
//import static com.mongodb.client.model.Filters.*;

public class MongoDAO {
	//JSON.parse(subfunnelList.toString())
	

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
	        System.out.println(campaignJsonArr.length());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConn(mongoClient);
		}
        return campaignJsonArr;
    }
	
	public JSONArray subscribersViewBasedOnFunnelData(String coll_name,String funnel_name,String sub_funnel_name,String start_date,String end_date) {
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
	        Bson datefilter =and(gte("sendstart_date", dateFormatter.parse(start_date)),lte("sendstart_date", dateFormatter.parse(end_date)));
            Bson filter1 =and(eq("funnelNodeName", funnel_name),eq("subFunnelNodeName", sub_funnel_name),datefilter);
            //Bson filter1 =and(eq("funnelNodeName", funnel_name),eq("subFunnelNodeName", sub_funnel_name));
	        
	        DistinctIterable<String> di = collection.distinct("subscriber_email", filter1,String.class);
	        MongoCursor<String> cursor = di.iterator();
	        String subscriber_email=null;
	        
	        try {
				while(cursor.hasNext()) {
					subscriber_email=cursor.next();
					System.out.println(subscriber_email);
					
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
					
					Bson filter2 =and(eq("subscriber_email", subscriber_email),eq("funnelNodeName", funnel_name),eq("subFunnelNodeName", sub_funnel_name),datefilter);
					FindIterable<Document> campaign_click_fi = collection.find(filter2);
					MongoCursor<Document> campaign_clickcursor = campaign_click_fi.iterator();
					subfunnelListJsonArr=new JSONArray();
					subfunnelListJsonObj=new JSONObject();
					subfunnelListJsonObj.put("subscriber_email", subscriber_email);
					subfunnelListJsonObj.put("sub_funnel_name", sub_funnel_name);
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
							subFunnelNodeName=doc.getString("subFunnelNodeName");
							//++subscriber_click_count;
							availableUrlJsonObj=new JSONObject();
			                availableUrlJsonObj.put("subFunnelNodeName",subFunnelNodeName);
							availableUrlJsonObj.put("campaign_id",campaign_id);
							availableUrlJsonObj.put("Sling_Subject",Sling_Subject);
							availableUrlJsonObj.put("clicks",clicks);
							availableUrlJsonObj.put("viewed",viewed);
							availableUrlJsonObj.put("open_rate",open_rate);
							availableUrlJsonObj.put("click_rate",click_rate);
							/*
							String click_rate_new=String.valueOf(((100/Double.valueOf(sent))*Double.valueOf(subscriber_click_count)));
							if(click_rate_new.indexOf(".")==1){
								click_rate_new=click_rate_new.substring(0, click_rate_new.indexOf(".")+3);
							}else{
								click_rate_new=click_rate_new.substring(0, click_rate_new.indexOf(".")+2);
							}
							availableUrlJsonObj.put("click_rate_new",click_rate_new);
							*/
							subfunnelListJsonArr.put(availableUrlJsonObj);
							
						}
						subfunnelListJsonObj.put("subscriber_campaign", subfunnelListJsonArr);
						
					} finally {
						campaign_clickcursor.close();
					}
	                
					funnelListJsonArr.put(subfunnelListJsonObj);
					
					 
					 
				}
			} finally {
				cursor.close();
			}
			
			System.out.println("funnelListJsonArr : "+funnelListJsonArr);
						
	    } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return funnelListJsonArr;
    }
	
	public JSONArray subscribersViewBasedOnSubFunnelData(String coll_name,String funnel_name,String sub_funnel_name,String start_date,String end_date) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCollection<Document> campaign_collection = null;
	    JSONArray funnelListJsonArr=new JSONArray();
	    JSONArray subFunnelListJsonArr=new JSONArray();
	    JSONArray subfunnelListJsonArr=null;
	    JSONObject subfunnelListJsonObj=null;
	    JSONObject availableUrlJsonObj=null;
	    
	    SimpleDateFormat dateFormatter=new SimpleDateFormat("dd MMM yyyy HH:mm"); 
	    try {
	        mongoClient=ConnectionHelper.getConnection();
	        database=mongoClient.getDatabase("phplisttest");
	        collection=database.getCollection(coll_name);
	        Bson datefilter =and(gte("sendstart_date", dateFormatter.parse(start_date)),lte("sendstart_date", dateFormatter.parse(end_date)));
            //Bson filter1 =and(eq("funnelNodeName", funnel_name),eq("subFunnelNodeName", sub_funnel_name),datefilter);
            //Bson filter1 =and(eq("funnelNodeName", funnel_name),datefilter);
	        Bson filter1 =and(eq("funnelNodeName", funnel_name));
	        
	        DistinctIterable<String> subfunnel_di = collection.distinct("subFunnelNodeName", filter1,String.class);
	        MongoCursor<String> subfunnel_cursor = subfunnel_di.iterator();
	        while(subfunnel_cursor.hasNext()) {
	        	subFunnelListJsonArr.put(subfunnel_cursor.next());
	        }
	        System.out.println(subFunnelListJsonArr);
	        //DistinctIterable<String> di = collection.distinct("subscriber_email", filter1,String.class);
            DistinctIterable<String> di = collection.distinct("subscriber_email", filter1,String.class);
	        MongoCursor<String> cursor = di.iterator();
	        String subscriber_email=null;
	        
	        try {
				while(cursor.hasNext()) {
					subscriber_email=cursor.next();
					System.out.println(subscriber_email);
					
					int subscriber_click_count=0;
					String campaignclickstatistics_firstclick=null;
					String campaignclickstatistics_latestclick=null;
					String subscriber_userid=null;
					int sent=0;
					String campaign_id=null;
			        String Sling_Subject=null;
					int clicks=0;
					int viewed=0;
					String open_rate=null;
					String click_rate=null;
					String subFunnelNodeName=null;
					String funnelNodeName=null;
					String subscriber_email_new=null;
					String urlclickstatistics_url=null;
					subfunnelListJsonArr=new JSONArray();
					subfunnelListJsonObj=new JSONObject();
					subfunnelListJsonObj.put("subscriber_email", subscriber_email);
					subfunnelListJsonObj.put("subfunnel_list", subFunnelListJsonArr);
					
					JSONArray subfunnelJsonArr=null;
					    for(int i=0;i<subFunnelListJsonArr.length();i++){
					    	System.out.println(subFunnelListJsonArr.getString(i));
					        //Bson filter2 =and(eq("subscriber_email", subscriber_email),eq("funnelNodeName", funnel_name),eq("subFunnelNodeName", sub_funnel_name),datefilter);
							Bson filter2 =and(eq("subscriber_email", subscriber_email),eq("funnelNodeName", funnel_name),eq("subFunnelNodeName", subFunnelListJsonArr.getString(i)));
							FindIterable<Document> campaign_click_fi = collection.find(filter2);
							MongoCursor<Document> campaign_clickcursor = campaign_click_fi.iterator();
							//subfunnelListJsonArr=new JSONArray();
							subfunnelJsonArr=new JSONArray();
							try {
								while(campaign_clickcursor.hasNext()) {
									Document doc=campaign_clickcursor.next();
									//System.out.println(doc);
									//System.out.println("urlclickstatistics_url : "+doc.getString("urlclickstatistics_url"));
									    sent=doc.getInteger("sent");
										campaign_id=doc.getString("id");
										Sling_Subject=doc.getString("Sling_Subject");
										clicks=doc.getInteger("clicks");
										viewed=doc.getInteger("viewed");
										open_rate=doc.getString("open_rate");
										click_rate=doc.getString("click_rate");
										subFunnelNodeName=doc.getString("subFunnelNodeName");
										funnelNodeName=doc.getString("funnelNodeName");
										subscriber_email_new=doc.getString("subscriber_email");
										urlclickstatistics_url=doc.getString("urlclickstatistics_url");
										subscriber_userid=doc.getString("subscriber_userid");
										if(urlclickstatistics_url==null || urlclickstatistics_url.equals("null")){
											urlclickstatistics_url="null";
										}
										//++subscriber_click_count;
										availableUrlJsonObj=new JSONObject();
										availableUrlJsonObj.put("funnelNodeName",funnelNodeName);
						                availableUrlJsonObj.put("subFunnelNodeName",subFunnelNodeName);
						                availableUrlJsonObj.put("subscriber_email",subscriber_email_new);
						                availableUrlJsonObj.put("subscriber_userid",subscriber_userid);
										availableUrlJsonObj.put("campaign_id",campaign_id);
										availableUrlJsonObj.put("urlclickstatistics_url",urlclickstatistics_url);
										availableUrlJsonObj.put("Sling_Subject",Sling_Subject);
										availableUrlJsonObj.put("clicks",clicks);
										availableUrlJsonObj.put("viewed",viewed);
										availableUrlJsonObj.put("open_rate",open_rate);
										availableUrlJsonObj.put("click_rate",click_rate);
										/*
										String click_rate_new=String.valueOf(((100/Double.valueOf(sent))*Double.valueOf(subscriber_click_count)));
										if(click_rate_new.indexOf(".")==1){
											click_rate_new=click_rate_new.substring(0, click_rate_new.indexOf(".")+3);
										}else{
											click_rate_new=click_rate_new.substring(0, click_rate_new.indexOf(".")+2);
										}
										availableUrlJsonObj.put("click_rate_new",click_rate_new);
										*/
										//subfunnelListJsonArr.put(availableUrlJsonObj);
										//System.out.println(availableUrlJsonObj);
										subfunnelJsonArr.put(availableUrlJsonObj);
										
								}
								subfunnelListJsonObj.put(subFunnelListJsonArr.getString(i),subfunnelJsonArr);
								//System.out.println(subfunnelListJsonObj);
								/*
								 * This code is used to remove duplicate from jsonArray
								Set<String> stationCodes=new HashSet<String>();
								JSONArray tempArray=new JSONArray();
								for(int i=0;i<subfunnelListJsonArr.length();i++){
								   String  camp_id=subfunnelListJsonArr.getJSONObject(i).getString("campaign_id");
								   if(stationCodes.contains(camp_id)){
								     continue;
								   }
								   else{
									   stationCodes.add(camp_id);
								       tempArray.put(subfunnelListJsonArr.getJSONObject(i));
								   }
		
								}
								*/
								//subfunnelListJsonObj.put("subscriber_campaign", tempArray);
								//subfunnelListJsonObj.put("subscriber_campaign", subfunnelListJsonArr);
								
							} finally {
								campaign_clickcursor.close();
							}
					    }
					funnelListJsonArr.put(subfunnelListJsonObj);
					
					 
					 
				}
			} finally {
				cursor.close();
			}
			
			System.out.println("funnelListJsonArr : "+funnelListJsonArr);
						
	    } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return funnelListJsonArr;
    }
	
	public JSONArray ucsCampaignClickStatistics(String coll_name,String urlclickstatistics_url,String campaignId) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCollection<Document> campaign_collection = null;
	    JSONArray funnelListJsonArr=new JSONArray();
	    JSONObject availableUrlJsonObj=null;
	    try {
	        mongoClient=ConnectionHelper.getConnection();
	        database=mongoClient.getDatabase("phplisttest");
	        collection=database.getCollection(coll_name);
	        Bson filter1 =eq("id", campaignId);
	        
	        DistinctIterable<String> di = collection.distinct("urlclickstatistics_url", filter1,String.class);
	        MongoCursor<String> cursor = di.iterator();
	        
	        /*
	        FindIterable<Document> fi = collection.find(filter1);
	        MongoCursor<Document> cursor = fi.iterator();
	       */
	        String campaign_id=null;
	        String urlclickstatistics_url_temp=null;
	        
	        try {
				while(cursor.hasNext()) {
					urlclickstatistics_url_temp=cursor.next();
					int subscriber_click_count=0;
					
					String Sling_Subject=null;
					String campaignclickstatistics_firstclick=null;
					String campaignclickstatistics_latestclick=null;
					int sent=0;
					
					
					Bson filter2 =and(eq("id", campaignId),eq("urlclickstatistics_url", urlclickstatistics_url_temp));
					FindIterable<Document> campaign_click_fi = collection.find(filter2);
					MongoCursor<Document> campaign_clickcursor = campaign_click_fi.iterator();
					try {
						while(campaign_clickcursor.hasNext()) {
							Document doc=campaign_clickcursor.next();
							//System.out.println(doc.getString("urlclickstatistics_latestclick"));
							sent=doc.getInteger("sent");
							Sling_Subject=doc.getString("Sling_Subject");
							campaignclickstatistics_firstclick=doc.getString("campaignclickstatistics_firstclick");
							campaignclickstatistics_latestclick=doc.getString("campaignclickstatistics_latestclick");
							++subscriber_click_count;
							
						}
						
					} finally {
						campaign_clickcursor.close();
					}
	                
					
					
					/*
					Document doc=cursor.next();
					System.out.println(doc.getString("urlclickstatistics_url")+"	"+doc.getString("Sling_Subject")+"	"
					+doc.getString("id")+"	"+doc.getString("subscriber_email")+"	"+doc.getString("campaignclickstatistics_firstclick")+"	"
							+doc.getString("campaignclickstatistics_latestclick")+"	"
									+doc.getInteger("sent"));
					*/
					//subscriber_email  campaignclickstatistics_firstclick  campaignclickstatistics_latestclick
					//campaignid
					//urlclickstatistics_url=cursor.next().toString();
					
					availableUrlJsonObj=new JSONObject();
					availableUrlJsonObj.put("campaign_id",campaign_id);
					availableUrlJsonObj.put("URL",urlclickstatistics_url_temp);
					availableUrlJsonObj.put("Campaign_Name",Sling_Subject);
					availableUrlJsonObj.put("FIRST_CLICK",campaignclickstatistics_firstclick);
					availableUrlJsonObj.put("LATEST_CLICK",campaignclickstatistics_latestclick);
					availableUrlJsonObj.put("CLICKS",subscriber_click_count);
					String click_rate=String.valueOf(((100/Double.valueOf(sent))*Double.valueOf(subscriber_click_count)));
					if(click_rate.indexOf(".")==1){
					   click_rate=click_rate.substring(0, click_rate.indexOf(".")+3);
					}else{
						click_rate=click_rate.substring(0, click_rate.indexOf(".")+2);
					}
					availableUrlJsonObj.put("CLICK_RATE",click_rate);
			        funnelListJsonArr.put(availableUrlJsonObj);
			        
					
					 
					 
				}
			} finally {
				cursor.close();
			}
			System.out.println("funnelListJsonArr : "+funnelListJsonArr);
						
	    } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return funnelListJsonArr;
    }
	
	public JSONArray ucsUrlClickStatistics(String coll_name,String urlclickstatistics_url) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCollection<Document> campaign_collection = null;
	    JSONArray funnelListJsonArr=new JSONArray();
	    JSONObject availableUrlJsonObj=null;
	    try {
	        mongoClient=ConnectionHelper.getConnection();
	        database=mongoClient.getDatabase("phplisttest");
	        collection=database.getCollection(coll_name);
	        Bson filter1 =eq("urlclickstatistics_url", urlclickstatistics_url);
	        DistinctIterable<String> di = collection.distinct("id", filter1,String.class);
	        MongoCursor<String> cursor = di.iterator();
	        String campaign_id=null;
	        try {
				while(cursor.hasNext()) {
					campaign_id=cursor.next();
					int campaign_count=0;
					String urlclickstatistics_latestclick=null;
					int subscriber_click_count=0;
					
					String Sling_Subject=null;
					String campaignclickstatistics_firstclick=null;
					String campaignclickstatistics_latestclick=null;
					int sent=0;
					
					Bson filter2 =and(eq("id", campaign_id),eq("urlclickstatistics_url", urlclickstatistics_url));
					FindIterable<Document> campaign_click_fi = collection.find(filter2);
					MongoCursor<Document> campaign_clickcursor = campaign_click_fi.iterator();
					
					try {
						while(campaign_clickcursor.hasNext()) {
							Document doc=campaign_clickcursor.next();
							sent=doc.getInteger("sent");
							Sling_Subject=doc.getString("Sling_Subject");
							campaignclickstatistics_firstclick=doc.getString("campaignclickstatistics_firstclick");
							campaignclickstatistics_latestclick=doc.getString("campaignclickstatistics_latestclick");
							++subscriber_click_count;
							
						}
					} finally {
						campaign_clickcursor.close();
					}
					availableUrlJsonObj=new JSONObject();
					availableUrlJsonObj.put("campaign_id",campaign_id);
					availableUrlJsonObj.put("URL_CLICK_STATISTICS",Sling_Subject);
					availableUrlJsonObj.put("FIRST_CLICK",campaignclickstatistics_firstclick);
					availableUrlJsonObj.put("LATEST_CLICK",campaignclickstatistics_latestclick);
					availableUrlJsonObj.put("CLICKS",subscriber_click_count);
					String click_rate=String.valueOf(((100/Double.valueOf(sent))*Double.valueOf(subscriber_click_count)));
					if(click_rate.indexOf(".")==1){
					   click_rate=click_rate.substring(0, click_rate.indexOf(".")+3);
					}else{
						click_rate=click_rate.substring(0, click_rate.indexOf(".")+2);
					}
					availableUrlJsonObj.put("CLICK_RATE",click_rate);
					availableUrlJsonObj.put("URL",urlclickstatistics_url);
			        funnelListJsonArr.put(availableUrlJsonObj);
					 
					 
				}
			} finally {
				cursor.close();
			}
			System.out.println("funnelListJsonArr : "+funnelListJsonArr);
						
	    } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return funnelListJsonArr;
    }
	
	public JSONArray findUniqueUrl(String coll_name,String Sling_Campaign_Id) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCollection<Document> campaign_collection = null;
	    JSONArray funnelListJsonArr=new JSONArray();
	    JSONObject availableUrlJsonObj=null;
	    String urlclickstatistics_url=null;
        try {
	        mongoClient=ConnectionHelper.getConnection();
	        database=mongoClient.getDatabase("phplisttest");
	        collection=database.getCollection(coll_name);
	        //Bson filter1 =eq("funnelNodeName", Sling_Campaign_Id);
	        //DistinctIterable<String> fi = collection.distinct("subFunnelNodeName",filter1,String.class);
	        
	        DistinctIterable<String> di = collection.distinct("urlclickstatistics_url", String.class);
	        MongoCursor<String> cursor = di.iterator();
	        /*
	        FindIterable<Document> fi = collection.find();
	        MongoCursor<Document> cursor = fi.iterator();
	        */
	        try {
				while(cursor.hasNext()) {
					/*
					Document doc=cursor.next();
					//doc.getString("urlclickstatistics_url");
					//doc.getString("Sling_Subject");
					System.out.println(doc.getString("urlclickstatistics_url")+"	"+doc.getString("Sling_Subject"));
					*/
					//campaignid
					urlclickstatistics_url=cursor.next().toString();
					
					int campaign_count=0;
					String urlclickstatistics_latestclick=null;
					int url_click_count=0;
					
					Bson filter3 =eq("urlclickstatistics_url", urlclickstatistics_url);
					FindIterable<Document> ttl_click_fi = collection.find(filter3);
					MongoCursor<Document> ttl_clickcursor = ttl_click_fi.iterator();
					
					try {
						while(ttl_clickcursor.hasNext()) {
							//ttl_clickcursor.next();
							// System.out.println(ttl_clickcursor.next().getString("urlclickstatistics_clicked"));
							Document doc=ttl_clickcursor.next();
							//System.out.println(doc.getString("urlclickstatistics_latestclick"));
							if(url_click_count==0){
								urlclickstatistics_latestclick=doc.getString("urlclickstatistics_latestclick");
							}
							//System.out.println("---"+ttl_clickcursor.next());
							++url_click_count;
							
						}
					} finally {
						ttl_clickcursor.close();
					}
					
					System.out.println(urlclickstatistics_url);
					Bson filter2 =eq("urlclickstatistics_url", urlclickstatistics_url);
					DistinctIterable<String> campaign_di = collection.distinct("id",filter2, String.class);
			        MongoCursor<String> campaign_cursor = campaign_di.iterator();
			        //System.out.println("---"+campaign_cursor.hasNext());
			        
			        try {
						while(campaign_cursor.hasNext()) {
							campaign_cursor.next();
							//System.out.println("---"+campaign_cursor.next());
							++campaign_count;
							
						}
					} finally {
						campaign_cursor.close();
					}
			        //System.out.println("campaign_count : "+campaign_count);
			        //System.out.println("url_click_count : "+url_click_count);
					//funnelListJsonArr.put(cursor.next());
			        availableUrlJsonObj=new JSONObject();
			        availableUrlJsonObj.put("AVAILABLE_URLS", urlclickstatistics_url);
			        availableUrlJsonObj.put("MSGS", campaign_count);
			        availableUrlJsonObj.put("LAST_CLICKED", urlclickstatistics_latestclick);
			        availableUrlJsonObj.put("CLICKS", url_click_count);
			        funnelListJsonArr.put(availableUrlJsonObj);
					 
					 
				}
			} finally {
				cursor.close();
			}
			System.out.println("funnelListJsonArr : "+funnelListJsonArr);
						
	    } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return funnelListJsonArr;
    }
	
	
	
	public JSONArray findAllUrlByBasicDBObject(String coll_name,String funnel_id) {
		MongoClient mongoClient = null;
		DB db =null;
		DBCollection collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=null;
	    
	    try {
	    	mongoClient = ConnectionHelper.getConn();
	        db = mongoClient.getDB("phplisttest");
	        collection = db.getCollection("funnel");

	        DBObject unwind = new BasicDBObject("$unwind", "$viewed_subscribers.data");
	        DBObject match = new BasicDBObject("$match", new BasicDBObject(
	                "viewed_subscribers.data.userid", ""));
	        DBObject project = new BasicDBObject("$project", new BasicDBObject(
	                "_id", 0).append("viewed_subscribers.data.urlclickstatistics", 1));
            List<DBObject> pipeline = Arrays.asList(unwind, match, project);
	        AggregationOutput output = collection.aggregate(pipeline);
            Iterable<DBObject> results = output.results();
	        for (DBObject result : results) {
	        	BSONObject urlclickstatistics=(BSONObject) ((BSONObject) ((BSONObject) result.get("viewed_subscribers")).get("data")).get("urlclickstatistics");
	        	campaignJsonArr=new JSONArray(urlclickstatistics.toString());
	        	System.out.println(campaignJsonArr.length());
	    	}
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConn(mongoClient);
		}
        return campaignJsonArr;

    }
	public  JSONArray findAllSubscriberByFilter(String coll_name) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	    JSONObject funnelJsonObj=null;
	    Document  funnelJsonDoc=null;
	    
	    String old_id=null;
	    String id=null;
	    String uuid=null;
	    String subject=null;
	    String sendstart=null;
	    String status=null;

	    int viewed=0;
	    int bounce_count=0;
	    int fwds=0;
	    int sent=0;
	    int clicks=0;
	    
	    //rate
	    JSONObject rate=null;
	    String open_rate=null;
	    String click_rate=null;
	    String click_per_view_rate=null;
	    String unique_click_rate=null;
	    
	    int linkcount=0;
	    int subscriber_count=0;
	    String Body=null;
	    String Sling_Campaign_Id=null;
	    String CreatedBy=null;
	    String List_Id=null;
	    String Sling_Subject=null;
	    String Type=null;
	    String subFunnelNodeName=null;
	    String subFunnelCounter=null;
	    String Current_Campaign=null;
	    String funnelNodeName=null;
	    String funnelCounter=null;
	    
	    //viewed_subscribers
	    JSONObject viewed_subscribers=null;
	    int total=0;
	    //Data
	    JSONArray data=null;
	    JSONObject dataJsonObj=null;
	    String data_campaignid=null;
	    String data_userid=null;
	    String data_viewed=null;
	    String data_email=null;
	    String data_uuid=null;
	    
	    //campaignclickstatistics
	    JSONArray campaignclickstatistics=null;
	    JSONObject campaignclickstatisticsJsonObj=null;
	    String campaignclickstatistics_firstclick=null;
		String campaignclickstatistics_latestclick=null;
		String campaignclickstatistics_clicked=null;
		
		//urlclickstatistics
		JSONArray urlclickstatistics=null;
		JSONObject urlclickstatisticsJsonObj=null;
		String urlclickstatistics_firstclick=null;
		String urlclickstatistics_latestclick=null;
		String urlclickstatistics_clicked=null;
		String urlclickstatistics_messageid=null;
		String urlclickstatistics_url=null;
	    
	    //linkcount viewed
	    //viewed_subscribers --data--  urlclickstatistics
	    
	    JSONArray campaignJsonArr=new JSONArray();
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(coll_name);
            //Bson andFilter=and(eq("Sling_Campaign_Id","499" ),gt("linkcount", 0), gt("viewed", 0));
            //Bson andFilter=and(gt("linkcount", 0), gt("viewed", 0));
            Bson andFilter=and(gte("linkcount", 0), gt("viewed", 0));
            FindIterable<Document> fi = collection.find(andFilter);
    		MongoCursor<Document> cursor = fi.iterator();
    		JSONObject campaignJsonObj=null;
    		try {
    			while(cursor.hasNext()) {
    				//campaignJsonArr.put(cursor.next().toJson());
    				campaignJsonObj=new JSONObject(cursor.next().toJson());
    				        //old_id
    					    id=campaignJsonObj.getString("id");
    					    uuid=campaignJsonObj.getString("uuid");
    					    subject=campaignJsonObj.getString("subject");
    					    sendstart=campaignJsonObj.getString("sendstart");
    					    status=campaignJsonObj.getString("status");

    					    viewed=campaignJsonObj.getInt("viewed");
    					    bounce_count=campaignJsonObj.getInt("bounce_count");
    					    fwds=campaignJsonObj.getInt("fwds");
    					    sent=campaignJsonObj.getInt("sent");
    					    clicks=campaignJsonObj.getInt("clicks");
    					    
    					    rate=campaignJsonObj.getJSONObject("rate");
	    					    open_rate=rate.getString("open_rate");
	    					    click_rate=rate.getString("open_rate");
	    					    click_per_view_rate=rate.getString("open_rate");
	    					    unique_click_rate=rate.getString("open_rate");
    					    
    					    linkcount=campaignJsonObj.getInt("linkcount");
    					    subscriber_count=campaignJsonObj.getInt("subscriber_count");
    					    
    					    Body=campaignJsonObj.getString("Body");
    					    Sling_Campaign_Id=campaignJsonObj.getString("Sling_Campaign_Id");
    					    CreatedBy=campaignJsonObj.getString("CreatedBy");
    					    List_Id=campaignJsonObj.getString("List_Id");
    					    Sling_Subject=campaignJsonObj.getString("Sling_Subject");
    					    Type=campaignJsonObj.getString("Type");
    					    subFunnelNodeName=campaignJsonObj.getString("subFunnelNodeName");
    					    subFunnelCounter=campaignJsonObj.getString("subFunnelCounter");
    					    Current_Campaign=campaignJsonObj.getString("Current_Campaign");
    					    funnelNodeName=campaignJsonObj.getString("funnelNodeName");
    					    funnelCounter=campaignJsonObj.getString("funnelCounter");  
    					    
    					    viewed_subscribers=campaignJsonObj.getJSONObject("viewed_subscribers");
    					    total=viewed_subscribers.getInt("total");
    					    
    					    data=viewed_subscribers.getJSONArray("data");
    					    
    					    for(int i=0;i<data.length();i++){
    					    	dataJsonObj=data.getJSONObject(i);
	    					    	data_campaignid=dataJsonObj.getString("campaignid"); 
	    					    	data_userid=dataJsonObj.getString("userid"); 
	    					    	data_viewed=dataJsonObj.getString("viewed"); 
	    					    	data_email=dataJsonObj.getString("email"); 
	    					    	data_uuid=dataJsonObj.getString("uuid"); 
	    					    	
	    					    	campaignclickstatistics=dataJsonObj.getJSONArray("campaignclickstatistics");
	    					    	      campaignclickstatisticsJsonObj=campaignclickstatistics.getJSONObject(0);
		    					    	      campaignclickstatistics_firstclick=campaignclickstatisticsJsonObj.getString("firstclick"); 
		    					    	      campaignclickstatistics_latestclick=campaignclickstatisticsJsonObj.getString("latestclick"); 
		    					    	      campaignclickstatistics_clicked=campaignclickstatisticsJsonObj.getString("clicked"); 
		    					    if(!campaignclickstatistics_firstclick.equals("null")){
		    					    	
		    					    	urlclickstatistics=dataJsonObj.getJSONArray("urlclickstatistics");
		    					    	for(int j=0;j<urlclickstatistics.length();j++){
		    					    		urlclickstatisticsJsonObj=urlclickstatistics.getJSONObject(j);
			    					    		urlclickstatistics_firstclick=urlclickstatisticsJsonObj.getString("firstclick"); 
			    					    		urlclickstatistics_latestclick=urlclickstatisticsJsonObj.getString("latestclick"); 
			    					    		urlclickstatistics_clicked=urlclickstatisticsJsonObj.getString("clicked");
			    					    		urlclickstatistics_messageid=urlclickstatisticsJsonObj.getString("messageid");
			    					    		urlclickstatistics_url=urlclickstatisticsJsonObj.getString("url");
			    					    		System.out.println("-----urlclickstatistics Found");
			    					    		System.out.println("UrL : "+urlclickstatistics_url);
			    					    		
			    					    		String strDate="27 Sep 2018 17:06";
			    					    		SimpleDateFormat dateFormatter=new SimpleDateFormat("dd MMM yyyy HH:mm"); 
			    					    		//Date date=dateFormatter.parse(strDate); 
			    					    		//System.out.println("Local Time: " + date);
			    					    		//date.setHours(date.getHours()-5);
			    					    		//date.setMinutes(date.getMinutes()-30);
			    					    		//System.out.println("Local Time: " + date);
			    					    		
			    					    		//funnelJsonObj=new JSONObject();
			    					    		funnelJsonDoc = new Document();
			    					    		funnelJsonDoc.put("id",id);
			    					    		funnelJsonDoc.put("uuid",uuid);
			    					    		funnelJsonDoc.put("subject",subject);
			    					    		funnelJsonDoc.put("sendstart",sendstart);
			    					    		funnelJsonDoc.put("sendstart_date",dateFormatter.parse(sendstart));
			    					    		funnelJsonDoc.put("status",status);

			    					    		funnelJsonDoc.put("viewed",viewed);
			    					    		funnelJsonDoc.put("bounce_count",bounce_count);
			    					    		funnelJsonDoc.put("fwds",fwds);
			    					    		funnelJsonDoc.put("sent",sent);
			    					    		funnelJsonDoc.put("clicks",clicks);

			    					    		funnelJsonDoc.put("open_rate",open_rate);
			    					    		funnelJsonDoc.put("click_rate",click_rate);
			    					    		funnelJsonDoc.put("click_per_view_rate",click_per_view_rate);
			    					    		funnelJsonDoc.put("unique_click_rate",unique_click_rate);

			    					    		funnelJsonDoc.put("linkcount",linkcount);
			    					    		funnelJsonDoc.put("subscriber_count",subscriber_count);

			    					    		funnelJsonDoc.put("Body",Body);
			    					    		funnelJsonDoc.put("Sling_Campaign_Id",Sling_Campaign_Id);
			    					    		funnelJsonDoc.put("CreatedBy",CreatedBy);
			    					    		funnelJsonDoc.put("List_Id",List_Id);
			    					    		funnelJsonDoc.put("Sling_Subject",Sling_Subject);
			    					    		funnelJsonDoc.put("Type",Type);

			    					    		funnelJsonDoc.put("subFunnelNodeName",subFunnelNodeName);
			    					    		funnelJsonDoc.put("subFunnelCounter",subFunnelCounter);
			    					    		funnelJsonDoc.put("Current_Campaign",Current_Campaign);
			    					    		funnelJsonDoc.put("funnelNodeName",funnelNodeName);
			    					    		funnelJsonDoc.put("funnelCounter",funnelCounter);

			    					    		funnelJsonDoc.put("subscriber_total",total);
			    					    		funnelJsonDoc.put("subscriber_campaignid",data_campaignid);
			    					    		funnelJsonDoc.put("subscriber_userid",data_userid);
			    					    		funnelJsonDoc.put("subscriber_viewed",data_viewed);
			    					    		funnelJsonDoc.put("subscriber_viewed_date",dateFormatter.parse(data_viewed));
			    					    		funnelJsonDoc.put("subscriber_email",data_email);
			    					    		funnelJsonDoc.put("subscriber_uuid",data_uuid);

			    					    		funnelJsonDoc.put("campaignclickstatistics_firstclick",campaignclickstatistics_firstclick);
			    					    		funnelJsonDoc.put("campaignclickstatistics_firstclick_date",dateFormatter.parse(campaignclickstatistics_firstclick));
			    					    		funnelJsonDoc.put("campaignclickstatistics_latestclick",campaignclickstatistics_latestclick);
			    					    		funnelJsonDoc.put("campaignclickstatistics_latestclick_date",dateFormatter.parse(campaignclickstatistics_latestclick));
			    					    		funnelJsonDoc.put("campaignclickstatistics_clicked",campaignclickstatistics_clicked);
			    					    		
			    					    		funnelJsonDoc.put("urlclickstatistics_firstclick",urlclickstatistics_firstclick);
			    					    		funnelJsonDoc.put("urlclickstatistics_firstclick_date",dateFormatter.parse(urlclickstatistics_firstclick));
			    					    		funnelJsonDoc.put("urlclickstatistics_latestclick",urlclickstatistics_latestclick);
			    					    		funnelJsonDoc.put("urlclickstatistics_latestclick_date",dateFormatter.parse(urlclickstatistics_latestclick));
			    					    		funnelJsonDoc.put("urlclickstatistics_clicked",urlclickstatistics_clicked);
			    					    		funnelJsonDoc.put("urlclickstatistics_messageid",urlclickstatistics_messageid);
			    					    		funnelJsonDoc.put("urlclickstatistics_url",urlclickstatistics_url);
			    					    		MongoDAO mdao=new MongoDAO();
			    								mdao.createOneUsingDocument("subscribers", funnelJsonDoc);
		    					    	}
		    					    	
		    					    }else{
	    					    		System.out.println("-----urlclickstatistics Not Found");
	    					    		
	    					    		String strDate="27 Sep 2018 17:06";
	    					    		SimpleDateFormat dateFormatter=new SimpleDateFormat("dd MMM yyyy HH:mm"); 
	    					    		//Date date=dateFormatter.parse(strDate); 
	    					    		//System.out.println("Local Time: " + date);
	    					    		//date.setHours(date.getHours()-5);
	    					    		//date.setMinutes(date.getMinutes()-30);
	    					    		//System.out.println("Local Time: " + date);
	    					    		
	    					    		//funnelJsonObj=new JSONObject();
	    					    		funnelJsonDoc = new Document();
	    					    		funnelJsonDoc.put("id",id);
	    					    		funnelJsonDoc.put("uuid",uuid);
	    					    		funnelJsonDoc.put("subject",subject);
	    					    		funnelJsonDoc.put("sendstart",sendstart);
	    					    		funnelJsonDoc.put("sendstart_date",dateFormatter.parse(sendstart));
	    					    		funnelJsonDoc.put("status",status);

	    					    		funnelJsonDoc.put("viewed",viewed);
	    					    		funnelJsonDoc.put("bounce_count",bounce_count);
	    					    		funnelJsonDoc.put("fwds",fwds);
	    					    		funnelJsonDoc.put("sent",sent);
	    					    		funnelJsonDoc.put("clicks",clicks);

	    					    		funnelJsonDoc.put("open_rate",open_rate);
	    					    		funnelJsonDoc.put("click_rate",click_rate);
	    					    		funnelJsonDoc.put("click_per_view_rate",click_per_view_rate);
	    					    		funnelJsonDoc.put("unique_click_rate",unique_click_rate);

	    					    		funnelJsonDoc.put("linkcount",linkcount);
	    					    		funnelJsonDoc.put("subscriber_count",subscriber_count);

	    					    		funnelJsonDoc.put("Body",Body);
	    					    		funnelJsonDoc.put("Sling_Campaign_Id",Sling_Campaign_Id);
	    					    		funnelJsonDoc.put("CreatedBy",CreatedBy);
	    					    		funnelJsonDoc.put("List_Id",List_Id);
	    					    		funnelJsonDoc.put("Sling_Subject",Sling_Subject);
	    					    		funnelJsonDoc.put("Type",Type);

	    					    		funnelJsonDoc.put("subFunnelNodeName",subFunnelNodeName);
	    					    		funnelJsonDoc.put("subFunnelCounter",subFunnelCounter);
	    					    		funnelJsonDoc.put("Current_Campaign",Current_Campaign);
	    					    		funnelJsonDoc.put("funnelNodeName",funnelNodeName);
	    					    		funnelJsonDoc.put("funnelCounter",funnelCounter);

	    					    		funnelJsonDoc.put("subscriber_total",total);
	    					    		funnelJsonDoc.put("subscriber_campaignid",data_campaignid);
	    					    		funnelJsonDoc.put("subscriber_userid",data_userid);
	    					    		funnelJsonDoc.put("subscriber_viewed",data_viewed);
	    					    		funnelJsonDoc.put("subscriber_viewed_date",dateFormatter.parse(data_viewed));
	    					    		funnelJsonDoc.put("subscriber_email",data_email);
	    					    		funnelJsonDoc.put("subscriber_uuid",data_uuid);

	    					    		funnelJsonDoc.put("campaignclickstatistics_firstclick",campaignclickstatistics_firstclick);
	    					    		funnelJsonDoc.put("campaignclickstatistics_firstclick_date",null);
	    					    		funnelJsonDoc.put("campaignclickstatistics_latestclick",campaignclickstatistics_latestclick);
	    					    		funnelJsonDoc.put("campaignclickstatistics_latestclick_date",null);
	    					    		funnelJsonDoc.put("campaignclickstatistics_clicked",campaignclickstatistics_clicked);
	    					    		
	    					    		funnelJsonDoc.put("urlclickstatistics_firstclick",null);
	    					    		funnelJsonDoc.put("urlclickstatistics_firstclick_date",null);
	    					    		funnelJsonDoc.put("urlclickstatistics_latestclick",null);
	    					    		funnelJsonDoc.put("urlclickstatistics_latestclick_date",null);
	    					    		funnelJsonDoc.put("urlclickstatistics_clicked",null);
	    					    		funnelJsonDoc.put("urlclickstatistics_messageid",null);
	    					    		funnelJsonDoc.put("urlclickstatistics_url",null);
	    					    		MongoDAO mdao=new MongoDAO();
	    								mdao.createOneUsingDocument("subscribers", funnelJsonDoc);
		    					    }
	        					    
    					    }
    					    
    					    
    				System.out.println(campaignJsonObj);
    			}
    		} finally {
    			cursor.close();
    		}
            System.out.println(campaignJsonArr.length());
                        
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return campaignJsonArr;
    }
	
	public  JSONArray findAllUrlByFilter(String coll_name) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	    JSONObject funnelJsonObj=null;
	    Document  funnelJsonDoc=null;
	    
	    String old_id=null;
	    String id=null;
	    String uuid=null;
	    String subject=null;
	    String sendstart=null;
	    String status=null;

	    int viewed=0;
	    int bounce_count=0;
	    int fwds=0;
	    int sent=0;
	    int clicks=0;
	    
	    //rate
	    JSONObject rate=null;
	    String open_rate=null;
	    String click_rate=null;
	    String click_per_view_rate=null;
	    String unique_click_rate=null;
	    
	    int linkcount=0;
	    int subscriber_count=0;
	    String Body=null;
	    String Sling_Campaign_Id=null;
	    String CreatedBy=null;
	    String List_Id=null;
	    String Sling_Subject=null;
	    String Type=null;
	    String subFunnelNodeName=null;
	    String subFunnelCounter=null;
	    String Current_Campaign=null;
	    String funnelNodeName=null;
	    String funnelCounter=null;
	    
	    //viewed_subscribers
	    JSONObject viewed_subscribers=null;
	    int total=0;
	    //Data
	    JSONArray data=null;
	    JSONObject dataJsonObj=null;
	    String data_campaignid=null;
	    String data_userid=null;
	    String data_viewed=null;
	    String data_email=null;
	    String data_uuid=null;
	    
	    //campaignclickstatistics
	    JSONArray campaignclickstatistics=null;
	    JSONObject campaignclickstatisticsJsonObj=null;
	    String campaignclickstatistics_firstclick=null;
		String campaignclickstatistics_latestclick=null;
		String campaignclickstatistics_clicked=null;
		
		//urlclickstatistics
		JSONArray urlclickstatistics=null;
		JSONObject urlclickstatisticsJsonObj=null;
		String urlclickstatistics_firstclick=null;
		String urlclickstatistics_latestclick=null;
		String urlclickstatistics_clicked=null;
		String urlclickstatistics_messageid=null;
		String urlclickstatistics_url=null;
	    
	    //linkcount viewed
	    //viewed_subscribers --data--  urlclickstatistics
	    
	    JSONArray campaignJsonArr=new JSONArray();
	    
	    JSONArray campaignJsonArr_Temp=new JSONArray();
	    JSONObject campaignJsonObj_Temp=null;
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(coll_name);
            //Bson andFilter=and(eq("Sling_Campaign_Id","499" ),gt("linkcount", 0), gt("viewed", 0));
            //Bson andFilter=and(gt("linkcount", 0), gt("viewed", 0));
            Bson andFilter=and(gte("linkcount", 0), gt("viewed", 0));
            FindIterable<Document> fi = collection.find(andFilter);
    		MongoCursor<Document> cursor = fi.iterator();
    		JSONObject campaignJsonObj=null;
    		//campaignJsonArr_Temp.p
    		try {
    			while(cursor.hasNext()) {
    				campaignJsonObj_Temp=new JSONObject();
    				//campaignJsonArr.put(cursor.next().toJson());
    				campaignJsonObj=new JSONObject(cursor.next().toJson());
    				        //old_id
    					    id=campaignJsonObj.getString("id");
    					    uuid=campaignJsonObj.getString("uuid");
    					    subject=campaignJsonObj.getString("subject");
    					    sendstart=campaignJsonObj.getString("sendstart");
    					    status=campaignJsonObj.getString("status");
    					    
    					    campaignJsonObj_Temp.put("id", id);
    					    campaignJsonObj_Temp.put("subject", subject);
    					    campaignJsonArr_Temp.put(campaignJsonObj_Temp);
    					    
    					    viewed=campaignJsonObj.getInt("viewed");
    					    bounce_count=campaignJsonObj.getInt("bounce_count");
    					    fwds=campaignJsonObj.getInt("fwds");
    					    sent=campaignJsonObj.getInt("sent");
    					    clicks=campaignJsonObj.getInt("clicks");
    					    
    					    rate=campaignJsonObj.getJSONObject("rate");
	    					    open_rate=rate.getString("open_rate");
	    					    click_rate=rate.getString("open_rate");
	    					    click_per_view_rate=rate.getString("open_rate");
	    					    unique_click_rate=rate.getString("open_rate");
    					    
    					    linkcount=campaignJsonObj.getInt("linkcount");
    					    subscriber_count=campaignJsonObj.getInt("subscriber_count");
    					    
    					    Body=campaignJsonObj.getString("Body");
    					    Sling_Campaign_Id=campaignJsonObj.getString("Sling_Campaign_Id");
    					    CreatedBy=campaignJsonObj.getString("CreatedBy");
    					    List_Id=campaignJsonObj.getString("List_Id");
    					    Sling_Subject=campaignJsonObj.getString("Sling_Subject");
    					    Type=campaignJsonObj.getString("Type");
    					    subFunnelNodeName=campaignJsonObj.getString("subFunnelNodeName");
    					    subFunnelCounter=campaignJsonObj.getString("subFunnelCounter");
    					    Current_Campaign=campaignJsonObj.getString("Current_Campaign");
    					    funnelNodeName=campaignJsonObj.getString("funnelNodeName");
    					    funnelCounter=campaignJsonObj.getString("funnelCounter");  
    					    
    					    viewed_subscribers=campaignJsonObj.getJSONObject("viewed_subscribers");
    					    total=viewed_subscribers.getInt("total");
    					    
    					    data=viewed_subscribers.getJSONArray("data");
    					    
    					    for(int i=0;i<data.length();i++){
    					    	dataJsonObj=data.getJSONObject(i);
	    					    	data_campaignid=dataJsonObj.getString("campaignid"); 
	    					    	data_userid=dataJsonObj.getString("userid"); 
	    					    	data_viewed=dataJsonObj.getString("viewed"); 
	    					    	data_email=dataJsonObj.getString("email"); 
	    					    	data_uuid=dataJsonObj.getString("uuid"); 
	    					    	
	    					    	campaignclickstatistics=dataJsonObj.getJSONArray("campaignclickstatistics");
	    					    	      campaignclickstatisticsJsonObj=campaignclickstatistics.getJSONObject(0);
		    					    	      campaignclickstatistics_firstclick=campaignclickstatisticsJsonObj.getString("firstclick"); 
		    					    	      campaignclickstatistics_latestclick=campaignclickstatisticsJsonObj.getString("latestclick"); 
		    					    	      campaignclickstatistics_clicked=campaignclickstatisticsJsonObj.getString("clicked"); 
		    					    if(!campaignclickstatistics_firstclick.equals("null")){
		    					    	
		    					    	urlclickstatistics=dataJsonObj.getJSONArray("urlclickstatistics");
		    					    	for(int j=0;j<urlclickstatistics.length();j++){
		    					    		urlclickstatisticsJsonObj=urlclickstatistics.getJSONObject(j);
			    					    		urlclickstatistics_firstclick=urlclickstatisticsJsonObj.getString("firstclick"); 
			    					    		urlclickstatistics_latestclick=urlclickstatisticsJsonObj.getString("latestclick"); 
			    					    		urlclickstatistics_clicked=urlclickstatisticsJsonObj.getString("clicked");
			    					    		urlclickstatistics_messageid=urlclickstatisticsJsonObj.getString("messageid");
			    					    		urlclickstatistics_url=urlclickstatisticsJsonObj.getString("url");
			    					    		System.out.println("-----urlclickstatistics Found");
			    					    		System.out.println("UrL : "+urlclickstatistics_url);
			    					    		
			    					    		String strDate="27 Sep 2018 17:06";
			    					    		SimpleDateFormat dateFormatter=new SimpleDateFormat("dd MMM yyyy HH:mm"); 
			    					    		//Date date=dateFormatter.parse(strDate); 
			    					    		//System.out.println("Local Time: " + date);
			    					    		//date.setHours(date.getHours()-5);
			    					    		//date.setMinutes(date.getMinutes()-30);
			    					    		//System.out.println("Local Time: " + date);
			    					    		
			    					    		//funnelJsonObj=new JSONObject();
			    					    		funnelJsonDoc = new Document();
			    					    		funnelJsonDoc.put("id",id);
			    					    		funnelJsonDoc.put("uuid",uuid);
			    					    		funnelJsonDoc.put("subject",subject);
			    					    		funnelJsonDoc.put("sendstart",sendstart);
			    					    		funnelJsonDoc.put("sendstart_date",dateFormatter.parse(sendstart));
			    					    		funnelJsonDoc.put("status",status);

			    					    		funnelJsonDoc.put("viewed",viewed);
			    					    		funnelJsonDoc.put("bounce_count",bounce_count);
			    					    		funnelJsonDoc.put("fwds",fwds);
			    					    		funnelJsonDoc.put("sent",sent);
			    					    		funnelJsonDoc.put("clicks",clicks);

			    					    		funnelJsonDoc.put("open_rate",open_rate);
			    					    		funnelJsonDoc.put("click_rate",click_rate);
			    					    		funnelJsonDoc.put("click_per_view_rate",click_per_view_rate);
			    					    		funnelJsonDoc.put("unique_click_rate",unique_click_rate);

			    					    		funnelJsonDoc.put("linkcount",linkcount);
			    					    		funnelJsonDoc.put("subscriber_count",subscriber_count);

			    					    		funnelJsonDoc.put("Body",Body);
			    					    		funnelJsonDoc.put("Sling_Campaign_Id",Sling_Campaign_Id);
			    					    		funnelJsonDoc.put("CreatedBy",CreatedBy);
			    					    		funnelJsonDoc.put("List_Id",List_Id);
			    					    		funnelJsonDoc.put("Sling_Subject",Sling_Subject);
			    					    		funnelJsonDoc.put("Type",Type);

			    					    		funnelJsonDoc.put("subFunnelNodeName",subFunnelNodeName);
			    					    		funnelJsonDoc.put("subFunnelCounter",subFunnelCounter);
			    					    		funnelJsonDoc.put("Current_Campaign",Current_Campaign);
			    					    		funnelJsonDoc.put("funnelNodeName",funnelNodeName);
			    					    		funnelJsonDoc.put("funnelCounter",funnelCounter);

			    					    		funnelJsonDoc.put("subscriber_total",total);
			    					    		funnelJsonDoc.put("subscriber_campaignid",data_campaignid);
			    					    		funnelJsonDoc.put("subscriber_userid",data_userid);
			    					    		funnelJsonDoc.put("subscriber_viewed",data_viewed);
			    					    		funnelJsonDoc.put("subscriber_viewed_date",dateFormatter.parse(data_viewed));
			    					    		funnelJsonDoc.put("subscriber_email",data_email);
			    					    		funnelJsonDoc.put("subscriber_uuid",data_uuid);

			    					    		funnelJsonDoc.put("campaignclickstatistics_firstclick",campaignclickstatistics_firstclick);
			    					    		funnelJsonDoc.put("campaignclickstatistics_firstclick_date",dateFormatter.parse(campaignclickstatistics_firstclick));
			    					    		funnelJsonDoc.put("campaignclickstatistics_latestclick",campaignclickstatistics_latestclick);
			    					    		funnelJsonDoc.put("campaignclickstatistics_latestclick_date",dateFormatter.parse(campaignclickstatistics_latestclick));
			    					    		funnelJsonDoc.put("campaignclickstatistics_clicked",campaignclickstatistics_clicked);
			    					    		
			    					    		funnelJsonDoc.put("urlclickstatistics_firstclick",urlclickstatistics_firstclick);
			    					    		funnelJsonDoc.put("urlclickstatistics_firstclick_date",dateFormatter.parse(urlclickstatistics_firstclick));
			    					    		funnelJsonDoc.put("urlclickstatistics_latestclick",urlclickstatistics_latestclick);
			    					    		funnelJsonDoc.put("urlclickstatistics_latestclick_date",dateFormatter.parse(urlclickstatistics_latestclick));
			    					    		funnelJsonDoc.put("urlclickstatistics_clicked",urlclickstatistics_clicked);
			    					    		funnelJsonDoc.put("urlclickstatistics_messageid",urlclickstatistics_messageid);
			    					    		funnelJsonDoc.put("urlclickstatistics_url",urlclickstatistics_url);
			    					    		MongoDAO mdao=new MongoDAO();
			    								mdao.createOneUsingDocument("url", funnelJsonDoc);
		    					    	}
		    					    	
		    					    }else{
		    					    	System.out.println("No urlclickstatistics Found");
		    					    	
		    					    	String strDate="27 Sep 2018 17:06";
	    					    		SimpleDateFormat dateFormatter=new SimpleDateFormat("dd MMM yyyy HH:mm"); 
	    					    		//Date date=dateFormatter.parse(strDate); 
	    					    		//System.out.println("Local Time: " + date);
	    					    		//date.setHours(date.getHours()-5);
	    					    		//date.setMinutes(date.getMinutes()-30);
	    					    		//System.out.println("Local Time: " + date);
	    					    		
	    					    		//funnelJsonObj=new JSONObject();
	    					    		funnelJsonDoc = new Document();
	    					    		funnelJsonDoc.put("id",id);
	    					    		funnelJsonDoc.put("uuid",uuid);
	    					    		funnelJsonDoc.put("subject",subject);
	    					    		funnelJsonDoc.put("sendstart",sendstart);
	    					    		funnelJsonDoc.put("sendstart_date",dateFormatter.parse(sendstart));
	    					    		funnelJsonDoc.put("status",status);

	    					    		funnelJsonDoc.put("viewed",viewed);
	    					    		funnelJsonDoc.put("bounce_count",bounce_count);
	    					    		funnelJsonDoc.put("fwds",fwds);
	    					    		funnelJsonDoc.put("sent",sent);
	    					    		funnelJsonDoc.put("clicks",clicks);

	    					    		funnelJsonDoc.put("open_rate",open_rate);
	    					    		funnelJsonDoc.put("click_rate",click_rate);
	    					    		funnelJsonDoc.put("click_per_view_rate",click_per_view_rate);
	    					    		funnelJsonDoc.put("unique_click_rate",unique_click_rate);

	    					    		funnelJsonDoc.put("linkcount",linkcount);
	    					    		funnelJsonDoc.put("subscriber_count",subscriber_count);

	    					    		funnelJsonDoc.put("Body",Body);
	    					    		funnelJsonDoc.put("Sling_Campaign_Id",Sling_Campaign_Id);
	    					    		funnelJsonDoc.put("CreatedBy",CreatedBy);
	    					    		funnelJsonDoc.put("List_Id",List_Id);
	    					    		funnelJsonDoc.put("Sling_Subject",Sling_Subject);
	    					    		funnelJsonDoc.put("Type",Type);

	    					    		funnelJsonDoc.put("subFunnelNodeName",subFunnelNodeName);
	    					    		funnelJsonDoc.put("subFunnelCounter",subFunnelCounter);
	    					    		funnelJsonDoc.put("Current_Campaign",Current_Campaign);
	    					    		funnelJsonDoc.put("funnelNodeName",funnelNodeName);
	    					    		funnelJsonDoc.put("funnelCounter",funnelCounter);

	    					    		funnelJsonDoc.put("subscriber_total",total);
	    					    		funnelJsonDoc.put("subscriber_campaignid",data_campaignid);
	    					    		funnelJsonDoc.put("subscriber_userid",data_userid);
	    					    		funnelJsonDoc.put("subscriber_viewed",data_viewed);
	    					    		funnelJsonDoc.put("subscriber_viewed_date",dateFormatter.parse(data_viewed));
	    					    		funnelJsonDoc.put("subscriber_email",data_email);
	    					    		funnelJsonDoc.put("subscriber_uuid",data_uuid);

	    					    		funnelJsonDoc.put("campaignclickstatistics_firstclick",campaignclickstatistics_firstclick);
	    					    		funnelJsonDoc.put("campaignclickstatistics_firstclick_date",null);
	    					    		funnelJsonDoc.put("campaignclickstatistics_latestclick",campaignclickstatistics_latestclick);
	    					    		funnelJsonDoc.put("campaignclickstatistics_latestclick_date",null);
	    					    		funnelJsonDoc.put("campaignclickstatistics_clicked",campaignclickstatistics_clicked);
	    					    		
	    					    		funnelJsonDoc.put("urlclickstatistics_firstclick",null);
	    					    		funnelJsonDoc.put("urlclickstatistics_firstclick_date",null);
	    					    		funnelJsonDoc.put("urlclickstatistics_latestclick",null);
	    					    		funnelJsonDoc.put("urlclickstatistics_latestclick_date",null);
	    					    		funnelJsonDoc.put("urlclickstatistics_clicked",null);
	    					    		funnelJsonDoc.put("urlclickstatistics_messageid",null);
	    					    		funnelJsonDoc.put("urlclickstatistics_url",null);
	    					    		MongoDAO mdao=new MongoDAO();
	    								mdao.createOneUsingDocument("url", funnelJsonDoc);
		    					    }
	        					    
    					    }
    					    
    					    
    				System.out.println(campaignJsonObj);
    			}
    		} finally {
    			cursor.close();
    		}
            System.out.println(campaignJsonArr.length());
                        
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        //return campaignJsonArr;
	    return campaignJsonArr_Temp;
    }	
	public JSONArray findUrlClickStatisticsBasedOnCampaignIDAndUserID(String coll_name,String Campaign_Id,String User_Id) {
		MongoClient mongoClient = null;
		DB db =null;
		DBCollection collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=null;
	    JSONArray campaignTempJsonArr=null;
	    
	    try {
	    	mongoClient = ConnectionHelper.getConn();
	        db = mongoClient.getDB("phplisttest");
	        collection = db.getCollection("funnel");

	        DBObject unwind = new BasicDBObject("$unwind", "$viewed_subscribers.data");
	        DBObject match = new BasicDBObject("$match", new BasicDBObject(
	                "viewed_subscribers.data.userid", User_Id));
	        DBObject project = new BasicDBObject("$project", new BasicDBObject(
	                "_id", 0).append("viewed_subscribers.data.urlclickstatistics", 1));
            List<DBObject> pipeline = Arrays.asList(unwind, match, project);
	        AggregationOutput output = collection.aggregate(pipeline);
            Iterable<DBObject> results = output.results();
            campaignJsonArr=new JSONArray();
	        for (DBObject result : results) {
	        	BSONObject urlclickstatistics=(BSONObject) ((BSONObject) ((BSONObject) result.get("viewed_subscribers")).get("data")).get("urlclickstatistics");
	        	campaignTempJsonArr=new JSONArray(urlclickstatistics.toString());
	        	//campaignJsonArr.put(urlclickstatistics.toString());
	        	for(int i=0;i<campaignTempJsonArr.length();i++){
	        		campaignJsonArr.put(campaignTempJsonArr.get(i));
	        	}
	        	System.out.println(campaignTempJsonArr.length());
	    	}
	        System.out.println(campaignJsonArr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConn(mongoClient);
		}
        return campaignJsonArr;
    }
	public JSONArray findUrlClickStatisticsBasedOnCampaignIDAndUserIDOld(String coll_name,String Campaign_Id,String User_Id) {
		MongoClient mongoClient = null;
		DB db =null;
		DBCollection collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=null;
	    
	    try {
	    	mongoClient = ConnectionHelper.getConn();
	        db = mongoClient.getDB("phplisttest");
	        collection = db.getCollection("funnel");

	        DBObject unwind = new BasicDBObject("$unwind", "$viewed_subscribers.data");
	        DBObject match = new BasicDBObject("$match", new BasicDBObject(
	                "viewed_subscribers.data.userid", User_Id));
	        DBObject project = new BasicDBObject("$project", new BasicDBObject(
	                "_id", 0).append("viewed_subscribers.data.urlclickstatistics", 1));
            List<DBObject> pipeline = Arrays.asList(unwind, match, project);
	        AggregationOutput output = collection.aggregate(pipeline);
            Iterable<DBObject> results = output.results();
	        for (DBObject result : results) {
	        	BSONObject urlclickstatistics=(BSONObject) ((BSONObject) ((BSONObject) result.get("viewed_subscribers")).get("data")).get("urlclickstatistics");
	        	campaignJsonArr=new JSONArray(urlclickstatistics.toString());
	        	System.out.println(campaignJsonArr.length());
	    	}
	        System.out.println(campaignJsonArr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConn(mongoClient);
		}
        return campaignJsonArr;
    }
	
	public void dropCollection(String collectionName) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=new JSONArray();
	    
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(collectionName);
            collection.drop();
            // System.out.println(myDoc.toJson());
                        
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
    
	}
	
	public JSONArray findCampaign(String coll_name,String funnel_id) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=new JSONArray();
	    
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(coll_name);
            Bson filter1 =eq("funnelNodeName", funnel_id);
            FindIterable<Document> fi = collection.find(filter1);
    		MongoCursor<Document> cursor = fi.iterator();
    		try {
    			while(cursor.hasNext()) {
    				campaignJsonArr.put(cursor.next().toJson());
    			}
    		} finally {
    			cursor.close();
    		}
           // System.out.println(myDoc.toJson());
                        
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return campaignJsonArr;
    }
	
	public JSONArray findUrlClickStatisticsBasedOnCampaignIDAndUserID_old(String coll_name,String Campaign_Id,String User_Id) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=new JSONArray();
	    
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(coll_name);
            Bson filter1 =eq("Sling_Campaign_Id", Campaign_Id);
            FindIterable<Document> fi = collection.find(filter1);
    		MongoCursor<Document> cursor = fi.iterator();
    		try {
    			while(cursor.hasNext()) {
    				campaignJsonArr.put(cursor.next().toJson());
    			}
    		} finally {
    			cursor.close();
    		}
           // System.out.println(myDoc.toJson());
                        
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return campaignJsonArr;
    }
	public JSONArray findCampaignBasedOnCampaignID(String coll_name,String Sling_Campaign_Id) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=new JSONArray();
	    
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(coll_name);
            Bson filter1 =eq("Sling_Campaign_Id", Sling_Campaign_Id);
            FindIterable<Document> fi = collection.find(filter1);
    		MongoCursor<Document> cursor = fi.iterator();
    		try {
    			while(cursor.hasNext()) {
    				campaignJsonArr.put(cursor.next().toJson());
    			}
    		} finally {
    			cursor.close();
    		}
           // System.out.println(myDoc.toJson());
                        
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return campaignJsonArr;
    }
	
	public JSONArray findCampaignBasedOnFunnelAndSubFunnelName(String coll_name,String funnel_name,String subfunnel_name) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=new JSONArray();
	    
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(coll_name);
            Bson filter1 = and(eq("funnelNodeName", funnel_name),
                    eq("subFunnelNodeName", subfunnel_name));
            FindIterable<Document> fi = collection.find(filter1);
    		MongoCursor<Document> cursor = fi.iterator();
    		try {
    			while(cursor.hasNext()) {
    				campaignJsonArr.put(cursor.next().toJson());
    			}
    		} finally {
    			cursor.close();
    		}
           // System.out.println(myDoc.toJson());
                        
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return campaignJsonArr;
    }
	
	public JSONArray distinctSortSubFunnelTest(String funnelName){
	        
			MongoClient mongoClient = null;
		    MongoDatabase database  = null;
		    DBCollection collection = null;
		    JSONArray funnelListJsonArr=new JSONArray();
	        try {
		        mongoClient=ConnectionHelper.getConnection();
		        database=mongoClient.getDatabase("phplisttest");
		        collection=(DBCollection) database.getCollection("funnel");
		        //Bson filter1 =eq("funnelNodeName", funnelName);
		        //Bson filter1 =eq("funnelNodeName", funnelName);
		       // collection.distinct("funnel_name").sort(new BasicDBObject("funnel_id",1));;
		        
		    } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException(e);
			} finally {
				ConnectionHelper.closeConnection(mongoClient);
			}
	        return funnelListJsonArr;
	}
	
	public JSONArray distinctSortSubFunnel(String funnelName){
        
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    JSONArray funnelListJsonArr=new JSONArray();
        try {
	        mongoClient=ConnectionHelper.getConnection();
	        database=mongoClient.getDatabase("phplisttest");
	        collection=database.getCollection("funnel");
	        //Bson filter1 =eq("funnelNodeName", funnelName);
	        
			
            FindIterable<Document> it = collection.find(eq("funnelNodeName", funnelName)).sort(new Document("Counter",1));
	        ArrayList<Document> docs = new ArrayList<Document>();
	            it.into(docs);
            for (Document doc : docs) {
	        	funnelListJsonArr.put(doc.getString("subFunnelNodeName"));
	            System.out.println(doc);
	        }
	        System.out.println("funnelListJsonArr : "+funnelListJsonArr);
	    } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return funnelListJsonArr;
    }
	
	
	public JSONArray distinctSubFunnel(String funnelName){
	        
			MongoClient mongoClient = null;
		    MongoDatabase database  = null;
		    MongoCollection<Document> collection = null;
		    JSONArray funnelListJsonArr=new JSONArray();
	        try {
		        mongoClient=ConnectionHelper.getConnection();
		        database=mongoClient.getDatabase("phplisttest");
		        collection=database.getCollection("funnel");
		        Bson filter1 =eq("funnelNodeName", funnelName);
		        DistinctIterable<String> fi = collection.distinct("subFunnelNodeName",filter1,String.class);
		        MongoCursor<String> cursor = fi.iterator();
		        try {
					while(cursor.hasNext()) {
						funnelListJsonArr.put(cursor.next());
					}
				} finally {
					cursor.close();
				}
				System.out.println("funnelListJsonArr : "+funnelListJsonArr);
							
		    } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException(e);
			} finally {
				ConnectionHelper.closeConnection(mongoClient);
			}
	        return funnelListJsonArr;
    }
	
	public JSONArray distinctFunnel(){
        
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    JSONArray funnelListJsonArr=new JSONArray();
        try {
	        mongoClient=ConnectionHelper.getConnection();
	        database=mongoClient.getDatabase("phplisttest");
	        collection=database.getCollection("funnel");
	        DistinctIterable<String> fi = collection.distinct("funnelNodeName", String.class);
			MongoCursor<String> cursor = fi.iterator();
			try {
				while(cursor.hasNext()) {
					funnelListJsonArr.put(cursor.next());
				}
			} finally {
				cursor.close();
			}
			System.out.println("funnelListJsonArr : "+funnelListJsonArr);
	    } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return funnelListJsonArr;
    }
	
	public String findByFunnelAndSubfunnelNameAndCampignId(String funnelName,String subFunnelName,String campaignId) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	    String status=null;
	     try {
        	 mongoClient=ConnectionHelper.getConnection();
             database=mongoClient.getDatabase("phplisttest");
             collection=database.getCollection("funnel");
             Bson filter1 =eq("funnelName", funnelName);
             
             Bson filter2 = and(eq("funnelName", funnelName),
                     eq("subFunnelName", subFunnelName));
             
             Bson filter3 = and(eq("funnelName", funnelName),
                     eq("subFunnelName", subFunnelName),
                     eq("campaignId", campaignId));
             MongoCursor<Document> iterator1 = collection.find(filter1).iterator();
             BasicDBList list1 = new BasicDBList();
             while (iterator1.hasNext()) {
                Document doc = iterator1.next();
                list1.add(doc);
             }
             if(list1.isEmpty()){
            	 status="false";
            	 //return status;
             }else{
            	 //System.out.println(JSON.serialize(list));
            	 MongoCursor<Document> iterator2 = collection.find(filter2).iterator();
                 BasicDBList list2 = new BasicDBList();
                 while (iterator2.hasNext()) {
                    Document doc = iterator2.next();
                    list2.add(doc);
                 }
                 if(list2.isEmpty()){
                	 status="false";
                	 //return status;
                  }else{
                	  MongoCursor<Document> iterator3 = collection.find(filter3).iterator();
                      BasicDBList list3 = new BasicDBList();
                      while (iterator3.hasNext()) {
                         Document doc = iterator3.next();
                         list3.add(doc);
                      }
                      if(list2.isEmpty()){
                    	 status="false";
                     	 //return status;
                      }else{
                    	 status="true";
                     	 //return status;
                      }
                  	
                  }
             }
             
            } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return status;
    }
	
	public Document findByFunnelAndSubfunnelName(String funnelName,String subFunnelName,String campaignId) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	     try {
        	 mongoClient=ConnectionHelper.getConnection();
             database=mongoClient.getDatabase("phplisttest");
             collection=database.getCollection("funnel");
             Bson filter = and(eq("funnelName", funnelName),
                  eq("subFunnelName", subFunnelName),
                  eq("campaignId", campaignId));
             MongoCursor<Document> iterator = collection.find(filter).iterator();
             BasicDBList list = new BasicDBList();
             while (iterator.hasNext()) {
                Document doc = iterator.next();
                list.add(doc);
             }
             if(list.isEmpty()){
            	 System.out.println("No data found");
             }else{
            	 System.out.println(JSON.serialize(list));
             }
             
            
            
            
            /*
             * 
             * 
             *  
             BasicDBObject andQuery = new BasicDBObject();
        	 List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
        	 obj.add(new BasicDBObject("funnel_id", "193"));
        	 obj.add(new BasicDBObject("funnelName", "RealState"));
        	 andQuery.put("$and", obj);
             System.out.println(andQuery.toString());
             * Block<Document> printBlock = new Block<Document>() {
				     @Override
				     public void apply(final Document document) {
				         System.out.println(document.toJson());
				     }
				};
				collection.find(and(gt("i", 50), lte("i", 100))).forEach(printBlock);
             */
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return myDoc;
    }
	
	
	public Document findById(String coll_name,String id) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	   
	    
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("webapp");
            collection=database.getCollection(coll_name);
            myDoc = collection.find(eq("_id", id)).first();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return myDoc;
    }
	
	public Document findByName(String coll_name,String fieldName,String fieldValue) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	   
	    
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(coll_name);
            myDoc = collection.find(eq(fieldName, fieldValue)).first();
            System.out.println(myDoc.toJson());
            /*
             * 
             * Block<Document> printBlock = new Block<Document>() {
				     @Override
				     public void apply(final Document document) {
				         System.out.println(document.toJson());
				     }
				};
				collection.find(and(gt("i", 50), lte("i", 100))).forEach(printBlock);
             */
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return myDoc;
    }
	public String createOneUsingDocumentForDoc(String coll_name,Document doc,SlingHttpServletResponse response){
		//json.get(key)
		
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    //Document doc=Document.parse(json.toString());
	    //System.out.println("doc : "+doc);
	    String str="Step 1";
	    PrintWriter out=null;		
        try {
        	out = response.getWriter();
        	out.println("str : "+str);
        	mongoClient=ConnectionHelper.getConnection();
        	str="Step 2";
        	out.println("str : "+str);
            database=mongoClient.getDatabase("phplisttest");
            str="Step 3";
            out.println("str : "+str);
            collection=database.getCollection(coll_name);
            str="Step 4";
            out.println("str : "+str);
            
            //collection.insertOne(doc, new InsertOneOptions());
            str="Step 5";
            out.println("str : "+str);
            //collection.insertMany(doc);
            str="Step 6";
            out.println("str : "+str);
            out.println("str : "+doc);
            collection.insertOne(doc);
           } catch (Exception e) {
                e.printStackTrace();
                out.println("str : "+e.getMessage()+"\t"+e.getStackTrace());
                //throw new RuntimeException(e);
			} finally {
				ConnectionHelper.closeConnection(mongoClient);
			}
        // return doc.getObjectId("_id").toString();
        return str;
    }
	
	public String createOneUsingDocument(String coll_name,Document doc){
		//json.get(key)
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    //Document doc=Document.parse(json.toString());
	    //System.out.println("doc : "+doc);
	    String str="Step 1";
	    		
        try {
        	mongoClient=ConnectionHelper.getConnection();
        	str="Step 2";
            database=mongoClient.getDatabase("phplisttest");
            str="Step 3";
            collection=database.getCollection(coll_name);
            str="Step 4";
            collection.insertOne(doc);
            str="Step 5";
           } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        // return doc.getObjectId("_id").toString();
        return str;
    }
	
	public String createOne(String coll_name,JSONObject json){
		//json.get(key)
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document doc=Document.parse(json.toString());
	    //System.out.println("doc : "+doc);
	    //DBObject dbObject = (DBObject)JSON.parse(json);
	    		
        try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(coll_name);
            collection.insertOne(doc);
           } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
         return doc.getObjectId("_id").toString();
    }
	public String createOneFunnel(String coll_name,Document doc){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
        try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(coll_name);
            collection.insertOne(doc);
           } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
         return doc.getObjectId("_id").toString();
    }
	public List<Document> createMany(String coll_name,List<Document> documents){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
        try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("webapp");
            collection=database.getCollection(coll_name);
            collection.insertMany(documents);
          } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return documents;
    }
	
	public long update(String coll_name,String fieldName,String fieldValue,String id) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    UpdateResult updateResult=null;
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("webapp");
            collection=database.getCollection(coll_name);
            if(id!=null){
                 updateResult=collection.updateOne(eq("_id", new ObjectId(id)), new Document("$set", new Document(fieldName, fieldValue)));
            }else{
            	updateResult=collection.updateOne(eq(fieldName, fieldValue), new Document("$set", new Document(fieldName, fieldValue)));
            }
          } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return updateResult.getModifiedCount();
    }
	
	public long updateNested(String coll_name,String fieldName,String fieldValue,String fieldUpdateValue) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    UpdateResult updateResult=null;
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("webapp");
            collection=database.getCollection(coll_name);
            if(fieldUpdateValue!=null){
                updateResult=collection.updateOne(eq(fieldName.replace(".$", ""), fieldValue), new Document("$set", new Document(fieldName, fieldUpdateValue)));
            }else{
              //updateResult=collection.updateOne(eq("permissions.force_web_traffic.port", "2233"), new Document("$set", new Document("permissions.$.force_web_traffic.ip_address", "101.55.66.33")));
            	updateResult=collection.updateOne(eq(fieldName.replace(".$", ""), fieldValue), new Document("$set", new Document(fieldName, fieldUpdateValue)));
            }
           } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return updateResult.getModifiedCount();
    }
	
	public long updateDoc(String coll_name,Document doc,String id) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    UpdateResult updateResult=null;
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("webapp");
            collection=database.getCollection(coll_name);
            if(id!=null){
                 updateResult=collection.updateOne(eq("_id", new ObjectId(id)), new Document("$set",doc));
            }else{
            
            }
          } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return updateResult.getModifiedCount();
    }
	public long  remove(String coll_name,String id) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    DeleteResult deleteResult=null;
	    
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("webapp");
            collection=database.getCollection(coll_name);
            deleteResult=collection.deleteOne(eq("_id", new ObjectId(id)));
            
            //DeleteResult deleteResult = collection.deleteMany(gte("i", 100));
            //System.out.println(deleteResult.getDeletedCount());
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return deleteResult.getDeletedCount();
    }

     protected void processRow(MongoCursor<Document> cursor) throws Exception {
    	try {
    	    while (cursor.hasNext()) {
    	        System.out.println(cursor.next().toJson());
    	    }
    	} finally {
    	    cursor.close();
    	}
    	/*
    	// Getting the iterable object 
        FindIterable<Document> iterDoc = collection.find(); 
        int i = 1; 

        // Getting the iterator 
        Iterator it = iterDoc.iterator(); 
      
        while (it.hasNext()) {  
           System.out.println(it.next());  
        i++; 
        }
        */
        //return wine;
    }
     public MongoCursor<Document> findAll(String coll_name) {
 		MongoClient mongoClient = null;
 	    MongoDatabase database  = null;
 	    MongoCollection<Document> collection = null;
 	    MongoCursor<Document> cursor=null;
 	   
 	    try {
         	mongoClient=ConnectionHelper.getConnection();
             database=mongoClient.getDatabase("webapp");
             collection=database.getCollection(coll_name);
             cursor = collection.find().iterator();
             
         } catch (Exception e) {
             e.printStackTrace();
             throw new RuntimeException(e);
 		} finally {
 			ConnectionHelper.closeConnection(mongoClient);
 		}
         return cursor;
     }
     public Document findFirst(String coll_name) {
 		MongoClient mongoClient = null;
 	    MongoDatabase database  = null;
 	    MongoCollection<Document> collection = null;
 	    Document myDoc=null;
 	    try {
         	mongoClient=ConnectionHelper.getConnection();
             database=mongoClient.getDatabase("webapp");
             collection=database.getCollection(coll_name);
             myDoc = collection.find().first();
             
         } catch (Exception e) {
             e.printStackTrace();
             throw new RuntimeException(e);
 		} finally {
 			ConnectionHelper.closeConnection(mongoClient);
 		}
         return myDoc;
     }
     
     public static int deleteById(MongoCollection<Document> col, Object id) {
    	    int count = 0;
    	    Bson filter = Filters.eq("_id", id);
    	    DeleteResult deleteResult = col.deleteOne(filter);
    	    count = (int) deleteResult.getDeletedCount();
    	    return count;
    	}
    	 
     
     public static Document updateById(MongoCollection<Document> col, Object id, Document newDoc,
    	        boolean repalce) {
    	    Bson filter = Filters.eq("_id", id);
    	    if (repalce)
    	        col.replaceOne(filter, newDoc);
    	    else
    	        col.updateOne(filter, new Document("$set", newDoc));
    	    return newDoc;
    	}
     
     /*
     @SuppressWarnings("unused")
	private Bson filter(String field, String value) {
         if (isBlank(value)) {
             return null;
         }
         if (isRegex(value)) {
             return Filters.regex(field, value.replaceAll("\\*", "\\.\\*"));
         }
         return Filters.eq(field, value);
     }
     
     private boolean isBlank(String str) {
         return str == null || str.isEmpty();
     }

     private boolean isRegex(String str) {
         return str.contains(".") || str.contains("*");
     }
     */
     /**
      * @param name
      * @param value
      * @return
      */
     /**
      * Gets a DbFilter object from a key object
      *
      * @param c     The class
      * @param index The index of the key
      * @param value The object of the key
      * @return This
      */
	
}
