package app;


import java.util.ArrayList;
import java.util.List;


import com.scg.domain.ClientAccount;
import com.scg.domain.Consultant;
import com.scg.domain.TimeCard;
import com.scg.net.server.CommandProcessor;
import com.scg.net.server.InvoiceServer;
import com.scg.util.ListFactory;

/**
 * The server application for assignment 08
 * 
 * @author craigrainey
 *
 */
public class Assignment09Server {

	/**	Port */
	public static final int DEFAULT_PORT = 10888;
	
	/** List of consultants */
	private static List<Consultant> consultants = new ArrayList<>();
	
	/** List of ClientAccounts */
	private static List<ClientAccount> clientAccounts = new ArrayList<>();
	
	/** List of Time Cards */
	private static List<TimeCard> timeCards = new ArrayList<>();

	
	/**
	 * Instantiates an InvoiceServer, initializes its account and consultant lists and starts it.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ListFactory.populateLists(clientAccounts, consultants, timeCards);
		InvoiceServer server = new InvoiceServer(DEFAULT_PORT, clientAccounts, consultants);
		server.run();
	}

}
