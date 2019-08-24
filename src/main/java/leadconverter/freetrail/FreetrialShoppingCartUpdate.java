package leadconverter.freetrail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.JSONArray;

import leadconverter.doctiger.LogByFileWriter;
import leadconverter.mongo.MongoDAO;

public class FreetrialShoppingCartUpdate {

	public String checkFreeTrialExpirationStatus(String userid) {

		String free_trail_status = "1";
		int expireFlag = 1;
		long free_trail_subscribers_count = Long
				.parseLong(ResourceBundle.getBundle("config").getString("free_trail_subscribers_count"));
		long subscribers_count =0;
		try {
			try {
			MongoDAO mdao = new MongoDAO();
			 subscribers_count = mdao.getSubscriberCountForLoggedInUserForFreeTrail("subscribers_details", userid);
			}catch (Exception e) {
				// TODO: handle exception
			}
			free_trail_status = checkfreetrial(userid);
			// long subscribers_count=2000;
			// String free_trail_status="0";
			System.out
					.println("free_trail_status : " + free_trail_status + "  subscribers_count : " + subscribers_count);
			if (subscribers_count <= free_trail_subscribers_count && free_trail_status.equals("0")) {
				System.out.println("Free Trial is Active");
				LogByFileWriter.logger_info("FreeTrialandCart : Free Trail Status : Free Train is Active");
				expireFlag = 0;
			} else if (free_trail_status.equals("1")) {
				System.out.println("Free Trial Date Expired");
				LogByFileWriter.logger_info("FreeTrialandCart : Free Trail Status : Free Train Date Expired");
				expireFlag = 1;
			} else if (subscribers_count > free_trail_subscribers_count) {
				System.out.println("Subscriber Count is More");
				LogByFileWriter.logger_info("FreeTrialandCart : Free Trail Status : Subscriber Count is More");
				expireFlag = 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// return Integer.toString(expireFlag);
		LogByFileWriter.logger_info("FreeTrialandCart : Free Trail Status : " + free_trail_status +" : subscribers_count :: "+subscribers_count+" :: free_trail_status = "+free_trail_status);
		return expireFlag + "";
	}

	public String checkfreetrial(String userid) {
		int expireFlag = 1;
		 Character x = '_';
		 userid = replaceLastChar(userid,x);
		String free_trial_api = ResourceBundle.getBundle("config").getString("free_trial_api") + userid
				+ "/LeadAutoConvFrTrial";
		// http://prod.bizlem.io:8087/apirest/trialmgmt/trialuser/akhilesh@bizlem.com/LeadAutoConvFrTrial
		//http://development.bizlem.io:8087/apirest/trialmgmt/trialuser/viki@gmail.com/LeadAutoConvFrTrial
		//LeadAutoConvFrTrial
		//String free_trial_api = "http://development.bizlem.io:8087/apirest/trialmgmt/trialuser/"+userid.replaceAll("_", "@")+"/LeadAutoConvFrTrial";
	
		try {
			
			URL url = new URL(free_trial_api);
			// System.out.println("Step 1");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 System.out.println("Step 2");
			conn.setRequestMethod("GET");
			conn.connect();
			 System.out.println("Step 3");
			InputStream in = conn.getInputStream();
			 System.out.println("Step 4");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String text = reader.readLine();
			 System.out.println("Step 5");
			 System.out.println(text);
			JSONObject obj = new JSONObject(text);
			expireFlag = obj.getInt("expireFlag");
			 System.out.println("56"+Integer.toString(expireFlag));
			conn.disconnect();
			System.out.println("777");
			LogByFileWriter.logger_info("FreeTrialandCart : Free Trail Status : " + expireFlag + "  For User " + userid);
			System.out.println("end");
		} catch (Exception ex) {
			System.out.println("Error : " + ex);
		}
		System.out.println("Integer.toString(expireFlag) : " + Integer.toString(expireFlag));
		
		
		return 	"1";//pls comm outInteger.toString(expireFlag);
	}
	public static int findLastIndex(String str, Character x) 
	{ 
	 int index = -1; 
	 try {
		for (int i = 0; i < str.length(); i++) 
		     if (str.charAt(i) == x) 
		         index = i;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	 return index; 
	} 

	public static String replaceLastChar(String str, Character x) {

		try {
			int index = findLastIndex(str, x); 
			 if (index == -1) {
			     System.out.println("Character not found"); 
			 }else {
			    
			     str = str.substring(0, index) + "@"
			    		 + str.substring(index+1,str.length());
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return str;
	}

	public Node getLeadAutoConverterNodeOld(String freetrialstatus, String email, String group, Session session1,
			SlingHttpServletResponse response) {

		
		PrintWriter out = null;
		// out.println("in getLeadAutoconNode");

		Node contentNode = null;
		Node appserviceNode = null;
		Node appfreetrialNode = null;
		Node emailNode = null;
		Node LeadAutoconNode = null;

		Node adminserviceidNode = null;
		String adminserviceid = "";

		try {
			out = response.getWriter();
			

			// out.println("in method ");
			// out.println("email "+email);

			// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			if (session1.getRootNode().hasNode("content")) {
				contentNode = session1.getRootNode().getNode("content");
			} else {
				contentNode = session1.getRootNode().addNode("content");
			}
			// out.println("contentNode "+contentNode);

			if (freetrialstatus.equalsIgnoreCase("0")) {

				if (contentNode.hasNode("services")) {
					appserviceNode = contentNode.getNode("services");

					// out.println("appserviceNode "+appserviceNode);

					if (appserviceNode.hasNode("freetrial")) {
						appfreetrialNode = appserviceNode.getNode("freetrial");

						// out.println("appfreetrialNode "+appfreetrialNode);

						if (appfreetrialNode.hasNode("users")
								&& appfreetrialNode.getNode("users").hasNode(email.replace("@", "_"))) {
							emailNode = appfreetrialNode.getNode("users").getNode(email.replace("@", "_"));
							// out.println("emailNode "+emailNode);
							if (emailNode.hasNode("LeadAutoConverter")) {
								LeadAutoconNode = emailNode.getNode("LeadAutoConverter");
							} else {
								LeadAutoconNode = emailNode.addNode("LeadAutoConverter");
							}
							// out.println("LeadAutoconNode "+LeadAutoconNode);

						} else {
							// emailNode=appfreetrialNode.getNode("users").addNode(email.replace("@", "_"));
						}
					} else {
						// appfreetrialNode=appserviceNode.addNode("freetrial");
					}
				} else {
					// appserviceNode=contentNode.addNode("services");
				}

			} else {
				appfreetrialNode=null;
				// out.println("in else");

				if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
					emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
					if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("leadautoconverter")
							&& emailNode.getNode("services").getNode("leadautoconverter").hasNodes()) {
						NodeIterator itr = emailNode.getNode("services").getNode("leadautoconverter").getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
							if (!adminserviceid.equalsIgnoreCase("LeadAutoConvFrTrial")) {
								if ((adminserviceid != "") && (!adminserviceid.equals("LeadAutoConvFrTrial"))) {

									if (contentNode.hasNode("services")) {
										appserviceNode = contentNode.getNode("services");
									} else {
										appserviceNode = contentNode.addNode("services");
									}
//									out.println("appserviceNode " + appserviceNode);
//									out.println("adminserviceid= " + adminserviceid+" ;; group= "+group);

									if (appserviceNode.hasNode(adminserviceid)) {
										appfreetrialNode = appserviceNode.getNode(adminserviceid);
										out.println("appfreetrialNode= " + appfreetrialNode);

										if (appfreetrialNode.hasProperty("producttype")
												&& appfreetrialNode.getProperty("producttype").getString().equals("leadautoconverter")) {
//									
										if (appfreetrialNode.hasNode(group)) {
											emailNode = appfreetrialNode.getNode(group);
										} else {
											emailNode = appfreetrialNode.addNode(group);
										}
										 out.println("emailNode "+emailNode);
										if (emailNode.hasNode("LeadAutoConverter")) {
											LeadAutoconNode = emailNode.getNode("LeadAutoConverter");
										} else {
											LeadAutoconNode = emailNode.addNode("LeadAutoConverter");
										}
									}
									}
//									break;
								}

							}
						}
					}
				}
//				out.println("adminserviceid " + adminserviceid);

			}

			session1.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// out.println(e.getMessage());
			LeadAutoconNode = null;
		}

		return LeadAutoconNode;
	}

	public Node getLeadAutoConverterNode(String freetrialstatus, String email, String group, Session session1,
			SlingHttpServletResponse response) {

		// freetrialstatus="0";
		PrintWriter out = null;
		// out.println("in getDocTigerAdvNode");

		Node contentNode = null;
		Node appserviceNode = null;
		Node appfreetrialNode = null;
		Node emailNode = null;
		Node LeadAutoNode = null;

		Node adminserviceidNode = null;
		String adminserviceid = "";
		try {
			out = response.getWriter();

			// out.println("freetrialstatus "+freetrialstatus);
			// out.println("email "+email);

			// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			if (session1.getRootNode().hasNode("content")) {
				contentNode = session1.getRootNode().getNode("content");
			} else {
				contentNode = session1.getRootNode().addNode("content");
			}
//			 out.println("contentNode "+contentNode);
			 
			if (freetrialstatus.equalsIgnoreCase("0")) {

				if (contentNode.hasNode("services")) {
					appserviceNode = contentNode.getNode("services");

//					 out.println("appserviceNode "+appserviceNode);

					if (appserviceNode.hasNode("freetrial")) {
						appfreetrialNode = appserviceNode.getNode("freetrial");

//						 out.println("appfreetrialNode "+appfreetrialNode);

						if (appfreetrialNode.hasNode("users")
								&& appfreetrialNode.getNode("users").hasNode(email.replace("@", "_"))) {
							emailNode = appfreetrialNode.getNode("users").getNode(email.replace("@", "_"));
							// out.println("emailNode "+emailNode);
							if (emailNode.hasNode("LeadAutoConverter")) {
								LeadAutoNode = emailNode.getNode("LeadAutoConverter");
							} else {
								LeadAutoNode = emailNode.addNode("LeadAutoConverter");
							}
							// out.println("DoctigerAdvNode "+DoctigerAdvNode);

						} else {
							// emailNode=appfreetrialNode.getNode("users").addNode(email.replace("@", "_"));
						}
					} else {
						// appfreetrialNode=appserviceNode.addNode("freetrial");
					}
				} else {
					// appserviceNode=contentNode.addNode("services");
				}

			} else {

//				 out.println("in else");

				if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
					emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
					if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("leadautoconverter")
							&& emailNode.getNode("services").getNode("leadautoconverter").hasNodes()) {
						NodeIterator itr = emailNode.getNode("services").getNode("leadautoconverter").getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
							if (!adminserviceid.equalsIgnoreCase("LeadAutoConvFrTrial")) {

//								 out.println("adminserviceid "+adminserviceid);
								if ((adminserviceid != "") && (!adminserviceid.equals("LeadAutoConvFrTrial"))) {

									if (contentNode.hasNode("services")) {
										appserviceNode = contentNode.getNode("services");
									} else {
										appserviceNode = contentNode.addNode("services");
									}
//									 out.println("appserviceNode "+appserviceNode);

									if (appserviceNode.hasNode(adminserviceid)) {
										appfreetrialNode = appserviceNode.getNode(adminserviceid);

//										 out.println("appfreetrialNode "+appfreetrialNode);
										String quantity = "";
										String Subscriber_count_Id = "0";
										String end_date = "";
										boolean validity = false;

										if (appfreetrialNode.hasProperty("quantity")) {
											quantity = appfreetrialNode.getProperty("quantity").getString();
											// out.println("quantity "+quantity);

											if (appfreetrialNode.hasProperty("Subscriber_count_Id")) {
												Subscriber_count_Id = appfreetrialNode.getProperty("Subscriber_count_Id")
														.getString();

												int qty = Integer.parseInt(quantity);
												int dcount = Integer.parseInt(Subscriber_count_Id);
												// out.println("qty "+ qty +"dcount "+dcount);
												if (qty > dcount) {
													validity = true;
													// out.println(" validity "+validity);
													if (appfreetrialNode.hasProperty("end_date")) {
														end_date = appfreetrialNode.getProperty("end_date").getString();
														String currentDate = new SimpleDateFormat("yyyy-MM-dd")
																.format(new Date());
														Date currentdateobj = new SimpleDateFormat("yyyy-MM-dd")
																.parse(currentDate);
														Date enddateobj = new SimpleDateFormat("yyyy-MM-dd")
																.parse(end_date);
														// out.println(" enddateobj "+enddateobj +"currentdateobj
														// "+currentdateobj);

														if (enddateobj.before(currentdateobj)) {
															validity = false;
															// out.println(" validity date "+validity);
														}
													} else {
														validity = true;
													}
												} else {
													validity = false;
												}
											} else {
												// donot have Document_count_Id id , means no document generated till
												// now
												validity = true;

											}
										} else if (appfreetrialNode.hasProperty("end_date")) {
											end_date = appfreetrialNode.getProperty("end_date").getString();
											String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
											Date currentdateobj = new SimpleDateFormat("yyyy-MM-dd").parse(currentDate);
											Date enddateobj = new SimpleDateFormat("yyyy-MM-dd").parse(end_date);
											// out.println(" enddateobj "+enddateobj +"currentdateobj "+currentdateobj);

											if (enddateobj.before(currentdateobj)) {
												validity = false;
												// out.println(" validity date "+validity);

											}
										} else {
//											 out.print("else vallidity is false");
											validity = false;
										}
//										out.println("LeadAutoConverter validity=  "+validity);
										if (validity) {
//											out.println("LeadAutoConverter validity=  "+validity);
											if (appfreetrialNode.hasNode(group)) {
												emailNode = appfreetrialNode.getNode(group);
											} else {
												emailNode = appfreetrialNode.addNode(group);
											}
//											 out.println("emailNode "+emailNode);
											if (emailNode.hasNode("LeadAutoConverter")) {
												LeadAutoNode = emailNode.getNode("LeadAutoConverter");
											} else {
												LeadAutoNode = emailNode.addNode("LeadAutoConverter");
											}
//											out.println("else LeadAutoNodes=  "+LeadAutoNode);
										} else {
//											out.println("else validity=  "+validity);
										}
										break;
									}
								}

								break;
							}
						}
					}
				}

			}

		 session1.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// out.println(e.getMessage());
			LeadAutoNode = null;
		}

		return LeadAutoNode;
	}

	public String updateSubscriberCounter(String useremail, String freetrialstatus, Node doctigerAdvNode,
			Session session, SlingHttpServletResponse response, int subscount) {
		Node serviceIdNode = null;
		Node GroupNode = null;
		Node useremailNode = null;
		try {
			if (freetrialstatus.equalsIgnoreCase("1")) {
				// increment count on groupnode
				GroupNode = doctigerAdvNode.getParent();
				if (GroupNode.hasProperty("Subscriber_count_group")) {
					int count = Integer.parseInt(GroupNode.getProperty("Subscriber_count_group").getString());
					int totalcount = count + subscount;
					GroupNode.setProperty("Subscriber_count_group", totalcount);
				} else {

					GroupNode.setProperty("Subscriber_count_group", subscount);
				}

				// increment count on useremail
				if (GroupNode.hasNode("users") && GroupNode.getNode("users").hasNode(useremail.replace("@", "_"))) {
					useremailNode = GroupNode.getNode("users").getNode(useremail.replace("@", "_"));

					if (useremailNode.hasProperty("Subscriber_count_user")) {
						int count = Integer.parseInt(useremailNode.getProperty("Subscriber_count_user").getString());
						int totalcount = count + subscount;
						useremailNode.setProperty("Subscriber_count_user", totalcount);
					} else {

						useremailNode.setProperty("Subscriber_count_user", subscount);
					}
				}

				// increment count on serviceid
				serviceIdNode = GroupNode.getParent();
				if (serviceIdNode.hasProperty("Subscriber_count_Id")) {
					int count = Integer.parseInt(serviceIdNode.getProperty("Subscriber_count_Id").getString());
					int totalcount = count + subscount;
					serviceIdNode.setProperty("Subscriber_count_Id", totalcount);
				} else {

					serviceIdNode.setProperty("Subscriber_count_Id", subscount);
				}

			}else {}
			session.save();
			return "true";

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return "false";
		}
	}

	
	public JSONObject getLeadAutoConverterGroupList( String email, Session session1,
			SlingHttpServletResponse response) {

		// freetrialstatus="0";
		PrintWriter out = null;
		// out.println("in getLeadAutoconNode");

		Node contentNode = null;
		Node appserviceNode = null;
		Node appfreetrialNode = null;
		Node emailNode = null;
		Node LeadAutoconNode = null;

		Node adminserviceidNode = null;
		String adminserviceid = "";
		JSONObject grjs=new JSONObject();
		JSONArray groupjsa=new JSONArray();

		try {
			out = response.getWriter();

			// out.println("in method ");
			// out.println("email "+email);

			// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			if (session1.getRootNode().hasNode("content")) {
				contentNode = session1.getRootNode().getNode("content");
			} else {
				contentNode = session1.getRootNode().addNode("content");
			}
		

				if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
					emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
					if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("leadautoconverter")
							&& emailNode.getNode("services").getNode("leadautoconverter").hasNodes()) {
						NodeIterator itr = emailNode.getNode("services").getNode("leadautoconverter").getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
							if (!adminserviceid.equalsIgnoreCase("LeadAutoConvFrTrial")) {
								if ((adminserviceid != "") && (!adminserviceid.equals("LeadAutoConvFrTrial"))) {

									if (contentNode.hasNode("services")) {
										appserviceNode = contentNode.getNode("services");
									} else {
										appserviceNode = contentNode.addNode("services");
									}
//									out.println("appserviceNode " + appserviceNode);
//									out.println("adminserviceid " + adminserviceid);
									if (appserviceNode.hasNode(adminserviceid)  ) { //!appserviceNode.hasNode("freetrial")
										appfreetrialNode = appserviceNode.getNode(adminserviceid);

//										out.println("appfreetrialNode " + appfreetrialNode);
										
										if (appfreetrialNode.hasProperty("producttype")
												&& appfreetrialNode.getProperty("producttype").getString().equals("leadautoconverter")) {
//											out.println("userNode= " + appfreetrialNode);
											NodeIterator groupItr = appfreetrialNode.getNodes();
											while (groupItr.hasNext()) {
												
												Node groupNode = groupItr.nextNode();
//												out.println("groupNode = " + groupNode);
												groupjsa.put(groupNode.getName());
												}
											grjs.put("adminserviceid", adminserviceid);
											
									}
									}
									break;
								}

							}
						}
					}
				}
			

			
				grjs.put("Groups", groupjsa);
			session1.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// out.println(e.getMessage());
			LeadAutoconNode = null;
		}

		return grjs;
	}
	public  String checkValiditytrialCart(String userid) {
		int expireFlag = 1;
		JSONObject obj = null;// "http://development.bizlem.io:8082/portal/servlet/service/ui.checkValidUser"
		String CheckfreetrailCart_api =ResourceBundle.getBundle("config").getString("CheckValidUsertrialandcart") +"?email=" + userid ;
		StringBuffer response = null;
		int responseCode = 0;
		try {
			URL url = new URL(CheckfreetrailCart_api);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			con.setDoOutput(true);
	
			responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println("responseCode : "+responseCode+"\n"+"ResponseBody== : "+response);
			String resp=response.toString();
			obj=new JSONObject(resp);
			System.out.println("obj.toString() : "+obj.toString());
			//LogByFileWriter.logger_info("CreateRuleEngine : " + "responseCode : "+responseCode+"\n"+"ResponseBody : "+response);
		} catch (Exception e) {
			System.out.println("Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
			//LogByFileWriter.logger_info("CreateRuleEngine : " + "Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
		}
		return obj.toString();
	}
	
	public static void main(String args[]) {
		System.out.println("method  ret = "+	new FreetrialShoppingCartUpdate().checkfreetrial("viki_gmail.com"));
	//new FreetrialShoppingCartUpdate().checkValiditytrialCart("");
		
	}
}
