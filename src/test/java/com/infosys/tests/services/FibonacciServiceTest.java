package com.infosys.tests.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.infosys.exceptions.ValidationException;
import com.infosys.fibonacci.service.FibonacciService;
import com.infosys.util.ValidationConstants;

@RunWith(SpringRunner.class)
@WebMvcTest(value = FibonacciService.class, secure = false)
public class FibonacciServiceTest {

	@Autowired
	private FibonacciService service;

	@Test
	public void testFibonacciNumber() throws ValidationException{
		String output = service.getFibonacciNumber(10);
		Assert.assertEquals("55", output);
		output = service.getFibonacciNumber(0);
		Assert.assertEquals("0", output);
	}

	@Test
	public void testInvalidFormatInputString() {
		try {
			service.getFibonacciNumber(-1);
			Assert.fail();
		} catch (ValidationException e) {
			Assert.assertEquals(ValidationConstants.NUMBER_LESS_THAN_ZERO, e.getErrorCode());
		}
	}

	@Test
	public void testLargeNumber() {
		try {
			service.getFibonacciNumber(100000);
			Assert.fail();
		} catch (ValidationException e) {
			Assert.assertEquals(ValidationConstants.NUMBER_IS_TOO_LARGE, e.getErrorCode());
		}
	}
}
