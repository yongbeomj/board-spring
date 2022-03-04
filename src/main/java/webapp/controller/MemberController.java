package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webapp.domain.dto.MemberDto;
import webapp.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;

@Controller
public class MemberController {

    @Autowired
    HttpServletRequest request; // 요청객체

    @Autowired
    MemberService memberService;

    // 회원가입
    @GetMapping("/member/signup")
    public String signup() {
        return "member/signup";
    }

    @PostMapping("/member/signupController")
    public String signupController(MemberDto memberDto) {
        // 1. 작성 데이터 가져온다. - form name과 dto가 동일하면 자동 주입
        // 2. 서비스에 dto 넘기기
        memberService.signup(memberDto);
        System.out.println("회원가입 성공");
        return "redirect:/";

    }

    // 로그인
    @GetMapping("/member/login")
    public String Login() {
        return "member/login";
    }

    @PostMapping("/member/loginController")
    public String loginController(MemberDto memberDto) {
        // 1. 서비스에 dto넘기고 결과값 dto에 저장
        MemberDto loginDto = memberService.login(memberDto);
        // 2. 받아온 정보를 세션에 저장
        if (loginDto != null) {
            HttpSession session = request.getSession(); // 세션 가져오기
            session.setAttribute("loginDto", loginDto); // 세션 설정
            System.out.println("로그인 성공");
            return "redirect:/";
        }
        return null;
    }


    // 마이페이지
    @GetMapping("/member/memberinfo")
    public String Memberinfo() {
        return "member/memberinfo";
    }

}
