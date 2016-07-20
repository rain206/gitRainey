package edu.uw.raineyck.exchange;

/**
 * Contants for the command strings composing the exchange protocol. The
 * protocol supports events and commands.
 * 
 * Events are one way messages sent from the exchange to the broker(s).
 * 
 * The protocol supports the following events: Event: [OPEN_EVNT] --- Event:
 * [CLOSED_EVNT] --- Event: [PRICE_CHANGE_EVNT][ELEMENT_DELIMITER]
 * symbol[ELEMENT_DELIMITER]price
 * 
 * Commands conform to a request/response model where request are sent from a
 * broker and the result is a response sent to the requesting broker from the
 * exchange.
 * 
 * The protocol supports the following commands: Request: [GET_STATE_CMD]
 * Response: [OPEN_STATE] | [CLOSED_STATE] --- Request:
 * [GET_QUOTE_CMD][ELEMENT_DELIMITER]symbol Response: price --- Request:
 * [EXECUTE_TRADE_CMD][ELEMENT_DELIMITER][BUY_ORDER] | [SELL_ORDER]
 * [ELEMENT_DELIMITER]account_id[ELEMENT_DELIMTER]symbol[ELEMENT_DELIMITER]
 * shares Response: execution_price
 * 
 * 
 * @author craigrainey
 *
 */
public abstract class ProtocolConstants {

	/** Identifies an order as a buy order */
	public final static String BUY_ORDER = "BUY_ORDER";

	/** The exchange has closed */
	public final static String CLOSED_EVNT = "CLOSED_EVNT";

	/** Indicates the exchange is closed */
	public final static String CLOSED_STATE = "CLOSED";

	/** The index of the command element */
	public static final int CMD_EVNT = 0;

	/** The maximum number of command elements used in the protocol */
	public final static String ELEMENT_DELIMITER = ":";

	/** Character encoding to use */
	public final static String ENCODING = "UTF-8";

	/** A request to execute a trade */
	public final static String EXECUTE_TRADE_CMD = "EXECUTE_TRADE_CMD";

	/** The index of the account id element in the execute trade command */
	public final static int EXECUTE_TRADE_CMD_ACCOUNT_ELEMENT = 2;

	/** The index of the shares element in the execute trade command */
	public static int EXECUTE_TRADE_CMD_SHARES_ELEMENT = 4;

	/** The index of the ticker element in the execute trade command */
	public static int EXECUTE_TRADE_CMD_TICKER_ELEMENT = 3;

	/** The index of the order type element in the execute trade command */
	public static int EXECUTE_TRADE_TYPE_ELEMENT = 1;

	/** A request for a stock price quote */
	public final static String GET_QUOTE_CMD = "GET_QUOTE_CMD";

	/** A request for the exchange's rate */
	public final static String GET_STATE_CMD = "GET_STATE_CMD";

	/** A request for the ticker symbol for the traded stocks */
	public final static String GET_TICKERS_CMD = "GET_TICKER_CMD";

	/** The invalid stock price - indicates stock is not on the exchange */
	public final static int INVALID_STOCK = -1;

	/** The exchange is opened */
	public final static String OPEN_EVNT = "OPEN_EVNT";

	/** Indicates the exhange is open */
	public final static String OPEN_STATE = "OPEN";

	/** A stock price has changed */
	public final static String PRICE_CHANGE_EVNT = "PRICE_CHANGE_EVNT";

	/** The index of the price element */
	public final static int PRICE_CHANGE_EVNT_PRICE_ELEMENT = 2;

	/** The index of the ticker element */
	public final static int PRICE_CHANGE_EVNT_TICKER_ELEMENT = 1;

	/** The index of the ticker element in the price quote command */
	public final static int QUOTE_CMD_TICKER_ELEMENT = 1;

	/** Identifies an order as a sell order */
	public final static String SELL_ORDER = "SELL_ORDER";

	/** Constructor */
	public ProtocolConstants() {
	}
}
