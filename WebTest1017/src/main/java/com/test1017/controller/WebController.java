package com.test1017.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

	@GetMapping("/")
	public String getMethodName() {
		return "redirect:/posts";
	}
	
}
