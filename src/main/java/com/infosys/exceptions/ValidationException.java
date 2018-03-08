package com.infosys.exceptions;

/**
 * ValidationException is used to wrap business exception and provide meaningful message.
 *
 * @author Rohit
 */
public class ValidationException extends Exception {

	/**Default serialVersionUID	 */
	private static final long serialVersionUID = 1L;

	/** Represents errorCode of an exception.*/
	private final int errorCode;

	/**
	 * Constructor to store errorCode.
	 *
	 * @param msg - Meaningful message of an exception.
	 * @param errorCode - Error code of an exception.
	 */
	public ValidationException(String msg, int errorCode){
		super(msg);
		this.errorCode = errorCode;
	}

	/**
	 * Constructor to map only errorCode passed as a parameter.
	 *
	 * @param errorCode - Represents an errorCode of an exception.
	 */
	public ValidationException(int errorCode){
		this.errorCode = errorCode;
	}

	/**
	 * Returns errorCode to pull the message from the properties file.
	 *
	 * @return int - ErrorCode.
	 */
	public int getErrorCode(){
		return errorCode;
	}
}