package leadconverter.create.rule;

import static com.mongodb.client.model.Filters.*;
import leadconverter.mongo.ConnectionHelper;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

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
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

public class MongoApplicationDataDAO {

	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub
		//MongoApplicationDataDAO madao=new MongoApplicationDataDAO();
		//System.out.println("doc : "+doc);
		JSONObject application_data_response_json=new JSONObject();
		application_data_response_json.put("id", 3);
		application_data_response_json.put("firstName", "Akhilesh Udaypratap Ydav From Mumbai");
		application_data_response_json.put("lastName", "Yadav");
		application_data_response_json.put("username", "akhilesh0308@bizlem.com");
		application_data_response_json.put("count", 0);
		application_data_response_json.put("password", "Akilesh");
		application_data_response_json.put("productType", "New Rule");
		application_data_response_json.put("expireday", "2019-03-30");
		application_data_response_json.put("joinedDate", "2019-03-30");
		application_data_response_json.put("freetrial", 0);
		application_data_response_json.put("expireFlag", 0);
		insertApplicationData(application_data_response_json);
		
        //boolean status=findAll("application_data","akhilesh@bizlem.com","New CarrotRule");
        //System.out.println("status : "+status);
    }
	public static String insertApplicationData(JSONObject application_data_response_json){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    String collection_name="application_data";
        try {
        	String id=application_data_response_json.getString("id");
			String firstName=application_data_response_json.getString("firstName");
			String lastName=application_data_response_json.getString("lastName");
			String username=application_data_response_json.getString("username");
			String count=application_data_response_json.getString("count");
			String password=application_data_response_json.getString("password");
			String productType=application_data_response_json.getString("productType");
			String expireday=application_data_response_json.getString("expireday");
			String joinedDate=application_data_response_json.getString("joinedDate");
			String freetrial=application_data_response_json.getString("freetrial");
			String expireFlag=application_data_response_json.getString("expireFlag");
			Document ap_data_dbobject = new Document();
			         ap_data_dbobject.put("id", id);
			         ap_data_dbobject.put("firstName", firstName);
			         ap_data_dbobject.put("lastName", lastName);
			         ap_data_dbobject.put("username", username);
			         ap_data_dbobject.put("count", count);
			         ap_data_dbobject.put("password", password);
			         ap_data_dbobject.put("productType", productType);
			         ap_data_dbobject.put("expireday", expireday);
			         ap_data_dbobject.put("joinedDate", joinedDate);
			         ap_data_dbobject.put("freetrial", freetrial);
			         ap_data_dbobject.put("expireFlag", expireFlag);
			
			boolean status=findAll(collection_name,username,productType);
			if(status==false){
				createOneApplicationData(collection_name,ap_data_dbobject);
			}else{
				updateApplicationData(collection_name,username,productType,ap_data_dbobject);
			}
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
         return "";
    }
	public static long updateApplicationData(String coll_name,String username,String productType,Document doc) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    UpdateResult updateResult=null;
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection(coll_name);
            Bson filter1 =and(eq("username", username),eq("productType", productType));
            updateResult=collection.updateOne(filter1, new Document("$set", doc));
          } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return updateResult.getModifiedCount();
    }
	public static String createOneApplicationData(String coll_name,Document doc){
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
	
	public static boolean findAll(String coll_name,String username,String productType) {
 		MongoClient mongoClient = null;
 	    MongoDatabase database  = null;
 	    MongoCollection<Document> collection = null;
 	    MongoCursor<Document> cursor=null;
 	    boolean status=false;
 	   
 	    try {
         	mongoClient=ConnectionHelper.getConnection();
             database=mongoClient.getDatabase("phplisttest");
             collection=database.getCollection(coll_name);
             Bson filter1 =and(eq("username", username),eq("productType", productType));
             cursor = collection.find(filter1).iterator();
             System.out.println(cursor.hasNext());
             status=cursor.hasNext();
             
         } catch (Exception e) {
             e.printStackTrace();
             throw new RuntimeException(e);
 		} finally {
 			ConnectionHelper.closeConnection(mongoClient);
 		}
         return status;
     }
}
