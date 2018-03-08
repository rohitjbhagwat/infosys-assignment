package com.infosys.beans;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.onearray.beans.OneArrayBean;

/**
 * Headers is a wrapper for setting <code>HttpHeaders</code>
 *
 * @author Rohit
 */
@Component
public class Headers {

	/** LOGGER is used to log debug, info and error messages. */
	private static final Logger LOGGER = LoggerFactory.getLogger(Headers.class);

	/** responseHeaders represents Headers to be set in the HttpResponse. */
	private HttpHeaders responseHeaders = new HttpHeaders();

	/** encoding represents content-encoding response header. Possible values are gzip, deflate, br.
	 * Value is read from application.properties file.
	 */
	@Value("${gzip.encoding}")
	private String encoding;

	/** fibonacciContentLength represents content-length response header for Fibonacci response.
	 * Value is read from application.properties file.
	 */
	@Value("${fibonacci.content_length}")
	private String fibonacciContentLength;

	/** defaultContentLength represents content-length response header for other responses.
	 * Value is read from application.properties file.
	 */
	@Value("${default.content_length}")
	private String defaultContentLength;

	/**
	 * setDefaultHeaders method is called when Spring initiates this bean.
	 * Default headers like pragma, date, content-encoding, vary etc are configured in this method.
	 */
	@PostConstruct
	public void setDefaultHeaders(){
		LOGGER.info("Headers.setDefaultHeaders()");
		responseHeaders.add("Pragma", "no-cache");
		Calendar now  = Calendar.getInstance();
		DateFormat dateFormat  = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		responseHeaders.set("Date", dateFormat.format(now.getTime()));
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(MessageFormat.format("encoding={0}",encoding));
		}
		if(encoding != null && !encoding.isEmpty()){
			responseHeaders.set("Content-Encoding",encoding);
			List<String> varyHeaders = new ArrayList<>();
			varyHeaders.add("Accept-Encoding");
			responseHeaders.setVary(varyHeaders);
		}
		responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		responseHeaders.setCacheControl("no-cache");
		responseHeaders.setExpires(-1);
	}

	/**
	 * getHeaders method returns the <code>HttpHeaders</code> set by setDefaultHeaders method.
	 * It calculates the content-length of the response based on the output parameter. If the content length
	 * in the output is less than the max content length configured in the application.properties then the actual
	 * content length is set. Else max content length is set.
	 *
	 * @param object An object of either <code>OneArrayBean</code> or <code>String</code> containing the output.
	 * @param function represents the name of the function like "Fibonacci","ReverseWords" or "OneArray".
	 * @return httpHeaders - An instance of <code>HttpHeaders</code>
	 */
	public HttpHeaders getHeaders(Object output, String function){
		LOGGER.info("Headers.getHeaders method");
		HttpHeaders headersCopy = new HttpHeaders();
		headersCopy.putAll(responseHeaders);
		int contentLength = 0;
		if(output instanceof OneArrayBean){
			ObjectMapper mapper = new ObjectMapper();
			String defaultJson = null;
			contentLength = Integer.valueOf(defaultContentLength);
			try {
				defaultJson = mapper.writeValueAsString(output);
				if(defaultJson.length() < Integer.valueOf(defaultContentLength)){
					contentLength = defaultJson.length();
				}
			} catch (JsonProcessingException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}else {
			if("Fibonacci".equalsIgnoreCase(function)){
				contentLength = Integer.valueOf(fibonacciContentLength);
			}else {
				contentLength = Integer.valueOf(defaultContentLength);
			}
			if(output.toString().length() < contentLength){
				contentLength = output.toString().length();
			}
		}
		headersCopy.setContentLength(contentLength);
		return headersCopy;
	}
}