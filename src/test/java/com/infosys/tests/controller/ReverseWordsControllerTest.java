package com.infosys.tests.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.infosys.beans.Headers;
import com.infosys.controller.ReverseWordsController;
import com.infosys.exceptions.ValidationException;
import com.infosys.reversewords.service.ReverseWordsService;
import com.infosys.util.ValidationConstants;

@RunWith(SpringRunner.class)
@WebMvcTest(value=ReverseWordsController.class, secure=false)
public class ReverseWordsControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	Environment env;

	@MockBean
	private ReverseWordsService service;

	@MockBean
	private Headers headers;

	@Test
	public void testGetReverseWordsType() throws Exception{
		String sentence = "woh era uoy";
		HttpHeaders httpHeaders = new HttpHeaders();
		Mockito.when(headers.getHeaders(Mockito.anyObject(), Mockito.anyString())).thenReturn(httpHeaders);
		Mockito.when(service.getReverseWords(
				Mockito.anyString())).thenReturn(sentence);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/ReverseWords?sentence=\"how are you\"")
			    .accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				  .andReturn();
		Assert.assertEquals(result.getResponse().getContentAsString(), sentence);
		Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
	}

	@Test
	public void testStringIsTooLong() throws Exception{
		HttpHeaders httpHeaders = new HttpHeaders();
		Mockito.when(headers.getHeaders(Mockito.anyObject(), Mockito.anyString())).thenReturn(httpHeaders);
		Mockito.when(service.getReverseWords(
				Mockito.anyString())).thenThrow(
						new ValidationException(ValidationConstants.INPUT_STRING_IS_TOO_LONG));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/ReverseWords?sentence=\"how are you\"")
			    .accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				  .andReturn();
		Assert.assertEquals(result.getResponse().getContentAsString(), env.getProperty("error_3"));
		Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value());
	}
}
