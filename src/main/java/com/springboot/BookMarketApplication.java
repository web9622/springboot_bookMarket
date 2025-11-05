package com.springboot;

import com.springboot.domain.Member;
import com.springboot.domain.Role;
import com.springboot.repository.MemberRepository;
import com.springboot.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan; // 🚨 Import는 이미 되어 있음
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EnableJpaAuditing
@ComponentScan(basePackages = "com.springboot") // 👈 🚨 이 줄을 추가합니다. (최상위 패키지 스캔)
@SpringBootApplication
public class BookMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookMarketApplication.class, args);
	}

	@Bean
	public AuditingEntityListener auditingEntityListener() {
		return new AuditingEntityListener();
	}

	// 관리자 정보를 Member 엔티티에 등록
	@Bean
	public CommandLineRunner run(MemberService memberService, MemberRepository memberRepository,PasswordEncoder passwordEncoder) throws Exception{
		return (String[] args) -> {
			// [수정된 부분]: "Admin" ID가 이미 DB에 존재하는지 먼저 확인합니다.
			if(memberRepository.findByMemberId("Admin")==null){
				Member member = new Member();
				member.setMemberId("Admin");
				member.setName("관리자");
				member.setPhone("");
				member.setEmail("");
				member.setAddress("");
				String password=passwordEncoder.encode("Admin1234");
				member.setPassword(password);
				member.setRole(Role.ADMIN);
				memberService.saveMember(member);
			}
		};
	}
}