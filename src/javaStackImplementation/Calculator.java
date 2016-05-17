package javaStackImplementation;

/**
 * The main class of a simple calculator. Create one of these and you'll
 * get the calculator on screen.
 * 
 * @author Pawel Paszki
 * @version 25/02/2016
 */
public class Calculator
{
	private CalcEngine engine;
	private UserInterface gui;

	/**
	 * Create a new calculator and show it.
	 */
	public Calculator()
	{
		engine = new CalcEngine();
		gui = new UserInterface(engine);
	}

	public static void main(String[] args)
	{
		Calculator cTest = new Calculator();
		while(true);
	}
}