package com.infosys.onearray.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents output bean for <code>OneArrayController</code>
 *
 * @author Rohit
 */
public class OneArrayBean {

	/** array contains the sorted numbers without duplicates. */
	@JsonProperty("Array")
	private Long[] array;


	/**
	 *
	 * Getter method.
	 *
	 * @return array - Array of Long numbers.
	 */
	public Long[] getArray() {
		return array;
	}

	/**
	 * Setter method.
	 *
	 * @param array - Array of Long numbers.
	 */
	public void setArray(Long[] array) {
		this.array = array;
	}
}