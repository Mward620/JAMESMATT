import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class SimulatorMatt {

	private PriorityQueue<Event> eventQueue; // All of the events in the bank (people entering and people leaving)
	private int numTellers; // The number of tellers working at the bank
	private boolean driveThroughExists; // weather or not the drive through exists
	private ArrayList<Teller> tellerList; // an ArrayList of tellers that will contain the user specified number of
											// tellers
	private Queue<Event> driveThroughQueue; // Queue that holds the line for the drive through
	private boolean tellerAtDriveThroughFlag; // flag saying if a teller is currently at the drive through
	private long worldTime; // holds the current time of the world
	private int customersHelped; // The total number of customers that have been helped
	private long globalCustomerWaitTime; // The total time customers have been waiting

	public SimulatorMatt(int numTellers, boolean driveThrough) {

		if (numTellers < 1) {
			throw new IllegalArgumentException(getClass().getName()
					+ " constructor through an exception. Num tellers should be greater than 0, it was " + numTellers);
		}

		this.eventQueue = LoadData.loadData(); // get a priority queue of all customers from LoadData
		this.numTellers = numTellers;
		this.driveThroughExists = driveThrough;
		this.tellerList = new ArrayList<Teller>(); // Initialize teller list
		arrayInitalizer(); // Add tellers to the teller list ArrayList
		this.tellerAtDriveThroughFlag = false; // initially there is no teller at the drive through
		this.driveThroughQueue = new LinkedList<Event>();
		this.globalCustomerWaitTime = 0;
	}

	/**
	 * arrayInitalizer private helper method populates the ArrayList tellerList with
	 * the number of tellers the user specified should exist
	 */
	private void arrayInitalizer() {
		for (int i = 0; i < getNumTellers(); i++) {
			tellerList.add(new Teller());
		}
	}

	/**
	 * private helper method that finds the teller with the shortest queue
	 * 
	 * @return the teller with the shortest queue
	 */
	public Teller tellerWithShortestQueue() {

		for (Teller e : getTellerList()) {

			if (e.getTellerEventQueue().isEmpty() && e.getCurrentEvent() == null) {

				return e;
			}
		}
		return Collections.min(getTellerList());
	}

	/**
	 * Private helper method that checks if the drive through queue is empty
	 * 
	 * @return boolean
	 */
	public boolean personInDrivethrough() {
		return !getDriveThroughQueue().isEmpty();
	}

	/**
	 * private helper method that gets the next event in the queue
	 * 
	 * @return next event
	 */
	public Event getNextEvent() {
		return eventQueue.poll();
	}

	/**
	 * Private helper method that updates the total time customers have been waiting
	 * 
	 * @param timeCustomerArived
	 */
	private void updateWorldCustomerWaitTime(long timeCustomerArived) {
		// total customer wait time + the world time - the time the customer entered the
		// bank
		setGlobalCustomerWaitTime(getGlobalCustomerWaitTime() + (getWorldTime() - timeCustomerArived));
	}

	/**
	 * private helper method that created removal event and adds it to the priority
	 * queue
	 * 
	 * @param removalEventTime
	 *            the time the removal event is supposed to happen
	 * 
	 * @param walkInEvent
	 *            if the event is walk in (true) or drive up (false)
	 */
	private void createRemovalEvent(Teller teller, int removalEventTime, boolean walkInEvent) {

		getEventQueue().offer(new Event(removalEventTime, (((getTellerList().indexOf(teller)) + 1) * -1), walkInEvent));
	}

	private void addEventToTeller(Teller tellerToUpdate, Event currentProcessEvent) {
		// get event off the queue and make it the tellers current event
		tellerToUpdate.setCurrentEvent(tellerToUpdate.getTellerEventQueue().poll());

		// Create a removal event for this event
		createRemovalEvent(tellerToUpdate, (int) getWorldTime() + currentProcessEvent.getEventType(),
				currentProcessEvent.isWalkInEvent());

		// Update the global customer waiting time
		updateWorldCustomerWaitTime(currentProcessEvent.getEventTime());

		// have the teller update it's idle time
		tellerToUpdate.updateTotalIdleTime(getWorldTime());
	}

	/**
	 * THIS IS THE METHOD THAT PROCESSES THROUGH ALL THE EVENTS
	 * 
	 * this is currently being built with the existence of a drive through
	 */
	public void processEvents() {
		Event currentProcessEvent; // current event to be processed
		worldTime = 0; // world time starts at 0

		// Loop though all the tellers and start them counting idle time, they should
		// all be empty
		for (Teller t : tellerList) {
			t.setTempIdleTime(worldTime);
		}

		// Loop while there are events in the priority queue
		while (!eventQueue.isEmpty()) {
			currentProcessEvent = getNextEvent(); // get the next event from the priority queue of events

			// Set the world time to the time of the current event
			setWorldTime(currentProcessEvent.getEventTime());

			// If currentProcessEvent is an arrival event
			if (!currentProcessEvent.isRemovalEvent()) {

				// if currentProcessEvent is a walk in event
				if (currentProcessEvent.isWalkInEvent()) {

					// Get the teller with the shortest line
					Teller shortestLineTeller = tellerWithShortestQueue();

					// put the currentProcessEvent on the shortestLineTeller's queue
					shortestLineTeller.getTellerEventQueue().offer(currentProcessEvent);

					// if the teller the event was added to's curentEvent is null (not with anyone)
					// make that event the teller's current event. Also stop counting idle time and
					// update the worlds customer wait times
					if (shortestLineTeller.getCurrentEvent() == null) {

						// get event off the queue and make it the tellers current event
						shortestLineTeller.setCurrentEvent(shortestLineTeller.getTellerEventQueue().poll());

						// Increment the number of customers teller has helped
						shortestLineTeller.setCustomersHelped(shortestLineTeller.getCustomersHelped() + 1);

						// Create a removal event for this event
						createRemovalEvent(shortestLineTeller,
								(int) getWorldTime() + currentProcessEvent.getEventType(),
								currentProcessEvent.isWalkInEvent());

						// Update the global customer waiting time
						updateWorldCustomerWaitTime(currentProcessEvent.getEventTime());

						// have the teller update it's idle time
						shortestLineTeller.updateTotalIdleTime(getWorldTime());
					}
				}
				// Else if the currentProcessEvent is a drive up event
				else if (!currentProcessEvent.isWalkInEvent()) {

					// Add the event to the drive through queue
					getDriveThroughQueue().offer(currentProcessEvent);
				}

			}
			// else if currentProcessEvent is a removal event
			else if (currentProcessEvent.isRemovalEvent()) {

				if (!currentProcessEvent.isWalkInEvent()) {
					System.out.println(currentProcessEvent);
				}

				// Teller that corresponds to the removal event
				Teller tellerToRemoveFrom = getTellerList().get(currentProcessEvent.getRemovalTellerIndex());

				// If the removal event is a walk in event
				if (currentProcessEvent.isWalkInEvent()) {

					if (!getDriveThroughQueue().isEmpty()) {
						tellerToRemoveFrom.setCurrentEvent(getDriveThroughQueue().poll());

						// Increment the number of customers teller has helped
						tellerToRemoveFrom.setCustomersHelped(tellerToRemoveFrom.getCustomersHelped() + 1);

						// Create a removal event for this event
						createRemovalEvent(tellerToRemoveFrom,
								(int) getWorldTime() + currentProcessEvent.getEventType(),
								currentProcessEvent.isWalkInEvent());

						// Update the global customer waiting time
						updateWorldCustomerWaitTime(currentProcessEvent.getEventTime());
					}

					// If the tellers line is empty
					else if (tellerToRemoveFrom.getTellerEventQueue().isEmpty()) {

						// Set curentEvent to null
						tellerToRemoveFrom.setCurrentEvent(null);

						// Start counting idle time
						tellerToRemoveFrom.setTempIdleTime(getWorldTime());

					}
					// If the tellers line is not empty
					else {

						// Set the current event to the next event in the Tellers queue
						tellerToRemoveFrom.setCurrentEvent(tellerToRemoveFrom.getTellerEventQueue().poll());

						// Increment the number of customers teller has helped
						tellerToRemoveFrom.setCustomersHelped(tellerToRemoveFrom.getCustomersHelped() + 1);

						// Create a removal event for this event
						createRemovalEvent(tellerToRemoveFrom,
								(int) getWorldTime() + currentProcessEvent.getEventType(),
								currentProcessEvent.isWalkInEvent());

						// Update the global customer waiting time
						updateWorldCustomerWaitTime(currentProcessEvent.getEventTime());
					}
				}
				// Else if the removal event is a drive through removal
				else {

					// If their are cars in the drive through queue make the tellers current event
					// that event
					System.out.println("PreRan");
					if (!getDriveThroughQueue().isEmpty()) {
						System.out.println("Ran");
						tellerToRemoveFrom.setCurrentEvent(getDriveThroughQueue().poll());

						// Increment the number of customers teller has helped
						tellerToRemoveFrom.setCustomersHelped(tellerToRemoveFrom.getCustomersHelped() + 1);

						// Create a removal event for this event
						createRemovalEvent(tellerToRemoveFrom,
								(int) getWorldTime() + currentProcessEvent.getEventType(),
								currentProcessEvent.isWalkInEvent());

						// Update the global customer waiting time
						updateWorldCustomerWaitTime(currentProcessEvent.getEventTime());

						setTellerAtDriveThroughFlag(true);
					}
					// Else replace the currentEvent with the next event in the tellers queue
					else {
						// If the tellers line is empty
						if (tellerToRemoveFrom.getTellerEventQueue().isEmpty()) {

							// Set curentEvent to null
							tellerToRemoveFrom.setCurrentEvent(null);

							// Start counting idle time
							tellerToRemoveFrom.setTempIdleTime(getWorldTime());

						}
						// If the tellers line is not empty
						else {

							// Set the current event to the next event in the Tellers queue
							tellerToRemoveFrom.setCurrentEvent(tellerToRemoveFrom.getTellerEventQueue().poll());

							// Increment the number of customers teller has helped
							tellerToRemoveFrom.setCustomersHelped(tellerToRemoveFrom.getCustomersHelped() + 1);

							// Create a removal event for this event
							createRemovalEvent(tellerToRemoveFrom,
									(int) getWorldTime() + currentProcessEvent.getEventType(),
									currentProcessEvent.isWalkInEvent());

							// Update the global customer waiting time
							updateWorldCustomerWaitTime(currentProcessEvent.getEventTime());
						}
						setTellerAtDriveThroughFlag(false);
					}
				}
			}
		}
	}

	/////////////// Setters and getters//////////////
	public PriorityQueue<Event> getEventQueue() {
		return eventQueue;
	}

	public int getNumTellers() {
		return numTellers;
	}

	public boolean isDriveThroughExists() {
		return driveThroughExists;
	}

	public ArrayList<Teller> getTellerList() {
		return tellerList;
	}

	public Queue<Event> getDriveThroughQueue() {
		return driveThroughQueue;
	}

	public boolean isTellerAtDriveThroughFlag() {
		return tellerAtDriveThroughFlag;
	}

	public void setTellerAtDriveThroughFlag(boolean tellerAtDriveThroughFlag) {
		this.tellerAtDriveThroughFlag = tellerAtDriveThroughFlag;
	}

	public long getWorldTime() {
		return worldTime;
	}

	public void setWorldTime(long worldTime) {
		this.worldTime = worldTime;
	}

	public int getCustomersHelped() {
		return customersHelped;
	}

	public void setCustomersHelped(int customersHelped) {
		this.customersHelped = customersHelped;
	}

	public long getGlobalCustomerWaitTime() {
		return globalCustomerWaitTime;
	}

	public void setGlobalCustomerWaitTime(long globalCustomerWaitTime) {
		this.globalCustomerWaitTime = globalCustomerWaitTime;
	}

}
