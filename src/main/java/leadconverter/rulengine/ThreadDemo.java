package leadconverter.rulengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.ServletException;

class ThreadDemo extends Thread {
	   private Thread t;
	   private String threadName;
	   
	   ThreadDemo( String name) {
	      threadName = name;
	      System.out.println("Creating " +  threadName );
	   }
	   
	   public void run() {
	      System.out.println("Running " +  threadName );
	      String url = ResourceBundle.getBundle("config").getString("Delete_Subscriber_From_List");
	  		String subscriberaddurl = ResourceBundle.getBundle("config").getString("Add_Subscriber_In_List");
	  		String ListId="901";
	  		String DraftListId="901";
	  		String SubscriberId="2074";
	  		String deletesubscriberinlistparameters = "?list_id=" + ListId +"&subscriber_id="+SubscriberId;
	  		String addsubscriberinlistparameters = "?list_id=" + DraftListId +"&subscriber_id="+ SubscriberId;
	      try {
	    	
	  			SimpleDateFormat sdf156 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  	  	      System.out.println("Start  :" + sdf156.format(new Date()));
	  			  for(int i=0;i<150;i++){  
	  				  System.out.println("-----------------------------------"+i+"--------------------------");
	  			      //sendpostdata("http://35.237.183.3/restapi/list-subscriber/listSubscribers.php","?list_id=901");
	  				  String apiresponse =sendpostdata(url,deletesubscriberinlistparameters.replace(" ", "%20")).replace("<pre>", "");
	  				  String responsedata =sendpostdata(subscriberaddurl,addsubscriberinlistparameters.replace(" ","%20")).replace("<pre>", "");
	  				  if(i==29){
	  				     Thread.sleep(1000*60);
	  				  }
	  			  }
	  			  System.out.println("End :" + sdf156.format(new Date()));
	         
	         
	      } catch (InterruptedException e) {
	         System.out.println("Thread " +  threadName + " interrupted.");
	      }catch (Exception e) {
		         System.out.println("Thread " +  threadName + " interrupted.");
		  }
	      System.out.println("Thread " +  threadName + " exiting.");
	   }
	   
	   public void start () {
	      System.out.println("Starting " +  threadName );
	      if (t == null) {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	   }
	   public static String sendpostdata(String callurl, String urlParameters)
				throws ServletException, IOException {

					URL url = new URL(callurl + urlParameters);
			//System.out.println("Url :" + url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");

			// 
			OutputStream writer = conn.getOutputStream();

			writer.write(urlParameters.getBytes());
			int responseCode = conn.getResponseCode();
			StringBuffer buffer = new StringBuffer();
			//
			System.out.println("responseCode :" + responseCode);
			if (responseCode == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					buffer.append(inputLine);
				}
				in.close();
				System.out.println("POST request Working");
				
			} else {
				System.out.println("POST request not worked");
			}
			writer.flush();
			writer.close();
			System.out.println("Response Body : "+buffer.toString());
			return buffer.toString();
		}
	}