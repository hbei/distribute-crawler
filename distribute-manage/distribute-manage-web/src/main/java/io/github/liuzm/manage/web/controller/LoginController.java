package io.github.liuzm.manage.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {
	
	@RequestMapping("login")
	public String login(){
		return "manage/login";
	}
	
	@RequestMapping("doLogin")
	public String doLogin(){
		return "manage/home";
	}
}
