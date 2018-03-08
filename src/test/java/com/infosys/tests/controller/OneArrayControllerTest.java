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
import com.infosys.controller.OneArrayController;
import com.infosys.exceptions.ValidationException;
import com.infosys.onearray.beans.OneArrayBean;
import com.infosys.onearray.service.OneArrayService;
import com.infosys.util.ValidationConstants;

@RunWith(SpringRunner.class)
@WebMvcTest(value=OneArrayController.class, secure=false)
public class OneArrayControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	Environment env;

	@MockBean
	private OneArrayService service;

	@MockBean
	private Headers headers;

	@Test
	public void testMakeOneArray() throws Exception{
		String input = "{ \"Array1\":[1,2,3],\"Array2\":[3,4,5],\"Array3\":[5,8,9] }";
		String output = "{\"Array\":[1,2,3,4,5]}";
		OneArrayBean bean = new OneArrayBean();
		bean.setArray(new Long[]{1l,2l,3l,4l,5l});
		HttpHeaders httpHeaders = new HttpHeaders();
		Mockito.when(headers.getHeaders(Mockito.anyObject(), Mockito.anyString())).thenReturn(httpHeaders);
		Mockito.when(service.convertToOneArray(Mockito.anyString())).thenReturn(bean);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/makeonearray")
				.content(input)
			    .accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				  .andReturn();
		Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
		Assert.assertEquals(result.getResponse().getContentAsString(), output);

	}

	@Test
	public void testIncorrectFormatOfInputString() throws Exception{
		String input = "{ \"Array1\":[1,2,3],\"Array2\":[3,4,5],\"Array3\":[5,8,9] }";
		HttpHeaders httpHeaders = new HttpHeaders();
		Mockito.when(headers.getHeaders(Mockito.anyObject(), Mockito.anyString())).thenReturn(httpHeaders);
		Mockito.when(service.convertToOneArray(
				Mockito.anyString())).thenThrow(
						new ValidationException(ValidationConstants.INCORRECT_FORMAT_OF_INPUT_STRING));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/makeonearray")
				.content(input)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				  .andReturn();
		Assert.assertEquals(result.getResponse().getContentAsString(), env.getProperty("error_5"));
		Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
	}
}
