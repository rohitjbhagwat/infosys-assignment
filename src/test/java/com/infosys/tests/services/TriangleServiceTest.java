package com.infosys.tests.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.infosys.exceptions.ValidationException;
import com.infosys.triangle.service.TriangleService;
import com.infosys.util.ValidationConstants;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TriangleService.class, secure = false)
public class TriangleServiceTest {

	@Autowired
	private TriangleService service;

	@Test
	public void testGetTriangleType() throws ValidationException {
		String triangleType = null;
		triangleType = service.getTriangleType(1, 2, 3);
		Assert.assertEquals("\"Scalene\"", triangleType);
		triangleType = service.getTriangleType(1, 2, 1);
		Assert.assertEquals("\"Isosceles\"", triangleType);
		triangleType = service.getTriangleType(1, 1, 3);
		Assert.assertEquals("\"Isosceles\"", triangleType);
		triangleType = service.getTriangleType(1, 3, 3);
		Assert.assertEquals("\"Isosceles\"", triangleType);
		triangleType = service.getTriangleType(1, 1, 1);
		Assert.assertEquals("\"Equilateral\"", triangleType);
	}

	@Test
	public void testInvalidTriangleCoordinates() {
		try {
			service.getTriangleType(0, 0, 0);
			Assert.fail();
		} catch (ValidationException e) {
			Assert.assertEquals(ValidationConstants.INVALID_TRIANGLE_COORDINATES, e.getErrorCode());
		}
	}
}
