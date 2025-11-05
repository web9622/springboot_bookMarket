package com.springboot.controller;

import com.springboot.domain.Member;
import com.springboot.domain.MemberFormDto;
import com.springboot.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/members")
public class MemberController {
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    // 신규 회원 등록 페이지 출력하기
    @GetMapping(value = "/add")
    public String requestAddMemberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/addMember";
    }

    // 신규 회원 등록하기
    @PostMapping(value = "/add")
    public String submitAddNewMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("memberFormDto", memberFormDto);
            return "member/addMember";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/addMember";
        }
        return "redirect:/login";
    }

    // 회원 정보 수정 페이지 출력하기
    @GetMapping(value = "/update/{memberId}")
    public String requestUpdateMemberForm(@PathVariable(name = "memberId") String memberId, Model model) {
        Member member = memberService.getMemberById(memberId);
        model.addAttribute("memberFormDto", member);
        return "member/updateMember";
    }


    // 회원 정보 수정하기
    @PostMapping(value = "/update")
    public String submitUpdateMember(@Valid MemberFormDto memberFormDto,
                                     BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/updateMember";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/addMember";
        }
        return "redirect:/members";
    }

    // 회원 정보 삭제하기
    @GetMapping(value = "/delete/{memberId}")
    public String deleteMember(@PathVariable(name = "memberId") String memberId) {
        memberService.deleteMember(memberId);
        return "redirect:/logout";
    }

    // 회원 가입 및 인증 시 인사말 페이지로 이동하기
    @GetMapping
    public String requestMain() {
        return "redirect:/";
    }

}//Controller
