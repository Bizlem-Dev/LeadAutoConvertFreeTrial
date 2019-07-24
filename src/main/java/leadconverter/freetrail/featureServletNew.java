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

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/featureServletNew" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class featureServletNew extends SlingAllMethodsServlet {
	@Reference
	private SlingRepository repos;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		Session session = null;
		Node mailtangynode = null;
		try {

			if (request.getRequestPathInfo().getExtension().equals("callservice")) {
				out.println("in callservice");
				String rmemail = request.getParameter("rm_email");
				FreeTrialandCart cart = new FreeTrialandCart();
				String freetrialstatus = cart.checkfreetrial(rmemail);
				mailtangynode = cart.getMailtangyNode(freetrialstatus, rmemail, "", session, response);

			} else {
				System.out.println("in else part");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

	}

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Session session = null;
		try {
			if (request.getRequestPathInfo().getExtension().equals("freetrail_addnode")) {

			
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
