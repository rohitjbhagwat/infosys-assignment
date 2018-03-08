package com.infosys.onearray.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.exceptions.ValidationException;
import com.infosys.onearray.beans.OneArrayBean;
import com.infosys.util.ValidationConstants;

/**
 * OneArrayServiceImpl is an implementation of OneArrayService and provides
 * methods to merge and sort number array.
 *
 * @author Rohit
 */
@Service
public class OneArrayServiceImpl implements OneArrayService{

	/** LOGGER is used to log debug, info and error messages. */
	private static final Logger LOGGER = LoggerFactory.getLogger(OneArrayServiceImpl.class);

	/** Represents value of default.content_length from application.properties file.*/
	@Value("${default.content_length}")
	private String contentLength;

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
	@Override
	public OneArrayBean convertToOneArray(final String  array)
											throws ValidationException {
		LOGGER.info("OneArrayServiceImpl.convertToOneArray");
		List<long[]> arrayList = parseInput(array);
		final Set<Long> set = new TreeSet<>();
		arrayList.stream().forEach(i -> {
			List<Long> l = Arrays.stream(i).boxed().collect(Collectors.toList());
			set.addAll(l);
		});
		OneArrayBean outputBean = convertToBean(set);
		ObjectMapper mapper = new ObjectMapper();
		String jsonOutput = null;
		try {
			jsonOutput = mapper.writeValueAsString(outputBean);
			if(jsonOutput.length() > Integer.valueOf(contentLength)){
				throw new ValidationException(
						ValidationConstants.INPUT_STRING_IS_TOO_LONG);
			}
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return outputBean;
	}

	private OneArrayBean convertToBean(Set<Long> set){
		OneArrayBean bean = new OneArrayBean();
		bean.setArray(set.toArray(new Long[set.size()]));
		return bean;
	}

	private List<long[]> parseInput(final String body) throws ValidationException{
		List<long[]> list = new ArrayList<>();
		try{
			int start = body.indexOf('{');
			int end = body.indexOf('}');
			String modifiedBody = body.substring(start+1, end);
			LOGGER.debug(MessageFormat.format("parseInput body={0}",modifiedBody));
			StringTokenizer tokens = new StringTokenizer(modifiedBody, "]");
			while(tokens.hasMoreTokens()){
				String array = tokens.nextToken();
				array = array.replace ("[", "");
				array = array.replace ("]", "");
				LOGGER.debug(MessageFormat.format("parseInput array={0}",array));
				if(array.indexOf(':') != -1){
					array = array.substring(array.indexOf(':')+1);
					String[] vals = array.split (",");
					long[] a = new long[vals.length];
					for(int i = 0; i < vals.length; i++){
						a[i] = Long.parseLong(vals[i]);
					}
					list.add(a);
				}
			}
		} catch(Exception e){
			LOGGER.error("parseInput() ",e);
			throw new ValidationException(
					ValidationConstants.INCORRECT_FORMAT_OF_INPUT_STRING);
		}
		return list;
	}
}