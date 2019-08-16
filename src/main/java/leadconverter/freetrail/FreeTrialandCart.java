//package leadconverter.freetrail;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import javax.jcr.Node;
//import javax.jcr.NodeIterator;
//import javax.jcr.Session;
//
//import leadconverter.doctiger.LogByFileWriter;
//import leadconverter.mongo.MongoDAO;
//
//import org.apache.sling.api.SlingHttpServletResponse;
//import org.apache.sling.commons.json.JSONObject;
//
//public class FreeTrialandCart {
//	ResourceBundle bundle = ResourceBundle.getBundle("config");
//	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
//
//	public static void main(String[] args) {
//		//String free_trail_status=new FreeTrialandCart().checkfreetrial("akhileshyadav3145.biz@gmail.com");
//		//System.out.println("free_trail_status : "+free_trail_status);
//		//"viki@gmail.com"
//		String free_trail_status=new FreeTrialandCart().checkFreeTrialExpirationStatus("viki@gmail.com");
//		System.out.println("free_trail_status : "+free_trail_status);
//		
//	}
//	
//	public String checkFreeTrialExpirationStatus(String userid) {
//		String free_trail_status="1";
//		int expireFlag = 1;
//		long free_trail_subscribers_count=Long.parseLong(ResourceBundle.getBundle("config").getString("free_trail_subscribers_count"));
//		try {
//			MongoDAO mdao=new MongoDAO();
//			long subscribers_count=mdao.getSubscriberCountForLoggedInUserForFreeTrail("subscribers_details",userid);
//			free_trail_status=new FreeTrialandCart().checkfreetrial(userid);
//			//long subscribers_count=2000;
//			//String free_trail_status="0";
//			System.out.println("free_trail_status : "+free_trail_status+"  subscribers_count : "+subscribers_count);
//			if(subscribers_count<=free_trail_subscribers_count&&free_trail_status.equals("0")){
//				System.out.println("Free Trial is Active");
//				LogByFileWriter.logger_info("FreeTrialandCart : Free Trail Status : Free Train is Active");
//				expireFlag=0;
//			}else if(free_trail_status.equals("1")){
//				System.out.println("Free Trial Date Expired");
//				LogByFileWriter.logger_info("FreeTrialandCart : Free Trail Status : Free Train Date Expired");
//				expireFlag=1;
//			}else if(subscribers_count>free_trail_subscribers_count){
//				System.out.println("Subscriber Count is More");
//				LogByFileWriter.logger_info("FreeTrialandCart : Free Trail Status : Subscriber Count is More");
//				expireFlag=1;
//			}
//		} catch (Exception ex) {
//			System.out.println("Error : "+ex);
//		}
//		//return Integer.toString(expireFlag);
//		LogByFileWriter.logger_info("FreeTrialandCart : Free Trail Status : " + free_trail_status);
//		return free_trail_status;
//	}
//
//	public String checkfreetrial(String userid) {
//		int expireFlag = 1;
//		String free_trial_api = ResourceBundle.getBundle("config").getString("free_trial_api") + userid + "/LeadAutoConvFrTrial";
//		// http://prod.bizlem.io:8087/apirest/trialmgmt/trialuser/akhilesh@bizlem.com/LeadAutoConvFrTrial
//		try {
//			URL url = new URL(free_trial_api);
//			//System.out.println("Step 1");
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			//System.out.println("Step 2");
//			conn.setRequestMethod("GET");
//			conn.connect();
//			//System.out.println("Step 3");
//			InputStream in = conn.getInputStream();
//			//System.out.println("Step 4");
//			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//			String text = reader.readLine();
//			//System.out.println("Step 5");
//			//System.out.println(text);
//			JSONObject obj = new JSONObject(text);
//			expireFlag = obj.getInt("expireFlag");
//			//System.out.println(Integer.toString(expireFlag));
//			conn.disconnect();
//		} catch (Exception ex) {
//			System.out.println("Error : "+ex);
//		}
//		LogByFileWriter.logger_info("FreeTrialandCart : Free Trail Status : " + expireFlag+"  For User "+userid);
//		return Integer.toString(expireFlag);
//	}
//
//	public Node getMailtangyNode(String freetrialstatus, String email, String group, Session session1,
//			SlingHttpServletResponse response) {
//
//		// freetrialstatus="0";
//		PrintWriter out = null;
//		// out.println("in getDocTigerAdvNode");
//
//		Node contentNode = null;
//		Node appserviceNode = null;
//		Node appfreetrialNode = null;
//		Node emailNode = null;
//		Node mailtangyNode = null;
//
//		Node adminserviceidNode = null;
//		String adminserviceid = "";
//		try {
//			out = response.getWriter();
//
//			// out.println("freetrialstatus "+freetrialstatus);
//			// out.println("email "+email);
//
////			 session1 = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
//			if (session1.getRootNode().hasNode("content")) {
//				contentNode = session1.getRootNode().getNode("content");
//			} else {
//				contentNode = session1.getRootNode().addNode("content");
//			}
////			 out.println("contentNode "+contentNode);
//
//			if (freetrialstatus.equalsIgnoreCase("0")) {
//
//				if (contentNode.hasNode("services")) {
//					appserviceNode = contentNode.getNode("services");
//
//					// out.println("appserviceNode "+appserviceNode);
//
//					if (appserviceNode.hasNode("freetrial")) {
//						appfreetrialNode = appserviceNode.getNode("freetrial");
//
//						// out.println("appfreetrialNode "+appfreetrialNode);
//
//						if (appfreetrialNode.hasNode("users")
//								&& appfreetrialNode.getNode("users").hasNode(email.replace("@", "_"))) {
//							emailNode = appfreetrialNode.getNode("users").getNode(email.replace("@", "_"));
////							 out.println("emailNode == "+emailNode);
//							if (emailNode.hasNode("MailTangy")) {
//								mailtangyNode = emailNode.getNode("MailTangy");
//							} else {
//								mailtangyNode = emailNode.addNode("MailTangy");
//							}
//							// out.println("DoctigerAdvNode "+DoctigerAdvNode);
//
//						} else {
//							// emailNode=appfreetrialNode.getNode("users").addNode(email.replace("@", "_"));
//						}
//					} else {
//						// appfreetrialNode=appserviceNode.addNode("freetrial");
//					}
//				} else {
//					// appserviceNode=contentNode.addNode("services");
//				}
//
//			} else {
//
//				// out.println("in else");
//
//				if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
//					emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
//					if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("mailtangy")
//							&& emailNode.getNode("services").getNode("mailtangy").hasNodes()) {
//						NodeIterator itr = emailNode.getNode("services").getNode("mailtangy").getNodes();
//						while (itr.hasNext()) {
//							adminserviceid = itr.nextNode().getName();
////							out.println("adminserviceid: " + adminserviceid);
//							if (!adminserviceid.equalsIgnoreCase("MailTangyFreeTrial")) {
//								break;
//							}
//						}
//					}
//				}
//				// out.println("adminserviceid "+adminserviceid);
//				if ((adminserviceid != "") && (!adminserviceid.equals("MailTangyFreeTrial"))) {
//
//					if (contentNode.hasNode("services")) {
//						appserviceNode = contentNode.getNode("services");
//					} else {
//						appserviceNode = contentNode.addNode("services");
//					}
////					out.println("appserviceNode " + appserviceNode);
//
//					if (appserviceNode.hasNode(adminserviceid)) {
//						appfreetrialNode = appserviceNode.getNode(adminserviceid);
//					} else {
//						appfreetrialNode = appserviceNode.addNode(adminserviceid);
//					}
//					if (appfreetrialNode.hasNode(group)) {
//						emailNode = appfreetrialNode.getNode(group);
//					} else {
//						emailNode = appfreetrialNode.addNode(group);
//					}
//					// out.println("emailNode "+emailNode);
//					if (emailNode.hasNode("MailTangy")) {
//						mailtangyNode = emailNode.getNode("MailTangy");
//					} else {
//						mailtangyNode = emailNode.addNode("MailTangy");
//					}
//				}
//			}
//
//			 session1.save();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			// out.println(e.getMessage());
//			mailtangyNode = null;
//		}
//
//		return mailtangyNode;
//	}
//}
