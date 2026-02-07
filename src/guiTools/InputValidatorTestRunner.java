package guiTools;

public class InputValidatorTestRunner {

	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests

	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\nTest Case Automation");

		/************** Username test cases **************/
		performUsernameTestCase(1, "JohnDoe26", true);
		performUsernameTestCase(2, "Doey!@#", false);
		performUsernameTestCase(3, "", false);
		performUsernameTestCase(4, "A".repeat(26), false);

		/************** Password test cases **************/
		performPasswordTestCase(1, "Ab1!5678", true);
		performPasswordTestCase(2, "Ab1!567", false);
		performPasswordTestCase(3, "password1!", false);
		performPasswordTestCase(4, "PASSWORD1!", false);
		performPasswordTestCase(5, "Password!", false);
		performPasswordTestCase(6, "Password123", false);
		performPasswordTestCase(7, "Pass 123!", false);
		performPasswordTestCase(8, "Ab1!5678Ab1!5678Ab1!5678Ab1!567", false);

		/************** Name test cases **************/
		performNameTestCase(1, "John A Doe", true);
		performNameTestCase(2, "JoHn' doe", false);
		performNameTestCase(3, "Doey<script>", false);
		performNameTestCase(4, "A".repeat(51), false);

		/************** Email address test cases **************/
		performEmailTestCase(1, "johnd@asu.edu", true);
		performEmailTestCase(2, "john.asu.edu", false);
		performEmailTestCase(3, "john$%@asu", false);
		performEmailTestCase(4, "a@b." + "x".repeat(61), false);

		/************** End of the test cases **************/

		/************** Test cases semi-automation report footer **************/
		System.out.println("______________________________________");
		System.out.println();
		System.out.println("Number of tests passed: " + numPassed);
		System.out.println("Number of tests failed: " + numFailed);
	}

	// Username test case
	public static void performUsernameTestCase(int testCase, String inputText, boolean expectedPass) {
		/************** Display an individual test case header **************/
		System.out.println("______________________________________\n\nTest case (Username): " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("_______");
		System.out.println("\nFinite state machine execution trace:");

		/************** Call the recognizer to process the input **************/
		String resultText = UserNameRecognizer.checkForValidUserName(inputText);

		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		interpretResult(inputText, resultText, expectedPass, "username");
	}

	// Password test case
	public static void performPasswordTestCase(int testCase, String inputText, boolean expectedPass) {
		/************** Display an individual test case header **************/
		System.out.println("______________________________________\n\nTest case (Password): " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("_______");
		System.out.println("\nEvaluator execution trace:");

		/************** Call the evaluator to process the input **************/
		String resultText = PasswordEvaluator.evaluatePassword(inputText);

		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		interpretResult(inputText, resultText, expectedPass, "password");
	}

	// Name test case
	public static void performNameTestCase(int testCase, String inputText, boolean expectedPass) {
		/************** Display an individual test case header **************/
		System.out.println("______________________________________\n\nTest case (Name): " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("_______");
		System.out.println("\nValidation execution trace:");

		/************** Call the recognizer to process the input **************/
		String resultText = NameRecognizer.checkFullName(inputText);

		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		interpretResult(inputText, resultText, expectedPass, "name");
	}

	// Email test case
	public static void performEmailTestCase(int testCase, String inputText, boolean expectedPass) {
		/************** Display an individual test case header **************/
		System.out.println("______________________________________\n\nTest case (Email): " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("_______");
		System.out.println("\nFinite state machine execution trace:");

		/************** Call the recognizer to process the input **************/
		String resultText = EmailAddressRecognizer.checkEmailAddress(inputText);

		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		interpretResult(inputText, resultText, expectedPass, "email address");
	}

	// Interprets the result showing Success or Failure
	private static void interpretResult(String inputText, String resultText, boolean expectedPass, String inputLabel) {
		// If the resulting text is not empty, the recognizer rejected the input
		if (resultText != null && !resultText.isEmpty()) {
			// If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The " + inputLabel + " <" + inputText + "> is invalid." +
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {
				System.out.println("***Success*** The " + inputLabel + " <" + inputText + "> is invalid." +
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		// If the resulting text is empty, the recognizer accepted the input
		else {
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {
				System.out.println("***Success*** The " + inputLabel + " <" + inputText +
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The " + inputLabel + " <" + inputText +
						"> was judged as valid" +
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
	}
}
