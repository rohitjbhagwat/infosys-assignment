package com.infosys.controller;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.beans.Headers;
import com.infosys.exceptions.ValidationException;
import com.infosys.fibonacci.service.FibonacciService;
import com.infosys.util.ValidationConstants;

/**
 * FibonacciController is used to accept an nth number and would return the
 * fibonacci of that nth number.
 *
 * @author Rohit
 */
@RestController
public class FibonacciController {

	/** LOGGER is used to log debug, info and error messages. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FibonacciController.class);

	/** Represents an instance of <code>FibonacciServiceImpl</code>. */
	@Autowired
	private FibonacciService service;

	/**
	 * Represents <code>HttpHeaders</code> to be set in the
	 * <code>ResponseEntity</code>
	 */
	@Autowired
	private Headers httpHeaders;

	/**
	 * Represents an instance of <code>Environment</code> object injected by
	 * Spring.
	 */
	@Autowired
	private Environment env;

	/**
	 * getFibonacciNumber is used to generate the Fibonacci number of nth
	 * series.
	 *
	 * URI :/api/Fibonacci?n=10
	 * Method: GET
	 *
	 * @param number
	 *            representing nth number.
	 * @return entity Instance of <code>ResponseEntity</code> containing the
	 *         Fibonacci number.
	 */
	@RequestMapping(value = "/api/Fibonacci", method = RequestMethod.GET)
	public ResponseEntity<String> getFibonacciNumber(@RequestParam("n") final long number) {
		ResponseEntity<String> entity = null;
		try {
			String output = service.getFibonacciNumber(number);
			LOGGER.info(MessageFormat.format("FibonacciController.getFibonacciNumber of {0} is {1}", number, output));
			HttpHeaders headers = httpHeaders.getHeaders(output, "Fibonacci");
			LOGGER.debug("FibonacciController response headers = {0}", headers);
			entity = new ResponseEntity<>(output, headers, HttpStatus.OK);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage(), e);
			HttpStatus httpStatusCode = null;
			if (e.getErrorCode() == ValidationConstants.NUMBER_LESS_THAN_ZERO) {
				httpStatusCode = HttpStatus.NOT_FOUND;
			} else if (e.getErrorCode() == ValidationConstants.NUMBER_IS_TOO_LARGE) {
				httpStatusCode = HttpStatus.BANDWIDTH_LIMIT_EXCEEDED;
			}
			entity = new ResponseEntity<>(env.getProperty("error_" + e.getErrorCode()), httpStatusCode);
		}
		return entity;
	}
}