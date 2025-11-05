package com.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	// /login GET 요청 처리
	@GetMapping("/login")
	public String login(HttpServletRequest request) {
		return "login"; // 🚨 경로 수정: "member/login" -> "login"
	}

	@GetMapping("/loginfailed")
	public String loginerror(Model model) {
		model.addAttribute("error", "true");
		return "login"; // 🚨 경로 수정: "member/login" -> "login"
	}

	@GetMapping("/logout")
	public String logout(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "login"; // 🚨 경로 수정: "member/login" -> "login"
	}
}