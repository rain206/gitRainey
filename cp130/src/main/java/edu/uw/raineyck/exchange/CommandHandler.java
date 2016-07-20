package edu.uw.raineyck.exchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.exchange.StockExchange;
import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.MarketBuyOrder;
import edu.uw.ext.framework.order.MarketSellOrder;
import edu.uw.ext.framework.order.Order;

/**
 * An instance of this class is dedicated to executing commands received form
 * clients
 * 
 * @author craigrainey
 *
 */
public class CommandHandler implements Runnable {

	/** socket */
	private Socket connectedSocket;

	/** Exchange used */
	private StockExchange exchange;

	/** Print Writer */
	private PrintWriter printWtr;

	/** Buffered Reader */
	private BufferedReader reader;

	/** Logger */
	private Logger log = LoggerFactory.getLogger(CommandHandler.class);

	/** Constructor */
	public CommandHandler() {
	}

	public CommandHandler(Socket socket, StockExchange realExchange) {
		this.connectedSocket = socket;
		this.exchange = realExchange;

		try {
			printWtr = new PrintWriter(connectedSocket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(connectedSocket.getInputStream()));

			log.info("Successfully connected to Proxy");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Process commands In example, instance of commandhandler receives command
	 * to get a quote for a certain stock symbol.
	 */
	public void run() {
		String inputLine = null;
		try {
			while ((inputLine = reader.readLine()) != null) {
				String[] cmdElements = inputLine.trim().split(ProtocolConstants.ELEMENT_DELIMITER);

				switch (cmdElements[ProtocolConstants.CMD_EVNT]) {
				case ProtocolConstants.GET_TICKERS_CMD:
					String[] tickerList = exchange.getTickers();

					StringBuilder tickerBuilder = new StringBuilder();
					for (int i = 0; i < tickerList.length; i++) {
						tickerBuilder.append(tickerList[i]);
						if ((i + 1) != tickerList.length) {
							tickerBuilder.append(ProtocolConstants.ELEMENT_DELIMITER);
						}
					}

					printWtr.println(tickerBuilder.toString());
					break;
				case ProtocolConstants.GET_QUOTE_CMD:
					String symbol = cmdElements[ProtocolConstants.QUOTE_CMD_TICKER_ELEMENT];
					StockQuote quote = exchange.getQuote(symbol);
					if (quote == null) {
						printWtr.println(-1);
						break;
					}
					int quotePrice = quote.getPrice();

					printWtr.println(quotePrice);
					break;
				case ProtocolConstants.EXECUTE_TRADE_CMD:
					String orderCmd = cmdElements[ProtocolConstants.EXECUTE_TRADE_TYPE_ELEMENT];
					String accountId = cmdElements[ProtocolConstants.EXECUTE_TRADE_CMD_ACCOUNT_ELEMENT];
					String tickerSymbol = cmdElements[ProtocolConstants.EXECUTE_TRADE_CMD_TICKER_ELEMENT];
					int shares = Integer.parseInt(cmdElements[ProtocolConstants.EXECUTE_TRADE_CMD_SHARES_ELEMENT]);
					int price = exchange.getQuote(tickerSymbol).getPrice();
					int executionPrice = shares * price;

					Order order = null;
					if (orderCmd.equals(ProtocolConstants.BUY_ORDER)) {
						order = new MarketBuyOrder(accountId, shares, tickerSymbol);
					} else {
						order = new MarketSellOrder(accountId, shares, tickerSymbol);
					}

					exchange.executeTrade(order);
					printWtr.println(executionPrice);
					break;

				case ProtocolConstants.GET_STATE_CMD:
					printWtr.println(exchange.isOpen());
					break;
				default:
					log.info("Could not handle command: " + cmdElements[ProtocolConstants.CMD_EVNT]);
					break;
				}
			}
		} catch (IOException ex) {
			log.warn("Error receiving//sending commands from command handler");
		} finally {
			try {
				if (connectedSocket != null) {
					connectedSocket.close();
				}
			} catch (IOException i) {
				log.warn("Error closing socket from Command Handler");
				i.printStackTrace();
			}
		}
	}
}
