package leadconverter.freetrail;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class GetValidityInfoFromEmail {

public String  getValidityInfo(String email,Session session,SlingHttpServletResponse response) throws JSONException {
	PrintWriter out=null;
	JSONObject validjson = new JSONObject();
	String status="False";
	try {
	//	String email = request.getParameter("email").replace("@", "_");


		Node contentNode = null;
		Node appserviceNode = null;
		Node appfreetrialNode = null;
		Node emailNode = null;

		Node adminserviceidNode = null;
		String adminserviceid = "";
		Node chekedemailNode = null;
		Node grpnode = null;
		
		try {
			out=response.getWriter();
			// out.println("freetrialstatus "+freetrialstatus);
			// out.println("email "+email);

			// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			if (session.getRootNode().hasNode("content")) {
				contentNode = session.getRootNode().getNode("content");
			} else {
				contentNode = session.getRootNode().addNode("content");
			}
			// out.println("contentNode "+contentNode);
			String freetrialstatus = new FreetrialShoppingCartUpdate()
					.checkFreeTrialExpirationStatus(email.replace("@", "_"));

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
								chekedemailNode = emailNode.getNode("LeadAutoConverter");
							} else {
								chekedemailNode = emailNode.addNode("LeadAutoConverter");
							}
//							validjson.put("User", email);
							validjson.put("status", "true");
							validjson.put("Node", chekedemailNode.getPath());
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

				// out.println("in else");

				if (contentNode.hasNode("user")
						&& contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
					emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
					if (emailNode.hasNode("services")
							&& emailNode.getNode("services").hasNode("leadautoconverter")
							&& emailNode.getNode("services").getNode("leadautoconverter").hasNodes()) {
						NodeIterator itr = emailNode.getNode("services").getNode("leadautoconverter")
								.getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
							if (!adminserviceid.equalsIgnoreCase("LeadAutoConvFrTrial")) {

								// out.println("adminserviceid "+adminserviceid);
								if ((adminserviceid != "") && (!adminserviceid.equals("LeadAutoConvFrTrial"))) {

									if (contentNode.hasNode("services")) {
										appserviceNode = contentNode.getNode("services");
									} else {
										appserviceNode = contentNode.addNode("services");
									}
									// out.println("appserviceNode "+appserviceNode);

									if (appserviceNode.hasNode(adminserviceid)) {
										appfreetrialNode = appserviceNode.getNode(adminserviceid);

										// out.println("appfreetrialNode "+appfreetrialNode);
										String quantity = "";
										String Subscriber_count_Id = "0";
										String end_date = "";
										String start_date = "";
										boolean validity = false;

										if (appfreetrialNode.hasProperty("quantity")) {
											quantity = appfreetrialNode.getProperty("quantity").getString();
											// out.println("quantity "+quantity);

											if (appfreetrialNode.hasProperty("Subscriber_count_Id")) {
												Subscriber_count_Id = appfreetrialNode
														.getProperty("Subscriber_count_Id").getString();

												int qty = Integer.parseInt(quantity);
												int dcount = Integer.parseInt(Subscriber_count_Id);
												// out.println("qty "+ qty +"dcount "+dcount);
												if (qty >= dcount) {
													validity = true;
													// out.println(" validity "+validity);
													if (appfreetrialNode.hasProperty("end_date")) {
														end_date = appfreetrialNode.getProperty("end_date")
																.getString();
														start_date = appfreetrialNode.getProperty("start_date")
																.getString();
														String currentDate = new SimpleDateFormat("yyyy-MM-dd")
																.format(new Date());
														Date currentdateobj = new SimpleDateFormat("yyyy-MM-dd")
																.parse(currentDate);
														Date enddateobj = new SimpleDateFormat("yyyy-MM-dd")
																.parse(end_date);
														// out.println(" enddateobj "+enddateobj
														// +"currentdateobj
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
												// donot have Document_count_Id id , means no document generated
												// till
												// now
												validity = true;

											}
										} else if (appfreetrialNode.hasProperty("end_date")) {
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
//												 out.print("else vallidity is false");
											validity = false;
										}

										if (validity) {
//												out.println("LeadAutoConverter validity=  "+validity);
											if (appfreetrialNode.hasProperty("producttype")
													&& appfreetrialNode.getProperty("producttype").getString()
															.equals("leadautoconverter")) {

												NodeIterator groupItr = appfreetrialNode.getNodes();
												while (groupItr.hasNext()) {
													Node groupNode = groupItr.nextNode();
													Node userNode = null;

//													out.println("groupNode = " + c);
													String group = null;
													group = groupNode.getName();
													if (groupNode.hasNode("users")) {
														userNode = groupNode.getNode("users");
														if (userNode.hasNode(email)) {
														Node	emailNode1 = userNode.getNode(email);
															grpnode = emailNode1.getParent();
//															out.println("grpnode : "+grpnode);
															if (groupNode.hasNode("LeadAutoConverter")) {
																chekedemailNode = groupNode.getNode("LeadAutoConverter");
														} 
//																out.println("chekedemailNode : "+chekedemailNode);
															validjson.put("User", email);
															validjson.put("adminserviceid", adminserviceid);
															validjson.put("Node", chekedemailNode.getPath());
															validjson.put("ShoppingNode", chekedemailNode);
															validjson.put("GroupNode", grpnode.getPath());
															validjson.put("Group", group);
															
															status="true";
															validjson.put("status",status);
															validjson.put("quantity", quantity);
															validjson.put("Subscriber_count_Id",
																	Subscriber_count_Id);
															validjson.put("startDate", start_date);
															validjson.put("endDate", end_date);

														}

													} else {

													}
												}
											}
//												if (appfreetrialNode.hasNode(group)) {
//													emailNode = appfreetrialNode.getNode(group);
//												} else {
//													emailNode = appfreetrialNode.addNode(group);
//												}
//												// out.println("emailNode "+emailNode);
//												if (emailNode.hasNode("LeadAutoConverter")) {
//													LeadAutoNode = emailNode.getNode("LeadAutoConverter");
//												} else {
//													LeadAutoNode = emailNode.addNode("LeadAutoConverter");
//												}

										} else {
//												out.println("else validity=  "+validity);
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
		

			// session.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			 out.println("1 e"+e);
			chekedemailNode = null;
			validjson.put("status", status);
		}
		

//		out.println(validjson.toString());//"chekedemailNode :: " + chekedemailNode + " ::: grpnode" + grpnode + "  :: validjson = "
		

	} catch (Exception ex) {
		out.print("2 e "+ex);
		validjson.put("status", status);
//		return ex.toString();
	}

	
	return validjson.toString();
}
public Node  getNodeInfo(String email,Session session,SlingHttpServletResponse response) throws JSONException {
	PrintWriter out=null;
	JSONObject validjson = new JSONObject();
	String status="False";
	Node chekedemailNode = null;
	try {
	//	String email = request.getParameter("email").replace("@", "_");


		Node contentNode = null;
		Node appserviceNode = null;
		Node appfreetrialNode = null;
		Node emailNode = null;

		Node adminserviceidNode = null;
		String adminserviceid = "";
		
		Node grpnode = null;
		email=email.replace("@", "_");
		try {
			out=response.getWriter();
			// out.println("freetrialstatus "+freetrialstatus);
			// out.println("email "+email);

			// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			if (session.getRootNode().hasNode("content")) {
				contentNode = session.getRootNode().getNode("content");
			} else {
				contentNode = session.getRootNode().addNode("content");
			}
			// out.println("contentNode "+contentNode);
			String freetrialstatus = new FreetrialShoppingCartUpdate()
					.checkFreeTrialExpirationStatus(email.replace("@", "_"));

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
								chekedemailNode = emailNode.getNode("LeadAutoConverter");
							} else {
								chekedemailNode = emailNode.addNode("LeadAutoConverter");
							}
//							validjson.put("User", email);
							validjson.put("status", "true");
							validjson.put("Node", chekedemailNode.getPath());
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

				// out.println("in else");

				if (contentNode.hasNode("user")
						&& contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
					emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
					if (emailNode.hasNode("services")
							&& emailNode.getNode("services").hasNode("leadautoconverter")
							&& emailNode.getNode("services").getNode("leadautoconverter").hasNodes()) {
						NodeIterator itr = emailNode.getNode("services").getNode("leadautoconverter")
								.getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
							if (!adminserviceid.equalsIgnoreCase("LeadAutoConvFrTrial")) {

								// out.println("adminserviceid "+adminserviceid);
								if ((adminserviceid != "") && (!adminserviceid.equals("LeadAutoConvFrTrial"))) {

									if (contentNode.hasNode("services")) {
										appserviceNode = contentNode.getNode("services");
									} else {
										appserviceNode = contentNode.addNode("services");
									}
									// out.println("appserviceNode "+appserviceNode);

									if (appserviceNode.hasNode(adminserviceid)) {
										appfreetrialNode = appserviceNode.getNode(adminserviceid);

										// out.println("appfreetrialNode "+appfreetrialNode);
										String quantity = "";
										String Subscriber_count_Id = "0";
										String end_date = "";
										String start_date = "";
										boolean validity = false;

										if (appfreetrialNode.hasProperty("quantity")) {
											quantity = appfreetrialNode.getProperty("quantity").getString();
											// out.println("quantity "+quantity);

											if (appfreetrialNode.hasProperty("Subscriber_count_Id")) {
												Subscriber_count_Id = appfreetrialNode
														.getProperty("Subscriber_count_Id").getString();

												int qty = Integer.parseInt(quantity);
												int dcount = Integer.parseInt(Subscriber_count_Id);
												// out.println("qty "+ qty +"dcount "+dcount);
												if (qty >= dcount) {
													validity = true;
													// out.println(" validity "+validity);
													if (appfreetrialNode.hasProperty("end_date")) {
														end_date = appfreetrialNode.getProperty("end_date")
																.getString();
														start_date = appfreetrialNode.getProperty("start_date")
																.getString();
														String currentDate = new SimpleDateFormat("yyyy-MM-dd")
																.format(new Date());
														Date currentdateobj = new SimpleDateFormat("yyyy-MM-dd")
																.parse(currentDate);
														Date enddateobj = new SimpleDateFormat("yyyy-MM-dd")
																.parse(end_date);
														// out.println(" enddateobj "+enddateobj
														// +"currentdateobj
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
												// donot have Document_count_Id id , means no document generated
												// till
												// now
												validity = true;

											}
										} else if (appfreetrialNode.hasProperty("end_date")) {
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
//												 out.print("else vallidity is false");
											validity = false;
										}

										if (validity) {
//												out.println("LeadAutoConverter validity=  "+validity);
											if (appfreetrialNode.hasProperty("producttype")
													&& appfreetrialNode.getProperty("producttype").getString()
															.equals("leadautoconverter")) {

												NodeIterator groupItr = appfreetrialNode.getNodes();
												while (groupItr.hasNext()) {
													Node groupNode = groupItr.nextNode();
													Node userNode = null;

//													out.println("groupNode = " + c);
													String group = null;
													group = groupNode.getName();
													if (groupNode.hasNode("users")) {
														userNode = groupNode.getNode("users");
														if (userNode.hasNode(email)) {
														Node	emailNode1 = userNode.getNode(email);
															grpnode = emailNode1.getParent();
//															out.println("grpnode : "+grpnode);
															if (groupNode.hasNode("LeadAutoConverter")) {
																chekedemailNode = groupNode.getNode("LeadAutoConverter");
														} 
//																out.println("chekedemailNode : "+chekedemailNode);
															validjson.put("User", email);
															validjson.put("adminserviceid", adminserviceid);
															validjson.put("Node", chekedemailNode.getPath());
															validjson.put("ShoppingNode", chekedemailNode);
															validjson.put("GroupNode", grpnode.getPath());
															validjson.put("Group", group);
															
															status="true";
															validjson.put("status",status);
															validjson.put("quantity", quantity);
															validjson.put("Subscriber_count_Id",
																	Subscriber_count_Id);
															validjson.put("startDate", start_date);
															validjson.put("endDate", end_date);

														}

													} else {

													}
												}
											}
//												if (appfreetrialNode.hasNode(group)) {
//													emailNode = appfreetrialNode.getNode(group);
//												} else {
//													emailNode = appfreetrialNode.addNode(group);
//												}
//												// out.println("emailNode "+emailNode);
//												if (emailNode.hasNode("LeadAutoConverter")) {
//													LeadAutoNode = emailNode.getNode("LeadAutoConverter");
//												} else {
//													LeadAutoNode = emailNode.addNode("LeadAutoConverter");
//												}

										} else {
//												out.println("else validity=  "+validity);
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
		

			// session.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			 out.println("1 e"+e);
			chekedemailNode = null;
			validjson.put("status", status);
		}
		

//		out.println(validjson.toString());//"chekedemailNode :: " + chekedemailNode + " ::: grpnode" + grpnode + "  :: validjson = "
		

	} catch (Exception ex) {
//		out.print("2 e "+ex);
		chekedemailNode = null;
		validjson.put("status", status);
//		return ex.toString();
	}

	
	return chekedemailNode;
}
}
