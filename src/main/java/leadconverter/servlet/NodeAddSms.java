package leadconverter.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
  @Property(name = "service.vendor", value = "VISL Company"),
  @Property(name = "sling.servlet.paths", value = { "/servlet/service/NodeAddSms" }),
  @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
  @Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
    "prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
    "productEdit" }) })

public class NodeAddSms extends SlingAllMethodsServlet {
 private static final long serialVersionUID = 1L;

 @Reference
 private SlingRepository repo;
 final String FILEEXTENSION[] = { "csv" };
 final int NUMBEROFRESULTSPERPAGE = 10;
 String fileType = "file";
 JSONObject mainjsonobject = new JSONObject();
 String urlParameters = mainjsonobject.toString();
 StringBuilder builder = new StringBuilder();
 Date currentTime = new Date();

 protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
   throws ServletException, IOException {
  PrintWriter out = response.getWriter();
 try {
  String count_Value = null;
	Node lead_converter = null;
	Node sms=null;
Node mobilenode=null;
	Session session = null;
	Node phonenumbernode=null;
	session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

	//out.println("Remote User : : :" + request.getRemoteUser());

	//SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

	//String DateToStr = format.format(currentTime);

	//out.println(" DateToStr          " + DateToStr);

	//out.println("Remote User : : :" + request.getRemoteUser());

	if (request.getRequestPathInfo().getExtension().equals("NodeAdd")) {

		String value = request.getParameter("smsdetails");
out.println(value);
		JSONObject object = new JSONObject(value);

		JSONArray jsonMainArr = object.getJSONArray("SmsDetails"); 

		for (int i = 0; i < jsonMainArr.length(); i++) {  	// **line 2**
		     JSONObject data = jsonMainArr.getJSONObject(i);
	
		     String name = data.getString("Name");
				String phonenumber = data.getString("Phone_Number");

				out.println("Name  : " + name);
				out.println("Phone NUmber  : " + phonenumber);

				Node content = session.getRootNode().getNode("content");

				if (!content.hasNode("Lead_Converter")) {
					lead_converter = content.addNode("Lead_Converter");
					out.println("Lead_Converter Node : : : " + lead_converter);
				} else {
					lead_converter = content.getNode("Lead_Converter");
					out.println("Lead_Converter Else : : :" + lead_converter);
				}
				  
			    if (!lead_converter.hasNode("Sms")) {
				      
				       sms=lead_converter.addNode("Sms");
				       out.println("Sms If"+sms);
				
				     } else {
				      sms = lead_converter.getNode("Sms");
				       out.println("Sms Else"+sms);

				     }
				     
				
				 	if (!sms.hasNode(phonenumber)) {
				 		phonenumbernode = sms.addNode(phonenumber);
				 		phonenumbernode.setProperty("Name", name);
				 		phonenumbernode.setProperty("Mobile_No", phonenumber);
				    


					} else {
						phonenumbernode = sms.getNode(phonenumber);
						phonenumbernode.setProperty("Name", name);
						phonenumbernode.setProperty("Mobile_No", phonenumber);
					}
			       
			}
		session.save();


				}

} catch (Exception e) {
	out.println(e.getMessage());
}

 }
}
 
 
 
