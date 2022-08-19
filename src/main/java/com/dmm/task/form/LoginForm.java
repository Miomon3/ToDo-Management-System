package com.dmm.task.form;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginForm {
	
	@Size(min = 1, max = 200)
	private String userName;
	
	@Size(min = 1, max = 200)
	private String password;
	
}
