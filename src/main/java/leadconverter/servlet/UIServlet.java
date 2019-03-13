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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/ui" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class UIServlet extends SlingAllMethodsServlet {

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
		/*
		if(request.getRequestPathInfo().getExtension().equals("fconfiguration")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fconfiguration").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("funnel")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.ffunnel").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("index")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.findex").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("indexData")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.index").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("list")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.flist").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("subscriberslist")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.subscriberslist").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("set-up-campaign")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fset-up-campaign").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("statistic")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fstatistic").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("statistic2")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fstatistic2").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("statistic3")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fstatistic3").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("statistic4")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fstatistic4").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else{
			 out.print("Rrquested extension is not an ESP resource");
		 }
		*/
		
		if(request.getRequestPathInfo().getExtension().equals("verify")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fverify").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("texts-only-template")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.ftexts-only-template").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("text-photos-template")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.ftext-photos-template").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("strat-from-scratch")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fstrat-from-scratch").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 } else if(request.getRequestPathInfo().getExtension().equals("smtp-setup")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fsmtp-setup").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("simpleLayout")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fsimpleLayout").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("set-up-campaign")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fset-up-campaign").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("set-up-campaign-complete")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fset-up-campaign-complete").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("select-tamplate")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fselect-tamplate").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("select-tamplate-file")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fselect-tamplate-file").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("news-letter-tamplate")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fnews-letter-tamplate").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("list")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.flist").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("index")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.findex").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("gallery-template")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fgallery-template").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("funnel")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.ffunnel").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("configuration")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fconfiguration").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else{
			 out.print("Rrquested extension is not an ESP resource");
		 }
		
		

		
		
		/*
		
		if(request.getRequestPathInfo().getExtension().equals("configuration")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.configuration").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("funnel")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.funnel").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("index")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.index").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("indexData")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.index").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("list")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.list").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("subscriberslist")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.subscriberslist").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("set-up-campaign")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.fset-up-campaign").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("statistic")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.statistic").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("statistic2")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.statistic2").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("statistic3")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.statistic3").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("statistic4")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.statistic4").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else{
			 out.print("Rrquested extension is not an ESP resource");
		 }
		 */

			 
	}
}

	
