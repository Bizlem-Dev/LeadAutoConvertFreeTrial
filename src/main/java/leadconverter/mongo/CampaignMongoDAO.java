package leadconverter.mongo;

import static com.mongodb.client.model.Filters.eq;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

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

public class CampaignMongoDAO {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//addCampaignDetails(new Document());
		  //AddSubscriberInList("");
		  //RemoveSubscribersFromList("");
		  removeSubscribersFromList(new Document());
		  //addSubscriberInList(new Document());
		  
				
	}//main method end
	
	public static void addCampaignDetails(String CreatedBy,String funnelName,String SubFunnelName,String CampaignNodeNameInSling,String FromName,
			String FromEmailAddress,String CampaignName,String Subject,String Body,String Type,String Campaign_Id,
			String List_Id,String campaign_status,String Campaign_Date,HashMap<String, String> subscribers){
		
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    String id=null;
	    Document subscriber=null;
	    //Document doc=Document.parse(json.toString());
	    //System.out.println("doc : "+doc);
	    //DBObject dbObject = (DBObject)JSON.parse(json);
	    try {
	         Document camp_details_doc=new Document();
		        camp_details_doc.put("CreatedBy", CreatedBy);
		        camp_details_doc.put("funnelName", funnelName);
		        camp_details_doc.put("SubFunnelName", SubFunnelName);
		        camp_details_doc.put("CampaignNodeNameInSling", CampaignNodeNameInSling);
		        camp_details_doc.put("FromName", FromName);
		        camp_details_doc.put("FromEmailAddress", FromEmailAddress);
		        camp_details_doc.put("CampaignName", CampaignName);
		        
		        camp_details_doc.put("Subject", Subject);
		        camp_details_doc.put("Body", Body);
		        camp_details_doc.put("Type", Type);
		        camp_details_doc.put("Campaign_Id", Campaign_Id);
		        camp_details_doc.put("List_Id", List_Id);
		        camp_details_doc.put("campaign_status", campaign_status);
		        camp_details_doc.put("Campaign_Date", Campaign_Date);
        
             List<Document> subscribers_doc_list = new ArrayList<Document>();
		        for (HashMap.Entry<String,String> entry : subscribers.entrySet()){
		                     subscriber=new Document();
		                     subscriber.put("id", entry.getKey());
		                     subscriber.put("email", entry.getValue());
		            subscribers_doc_list.add(subscriber);
		            } 
                camp_details_doc.put("subscribers", subscribers_doc_list);
		        
        
	        	mongoClient=ConnectionHelper.getConnection();
	            database=mongoClient.getDatabase("phplisttest");
	            collection=database.getCollection("campaign_details");
	            collection.insertOne(camp_details_doc);
            } catch (Exception e) {
            
		    } finally {
				ConnectionHelper.closeConnection(mongoClient);
		    }
	}
	
	public static void removeSubscribersFromList(Document subscriber_doc){
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
	public static void addSubscriberInList(Document subscriber_doc){
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
	public static void updateCampaignDateInCampaignDetails(String campaign_id,String Campaign_Date){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    try {
	    	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("campaign_details");
	        Document query = new Document();
		             query.put("Campaign_Id", campaign_id);
		             //query.put("List_Id", list_id);
		             //query.put("funnelName", funnelName);
		             //query.put("List_Id", SubFunnelName);
		   
		    Document data = new Document();
		             data.put("Campaign_Date", Campaign_Date);
	
		    Document command = new Document();
		    command.put("$set", data);
	
		    collection.updateOne(query, command);
	    } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	}
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

}
