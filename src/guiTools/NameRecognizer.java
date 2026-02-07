package guiTools;

public class NameRecognizer {

	public static String fullNameErrorMessage = "";		// The error message text
	public static String fullNameInput = "";			// The input being processed
	public static int fullNameIndexofError = -1;		// The index where the error was located
	private static int state = 0;						// The current state value
	private static int nextState = 0;					// The next state value
	private static boolean finalState = false;			// Is this state a final state?
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;						// The flag that specifies if the FSM is
														// running
	private static int fullNameSize = 0;				// A full name may not exceed 50 characters

	/**********
	 * This private method displays the input line and then on a line under it displays a marker
	 * at the point where an error was detected.  This method is designed to be used to
	 * display the error message on the console terminal.
	 *
	 * @param input				The input string
	 * @param currentCharNdx	The location where an error was found
	 * @return					The input line up to the error point followed by a marker
	 */

	// Private method to display debugging data
	private static void displayDebuggingInfo() {
		// Display the current state of the FSM as part of an execution trace
		if (currentCharNdx >= inputLine.length())
			// display the line with the current state numbers aligned
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state +
					((finalState) ? "       F   " : "           ") + "None");
		else
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state +
					((finalState) ? "       F   " : "           ") + "  " + currentChar + " " +
					((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") +
					nextState + "     " + fullNameSize);
	}

	// Private method to move to the next character within the limits of the input line
	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			currentChar = ' ';
			running = false;
		}
	}

	// Private method to display the input line
	private static String displayInput(String input, int currentCharNdx) {
		// Display the entire input line
		String result = input.substring(0, currentCharNdx) + "?\n";
		return result;
	}

	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
	 * method.
	 *
	 * @param input		The input string for the Finite State Machine
	 * @return			An output string that is empty if everything is okay or it is a String
	 * 						with a helpful description of the error
	 */
	public static String checkFullName(String input) {
		// Check to ensure that there is input to process
		if (input == null || input.length() <= 0) {
			fullNameIndexofError = 0;	// Error at first character
			fullNameErrorMessage = "\n*** ERROR *** The full name is empty.\n";
			return fullNameErrorMessage + displayInput(input != null ? input : "", 0);
		}

		// The local variables used to perform the Finite State Machine simulation
		state = 0;							// This is the FSM state number
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		currentChar = input.charAt(0);		// The current character from the above indexed position

		// The Finite State Machine continues until the end of the input is reached or at some
		// state the current character does not match any valid transition to a next state

		fullNameInput = input;				// Save a copy of the input
		running = true;						// Start the loop
		nextState = -1;						// There is no next state
		System.out.println("\nCurrent Final Input  Next\nState   State Char  State  Size");

		// This is the place where semantic actions for a transition to the initial state occur
		fullNameSize = 0;					// Initialize the full name size

		// Let's ensure the full name is not too long
		if (input.length() > 50) {
			fullNameErrorMessage = "A valid full name must be no more than 50 characters.\n";
			return fullNameErrorMessage + displayInput(input, 50);
		}

		// The Finite State Machine continues until the end of the input is reached or at some
		// state the current character does not match any valid transition to a next state
		while (running) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			nextState = -1;					// Default to there is no next state

			switch (state) {
			case 0:
				// State 0 has 1 valid transition. The name must start with a letter.
				// The current character is checked against A-Z, a-z. If any are matched
				// the FSM goes to state 1

				// A-Z, a-z -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z') ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z')) {	// Check for a-z
					nextState = 1;

					// Count the character
					fullNameSize++;
				}
				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				break;

			case 1:
				// State 1 is the "in name" state (last character was a letter). It has two valid
				// transitions: letter -> stay in state 1, space -> state 2.

				// A-Z, a-z -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z') ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z')) {	// Check for a-z
					nextState = 1;

					// Count the character
					fullNameSize++;
				}
				// Space -> State 2 (between words)
				else if (currentChar == ' ') {
					nextState = 2;

					// Count the space
					fullNameSize++;
				}
				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				// If the size is larger than 50, the loop must stop
				if (fullNameSize > 50)
					running = false;
				break;

			case 2:
				// State 2 deals with a character after a space (e.g., between first and middle name).
				// Letter -> state 1; space -> stay in state 2 (multiple spaces allowed).

				// A-Z, a-z -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z') ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z')) {	// Check for a-z
					nextState = 1;

					// Count the character
					fullNameSize++;
				}
				// Space -> State 2
				else if (currentChar == ' ') {
					nextState = 2;

					// Count the space
					fullNameSize++;
				}
				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				// If the size is larger than 50, the loop must stop
				if (fullNameSize > 50)
					running = false;
				break;
			}

			if (running) {
				displayDebuggingInfo();
				// When the processing of a state has finished, the FSM proceeds to the next
				// character in the input and if there is one, it fetches that character and
				// updates the currentChar.  If there is no next character the currentChar is
				// set to a blank.
				moveToNextCharacter();

				// Move to the next state
				state = nextState;

				// Is the new state a final state?  If so, signal this fact.
				finalState = (state == 1);

				// Ensure that one of the cases sets this to a valid value
				nextState = -1;
			}
			// Should the FSM get here, the loop starts again
		}
		displayDebuggingInfo();

		System.out.println("The loop has ended.");

		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// on the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states and that
		// makes it possible for this code to display a very specific error message to improve the
		// user experience.
		fullNameIndexofError = currentCharNdx;	// Set index of a possible error
		fullNameErrorMessage = "\n*** ERROR *** ";

		// The following code is a slight variation to support just console output.
		switch (state) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			fullNameErrorMessage += "Name must start with a letter (A-Z or a-z).\n";
			return fullNameErrorMessage + displayInput(input, currentCharNdx);

		case 1:
			// State 1 is a final state.  Check to see if the full name length is valid and that
			// the whole string has been consumed.

			if (fullNameSize > 50) {
				// Full name is too long
				fullNameErrorMessage += "Name must have no more than 50 characters.\n";
				return fullNameErrorMessage + displayInput(input, currentCharNdx);
			}
			else if (currentCharNdx < input.length()) {
				// There are characters remaining in the input, so an invalid character was found
				fullNameErrorMessage +=
						"Name may only contain letters and spaces. No special characters or symbols.\n";
				return fullNameErrorMessage + displayInput(input, currentCharNdx);
			}
			else {
				// Full name is valid
				fullNameIndexofError = -1;
				fullNameErrorMessage = "";
				return "";
			}

		case 2:
			// State 2 is not a final state (we ended after a space). Either trailing space or
			// invalid character.
			if (currentCharNdx >= input.length()) {
				fullNameErrorMessage += "Name cannot end with a space.\n";
				return fullNameErrorMessage + displayInput(input, currentCharNdx);
			}
			else {
				fullNameErrorMessage +=
						"Name may only contain letters and spaces. No special characters or symbols.\n";
				return fullNameErrorMessage + displayInput(input, currentCharNdx);
			}

		default:
			// This is for the case where we have a state that is outside of the valid range.
			// This should not happen
			return "";
		}
	}
}
