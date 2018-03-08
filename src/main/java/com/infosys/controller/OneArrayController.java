package com.infosys.controller;

import java.text.MessageFormat;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.beans.Headers;
import com.infosys.exceptions.ValidationException;
import com.infosys.onearray.beans.OneArrayBean;
import com.infosys.onearray.service.OneArrayService;
import com.infosys.util.ValidationConstants;

/**
 * Controller class to merge two integer array into one and the resultant array
 * will be sorted without any duplicates.
 *
 * @author Rohit
 */
@RestController
public class OneArrayController {

	/** LOGGER is used to log debug, info and error messages. */
	private static final Logger LOGGER = LoggerFactory.getLogger(OneArrayController.class);

	/** Represents an instance of <code>OneArrayServiceImpl</code>. */
	@Autowired
	private OneArrayService service;

	/**
	 * Represents an instance of <code>Environment</code> object injected by
	 * Spring.
	 */
	@Autowired
	private Environment env;

	/**
	 * Represents <code>HttpHeaders</code> to be set in the
	 * <code>ResponseEntity</code>
	 */
	@Autowired
	private Headers httpHeaders;

	/**
	 * makeOneArray is merges two input integer array into one output array. The
	 * output array is sorted and without any duplicates.
	 *
	 * URI :/api/makeonearray
	 * Method: POST
	 *
	 * @param body
	 *            Instance of String containing input arrays in JSON format.
	 * @return entity Instance of <code>ResponseEntity</code> containing the
	 *         merged array.
	 */
	@RequestMapping(value = "/api/makeonearray", method = RequestMethod.POST)
	public ResponseEntity makeOneArray(@RequestBody() String body) {
		ResponseEntity entity = null;
		try {
			final OneArrayBean output = service.convertToOneArray(body);
			final StringBuilder arrayBuilder = new StringBuilder();
			Arrays.stream(output.getArray()).forEach(number -> arrayBuilder.append(number + ","));
			LOGGER.info(MessageFormat.format("makeOneArray: input = {0} and output = {1}", body, arrayBuilder));
			final HttpHeaders headers = httpHeaders.getHeaders(output, "OneArray");
			LOGGER.debug(MessageFormat.format("Response headers for makeOneArray = {0}", headers));
			entity = new ResponseEntity<>(output, headers, HttpStatus.OK);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage(), e);
			HttpStatus httpStatus = null;
			if (e.getErrorCode() == ValidationConstants.INCORRECT_FORMAT_OF_INPUT_STRING) {
				httpStatus = HttpStatus.BAD_REQUEST;
			} else if (e.getErrorCode() == ValidationConstants.INPUT_STRING_IS_TOO_LONG) {
				httpStatus = HttpStatus.BANDWIDTH_LIMIT_EXCEEDED;
			}
			entity = new ResponseEntity<>(env.getProperty("error_" + e.getErrorCode()), httpStatus);
		}
		return entity;
	}
}