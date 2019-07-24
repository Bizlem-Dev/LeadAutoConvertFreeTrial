package leadconverter.freetrail;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;

import leadconverter.freetrail.FreeTrialandCart;
import leadconverter.mongo.MongoDAO;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/freetrailervlet" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class freeTrailServlet extends SlingAllMethodsServlet {
	@Reference
	private SlingRepository repos;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		Session session = null;
		Node mailtangynode = null;
		try {

			if (request.getRequestPathInfo().getExtension().equals("get_subscriber_status")) {
				//out.println("in callservice");
				String logged_in_user_email = request.getParameter("rm_email");
				//FreeTrialandCart cart = new FreeTrialandCart();
				//String freetrialstatus = cart.checkfreetrial(logged_in_user_email);
				//mailtangynode = cart.getMailtangyNode(freetrialstatus, logged_in_user_email, "", session, response);
				
				MongoDAO mdao=new MongoDAO();
				long subscribers_count=mdao.getSubscriberCountForLoggedInUserForFreeTrail("subscribers_details",logged_in_user_email);
				String free_trail_status=new FreeTrialandCart().checkfreetrial(logged_in_user_email);
				//long subscribers_count=2000;
				//String free_trail_status="0";
				if(subscribers_count<=2000&&free_trail_status.equals("0")){
					System.out.println("Free Train is Active");
					out.println("Free Train is Active");
				}else if(free_trail_status.equals("1")){
					System.out.println("Free Train Date Expired");
					out.println("Free Train Date Expired");
				}else if(subscribers_count>2000){
					System.out.println("Subscriber Count is More");
					out.println("Subscriber Count is More");
				}

			} else if (request.getRequestPathInfo().getExtension().equals("get_subscriber_status_test")) {
				long subscribers_count=2000;
				String free_trail_status="0";
				if(subscribers_count<=2000&&free_trail_status.equals("0")){
					System.out.println("Free Train is Active");
					out.println("Free Train is Active");
				}else if(free_trail_status.equals("1")){
					System.out.println("Free Train Date Expired");
					out.println("Free Train Date Expired");
				}else if(subscribers_count>2000){
					System.out.println("Subscriber Count is More");
					out.println("Subscriber Count is More");
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.println("Error : "+e.getMessage());
		}

	}

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Session session = null;
		try {
			if (request.getRequestPathInfo().getExtension().equals("asd_freetrail_addnode")) {

			
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
