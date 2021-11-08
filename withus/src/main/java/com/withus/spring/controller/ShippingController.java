package com.withus.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.withus.spring.service.ShippingService;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/**")
public class ShippingController {
	
	@Autowired
	ShippingService shippingService;
	
	@RequestMapping("/shippingForm/**")
	public String shippingForm() {
		return "main/popup/shippingForm";
	}
	
	@RequestMapping("/deliverForm/**")
	public String deleverForm(Model model) {
		System.out.println("deliverForm 확인용 접속");
		model.addAttribute("list", shippingService.selectByShp());
		System.out.println(model.toString());
		return "main/deliverForm";
	}
	
}
