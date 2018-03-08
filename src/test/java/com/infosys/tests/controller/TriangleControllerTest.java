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
import com.infosys.controller.TriangleController;
import com.infosys.exceptions.ValidationException;
import com.infosys.triangle.service.TriangleService;
import com.infosys.util.ValidationConstants;

@RunWith(SpringRunner.class)
@WebMvcTest(value=TriangleController.class, secure=false)
public class TriangleControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	Environment env;

	@MockBean
	private TriangleService service;

	@MockBean
	private Headers headers;

	@Test
	public void testGetTriangleType() throws Exception{
		String triangleType = "Scalene";
		HttpHeaders httpHeaders = new HttpHeaders();
		Mockito.when(headers.getHeaders(Mockito.anyObject(), Mockito.anyString())).thenReturn(httpHeaders);
		Mockito.when(service.getTriangleType(
				Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(triangleType);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/TriangleType?a=1&b=1&c=3")
			    .accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				  .andReturn();
		Assert.assertEquals(result.getResponse().getContentAsString(), triangleType);
	}

	@Test
	public void testInvalidTriangleType() throws Exception{
		HttpHeaders httpHeaders = new HttpHeaders();
		Mockito.when(headers.getHeaders(Mockito.anyObject(), Mockito.anyString())).thenReturn(httpHeaders);
		Mockito.when(service.getTriangleType(
				Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenThrow(
						new ValidationException(ValidationConstants.INVALID_TRIANGLE_COORDINATES));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/TriangleType?a=1&b=1&c=0")
			    .accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				  .andReturn();
		Assert.assertEquals(result.getResponse().getContentAsString(), env.getProperty("error_4"));
		Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
	}
}
