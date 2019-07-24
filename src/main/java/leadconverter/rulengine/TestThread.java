package leadconverter.rulengine;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import leadconverter.mongo.ListMongoDAO;

public class TestThread {

	   public static void main(String args[]) throws ParseException {
	      //ThreadDemo T1 = new ThreadDemo( "Thread-1");
	      //T1.start();
	      
	      //ThreadDemo T2 = new ThreadDemo( "Thread-2");
	      //T2.start();
		   
		   //String campaign_activate_days=ResourceBundle.getBundle("config").getString("campaign_activate_days");
		   String campaign_activate_days="3";
		   DateFormat date_formatter_with_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   
		   //Date update_list_activate_date=new Date();
	    	//    update_list_activate_date.setDate(update_list_activate_date.getDate()+Integer.parseInt(campaign_activate_days));
	       //String date_str=date_formatter_with_timestamp.format(update_list_activate_date);
	       
		   Date update_list_activate_date=null;
		   String date_str=null;
		   String start_date="2019-06-27 11:42:37";
		   int x = 1;
	       int date_count = 1;
	       int date_add = 0;
	       
	        // Exit when x becomes greater than 4 
	        while (x <= 4) 
	        { 
	            //System.out.println("Value of x:" + x); 
	  
	            // Increment the value of x for 
	            // next iteration 
	            if(x!=2){
	            	System.out.println("Value of x:" + x);
	            	System.out.println("Value of date_count:" + date_count);
	            	//update_list_activate_date=new Date();
	            	update_list_activate_date=date_formatter_with_timestamp.parse(start_date);
	            	date_add=date_count*Integer.parseInt(campaign_activate_days);
	            	update_list_activate_date.setDate(update_list_activate_date.getDate()+date_add);
	            	date_str=date_formatter_with_timestamp.format(update_list_activate_date);
	            	System.out.println("date_str:" + date_str); 
	            	date_count++;
	            }
	            x++; 
	        } 
	    
	   }   
}
