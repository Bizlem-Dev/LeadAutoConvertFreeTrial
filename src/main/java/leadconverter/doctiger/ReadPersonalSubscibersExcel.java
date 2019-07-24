package leadconverter.doctiger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;





import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.sling.commons.json.JSONArray;

import com.sun.jersey.core.util.Base64;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/uploadPersonalSubscibersExcel" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class ReadPersonalSubscibersExcel extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		JSONObject js = new JSONObject();
		String moduletype = "";
		String email = "";
		//Node userNode = null;
		//Node emailNode = null;
		Node dtaNode = null;
		
		try {
			//Session session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();

			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
			String res = buf.toString("UTF-8");

			JSONObject resultjsonobject = new JSONObject(res);

			email = resultjsonobject.getString("Email");
			
			String filename = resultjsonobject.getString("filename");
			int e = filename.indexOf(".");
			String extn = filename.substring(e + 1);
			if(extn.equals("xls")) {
				moduletype = resultjsonobject.getString("Moduletype");
				
				String data = resultjsonobject.getString("filedata");
				
				byte[] bytes = Base64.decode(data);
				
				Node jcrNode = null;
				Node filefileNode = null;
				InputStream myInputStream = new ByteArrayInputStream(bytes);
				//JSONArray subsciber_json_array=ReadExcel(myInputStream,response);
				JSONObject final_subsciber_json_obj=ReadExcel(myInputStream,response);
				uploadPersonalSubscribersListInPhpList(final_subsciber_json_obj,response);
				//String response103=getjsonReadExcel(myInputStream,"1");
				//out.println("response103 : "+response103);
				//LogByFileWriter.logger_info("ReadPersonalSubscibersExcel : " + "response103 : "+response103);
	
			}else {
				js.put("status", "error");
				js.put("message", "Please upload xls file");
				out.println(js);
				LogByFileWriter.logger_info("ReadPersonalSubscibersExcel : JSON " + js);
			}
			//session.save();
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			try {
				js.put("status", "error");
				js.put("message", e.getMessage());
				out.println(js);
				LogByFileWriter.logger_info("ReadPersonalSubscibersExcel : JSON " + js);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}	
	}
	
	public JSONObject ReadExcel(InputStream ins,SlingHttpServletResponse response) {
		
		JSONObject json = new JSONObject();

		JSONArray array = new JSONArray();
		String js = "";
		
		JSONArray subsciber_header_arr = new JSONArray();
		JSONObject subsciber_json_obj = null;
		JSONArray subsciber_json_array = new JSONArray();
		
		JSONObject final_subsciber_json_obj = new JSONObject();
		try {
			PrintWriter out = response.getWriter();
			// Create Workbook instance holding reference to .xlsx file
			HSSFWorkbook workbook = new HSSFWorkbook(ins);

			// Get first/desired sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);
			//out.println(sheet.getSheetName());
			LogByFileWriter.logger_info("ReadPersonalSubscibersExcel : Personal Excel Sheet Name " + sheet.getSheetName());
			int rowcount;
			// Iterate through each rows one by one
			//out.println("last " + sheet.getLastRowNum());
			
			for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){
				Row row = sheet.getRow(rowIndex);
				subsciber_json_obj=new JSONObject();
				if(rowIndex==0){
					for(int collIndex=0;collIndex<=row.getLastCellNum();collIndex++){
						Cell cell = row.getCell(collIndex);
						try {
							switch (cell.getCellType()) {
							
							case Cell.CELL_TYPE_STRING:
								subsciber_header_arr.put(cell.getStringCellValue());
								break;
							case Cell.CELL_TYPE_NUMERIC:
								subsciber_header_arr.put(Double.toString(cell.getNumericCellValue()));
								break;
							}
							}catch (Exception e) {
								// TODO: handle exception
							}
					}
				}else{
					for(int collIndex=0;collIndex<=row.getLastCellNum();collIndex++){
						Cell cell = row.getCell(collIndex);
						try {
							switch (cell.getCellType()) {
							
							case Cell.CELL_TYPE_STRING:
								subsciber_json_obj.put(subsciber_header_arr.getString(collIndex), cell.getStringCellValue());
		                        break;
							case Cell.CELL_TYPE_NUMERIC:
								subsciber_json_obj.put(subsciber_header_arr.getString(collIndex), cell.getNumericCellValue());
								break;
							}
							}catch (Exception e) {
								// TODO: handle exception
							}
					}
					subsciber_json_array.put(subsciber_json_obj);
				}
				
			}
			ins.close();
			final_subsciber_json_obj.put("header", subsciber_header_arr);
			final_subsciber_json_obj.put("subsciber_json_array", subsciber_json_array);
		} catch (Exception e) {
			//js=e.getMessage();
		}finally{
		  
		}
		return final_subsciber_json_obj;
	}

	
		
	public String uploadPersonalSubscribersListInPhpList(JSONObject final_subsciber_json_obj,SlingHttpServletResponse response) {
	   try {
		   JSONArray personal_subscribers_json_arr=final_subsciber_json_obj.getJSONArray("subsciber_json_array");
		   JSONArray subsciber_header_arr=final_subsciber_json_obj.getJSONArray("header");
		   PrintWriter out = response.getWriter();
		   JSONObject subscriber_json_obj =null;
	       JSONObject subscriber_tmp_json_obj =null;
	       JSONArray subscribers_json_arr= new JSONArray();
	       JSONArray mongo_subscribers_json_arr= new JSONArray();
           for (int i=0; i < personal_subscribers_json_arr.length(); i++) {
           	        subscriber_tmp_json_obj =(JSONObject) personal_subscribers_json_arr.get(i);
	    			subscriber_json_obj = new JSONObject();
	    			subscriber_json_obj.put("Name",
	    					subscriber_tmp_json_obj.getString(subsciber_header_arr.getString(1)).replace(" ", ""));
	    			subscriber_json_obj.put("email",
							subscriber_tmp_json_obj.getString(subsciber_header_arr.getString(2)).replace(" ", ""));
	    			subscriber_json_obj.put("confirmed", 1);
	    			subscriber_json_obj.put("htmlemail", 1);
	    			subscriber_json_obj.put("password", 0);
	    			subscriber_json_obj.put("disabled", 0);
	    			subscriber_json_obj.put("foreignkey", "");
	    			subscriber_json_obj.put("subscribepage", 0);
					subscribers_json_arr.put(subscriber_json_obj);
	    	}
           //out.println("subscribers_json_arr : "+subscribers_json_arr.toString());
		   String urlParameters = "?subscribers="
					+ subscribers_json_arr.toString();
		   String phpurl = ResourceBundle.getBundle("config").getString("Phplist_Url");
           String postresponse = this.sendpostdata(phpurl,
					urlParameters.replace(" ", "%20"), response)
					.replace("<pre>", "");
			JSONObject bufferjson = new JSONObject(postresponse);
			JSONArray subscriberdata = bufferjson
					.getJSONArray("data");
			//out.println("subscriberdata : "+subscriberdata.toString());
			for (int subscriberidloop = 0; subscriberidloop < subscriberdata
					.length(); subscriberidloop++) {
				JSONObject data = subscriberdata
						.getJSONObject(subscriberidloop);
				String subscriberid = data.getString("id");
				//out.println("subscriberid : "+subscriberid.toString());
				subscriber_tmp_json_obj =(JSONObject) personal_subscribers_json_arr.get(subscriberidloop);
				subscriber_tmp_json_obj.put("subscriberid", subscriberid);
				out.println("subscriber_tmp_json_obj : "+subscriber_tmp_json_obj.toString());
				LogByFileWriter.logger_info("ReadPersonalSubscibersExcel : Peronal Subscriber Details : " + subscriber_tmp_json_obj.toString());
				PersonalSubscriberMongoDAO.savePersonalSuscriber(subscriber_tmp_json_obj,subsciber_header_arr,subscriber_tmp_json_obj.getString("Email"));
			}
		
			

		}catch(Exception e) {
			e.getMessage();
		}
		
		
		
		return "";
		
	}
	
	public String sendpostdata(String callurl, String urlParameters,
			SlingHttpServletResponse response) throws ServletException,
			IOException {

		PrintWriter out = response.getWriter();
		//out.println("urlParameters :" + urlParameters);
		URL url = new URL(callurl + urlParameters.replace("\\", ""));
		//out.println("Url :" + url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		//
		OutputStream writer = conn.getOutputStream();

		writer.write(urlParameters.getBytes());
		// out.println("Writer Url : "+writer);
		int responseCode = conn.getResponseCode();
		//out.println("POST Response Code :: " + responseCode);
		StringBuffer buffer = new StringBuffer();
		//
		if (responseCode == 200) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			//
			// out.println(buffer.toString());
		} else {
			out.println("POST request not worked");
			LogByFileWriter.logger_info("ReadPersonalSubscibersExcel : " + "POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}
}
