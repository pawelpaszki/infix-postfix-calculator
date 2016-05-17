package customStackImplementation;

import java.util.Arrays;

/**
 * This class is a sustom version of a Java Stack with basically the same 
 * functionality
 * 
 * @author Pawel Paszki
 * version 25/02/2016
 *
 */
public class CustomStack {
	private String[] stackArray;
	private int size;
	private int topIndex = -1;

	/**
	 * create CustomStack object
	 * @param size specifies the number of elements the stack can hold
	 */
	public CustomStack(int size) {
		this.size = size;
		stackArray = new String[size];
		Arrays.fill(stackArray, "");
	}

	/**
	 * 
	 * @param value is pushed to the top of the stack
	 */
	public void push(String value) {
		if (topIndex + 1 < size) {
			stackArray[++topIndex] = value;
		}
	}

	/**
	 * 
	 * @return the top value from the stack, which also is removed after 
	 * being returned
	 */
	public String pop() {
		String topValue = null;
		if (topIndex >= 0) {
			topValue = stackArray[topIndex--];
		}
		return topValue;
	}
	
	/**
	 * 
	 * @return the top value of the stack, but not remove it
	 */
	public String peek() {
		String topValue = null;
		if(topIndex >= 0) {
			topValue = stackArray[topIndex];
		} else {
			topValue = "";
		}
		return topValue;
	}
	
	/**
	 * 
	 * @return true, if the stack is empty
	 */
	public boolean isEmpty() {
		return topIndex < 0;
	}
	
	/**
	 * 
	 * @return the size of the stack
	 */
	public int size() {
		return topIndex + 1;
	}

	/**
	 * 
	 * @param index specified
	 * @return the value with given index
	 */
	public String get(int index) {
		if(index >=0 && index < size()) {
			return stackArray[index];
		}
		return null;
	}
}