package leadconverter.mongo;

import static com.mongodb.client.model.Filters.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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



public class Test {
	
	
	public static JSONArray crudOnDate(String coll_name,String url,String funnel_id) {
		MongoClient mongoClient = null;
		DB db =null;
		DBCollection collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=null;
	    String array_names[] = {"John","Tim","Brit","Robin","Smith","Lora","Jennifer","Lyla","Victor","Adam"};
	     
	    String array_address[][] ={
	        {"US", "FL", " Miami"},
	        {"US", "FL", " Orlando"},
	        {"US", "CA", "San Diego"},
	        {"US", "FL", " Orlando"},
	        {"US", "FL", " Orlando"},
	        {"US", "NY", "New York"},
	        {"US", "NY", "Buffalo"},
	        {"US", "TX", " Houston"},
	        {"US", "CA", "San Diego"},
	        {"US", "TX", " Houston"}
	    };
	    
	    try {
	    	mongoClient = ConnectionHelper.getConn();
	        db = mongoClient.getDB("phplisttest");
	        collection = db.getCollection("funnel");
	        DBObject unwind = new BasicDBObject("$unwind", "$viewed_subscribers.data");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConn(mongoClient);
		}
        return campaignJsonArr;

    }
	
	public static JSONArray subscribersViewBasedOnFunnelDataOld(String coll_name,String funnel_name,String sub_funnel_name,String start_date,String end_date) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	    //subscribers
	    
