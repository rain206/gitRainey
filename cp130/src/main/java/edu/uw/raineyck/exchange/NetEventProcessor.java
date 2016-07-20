package edu.uw.raineyck.exchange;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.exchange.ExchangeEvent;
import edu.uw.ext.framework.exchange.ExchangeListener;

/**
 * Listens for (by joining the multicast group) and processes events received
 * from the exchange. Processing the events consists of propagating them to
 * registered listeners
 * 
 * @author craigrainey
 *
 */
public class NetEventProcessor implements Runnable {

	/** Byte size for buffer */
	private final int MAX_BYTES = 1024;

	/** Event Port */
	private int eventPort;

	/** Inet Address */
	private InetAddress address;

	/**
	 * Buffer of bytes that is used to store incoming bytes containing
	 * information from the server
	 */
	private byte[] buffer = new byte[MAX_BYTES];

	/** Event Listener List */
	private EventListenerList listenerList = new EventListenerList();

	/** Logger */
	private Logger log = LoggerFactory.getLogger(NetEventProcessor.class);

	/** Simple Constructor */
	public NetEventProcessor() {
	}

	/** Constructor */
	public NetEventProcessor(String eventIPAddress, int eventPort) {
		this.eventPort = eventPort;

		try {
			/*
			 * Gets the address we are connecting to
			 */
			address = InetAddress.getByName(eventIPAddress);
		} catch (UnknownHostException u) {
			log.warn("Uknown Host Exception from Net Event Processor");
			u.printStackTrace();
		}
	}

	/** Adds a market listener */
	public void addExchangeListener(ExchangeListener newListener) {
		listenerList.add(ExchangeListener.class, newListener);
	}

	/** Removes a market listener */
	public void removeExchangeListener(ExchangeListener removeListener) {
		listenerList.add(ExchangeListener.class, removeListener);
	}

	private void fireExchangeEvent(ExchangeEvent evnt) {
		ExchangeListener[] listeners;
		listeners = listenerList.getListeners(ExchangeListener.class);

		for (ExchangeListener listener : listeners) {
			switch (evnt.getEventType()) {
			case OPENED:
				listener.exchangeOpened(evnt);
				break;
			case CLOSED:
				listener.exchangeClosed(evnt);
				break;
			case PRICE_CHANGED:
				listener.priceChanged(evnt);
				break;
			default:
				log.warn("Error attempting to fire unknown event type: " + evnt.getEventType());
				break;
			}
			listener.exchangeOpened(evnt);
		}
	}

	/** Continuously accepts and processes market and price change events */
	@Override
	public void run() {
		try (MulticastSocket clientSocket = new MulticastSocket(eventPort)) {
			clientSocket.joinGroup(address);
			log.info("Net Event Processor joined multicast group");
			DatagramPacket msgPacket = new DatagramPacket(buffer, buffer.length);

			while (true) {
				clientSocket.receive(msgPacket);

				/*
				 * Receives the multi-cast message from the network adapter and
				 * processes the event
				 */
				String message = new String(buffer, 0, buffer.length).trim();
				if (message.equals(ProtocolConstants.OPEN_EVNT)) {
					log.info("Real exchange has opened!");
					ExchangeEvent openEvnt = ExchangeEvent.newOpenedEvent(this);
					fireExchangeEvent(openEvnt);
				} else if (message.equals(ProtocolConstants.CLOSED_EVNT)) {
					log.info("Real exchange has closed!");
					ExchangeEvent closedEvnt = ExchangeEvent.newClosedEvent(this);
					fireExchangeEvent(closedEvnt);
				} else {
					String[] priceChgElement = message.split(ProtocolConstants.ELEMENT_DELIMITER);

					String display = String.format("%-20s %9s %-5s %s %s", priceChgElement[0], "Symbol:",
							priceChgElement[1], "New Price:", priceChgElement[2]);
					log.info(display);

					String ticker = priceChgElement[ProtocolConstants.PRICE_CHANGE_EVNT_TICKER_ELEMENT];
					int stockPrice = Integer
							.parseInt(priceChgElement[ProtocolConstants.PRICE_CHANGE_EVNT_PRICE_ELEMENT]);

					ExchangeEvent priceChangeEvnt = ExchangeEvent.newPriceChangedEvent(this, ticker, stockPrice);
					fireExchangeEvent(priceChangeEvnt);
				}

			}
		} catch (IOException e) {
			log.warn("Error running Net Event Processor");
			e.printStackTrace();
		}
	}

}
