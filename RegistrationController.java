package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import member.MemberRegistRequest;
import member.MemberService;

@Controller
@RequestMapping("/member/regist")
public class RegistrationController {
private static final String MEMBER_REGISTRATION_FORM = "/member/registrationForm";
MemberService memberService;

//@RequestMapping(method = RequestMethod.Get)
@GetMapping
public String form(@ModelAttribute("memberInfo") MemberRegistRequest memRegReq) {
	return MEMBER_REGISTRATION_FORM;
}
	
@PostMapping
public String regist(@ModelAttribute("memberInfo") MemberRegistRequest memRegReq) {
	memberService.registNewMember(memRegReq);
	return "/member/registerd";
}

public void setMemberService(MemberService memberService) {
	this.memberService = memberService;
}


	
}
