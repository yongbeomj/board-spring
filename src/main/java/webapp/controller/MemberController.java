package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webapp.domain.dto.MemberDto;
import webapp.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;

@Controller
public class MemberController {

    @Autowired
    HttpServletRequest request; // 요청객체

    @Autowired
    MemberService memberService;

    // 회원가입
    @GetMapping("/member/signup")
    public String Signup(){
        return "member/signup";
    }

    @PostMapping("/member/signupcontroller")
    public String signupcontroller(MemberDto memberDto){
        // 1. 작성 데이터 가져온다. - form name과 dto가 동일하면 자동 주입
        // 2. 서비스에 dto 넘기기
        memberService.Signup(memberDto);
        return "redirect:/";

    }


    // 로그인
    @GetMapping("/member/login")
    public String Login(){
        return "member/login";
    }

    // 마이페이지
    @GetMapping("/member/memberinfo")
    public String Memberinfo(){
        return "member/memberinfo";
    }

}
