package com.infosys.reversewords.service;

import com.infosys.exceptions.ValidationException;

/**
 * ReverseWordsService is an interface and provides methods to reverse each word in a given sentence
 * which is separated by a space.
 *
 * @author Rohit
 */
public interface ReverseWordsService {

	/**
	 * getReverseWords reverses each word in the given sentence. A sentence should have a space to
	 * consider the next sequence of characters as word.
	 *
	 * Sample input: how are you?
	 * Sample output: woh era ?uoy
	 *
	 * @param sentence An input sentence whose words need to be reversed.
	 * @return Sentence containing reversed words.
	 * @throws ValidationException Exception is raised if the output string length
	 * is larger than the configured value in application.properties file.
	 */
	public String getReverseWords(String sentence) throws ValidationException;
}