package edu.uw.raineyck.exchange;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.exchange.StockExchange;

/**
 * Accepts command request and dispatches them to a CommandHandler
 * 
 * @author craigrainey
 *
 */
public class CommandListener implements Runnable {

	/** Command Port to listen for connections on */
	private int commandPort;

	/** Stock Exchange to be used to execute the commands */
	private StockExchange realExchange;

	private volatile boolean listening = true;

	/** Server Socket */
	private ServerSocket serverSocket;

	/** Executor to launch new threads */
	private ExecutorService executor = Executors.newCachedThreadPool();

	/** Logger */
	private Logger log = LoggerFactory.getLogger(CommandListener.class);

	/** Constructor */
	public CommandListener(int commandPort, StockExchange thisExchange) {
		this.commandPort = commandPort;
		this.realExchange = thisExchange;
	}

	/**
	 * Accept connections and create a CommandExecutor for dispatching the
	 * command
	 */
	public void run() {
		Socket client = null;
		try {
			serverSocket = new ServerSocket(commandPort);
			log.info("Server ready on port: " + commandPort);

			while (listening) {

				client = serverSocket.accept();
				CommandHandler cmdHandler = new CommandHandler(client, realExchange);
				executor.execute(cmdHandler);

				if (client == null) {
					continue;
				}
			}
		} catch (IOException ex) {
			log.info("Error connecting command listener to network proxy");
			ex.printStackTrace();
		} finally {
			terminate();
		}
	}

	/**
	 * Terminates this thread gracefully
	 */
	public void terminate() {
		listening = false;
	}
}
