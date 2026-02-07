package guiTools;


public class EmailAddressRecognizer {
	/**
	 * <p> Title: FSM-translated EmailAddressRecognizer. </p>
	 * 
	 * <p> Description: A demonstration of the mechanical translation of Finite State Machine 
	 * diagram into an executable Java program using the Email Address Recognizer. The code 
	 * detailed design is based on a while loop with a select list</p>
	 * 
	 * <p> Copyright: Lynn Robert Carter © 2022 </p>
	 * 
	 * @author Lynn Robert Carter
	 * 
	 * @version 0.00		2018-02-04	Initial baseline 
	 * @version 2.00		2022-01-06	Rewritten to recognize email addresses and enhanced
	 * 										to support FSM with up through 999 states for the 
	 * 										trace output to align nicely
	 * @version 3.00		2022-03-22	Adjusted to clean up the code and resolving alignment
	 * 										issues with the design and to correct the issue
	 * 										with an empty email address
	 * 
	 */

	/**********************************************************************************************
	 * 
	 * Result attributes to be used for GUI applications where a detailed error message and a 
	 * pointer to the character of the error will enhance the user experience.
	 * 
	 */

	public static String emailAddressErrorMessage = "";	// The error message text
	public static String emailAddressInput = "";		// The input being processed
	public static int emailAddressIndexofError = -1;	// The index where the error was located
	private static int state = 0;						// The current state value
	private static int nextState = 0;					// The next state value
	private static boolean finalState = false;			// Is this state a final state?
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;						// The flag that specifies if the FSM is 
														// running
	private static int domainPartCounter = 0;			// A domain name may not exceed 63 characters

