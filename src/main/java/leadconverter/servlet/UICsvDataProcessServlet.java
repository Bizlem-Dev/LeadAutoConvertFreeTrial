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
import leadconverter.impl.Searching_list_DaoImpl;
import leadconverter.mailcheker.spamassertion.MailChecker;

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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/uicsvdata" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class UICsvDataProcessServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = new JSONObject();	

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONArray mainarray = new JSONArray();
		JSONObject jsonobject = null;
		String listid = null;

		String remoteuser = request.getRemoteUser();
		System.out.println("uicsvdata post called");
		JSONObject testjsonobject = new JSONObject();
		try {
			Session session = null;

			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			Node content = session.getRootNode().getNode("content");
			System.out.println("uicsvdata called");
			if (request.getParameterMap().get("0") != null) {
				RequestParameter[] ap = request.getRequestParameterMap().get("0");
				testjsonobject.put("Step", "Step 1");
				for (int i = 0; i < ap.length; i++) {
					if (ap[i].getFileName() != null && ap[i].getFileName().trim() != "") {

						String filename = ap[i].getFileName();
						System.out.println("filename : "+filename);
						fileType = "";
						testjsonobject.put("Step", "Step 2");
						if (ap[i] != null &&  ap[i].getSize() != 0) {
							for (int j = 0; j < FILEEXTENSION.length; j++) {
								if (filename.indexOf(FILEEXTENSION[j]) != -1) {
									fileType = FILEEXTENSION[j];
								}
								//out.println("4 " + fileType);
							}
							testjsonobject.put("Step", "Step 3");
							InputStream stream = ap[i].getInputStream();
							
                            try {
								BufferedReader reader = null;
								String line = "";
								String cvsSplitBy = ",";
								String email_id = null;
								String email_id_status = null;
								reader = new BufferedReader(new InputStreamReader(stream));
								
									System.out.println("list Success");
									while ((line = reader.readLine()) != null) {
										String[] data = line.split(cvsSplitBy);
										jsonobject = new JSONObject();
										//email_id=data[0].toString().replace("\"", "");
										System.out.println("Email id : " + data[0]);
										System.out.println("Name : "+data[1]);
										jsonobject.put("EmailAddress", data[0].toString().replace("\"", ""));
										jsonobject.put("FirstName", data[1].toString().replace("\"", ""));
										jsonobject.put("LastName", data[2].toString().replace("\"", ""));
										jsonobject.put("PhoneNumber", data[3].toString().replace("\"", ""));
										jsonobject.put("Address", data[4].toString().replace("\"", ""));
										jsonobject.put("CompanyName", data[5].toString().replace("\"", ""));
										jsonobject.put("CompanyHeadCount", data[6].toString().replace("\"", ""));
										jsonobject.put("Industry", data[7].toString().replace("\"", ""));
										jsonobject.put("Institute", data[8].toString().replace("\"", ""));
										jsonobject.put("Source", data[9].toString().replace("\"", ""));
										//jsonobject.put("Email_Status", MailChecker.emailValidation(email_id));
										
										//out.println("EmailAddress : "+data[0].toString());
										
										/*
										jsonobject.put("EmailAddress", data[0].toString().replace(" ", ""));
										jsonobject.put("FirstName", data[1].toString().replace(" ", ""));
										jsonobject.put("LastName", data[2].toString().replace(" ", ""));
										jsonobject.put("PhoneNumber", data[3].toString().replace(" ", ""));
										jsonobject.put("Address", data[4].toString().replace(" ", ""));
										
										jsonobject.put("confirmed", 1);
										jsonobject.put("htmlemail", 1);
										jsonobject.put("password", 0);
										jsonobject.put("disabled", 0);
										jsonobject.put("foreignkey", "");
										jsonobject.put("subscribepage", 0);
										*/
										System.out.println("jsonobject : "+jsonobject);
										mainarray.put(jsonobject);
									}
									
                                }
								catch(Exception ex) {
									  out.println("Exception ex : "+ex.getMessage());
									//testjsonobject.put("Step", "Step 4");
									//out.println(testjsonobject);
								}
                            out.println(mainarray);
						
						}
					}
				}

			}
			
		} catch (Exception e) {

			  out.println("Exception ex : : : " + e.getMessage());
			//out.println(testjsonobject);
		}

	}
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		System.out.println("servlet/service/uicsvdata Get Mrthod Called");
		
	}
}

