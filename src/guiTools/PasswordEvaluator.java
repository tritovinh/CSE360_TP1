package guiTools;

/*******
 * <p> Title: PasswordEvaluator Class. </p>
 * 
 * <p> Description: A Java  </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2022 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00	2022-02-25 A set of semi-automated test cases
 * @version 2.00	2024-09-22 Updated for use at ASU
 * 
 * changes made by Rio McCue for use in Team Project 1
 * 
 */


public class PasswordEvaluator{
	
	public static String passwordErrorMessage;	 		// The error message text
	public static String passwordInput = "";			// The input being processed
	public static int passwordIndexofError = -1;		// The index where the error was located
	public static boolean foundUpperCase = false;
	public static boolean foundLowerCase = false;
	public static boolean foundNumericDigit = false;
	public static boolean foundSpecialChar = false;
	public static boolean foundLongEnough = false;
	public static boolean foundTooLong = false;
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;						// The flag that specifies if the FSM is 
														// running
/**********
 	* <p> Title: evaluatePassword - Public Method </p>
 * 
 * <p> Description: This method is a mechanical transformation of a Directed Graph diagram 
 * into a Java method. This method is used by both the GUI version of the application as well
 * as the testing automation version.
 * 
 * @param input		The input string evaluated by the directed graph processing
 * @return			An output string that is empty if every things is okay or it will be
 * 						a string with a helpful description of the error follow by two lines
 * 						that shows the input line follow by a line with an up arrow at the
 *						point where the error was found.
 */


public static String evaluatePassword(String input) {
	// The following are the local variable used to perform the Directed Graph simulation
	passwordErrorMessage = "";
	passwordIndexofError = 0;			// Initialize the IndexofError
	inputLine = input;					// Save the reference to the input line as a global
	currentCharNdx = 0;					// The index of the current character
	
	if(input.length() <= 0) {
		return "The password is empty!";
	}
	
	// The input is not empty, so we can access the first character
	currentChar = input.charAt(0);		// The current character from the above indexed position

	// The Directed Graph simulation continues until the end of the input is reached or at some 
	// state the current character does not match any valid transition to a next state.  This
	// local variable is a working copy of the input.
	passwordInput = input;				// Save a copy of the input
	
	// The following are the attributes associated with each of the requirements
	foundUpperCase = false;				// Reset the Boolean flag
	foundLowerCase = false;				// Reset the Boolean flag
	foundNumericDigit = false;			// Reset the Boolean flag
	foundSpecialChar = false;			// Reset the Boolean flag
	foundNumericDigit = false;			// Reset the Boolean flag
	foundLongEnough = false;			// Reset the Boolean flag
	foundTooLong = true;				// Reset the Boolean flag
	
	// This flag determines whether the directed graph (FSM) loop is operating or not
	running = true;						// Start the loop

	// The Directed Graph simulation continues until the end of the input is reached or at some
	// state the current character does not match any valid transition
	while (running) {
		// The cascading if statement sequentially tries the current character against all of
		// the valid transitions, each associated with one of the requirements
		if (currentChar >= 'A' && currentChar <= 'Z') {
			System.out.println("Upper case letter found");
			foundUpperCase = true;
		} else if (currentChar >= 'a' && currentChar <= 'z') {
			System.out.println("Lower case letter found");
			foundLowerCase = true;
		} else if (currentChar >= '0' && currentChar <= '9') {
			System.out.println("Digit found");
			foundNumericDigit = true;
		} else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currentChar) >= 0) {
			System.out.println("Special character found");
			foundSpecialChar = true;
		} else {
			passwordIndexofError = currentCharNdx;
			return "*** Error *** An invalid character has been found!";
		}
		if (currentCharNdx >= 7) {
			System.out.println("At least 8 characters found");
			foundLongEnough = true;
		}
		if (currentCharNdx >= 25) {
			System.out.println("Too many characters!");
			foundTooLong = false;
			
		}
		
		// Go to the next character if there is one
		currentCharNdx++;
		if (currentCharNdx >= inputLine.length())
			running = false;
		else
			currentChar = input.charAt(currentCharNdx);
		
		System.out.println();
	}
	
	// Construct a String with a list of the requirement elements that were found.
	String errMessage = "";
	if (!foundUpperCase)
		errMessage += "Upper case; ";
	
	if (!foundLowerCase)
		errMessage += "Lower case; ";
	
	if (!foundNumericDigit)
		errMessage += "Numeric digits; ";
		
	if (!foundSpecialChar)
		errMessage += "Special character; ";
		
	if (!foundLongEnough)
		errMessage += "Long Enough; ";
	
	if (!foundTooLong)
		errMessage += "Too Long; ";
		
	// if there are no errors
	if (errMessage == "")
		return "";
	
	passwordErrorMessage = "There is no " + errMessage;
	// If it gets here, there something was not found, so return an appropriate message
	passwordIndexofError = currentCharNdx;
	return errMessage;
}
}