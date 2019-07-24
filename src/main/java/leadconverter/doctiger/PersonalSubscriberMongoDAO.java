package leadconverter.doctiger;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import leadconverter.mongo.ConnectionHelper;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.bson.BSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
//import org.json.simple.JSONArray;



import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

public class PersonalSubscriberMongoDAO {
	
	private static MongoClient mongoClient = null;
	private static DB db =null;
	private static DBCollection collection = null;
    Document myDoc=null;
    
    JSONArray campaignJsonArr=new JSONArray();
    
    private static void cleanup()
    {
    	mongoClient = ConnectionHelper.getConn();
		db = mongoClient.getDB("phplisttest");
        collection = db.getCollection("personal_subscribers");
    }
	
	public static String savePersonalSuscriber(JSONObject ps_json_obj,JSONArray subsciber_header_arr,String username )
    {  
	 try{         
		    cleanup();
			BasicDBObject document = new BasicDBObject();
			for(int i=0;i<subsciber_header_arr.length();i++){
				document.put( subsciber_header_arr.getString(i),    ps_json_obj.getString(subsciber_header_arr.getString(i)) );
			}
		    document.put( "subscriberid",   ps_json_obj.getString("subscriberid") );
		    boolean ga_status=chkGAData(collection,username,document);
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "fdf";
    }
	
	private static boolean chkGAData(DBCollection collection,String email,BasicDBObject document)
    {
    	boolean gadata_availability=false;
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put( "Email", email );
        
        DBCursor cursor = collection.find(whereQuery);
        
        System.out.println(cursor.size());
        LogByFileWriter.logger_info("PersonalSubscriberMongoDAO : chkGAData " + cursor.size());
        if(cursor.size()==0){
        	//addGAData(email,account);
            insertGAData(document);
        	
        }else{
        	while (cursor.hasNext()) {
            	DBObject db_obj=cursor.next();
                System.out.println(db_obj);
                LogByFileWriter.logger_info("PersonalSubscriberMongoDAO : db_obj " + db_obj);
                gadata_availability=db_obj.containsField("Email");
                
                System.out.println("gadata_availability : "+gadata_availability);
                LogByFileWriter.logger_info("PersonalSubscriberMongoDAO : " + "gadata_availability : "+gadata_availability);
                if(gadata_availability==true){
            		System.out.println("update ga in collection");
            		LogByFileWriter.logger_info("PersonalSubscriberMongoDAO : " + "update ga in collection");
            		// update ga in collection
            		updateGAData(email,document);
            	}else{
            	 // update collection
            		//addGAData(email,account);
                    insertGAData(document);
                }
            }	
        	
        }
        /*
        
        */
		return gadata_availability;
    }
	
	private static void insertGAData(BasicDBObject gadata)
    {
    	collection.insert(gadata);
    }
	private static void updateGAData( String subscriber_email,BasicDBObject gadata )
    {
    	BasicDBObject match = new BasicDBObject();
        match.put( "Email", subscriber_email );

        BasicDBObject update = new BasicDBObject();
        update.put( "$push", new BasicDBObject(gadata) );

        collection.update( match, update );
    }
}

