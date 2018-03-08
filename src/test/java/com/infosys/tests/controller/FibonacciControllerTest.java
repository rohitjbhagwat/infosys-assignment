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
import com.infosys.controller.FibonacciController;
import com.infosys.exceptions.ValidationException;
import com.infosys.fibonacci.service.FibonacciService;
import com.infosys.util.ValidationConstants;

@RunWith(SpringRunner.class)
@WebMvcTest(value=FibonacciController.class, secure=false)
public class FibonacciControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	Environment env;

	@MockBean
	private FibonacciService service;

	@MockBean
	private Headers headers;

	@Test
	public void testGetFibonacciNumber() throws Exception{
		String output = "1234";
//		headers.add("Content-Type", "application/json");
		Mockito.when(service.getFibonacciNumber(
				Mockito.anyInt())).thenReturn(output);
		HttpHeaders httpHeaders = new HttpHeaders();
		Mockito.when(headers.getHeaders(Mockito.anyObject(), Mockito.anyString())).thenReturn(httpHeaders);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/Fibonacci?n=10")
			    .accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				  .andReturn();
		Assert.assertEquals(result.getResponse().getContentAsString(), output);
	}

	@Test
	public void testNegativeFibonacciNumber() throws Exception{
		HttpHeaders headers = Mockito.mock(HttpHeaders.class);
		headers.add("Content-Type", "application/json");
		Mockito.when(service.getFibonacciNumber(
				Mockito.anyInt())).thenThrow(
						new ValidationException(ValidationConstants.NUMBER_LESS_THAN_ZERO));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/Fibonacci?n=-1")
			    .accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				  .andReturn();
		Assert.assertEquals(result.getResponse().getContentAsString(), env.getProperty("error_1"));
		Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void testLargeFibonacciNumber() throws Exception {
		HttpHeaders headers = Mockito.mock(HttpHeaders.class);
		headers.add("Content-Type", "application/json");
		Mockito.when(service.getFibonacciNumber(
				Mockito.anyInt())).thenThrow(
						new ValidationException(ValidationConstants.NUMBER_IS_TOO_LARGE));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/Fibonacci?n=10000000")
			    .accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				  .andReturn();
		Assert.assertEquals(result.getResponse().getContentAsString(), env.getProperty("error_2"));
		Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value());
	}
}
