package com.infosys.fibonacci.service;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.infosys.exceptions.ValidationException;
import com.infosys.util.ValidationConstants;

/**
 * FibonacciServiceImpl  is used to find the Fibonacci number.
 * An implementation of <code>FibonacciService</code>
 *
 * @author Rohit
 */
@Service
public class FibonacciServiceImpl implements FibonacciService{

	/** LOGGER is used to log debug, info and error messages. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FibonacciServiceImpl.class);

	/** Represents fibonacci.max_number value configured in application.properties file.*/
	@Value("${fibonacci.max_number}")
	private String maxValue;

	/** map represents a cache to store the Fibonacci number so that subsequent request for
	 * the nth number doesnt compute the fibonacci number.*/
	private static ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<>();

	/**
	 *Calculates the nth fibonacci number.
	 *
	 *@param number represents nth number.
	 *@return String representing fibonacci number.
	 *@throws ValidationException Exception if the Fibonacci number is too large.
	 */
	public String getFibonacciNumber(final long number) throws ValidationException {
		LOGGER.info("FibonacciServiceImpl.getFibonacciNumber");
		String output = null;
		if (number == 0 || number == 1) {
			output = String.valueOf(number);
		} else if (number < 0) {
			throw new ValidationException(
					ValidationConstants.NUMBER_LESS_THAN_ZERO);
		} else if (number > Long.valueOf(maxValue)){
			throw new ValidationException("Number is too large",
					ValidationConstants.NUMBER_IS_TOO_LARGE);
		} else {
			if(map.get(number) == null){
				BigDecimal number1 = BigDecimal.ONE;
				BigDecimal number2 = BigDecimal.ONE;
				BigDecimal temp;
				for(long i = 2; i < number; i++){
					temp = number2.add(number1);
					number1 = number2;
					number2 = temp;
				}
				map.put(number, number2.toPlainString());
			}
			output = map.get(number);
		}
		return output;
	}
}