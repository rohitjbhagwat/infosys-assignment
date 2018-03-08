package com.infosys.reversewords.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.infosys.exceptions.ValidationException;
import com.infosys.util.ValidationConstants;

/**
 * An implementation of <code>ReverseWordsService.</code> and provides methods to reverse
 * each word in a given sentence which is separated by a space.
 *
 * @author Rohit
 */
@Service
public class ReverseWordsServiceImpl implements ReverseWordsService{

	/** LOGGER is used to log debug, info and error messages. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReverseWordsServiceImpl.class);

	/** Represents value of default.content_length from application.properties file.*/
	@Value("${default.content_length}")
	private String contentLength;

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
	@Override
	public String getReverseWords(String sentence) throws ValidationException {
		LOGGER.info("ReverseWordsServiceImpl.getReverseWords");
		if(sentence == null || sentence.isEmpty()){
			return sentence;
		}
		if(sentence.length() > Integer.parseInt(contentLength)){
			throw new ValidationException(
					ValidationConstants.INPUT_STRING_IS_TOO_LONG);
		}
		String[] inputSplit = sentence.split(" ");
		StringBuilder temp = new StringBuilder();
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < inputSplit.length; i++){
			output.append(temp.append(inputSplit[i]).reverse()+" ");
			temp.setLength(0);
		}
		return new StringBuilder("\"").append(output.toString().trim()).append("\"").toString();
	}
}