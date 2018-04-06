import java.util.Arrays;

public class Driver {

	public static void main(String[] args) {
		SimulatorMatt sim = new SimulatorMatt(3, true); // create a new bank simulator

		sim.processEvents(); // calls the process events method that processes all events in the priority
		// queue

		for (Teller t : sim.getTellerList()) {
			System.out.println(t.toString());
		}

		System.out.println(sim.getDriveThroughQueue());

	}

}
