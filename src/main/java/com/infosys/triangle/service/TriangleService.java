package com.infosys.triangle.service;

import com.infosys.exceptions.ValidationException;

/**
 * TriangleService is an interface and provides methods to get triangle type based on the
 * coordinates provided.
 *
 * @author Rohit
 */
public interface TriangleService {

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
	public String getTriangleType(int length, int breadth, int height)
			throws ValidationException;
}
