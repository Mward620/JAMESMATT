import java.io.*;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.swing.*;

/**
 * @author Prof. Jarvis
 * @editors James Allender, Matt Ward CISC 231 Prof. Sawin, Assignment 4,
 *          4/4/18, LoadData Class
 * 
 *          Present a file-selection dialog box, and allow the user to select a
 *          file to use as input. that file is processed into a priority queue
 *          by the loadData method
 */

public class LoadData {

	/**
	 * loadData method presents the user with a JFileChooser dialog box and takes
	 * the selected file and loops through all the lines in that file and creates an
	 * Event object for that line and adds that line to a priority queue and then
	 * returns that priority queue
	 * 
	 * @return Priority queue eventQueue
	 */
	public static PriorityQueue<Event> loadData() {
		/// UI STUFF/////////////////////////////
		int buttonPressed;
		JFileChooser fileChooser;
		File selectedFile;
		PriorityQueue<Event> eventQueue = new PriorityQueue<Event>();

		selectedFile = null;

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} //
		catch (Exception E) {
			System.out.println("UI manager look could not be set");
		}

		fileChooser = new JFileChooser();
		buttonPressed = fileChooser.showOpenDialog(null);
		if (buttonPressed == JFileChooser.APPROVE_OPTION) // changed to APPROVE_OPTION from APPROVE_BUTTON)
		{
			selectedFile = fileChooser.getSelectedFile();
		}
		////////////////////////////////////////

		try {
			// Create Scanner
			Scanner fileScanner = new Scanner(selectedFile);

			// Loop while there are lines in the file
			while (fileScanner.hasNext()) {
				// Create a new event for the line and add it to the priority queue
				eventQueue.offer(new Event(fileScanner.nextInt(), fileScanner.nextInt(), fileScanner.nextBoolean()));
			}

			// Close the scanner
			fileScanner.close();

		} catch (FileNotFoundException e) {
			System.out.println("File Was Not found");
			e.printStackTrace();
		}

		// Return the priority queue
		return eventQueue;
	}
}
