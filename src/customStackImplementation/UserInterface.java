package customStackImplementation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * A graphical user interface for the calculator. No calculation is being done
 * here. This class is responsible just for putting up the display on screen. It
 * then refers to the "CalcEngine" to do all the real work.
 * 
 * @author Pawel Paszki
 * @version 25/02/2016
 */
public class UserInterface implements ActionListener {
	private CalcEngine calc;
	private JFrame frame;
	private JTextField infixDisplay;
	private JLabel status;
	private JTextField postfixDisplay;

	/**
	 * Create a user interface for a given calcEngine.
	 */
	public UserInterface(CalcEngine engine) {
		calc = engine;
		makeFrame();
		frame.setVisible(true);
	}

	/**
	 * Make the frame for the user interface.
	 */
	private void makeFrame() {
		frame = new JFrame(calc.getTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.BLACK);

		JPanel contentPane = (JPanel) frame.getContentPane();
		contentPane.setLayout(new BorderLayout(8, 8));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel displayPanel = new JPanel(new GridLayout(2, 1));

		infixDisplay = new JTextField();
		postfixDisplay = new JTextField();
		customiseTextField(infixDisplay);
		customiseTextField(postfixDisplay);
		displayPanel.add(postfixDisplay);
		displayPanel.add(infixDisplay);
		contentPane.add(displayPanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new GridLayout(5, 4));
		addButton(buttonPanel, "C");
		addButton(buttonPanel, "(");
		addButton(buttonPanel, ")");
		addButton(buttonPanel, "/");
		addButton(buttonPanel, "7");
		addButton(buttonPanel, "8");
		addButton(buttonPanel, "9");
		addButton(buttonPanel, "*");
		addButton(buttonPanel, "4");
		addButton(buttonPanel, "5");
		addButton(buttonPanel, "6");
		addButton(buttonPanel, "+");
		addButton(buttonPanel, "1");
		addButton(buttonPanel, "2");
		addButton(buttonPanel, "3");
		addButton(buttonPanel, "-");
		addButton(buttonPanel, "0");
		addButton(buttonPanel, ".");
		addButton(buttonPanel, "^");
		addButton(buttonPanel, "=");

		contentPane.add(buttonPanel, BorderLayout.CENTER);
		Dimension dimension = new Dimension(350, 350);
		contentPane.setPreferredSize(dimension);

		status = new JLabel(calc.getAuthor());
		contentPane.add(status, BorderLayout.SOUTH);
		status.setHorizontalAlignment(SwingConstants.RIGHT);

		frame.pack();
		frame.setLocationRelativeTo(null);
	}

	/**
	 * this method changes the appearance of the passed textField: sets
	 * background to black, applies font properties, size and centres the text
	 * inside the textField (textField becomes uneditable - only by pressing
	 * buttons the value can be input into it)
	 * 
	 * @param textfield
	 */
	private void customiseTextField(JTextField textfield) {
		Dimension textFieldDimension = new Dimension(350, 30);
		textfield.setPreferredSize(textFieldDimension);
		textfield.setHorizontalAlignment(SwingConstants.CENTER); // text centred
		textfield.setEditable(false); // no typing in input
		textfield.setFont(new Font("Arial", Font.BOLD, 20)); // fony style
		textfield.setBackground(Color.BLACK); // background color
		textfield.setForeground(new Color(0, 255, 35)); // font color
	}

	/**
	 * Add a button to the button panel.
	 */
	private void addButton(Container panel, String buttonText) {
		JButton button = new JButton(buttonText);
		button.addActionListener(this);
		button.setFont(new Font("Arial", Font.BOLD, 20));
		button.setForeground(new Color(0, 255, 35)); // font color
		button.setBackground(Color.BLACK);
		panel.add(button);
	}

	/**
	 * An interface action has been performed. when the "=" button is pressed
	 * calcEngine tries to evaluate the expression and as long as it is valid -
	 * it will evaluate it and the result will be printed along with the
	 * expression in postfix notation. C clears both textFields. other buttons
	 * will make their representative values added to the end of the expression
	 */
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equals("=")) {
			calc.getResult();
			postfixDisplay.setText(calc.getPostFixDisplayValue());
		} else {
			postfixDisplay.setText("");
			if (command.equals("C")) {
				calc.clear();
			}
			if (calc.getDisplayValue().startsWith("=")) {
				calc.clear();
			}
			calc.buttonPressed(command);
		}
		redisplay();
	}

	/**
	 * Update the interface display to show the current value of the calculator.
	 */
	private void redisplay() {
		infixDisplay.setText(calc.getDisplayValue());
	}
}
