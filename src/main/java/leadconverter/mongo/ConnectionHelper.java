package leadconverter.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ConnectionHelper {
	
	private static MongoClient mongoClient;
    public  static  MongoDatabase database;
	
	
	//public  static  MongoClient mongoClient = null;
	DB db = null;
	
	public static Mongo getMongoConn(){
		Mongo mongo = null;
		try {
			   mongo      = new Mongo();
			}
		    catch (Exception e) {
		        e.printStackTrace();
		    }
		    return mongo;
       }
	
	public static MongoClient getConnection(){
		try {
			   MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
			   mongoClient = new MongoClient(connectionString);
			   System.out.println("Connecting to MongoDB  Server new version.......  "+mongoClient.getAddress());
            }
		    catch (Exception e) {
		        e.printStackTrace();
		    }
		    return mongoClient;
       }
	@SuppressWarnings({ "unused", "deprecation"})
	public static MongoClient getConn(){
		try {
			   mongoClient = new MongoClient(new ServerAddress("localhost", 27017));
			   System.out.println("Connecting to MongoDB  Server old version.......  "+mongoClient.getAddress());
            }
		    catch (Exception e) {
		        e.printStackTrace();
		    }
		    return mongoClient;
       }
	
	public static void dropConnection(MongoCollection<?> collection)
	{
		try {
			if (collection != null) {
				collection.drop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeConnection(MongoClient connection)
	{
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void dropConn(DBCollection collection)
	{
		try {
			if (collection != null) {
				collection.drop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeConn(MongoClient connection)
	{
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//mongoClient = new MongoClient();
    // mongoClient = new MongoClient("localhost");
    // mongoClient = new MongoClient("localhost", 27017);
    //   mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    // MongoClient mongoClient = new MongoClient(
    //  	    new MongoClientURI("mongodb://host1:27017,host2:27017,host3:27017/?replicaSet=myReplicaSet"));
    //   MongoClient mongoClient = new MongoClient(
    //   		Arrays.asList(new ServerAddress("host1", 27017),
    //   		              new ServerAddress("host2", 27017),
    //   		              new ServerAddress("host3", 27017)));
    //MongoClientURI uri = new MongoClientURI("mongodb://user1:pwd1@host1/?authSource=db1&ssl=true");   
	/* 
	MongoClient mongoClient = new MongoClient();
	DB db = mongoClient.getDB("test");
	
	char[] password = new char[] {'s', 'e', 'c', 'r', 'e', 't'};
	boolean authenticated = ((Object) db).authenticate("root", password);
	 
	if (authenticated) {
	    System.out.println("Successfully logged in to MongoDB!");
	} else {
	    System.out.println("Invalid username/password");
	}
	*/
	/*
	MongoCredential journaldevAuth = MongoCredential.createPlainCredential("admin", "webapp", "admin".toCharArray());
	List<MongoCredential> auths = new ArrayList<MongoCredential>();
	auths.add(journaldevAuth);
	
	ServerAddress serverAddress = new ServerAddress("localhost", 27017);
	mongoClient = new MongoClient(serverAddress, auths);
	*/
	
	 /*
	 String user="admin"; // the user name
	 String database_name="webapp"; // the name of the database in which the user is defined
	 char[] password={ 'a', 'd', 'm', 'i', 'n' };; // the password as a character array
	 MongoCredential credential = MongoCredential.createCredential(user, database_name, password);
     MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();
     mongoClient = new MongoClient(new ServerAddress("localhost", 27017),
	                                           Arrays.asList(credential),
	                                           options);
	 */    
	
	
}
