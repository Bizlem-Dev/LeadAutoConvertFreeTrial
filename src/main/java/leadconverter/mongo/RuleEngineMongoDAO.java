package leadconverter.mongo;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import leadconverter.doctiger.LogByFileWriter;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;

public class RuleEngineMongoDAO {
	
	public static void main(String[] args) {
		//Document doc=Document.parse(json.toString());
	    //DBObject dbObject = (DBObject)JSON.parse(json);
		// TODO Auto-generated method stub
		//addSubscribersDetails(new Document());
		  //removeSubscribers(new Document());
		  //addSubscribers(new Document());
		  //addRuleFieldsDetails();
		//findFieldsFromRuleFieldsDetails();
		findCategoryFromRuleFieldsDetails();
				
	}//main method end
	
	public static String addRuleFieldsDetails(){
		System.out.println("Inside Method addRuleFieldsDetails");
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    Document rule_fields_details_doc=null;
	    Document subscriber_set_doc=null;
	    Document subscriber_query=null;
	    try {
	    	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("rule_fields_details");
            
		    String created_by="viki@gmail.com";
		    String funnelName="June24F06";
		    
		    
		    	
		    List<String> fields_doc_list = new ArrayList<String>();
		    fields_doc_list.add("CreatedBy");
		    fields_doc_list.add("FunnelName");
		    fields_doc_list.add("SubFunnelName");
		    fields_doc_list.add("Category");
		    fields_doc_list.add("SubscriberEmail");
		    fields_doc_list.add("SourceMedium");
		    fields_doc_list.add("Source");
		    fields_doc_list.add("ListId");
		    fields_doc_list.add("CampaignId");
		    fields_doc_list.add("SubscriberId");
		    fields_doc_list.add("GAUser");
		    fields_doc_list.add("SessionCount");
		    fields_doc_list.add("MostRecent_SessionCount");
		    fields_doc_list.add("Recent_SessionCount");
		    fields_doc_list.add("AvgSesionDuration");
		    fields_doc_list.add("MostRecent_AvgSesionDuration");
		    fields_doc_list.add("Recent_AvgSesionDuration");
		    fields_doc_list.add("URL");
		    
		    
		   
		    
		    List<String> category_doc_list = new ArrayList<String>();
		    category_doc_list.add("Explore");
		    category_doc_list.add("Entice");
		    category_doc_list.add("Inform");
		    category_doc_list.add("Warm");
		    category_doc_list.add("Connect");
		    
            
		    rule_fields_details_doc=new Document();
		    rule_fields_details_doc.put("created_by",created_by);
		    rule_fields_details_doc.put("funnelName",funnelName);
		    rule_fields_details_doc.put("fields",fields_doc_list);
		    rule_fields_details_doc.put("category",category_doc_list);
            
            collection.insertOne(rule_fields_details_doc);
			    //collection.insertMany(subscribers_doc_list);
	            
            } catch (Exception ex) {
            	System.out.println("Error inside Method addSubscribersDetails() : "+ex.getMessage());
		    } finally {
				ConnectionHelper.closeConnection(mongoClient);
		    }
	    return "rule_fields_details Records Saved";
	}
	public static JSONArray findFieldsFromRuleFieldsDetails(){
		JSONArray fields_json_arr=new JSONArray();
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCursor<Document> ruleFieldsDetailsCursor=null;
	    Document rule_fields_details_doc=null;
	 
	    try {
	    	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("rule_fields_details");
            
	        Document unwind = new Document("$unwind", "$fields");
	        Document match = new Document("$match", new Document(
		                "funnelName", "June24F06").append("created_by", "viki@gmail.com"));
	            
	        Document project = new Document("$project", new Document(
		                "_id", 0).append("fields", 1));
	        List<Document> pipeline = Arrays.asList(unwind, match, project);
	        AggregateIterable<Document> output=collection.aggregate(pipeline);
	        ruleFieldsDetailsCursor=output.iterator();
	        
	        System.out.println("Fields Found : "+ruleFieldsDetailsCursor.hasNext());
	        if(ruleFieldsDetailsCursor.hasNext()){
				while(ruleFieldsDetailsCursor.hasNext()) {
					rule_fields_details_doc=ruleFieldsDetailsCursor.next();
					fields_json_arr.put(rule_fields_details_doc.getString("fields"));
				}
            }else{
            	 System.out.println("No Fields Found");
            	//LogByFileWriter.logger_info("CampaignSheduleMongoDAO: "+"No Campaign Found To Schedule ");
            }
           } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
        }
	    System.out.println(fields_json_arr);
	    return fields_json_arr;
	 }
	public static JSONArray findCategoryFromRuleFieldsDetails(){
		JSONArray category_json_arr=new JSONArray();
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    MongoCursor<Document> ruleFieldsDetailsCursor=null;
	    Document rule_fields_details_doc=null;
	 
	    try {
	    	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("rule_fields_details");
            
	        Document unwind = new Document("$unwind", "$category");
	        Document match = new Document("$match", new Document(
		                "funnelName", "June24F06").append("created_by", "viki@gmail.com"));
	        Document project = new Document("$project", new Document(
		                "_id", 0).append("category", 1));
	        List<Document> pipeline = Arrays.asList(unwind, match, project);
	        AggregateIterable<Document> output=collection.aggregate(pipeline);
	        ruleFieldsDetailsCursor=output.iterator();
	        System.out.println("Category Found : "+ruleFieldsDetailsCursor.hasNext());
	        if(ruleFieldsDetailsCursor.hasNext()){
				while(ruleFieldsDetailsCursor.hasNext()) {
					rule_fields_details_doc=ruleFieldsDetailsCursor.next();
					category_json_arr.put(rule_fields_details_doc.getString("category"));
				}
            }else{
            	 System.out.println("No Category Found");
            	//LogByFileWriter.logger_info("CampaignSheduleMongoDAO: "+"No Campaign Found To Schedule ");
            }
           } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
        }
	    System.out.println(category_json_arr);
	    return category_json_arr;
	 }
}