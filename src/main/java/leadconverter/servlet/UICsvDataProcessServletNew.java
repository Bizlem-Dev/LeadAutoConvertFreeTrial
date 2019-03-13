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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/uicsvdatanew" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class UICsvDataProcessServletNew extends SlingAllMethodsServlet {

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
		JSONObject jsonobject = new JSONObject();
		String listid = null;

		String remoteuser = request.getRemoteUser();

		try {
			Session session = null;

			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			Node content = session.getRootNode().getNode("content");
			//out.println("uicsvdata called");
			if (request.getParameterMap().get("0") != null) {
				RequestParameter[] ap = request.getRequestParameterMap().get("0");
				
				for (int i = 0; i < ap.length; i++) {
					if (ap[i].getFileName() != null && ap[i].getFileName().trim() != "") {

						String filename = ap[i].getFileName();

						fileType = "";
						if (ap[i] != null &&  ap[i].getSize() != 0) {
							for (int j = 0; j < FILEEXTENSION.length; j++) {
								if (filename.indexOf(FILEEXTENSION[j]) != -1) {
									fileType = FILEEXTENSION[j];
								}
								//out.println("4 " + fileType);
							}

							InputStream stream = ap[i].getInputStream();
							
                            try {
								BufferedReader reader = null;
								String line = "";
								String cvsSplitBy = ",";
								reader = new BufferedReader(new InputStreamReader(stream));
								
									//out.println("list Success");
									while ((line = reader.readLine()) != null) {
										String[] data = line.split(cvsSplitBy);
										jsonobject = new JSONObject();
										//out.println("Email id : " + data[0]);
										//out.println("Name : "+data[1]);
										jsonobject.put("EmailAddress", data[0].toString());
										jsonobject.put("FirstName", data[1].toString());
										jsonobject.put("LastName", data[2].toString());
										jsonobject.put("PhoneNumber", data[3].toString());
										jsonobject.put("Address", data[4].toString());
										jsonobject.put("CompanyName", data[5].toString());
										jsonobject.put("CompanyHeadCount", data[6].toString());
										jsonobject.put("Industry", data[7].toString());
										jsonobject.put("Institute", data[8].toString());
										jsonobject.put("Source", data[9].toString());
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
										mainarray.put(jsonobject);
									}
									
                                }
								catch(Exception ex) {
									out.println("Exception ex : "+ex.getMessage());
								}
                            out.println(mainarray);
						
						}
					}
				}

			}
			
		} catch (Exception e) {

			out.println("Exception ex : : : " + e.getMessage());
		}

	}
}

