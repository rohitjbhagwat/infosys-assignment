package com.infosys.triangle.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.infosys.exceptions.ValidationException;
import com.infosys.util.ValidationConstants;

/**
 * TriangleServiceImpl is an implementation of <code>TriangleService</code> interface and
 * provides methods to get triangle type based on the coordinates provided.
 *
 * @author Rohit
 */
@Service
public class TriangleServiceImpl implements TriangleService{

	/** LOGGER is used to log debug, info and error messages. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TriangleServiceImpl.class);

	/**
	 * getTriangleType method finds the triangle type based on co-ordinates.
	 * 1. If length, breadth and height have same values then the method returns "Equilateral".
	 * 2. If length & breadth or breadth & height have same values then the method returns "Isosceles".
	 * 3. If length, breadth and height have different values then the method returns "Scalene".
	 *
	 * @param length Represents one side of triangle.
	 * @param breadth Represents one side of triangle.
	 * @param height Represents one side of triangle.
	 * @return Either "Equilateral","Isosceles" or "Scalene" based on co-ordinates.
	 * @throws ValidationException Exception if invalid coordinates are provided.
	 */
	@Override
	public String getTriangleType(int length, int breadth, int height)
											throws ValidationException {
		LOGGER.info("TriangleServiceImpl.getTriangleType");
		String triangleType = null;
		if(length == 0 || breadth == 0 || height == 0){
			throw new ValidationException(
					ValidationConstants.INVALID_TRIANGLE_COORDINATES);
		}
		if((length == breadth) && (breadth == height)){
			triangleType = "\"Equilateral\"";
		}else if ((length == breadth) || (length == height) || (breadth == height)){
			triangleType = "\"Isosceles\"";
		}else {
			triangleType = "\"Scalene\"";
		}
		return triangleType;
	}
}