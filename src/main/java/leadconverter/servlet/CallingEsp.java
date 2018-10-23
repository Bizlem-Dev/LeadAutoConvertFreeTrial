package leadconverter.servlet;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jxl.*;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.jsoup.Jsoup;
import org.osgi.service.http.HttpService;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/CallingEsp" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class CallingEsp extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };
 
	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject=null;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

			 if (request.getRequestPathInfo().getExtension().equals("leadconverternew3")) {

				PrintWriter o = response.getWriter();
				try {
					request.getRequestDispatcher("/content/static/.LeadConverterNew3").forward(request, response);
					// o.print("working");
				} catch (Exception e) {
					o.print(e.getMessage());
				}
			}

			 else	 if (request.getRequestPathInfo().getExtension().equals("leadconverternew2")) {

				PrintWriter o = response.getWriter();
				try {
					request.getRequestDispatcher("/content/static/.LeadConverterNewUI").forward(request, response);
					// o.print("working");
				} catch (Exception e) {
					o.print(e.getMessage());
				}
			}
			 else	 if (request.getRequestPathInfo().getExtension().equals("Error")) {

					PrintWriter o = response.getWriter();
					try {
						request.getRequestDispatcher("/content/static/.Error").forward(request, response);
						// o.print("working");
					} catch (Exception e) {
						o.print(e.getMessage());
					}
		
				}
			 
			 else	 if (request.getRequestPathInfo().getExtension().equals("Admin")) {

					PrintWriter o = response.getWriter();
					try {
						request.getRequestDispatcher("/content/static/.admin").forward(request, response);
						// o.print("working");
					} catch (Exception e) {
						o.print(e.getMessage());
					}
		
				}
			 
			 else	 if (request.getRequestPathInfo().getExtension().equals("arabic")) {

					PrintWriter o = response.getWriter();
					try {
						request.getRequestDispatcher("/content/static/.arabic").forward(request, response);
						// o.print("working");
					} catch (Exception e) {
						o.print(e.getMessage());
					}
		
				}
			 
	}
}

	