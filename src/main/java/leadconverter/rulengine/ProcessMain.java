package leadconverter.rulengine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;



public class ProcessMain {
	public static void main(String[] args) {
		int campaignId=0;
		String campaigncreate_by=null;
		StringBuilder builder = new StringBuilder();
		try {
			   //URL urlCampaign=new URL("http://35.201.178.201:8082/portal/servlet/service/PhpData.phpdata");
			   URL urlCampaign=new URL(ResourceBundle.getBundle("config").getString("campaignForRuleEngine"));
			   URLConnection urlConnectionCampaign = urlCampaign.openConnection();
		       BufferedReader bufferedReaderCampaign = new BufferedReader(new InputStreamReader(urlConnectionCampaign.getInputStream()));
		       String lineCampaign;
		       while ((lineCampaign = bufferedReaderCampaign.readLine()) != null)
		       { 
		    	   String email=null;
		    	   builder.append(lineCampaign + "\n");
		       }
		       //System.out.println("builder.toString() : "+builder.toString());
		       JSONObject jsonObject=new JSONObject(builder.toString());
		       JSONArray jsonArray = (JSONArray) jsonObject.get("Data");
		       for(int i=0;i<jsonArray.length();i++) {
		    	   JSONObject jsonobjectcampen = (JSONObject) jsonArray.get(i);
		                      campaignId=jsonobjectcampen.getInt("Campaign_Id");
		                      campaigncreate_by=jsonobjectcampen.getString("Created_By");
		           //System.out.println("jsonArray"+"   campaignId   "+campaignId+"  campaigncreate_by  : "+campaigncreate_by);
		           StringBuilder builder1 = new StringBuilder();
	               //new Subscriber(subsriberId, mailBounce, mailDelivered, mailOpen, mailBounceCount, mailHotLink1ClickCount, mailHotLink2ClickCount, mailHotLink3ClickCount, mailHotLink4ClickCount, mailHotLink5ClickCount, mailNormalLink1ClickCount, mailNormalLink2ClickCount, mailNormalLink3ClickCount, mailNormalLink4ClickCount, mailNormalLink5ClickCount, mailUnsubscribe, clickedOnHotLinks, clickedOnNormalLinks)
		             URL url=new URL(ResourceBundle.getBundle("config").getString("webservice_campaigndatabyid")+campaignId);
		             URLConnection urlConnection = url.openConnection();
			         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			         String line;
			         while ((line = bufferedReader.readLine()) != null)
			         { 
			    	   String email=null;
			    	   builder1.append(line + "\n");
			         }
	                 //System.out.println("builder1   :"+builder1.toString());
	                 JSONArray array=new JSONArray(builder1.toString());
	                 //System.out.println("array   :"+array);
	                 ProcessTest main=new ProcessTest();
	                 main.startprocess(array,campaigncreate_by);
	                 break;
	            }
	 	    }catch (Exception e) {
	 	      System.out.println(e.getMessage());
	        }
		
      }
	
	public static void callRuleEngine() {
		int campaignId=0;
		String campaigncreate_by=null;
		StringBuilder builder = new StringBuilder();
		try {
			   //URL urlCampaign=new URL("http://35.201.178.201:8082/portal/servlet/service/PhpData.phpdata");
			   URL urlCampaign=new URL(ResourceBundle.getBundle("config").getString("campaignForRuleEngine"));
			   URLConnection urlConnectionCampaign = urlCampaign.openConnection();
		       BufferedReader bufferedReaderCampaign = new BufferedReader(new InputStreamReader(urlConnectionCampaign.getInputStream()));
		       String lineCampaign;
		       while ((lineCampaign = bufferedReaderCampaign.readLine()) != null)
		       { 
		    	   String email=null;
		    	   builder.append(lineCampaign + "\n");
		       }
		       //System.out.println("builder.toString() : "+builder.toString());
		       JSONObject jsonObject=new JSONObject(builder.toString());
		       JSONArray jsonArray = (JSONArray) jsonObject.get("Data");
		       for(int i=0;i<jsonArray.length();i++) {
		    	   JSONObject jsonobjectcampen = (JSONObject) jsonArray.get(i);
		                      campaignId=jsonobjectcampen.getInt("Campaign_Id");
		                      campaigncreate_by=jsonobjectcampen.getString("Created_By");
		           //System.out.println("jsonArray"+"   campaignId   "+campaignId+"  campaigncreate_by  : "+campaigncreate_by);
		           StringBuilder builder1 = new StringBuilder();
	               //new Subscriber(subsriberId, mailBounce, mailDelivered, mailOpen, mailBounceCount, mailHotLink1ClickCount, mailHotLink2ClickCount, mailHotLink3ClickCount, mailHotLink4ClickCount, mailHotLink5ClickCount, mailNormalLink1ClickCount, mailNormalLink2ClickCount, mailNormalLink3ClickCount, mailNormalLink4ClickCount, mailNormalLink5ClickCount, mailUnsubscribe, clickedOnHotLinks, clickedOnNormalLinks)
		           //URL url=new URL("http://191.101.165.251/webservice/campaigndatabyid.php?campid="+campaignId+"");
		             URL url=new URL(ResourceBundle.getBundle("config").getString("webservice_campaigndatabyid")+campaignId);
		             URLConnection urlConnection = url.openConnection();
			         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			         String line;
			         while ((line = bufferedReader.readLine()) != null)
			         { 
			    	   String email=null;
			    	   builder1.append(line + "\n");
			         }
	                 //System.out.println("builder1   :"+builder1.toString());
	                 JSONArray array=new JSONArray(builder1.toString());
	                 //System.out.println("array   :"+array);
	                 ProcessTest main=new ProcessTest();
	                 main.startprocess(array,campaigncreate_by);
	                 break;
	            }
	 	    }catch (Exception e) {
	 	      System.out.println(e.getMessage());
	        }
		
      }
}
