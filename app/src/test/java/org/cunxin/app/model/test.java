package org.cunxin.app.model;


import org.cunxin.app.model.service.User;
import org.cunxin.app.util.JsonConverter;
import org.testng.annotations.Test;

@Test
//There is a Test annotation, don't need main again
public class test {

	public void testSetUp(){
		User user = new User();
		user.age =13;
		user.firstName = "fuck";
		String s = JsonConverter.toJson(user);
		System.out.println(s);
	}

}
