package com.infosys.onearray.service;

import com.infosys.exceptions.ValidationException;
import com.infosys.onearray.beans.OneArrayBean;

/**
 * OneArrayService is an interface and provides methods to merge arrays into a single
 * array containing unique and sorted elements.
 *
 * @author Rohit
 */
public interface OneArrayService {

	/**
	 * convertToOneArray merges arrays passed in JSON format as input and returns
	 * an array which contains numbers which are unique and in sorted order.
	 *
	 * Sample input:
	 * {
			"Array1":[1,2,3,4,5],
			"Array2":[9,7,8,6,4],
			"Array":[1,3,5,7,10]
		}
	 *
	 * Sample output:
	 * "Arrays":[1,2,3,4,5,6,7,8,9,10]
	 *
	 * @param arrays Input array containing numbers
	 * @return An instance of OneArrayBean containing the merged array.
	 * @throws ValidationException Exception incase the input array is not as per the specification.
	 */
	public OneArrayBean convertToOneArray(String arrays)
			throws ValidationException;
}
