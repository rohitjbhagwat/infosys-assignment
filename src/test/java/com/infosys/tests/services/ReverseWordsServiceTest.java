package com.infosys.tests.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.infosys.exceptions.ValidationException;
import com.infosys.reversewords.service.ReverseWordsService;
import com.infosys.util.ValidationConstants;

@RunWith(SpringRunner.class)
@WebMvcTest(value=ReverseWordsService.class, secure=false)
public class ReverseWordsServiceTest {

	@Autowired
	private ReverseWordsService service;

	@Test
	public void testGetReverseWords() throws ValidationException{
		String sentence = "how are you?";
		String expectedOutput = "\"woh era ?uoy\"";
		String actualOutput = null;
		actualOutput = service.getReverseWords(sentence);
		Assert.assertEquals(actualOutput, expectedOutput);
	}

	@Test
	public void testLargeStringInput(){
		String sentence = "Spring does a lot of things. But underneath all of the fantastic functionality "+
				"it adds to enterprise development, its primary features are dependency injection "+
				"(DI) and aspect-oriented programming (AOP). "+
				"Starting in chapter 1, “Springing into action,” I’ll give you a quick overview of "+
				"the Spring Framework, including a quick overview of DI and AOP in Spring and "+
				"show how they help with decoupling application components.";
		try {
			service.getReverseWords(sentence);
			Assert.fail();
		} catch (ValidationException e) {
			Assert.assertEquals(
					ValidationConstants.INPUT_STRING_IS_TOO_LONG,
					e.getErrorCode());
		}
	}

	@Test
	public void testStringInputWithoutSpace() throws ValidationException{
		String sentence = "Springdoesalotofthings.Butunderneathallofthefantasticfunctionality";
		String expectedOutput = "\"ytilanoitcnufcitsatnafehtfollahtaenrednutuB.sgnihtfotolaseodgnirpS\"";
		String actualOutput = service.getReverseWords(sentence);
		Assert.assertEquals(actualOutput, expectedOutput);
	}

	@Test
	public void testNullStringInput() throws ValidationException{
		String sentence = null;
		String expectedOutput = null;
		String actualOutput = service.getReverseWords(sentence);
		Assert.assertEquals(actualOutput, expectedOutput);
	}
}
