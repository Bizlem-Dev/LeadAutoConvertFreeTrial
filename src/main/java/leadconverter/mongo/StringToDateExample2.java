package leadconverter.mongo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;  
import java.util.ArrayList;
import java.util.Date;  
import java.util.List;
import java.util.TimeZone;

import leadconverter.doctiger.LogByFileWriter;
import leadconverter.rulengine.RulEngineMongoDAO;
import leadconverter.rulengine.RuleEngineCall;

import org.apache.sling.commons.json.JSONObject;
import org.bson.Document;
import org.json.JSONArray;
public class StringToDateExample2 {  
public static void main(String[] args)throws Exception {  
	
	//String filepathfromourside="/home/ubuntu/TestedMailJSon/"+attachmentNode.getName().toString()+"_"+timestampDateAndTime+".txt";
	//SimpleDateFormat sdf156 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//SimpleDateFormat sdf156 = new SimpleDateFormat("yyyy-MM-dd");
	//Date date_campare156 = new Date();
	//String date156_str = sdf156.format(date_campare156);
	//log4j.appender.file.File=E:\\bizlem\\LeadAutoConverter\\log4j\\logs\\GALeadConverter.log
	
	
	//logger_info_by_filewrite(filepathfromourside,filepathfromourside);
	
	String currentCampign="viki_gmail.com_Explore_1";
	System.out.println(" index length  : "+currentCampign.length());
	//logger_info(" index length  : "+currentCampign.length(),filepathfromourside);
	System.out.println(" index : "+currentCampign.substring(currentCampign.length()-1, currentCampign.length()));
	//logger_info(" index : "+currentCampign.substring(currentCampign.length()-1, currentCampign.length())+currentCampign.length(),filepathfromourside);
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
    //Date date1 = sdf.parse("2009-12-31");
    //Date date2 = sdf.parse("2010-01-31");
	
	//Apr 21, 2019, 9:36:00 PM
    
	/*
    Date date_campare1 = new Date();
    Date date_campare2 = new Date();
    
    Date date1 = sdf1.parse(sdf1.format(date_campare1));
    Date date2 = sdf1.parse("2019-03-29 19:20:51");
    
    System.out.println("date1 : " + sdf1.format(date1));
    System.out.println("date2 : " + sdf1.format(date2));

    if (date1.compareTo(date2) > 0) {
        System.out.println("Date1 is after Date2");
    } else if (date1.compareTo(date2) < 0) {
        System.out.println("Date1 is before Date2");
    } else if (date1.compareTo(date2) == 0) {
        System.out.println("Date1 is equal to Date2");
    } else {
        System.out.println("How to get here?");
    }
	*/
	
	
    System.out.println("===========================================================================");
	
	
	
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date101 = new Date();
	String date_str=dateFormat.format(date101);
	System.out.println("Today's Date : "+date_str); //2016/11/16 12:08:43
	
	//Date date_after_addition = new Date(date_str);
	//System.out.println("date_after_addition  : "+date_after_addition); //2016/11/16 12:08:43
	
	
	
	DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateInString = "7-Jun-2013";

    try {

        Date date = dateFormat1.parse(date_str);
        int date_difference=45;
        date.setDate(date.getDate()+date_difference);
        System.out.println("date : "+date);
        System.out.println("dateFormat1 : "+dateFormat1.format(date));
        
        Date date102 = new Date();
        int date_difference1=45;
        date102.setDate(date102.getDate()+date_difference1);
        System.out.println("date102 : "+date102);
        System.out.println("dateFormat1 date102 : "+dateFormat1.format(date102));

    } catch (ParseException e) {
        e.printStackTrace();
    }
	
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss zzz");
	 
    Date date = new Date();
    
    List<Document> writes = new ArrayList<Document>();

    //System.out.println("Local Time: " + sdf.format(date));
    
    JSONObject json=new JSONObject();
    json.put("name", "Akhilesh");
    json.put("Surname", "Yadav");
  
    Document doc=new Document();
    doc.put("Person", json);
    //System.out.println("doc " + doc);
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    //System.out.println("GMT Time  : " + sdf.format(date));
  //DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
    //format.parse("2018-09-27T28:16:00Z")
	//25 Sep 2018 17:06
	//      ISODate("2018-12-18T13:39:56.735Z")
	//new Date()   Wed Dec 19 11:24:16 IST 2018
	//System.out.println("new Date() : "+new Date());
	String sDate0="25 Sep 2018 17:06";
	SimpleDateFormat formatter0=new SimpleDateFormat("dd MMM yyyy HH:mm"); 
	Date date0=formatter0.parse(sDate0); 
	//System.out.println(sDate0.replace(" ", "-"));
	
	//       sDate0=sDate0.re
    String sDate1="31/12/1998";  
    String sDate2 = "31-Dec-1998";  
    String sDate3 = "12 31, 1998";  
    String sDate4 = "Thu, Dec 31 1998";  
    String sDate5 = "Thu, Dec 31 1998 23:37:50";  
    String sDate6 = "31-Dec-1998 23:37:50";  
    //SimpleDateFormat formatter0=new SimpleDateFormat("dd MMM yyyy HH:mm"); 
    
    //2019-03-15 01:00:00
     
    
    
    
    SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");  
    SimpleDateFormat formatter2=new SimpleDateFormat("dd-MMM-yyyy");  
    SimpleDateFormat formatter3=new SimpleDateFormat("MM dd, yyyy");  
    SimpleDateFormat formatter4=new SimpleDateFormat("E, MMM dd yyyy");  
    SimpleDateFormat formatter5=new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");  
    SimpleDateFormat formatter6=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    
    
    //Date date0=formatter0.parse(sDate0); 
    
    Date date1=formatter1.parse(sDate1);  
    Date date2=formatter2.parse(sDate2);  
    Date date3=formatter3.parse(sDate3);  
    Date date4=formatter4.parse(sDate4);  
    Date date5=formatter5.parse(sDate5);  
    Date date6=formatter6.parse(sDate6); 
    
    //System.out.println(sDate0+"\t"+date0);
    
    System.out.println(sDate0+"\t"+date0);
    System.out.println(sDate1+"\t"+date1); 
    System.out.println(sDate1+"\t"+date1);  
    System.out.println(sDate2+"\t"+date2);  
    System.out.println(sDate3+"\t"+date3);  
    System.out.println(sDate4+"\t"+date4);  
    System.out.println(sDate5+"\t"+date5);  
    System.out.println(sDate6+"\t"+date6); 
    
    
  //Apr 21, 2019
  	//20190513
  	String date_str_txt="20191213";
  	String final_date_str=date_str_txt.substring(0, 4)+"-"+date_str_txt.substring(4, 6)+"-"+date_str_txt.substring(6, 8);
  	System.out.println(" final_date_str  : "+final_date_str);
  	
  	SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");
  	Date date105 = sdf10.parse(final_date_str);
  	System.out.println(" final_date_str date1  : "+date105);
  	
  	
    //SimpleDateFormat sdf101 = new SimpleDateFormat("MMM dd, yyyy");
  	SimpleDateFormat sdf101 = new SimpleDateFormat("yyyy-MM-dd");
    //Date date1012 = sdf101.parse("Apr 21, 2019");
    
    Date date_campare1 = new Date();
    Date date_campare2 = new Date();
    
    Date tmp_date1 = sdf101.parse(sdf101.format(date_campare1));
    Date tmp_date2 = sdf101.parse(final_date_str);
    
    System.out.println("date1 : " + sdf101.format(tmp_date1));
    System.out.println("date2 : " + sdf101.format(tmp_date2));

    if (tmp_date1.compareTo(tmp_date2) > 0) {
        System.out.println("Date1 is after Date2");
    } else if (tmp_date1.compareTo(tmp_date2) < 0) {
        System.out.println("Date1 is before Date2");
    } else if (tmp_date1.compareTo(tmp_date2) == 0) {
        System.out.println("Date1 is equal to Date2");
    } else {
        System.out.println("How to get here?");
    }
    
    String date_str_txt_temp="201905140031";
    String final_date_str_temp=date_str_txt.substring(0, 4)+"-"+date_str_txt.substring(4, 6)+"-"+date_str_txt.substring(6, 8);
    String final_date_str_minute=date_str_txt_temp.substring(8, 10);
    String final_date_str_secount=date_str_txt_temp.substring(10, 12);
    
    String final_date_str_temp1=date_str_txt.substring(0, 4)+"-"+date_str_txt.substring(4, 6)+"-"+date_str_txt.substring(6, 8)+" "+
    		date_str_txt_temp.substring(8, 10)+":"+date_str_txt_temp.substring(10, 12)+":00";
    System.out.println(" final_date_str_temp  : "+final_date_str_temp);
    System.out.println(" final_date_str_minute  : "+final_date_str_minute);
    System.out.println(" final_date_str_secount  : "+final_date_str_secount);
    
    
    
    System.out.println(" final_date_str_temp1  : "+final_date_str_temp1);
    
    DateFormat dateFormat108 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    //dateFormat108.parse(final_date_str_temp1);
    System.out.println(" final_date_str_temp1 date : "+dateFormat108.parse(final_date_str_temp1));
    //String dateInString = "7-Jun-2013";
    
    //System.out.println("GetSubscriberEmailID : "+RuleEngineCall.GetSubscriberEmailID("2100"));
    
    //String date1012_str=sdf101.format(date1012);
	//System.out.println("Today's Date date1012_str: "+date1012_str); //2016/11/16 12:08:43
    String hostname="bizlem.com";
    
    String path1="/useCases.html?EMAIL=purva.sawant@bizlem.com";  //bizlem.com/useCases.html
    //String path1="/products.html";   //bizlem.com/products.html
    //String path1="/";
    //String path1="/?EMAIL=akhilesh@bizlem.com";
    
    if(path1.length()==1){
    	path1=hostname;
    	System.out.println(path1);
    }else{
       if(path1.contains("/?")){
    	   path1=hostname+path1.substring(0, path1.indexOf("/?"));
    	   //System.out.println(path1);
       }else if(path1.contains("?")){
    	   path1=hostname+path1.substring(0, path1.indexOf("?"));
       }else if(path1.contains("/")){
    	   path1=hostname+path1;
       }
    }
    System.out.println(path1);
    /*
    for(int ak=0;ak<500;ak++){
    	System.out.println("i = "+ak);
    	LogByFileWriter.logger_info("-------");
    	LogByFileWriter.logger_info("Before we go ahead, One key point I would like to add that unlike other data types in Java, Strings are immutable. By immutable, we mean that Strings are constant, their values cannot be changed after they are created. Because String objects are immutable, they can be shared. For example");
    	LogByFileWriter.logger_info(path1);
    }
    */
    /*
      RulEngineMongoDAO rdao=new RulEngineMongoDAO();
	  //JSONArray mainJsonArray=new JSONArray(rdao.subscribersViewBasedOnFunnelData("url",campaignId,subscriberId).toString());
	  //JSONArray mainJsonArray=new JSONArray(rdao.subscribersViewBasedOnFunnelData("subscribers",campaignId,subscriberId).toString());
	  JSONArray mainJsonArray=new JSONArray(rdao.tempSubscribersViewBasedOnFunnelData("subscribers").toString());
	  System.out.println(" temp_subscribers "+mainJsonArray);
	  */
    DateFormat dateFormat1087 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat date_formatter=new SimpleDateFormat("yyyy-MM-dd"); 
	  
     
    /*
     *  31/12/1998	                  Thu Dec 31 00:00:00 IST 1998
		31-Dec-1998	                  Thu Dec 31 00:00:00 IST 1998
		12 31, 1998	                  Thu Dec 31 00:00:00 IST 1998
		Thu, Dec 31 1998	          Thu Dec 31 00:00:00 IST 1998
		Thu, Dec 31 1998 23:37:50	  Thu Dec 31 23:37:50 IST 1998
		31-Dec-1998 23:37:50	      Thu Dec 31 23:37:50 IST 1998
		                              Wed Dec 19 11:24:16 IST 2018
     */
    
    
    /*
     *SimpleDateFormat formatter7=new SimpleDateFormat("dd MMM yyyy HH:mm");
    Date today = new Date();
    String today_str=formatter7.format(today);
    //System.out.println("today_str : "+today_str);
    //System.out.println("today_str : "+"27 Sep 2018 09:49");
    
    Date date_campare1 = new Date();
    date_campare1.setDate(date_campare1.getDate()-7);
    Date date_campare2 = new Date();
    
    Date date1 = formatter7.parse(formatter7.format(date_campare1));
    Date date2 = formatter7.parse("12 Apr 2019 12:52");
    
    
    
    System.out.println("date1 : " + formatter7.format(date1));
    System.out.println("date2 : " + formatter7.format(date2));
    String recent=null;
    if (date2.compareTo(date1) > 0) {
        System.out.println("Date2 is after Date1");
        recent="YES";
    } else if (date2.compareTo(date1) < 0) {
        System.out.println("Date2 is before Date1");
        recent="NO";
    } else if (date2.compareTo(date1) == 0) {
        System.out.println("Date2 is equal to Date1");
        recent="YES";
    } 
    System.out.println("Is it Recent? : "+recent);
     
     */
}  

}  
