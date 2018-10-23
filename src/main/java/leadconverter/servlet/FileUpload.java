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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/FileUpload" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class FileUpload extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject=null;

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		try {
			Session session = null;

			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));


			if (request.getRequestPathInfo().getExtension().equals("readingexcel")) {

				if (request.getParameterMap().get("file") != null) {
					RequestParameter[] ap = request.getRequestParameterMap().get("file");
					out.println("ap.length  " + ap.length);

				

					for (int i = 0; i < ap.length; i++) {
						if (ap[i].getFileName() != null && ap[i].getFileName().trim() != "") {

							String filename = ap[i].getFileName();

							fileType = "";

							if (ap[i] != null && ap[i].getSize() != 0) {
								for (int j = 0; j < FILEEXTENSION.length; j++) {
									if (filename.indexOf(FILEEXTENSION[j]) != -1) {
										fileType = FILEEXTENSION[j];
									}
									out.println("4 " + fileType);
								}

								InputStream stream = ap[i].getInputStream();

								BufferedReader reader = null;
								String line = "";
								String cvsSplitBy = ",";

								reader = new BufferedReader(new InputStreamReader(stream));
								while ((line = reader.readLine()) != null) {
									String[] data = line.split(cvsSplitBy);

									JSONArray mainarray = new JSONArray();
									JSONObject jsonobject = new JSONObject();
									mainjsonobject = new JSONObject();
									out.println("Email id : " + data[0]);
									jsonobject.put("Name", data[1].toString());
									jsonobject.put("Email Id", data[0].toString());
									mainarray.put(jsonobject.toString());
									mainjsonobject.put("Data", mainarray);
									out.println(mainjsonobject);
								}
								
							String	 urlParameters=mainjsonobject.toString();
							String  urlrequest=null;
							//this.sendpostdata(urlrequest, urlParameters, response);
							urlrequest="http://35.221.160.146:8082/portal//servlet/service/FileUpload.readingexcel";
							this.sendpostdata(urlrequest, urlParameters, response);
							}
													}

					}
				}
			}
		} catch (Exception e) {

			out.println("Exception ex : : : " + e.getMessage());
		}
	}
	
	public void sendpostdata(String urlrequest,String urlParameters,SlingHttpServletResponse response)	throws ServletException, IOException {
		
		PrintWriter out=response.getWriter();
		
		out.println("My Method ");
		out.println("My Method : : : "+urlParameters);
	//String urlParameters = mainjsonobject.toString();
	//String urlrequest = "http://35.221.160.146:8082/portal//servlet/service/FileUpload.readingexcel";
	URL url = new URL(urlrequest);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json");
	conn.setRequestProperty("Accept", "application/json");
	conn.setUseCaches(false);

	OutputStream writer = conn.getOutputStream();

	writer.write(urlParameters.getBytes());
	out.println("Writer Url : "+writer);
	int responseCode = conn.getResponseCode();
  	out.println("POST Response Code :: " + responseCode);

	if (responseCode == HttpURLConnection.HTTP_OK) { //success
		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String inputLine;
		StringBuffer buffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			buffer.append(inputLine);
		}
		in.close();

		out.println(buffer.toString());
	} else {
		out.println("POST request not worked");
	}
	writer.flush();
	writer.close();
	}


	
}
