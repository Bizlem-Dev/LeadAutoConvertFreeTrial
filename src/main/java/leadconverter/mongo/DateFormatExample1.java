package leadconverter.mongo;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class DateFormatExample1 {

    public static void main(String[] args) throws ParseException {
    	
    	
    	String dateString = "Apr 21, 2019";
    	
    	// Get the default MEDIUM/SHORT DateFormat
        DateFormat format = 
            DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM, DateFormat.SHORT);
    	
    	Date date = format.parse(dateString);
        System.out.println("Original string: " + dateString);
        System.out.println("Parsed date    : " + 
             date.toString());
    	
    	
        // Make a new Date object. It will be initialized to the current time.
        Date now = new Date();
        
        System.out.println(" 0. " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(now));
        
        

        // See what toString() returns
        System.out.println(" 1. " + now.toString());

        // Next, try the default DateFormat
        System.out.println(" 2. " + DateFormat.getInstance().format(now));

        // And the default time and date-time DateFormats
        System.out.println(" 3. " + DateFormat.getTimeInstance().format(now));
        System.out.println(" 4. " + 
            DateFormat.getDateTimeInstance().format(now));

        // Next, try the short, medium and long variants of the 
        // default time format 
        System.out.println(" 5. " + 
            DateFormat.getTimeInstance(DateFormat.SHORT).format(now));
        System.out.println(" 6. " + 
            DateFormat.getTimeInstance(DateFormat.MEDIUM).format(now));
        System.out.println(" 7. " + 
            DateFormat.getTimeInstance(DateFormat.LONG).format(now));

        // For the default date-time format, the length of both the
        // date and time elements can be specified. Here are some examples:
        System.out.println(" 8. " + DateFormat.getDateTimeInstance(
            DateFormat.SHORT, DateFormat.SHORT).format(now));
        System.out.println(" 9. " + DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM, DateFormat.SHORT).format(now));
        // 13 May, 2019 1:25 PM
        System.out.println("10. " + DateFormat.getDateTimeInstance(
            DateFormat.LONG, DateFormat.LONG).format(now));
        //13 May, 2019 1:25:14 PM IST
    }
}
