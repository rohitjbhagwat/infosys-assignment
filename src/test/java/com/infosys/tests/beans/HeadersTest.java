package com.infosys.tests.beans;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.beans.Headers;
import com.infosys.onearray.beans.OneArrayBean;

@RunWith(SpringRunner.class)
@WebMvcTest(value=Headers.class, secure=false)
public class HeadersTest {

	@Autowired
	Environment env;

	@Autowired
	private Headers headersBean;

	@Test
	public void testDefaultHeaders() throws Exception{
		HttpHeaders httpHeaders = headersBean.getHeaders(new Object(), "");
		Assert.assertTrue(httpHeaders.getContentLength() > 0);
		Assert.assertEquals("no-cache",httpHeaders.getPragma());
		Assert.assertTrue(httpHeaders.getDate() > 0);
		Assert.assertNotNull(httpHeaders.get("Content-Encoding"));
		Assert.assertTrue(httpHeaders.getVary().size() > 0);
		Assert.assertEquals("no-cache",httpHeaders.getCacheControl());
		Assert.assertEquals(MediaType.APPLICATION_JSON_UTF8_VALUE.toString(),httpHeaders.getContentType().toString());
		Assert.assertTrue(httpHeaders.getExpires() <=0 );
	}

	@Test
	public void testGetHeaders() throws Exception{
		OneArrayBean bean = new OneArrayBean();
		Long[] numbers = new Long[]{1L,2L,3L,4L,5L};
		bean.setArray(numbers);
		ObjectMapper mapper = new ObjectMapper();
		String output = mapper.writeValueAsString(bean);
		HttpHeaders httpHeaders = headersBean.getHeaders(bean, "");
		Assert.assertEquals(output.length(),httpHeaders.getContentLength());

		String input = "1234";
		httpHeaders = headersBean.getHeaders(input, "Fibonacci");
		Assert.assertEquals(input.length(),httpHeaders.getContentLength());
		String longInput = "1234237273472937492374982734982734892734927934782934"
				+ "239847923748927348927348279472893478273498273497293482847"
				+ "827348972849728347924782734927349723894728374982749827349827";
		httpHeaders = headersBean.getHeaders(longInput, "Fibonacci");
		Assert.assertEquals(Long.valueOf(env.getProperty("fibonacci.content_length")).longValue(),
											httpHeaders.getContentLength());
		httpHeaders = headersBean.getHeaders(input, "");
		Assert.assertEquals(input.length(),httpHeaders.getContentLength());
		httpHeaders = headersBean.getHeaders(longInput, "");
		Assert.assertEquals(Long.valueOf(env.getProperty("default.content_length")).longValue(),
											httpHeaders.getContentLength());
	}
}
