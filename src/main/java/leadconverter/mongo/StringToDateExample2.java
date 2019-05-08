package leadconverter.mongo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;  
import java.util.ArrayList;
import java.util.Date;  
import java.util.List;
import java.util.TimeZone;

import org.apache.sling.commons.json.JSONObject;
import org.bson.Document;
public class StringToDateExample2 {  
public static void main(String[] args)throws Exception {  
	
	
	String currentCampign="viki_gmail.com_Explore_1";
	System.out.println(" index length  : "+currentCampign.length());
	System.out.println(" index : "+currentCampign.substring(currentCampign.length()-1, currentCampign.length()));
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //Date date1 = sdf.parse("2009-12-31");
    //Date date2 = sdf.parse("2010-01-31");
    
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
    /*
    Date date1=formatter1.parse(sDate1);  
    Date date2=formatter2.parse(sDate2);  
    Date date3=formatter3.parse(sDate3);  
    Date date4=formatter4.parse(sDate4);  
    Date date5=formatter5.parse(sDate5);  
    Date date6=formatter6.parse(sDate6); 
    */
    //System.out.println(sDate0+"\t"+date0);
    /*
    System.out.println(sDate0+"\t"+date0);
    System.out.println(sDate1+"\t"+date1); 
    System.out.println(sDate1+"\t"+date1);  
    System.out.println(sDate2+"\t"+date2);  
    System.out.println(sDate3+"\t"+date3);  
    System.out.println(sDate4+"\t"+date4);  
    System.out.println(sDate5+"\t"+date5);  
    System.out.println(sDate6+"\t"+date6); 
    */ 
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
