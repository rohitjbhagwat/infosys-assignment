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
import com.infosys.triangle.service.TriangleService;
import com.infosys.util.ValidationConstants;

/**
 * Controller class to identify triangle type based on the coordinates provided.
 *
 * @author Rohit
 */
@RestController
public class TriangleController {

	/** LOGGER is used to log debug, info and error messages. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TriangleController.class);

	/** Represents an instance of <code>TriangleServiceImpl</code>. */
	@Autowired
	private TriangleService service;

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
	 * getTriangleType identifies a triangle based on the co-ordinates provided.
	 * Return values are one of the following
	 * 1. "Equilateral"
	 * 2. "Isosceles"
	 * 3. "Scalene".
	 *
	 * URI :/api/TriangleType
	 * Method: GET
	 *
	 * @param length Represents one side of triangle
	 * @param breadth Represents one side of triangle
	 * @param height Represents one side of triangle
	 * @return entity - Instance of <code>ResponseEntity</code> containing the
	 *         triangle type.
	 */
	@RequestMapping(value="/api/TriangleType", method = RequestMethod.GET)
	public ResponseEntity<String> getTriangleType(@RequestParam("a") int length,
			@RequestParam("b") int breadth,
			@RequestParam("c") int height){
		ResponseEntity<String> entity = null;
		try {
			String output = service.getTriangleType(length, breadth, height);
			LOGGER.info(MessageFormat.format(
					"getTriangleType input=(a={0},b={1},c={2}) and output={3}",
							length,breadth,height,output));
			HttpHeaders headers = httpHeaders.getHeaders(output, "Triangle");
			LOGGER.debug(MessageFormat.format("Response headers for getTriangleType = {0}", headers));
			entity = new ResponseEntity<>(output, headers, HttpStatus.OK);
		}catch(ValidationException e){
			LOGGER.error(e.getMessage(), e);
			HttpStatus httpStatus = null;
			if(e.getErrorCode() == ValidationConstants.INVALID_TRIANGLE_COORDINATES){
				httpStatus = HttpStatus.BAD_REQUEST;
			}
			entity = new ResponseEntity<>(
					env.getProperty("error_"+e.getErrorCode()),httpStatus);
		}
		return entity;
	}
}