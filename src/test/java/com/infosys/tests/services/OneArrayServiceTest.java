package com.infosys.tests.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.infosys.exceptions.ValidationException;
import com.infosys.onearray.beans.OneArrayBean;
import com.infosys.onearray.service.OneArrayService;
import com.infosys.util.ValidationConstants;

@RunWith(SpringRunner.class)
@WebMvcTest(value = OneArrayService.class, secure = false)
public class OneArrayServiceTest {

	@Autowired
	private OneArrayService service;

	@Test
	public void testConvertToOneArray() throws ValidationException {
		String input = "{" + "\"Array1\":[1,2,3,4,5]," + "\"Array2\":[9,7,8,6,4]," + "\"Array\":[1,3,5,7,10]" + "}";
		int expectedSize = 10;
		OneArrayBean output = null;
		output = service.convertToOneArray(input);
		Assert.assertNotNull(output.getArray());
		Assert.assertEquals(output.getArray().length, expectedSize);
		Assert.assertEquals(output.getArray()[0], new Long(1));
		Assert.assertEquals(output.getArray()[expectedSize - 1], new Long(10));
	}

	@Test
	public void testInvalidFormatInputString() {
		String invalidInput = "{" + "\"Array1\":1,2,3,4,5," + "\"Array2\":[9,7,8,6,4]," + "\"Array\":[1,3,5,7,10]"
				+ "}";
		try {
			service.convertToOneArray(invalidInput);
			Assert.fail();
		} catch (ValidationException e) {
			Assert.assertEquals(ValidationConstants.INCORRECT_FORMAT_OF_INPUT_STRING, e.getErrorCode());
		}
	}

	@Test
	public void testContentLength() {
		String invalidInput = "{"
				+ "\"Array1\":[123243,253234,34532342,44563423,56344234,6533342,7523423,852342,93521,103453453,1135345,12353453,1352623,15632435,163231231,123123,12],"
				+ "\"Array2\":[9232,2424723423,234282342,2342346,234234],"
				+ "\"Array\":[2342321,234234233,24242345,2423427,15335340]" + "}";
		try {
			service.convertToOneArray(invalidInput);
			Assert.fail();
		} catch (ValidationException e) {
			Assert.assertEquals(ValidationConstants.INPUT_STRING_IS_TOO_LONG, e.getErrorCode());
		}
	}
}
