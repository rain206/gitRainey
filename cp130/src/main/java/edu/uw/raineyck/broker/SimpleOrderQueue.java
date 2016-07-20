package edu.uw.raineyck.broker;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import edu.uw.ext.framework.broker.DispatchFilter;
import edu.uw.ext.framework.broker.OrderProcessor;
import edu.uw.ext.framework.broker.OrderQueue;

/**
 * A simple OrderQueue implementation backed by a TreeSet
 * 
 * @author craigrainey
 *
 * @param <T>
 *            - the dispatch threshold type
 * @param <E>
 *            - the type of order contained in the queue
 */
public class SimpleOrderQueue<T, E extends edu.uw.ext.framework.order.Order> implements OrderQueue<T, E>, Runnable {

	/** Threshold */
	private T threshold;

	/** Order processor */
	private OrderProcessor orderProc;

	/** Dispatch Filter */
	private DispatchFilter<T, E> dispatchFilter;

	/** TreeSet that contains the orders */
	private TreeSet<E> orderQueue;

	/** Dispatching Thread */
	private Thread dispatchThread = new Thread(this);

	/** Queue Lock */
	private final Lock queueLock = new ReentrantLock();

	/** Queue Condition */
	private final Condition queueCondition = queueLock.newCondition();

	/** Order Processor Lock */
	private final Lock orderProcLock = new ReentrantLock();

	/**
	 * Constructor
	 * 
	 * @param threshold
	 *            - threshold for queue dispatch
	 * @param filter
	 *            - the dispatch filter used to control dispatching from this
	 *            queue
	 */
	public SimpleOrderQueue(T threshold, DispatchFilter<T, E> filter) {
		this.threshold = threshold;
		this.dispatchFilter = filter;
		this.orderQueue = new TreeSet<>();
		dispatchThread.start();
	}

	/**
	 * Constructor
	 * 
	 * @param threshold
	 *            - threshold for queue dispatch
	 * @param filter
	 *            - dispatch filter used to control dispatching from this queue
	 * @param cmp
	 *            - comparator to be used for ordering
	 */
	public SimpleOrderQueue(T threshold, DispatchFilter<T, E> filter, Comparator<E> cmp) {
		this.threshold = threshold;
		this.dispatchFilter = filter;
		this.orderQueue = new TreeSet<>(cmp);
		dispatchThread.start();
	}

	/** Adds the specified order to the queue */
	@Override
	public void enqueue(E order) {
		queueLock.lock();
		try {
			orderQueue.add(order);
			dispatchOrders();
		} finally {
			queueLock.unlock();
		}
	}

	/**
	 * Removes the highest dispatchable order in the queue
	 */
	@Override
	public E dequeue() {
		queueLock.lock();

		E dequeuedOrder = null;
		try {
			if (!orderQueue.isEmpty()) {
				dequeuedOrder = orderQueue.first();
				if (dispatchFilter.checkDispatch(threshold, dequeuedOrder) == true) {
					orderQueue.remove(dequeuedOrder);
				} else {
					/*
					 * Mostly intended for dispatchOrders to stop operations if
					 * the "first" order is not dispatchable.
					 */
					dequeuedOrder = null;
				}

			}
		} finally {
			queueLock.unlock();
		}
		return dequeuedOrder;
	}

	/** Executes the orderProcessor for each dispatchable order */
	@Override
	public void dispatchOrders() {
		// E order = dequeue();
		// while (order != null) {
		// orderProc.process(order);
		// order = dequeue();
		// }
		queueLock.lock();
		try {
			queueCondition.signal();
		} finally {
			queueLock.unlock();
		}
	}

	/** Registers the callback to be used during order processing */
	@Override
	public void setOrderProcessor(OrderProcessor proc) {
		orderProcLock.lock();
		try {
			orderProc = proc;
		} finally {
			orderProcLock.unlock();
		}
	}

	/** Adjust the threshold and dispatches orders */
	@Override
	public void setThreshold(T newThreshold) {
		threshold = newThreshold;
		dispatchOrders();
	}

	/**
	 * Gets the threshold
	 */
	@Override
	public T getThreshold() {
		return threshold;
	}

	@Override
	public void run() {
		while (true) {
			String threadName = Thread.currentThread().getName();
			try {
				E order;
				queueLock.lock();
				try {
					order = dequeue();
					if (order == null) {
						queueCondition.await();
					}
				} finally {
					queueLock.unlock();
				}

				orderProcLock.lock();
				try {
					if (order != null) {
						orderProc.process(order);
						System.out.println(
								String.format("Processed order %s on thread %s", order.getAccountId(), threadName));
					}
				} finally {
					orderProcLock.unlock();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
