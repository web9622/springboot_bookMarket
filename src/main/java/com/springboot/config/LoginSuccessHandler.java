package com.springboot.config;

import com.springboot.domain.Member;
import com.springboot.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    public LoginSuccessHandler(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String username = authentication.getName();
        Member member = memberRepository.findByMemberId(username);

        System.out.println("✅ 로그인 성공 - memberId: " + (member != null ? member.getMemberId() : "NULL"));

        // 🔹 세션에 로그인 정보 저장
        HttpSession session = request.getSession();
        session.setAttribute("userLoginInfo", member);

        System.out.println("✅ 세션에 저장된 값: " + session.getAttribute("userLoginInfo"));

        // 로그인 성공 후 이동 경로 지정
        response.sendRedirect("/BookMarket/books");
    }
}
