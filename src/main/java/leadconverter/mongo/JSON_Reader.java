package leadconverter.mongo;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;


import com.mongodb.util.JSON;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;




public class JSON_Reader
{
    public static void main(String args[])
    {
       
        try
        {  
        	/*
        	// JSONParser parser = new JSONParser();
            Object object = parser
                    .parse(new FileReader("E:\\bizlem\\LeadAutoConverter\\sample.txt"));
            
            //convert Object to JSONObject
            JSONArray campaignArray = (JSONArray)object;
            JSONObject campaignObject=null;
            String campaignid=null;
            String uuid=null;
            String subject=null;
            String status=null;
            String sendstart=null;
            String sent=null;
            String bouncecount=null;
            String fwds=null;
            String viewed=null;
            String clicks=null;
            String rate=null;
            String linkcount=null;
            String subscribercount=null;
            
            JSONArray subscribersArray = null;
            JSONObject subscriberObject=null;
            String subscriberCampaignid=null;
            String userid=null;
            String email=null;
            String subscriberUuid=null;
            String subscriberViewed=null;
            
            JSONArray campaignclickstatisticsArray = null;
            
            JSONObject campaignclickstatisticsObject=null;
            String campaignClickStatisticsFirstClick=null;
            String campaignClickStatisticsLatestClick=null;
            String campaignClickStatisticsClicks=null;
            
            JSONArray urlclickstatisticsArray = null;
            
            JSONObject urlclickstatisticsObject=null;
            String urlclickstatisticsFirstClick=null;
            String urlclickstatisticsLatestClick=null;
            String urlclickstatisticsClicks=null;
            String urlclickstatisticsUrl=null;
            String urlclickstatisticsMessageid=null;
            
            
            
            for(int i=0;i<campaignArray.size();i++){
            	campaignObject=(JSONObject) campaignArray.get(i);
            	campaignid = (String) campaignObject.get("campaignid");
            	//insert2Funnel(campaignid,campaignObject);
            	System.out.println(campaignid);
            	subscribersArray=(JSONArray) campaignObject.get("subscribers");
            	for(int j=0;j<subscribersArray.size();j++){
            		subscriberObject=(JSONObject) subscribersArray.get(j);
            		userid=(String) subscriberObject.get("userid");
            		System.out.println(userid);
            		campaignclickstatisticsArray=(JSONArray) subscriberObject.get("campaignclickstatistics");
            		for(int k=0;k<campaignclickstatisticsArray.size();k++){
            			campaignclickstatisticsObject=(JSONObject) campaignclickstatisticsArray.get(k);
            			System.out.println(campaignclickstatisticsObject);
            		}
            		urlclickstatisticsArray=(JSONArray) subscriberObject.get("urlclickstatistics");
            		for(int l=0;l<urlclickstatisticsArray.size();l++){
            			urlclickstatisticsObject=(JSONObject) urlclickstatisticsArray.get(l);
            			System.out.println(urlclickstatisticsObject);
            		}
            		
            	}
            }
            */
            
            //System.out.println(campaignArray);
            
            /*
            //Reading the String
            String name = (String) jsonObject.get("Name");
            Long age = (Long) jsonObject.get("Age");
            
            //Reading the array
            JSONArray countries = (JSONArray)jsonObject.get("Countries");
            
            //Printing all the values
            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
            System.out.println("Countries:");
            for(Object country : countries)
            {
                System.out.println("\t"+country.toString());
            }
            */
        }
        catch(Exception fe)
        {
            fe.printStackTrace();
        }
       
    }
    public static String insert2Funnel(String campignId,String funnelDetails,org.apache.sling.commons.json.JSONObject campaignObject1,org.apache.sling.commons.json.JSONArray subscribers_json_arr){
    	MongoDAO mdao=new MongoDAO();
    	
    	JSONObject  campaignObject=null;
    	JSONObject  funnelDetailsJsonObj=null;
    	String funnelNodeName=null;
    	
    	try {
		        campaignObject=new JSONObject(campaignObject1.toString());
		        funnelDetailsJsonObj=new JSONObject(funnelDetails.toString());
		        funnelNodeName=funnelDetailsJsonObj.getString("funnelNodeName");
    		    //JSON.parse(subscribers_json_arr.toString())
    		    
			
				JSONArray subfunnelList=new JSONArray();
				          subfunnelList.put("Unknown");
				          subfunnelList.put("Warm");
				          subfunnelList.put("Entice");
				          subfunnelList.put("Convert");
				
				JSONArray Unknown=new JSONArray();
				          Unknown.put(campaignObject);
				
				JSONArray Warm=new JSONArray();
				          Warm.put(campaignObject);
				
				JSONArray Entice=new JSONArray();
				          Entice.put(campaignObject);
				
				JSONArray Convert=new JSONArray();
				          Convert.put(campaignObject);
				
				Document funnelDoc = new Document("funnel_id",funnelNodeName)
						.append("funnelName", "RealState")
		                .append("subfunnel", JSON.parse(subfunnelList.toString()))
		                .append("Unknown", JSON.parse(Unknown.toString()))
		                .append("Warm", JSON.parse(Warm.toString()))
		                .append("Entice", JSON.parse(Entice.toString()))
		                .append("Convert",JSON.parse(Convert.toString()));
			//	mdao.createOne("funnel", funnelDoc);
				
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return campaignObject.toString();  
    }
    
    public static String checkCampaignInSubfunnel(String campignId,JSONObject campaignObject){
		return campignId;
    	
    	
    }
}
