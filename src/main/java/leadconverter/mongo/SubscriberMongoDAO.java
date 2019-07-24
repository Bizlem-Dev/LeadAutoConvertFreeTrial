package leadconverter.mongo;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;

public class SubscriberMongoDAO {
	
	public static void main(String[] args) {
		//Document doc=Document.parse(json.toString());
	    //DBObject dbObject = (DBObject)JSON.parse(json);
		// TODO Auto-generated method stub
		//addSubscribersDetails(new Document());
		  removeSubscribers(new Document());
		  addSubscribers(new Document());
				
	}//main method end
	
	public static String addSubscribersDetails(JSONObject subscriber_details){
		System.out.println("Inside Method addSubscribersDetails");
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document subscriber=null;
	    Document subscriber_set_doc=null;
	    Document subscriber_query=null;
	    //System.out.println("subscriber_details Start---------------------------");
	    //System.out.println(subscriber_details);
	    //System.out.println("subscriber_details End---------------------------");
	    try {
	    	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("subscribers_details");
            
		    JSONArray subscribers_details_json_arr=subscriber_details.
		    		  getJSONArray("subscribers_details_json_arr");
		    JSONArray subscribers_id_json_arr=subscriber_details.
		    		  getJSONArray("subscribers_id_json_arr");
		    String created_by=subscriber_details.getString("created_by");
		    String funnelName=subscriber_details.getString("funnelName");
		    String fromName=subscriber_details.getString("fromName");
		    String fromEmailAddress=subscriber_details.getString("fromEmailAddress");
		    String listname=subscriber_details.getString("listname");
		    String listid=subscriber_details.getString("listid");
		    String remote_user=subscriber_details.getString("remote_user");
		    
		    	
		    List<Document> subscribers_doc_list = new ArrayList<Document>();
			    for (int i = 0; i < subscribers_id_json_arr.length(); i++) {
			    	subscriber=new Document();
			    	subscriber_query=new Document();
					JSONObject subscribers_id_json_obj = subscribers_id_json_arr.getJSONObject(i);
					JSONObject subscribers_details_json_obj = subscribers_details_json_arr.getJSONObject(i);
					//System.out.println("addSubscribersDetails Step 0");
					subscriber.put("CreatedBy", created_by);
		    	    subscriber.put("FunnelName", funnelName);
		    	    subscriber.put("FromName", fromName);
		    	    subscriber.put("FromEmailAddress",fromEmailAddress);
		    	    subscriber.put("ListName", listname);
		    	    subscriber.put("ListId", listid);
		    	    subscriber.put("RemoteUser", remote_user);
		    	    
		    	    subscriber_query.put("CreatedBy", created_by);
		    	    subscriber_query.put("FunnelName", funnelName);
		    	    subscriber_query.put("FromName", fromName);
		    	    subscriber_query.put("FromEmailAddress",fromEmailAddress);
		    	    subscriber_query.put("ListName", listname);
		    	    subscriber_query.put("ListId", listid);
		    	    subscriber_query.put("RemoteUser", remote_user);
		    	    
					subscriber.put("SubscriberId", subscribers_id_json_obj.getString("id"));
					subscriber.put("SubscriberEmail", subscribers_id_json_obj.getString("email"));
					System.out.println("SubscriberId : "+subscribers_id_json_obj.getString("id")+"  "+
							           "SubscriberEmail : "+subscribers_id_json_obj.getString("email"));
					subscriber_query.put("SubscriberId", subscribers_id_json_obj.getString("id"));
					subscriber_query.put("SubscriberEmail", subscribers_id_json_obj.getString("email"));
					// EmailAddress FirstName LastName PhoneNumber Address CompanyName CompanyHeadCount Industry Institute Source
		            subscriber.put("EmailAddress", subscribers_details_json_obj.getString("EmailAddress"));
		            subscriber.put("FirstName", subscribers_details_json_obj.getString("FirstName"));
		            subscriber.put("LastName", subscribers_details_json_obj.getString("LastName"));
		            subscriber.put("PhoneNumber", subscribers_details_json_obj.getString("PhoneNumber"));
		            subscriber.put("Address", subscribers_details_json_obj.getString("Address"));
		            subscriber.put("CompanyName", subscribers_details_json_obj.getString("CompanyName"));
		            subscriber.put("CompanyHeadCount", subscribers_details_json_obj.getString("CompanyHeadCount"));
		            subscriber.put("Industry", subscribers_details_json_obj.getString("Industry"));
		            subscriber.put("Institute", subscribers_details_json_obj.getString("Institute"));
		            subscriber.put("Source", subscribers_details_json_obj.getString("Source"));
		            //Bson filter = subscriber_query;
		            //subscribers_doc_list.add(subscriber);
		            subscriber_set_doc=new Document();
		            subscriber_set_doc.put("$set",subscriber);
		            
		            collection.updateOne(subscriber_query, subscriber_set_doc,new UpdateOptions().upsert( true ));
				}
			    //collection.insertMany(subscribers_doc_list);
	            
            } catch (Exception ex) {
            	System.out.println("Error inside Method addSubscribersDetails() : "+ex.getMessage());
		    } finally {
				ConnectionHelper.closeConnection(mongoClient);
		    }
	    return "Subscribers Records Saved";
	}
	public static void addSubscribers(Document subscriber_doc){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("campaign_details");
            Document query = new Document();
                     query.put("Campaign_Id", "1021");   
            Document subscriber = new Document();
                     subscriber.put( "id",    "2339" );
                     subscriber.put( "email",   "sumitrathod@bizlem.com" );
            Document updateQuery = new Document();
                     updateQuery.put( "$addToSet", new Document( "subscribers", subscriber ) );
            UpdateOptions options = new UpdateOptions().upsert(true);         
            
            collection.updateOne(query, updateQuery);
                     
           } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	}
	public static void removeSubscribers(Document subscriber_doc){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("campaign_details");
            Document query = new Document();
                     query.put("Campaign_Id", "1021");   
            Document subscriber = new Document();
                     subscriber.put( "id",    "2338" );
                     subscriber.put( "email",   "sumit@bizlem.com" );
            Document updateQuery = new Document();
                     updateQuery.put( "$pull", new Document( "subscribers", subscriber ) );
            UpdateOptions options = new UpdateOptions().upsert(true);         
            
            collection.updateOne(query, updateQuery);
                     
           } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	}
	
}

