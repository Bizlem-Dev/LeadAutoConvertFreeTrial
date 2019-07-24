//package leadconverter.activemq;
//import java.io.InputStream;
//import java.util.Random;
//import java.util.ResourceBundle;
//
//import javax.jms.Connection;
//import javax.jms.ConnectionFactory;
//import javax.jms.DeliveryMode;
//import javax.jms.Destination;
//import javax.jms.Message;
//import javax.jms.MessageConsumer;
//import javax.jms.MessageProducer;
//import javax.jms.QueueSender;
//import javax.jms.Session;
//import javax.jms.TextMessage;
//
//import org.apache.activemq.ActiveMQConnectionFactory;
//import org.apache.activemq.ActiveMQSession;
//import org.apache.sling.commons.json.JSONException;
//import org.apache.sling.commons.json.JSONObject;
//
//
//
//
//
//
//public class Producer {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		Producer producer = new Producer();
//		String correlationId = producer.createRandomString();
//		JSONObject json = new JSONObject();
//		try {
//			json.put("name", "/home/ubuntu/test");
//			producer.GetProducer("inbound.queue", correlationId, json.toString());
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
//	
//	public String createRandomString() {
//		Random random = new Random(System.currentTimeMillis());
//		long randomLong = random.nextLong();
//		return Long.toHexString(randomLong);
//	}
//
//
//
//public String GetProducer(String queueName ,String correlationId,String jsonString) {
//		
//		Connection connection = null;
//		ActiveMQSession session = null;
//		Destination destination = null;
//		MessageProducer producer = null;
//		 String activeMQURL = null;
//			String activeMQUsername = null;
//			String activeMQPassword = null;
//	//	MessageConsumer consumer = null;
//		
//		try {
//		// bundle = ResourceBundle.getBundle("serverConfig");
//		
//			// bundle = ResourceBundle.getBundle("serverConfig");
//			// activeMQURL=bundle.getString("activeMQ.URL");
//			 activeMQURL="http://34.74.142.84:8161/admin/";
////			 activeMQUsername=bundle.getString("activeMQ.Username");
////			 activeMQPassword = bundle.getString("activeMQ.Password");
//			 
//			 activeMQUsername="admin";
//			 activeMQPassword ="admin";
//			 
//
//		/*ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
//		"tcp://" + prop.getProperty("activeMQ.URL") + ":61616");*/
//	//ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(activeMQUsername, activeMQPassword, activeMQURL);
//	ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(activeMQUsername, activeMQPassword, "tcp://34.74.142.84:61616");
//     connection = connectionFactory.createConnection();
//    session = (ActiveMQSession) connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
//          
//            destination = session.createQueue(queueName);
//            producer = session.createProducer(destination);
//            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//          connection.start();
//          TextMessage textMessage = session.createTextMessage();
//  		System.out.println(" jsonString in producer of SFDC param :: "+jsonString);
//
//          textMessage.setText(jsonString);
//          textMessage.setJMSCorrelationID(correlationId);
//			textMessage.setJMSRedelivered(true);
//			producer.send(textMessage);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
////			e.printStackTrace();
//			System.out.println(e.getMessage());
//		}finally {
//			try {
//			connection.close();
//			connection=null;
//			}catch (Exception e) {
//				// TODO: handle exception
//				System.out.println(e.getMessage());
//			}
//		}
//  		System.out.println("correlationId Producer :: "+correlationId);
//
//		return correlationId;
//	}
//
//}
