package org.cunxin.app.model;



public class test {
	static public void main(String[] args) {
		setUp();
	}

	static protected void setUp(){
		User user = new User();
		user.age =13;
		user.firstName = "fuck";
		String s = JsonConverter.toJson(user);
		System.out.println(s);
	}

}
