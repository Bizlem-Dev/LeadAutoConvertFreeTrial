package leadconverter.mongo;

import java.text.SimpleDateFormat;  
import java.util.ArrayList;
import java.util.Date;  
import java.util.List;
import java.util.TimeZone;

import org.apache.sling.commons.json.JSONObject;
import org.bson.Document;
public class StringToDateExample2 {  
public static void main(String[] args)throws Exception {  
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss zzz");
	 
    Date date = new Date();
    
    List<Document> writes = new ArrayList<Document>();

    System.out.println("Local Time: " + sdf.format(date));
    
    JSONObject json=new JSONObject();
    json.put("name", "Akhilesh");
    json.put("Surname", "Yadav");
  
    Document doc=new Document();
    doc.put("Person", json);
    System.out.println("doc " + doc);
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    System.out.println("GMT Time  : " + sdf.format(date));
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
    System.out.println(sDate0+"\t"+date0);
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
}  
}  
