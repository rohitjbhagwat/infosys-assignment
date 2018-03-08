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
import com.infosys.reversewords.service.ReverseWordsService;
import com.infosys.util.ValidationConstants;

/**
 * Controller class to reverse each word in a given sentence.
 *
 * @author Rohit
 */
@RestController
public class ReverseWordsController {

	/** LOGGER is used to log debug, info and error messages. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReverseWordsController.class);

	/** Represents an instance of <code>ReverseWordsServiceImpl</code>. */
	@Autowired
	private ReverseWordsService service;

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
	 * getReverseWords reverses each word separated by space in a given
	 * sentence.
	 *
	 * URI :/api/ReverseWords
	 * Method: GET
	 *
	 * @param sentence
	 *            Instance of String containing words separated by spaces.
	 * @return entity Instance of <code>ResponseEntity</code> containing the
	 *         reversed String.
	 */
	@RequestMapping(value = "/api/ReverseWords", method = RequestMethod.GET)
	public ResponseEntity<String> getReverseWords(@RequestParam("sentence") String sentence) {
		ResponseEntity<String> entity = null;
		try {
			String output = service.getReverseWords(sentence);
			LOGGER.info(MessageFormat.format("getReverseWords: input = {0} and output = {1}", sentence, output));
			HttpHeaders headers = httpHeaders.getHeaders(output, "ReverseWords");
			LOGGER.debug(MessageFormat.format("Response headers for getReverseWords {0}", headers));
			entity = new ResponseEntity<>(output, headers, HttpStatus.OK);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage(), e);
			HttpStatus httpStatus = null;
			if (e.getErrorCode() == ValidationConstants.INPUT_STRING_IS_TOO_LONG) {
				httpStatus = HttpStatus.BANDWIDTH_LIMIT_EXCEEDED;
			}
			entity = new ResponseEntity<>(env.getProperty("error_" + e.getErrorCode()), httpStatus);
		}
		return entity;
	}
}