import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Teller implements Comparable<Teller> {

	private Queue<Event> tellerEventQueue; // queue that hold the line of the teller
	private Event currentEvent; // the event that is currently occupying the teller, null if they are not
								// processing an event
	private long totalIdleTime; // how long a teller has been idle
	private long tempIdleTime; // temp variable used to calculate the amount of time a teller has been idle
	private int customersHelped; // The total number of customers that have been helped

	//mat has a face is

	public Teller() {
		tellerEventQueue = new LinkedList<Event>(); // create a new empty queue to hold the events the teller is waiting
													// in
		currentEvent = null; // at creation the teller is not handling an event
		this.totalIdleTime = 0; // Initial idle time for a teller should be 0
	}

	@Override
	/**
	 * Overridden compareTo method that compares Tellers based on their queue size
	 */
	public int compareTo(Teller otherTeller) {
		return Integer.compare(this.getTellerEventQueue().size(), otherTeller.getTellerEventQueue().size());
	}

	/**
	 * Private helper method that updates the total idle time
	 */
	public void updateTotalIdleTime(long currentWorldTime) {
		totalIdleTime += (currentWorldTime - tempIdleTime); // update total idle time
	}

	//////////// Accessors and mutators/////////
	/**
	 * tellerEventQueue accessor
	 *
	 * @return tellerEventQueue
	 */
	public Queue<Event> getTellerEventQueue() {
		return tellerEventQueue;
	}

	/**
	 * currentEvent accessor
	 *
	 * @return currentEvent
	 */
	public Event getCurrentEvent() {
		return currentEvent;
	}

	/**
	 * currentEvent mutator
	 *
	 * @param currentEvent
	 */
	public void setCurrentEvent(Event currentEvent) {
		this.currentEvent = currentEvent;
	}

	/**
	 * idleTime accessor
	 *
	 * @return idleTime
	 */
	public long getIdleTime() {
		return totalIdleTime;
	}

	/**
	 * idleTime mutator
	 *
	 * @param idleTime
	 */
	public void setIdleTime(long totalIdleTime) {
		this.totalIdleTime = totalIdleTime;
	}

	/**
	 * tempIdleTime accessor
	 *
	 * @return tempIdleTime
	 */
	public long getTempIdleTime() {
		return tempIdleTime;
	}

	/**
	 * tempIdleTime mutator
	 *
	 * @param idleTime
	 */
	public void setTempIdleTime(long tempIdleTime) {
		this.tempIdleTime = tempIdleTime;
	}

	public int getCustomersHelped() {
		return customersHelped;
	}

	public void setCustomersHelped(int customersHelped) {
		this.customersHelped = customersHelped;
	}

	@Override
	/**
	 * To string to see the state of a teller
	 */
	public String toString() {
		return ("tellerEventQueue size: " + getTellerEventQueue().size() + " Current Event: " + getCurrentEvent()
				+ " Idle Time: " + totalIdleTime + "\nTeller Queue:" + Arrays.toString(getTellerEventQueue().toArray())
				+ "\n");
	}

}