/*
public static void AddSubscriberInList(String id){
 	
 	Mongo   mongo      = ConnectionHelper.getMongoConn();
 	DB     database = mongo.getDB("phplisttest");
 	DBCollection collection = database.getCollection("campaign_details");
 	
 	BasicDBObject match = new BasicDBObject();
 	//match.put( "_id", new ObjectId (id) );
     match.put( "Campaign_Id", "1021" );

     BasicDBObject subscriber = new BasicDBObject();
     subscriber.put( "id",    "2238" );
     subscriber.put( "email",   "vinayak101@gmail.com" );
     
     BasicDBObject update = new BasicDBObject();
     update.put( "$push", new BasicDBObject( "subscribers", subscriber ) );

     collection.update( match, update );
 }
public static void RemoveSubscribersFromList(String id){
	
	Mongo   mongo      = ConnectionHelper.getMongoConn();
	DB     database = mongo.getDB("phplisttest");
	DBCollection collection = database.getCollection("campaign_details");
	
	BasicDBObject match = new BasicDBObject();
	//match.put( "_id", new ObjectId (id) );
    match.put( "Campaign_Id", "1021" );

    BasicDBObject subscriber = new BasicDBObject();
    subscriber.put( "id",    "2238" );
    subscriber.put( "email",   "vinayak@gmail.com" );
    
    BasicDBObject update = new BasicDBObject();
    update.put( "$pull", new BasicDBObject( "subscribers", subscriber ) );

    collection.update( match, update );
}
*/
/*
UpdateOptions options = new UpdateOptions().upsert(true);
Sling Campaign Node Name : logged_in_user + "_" + type + "_"+ String.valueOf(count_Value1)
FindIterable<Document> c1 = collection.find();
				MongoCursor<Document> sessionCountCursor = c1.iterator();
				System.out.println("-------------------------------------------------------------");
				try {
					while(sessionCountCursor.hasNext()) {
						Document doc1=sessionCountCursor.next();
						System.out.println(doc1);
						id=doc1.getObjectId("_id").toString();
						System.out.println("id  :  "+id);
						}
				} finally {
					sessionCountCursor.close();
				}
	
*/
