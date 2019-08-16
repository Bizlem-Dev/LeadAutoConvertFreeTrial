package leadconverter.mongo;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.ResourceBundle;

import leadconverter.mongo.ConnectionHelper;

import org.apache.sling.commons.json.JSONArray;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class PhpListInstanceChecker {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String user_name=ResourceBundle.getBundle("config").getString("add_rule_engine_UserName");
		//String add_rule_api=ResourceBundle.getBundle("config").getString("add_rule_api");
		Map<String,String> map=new HashMap<String,String>();   
	    //Adding elements to map  
	    map.put("remoteuser","abhi@gmail.com");
	    map.put("phplistInstance","config_zimbra"); 
	    map.put("funnelName","July18F01");  
	    map.put("fromName","Akhilesh Yadav");
	    map.put("fromEmailAddress","viki@gmail.com");
	    map.put("trackOpens","false");
	    map.put("trackClicks","false");
	    map.put("trackPlainTextClicks","false");
	    map.put("googleAnalyticssLinkTracking","false");
	    map.put("autoTweetAfterSending","true");
	    map.put("autoPost2SocialMedia","false");
	    //addPhpListInstanceDetails(map);
	    
	    /*
		config_amazonses
		config_gmass
		config_sendgrid
		config_zimbra
		config
		ResourceBundle.getBundle("config")
		
		viki@gmail.com akhi@gmail.com teju@gmail.com siva@gmail.com abhi@gmail.com
		*/
	    
	    ArrayList<String> user=new ArrayList<String>();
	    user.add("viki@gmail.com");
	    user.add("akhi@gmail.com");
	    user.add("teju@gmail.com");
	    user.add("siva@gmail.com");
	    user.add("abhi@gmail.com");
	    
	    ListIterator<String> litr=user.listIterator();
	    String instance_name=null;
	    String logged_in_user=null;
	    while(litr.hasNext()){
	    	logged_in_user=litr.next();
	    	instance_name=getPhpListInstanceDetails(logged_in_user);
			String List_Add_Url=ResourceBundle.getBundle(instance_name).getString("List_Add_Url");
			
			System.out.println("List_Add_Url : "+List_Add_Url);
	    }
	    /*
	    String instance_name=null;
	    String logged_in_user=null;
	    logged_in_user=litr.next();
    	instance_name=getPhpListInstanceDetails(logged_in_user);
		String List_Add_Url=ResourceBundle.getBundle(instance_name).getString("List_Add_Url");
	    */
	    
	}
	
	public static void getPhplistInstance(String logged_in_user) {
		// TODO Auto-generated method stub
		String user_name=ResourceBundle.getBundle("config").getString("add_rule_engine_UserName");
	}
	
	public static String getPhpListInstanceDetails(String CreatedBy) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    String phplist_instance=null;
	    
	    try {
	        mongoClient=ConnectionHelper.getConnection();
	        database=mongoClient.getDatabase("phplisttest");
	        collection=database.getCollection("phplist_instance_details");
	        Bson filter1 =eq("remoteuser", CreatedBy);
	        DistinctIterable<String> di = collection.distinct("phplistInstance", filter1,String.class);
	        MongoCursor<String> cursor = di.iterator();
	            while(cursor.hasNext()) {
		        	phplist_instance=cursor.next();
				}
		    }
	        catch (Exception ex) {
	            System.out.println("Error : "+ex.getMessage());
			}
	    return phplist_instance;
	    }
	
	public static void addPhpListInstanceDetails(Map<String,String> funnel_details_map){
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> collection = null;
	    try {
        	mongoClient=ConnectionHelper.getConnection();
            database=mongoClient.getDatabase("phplisttest");
            collection=database.getCollection("phplist_instance_details");
            Document funnel_details_doc = new Document();
            
            funnel_details_doc.put("remoteuser",funnel_details_map.get("remoteuser"));
            funnel_details_doc.put("phplistInstance",funnel_details_map.get("phplistInstance"));
            funnel_details_doc.put("funnelName",funnel_details_map.get("funnelName"));  
            funnel_details_doc.put("fromName",funnel_details_map.get("fromName"));
            funnel_details_doc.put("fromEmailAddress",funnel_details_map.get("fromEmailAddress"));
            funnel_details_doc.put("trackOpens",funnel_details_map.get("trackOpens"));
            funnel_details_doc.put("trackClicks",funnel_details_map.get("trackClicks"));
            funnel_details_doc.put("trackPlainTextClicks",funnel_details_map.get("trackPlainTextClicks"));
            funnel_details_doc.put("googleAnalyticssLinkTracking",funnel_details_map.get("googleAnalyticssLinkTracking"));
            funnel_details_doc.put("autoTweetAfterSending",funnel_details_map.get("autoTweetAfterSending"));
            funnel_details_doc.put("autoPost2SocialMedia",funnel_details_map.get("autoPost2SocialMedia"));
            collection.insertOne(funnel_details_doc);;
                     
           } catch (Exception ex) {
            System.out.println("Error : "+ex.getMessage());
		}
	}

}
