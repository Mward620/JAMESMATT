
/**
 * 
 * @author James Allender, Matt Ward, CISC 231 Prof. Sawin, Assignment 4,
 *         4/4/18, Event Class
 * 
 *         An event has an eventTime which is the time the event should happen,
 *         a eventType which when positive is the transaction time for a
 *         customer and when negative is the number of the teller the customer
 *         Which should be removed is at, positive eventTypes are customers
 *         coming into the bank, negative eventTypes are customers leaving the
 *         bank, and a boolean driveThrough which is if the customer is in the
 *         drive through or not
 *
 */

public class Event implements Comparable<Event> {

	private int eventTime; // Time event happens
	private int eventType; // is the transaction time for a customer and when negative is the number of the
							// teller the customer Which should be removed is at, positive eventTypes are
							// customers coming into the bank
	private boolean walkInEvent; // is this event in the drive through
	private boolean isRemovalEvent; // if this even is a removal event;

	/**
	 * Constructor creates a new event object.
	 * 
	 * @param eventTime
	 * @param eventType
	 * @param driveThrough
	 */
	public Event(int eventTime, int eventType, boolean walkInEvent) {
		this.eventTime = eventTime;
		this.eventType = eventType;
		this.walkInEvent = walkInEvent;
		this.isRemovalEvent = (eventType < 0); // if eventType < 0 then true, this is a removal event
	}

	/**
	 * Method returns the index in the ArrayList the teller that event is supposed
	 * to occur at if the event is a removal event or -1 if the even is not a
	 * removal event
	 * 
	 * @return the ArrayList index of the teller
	 */
	public int getRemovalTellerIndex() {
		// if the eventType is negative meaning it is a removal event
		if (eventType < 0) {
			// invert event type to positive and subtract 1 to get the index of the
			// ArrayList its teller is at
			return ((eventType * -1) - 1);
		}
		// if the eventType is positive (not a removal event)
		else {
			return -1;
		}
	}

	@Override
	/**
	 * Overridden compareTo method that compares Events against each other based on
	 * their event time
	 */
	public int compareTo(Event event) {
		return Integer.compare(this.eventTime, event.eventTime);
	}

	/**
	 * eventTime accessor
	 * 
	 * @return eventTime
	 */
	public int getEventTime() {
		return eventTime;
	}

	/**
	 * eventType accessor
	 * 
	 * @return eventType
	 */
	public int getEventType() {
		return eventType;
	}

	/**
	 * removalEvent accessor
	 * 
	 * @return removalEvent
	 */
	public boolean isRemovalEvent() {
		return isRemovalEvent;
	}

	/**
	 * walkInEvent accessor
	 * 
	 * @return driveThrough
	 */
	public boolean isWalkInEvent() {
		return walkInEvent;
	}

	@Override
	/**
	 * To string to see the state of an event
	 */
	public String toString() {
		return ("Event Time: " + eventTime + " Event Type: " + eventType + " Walk In: " + walkInEvent);
	}
}
