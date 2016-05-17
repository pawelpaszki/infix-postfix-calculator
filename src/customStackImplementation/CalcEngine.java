package customStackImplementation;

/**
 * The main part of the calculator doing the calculations.
 * 
 * @author Pawel Paszki
 * @version 25/02/2016
 */
public class CalcEngine {

	private String displayValue;
	private String postfixDisplayValue;
	private boolean incorrectExpressionFound = false;

	/**
	 * Create a CalcEngine instance. Initialise its state so that it is ready
	 * for use.
	 */
	public CalcEngine() {
		displayValue = "";
	}

	/**
	 * Return the expression to be displayed
	 */
	public String getDisplayValue() {
		return (displayValue);
	}

	/**
	 * do suitable action according to the button pressed - 
	 * as a first value only a digit or ".", "(", "-" allowed.
	 * only one decimal point allowed in any given number
	 */
	public void buttonPressed(String value) {
		if (displayValue.length() == 0) {
			switch (value) {
			case "0":
			case "1":
			case "2":
			case "3":
			case "4":
			case "5":
			case "6":
			case "7":
			case "8":
			case "9":
			case ".":
			case "(":
			case "-":
				displayValue += value;
				break;
			default:
				displayValue = "";
				break;
			}
		} else {
			// in this if statement a check is performed, so that if there is a
			// value with a decimal point in it - another decimal point cannot
			// be added
			if (value.equals(".")) {
				boolean dotFound = false;
				for (int i = displayValue.length() - 1; i >= 0; i--) {
					if (Character.isDigit(displayValue.charAt(i))) {
						continue;
					} else {
						if (displayValue.charAt(i) == '.') {
							dotFound = true;
							break;
						} else {
							break;
						}
					}
				}
				if (!dotFound) {
					displayValue += value;
				}
			} else {
				displayValue += value;
			}
		}
	}

	/**
	 * this method (after "=" button was pressed) takes the displayValue
	 * and parses it into postfix notation 
	 * @return stack representing postfix notation, if conversion successful
	 */
	private CustomStack infixToPostfix() {
		String itemToPush = "";
		CustomStack postfixNotationStack = new CustomStack(displayValue.length());
		CustomStack temporaryOperatorStack = new CustomStack(displayValue.length());
		for (int i = 0; i < displayValue.length(); i++) {
			if (i == 0) { // char at index 0 - either a digit or opening
							// parenthesis or a minus sign
				if (Character.isDigit(displayValue.charAt(i)) || displayValue.charAt(i) == '-') {
					itemToPush += displayValue.charAt(i);
				} else {
					temporaryOperatorStack.push(String.valueOf(displayValue.charAt(i)));
				}
			} else {
				// if current character is a digit or a decimal point - it is
				// added
				// to the end of current itemToPush
				if (Character.isDigit(displayValue.charAt(i)) || displayValue.charAt(i) == '.') {
					itemToPush += displayValue.charAt(i);
					if (i + 1 == displayValue.length()) {
						postfixNotationStack.push(itemToPush);
					}
				} else {
					// if not digit push value to operands stack
					postfixNotationStack.push(itemToPush);
					itemToPush = "";
				}
				if (displayValue.charAt(i) == '(') {
					temporaryOperatorStack.push(String.valueOf(displayValue.charAt(i)));
				}
				if (displayValue.charAt(i) == ')') {
					while (!temporaryOperatorStack.peek().equals("(")) {
						postfixNotationStack.push(String.valueOf(temporaryOperatorStack.pop()));
					}
					temporaryOperatorStack.pop();
				}
				if (isOperator(displayValue.charAt(i))) {
					while (!temporaryOperatorStack.isEmpty() && (!(temporaryOperatorStack.peek().equals(")"))
							&& (getPrecedence(String.valueOf(displayValue.charAt(i)))) <= getPrecedence(
									temporaryOperatorStack.peek()))) {
						postfixNotationStack.push(String.valueOf(temporaryOperatorStack.pop()));
					}
					temporaryOperatorStack.push(String.valueOf(displayValue.charAt(i)));
				}
			}
		}
		while (!temporaryOperatorStack.isEmpty()) {
			postfixNotationStack.push(String.valueOf(temporaryOperatorStack.pop()));
		}
		postfixDisplayValue = "";
		CustomStack reversedPostfix = new CustomStack(displayValue.length());
		for (int i = postfixNotationStack.size() - 1; i >= 0; i--) {
			if (postfixNotationStack.get(i) != "") {
				reversedPostfix.push(postfixNotationStack.get(i));
			}
		}
		for (int i = 0; i < postfixNotationStack.size(); i++) {
			if (!postfixNotationStack.get(i).equals("")) {
				postfixDisplayValue += " " + postfixNotationStack.get(i);
			}
		}
		return reversedPostfix;
	}

	/**
	 * when called - this method will initiate evaluation of an expression
	 * given that the expression is longer than 3 characters
	 */
	public void getResult() {
		if (displayValue.length() < 3) {
			displayValue = "";
			postfixDisplayValue = "";
		} else {
			if (parenthesisBalanced()) {
				System.out.println("brackets balanced");
				try {
					displayValue = "= " + evaluatePostfix(infixToPostfix());
				} catch (Exception e) {
					postfixDisplayValue = "error! incorrect expression";
					displayValue = "";
					setIncorrectExpressionFound(true);
				}
			} else {
				clear();
			}
		}
	}

