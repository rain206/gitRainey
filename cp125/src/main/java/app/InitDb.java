package app;


import java.util.ArrayList;
import java.util.List;
import com.scg.domain.ClientAccount;
import com.scg.domain.Consultant;
import com.scg.domain.TimeCard;
import com.scg.persistent.DbServer;
import com.scg.util.ListFactory;

/**
 * Creates a consultants, clients, and timecards to then serialize the client and timecard
 * lists into files
 * @author craigrainey
 */
public final class InitDb {
	
	/** String for the username	 */
	private final static String username = "student";
	
	/** String for the password	 */
	private final static String password = "student";
	
	/** String for the db URL */
	private final static String dbUrl = "jdbc:derby://localhost:1527/memory:scgDb";

	/** private constructor so this cannot be instantiated	 */
	protected InitDb() {
	}
	

	/**
	 * Creates and populates timeCards, consultants, and clients arrays. Serializes client
	 * and timeCard lists into respective files
	 *  
	 * @param args
	 */
	public static void main (String[] args ) throws Exception {

		/* Create and populate list using ListFactory */
	    List<TimeCard> timeCards = new ArrayList<>();
	    List<Consultant> consultants = new ArrayList<>();
	    List<ClientAccount> clients = new ArrayList<>();
	    ListFactory.populateLists(clients, consultants, timeCards);
	    
	    DbServer scgDB = new DbServer(dbUrl, username, password);
	    
	    //Add clients to server
	    for (ClientAccount client : clients) {
	    	scgDB.addClient(client);
	    }
	    
	    for (Consultant consult : consultants) {
	    	scgDB.addConsultant(consult);
	    }
	    
	    for (TimeCard cards : timeCards) {
	    	scgDB.addTimeCard(cards);
	    }
	    
	    
	    
	    

	    
//	    /*	 Serialize the time card list */
//	    try {
//	    	FileOutputStream fileOutTime = new FileOutputStream("TimeCardList.ser");
//	    	ObjectOutputStream out = new ObjectOutputStream(fileOutTime);
//	    	out.writeObject(timeCards);
//	    	System.out.printf("Serialized date is saved in TimeCardList.ser");
//	    	out.close();
//	    	fileOutTime.close();
//
//	    } catch (IOException e) {
//	    	e.printStackTrace();
//	    }
//	    
//	    /* Serialize the client list  */
//	    try {
//	    	FileOutputStream fileOutClient = new FileOutputStream("ClientList.ser");
//	    	ObjectOutputStream out = new ObjectOutputStream(fileOutClient);
//	    	out.writeObject(clients);
//	    	System.out.printf("Serialized date is saved in ClientList.ser");
//	    	out.close();
//	    	fileOutClient.close();
//	    } catch (IOException e) {
//	    	e.printStackTrace();
//	    }
	}
}
