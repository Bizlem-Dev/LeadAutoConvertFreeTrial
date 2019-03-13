package leadconverter.mongo;

import java.net.UnknownHostException;
import java.text.DateFormat;
//import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
 

import java.util.Locale;
import java.util.TimeZone;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
  
/**
 * Java + MongoDB Simple Example
 * 
 */
public class CRUDOnDate {
     
 
	  static String array_names[] = {"John","Tim","Brit","Robin","Smith","Lora","Jennifer","Lyla","Victor","Adam"};
	     
	    static String array_address[][] ={
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
	    
	    public static String createOneUsingDocument(String coll_name,Document doc){
			//json.get(key)
			MongoClient mongoClient = null;
		    MongoDatabase database  = null;
		    MongoCollection<Document> collection = null;
		    //Document doc=Document.parse(json.toString());
		    //System.out.println("doc : "+doc);
		    		
	        try {
	        	mongoClient=ConnectionHelper.getConnection();
	            database=mongoClient.getDatabase("date");
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
	    
	    public static void main(String[] args) {
	    	//public Document(Map<String,Object> map)
	    	String strDate="27 Sep 2018 17:06";
	        //Thu Sep 27 16:54:30 IST 2018
	    	SimpleDateFormat dateFormatter=new SimpleDateFormat("dd MMM yyyy HH:mm"); 
	    	try {
				Date date=dateFormatter.parse(strDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    	
	    	
	    }
     
  public static void mainBackUp(String[] args) {
  
    try {
   
    // Connect to mongodb
    MongoClient mongo = new MongoClient("localhost", 27017);
  
    // get database 
    // if database doesn't exists, mongodb will create it for you
    DB db = mongo.getDB("date");
  
    // get collection
    // if collection doesn't exists, mongodb will create it for you
    DBCollection collection = db.getCollection("emps");
  
    /**** Insert ****/
    // create a document to store key and value
     
    BasicDBObject document ;
    String address[];
    //11:36
    String strDate="27 Sep 2018 17:06";
    //Thu Sep 27 16:54:30 IST 2018
	SimpleDateFormat dateFormatter=new SimpleDateFormat("dd MMM yyyy HH:mm"); 
	Date date=dateFormatter.parse(strDate); 
	//dateFormatter.parse(sendstart)
	System.out.println("Local Time: " + date);
	//date.setHours(date.getHours()-5);
	//date.setMinutes(date.getMinutes()-30);
	//System.out.println("Local Time: " + date);
	
	document=new BasicDBObject();
	//document.append("Akhiles",dateFormatter.parse(dateFormatter.format(date)));
	document.append("Akhiles",dateFormatter.parse(strDate));
	collection.insert(document);
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy MMM dd HH:mm:ss zzz");
	sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
	//System.out.println(sdf1.format(date));
    //System.out.println("GMT Time  : " + sdf1.parse(sdf1.format(date)));
    
    //System.out.println("GMT Time 1  : " + new Date(sdf1.format(date)));
    //sdf1.for
    
    
	System.out.println("Local Time 1 : " + date);
    BasicDBObject query = new BasicDBObject("Akhiles", new BasicDBObject("$gte",date));
    DBCursor cursor = collection.find(query);
    try {
       while(cursor.hasNext()) {
           System.out.println(cursor.next());
       }
    } finally {
       cursor.close();
    }
	
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss zzz");
	Date date1 = new Date();
    //System.out.println("Local Time: " + sdf.format(date1));
    document=new BasicDBObject();
	document.append("Akhiles",sdf.parse(sdf.format(date1)));
	//collection.insert(document);
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    //System.out.println("GMT Time  : " + sdf.format(date1));
    
    document=new BasicDBObject();
	document.append("Akhiles",sdf.parse(sdf.format(date1)));
	//collection.insert(document);
	
	//new date();

	
	//2018-09-27T28:16:00Z
	//2018-09-27T22:46:00Z
	//            5:30
	            
	//Bson filter = new Document("$gte", format.parse("2015-05-01T00:00:00Z")).append("$lt", format.parse("2015-05-02T00:00:00Z"));
	//long count = db.getCollection("colection").count(new Document("field",filter) );
	
	/*
    BasicDBObject query = new BasicDBObject("join", new BasicDBObject("$gt",date));
    DBCursor cursor = collection.find(query);
    try {
       while(cursor.hasNext()) {
           System.out.println(cursor.next());
       }
    } finally {
       cursor.close();
    }
    */
    
    //YYYY-MM-DDThh::mm::ss
    //String strDate="25 Sep 2018 17:06";
    /*
    String strDate1="2015-11-01T00:00:00Z";
	SimpleDateFormat dateFormatter1=new SimpleDateFormat("YYYY-MM-DDThh:mm:ss"); 
	Date date1=dateFormatter1.parse(strDate1); 
	System.out.println("date1 : "+date1);
	*/
    /*
    for(int i = 0 ; i < array_names.length ; i++){
    	if(i==0){
	        document = new BasicDBObject();
	        //value -> String
	        document.append("name", array_names[i]); 
	        // value -> int
	        document.append("age", (int)(Math.random()*60));
	        // value -> date
	        //25 Sep 2018 17:06
	         //YYYY-MM-DDThh::mm::ss
	        String strDate="25 Sep 2018 17:06";
	    	SimpleDateFormat dateFormatter=new SimpleDateFormat("dd MMM yyyy HH:mm"); 
	    	Date date=dateFormatter.parse(strDate); 
	        document.append("join", date);
	        // value -> array
	        document.append("friends", pickFriends()); 
	         
	        address = pickAddress();
	        // value --> document
	        document.append("address", new BasicDBObject("country",address[0])
	                                    .append("state", address[1])
	                                    .append("city", address[2])); 
	        collection.insert(document);
    	}
 
    }
    */
	/*
	// get count
	System.out.println("All Persons: "+collection.getCount());
    //------------------------------------
    // get all document
    DBCursor cursor = collection.find();
    try {
       while(cursor.hasNext()) {
           System.out.println(cursor.next());
       }
    } finally {
       cursor.close();
    }
    
    //------------------------------------
 
    // get documents by query
    BasicDBObject query = new BasicDBObject("age", new BasicDBObject("$gt", 40));
 
    cursor = collection.find(query);
    System.out.println("Person with age > 40 --> "+cursor.count());
     
  
   //update documents found by query "age > 30" with udpateObj "age = 20"
    BasicDBObject newDocument = new BasicDBObject();
    newDocument.put("age", 20);
  
    BasicDBObject updateObj = new BasicDBObject();
    updateObj.put("$set", newDocument);
  
    collection.update(query, updateObj,false,true);
  
    cursor = collection.find(query);
    System.out.println("Person with age > 40 after update --> "+cursor.count());
     
     
    //get all again
    cursor = collection.find();
    try {
       while(cursor.hasNext()) {
           System.out.println(cursor.next());
       }
    } finally {
       cursor.close();
    }
    */
    /**** Done ****/
    System.out.println("Done");
  
    } catch (MongoException e) {
            e.printStackTrace();
    } catch (Exception e) {
            e.printStackTrace();
    }
  
  }
	  //----------------------------------------------------
	  //These methods are just jused to build random data
	  private static String[] pickFriends(){
	      int numberOfFriends = (int) (Math.random()* 10);
	      LinkedList<String> friends = new LinkedList<String>();
	      int random = 0;
	      while(friends.size() < numberOfFriends){
	          random = (int) (Math.random()*10);
	          if(!friends.contains(array_names[random]))
	              friends.add(array_names[random]);
	               
	      }
	      String a[] = {};
	      return  friends.toArray(a);
	  }
	  private static String[] pickAddress(){
	      int random = (int) (Math.random()*10);
	      return array_address[random];
	  }
	}