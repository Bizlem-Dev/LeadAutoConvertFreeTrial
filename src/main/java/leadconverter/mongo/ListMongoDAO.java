package leadconverter.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletResponse;
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
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class ListMongoDAO {
	
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		//addCampaignList(new Document());
		//addSubscriberInCampaignList("955","2080");
		//removeSubscribersFromCampaignList("975","7101");
		//removeSubscribersFromCampaignList("975","7102");
		//updateCampaignListStatusAndName("955","active 1");
		
		/*
		updateCampaignListAtivateDate("955", "2019-06-10 16:48:57");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat date_formatter_with_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String list_activate_days=ResourceBundle.getBundle("config").getString("list_activate_days");
		Date date1 = new Date();
		Date date2 = null;
		Date update_list_activate_date=null;
		String list_activate_date=null;
		try {
			String ListId="955";
	        JSONObject campaign_list_json_obj=getCampaignList("viki_gmail.com","Funnel03June1","Explore","955");
	        if(campaign_list_json_obj.length()>0){
	        	int ListSubscriberCount=campaign_list_json_obj.getInt("ListSubscriberCount");
	        	list_activate_date=campaign_list_json_obj.getString("ListActivateDateStr");
	        	date1=sdf.parse(sdf.format(date1));
	        	date2=sdf.parse(list_activate_date);
	        	System.out.println("date1 : " + sdf.format(date1));
        	    System.out.println("date2 : " + sdf.format(date2));
        	    
	        	// Checking for atleast one subscriber in The Draft Campaign list
	        	if(ListSubscriberCount>0){
	        		System.out.println("Subscriber Found");
	        		if (date1.compareTo(date2) == 0) {
	        			System.out.println("Date1 is equal to Date2");
	        	    	System.out.println("Make This Draft List To Active");
	        	    }else if (date1.compareTo(date2) < 0) {
	        	    	System.out.println("Date1 is before to Date2");
	        	    }else if (date1.compareTo(date2) > 0) {
	        	    	System.out.println("Date1 is after to Date2");
	        	    }
	        	}else{
	        		System.out.println("Subscriber Not Found");
	        		if (date1.compareTo(date2) == 0) {
	        	    	System.out.println("Date1 is equal to Date2");
	        	    	System.out.println("Reshedule Activation Date OF Draft List");
	        	    	update_list_activate_date=new Date();
	        	    	update_list_activate_date.setDate(update_list_activate_date.getDate()+Integer.parseInt(list_activate_days));
	        		    updateCampaignListAtivateDate(ListId, date_formatter_with_timestamp.format(update_list_activate_date));
	        	    }else if (date1.compareTo(date2) < 0) {
	        	    	System.out.println("Date1 is before to Date2");
	        	    }else if (date1.compareTo(date2) > 0) {
	        	    	System.out.println("Date1 is after to Date2");
	        	    }
	        		
	        	}
			} 
        }
		catch (Exception ex) {
			// TODO Auto-generated catch block
			System.out.println("Date Exception : " + ex.getMessage());
		}
		*/
        
	    /*
	    if (date1.compareTo(date2) > 0) {
	    	System.out.println("Date1 is after Date2");
	    	System.out.println("Put the subscriber in draft list");
	     // Put the subscriber in draft list
	        //String addsubscriberinlistparameters = "?list_id=" + ActiveListId +"&subscriber_id="+ SubscriberId;
	        
			//String addsubscriberinlistparameters = "?list_id=" + DraftListId +"&subscriber_id="+ SubscriberId;
			//String responsedata =this.sendpostdata(subscriberaddurl,addsubscriberinlistparameters.replace(" ","%20"),response).replace("<pre>", "");
	    } else if (date1.compareTo(date2) < 0) {
	    	System.out.println("Date1 is before Date2");
	        //write logic here
	        // Put the subscriber in current List
	    	System.out.println("Put the subscriber in current List");
	        //String addsubscriberinlistparameters = "?list_id=" + ActiveListId +"&subscriber_id="+ SubscriberId;
			//String responsedata =this.sendpostdata(subscriberaddurl,addsubscriberinlistparameters.replace(" ","%20"),response).replace("<pre>", "");
	    } else if (date1.compareTo(date2) == 0) {
	    	System.out.println("Date1 is equal to Date2");
	     // Put the subscriber in draft list
	    	System.out.println("Put the subscriber in draft list");
	        //String addsubscriberinlistparameters = "?list_id=" + ActiveListId +"&subscriber_id="+ SubscriberId;
	        
			//String addsubscriberinlistparameters = "?list_id=" + DraftListId +"&subscriber_id="+ SubscriberId;
			//String responsedata =this.sendpostdata(subscriberaddurl,addsubscriberinlistparameters.replace(" ","%20"),response).replace("<pre>", "");
	    } else {
	    	System.out.println("How to get here?");
	    }
	    */
		//findListIdForSubscriber("955","2076","2081");
		findCampaignDetailsBasedOnCampaignID("1099","innovatters@gmail.com");//innovatters@gmail.com  akhilesh@bizlem.com
		//findListIdForSubscriber("viki_gmail.com","FunnelJune0701","Connect","7101");
		//String CreatedBy,String funnelName,String SubFunnelName,String SubscriberId
	}
	
	public static void findCampaignDetailsBasedOnCampaignID(String CampaignId,String SubscriberEmail){
		JSONObject campaign_list_json_obj=new JSONObject();
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCursor<Document> camapignDetailsCursor=null;
	    MongoCursor<Document> camapignDetailsSubscribersCursor=null;
	    Document campaign_details_doc=null;
	    Document campaign_details_subscribers_doc=null;
	    Document email_doc=null;
	    Bson filter =null;
	    Bson get_subscriber_id_filter=null;
	    String CreatedBy=null;
	    String funnelName=null;
	    String SubFunnelName=null;
	    String List_Id=null;
	    String SubscriberId=null;
	    
	    
	    try {
	    	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("campaign_details");
            filter=eq("Campaign_Id", CampaignId);
            
            camapignDetailsCursor = collection.find(filter).iterator();
            if(camapignDetailsCursor.hasNext()){
				while(camapignDetailsCursor.hasNext()) {
					    campaign_details_doc=camapignDetailsCursor.next();
						//System.out.println(campaign_details_doc.toJson());
						CreatedBy=campaign_details_doc.getString("CreatedBy");
					    funnelName=campaign_details_doc.getString("funnelName");
					    SubFunnelName=campaign_details_doc.getString("SubFunnelName");
					    List_Id=campaign_details_doc.getString("List_Id");
					    //SubscriberEmail FunnelName 
					    //get_subscriber_id_filter=and(eq("CreatedBy", CreatedBy.replace("_", "@"))
					    		//,eq("funnelName", funnelName),eq("SubscriberEmail", SubscriberEmail));
					    get_subscriber_id_filter=eq("SubscriberEmail", SubscriberEmail);
	                    Document unwind = new Document("$unwind", "$subscribers");
	                    Document match = new Document("$match", new Document(
	        	                "subscribers.email", SubscriberEmail).append("Campaign_Id", CampaignId));
	                    
	                    Document project = new Document("$project", new Document(
	        	                "_id", 0).append("subscribers", 1));
	                    List<Document> pipeline = Arrays.asList(unwind, match, project);
	                    AggregateIterable<Document> output=collection.aggregate(pipeline);
	                    if(output.first()!=null){
	                        email_doc=(Document) output.first().get("subscribers");
	                        SubscriberId=email_doc.getString("id");
	                        //System.out.println("email_doc : "+email_doc);
	                        System.out.println("Subscribers("+SubscriberEmail+ ") Is Found In List Id : "+List_Id+"  SubscriberId : "+SubscriberId);
	                        //findListIdForSubscriber(CreatedBy,funnelName,SubFunnelName,SubscriberId);
	                        deleteSubscriberFromPhpList(List_Id,SubscriberId);
	                        removeSubscriberFromCampaignList(collection,CampaignId,List_Id,SubscriberId,SubscriberEmail);
	                    }else{
	                    	System.out.println("Subscribers("+SubscriberEmail+ ") Is Not Found In List Id : "+List_Id);
	                    	System.out.println("Going to fetch SubscriberId from subscribers_details Collection");
	                    	SubscriberId=findSubscriberIdBasedOnSubscriberEmail(database,get_subscriber_id_filter);
	                    	System.out.println("Subscribers("+SubscriberEmail+ ") Is Found and SubscriberId : "+SubscriberId);
	                    	if(SubscriberId!=null){
	                    		List_Id=findListIdForSubscriber(CreatedBy,funnelName,SubFunnelName,SubscriberId);
	                    		if(List_Id!=null){
		                    		// Do your stuff here
	                    			removeSubscribersFromCampaignList(List_Id,SubscriberId);
	                    			deleteSubscriberFromPhpList(List_Id,SubscriberId);
		                    	}else{
		                    		System.out.println("No List Id Found For SubscriberId : "+SubscriberId);
		                    	}
	                    	}else{
	                    		System.out.println("No List Id Found For SubscriberId : "+SubscriberId);
	                    	}
	                    	
	                    }
	             }
            }else{
            	System.out.println("No campaign Found For Campaign Id : "+CampaignId);
            }
     	   } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	}
	
	public static void deleteSubscriberFromPhpList(String ListId,String UserId){
	    String Delete_Subscriber_From_List_Url = ResourceBundle.getBundle("config").getString("Delete_Subscriber_From_List");
    	String deletesubscriberinlistparameters = "?list_id=" + ListId +"&subscriber_id="+ UserId;
		try {
			String responsedata =sendpostdata(Delete_Subscriber_From_List_Url,deletesubscriberinlistparameters.replace(" ","%20")).replace("<pre>", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
   
   public static void removeSubscriberFromCampaignList(MongoCollection<Document> collection,
		   String CampaignId,String List_Id,String SubscriberId,String SubscriberEmail){
		try {
            Document query = new Document();
                     query.put("Campaign_Id", CampaignId);   
            Document subscriber = new Document();
                    subscriber.put( "id",    SubscriberId );
                    subscriber.put( "email",   SubscriberEmail );
            Document updateQuery = new Document();
                    updateQuery.put( "$pull", new Document( "subscribers", subscriber ) );
            UpdateOptions options = new UpdateOptions().upsert(true);         
           
            collection.updateOne(query, updateQuery);
                    
          } catch (Exception ex) {
           System.out.println("Error : "+ex.getMessage());
		}
	}
   
	public static String findListIdForSubscriber(String CreatedBy,String funnelName,String SubFunnelName,String SubscriberId){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCursor<Document> sessionCountCursor=null;
	    Bson get_subscriber_id_filter=null;
	    JSONObject campaign_list_json_obj=new JSONObject();
	    String ListId=null;
	    try {
	    	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("campaign_list_details");
            List<String> subscribers_id_arrlist = new ArrayList<String>();
                         subscribers_id_arrlist.add(SubscriberId);
                         //subscribers_id_arrlist.add(SubscriberId1);
            Document all_query = new Document();
                     all_query.put("$all", subscribers_id_arrlist);
                     
            Document search_query = new Document();
                     search_query.put("ListSubscribersArr", all_query);
            //FindIterable<Document> iterable = collection.find().projection(projection);
            get_subscriber_id_filter=and(eq("CreatedBy", CreatedBy)
			    		,eq("FunnelName", funnelName),eq("ListStatus", "active"),search_query);//eq("SubFunnelName", SubFunnelName),
            System.out.println("get_subscriber_id_filter : "+get_subscriber_id_filter);
            sessionCountCursor = collection.find(get_subscriber_id_filter).iterator();
            System.out.println("Subscriber List Found Status : "+sessionCountCursor.hasNext());
                if(sessionCountCursor.hasNext()){
					while(sessionCountCursor.hasNext()) {
							Document campaign_list_doc=sessionCountCursor.next();
							ListId=campaign_list_doc.getString("ListId");
					}
                }else{
                	System.out.println("SubscriberId : "+SubscriberId+" is not found in list");
                }
     	   } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	    
	    return ListId;
	}
	
	public static String findSubscriberIdBasedOnSubscriberEmail(MongoDatabase database,Bson get_subscriber_id_filter){
		MongoCollection<Document> subscribers_details_collection = null;
	    MongoCursor<Document> subscribersDetailsCursor=null;
	    JSONObject campaign_list_json_obj=new JSONObject();
	    String SubscriberId=null;
	    try {
	    	subscribers_details_collection=database.getCollection("subscribers_details");
	    	subscribersDetailsCursor = subscribers_details_collection.find(get_subscriber_id_filter).iterator();
	            while(subscribersDetailsCursor.hasNext()) {
							Document subscribers_details_doc=subscribersDetailsCursor.next();
							SubscriberId=subscribers_details_doc.getString("SubscriberId");
							//System.out.println(subscribers_details_doc);
							//campaign_list_json_obj.put("", campaign_list_doc.getString(""));
				}
     	   } catch (Exception ex) {
             System.out.println("Error : "+ex.getMessage());
		   }
	    return SubscriberId;
	}
		
	//------------------------------------------------------------------------------//-----------------------------------------------------------//
	//------------------------------------------------------------------------------//-----------------------------------------------------------//
	//------------------------------------------------------------------------------//-----------------------------------------------------------//
	//------------------------------------------------------------------------------//-----------------------------------------------------------//
	//------------------------------------------------------------------------------//-----------------------------------------------------------//
	//------------------------------------------------------------------------------//-----------------------------------------------------------//
	//------------------------------------------------------------------------------//-----------------------------------------------------------//
	//------------------------------------------------------------------------------//-----------------------------------------------------------//
	//------------------------------------------------------------------------------//-----------------------------------------------------------//
	
	public static void addCampaignList(Document campaign_list_doc){
	   	
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    DateFormat date_formatter_with_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String list_activate_days=ResourceBundle.getBundle("config").getString("list_activate_days");
	    Date date=null;
	    try {
	    	date=new Date();
		    date.setDate(date.getDate()+Integer.parseInt(list_activate_days));
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("campaign_list_details");
            /*
            Document query = new Document();
                     query.put("CreatedBy", "viki_gmail.com");
                     query.put("FunnelName", "Funnel03June1");
                     query.put("SubFunnelName", "Explore");
                     query.put("ListId", "955");
                     query.put("ListName", "ActiveList_1");
                     query.put("ListSubscriberCount",0);
                     query.put("ListStatus", "active");
                     query.put("ListActivateDate", date);
                     query.put("ListActivateDateStr",date_formatter_with_timestamp.format(date));
                     
                     List<String> subscribers_doc_list = new ArrayList<String>();
                     //subscribers_doc_list.add("2075");
                     //subscribers_doc_list.add("2076");
                     //subscribers_doc_list.add("2077");
                     query.put("ListSubscribersArr", subscribers_doc_list);
                     
            collection.insertOne(query); 
            */
            collection.insertOne(campaign_list_doc);
           } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	   }
	public static void removeSubscribersFromCampaignList(String ListId,String UserId){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("campaign_list_details");
            Document query = new Document();
                     query.put("ListId", ListId); 
            Document updateQuery = new Document();
                     updateQuery.put( "$pull", new Document( "ListSubscribersArr", UserId ) );
            UpdateResult upres= collection.updateOne(query, updateQuery);
            System.out.println("No of Record Modified : " + upres.getModifiedCount());
	         if(upres.getModifiedCount()>0){
	        	 Document count = new Document();
                          count.put("ListSubscriberCount", -1); 
            	 Document count_update_query = new Document();
		    	          count_update_query.put( "$inc", count);
		    	 collection.updateOne(query, count_update_query);
	         }
           } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	}
	public static void addSubscriberInCampaignList(String ListId,String UserId){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    try {
        	 mongoClient=ConnectionHelper.getConnection();
             database=mongoClient.getDatabase("phplisttest");
             collection=database.getCollection("campaign_list_details");
             Document query = new Document();
                     query.put("ListId", ListId); 
             Document updateQuery = new Document();
                      updateQuery.put( "$addToSet", new Document( "ListSubscribersArr", UserId ));
             UpdateResult upres= collection.updateOne(query, updateQuery);
             System.out.println("No of Record Modified : " + upres.getModifiedCount());
             if(upres.getModifiedCount()>0){
            	 Document count = new Document();
                          count.put("ListSubscriberCount", 1); 
             	 Document count_update_query = new Document();
	         	          count_update_query.put( "$inc", count);
	         	 collection.updateOne(query, count_update_query);
	         }
                     
           } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	}
	public static void addListCampaign(Document list_campaign_doc,String ListId){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("campaign_list_details");
            Document query = new Document();
                     query.put("ListId", ListId);   
            Document updateQuery = new Document();
                     updateQuery.put( "$addToSet", new Document( "ListCampaignArr", list_campaign_doc ) );
            UpdateOptions options = new UpdateOptions().upsert(true);         
            
            collection.updateOne(query, updateQuery);
                     
           } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	}
	public static void updateCampaignListStatusAndName(String ListId,String status,String ListName){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    try {
        	 mongoClient=ConnectionHelper.getConnection();
             database=mongoClient.getDatabase("phplisttest");
             collection=database.getCollection("campaign_list_details");
             Document query = new Document();
                     query.put("ListId", ListId); 
             Document list_status_doc = new Document();
                      list_status_doc.put("ListStatus", status); 
                      list_status_doc.put("ListName", ListName); 
             Document updateQuery = new Document();
                      updateQuery.put("$set", list_status_doc );
             UpdateResult upres= collection.updateOne(query, updateQuery);
             System.out.println("No of Record Modified : " + upres.getModifiedCount());
                     
           } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	}
	public static void updateCampaignListAtivateDate(String ListId,String date_str){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    try {
        	 mongoClient=ConnectionHelper.getConnection();
             database=mongoClient.getDatabase("phplisttest");
             collection=database.getCollection("campaign_list_details");
             Document query = new Document();
                     query.put("ListId", ListId); 
             Document list_status_doc = new Document();
                      list_status_doc.put("ListActivateDateStr", date_str); 
             Document updateQuery = new Document();
                      updateQuery.put("$set", list_status_doc );
             UpdateResult upres= collection.updateOne(query, updateQuery);
             System.out.println("No of Record Modified : " + upres.getModifiedCount());
                     
           } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	}
	public static JSONObject getCampaignList(String CreatedBy,String FunnelName,String SubFunnelName,String ListId){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCursor<Document> sessionCountCursor=null;
	    JSONObject campaign_list_json_obj=new JSONObject();
	    try {
        	 mongoClient=ConnectionHelper.getConnection();
             database=mongoClient.getDatabase("phplisttest");
             collection=database.getCollection("campaign_list_details");
             Document query = new Document();
                      query.put("ListId", ListId); 
             Bson filter =and(eq("CreatedBy", CreatedBy),eq("FunnelName", FunnelName)
            		 ,eq("SubFunnelName", SubFunnelName),eq("ListId", ListId));
             sessionCountCursor = collection.find(filter).iterator();
             System.out.println("Subscriber List Found Status : "+sessionCountCursor.hasNext());
				while(sessionCountCursor.hasNext()) {
						Document campaign_list_doc=sessionCountCursor.next();
						//System.out.println(campaign_list_doc);
						campaign_list_json_obj.put("ListActivateDateStr", campaign_list_doc.getString("ListActivateDateStr"));
						campaign_list_json_obj.put("ListSubscriberCount", campaign_list_doc.getInteger("ListSubscriberCount"));
						campaign_list_json_obj.put("ListStatus", campaign_list_doc.getString("ListStatus"));
						//campaign_list_json_obj.put("", campaign_list_doc.getString(""));
			     }
      	   } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		   } finally {
					sessionCountCursor.close();
		   }
	    return campaign_list_json_obj;
	}
	
	public static void findListIdForSubscriberTest(String ListId,String SubscriberId,String SubscriberId1){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCursor<Document> sessionCountCursor=null;
	    JSONObject campaign_list_json_obj=new JSONObject();
	    try {
	    	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("campaign_list_details");
            List<String> subscribers_id_arrlist = new ArrayList<String>();
                         subscribers_id_arrlist.add(SubscriberId);
                         //subscribers_id_arrlist.add(SubscriberId1);
            Document all_query = new Document();
                     all_query.put("$all", subscribers_id_arrlist);
            Document search_query = new Document();
                     search_query.put("ListSubscribersArr", all_query);
            //FindIterable<Document> iterable = collection.find().projection(projection);
            sessionCountCursor = collection.find(search_query).iterator();
            System.out.println("Subscriber List Found Status : "+sessionCountCursor.hasNext());
				while(sessionCountCursor.hasNext()) {
						Document campaign_list_doc=sessionCountCursor.next();
						System.out.println(campaign_list_doc);
						//campaign_list_json_obj.put("", campaign_list_doc.getString(""));
			     }
     	   } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	}
	public static String sendpostdata(String callurl, String urlParameters)
			throws ServletException, IOException {

		URL url = new URL(callurl + urlParameters);
		System.out.println("Url :" + url);
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
			System.out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}

}
