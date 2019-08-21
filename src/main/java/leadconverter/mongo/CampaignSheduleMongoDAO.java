package leadconverter.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;

import leadconverter.doctiger.LogByFileWriter;

import leadconverter.freetrail.FreetrialShoppingCartUpdate;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.parser.JSONParser;

//import com.leadconverter.freetrail.CheckValidUserforFreetrialAndCart;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class CampaignSheduleMongoDAO {

	public static void main(String[] args) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// findCampaignToScheduleFromCampaignDetails();
		// findCampaignToScheduleFromCampaignListDetails();

		// findCampaignToScheduleFromCampaignListDetails();
		String category = "Explore";
		// String category="NoExplore";

		// findListIdForCampaignFromCampaignDetails("1118");
		String body = "Hi This is Test Massage";
		String subject = "Test 15-06-2019 13th Mail";
		String fromfield = "Akhilesh";
		String replyto = "Subscribers";
		String embargo = "2019-06-24 18:49:23";
		String footer = "Hi This is Footer";
		String bodyvalue = URLEncoder.encode(body);
		String campaignaddurl = ResourceBundle.getBundle("config").getString("Campaign_Add_Url_New");
		String campaigngetbyid = ResourceBundle.getBundle("config").getString("campaigngetbyid");
		String campaigngetbyid_url_parameter = "?id=" + "1472";
		String campaigngetbyid_url_response = sendpostdata(campaigngetbyid,
				campaigngetbyid_url_parameter);
		System.out.println("resp= "+campaigngetbyid_url_response);
//		JSONObject response_data_json_obj = (JSONObject) new JSONObject(
//				campaigngetbyid_url_response.replace("<pre>", "")).get("data");
//		String status = response_data_json_obj.getString("status");

		
		/*
		 * String campaignaddapiurlparameters = "subject=" + subject + "&fromfield=" +
		 * fromfield + "&replyto=" + replyto +
		 * "&message="+body+"&textmessage=hii&footer="+footer+
		 * "&status=draft&sendformat=html&template=&embargo=&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
		 * String campaignresponse = sendHttpPostData(campaignaddurl,
		 * campaignaddapiurlparameters.replace(" ", "%20").replace("\r",
		 * "").replace("\n", "")) .replace("<pre>", "");
		 */

		String campaignaddapiurlparameters = "subject=" + subject + "&fromfield=" + fromfield + "&replyto=" + replyto
				+ "&message=" + body + "&textmessage=hii&footer=" + footer
				+ "&status=draft&sendformat=html&template=&embargo=" + embargo
				+ "&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
