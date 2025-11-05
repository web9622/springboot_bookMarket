package com.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig{

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				.csrf(AbstractHttpConfigurer::disable)

				.authorizeHttpRequests(
						authorizeRequests -> authorizeRequests
								.requestMatchers("/books/add").hasRole("ADMIN" )
								.requestMatchers("/order/list").hasRole("ADMIN" )

								// 🚨 로그인/회원가입 경로 및 정적 리소스 경로를 명시적으로 허용하여 404 오류 해결
								.requestMatchers("/login", "/loginfailed", "/members/add").permitAll()
								.requestMatchers("/css/**", "/js/**", "/images/**", "/upload/**").permitAll()

								.anyRequest().permitAll()
				)
				.formLogin(
						formLogin->formLogin

								.loginPage("/login") // 로그인 폼을 보여줄 Controller 경로
								.loginProcessingUrl("/perform_login") // 폼 제출 처리 URL (login.html action과 일치)
								.successHandler(loginSuccessHandler) // 로그인 성공 처리 핸들러
								.failureUrl("/loginfailed")
								.usernameParameter("username")
								.passwordParameter("password")

				)

				.logout(
						logout -> logout
								.logoutUrl("/logout")
								.logoutSuccessUrl("/login")
				);

		return http.build();

	}
}