	/**********
	 * This private method display the input line and then on a line under it displays an up arrow
	 * at the point where an error should one be detected.  This method is designed to be used to 
	 * display the error message on the console terminal.
	 * 
	 * @param input				The input string
	 * @param currentCharNdx	The location where an error was found
	 * @return					Two lines, the entire input line followed by a line with an up arrow
	 */
	private static String displayInput(String input, int currentCharNdx) {
		// Display the entire input line
		String result = input.substring(0,currentCharNdx) + "?\n";

		return result;
	}


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
					nextState + "     " + domainPartCounter);
	}

	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			System.out.println("End of input was found!");
			currentChar = ' ';
			running = false;
		}
	}

	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for the Finite State Machine
	 * @return			An output string that is empty if every things is okay or it will be
	 * 						a string with a help description of the error follow by two lines
	 * 						that shows the input line follow by a line with an up arrow at the
	 *						point where the error was found.
	 */
	public static String checkEmailAddress(String input) {
		// The following are the local variable used to perform the Finite State Machine simulation
		state = 0;							// This is the FSM state number
		domainPartCounter = 0;
		finalState = false;
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		emailAddressInput = input;			// Save a copy of the input

		// Let's ensure there is input
		if (input.length() <= 0) {
			emailAddressErrorMessage = "There was no email address found.\n";
			return emailAddressErrorMessage + displayInput(input, 0);
		}
		currentChar = input.charAt(0);		// The current character from the above indexed position

		// Let's ensure the address is not too long
		//if (input.length() > 255) {
		//	emailAddressErrorMessage = "A valid email address must be no more than 255 characters.\n";
		//	return emailAddressErrorMessage + displayInput(input, 255);
		//}
		if (input.length() > 64) {
		    emailAddressErrorMessage = "A valid email address must be no more than 64 characters.\n";
		    return emailAddressErrorMessage + displayInput(input, 64);
		}

		running = true;						// Start the loop
		System.out.println("\nCurrent Final Input  Next  DomainName\nState   State Char  State  Size");

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			nextState = -1;						// Default to there is no next state		
			
			switch (state) {
			case 0: 
				// State 0 has just 1 valid transition.
				// The current character is must be checked against 62 options. If any are matched
				// the FSM must go to state 1
				// The first and the second check for an alphabet character the third a numeric
				if ((currentChar >= 'A' && currentChar <= 'Z')|| 		// Upper case
						(currentChar >= 'a' && currentChar <= 'z') ||	// Lower case
						(currentChar >= '0' && currentChar <= '9')) {	// Digit
					nextState = 1;
				}
								
				// If it is none of those characters, the FSM halts
				else { 
					running = false;
				}
				
				break;				
				// The execution of this state is finished
			
			case 1:
			    // Local part: allow letters, digits, '.', '_', '-'
			    // Or '@' to begin domain
			    if ((currentChar >= 'A' && currentChar <= 'Z') ||
			        (currentChar >= 'a' && currentChar <= 'z') ||
			        (currentChar >= '0' && currentChar <= '9') ||
			        currentChar == '.' || currentChar == '_' || currentChar == '-') {
			        nextState = 1; // stay in local part
			    }
			    else if (currentChar == '@') {
			        nextState = 2; // move to domain start
			    }
			    else {
			        running = false;
			    }
			    break;

			case 2:
			    // First character after '@' must be alphanumeric
			    if ((currentChar >= 'A' && currentChar <= 'Z') ||
			        (currentChar >= 'a' && currentChar <= 'z') ||
			        (currentChar >= '0' && currentChar <= '9')) {
			        domainPartCounter = 1;
			        nextState = 3; // domain body
			    }
			    else {
			        running = false;
			    }
			    break;

			case 3:
			    // Domain body: alphanumeric continues label; '.' starts next label
			    if ((currentChar >= 'A' && currentChar <= 'Z') ||
			        (currentChar >= 'a' && currentChar <= 'z') ||
			        (currentChar >= '0' && currentChar <= '9')) {

			        domainPartCounter++;
			        if (domainPartCounter > 63) {
			            running = false;
			        } else {
			            nextState = 3;
			        }
			    }
			    else if (currentChar == '.') {
			        domainPartCounter = 0;
			        nextState = 4;
			    }
			    else {
			        running = false;
			    }
			    break;

			case 4:
			    // After '.', next label must start alphanumeric
			    if ((currentChar >= 'A' && currentChar <= 'Z') ||
			        (currentChar >= 'a' && currentChar <= 'z') ||
			        (currentChar >= '0' && currentChar <= '9')) {
			        domainPartCounter = 1;
			        nextState = 3;
			    }
			    else {
			        running = false;
			    }
			    break;


			}
			
			if (running) {
				displayDebuggingInfo();
				// When the processing of a state has finished, the FSM proceeds to the next character
				// in the input and if there is one, it fetches that character and updates the 
				// currentChar.  If there is no next character the currentChar is set to a blank.
				
				moveToNextCharacter();
				
				// Move to the next state
				state = nextState;
				nextState = -1;
			}
			// Should the FSM get here, the loop starts again

		}
		displayDebuggingInfo();
		
		System.out.println("The loop has ended.");

		emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
		
		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states and that
		// makes it possible for this code to display a very specific error message to improve the
		// user experience.
		switch (state) {
		case 0:
		    emailAddressIndexofError = currentCharNdx;
		    emailAddressErrorMessage = "May only be alphanumeric.\n";
		    return emailAddressErrorMessage + displayInput(input, currentCharNdx);

		case 1:
		    emailAddressIndexofError = currentCharNdx;
		    emailAddressErrorMessage = "Invalid character in the local part or missing '@'.\n";
		    return emailAddressErrorMessage + displayInput(input, currentCharNdx);

		case 2:
		    emailAddressIndexofError = currentCharNdx;
		    emailAddressErrorMessage = "After '@', the domain must start with a letter or digit.\n";
		    return emailAddressErrorMessage + displayInput(input, currentCharNdx);

		case 3:
		    // FINAL STATE: valid ONLY if we've consumed the entire string
		    if (currentCharNdx < input.length()) {
		        emailAddressIndexofError = currentCharNdx;
		        emailAddressErrorMessage = "This must be the end of the input.\n";
		        return emailAddressErrorMessage + displayInput(input, currentCharNdx);
		    } else {
		        emailAddressIndexofError = -1;
		        emailAddressErrorMessage = "";
		        return "";
		    }

		case 4:
		    emailAddressIndexofError = currentCharNdx;
		    emailAddressErrorMessage = "After '.', the next domain part must start with a letter or digit.\n";
		    return emailAddressErrorMessage + displayInput(input, currentCharNdx);

		default:
		    // Safety net
		    emailAddressIndexofError = currentCharNdx;
		    emailAddressErrorMessage = "Invalid email address.\n";
		    return emailAddressErrorMessage + displayInput(input, currentCharNdx);
		}

		
	}
}
