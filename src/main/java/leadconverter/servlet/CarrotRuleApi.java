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
  @Property(name = "sling.servlet.paths", value = { "/servlet/service/CarrotRule" }),
  @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
  @Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
    "prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
    "productEdit" }) })

public class CarrotRuleApi extends SlingAllMethodsServlet {
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
	Session session = null;
	session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

	//out.println("Remote User : : :" + request.getRemoteUser());

	//SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

	//String DateToStr = format.format(currentTime);

	//out.println(" DateToStr          " + DateToStr);

	//out.println("Remote User : : :" + request.getRemoteUser());

	if (request.getRequestPathInfo().getExtension().equals("CarrotRule")) {

		String projectname = request.getParameter("projectname");

		out.println(projectname);

			session.save();


				}

} catch (Exception e) {
	out.println(e.getMessage());
}

 }
}
 
 
 
