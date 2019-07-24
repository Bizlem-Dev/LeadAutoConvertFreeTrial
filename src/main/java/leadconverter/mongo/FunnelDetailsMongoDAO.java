package leadconverter.mongo;

import java.util.Arrays;
import java.util.List;

import org.apache.sling.commons.json.JSONArray;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class FunnelDetailsMongoDAO {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static void getFunnelDetail(){
		
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
	    //return category_json_arr;
	}

}