	    JSONArray campaignJsonArr=new JSONArray();
	    SimpleDateFormat dateFormatter=new SimpleDateFormat("dd MMM yyyy HH:mm"); 
		
	    
	    try {
	    	Date date0=dateFormatter.parse("24 Oct 2018 07:43"); 
	    	System.out.println("date0 : "+date0);
	    	//date0.setHours(date0.getHours()+5);
	    	//date0.setMinutes(date0.getMinutes()+30);
	    	System.out.println("date0 : "+date0);
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(coll_name);
            Bson datefilter =and(gte("sendstart_date", dateFormatter.parse(start_date)),lte("sendstart_date", dateFormatter.parse(end_date)));
            //Bson datefilter =lte("sendstart_date", date0);
            //Bson filter1 =and(eq("funnelNodeName", funnel_name),eq("subFunnelNodeName", sub_funnel_name),datefilter);
            Bson filter1 =and(eq("funnelNodeName", funnel_name),eq("subFunnelNodeName", sub_funnel_name));
            FindIterable<Document> fi = collection.find(filter1);
    		MongoCursor<Document> cursor = fi.iterator();
    		try {
    			while(cursor.hasNext()) {
    				Document doc=cursor.next();
    				/*
    				Document viewed_subscribers_doc=(Document) doc.get("viewed_subscribers");
    				List<Document> data_arr_doc=(List<Document>) viewed_subscribers_doc.get("data");
    				System.out.println(data_arr_doc);
    				Document data_obj_doc=null;
    				
    				for(int i=0;i<data_arr_doc.size();i++){
    					 data_obj_doc=data_arr_doc.get(i);
    					 campaignJsonArr.put(data_obj_doc);
    				}
    				*/
    				
    				campaignJsonArr.put(doc.getString("subscriber_email"));
    			    
    			}
    		} finally {
    			cursor.close();
    		}
    		System.out.println(campaignJsonArr.length());
            System.out.println(campaignJsonArr);
                        
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
			/*
			 * 
			 * CampaignId  : id
			Sling_Subject : Sling_Subject
			Url  : urlclickstatistics_url
			FirstClick : campaignclickstatistics_firstclick             urlclickstatistics_firstclick
			LastClick  : campaignclickstatistics_latestclick           urlclickstatistics_latestclick
			Click      : clicks  campaignclickstatistics_clicked         urlclickstatistics_clicked
			click count  : clicks
			open count   : viewed
			open rate  : open_rate
			click rate  : click_rate
			
			subscriber_email
			*/
		}
        return campaignJsonArr;
    }
	public static JSONArray subscribersViewBasedOnFunnelData(String coll_name,String funnel_name,String sub_funnel_name,String start_date,String end_date) {
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
	
	public static JSONArray subscribersViewBasedOnSubFunnelData(String coll_name,String funnel_name,String sub_funnel_name,String start_date,String end_date) {
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
										if(urlclickstatistics_url==null || urlclickstatistics_url.equals("null")){
											urlclickstatistics_url="null";
										}
										//++subscriber_click_count;
										availableUrlJsonObj=new JSONObject();
										availableUrlJsonObj.put("funnelNodeName",funnelNodeName);
						                availableUrlJsonObj.put("subFunnelNodeName",subFunnelNodeName);
						                availableUrlJsonObj.put("subscriber_email",subscriber_email_new);
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
	
	public static JSONArray findUrlClickStatisticsBasedOnCampaignIDAndUserIDTest(String coll_name,String Campaign_Id,String User_Id) {
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

	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub
		MongoDAO mdao=new MongoDAO();
		Document campaign_info = new Document("funnel_id", 3);
		campaign_info.append("funnel_name", "RealState");
         //findAllUrlByFilter("funnel");
		//findAllUrlByBasicDBObject("funnel","http://carrotrule.com/","448");
		//findUrlClickStatisticsBasedOnCampaignIDAndUserID("","","2084");
		//mdao.findAllSubscriberByFilter("funnel");
		
		//mdao.findAllUrlByFilter("funnel");
		//mdao.findAllSubscriberByFilter("funnel");
		//mdao.findUrlClickStatisticsBasedOnCampaignIDAndUserID("","","2084");
		//subscribersViewBasedOnFunnelData("subscribers","1","Inform","24 Oct 2018 06:43","24 Oct 2018 09:43");
		//subscribersViewBasedOnSubFunnelData("subscribers","2","Inform","24 Oct 2018 06:43","24 Oct 2018 09:43");
		//subscribersViewBasedOnFunnelData("date_funnel","1","Inform","24 Oct 2018 06:43","24 Oct 2018 09:43");
		//mdao.distinctSortSubFunnelTest("");
		findUrlClickStatisticsBasedOnCampaignIDAndUserIDTest("funnel","499","2084");//7:43+30 73  6985 2084 2674
		//mdao.findUrlClickStatisticsBasedOnCampaignIDAndUserID("funnel","499","2084");//7:43+30 73  6985 2084 2674
		//mdao.distinctSubFunnel("2");
		//mdao.dropCollection("funnel");
		//String response=mdao.createOneFunnel("funnel",campaign_info);
		//String response=mdao.findByFunnelAndSubfunnelNameAndCampignId("funnel", "funnel_id","193");
		//System.out.println("response: "+response);
		JSONObject CampignDetailsJsonObj=new JSONObject();
		CampignDetailsJsonObj.put("name", "Akh");
		CampignDetailsJsonObj.put("name1", "Akh1");
		CampignDetailsJsonObj.put("name2", "Akh2");
		
		JSONObject CampignDetailsJsonObj1=new JSONObject();
		CampignDetailsJsonObj1.put("name3", "Akh3");
		CampignDetailsJsonObj1.put("name4", "Akh4");
		CampignDetailsJsonObj1.put("name5", "Akh5");
		
		//Document doc1=new Document();
	   // doc1.parse(CampignDetailsJsonObj.toString());
	   // System.out.println("doc1: "+doc1.parse(CampignDetailsJsonObj.toString()));
	    
		
		//mergeJSONObjects(CampignDetailsJsonObj,CampignDetailsJsonObj1);
		
		
		
		
		
		/*
		Document ladp_info = new Document("ldap_ip_address", mainjson.get("ldap_ip_address"))
                .append("ldap_port", mainjson.get("ldap_port"))
                .append("ldap_user_name", mainjson.get("ldap_user_name"))
                .append("ldap_password",mainjson.get("ldap_password"));
        Document doc1 = new Document("ldap",ladp_info);
    	System.out.println("Output doc1: "+doc1);
        */
	}
	
	public static JSONArray findUrlClickStatisticsBasedOnCampaignIDAndUserID(String coll_name,String Campaign_Id,String User_Id) {
		MongoClient mongoClient = null;
		DB db =null;
		DBCollection collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=null;
	    
	    try {
	    	mongoClient = ConnectionHelper.getConn();
	        db = mongoClient.getDB("phplisttest");
	        collection = db.getCollection("date_funnel");

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
	        	System.out.println(campaignJsonArr);
	        	System.out.println(campaignJsonArr.length());
	    	}
	        //System.out.println(campaignJsonArr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConn(mongoClient);
		}
        return campaignJsonArr;
    }
	
	public static JSONArray findAllUrlByBasicDBObject(String coll_name,String url,String funnel_id) {
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
	                "viewed_subscribers.data.urlclickstatistics.url", url).append("viewed_subscribers.data.urlclickstatistics.messageid", funnel_id));
	        DBObject project = new BasicDBObject("$project", new BasicDBObject(
	                "_id", 0).append("viewed_subscribers.data.urlclickstatistics", 1));
            List<DBObject> pipeline = Arrays.asList(unwind, match, project);
	        AggregationOutput output = collection.aggregate(pipeline);
            Iterable<DBObject> results = output.results();
	        for (DBObject result : results) {
	        	BSONObject urlclickstatistics=(BSONObject) ((BSONObject) ((BSONObject) result.get("viewed_subscribers")).get("data")).get("urlclickstatistics");
	        	campaignJsonArr=new JSONArray(urlclickstatistics.toString());
	        	System.out.println(urlclickstatistics);
	        	//campaignJsonArr(urlclickstatistics.);
	    	}
	        //System.out.println(urlclickstatistics);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConn(mongoClient);
		}
        return campaignJsonArr;

    }
	
	public static JSONArray findUcsUserClickStatisticsCampaignBased(String coll_name,String Sling_Campaign_Id,String url) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	    
	    JSONArray campaignJsonArr=new JSONArray();
	    
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(coll_name);
            Bson filter1 =and(eq("Sling_Campaign_Id", Sling_Campaign_Id),
            		eq("Sling_Campaign_Id", Sling_Campaign_Id));
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
	
	public static JSONArray ucsCampaignClickStatistics(String coll_name,String urlclickstatistics_url,String campaignId) {
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
					availableUrlJsonObj.put("sent",sent);
					availableUrlJsonObj.put("campaign_id",campaign_id);
					availableUrlJsonObj.put("URL",urlclickstatistics_url_temp);
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
	
	
	public static JSONArray findAllUrlByFilter(String coll_name) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document myDoc=null;
	    JSONObject funnelJsonObj=null;
	    
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
            Bson andFilter=and(gt("linkcount", 0), gt("viewed", 0));
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
			    					    		funnelJsonObj=new JSONObject();
			    					    		funnelJsonObj.put("id",id);
			    					    		funnelJsonObj.put("uuid",uuid);
			    					    		funnelJsonObj.put("subject",subject);
			    					    		funnelJsonObj.put("sendstart",sendstart);
			    					    		funnelJsonObj.put("status",status);

			    					    		funnelJsonObj.put("viewed",viewed);
			    					    		funnelJsonObj.put("bounce_count",bounce_count);
			    					    		funnelJsonObj.put("fwds",fwds);
			    					    		funnelJsonObj.put("sent",sent);
			    					    		funnelJsonObj.put("clicks",clicks);

			    					    		funnelJsonObj.put("open_rate",open_rate);
			    					    		funnelJsonObj.put("click_rate",click_rate);
			    					    		funnelJsonObj.put("click_per_view_rate",click_per_view_rate);
			    					    		funnelJsonObj.put("unique_click_rate",unique_click_rate);

			    					    		funnelJsonObj.put("linkcount",linkcount);
			    					    		funnelJsonObj.put("subscriber_count",subscriber_count);

			    					    		funnelJsonObj.put("Body",Body);
			    					    		funnelJsonObj.put("Sling_Campaign_Id",Sling_Campaign_Id);
			    					    		funnelJsonObj.put("CreatedBy",CreatedBy);
			    					    		funnelJsonObj.put("List_Id",List_Id);
			    					    		funnelJsonObj.put("Sling_Subject",Sling_Subject);
			    					    		funnelJsonObj.put("Type",Type);

			    					    		funnelJsonObj.put("subFunnelNodeName",subFunnelNodeName);
			    					    		funnelJsonObj.put("subFunnelCounter",subFunnelCounter);
			    					    		funnelJsonObj.put("Current_Campaign",Current_Campaign);
			    					    		funnelJsonObj.put("funnelNodeName",funnelNodeName);
			    					    		funnelJsonObj.put("funnelCounter",funnelCounter);

			    					    		funnelJsonObj.put("subscriber_total",total);
			    					    		funnelJsonObj.put("subscriber_campaignid",data_campaignid);
			    					    		funnelJsonObj.put("subscriber_userid",data_userid);
			    					    		funnelJsonObj.put("subscriber_viewed",data_viewed);
			    					    		funnelJsonObj.put("subscriber_email",data_email);
			    					    		funnelJsonObj.put("subscriber_uuid",data_uuid);

			    					    		funnelJsonObj.put("campaignclickstatistics_firstclick",campaignclickstatistics_firstclick);
			    					    		funnelJsonObj.put("campaignclickstatistics_latestclick",campaignclickstatistics_latestclick);
			    					    		funnelJsonObj.put("campaignclickstatistics_clicked",campaignclickstatistics_clicked);
			    					    		
			    					    		funnelJsonObj.put("urlclickstatistics_firstclick",urlclickstatistics_firstclick);
			    					    		funnelJsonObj.put("urlclickstatistics_latestclick",urlclickstatistics_latestclick);
			    					    		funnelJsonObj.put("urlclickstatistics_clicked",urlclickstatistics_clicked);
			    					    		funnelJsonObj.put("urlclickstatistics_messageid",urlclickstatistics_messageid);
			    					    		funnelJsonObj.put("urlclickstatistics_url",urlclickstatistics_url);
			    					    		MongoDAO mdao=new MongoDAO();
			    								mdao.createOne("url", funnelJsonObj);
		    					    	}
		    					    	
		    					    }else{
		    					    	System.out.println("No urlclickstatistics Found");
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


	
	public static JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
		JSONObject mergedJSON =null;
		System.out.println("mergedJSON : 1");
		try {
			mergedJSON = new JSONObject(json2.toString());
			Iterator<String> itr=json1.keys();
			while (itr.hasNext()) {
				String key=itr.next();
				mergedJSON.put(key,json1.getString(key));
	            
			}
			System.out.println("mergedJSON : "+mergedJSON);
		} catch (Exception e) {
			throw new RuntimeException("JSON Exception" + e);
		}
		return mergedJSON;
	}

}
