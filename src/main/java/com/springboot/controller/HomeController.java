package com.springboot.controller;

import com.springboot.domain.Member;
import com.springboot.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @Autowired
    private MemberService memberService;

    // 🚨 404 오류 해결: 가장 간단한 루트 매핑만 남깁니다.
    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    // 이전에 로그인 정보를 세션에 저장하던 로직은 LoginSuccessHandler로 옮겨졌습니다.
    // 이 메소드만 남겨 충돌을 피합니다.
}