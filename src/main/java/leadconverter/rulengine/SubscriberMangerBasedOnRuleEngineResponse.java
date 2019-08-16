package leadconverter.rulengine;

import java.io.*;
import java.util.*;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
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
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import leadconverter.doctiger.LogByFileWriter;
import leadconverter.freetrail.FreetrialShoppingCartUpdate;
import leadconverter.freetrail.GetValidityInfoFromEmail;
import leadconverter.mongo.ConnectionHelper;
import leadconverter.mongo.ListMongoDAO;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/subscribermanger" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class SubscriberMangerBasedOnRuleEngineResponse extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = null;

// http://development.bizlem.io:8082/portal/servlet/service/subscribermanger.activate_draft_list
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		try {
			Session session = null;
			if (request.getRequestPathInfo().getExtension().equals("activate_draft_list")) {
//				
				try {
					session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
					String activeListName = null;
					String free_trail_status = null;
					NodeIterator funnelItr =null;
//				String CreatedBy;
					
					NodeIterator servItr = session.getRootNode().getNode("content").getNode("services").getNodes();
					while (servItr.hasNext()) {
						Node userNode = servItr.nextNode();
						
						
//						NodeIterator userItr = userNode.getNodes();
						
						if(userNode.getName().equals("freetrial")) {
//							Node freetrial=userNode.getNode("freetrial");
							
							if(userNode.hasNode("users")) {
								Node usernode1=userNode.getNode("users");
							
								NodeIterator groupItr = usernode1.getNodes();
								while (groupItr.hasNext()) {
									Node groupNode = groupItr.nextNode();
									out.println("groupNode= " +groupNode);
									if (groupNode.hasNode("LeadAutoConverter")) {
										 funnelItr = groupNode.getNode("LeadAutoConverter").getNode("Email")
												.getNode("Funnel").getNodes();
										while (funnelItr.hasNext()) {
											Node funnelNode = funnelItr.nextNode();
											String funnelName = funnelNode.getName();// currentcampaign
											out.println("funnelName = " + funnelName);
											String CreatedBy = "";
											if (funnelNode.hasProperty("CreatedBy")) {
												CreatedBy = funnelNode.getProperty("CreatedBy").getString();
											}
											out.println("CreatedBy = " + CreatedBy);
											out.println("funnelNode = " + funnelNode);
										
											if (funnelNode.hasNodes()) {
												NodeIterator campaignNodeItr = funnelNode.getNodes();
												while (campaignNodeItr.hasNext()) {
													Node subFunnelNode = campaignNodeItr.nextNode();
													String subFunnelName = subFunnelNode.getName();
													out.println("subFunnelName = " + subFunnelName);
													out.println("subFunnelNode = " + subFunnelNode);
													if (subFunnelNode.hasNode("List")) {
														free_trail_status = new FreetrialShoppingCartUpdate()
																.checkFreeTrialExpirationStatus(
																		CreatedBy.replace("_", "@"));
														if (free_trail_status.equals("0")) {
															out.println("free_trail_statuscheck====== = " + free_trail_status);

															Node listNode = subFunnelNode.getNode("List");
														
															
															Node activeListNode = listNode.getNode("ActiveList");
//															out.println("activeListNode = " + activeListNode);
									//draftListId,response,subFunnelNode,activeListNode,draftListNode,draftListChildNode,						
															if (listNode.hasNode("DraftList")) {
																Node draftListNode = listNode.getNode("DraftList");
																NodeIterator draftListItr = draftListNode.getNodes();
																while (draftListItr.hasNext()) {
																	Node draftListChildNode = draftListItr.nextNode();
																	String draftListId = draftListChildNode.getName();
																	String activeListStartDate = draftListChildNode
																			.getProperty("StartDate").getString();
																	/*
																	 * CreatedBy funnelNode funnelName subFunnelNode
																	 * subFunnelName DraftListNode DraftListChildNode
																	 * DraftListId ActiveListStartDate
																	 */
																	out.println("subFunnelNode = " + subFunnelNode);
																	out.println("draftListChildNode = "
																			+ draftListChildNode);
																	if (!subFunnelName.equals("Explore")) {
																		activeListName = CreatedBy + "_" + funnelName
																				+ "_" + subFunnelName;
																		// out.println("funnelName : "+funnelName);
																		// out.println("-----subFunnelName :
																		// "+subFunnelName);
																		// out.println("-----DraftListName :
																		// "+draftListId);
																		// out.println("-----ActiveListStartDate :
																		// "+activeListStartDate);
																		// out.println("Going... to call method
																		// checkStatusOfDraftListAndActivateIT()");
															checkStatusOfDraftListAndActivateIT(CreatedBy,funnelName,subFunnelName,draftListId,response,subFunnelNode,activeListNode,draftListNode,draftListChildNode,activeListStartDate,activeListName);
																		// out.println("Method checkStatusOfDraftList()
																		// have Called");
																		// out.println("-----------------------------------------------");
																	}
																}
															} else {
																out.println("Does Not Found Any List In Draft!");
																LogByFileWriter.logger_info(
																		"SubscriberMangerBasedOnRuleEngineResponse: "
																				+ "Does Not Found Any List In Draft!");
															}

														} else {
															System.out.println(
																	"Freetrail Expired for User : " + CreatedBy);
															LogByFileWriter.logger_info(
																	"SubscriberMangerBasedOnRuleEngineResponse: "
																			+ "Freetrail Expired for User : "
																			+ CreatedBy);
														}

													}
												}
											}

										}
									}
								
							}
								
							}
								
							
					}
//						while (userItr.hasNext()) {
							
							if (userNode.hasProperty("producttype")
									&& userNode.getProperty("producttype").getString().equals("leadautoconverter")) {
							
									NodeIterator groupItr = userNode.getNodes();
								while (groupItr.hasNext()) {
									Node groupNode = groupItr.nextNode();
									out.println("groupNode = " + groupNode);
									String group=null;
									group=groupNode.getName();
									if (groupNode.hasNode("LeadAutoConverter")) {
										 funnelItr = groupNode.getNode("LeadAutoConverter").getNode("Email")
												.getNode("Funnel").getNodes();
										while (funnelItr.hasNext()) {
											Node funnelNode = funnelItr.nextNode();
											String funnelName = funnelNode.getName();// currentcampaign
											out.println("funnelName = " + funnelName);
											String CreatedBy = "";
											if (funnelNode.hasProperty("CreatedBy")) {
												CreatedBy = funnelNode.getProperty("CreatedBy").getString();
											}
											out.println("CreatedBy = " + CreatedBy);
											out.println("funnelNode = " + funnelNode);
										
											if (funnelNode.hasNodes()) {
												NodeIterator campaignNodeItr = funnelNode.getNodes();
												while (campaignNodeItr.hasNext()) {
													Node subFunnelNode = campaignNodeItr.nextNode();
													String subFunnelName = subFunnelNode.getName();
//													out.println("subFunnelName = " + subFunnelName);
//													out.println("subFunnelNode = " + subFunnelNode);
													if (subFunnelNode.hasNode("List")) {
														free_trail_status = new FreetrialShoppingCartUpdate()
																.checkFreeTrialExpirationStatus(
																		CreatedBy.replace("_", "@"));
														Node shoppingnode=null;
														shoppingnode = new FreetrialShoppingCartUpdate().getLeadAutoConverterNode(free_trail_status, CreatedBy.replace("_", "@"), group,
																session, response);
														out.println("shoppingnode ========== "+shoppingnode);
														if (shoppingnode!=null) {
															out.println("free_trail_statuscheck = " + free_trail_status);

															Node listNode = subFunnelNode.getNode("List");
															out.println("listNode = " + listNode);
															
															Node activeListNode = listNode.getNode("ActiveList");
															out.println("activeListNode = " + activeListNode);
									//draftListId,response,subFunnelNode,activeListNode,draftListNode,draftListChildNode,						
															if (listNode.hasNode("DraftList")) {
																Node draftListNode = listNode.getNode("DraftList");
																NodeIterator draftListItr = draftListNode.getNodes();
																while (draftListItr.hasNext()) {
																	Node draftListChildNode = draftListItr.nextNode();
																	String draftListId = draftListChildNode.getName();
																	String activeListStartDate = draftListChildNode
																			.getProperty("StartDate").getString();
																	/*
																	 * CreatedBy funnelNode funnelName subFunnelNode
																	 * subFunnelName DraftListNode DraftListChildNode
																	 * DraftListId ActiveListStartDate
																	 */
																	out.println("subFunnelNode = " + subFunnelNode);
																	out.println("draftListChildNode = "
																			+ draftListChildNode);
																	if (!subFunnelName.equals("Explore")) {
																		activeListName = CreatedBy + "_" + funnelName
																				+ "_" + subFunnelName;
																		// out.println("funnelName : "+funnelName);
																		// out.println("-----subFunnelName :
																		// "+subFunnelName);
																		// out.println("-----DraftListName :
																		// "+draftListId);
																		// out.println("-----ActiveListStartDate :
																		// "+activeListStartDate);
																		// out.println("Going... to call method
																		// checkStatusOfDraftListAndActivateIT()");
																	checkStatusOfDraftListAndActivateIT(CreatedBy,funnelName,subFunnelName,draftListId,response,subFunnelNode,activeListNode,draftListNode,draftListChildNode,activeListStartDate,activeListName);
																		// out.println("Method checkStatusOfDraftList()
																		// have Called");
																		// out.println("-----------------------------------------------");
																	}
																}
															} else {
																out.println("Does Not Found Any List In Draft!");
																LogByFileWriter.logger_info(
																		"SubscriberMangerBasedOnRuleEngineResponse: "
																				+ "Does Not Found Any List In Draft!");
															}

														} else {
															
															System.out.println(
																	"Freetrail Expired for User : " + CreatedBy);
															LogByFileWriter.logger_info(
																	"SubscriberMangerBasedOnRuleEngineResponse: "
																			+ "Freetrail Or Shopping cart Expired for User : "
																			+ CreatedBy);
														}

													}
												}
											}

										}
									}

								}

							} else {
							}
//						}
//						String CreatedBy = userNode.getName();
//
//						if (!userNode.getName().equals("<%=request.getRemoteUser()%>")) {
//
//						}
					
					}
					session.save();
				} catch (Exception ex) {
					out.println("Exception ex :=" + ex.getMessage() + ex.getCause());
				}

			} else if (request.getRequestPathInfo().getExtension().equals("updateCampaignListAtivateDate")) {
				out.println("Inside updateCampaignListAtivateDate");
				String listid_tm = request.getParameter("listid");
				String date_str = request.getParameter("date_str");
				ListMongoDAO.updateCampaignListAtivateDate(listid_tm, date_str);// "2019-06-10 16:48:57"
			} else if (request.getRequestPathInfo().getExtension().equals("removeSubFromList")) {
				out.println("Inside removeSubFromList");
				String listid_tm = request.getParameter("listid");
				String subid = request.getParameter("subid");
				ListMongoDAO.removeSubscribersFromCampaignList(listid_tm, subid);// "2019-06-10 16:48:57"
			}

		} catch (Exception e) {

			out.println("Exception ex : : : " + e.getStackTrace());
		}
	}

	// This method is used to check the status of draft list and move it to Active
	// List Or re-schedule it
	public static void checkStatusOfDraftListAndActivateIT(String CreatedBy, String funnelName, String subFunnelName,
			String draftListId, SlingHttpServletResponse response, Node subFunnelNode, Node activeListNode,
			Node draftListNode, Node draftListChildNode, String activeListStartDate, String activeListName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat date_formatter_with_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String listActivateDays = ResourceBundle.getBundle("config").getString("list_activate_days");
		String campaignSctivateDays = ResourceBundle.getBundle("config").getString("campaign_activate_days");
		Date date1 = new Date();
		Date date2 = null;
		Date updateListActivateDate = null;
		Node activeListChildNode = null;
		String counter = null;
		int counterNumer = 0;
		try {
			PrintWriter out = response.getWriter();
			// out.println("Inside method checkStatusOfDraftList() ");
			JSONObject campaign_list_json_obj = ListMongoDAO.getCampaignList(CreatedBy, funnelName, subFunnelName,
					draftListId);
			out.println("campaign_list_json_obj : " + campaign_list_json_obj);
			if (campaign_list_json_obj.length() > 0) {
				int ListSubscriberCount = campaign_list_json_obj.getInt("ListSubscriberCount");
				// activeListStartDate=campaign_list_json_obj.getString("ListActivateDateStr");
				date1 = sdf.parse(sdf.format(date1)); // Current Date
				date2 = sdf.parse(activeListStartDate); // Draft List Activate Date
				out.println("Current Date : " + sdf.format(date1));
				out.println("Draft List Activate Date : " + sdf.format(date2));
				LogByFileWriter.logger_info(
						"SubscriberMangerBasedOnRuleEngineResponse: " + "Current Date : " + sdf.format(date1));
				LogByFileWriter.logger_info("SubscriberMangerBasedOnRuleEngineResponse: "
						+ "Draft List Activate Date : " + sdf.format(date2));

				// Checking for atleast one subscriber in The Draft Campaign list
				if (ListSubscriberCount > 0) {
					if (date1.compareTo(date2) == 0) {
						out.println(
								"Subscriber Found! --> Date1 is equal to Date2! --> Make This Draft List To Active");
						LogByFileWriter.logger_info("SubscriberMangerBasedOnRuleEngineResponse: "
								+ "Subscriber Found! --> Date1 is equal to Date2! --> Make This Draft List To Active");
						activeListChildNode = activeListNode.addNode(draftListId);
						activeListChildNode.setProperty("StartDate", activeListStartDate);
						activeListChildNode.setProperty("DistanceBetweenCampaign", campaignSctivateDays);
						if (activeListNode.hasProperty("ActiveListCounter")) {
							counter = activeListNode.getProperty("ActiveListCounter").getString();
							counterNumer = Integer.parseInt(counter) + 1;
						}
						ListMongoDAO.updateCampaignListStatusAndName(draftListId, "active",
								(activeListName + "_ActiveList_" + String.valueOf(counterNumer)));
						activeListNode.setProperty("ActiveListCounter", String.valueOf(counterNumer));
						draftListNode.setProperty("StartDate", "0000-00-00 00:00:00");
						draftListChildNode.remove();
						// Setting Campaigns For ActiveList
						addCampaignsToActiveList(subFunnelNode, activeListChildNode, response, activeListStartDate);
					} else {
						out.println("Subscriber Found! --> Date1 is Not equal to Date2");
						LogByFileWriter.logger_info("SubscriberMangerBasedOnRuleEngineResponse: "
								+ "Subscriber Found! --> Date1 is Not equal to Date2");
					}
				} else {
					if (date1.compareTo(date2) == 0) {
						out.println(
								"Subscriber Not Found! --> Date1 is equal to Date2! --> Reshedule Activation Date OF Draft List");
						LogByFileWriter.logger_info("SubscriberMangerBasedOnRuleEngineResponse: "
								+ "Subscriber Not Found! --> Date1 is equal to Date2! --> Reshedule Activation Date OF Draft List");
						updateListActivateDate = new Date();
						updateListActivateDate
								.setDate(updateListActivateDate.getDate() + Integer.parseInt(listActivateDays));
						draftListNode.setProperty("StartDate",
								date_formatter_with_timestamp.format(updateListActivateDate));
						draftListChildNode.setProperty("StartDate",
								date_formatter_with_timestamp.format(updateListActivateDate));
						// ListMongoDAO.updateCampaignListAtivateDate(ListId,date_formatter_with_timestamp.format(update_list_activate_date));
					} else {
						out.println("Subscriber Not Found! --> Date1 is Not equal to Date2");
						LogByFileWriter.logger_info("SubscriberMangerBasedOnRuleEngineResponse: "
								+ "Subscriber Not Found! --> Date1 is Not equal to Date2");
					}
				}
			}

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			System.out.println("Date Exception : " + ex.getMessage());
		}
	}

	// This method is used to get All Campaigns in Given category and assign this
	// Campaigns to ActiveList
	public static void addCampaignsToActiveList(Node subFunnelNode, Node activeListChildNode,
			SlingHttpServletResponse response, String activeListStartDate) {
		DateFormat simpleDateFormatTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String campaignActivateDays = ResourceBundle.getBundle("config").getString("campaign_activate_days");
		String campaignActivateHr = ResourceBundle.getBundle("config").getString("campaign_activate_hrs");
		try {
			PrintWriter out = response.getWriter();
			Node activeListCampaignNode = activeListChildNode.addNode("Campaign");
			activeListCampaignNode.setProperty("StartDate", activeListStartDate);
			activeListCampaignNode.setProperty("DistanceBetweenCampaign", campaignActivateDays);
			Node campaignNode = null;
			Node activeLitCampaignChildNode = null;

			String Campaign_Name = null;
			String Campaign_Id = null;
			String campaign_status = null;
			String activeListId = null;
			int dateCounter = 0;
			int noOfDaysAdded = 0;
			Date campaign_send_date = null;
			String campaignSendDate = null;
			Document list_campaign_doc = null;

			if (subFunnelNode.hasNodes()) {
				NodeIterator campaignsitr = subFunnelNode.getNodes();
				// campaign_send_date=simpleDateFormatTimeStamp.parse(activeListStartDate);
				while (campaignsitr.hasNext()) {
					campaign_send_date = simpleDateFormatTimeStamp.parse(activeListStartDate);
					campaignNode = campaignsitr.nextNode();
					Campaign_Name = campaignNode.getName();

					if (!Campaign_Name.equals("List")) {
						noOfDaysAdded = dateCounter * Integer.parseInt(campaignActivateDays);
						campaign_send_date.setDate(campaign_send_date.getDate() + noOfDaysAdded);
						// campaign_send_date.setHours(campaign_send_date.getHours()-Integer.parseInt(campaignActivateHr));
						campaignSendDate = simpleDateFormatTimeStamp.format(campaign_send_date);

						Campaign_Id = campaignNode.getProperty("Campaign_Id").getString();
						campaign_status = campaignNode.getProperty("campaign_status").getString();
						activeListId = activeListChildNode.getName();

						out.println("campaign Send Date:" + campaignSendDate + " ActiveList Id :" + activeListId);

						activeLitCampaignChildNode = activeListCampaignNode.addNode(Campaign_Name);
						activeLitCampaignChildNode.setProperty("Campaign_Id", Campaign_Id);
						activeLitCampaignChildNode.setProperty("List_Id", activeListId);
						activeLitCampaignChildNode.setProperty("campaign_status", campaign_status);
						activeLitCampaignChildNode.setProperty("Campaign_Date", campaignSendDate);

						list_campaign_doc = new Document();
						list_campaign_doc.put("Campaign_Id", Campaign_Id);
						list_campaign_doc.put("List_Id", activeListId);
						list_campaign_doc.put("campaign_status", campaign_status);
						list_campaign_doc.put("Campaign_Date", campaignSendDate);
						ListMongoDAO.addListCampaign(list_campaign_doc, activeListId);

						dateCounter++;
					}
				}
			}
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			System.out.println("Date Exception : " + ex.getMessage());
		}
	}
	/*
	 * 
	 * 
	 * DDDDDDDDDDDDDDDDD OOOOOOOOOOOOOOOO PPPPPP OOOOOOOOOOO SSSSSSSSSSSS
	 * TTTTTTTTTTTTTTTTT
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		try {

			Session session = null;
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

			Node content = session.getRootNode().getNode("content");
			Node content_ip = session.getRootNode().getNode("content");
			// Node content_ip = session.getRootNode().getNode("content");
			DateFormat date_formatter_with_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String list_activate_days = ResourceBundle.getBundle("config").getString("list_activate_days");

			Date set_date = null;

			if (request.getRequestPathInfo().getExtension().equals("list_subscriber_move_rulengine")) {
				try {
				StringBuilder builder = new StringBuilder();
				BufferedReader bufferedReaderCampaign = request.getReader();

				String brokerageline;
				while ((brokerageline = bufferedReaderCampaign.readLine()) != null) {
					builder.append(brokerageline + "\n");
				}
				out.println("Inside list_subscriber_move_rulengine");
				JSONObject json_object = new JSONObject(builder.toString());
				String funnelName = json_object.getString("FunnelName");
				String subFunnelName = json_object.getString("SubFunnelName");
				String subscriber_email = json_object.getString("SubscriberEmail");
				String SubscriberId = json_object.getString("SubscriberId");
				String ListId = json_object.getString("ListId");
				String CampaignId = json_object.getString("CampaignId");
				String Category = json_object.getString("SubFunnelName");
				String CreatedBy = json_object.getString("CreatedBy");
				// String OutPutTemp_1=json_object.getString("OPTemp");
				String Move_Category = json_object.getString("Output");

//				Node shoppingnode = null;
//				String expstatus = new FreetrialShoppingCartUpdate().checkFreeTrialExpirationStatus(logged_in_user);
//				String group = "";
//				group = request.getParameter("group");
//				shoppingnode = new FreetrialShoppingCartUpdate().getLeadAutoConverterNode(expstatus, logged_in_user,
//						group, session, response);

//				if (shoppingnode != null) {
				out.println("funnelName : " + funnelName);
				out.println("subscriber_email : " + subscriber_email);
				out.println("SubscriberId : " + SubscriberId);
				out.println("ListId : " + ListId);

				Node ListNode = null;
				Node ActiveListNode = null;
				Node DraftListNode = null;
				Node DraftListChildNode = null;
				String ActiveListId = null;
				String DraftListId = null;
				String Counter = null;
				int campaign_counter_int = 0;

				String Add_Subscriber_In_List_Url = ResourceBundle.getBundle("config")
						.getString("Add_Subscriber_In_List");
				String Delete_Subscriber_From_List_Url = ResourceBundle.getBundle("config")
						.getString("Delete_Subscriber_From_List");
				
				
			
				Node shoppingnode=null;
				try {
					shoppingnode = new GetValidityInfoFromEmail().getNodeInfo(CreatedBy, session, response);
//					out.println(jsresp);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(shoppingnode!=null) {
//						Node userNode = session.getRootNode().getNode(leadnode);
					Node subFunnelNode =shoppingnode.getNode("Email").getNode("Funnel")
							.getNode(funnelName).getNode(Move_Category);
				
				
				
					
				out.println("subFunneNode : " + subFunnelNode.getName());
				ListNode = subFunnelNode.getNode("List");
				out.println("ListNode : " + ListNode.getName());
				ActiveListNode = ListNode.getNode("ActiveList");
				DraftListNode = ListNode.getNode("DraftList");
				String listid = null;
				String listname = CreatedBy + "_" + funnelName + "_" + Move_Category;
				if (!DraftListNode.hasNodes()) {
					String counter = null;
					if (DraftListNode.hasProperty("DraftListCounter")) {
						counter = DraftListNode.getProperty("DraftListCounter").getString();
					}
					int counter_num = Integer.parseInt(counter) + 1;
					DraftListNode.setProperty("DraftListCounter", String.valueOf(counter_num));
					set_date = new Date();
					set_date.setDate(set_date.getDate() + Integer.parseInt(list_activate_days));
					listname = listname + "_DraftList_" + String.valueOf(counter_num);
					// String listname ="Draft_List_"+OutPutTemp;
					String listurl = ResourceBundle.getBundle("config").getString("List_Add_Url");

					String listparameter = "?name=" + listname + "&description=This Belongs to " + "&listorder=" + 90
							+ "&active=" + 1;
					String listresponse = this.sendpostdata(listurl, listparameter.replace(" ", "%20"), response)
							.replace("<pre>", "");
					JSONObject listjson = new JSONObject(listresponse);
					String liststatusresponse = listjson.getString("status");
					JSONObject getjsonid = listjson.getJSONObject("data");
					listid = getjsonid.getString("id");
					DraftListChildNode = DraftListNode.addNode(listid);
					DraftListNode.setProperty("StartDate", date_formatter_with_timestamp.format(set_date));
					DraftListChildNode.setProperty("StartDate", date_formatter_with_timestamp.format(set_date));
					DraftListId = listid;

					Document campaign_list_doc = new Document();
					campaign_list_doc.put("CreatedBy", CreatedBy);
					campaign_list_doc.put("FunnelName", funnelName);
					campaign_list_doc.put("SubFunnelName", Move_Category);
					campaign_list_doc.put("ListId", listid);
					campaign_list_doc.put("ListName", listname);
					campaign_list_doc.put("ListSubscriberCount", 0);
					campaign_list_doc.put("ListStatus", "draft");
					campaign_list_doc.put("ListActivateDate", set_date);
					campaign_list_doc.put("ListActivateDateStr", date_formatter_with_timestamp.format(set_date));
					List<String> subscribers_doc_list = new ArrayList<String>();
					List<Document> campaigns_doc_list = new ArrayList<Document>();

					campaign_list_doc.put("ListSubscribersArr", subscribers_doc_list);
					campaign_list_doc.put("ListCampaignArr", campaigns_doc_list);
					ListMongoDAO.addCampaignList(campaign_list_doc);
					addSubscriberInPhpListAndMongo(Add_Subscriber_In_List_Url, DraftListId, SubscriberId, response);
					// We also need to delete list from mongodb campaign_details collection and
					// phplist
				} else {
					NodeIterator DraftListNodeTtr = DraftListNode.getNodes();
					while (DraftListNodeTtr.hasNext()) {
						Node draftNode = DraftListNodeTtr.nextNode();
						DraftListId = draftNode.getName();
						out.println("DraftListId : " + DraftListId);
						String ActiveListStartDate = draftNode.getProperty("StartDate").getString();
						// addSubscriberInPhpListAndMongo(Add_Subscriber_In_List_Url,DraftListId,SubscriberId,response);
						out.println("Going... to call method checkStatusOfDraftList()");
						moveSubscriberToDraftList(Add_Subscriber_In_List_Url, CreatedBy, funnelName, Move_Category,
								DraftListId, listname, SubscriberId, response);
						out.println("Method checkStatusOfDraftList() have Called");
					}
				}
				// String deletesubscriberinlistparameters = "?list_id=" + ListId
				// +"&subscriber_id="+SubscriberId;
				// String apiresponse
				// =sendpostdata(Delete_Subscriber_From_List_Url,deletesubscriberinlistparameters.replace("
				// ", "%20"),response).replace("<pre>", "");

				session.save();
				ListMongoDAO.findCampaignDetailsBasedOnCampaignID(CampaignId, subscriber_email);
			}
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			

		} catch (Exception e) {
			out.println("Exception : : :" + e.getMessage());
		}

	}

	// This method is used to check the status of draft list and move it to Active
	// List Or re-schedule it
	public static void moveSubscriberToDraftList(String Add_Subscriber_In_List_Url, String created_by,
			String funnel_name, String sub_funnel_name, String DraftListId, String listname, String UserId,
			SlingHttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			addSubscriberInPhpListAndMongo(Add_Subscriber_In_List_Url, DraftListId, UserId, response);
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			System.out.println("Date Exception : " + ex.getMessage());
		}
	}

	public static void addSubscriberInPhpListAndMongo(String Add_Subscriber_In_List_Url, String ListId, String UserId,
			SlingHttpServletResponse response) {

		String addsubscriberinlistparameters = "?list_id=" + ListId + "&subscriber_id=" + UserId;
		try {
			ListMongoDAO.addSubscriberInCampaignList(ListId, UserId);
			String responsedata = sendpostdata(Add_Subscriber_In_List_Url,
					addsubscriberinlistparameters.replace(" ", "%20"), response).replace("<pre>", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String sendpostdata(String callurl, String urlParameters, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		URL url = new URL(callurl + urlParameters);
		out.println("Url :" + url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		OutputStream writer = conn.getOutputStream();
		writer.write(urlParameters.getBytes());
		int responseCode = conn.getResponseCode();
		StringBuffer buffer = new StringBuffer();
		if (responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
		} else {
			out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}

	public String sendpostdataToCreateList(String callurl, String urlParameters, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		// out.println("urlParameters :" + urlParameters);
		// URL url = new URL(callurl + urlParameters.replace("\\", ""));
		URL url = new URL(callurl);
		// out.println("Url :" + url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		//
		OutputStream writer = conn.getOutputStream();

		writer.write(urlParameters.getBytes());
		// out.println("Writer Url : "+writer);
		int responseCode = conn.getResponseCode();
		// out.println("POST Response Code :: " + responseCode);
		StringBuffer buffer = new StringBuffer();
		//
		if (responseCode == 200) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			//
			// out.println(buffer.toString());
		} else {
			out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}

}
