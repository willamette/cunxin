package org.cunxin.app.model.service;

public class User {
	public Integer id;
    public String mail;
    public String weibo;
    public String pwdMD5;
    
    public String firstName;
    public String lastName;
    public int age;
    public String gender;
    
    public boolean isDeleted = false;
    
    public boolean isValid() {
    	if (id != null && id > 0 && mail != null)
    		return true;
    	return false;
    }
}