	/**
	 * 
	 * @param reversedPostfix is passed and processed in order to evaluate 
	 * the postfix expression
	 * @return the result if an expression is valid and can be evaluated
	 */
	private double evaluatePostfix(CustomStack reversedPostfix) {
		CustomStack temporaryOperatorStack = reversedPostfix;
		CustomStack operandStack = new CustomStack(displayValue.length());
		while (temporaryOperatorStack.size() > 0) {
			if (isNumeric(temporaryOperatorStack.peek())) {
				operandStack.push(temporaryOperatorStack.pop());
			} else {
				String operator = temporaryOperatorStack.pop();
				double firstOperand = Double.valueOf(operandStack.pop());
				double secondOperand = Double.valueOf(operandStack.pop());
				switch (operator) {
				case "*":
					operandStack.push(String.valueOf(multiply(firstOperand, secondOperand)));
					break;
				case "/":
					operandStack.push(String.valueOf(divide(secondOperand, firstOperand)));
					break;
				case "+":
					operandStack.push(String.valueOf(add(secondOperand, firstOperand)));
					break;
				case "-":
					operandStack.push(String.valueOf(subtract(secondOperand, firstOperand)));
					break;
				case "^":
					operandStack.push(String.valueOf(powerOf(secondOperand, firstOperand)));
					break;
				}
			}
		}
		return Double.valueOf(operandStack.peek());
	}

	/**
	 * 
	 * @param str is passed
	 * @return true if the parameter is a number, false otherwise
	 */
	private boolean isNumeric(String str) {
		return str.matches("\\d*(\\.\\d+)?");
	}

	/**
	 * 
	 * @return true if the expression has balanced parenthesis - ie 
	 * opening and closing parenthesis match
	 */
	private boolean parenthesisBalanced() {
		CustomStack expressionStack = new CustomStack(displayValue.length());
		boolean parenthesisBalanced = true;
		int index = 0;
		while (parenthesisBalanced && index < displayValue.length()) {
			char nextCharacter = displayValue.charAt(index);
			switch (nextCharacter) {
			case '(':
				expressionStack.push(String.valueOf(nextCharacter));
				break;
			case ')':
				if (expressionStack.isEmpty()) {
					parenthesisBalanced = false;
				} else {
					expressionStack.pop();
				}
				break;
			}
			index++;
		}
		if (!expressionStack.isEmpty()) {
			parenthesisBalanced = false;
		}
		return parenthesisBalanced;
	}

	/**
	 * 
	 * @param firstNumber
	 * @param secondNumber
	 * @return the sum of two parameters
	 */
	private double add(double firstNumber, double secondNumber) {
		return firstNumber + secondNumber;
	}

	/**
	 * 
	 * @param firstNumber
	 * @param secondNumber
	 * @return value of secondNumber subtracted from firstNumber
	 */
	private double subtract(double firstNumber, double secondNumber) {
		return firstNumber - secondNumber;
	}

	/**
	 * 
	 * @param firstNumber
	 * @param secondNumber
	 * @return product of two parameters
	 */
	private double multiply(double firstNumber, double secondNumber) {
		return firstNumber * secondNumber;
	}

	/**
	 * 
	 * @param firstNumber
	 * @param secondNumber
	 * @return value of firstNumber divided by second
	 */
	private double divide(double firstNumber, double secondNumber) {
		return firstNumber / secondNumber;
	}

	/**
	 * 
	 * @param firstNumber
	 * @param secondNumber
	 * @return the value of firstNumber raised to the power of secondNumber
	 */
	private double powerOf(double firstNumber, double secondNumber) {
		return Math.pow(firstNumber, secondNumber);
	}

	/**
	 * 
	 * @param operator
	 * @return the precedence of a passed operator (3 is the highest, 1 the lowest)
	 */
	private int getPrecedence(String operator) {
		switch (operator) {
		case "^":
			return 3;
		case "*":
		case "/":
			return 2;
		case "+":
		case "-":
			return 1;
		}
		return -1;
	}

	/**
	 * 
	 * @param character
	 * @return true if a passed parameter is an operator
	 */
	private boolean isOperator(char character) {
		return (character == '^' || character == '*' || character == '/' || character == '+' || character == '-');
	}

	/**
	 * The 'C' (clear) button was pressed.
	 */
	public void clear() {
		displayValue = "";
		setIncorrectExpressionFound(false);
	}

	/**
	 * Return the title of this calculation engine.
	 */
	public String getTitle() {
		return ("My Calculator");
	}

	/**
	 * Return the author of this engine. This string is displayed as it is, so
	 * it should say something like "Written by H. Simpson".
	 */
	public String getAuthor() {
		return ("Pawel Paszki");
	}

	/**
	 * 
	 * @return postfixDisplayValue
	 */
	public String getPostFixDisplayValue() {
		return postfixDisplayValue;
	}

	/**
	 * @return the incorrectExpressionFound
	 */
	public boolean isIncorrectExpressionFound() {
		return incorrectExpressionFound;
	}

	/**
	 * @param incorrectExpressionFound the incorrectExpressionFound to set
	 */
	public void setIncorrectExpressionFound(boolean incorrectExpressionFound) {
		this.incorrectExpressionFound = incorrectExpressionFound;
	}

}