//		String campaignresponse = sendHttpPostData(campaignaddurl,
//				campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", "")).replace("<pre>",
//						"");

	}

	public static void findCampaignToScheduleFromCampaignDetails() {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCursor<Document> campaignDetailsCursor = null;
		Document campaign_details_doc = null;
		Bson category_filter = null;
		JSONObject campaign_list_json_obj = new JSONObject();

		String Campaign_Id = null;
		String List_Id = null;
		String campaign_status = null;
		String Campaign_Date = null;
		String CreatedBy = null;
		String free_trail_status = null;
		int count = 0;
		CampaignSheduleMongoDAO csmdao = null;
		try {
			csmdao = new CampaignSheduleMongoDAO();
			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("phplisttest");
			collection = database.getCollection("campaign_details");
			category_filter = and(eq("SubFunnelName", "Explore"), eq("campaign_status", "draft"));// submitted draft
			// category_filter=eq("SubFunnelName", "Explore");
			campaignDetailsCursor = collection.find(category_filter).iterator();
			System.out.println("Campaign Found To Schedule : " + campaignDetailsCursor.hasNext());
			LogByFileWriter.logger_info(
					"CampaignSheduleMongoDAO: " + "Campaign Found To Schedule : " + campaignDetailsCursor.hasNext());
			if (campaignDetailsCursor.hasNext()) {
				while (campaignDetailsCursor.hasNext()) {
					campaign_details_doc = campaignDetailsCursor.next();
					Campaign_Id = campaign_details_doc.getString("Campaign_Id");
					List_Id = campaign_details_doc.getString("List_Id");
					campaign_status = campaign_details_doc.getString("campaign_status");
					Campaign_Date = campaign_details_doc.getString("Campaign_Date");
					CreatedBy = campaign_details_doc.getString("CreatedBy");
					CreatedBy = CreatedBy.replace("_", "@");
				//	free_trail_status = new FreetrialShoppingCartUpdate().checkFreeTrialExpirationStatus(CreatedBy);
					// System.out.println(campaign_details_doc);
//					if (free_trail_status.equals("0")) {
					  String validuserresp = new FreetrialShoppingCartUpdate().checkValiditytrialCart(CreatedBy);
//					    if(free_trail_status.equals("0")){
					   JSONObject validjs =new JSONObject(validuserresp);
						if(validjs.has("status")&& validjs.get("status").equals("true")) {
					
					    
						System.out.println("CampaignSheduleMongoDAO: " + "Freetrail Active for User : " + CreatedBy);
						updateCampaignStatusInCampaignDetails(collection, Campaign_Id, List_Id, "submitted");// submitted
																												// draft
						System.out.println(
								"Sr No. : " + count + " Campaign Id  " + Campaign_Id + "  List Id : " + List_Id);
						LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "Sr No. : " + count + " Campaign Id  "
								+ Campaign_Id + "  List Id : " + List_Id);
						csmdao.activateCampaign(Campaign_Id, List_Id, List_Id, Campaign_Date, campaign_status,
								"Explore");
						// updateCampaignStatusInCampaignDetails(collection,"1102","956","submitted");
					} else {
						System.out.println("Freetrail Expired for User : " + CreatedBy);
						LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + CreatedBy);
					}

					count++;
				}
			} else {
				// System.out.println("No Campaign Found To Schedule ");
				// LogByFileWriter.logger_info("CampaignSheduleMongoDAO: "+"No Campaign Found To
				// Schedule ");
			}
			System.out.println("Total number of Campaign Found To Schedule : " + count);
			LogByFileWriter.logger_info("CampaignSheduleMongoDAO: "
					+ "Total number of Campaign Found To Schedule In CampaignDetails Collection : " + count);
		} catch (Exception ex) {
			System.out.println("Error : " + ex.getMessage());
		}

	}

	public static void findCampaignToScheduleFromCampaignListDetails() {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCursor<Document> campaignListDetailsCursor = null;
		Document campaign_list_details_doc = null;
		Bson category_filter = null;
		JSONObject campaign_list_json_obj = new JSONObject();
		String Campaign_Id = null;
		String List_Id = null;
		String List_Id_Delete = null;
		String campaign_status = null;
		String Campaign_Date = null;

//		String CreatedBy = null;
		String free_trail_status = null;

		int count = 0;
		CampaignSheduleMongoDAO csmdao = null;
		try {
			csmdao = new CampaignSheduleMongoDAO();
			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("phplisttest");
			collection = database.getCollection("campaign_list_details");

			Document unwind = new Document("$unwind", "$ListCampaignArr");
			Document match = new Document("$match",
					new Document("ListCampaignArr.campaign_status", "draft").append("ListStatus", "active"));

			Document project = new Document("$project", new Document("_id", 0).append("ListCampaignArr", 1));
			List<Document> pipeline = Arrays.asList(unwind, match, project);
			AggregateIterable<Document> output = collection.aggregate(pipeline);
			campaignListDetailsCursor = output.iterator();

			System.out.println("Campaign Found To Schedule : " + campaignListDetailsCursor.hasNext());
			LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "Campaign Found To Schedule : "
					+ campaignListDetailsCursor.hasNext());
			if (campaignListDetailsCursor.hasNext()) {
				while (campaignListDetailsCursor.hasNext()) {
					campaign_list_details_doc = (Document) campaignListDetailsCursor.next().get("ListCampaignArr");
					Campaign_Id = campaign_list_details_doc.getString("Campaign_Id");
					List_Id = campaign_list_details_doc.getString("List_Id");
					campaign_status = campaign_list_details_doc.getString("campaign_status");
					Campaign_Date = campaign_list_details_doc.getString("Campaign_Date");
					System.out.println(campaign_list_details_doc);
					LogByFileWriter.logger_info(
							"CampaignSheduleMongoDAO: " + "Campaign_Id : " + Campaign_Id + " List_Id : " + List_Id
									+ " campaign_status : " + campaign_status + " Campaign_Date : " + Campaign_Date);
					// List_Id_Delete=findListIdForCampaignFromCampaignDetails(Campaign_Id);

//					CreatedBy = campaign_list_details_doc.getString("CreatedBy");
//					CreatedBy = CreatedBy.replace("_", "@");
				//	free_trail_status = new FreetrialShoppingCartUpdate().checkFreeTrialExpirationStatus(CreatedBy);
					// System.out.println(campaign_details_doc);
//					if (free_trail_status.equals("0")) {
					//  String validuserresp = new FreetrialShoppingCartUpdate().checkValiditytrialCart(CreatedBy);
//					    if(free_trail_status.equals("0")){
//					   JSONObject validjs =new JSONObject(validuserresp);
//						if(validjs.has("status")&& validjs.get("status").equals("true")) {
//						System.out.println("CampaignSheduleMongoDAO: " + "Freetrail Active for User : " + CreatedBy);
						csmdao.activateCampaign(Campaign_Id, List_Id, List_Id, Campaign_Date, campaign_status,
								"NoExplore");
						updateCampaignStatusInCampaignListDetails(collection, Campaign_Id, List_Id, "submitted");
						LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "No Campaign Found To Schedule ");
						// updateCampaignStatusInCampaignDetails(collection,"1102","956","submitted");
//					} else {
//						System.out.println("Freetrail Expired for User : " + CreatedBy);
//						LogByFileWriter
//								.logger_info("CampaignSheduleMongoDAO: " + "Freetrail Expired for User : " + CreatedBy);
//					}

					count++;
				}
			} else {
				// System.out.println("No Campaign Found To Schedule ");
				// LogByFileWriter.logger_info("CampaignSheduleMongoDAO: "+"No Campaign Found To
				// Schedule ");
			}

			System.out.println("No. Campaign Found To Schedule : " + count);
			LogByFileWriter.logger_info("CampaignSheduleMongoDAO: "
					+ "Total number Of Campaign Found To Schedule In CampaignListDetails Collection : " + count);
		} catch (Exception ex) {
			System.out.println("Error : " + ex.getMessage());
			LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "Error : " + ex.getMessage());
		}

	}

	public static void updateCampaignStatusInCampaignListDetails(MongoCollection<Document> collection,
			String campaign_id, String list_id, String campain_status) {
		// ObjectId _id = new ObjectId("4e71b07ff391f2b283be2f95");
		// ObjectId arrayId = new ObjectId("4e639a918dca838d4575979c");
		Document query = new Document();
		query.put("ListCampaignArr.Campaign_Id", campaign_id);
		query.put("ListCampaignArr.List_Id", list_id);

		Document data = new Document();
		data.put("ListCampaignArr.$.campaign_status", campain_status);

		Document command = new Document();
		command.put("$set", data);

		collection.updateOne(query, command);
	}

	public static void updateCampaignStatusInCampaignDetails(MongoCollection<Document> collection, String campaign_id,
			String list_id, String campain_status) {
		// ObjectId _id = new ObjectId("4e71b07ff391f2b283be2f95");
		// ObjectId arrayId = new ObjectId("4e639a918dca838d4575979c");
		Document query = new Document();
		query.put("Campaign_Id", campaign_id);
		query.put("List_Id", list_id);

		Document data = new Document();
		data.put("campaign_status", campain_status);

		Document command = new Document();
		command.put("$set", data);

		collection.updateOne(query, command);
	}

	public void activateCampaign(String Campaign_Id, String list_id, String list_id_delete, String Campaign_Date,
			String campaign_status, String category) {
		// Campaign_Date or embargo
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String campaign_start_days = ResourceBundle.getBundle("config").getString("campaign_start_days");
		today.setDate(today.getDate() + Integer.parseInt(campaign_start_days));
		Date date1;
		Date date2;
		String status_response = null;
		String campaign_list_add_response = null;
		String campaign_list_delete_response = null;
		String campaignlistresponse = null;
		LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "Inside Method activateCampaign()");

		LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "date1(Today): " + sdf1.format(today));
		LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "date2(Campaign_Date): " + Campaign_Date);
		try {
			date1 = sdf1.parse(sdf1.format(today));
			date2 = sdf1.parse(Campaign_Date);

			if (date1.compareTo(date2) == 0) {
				System.out.println("Date1 is equal to Date2");
				LogByFileWriter
						.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : " + "Date1 is equal to Date2");

				System.out.println("current_campaign_status : " + campaign_status);
				LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
						+ "current_campaign_status : " + campaign_status);
				if (!campaign_status.equals("sent")) {
					LogByFileWriter.logger_info("===========================Active==============================");
					LogByFileWriter
							.logger_info("Active List Id : " + list_id + "  Active Campaign Id : " + Campaign_Id);
					LogByFileWriter.logger_info("===========================Active==============================");

					if (!category.equals("Explore")) {
						// This method call will delete List From Campaign
						// campaign_list_delete_response=this.campaignListDelete(list_id_delete,Campaign_Id);
						// System.out.println("campaign_list_delete_response :
						// "+campaign_list_delete_response);

						// This method call will Add List to Campaign
						campaign_list_add_response = this.campaignListAdd(list_id, Campaign_Id);
						LogByFileWriter.logger_info("campaign_list_add_response : " + campaign_list_add_response);
					} else {

					}

					// This method call will Update Embargo(Campaign send date & time) of Campaign

					// Checking Campaign status for setting campaign status
					String campaigngetbyid_url = ResourceBundle.getBundle("config").getString("campaigngetbyid");
					String campaigngetbyid_url_parameter = "?id=" + Campaign_Id;
					String campaigngetbyid_url_response = sendpostdata(campaigngetbyid_url,
							campaigngetbyid_url_parameter);
					JSONObject response_data_json_obj = (JSONObject) new JSONObject(
							campaigngetbyid_url_response.replace("<pre>", "")).get("data");
					String status = response_data_json_obj.getString("status");

					if (status.equals("sent") || status.equals("submitted")) {
						System.out.println("No need to Anything : " + status);
						LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
								+ "No need to Anything : " + status);
						if (status.equals("sent")) {
							status_response = this.campaignStatusUpdate(Campaign_Id, "submitted");
							System.out.println("status_response reque: " + status_response);
							LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
									+ "status_response reque: " + status_response);
						}
					} else {

						System.out.println("Do your stuff here " + status);
						LogByFileWriter.logger_info(
								"CampaignSheduleMongoDAO: " + "CampaignActivator : " + "Do your stuff here " + status);
						String embargoupdateurl = ResourceBundle.getBundle("config").getString("embargoupdateurl");
						String embargoupdateurlparameters = "id=" + Campaign_Id + "&embargo=" + Campaign_Date;
						// out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
						campaignlistresponse = this
								.sendHttpPostData(embargoupdateurl, embargoupdateurlparameters.replace(" ", "%20"))
								.replace("<pre>", "");
						System.out.println("campaignresponse : " + campaignlistresponse);
						LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
								+ "campaignresponse : " + campaignlistresponse);
						// This method call will Activate The Campaign for sending
						if (listSubscribersCount(list_id, Campaign_Id) > 0) {
							status_response = this.campaignStatusUpdate(Campaign_Id, "submitted");
							System.out.println("status_response : " + status_response);
							LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
									+ "status_response : " + status_response);
						} else {
							System.out.println("No Subscribers Found For List Id : " + list_id);
							LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
									+ "status_response : " + "No Subscribers Found For List Id : " + list_id);
						}

					}

				} else {
					System.out.println("Current Campaign Status is Not equal to Sent");
					LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
							+ "Current Campaign Status is Not equal to Sent");
				}

			} else if (date1.compareTo(date2) > 0) {
				System.out.println("Date1 is after Date2");
				LogByFileWriter
						.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : " + "Date1 is after Date2");
			} else if (date1.compareTo(date2) < 0) {
				System.out.println("Date1 is before Date2");
				LogByFileWriter
						.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : " + "Date1 is before Date2");
			}
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			LogByFileWriter.logger_info("ErroR CampaignSheduleMongoDAO: " + ex.getMessage());
		}
	}

	// campaignStatusUpdate
	public String campaignStatusUpdate(String id, String status) throws ServletException, IOException {
		String campaignaddurl = ResourceBundle.getBundle("config").getString("campaignStatusUpdate");
		String campaignaddapiurlparameters = "?id=" + id + "&status=" + status;
		String campaignresponse = this
				.sendpostdata(campaignaddurl,
						campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""))
				.replace("<pre>", "");
		return campaignresponse;
	}

	// listCampaignAdd
	public String campaignListAdd(String listid, String campid) throws ServletException, IOException {
		String campaignaddurl = ResourceBundle.getBundle("config").getString("listCampaignAdd");
		String campaignaddapiurlparameters = "?listid=" + listid + "&campid=" + campid;
		String campaignresponse = this
				.sendpostdata(campaignaddurl,
						campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""))
				.replace("<pre>", "");
		return campaignresponse;
	}

	// listCampaignDelete
	public String campaignListDelete(String listid, String campid) throws ServletException, IOException {
		String campaignaddurl = ResourceBundle.getBundle("config").getString("listCampaignDelete");
		String campaignaddapiurlparameters = "?listid=" + listid + "&campid=" + campid;
		String campaignresponse = this
				.sendpostdata(campaignaddurl,
						campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""))
				.replace("<pre>", "");
		return campaignresponse;
	}

	// listSubscribersCount
	public int listSubscribersCount(String listid, String campid) throws ServletException, IOException {
		int sub_count = 0;
		try {
			String listSubscribersCountUrl = ResourceBundle.getBundle("config").getString("listSubscribersCount");
			String listSubscribersCountUrlparameters = listid;
			String SubscribersCountResponse = this
					.sendpostdata(listSubscribersCountUrl,
							listSubscribersCountUrlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""))
					.replace("<pre>", "");
			JSONObject sub_count_json = new JSONObject(SubscribersCountResponse);
			if (sub_count_json.getString("status").equals("success")) {
				sub_count = ((JSONObject) sub_count_json.get("data")).getInt("count");
				System.out.println("sub_count :" + sub_count);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sub_count;
	}

	public static String sendHttpPostData(String campaignaddurl, String campaignaddapiurlparameters)
			throws ServletException, IOException {

		URL url = new URL(campaignaddurl);
		byte[] postDataBytes = campaignaddapiurlparameters.toString().getBytes("UTF-8");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		StringBuffer buffer = new StringBuffer();
		for (int c; (c = in.read()) >= 0;)
			buffer.append((char) c);
		System.out.println("response : " + buffer.toString());
		return buffer.toString();
	}

	public static String sendpostdata(String callurl, String urlParameters) throws ServletException, IOException {
		URL url = new URL(callurl + urlParameters.replace("\\", ""));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		OutputStream writer = conn.getOutputStream();
		writer.write(urlParameters.getBytes());
		int responseCode = conn.getResponseCode();
		StringBuffer buffer = new StringBuffer();
		if (responseCode == 200) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
		} else {
			System.out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}

	public static String findListIdForCampaignFromCampaignDetails(String CampaignId) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCursor<Document> campaignDetailsCursor = null;
		Document campaign_details_doc = null;
		Bson category_filter = null;
		JSONObject campaign_list_json_obj = new JSONObject();

		String Campaign_Id = null;
		String List_Id = null;
		String campaign_status = null;
		String Campaign_Date = null;
		CampaignSheduleMongoDAO csmdao = null;
		try {
			csmdao = new CampaignSheduleMongoDAO();
			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("phplisttest");
			collection = database.getCollection("campaign_details");
			// category_filter=and(eq("SubFunnelName", "Explore"),eq("campaign_status",
			// "draft"));// submitted draft
			category_filter = eq("Campaign_Id", CampaignId);
			campaignDetailsCursor = collection.find(category_filter).iterator();
			System.out.println("Campaign Found To Schedule : " + campaignDetailsCursor.hasNext());
			if (campaignDetailsCursor.hasNext()) {
				while (campaignDetailsCursor.hasNext()) {
					campaign_details_doc = campaignDetailsCursor.next();
					Campaign_Id = campaign_details_doc.getString("Campaign_Id");
					List_Id = campaign_details_doc.getString("List_Id");
				}
			} else {
				System.out.println("No List Id Found For Campaign");
			}
			System.out.println("List_Id : " + List_Id);
		} catch (Exception ex) {
			System.out.println("Error : " + ex.getMessage());
		}
		return List_Id;

	}

	public void activateCampaignBackUp(String Campaign_Id, String list_id, String Campaign_Date, String campaign_status,
			String category) {
		// Campaign_Date or embargo
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String campaign_start_days = ResourceBundle.getBundle("config").getString("campaign_start_days");
		today.setDate(today.getDate() + Integer.parseInt(campaign_start_days));
		Date date1;
		Date date2;
		String status_response = null;
		String campaign_list_add_response = null;
		String campaign_list_delete_response = null;
		String campaignlistresponse = null;

		try {
			date1 = sdf1.parse(sdf1.format(today));
			date2 = sdf1.parse(Campaign_Date);
			if (date1.compareTo(date2) == 0) {
				System.out.println("Date1 is equal to Date2");
				LogByFileWriter
						.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : " + "Date1 is equal to Date2");

				System.out.println("current_campaign_status : " + campaign_status);
				LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
						+ "current_campaign_status : " + campaign_status);
				if (!campaign_status.equals("sent")) {
					System.out.println("===========================Active==============================");
					System.out.println("Active List Id : " + list_id + "  Active Campaign Id" + Campaign_Id);
					System.out.println("===========================Active==============================");

					// This method call will delete List From Campaign
					campaign_list_delete_response = this.campaignListDelete(list_id, Campaign_Id);
					System.out.println("campaign_list_delete_response : " + campaign_list_delete_response);

					// This method call will Add List to Campaign
					campaign_list_add_response = this.campaignListAdd(list_id, Campaign_Id);
					System.out.println("campaign_list_add_response : " + campaign_list_add_response);

					// This method call will Update Embargo(Campaign send date & time) of Campaign

					// Checking Campaign status for setting campaign status
					String campaigngetbyid_url = ResourceBundle.getBundle("config").getString("campaigngetbyid");
					String campaigngetbyid_url_parameter = "?id=" + Campaign_Id;
					String campaigngetbyid_url_response = sendpostdata(campaigngetbyid_url,
							campaigngetbyid_url_parameter);
					JSONObject response_data_json_obj = (JSONObject) new JSONObject(
							campaigngetbyid_url_response.replace("<pre>", "")).get("data");
					String status = response_data_json_obj.getString("status");

					if (status.equals("sent") || status.equals("submitted")) {
						System.out.println("No need to Anything : " + status);
						LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
								+ "No need to Anything : " + status);
						if (status.equals("sent")) {
							status_response = this.campaignStatusUpdate(Campaign_Id, "submitted");
							System.out.println("status_response reque: " + status_response);
							LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
									+ "status_response reque: " + status_response);
						}
					} else {

						System.out.println("Do your stuff here " + status);
						LogByFileWriter.logger_info(
								"CampaignSheduleMongoDAO: " + "CampaignActivator : " + "Do your stuff here " + status);
						String embargoupdateurl = ResourceBundle.getBundle("config").getString("embargoupdateurl");
						String embargoupdateurlparameters = "id=" + Campaign_Id + "&embargo=" + Campaign_Date;
						// out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
						campaignlistresponse = this
								.sendHttpPostData(embargoupdateurl, embargoupdateurlparameters.replace(" ", "%20"))
								.replace("<pre>", "");
						System.out.println("embargo updateur response : " + campaignlistresponse);
						LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
								+ "embargo updateur response : " + campaignlistresponse);
						// This method call will Activate The Campaign for sending
						if (listSubscribersCount(list_id, Campaign_Id) > 0) {
							status_response = this.campaignStatusUpdate(Campaign_Id, "submitted");
							System.out.println("status_response : " + status_response);
							LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
									+ "status_response : " + status_response);
						} else {
							System.out.println("No Subscribers Found For List Id : " + list_id);
							LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
									+ "status_response : " + "No Subscribers Found For List Id : " + list_id);
						}

					}

				} else {
					System.out.println("Current Campaign Status is Not equal to Sent");
					LogByFileWriter.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : "
							+ "Current Campaign Status is Not equal to Sent");
				}

			} else if (date1.compareTo(date2) > 0) {
				System.out.println("Date1 is after Date2");
				LogByFileWriter
						.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : " + "Date1 is after Date2");
			} else if (date1.compareTo(date2) < 0) {
				System.out.println("Date1 is before Date2");
				LogByFileWriter
						.logger_info("CampaignSheduleMongoDAO: " + "CampaignActivator : " + "Date1 is before Date2");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
