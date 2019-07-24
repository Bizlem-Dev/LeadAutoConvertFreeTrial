package leadconverter.create.rule;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import leadconverter.mongo.ConnectionHelper;
import leadconverter.mongo.ListMongoDAO;
import leadconverter.mongo.MongoDAO;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//addCampaignsToActiveList("2019-07-01 15:45:03");
		//new Test2().getSubscriberCountForLoggedInUserForFreeTrail("subscribers_details","viki@gmail.com");
		MongoDAO mdao=new MongoDAO();
		mdao.getSubscriberCountForLoggedInUserForFreeTrail("subscribers_details","viki@gmail.com");
	}
	public long getSubscriberCountForLoggedInUserForFreeTrail(String coll_name,String logged_in_user_email) {
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> subscribers_details_collection = null;
	    long subscriber_count=0;
	    try {
	        mongoClient=ConnectionHelper.getConnection();
	        database=mongoClient.getDatabase("phplisttest");
	        subscribers_details_collection=database.getCollection(coll_name);
	        Bson filter =eq("CreatedBy", logged_in_user_email);
	        subscriber_count=subscribers_details_collection.count(filter);
	        System.out.println("subscriber_count : "+subscriber_count);
						
	    } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.closeConnection(mongoClient);
		}
        return subscriber_count;
    }
	public static void addCampaignsToActiveList(String activeListStartDate){
		DateFormat simpleDateFormatTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String campaignActivateDays=ResourceBundle.getBundle("config").getString("campaign_activate_days");
		String campaignActivateHr=ResourceBundle.getBundle("config").getString("campaign_activate_hrs");
		try {
			Node campaignNode=null;
			Node activeLitCampaignChildNode=null;
			
			String Campaign_Name=null;
			String Campaign_Id=null;
			String campaign_status=null;
			String activeListId=null;
			int dateCounter = 0;
		    int noOfDaysAdded = 0;
		    Date campaign_send_date=null;
		    String campaignSendDate=null;
		    Document list_campaign_doc=null;
		    
		    //campaign_send_date=simpleDateFormatTimeStamp.parse(activeListStartDate);
							
		     int i=10;
	         while(i>0){
	        	 
			     noOfDaysAdded=dateCounter*Integer.parseInt(campaignActivateDays);
			     System.out.println("dateCounter :" + dateCounter+"   noOfDaysAdded :" + noOfDaysAdded);
			     campaign_send_date=simpleDateFormatTimeStamp.parse(activeListStartDate);
			     campaign_send_date.setDate(campaign_send_date.getDate()+noOfDaysAdded);
			     //campaign_send_date.setHours(campaign_send_date.getHours()-Integer.parseInt(campaignActivateHr));
			     campaignSendDate=simpleDateFormatTimeStamp.format(campaign_send_date);
			     
			     System.out.println("campaignActivateDays:" + campaignActivateDays+"   campaign Send Date:" + campaignSendDate); 
	             
	             dateCounter++;
			     i--;
			}
		}
		catch (Exception ex) {
			// TODO Auto-generated catch block
			System.out.println("Date Exception : " + ex.getMessage());
		}
	}

}
