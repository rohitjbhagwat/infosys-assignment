package com.infosys.fibonacci.service;

import com.infosys.exceptions.ValidationException;

/**
 * FibonacciService is used to find the Fibonacci number.
 *
 * @author Rohit
 */
public interface FibonacciService {

	/**
	 * Returns the nth fibonacci number.
	 *
	 * @param number - nth number
	 * @return fibonacci number.
	 * @throws ValidationException - exception in case the number is too big to process.
	 * @see FibonacciServiceImpl#getFibonacciNumber(long)
	 */
	public String getFibonacciNumber(long number) throws ValidationException;
}
