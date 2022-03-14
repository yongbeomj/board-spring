package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        return "redirect:/";
    }

    // 로그인
    @GetMapping("/member/login")
    public String Login() {
        return "member/login";
    }

    @PostMapping("/member/logincontroller")
    public String logincontroller(@RequestParam("mid") String mid, @RequestParam("mpw") String mpw) {
        boolean result = memberService.login(mid, mpw);
        if (result) {
            return "redirect:/";
//            return "1";
        } else {
            return "redirect:/member/login";
        }
    }

    // 마이페이지
    @GetMapping("/member/info")
    public String Memberinfo() {
        return "member/info";
    }

    // 로그아웃 처리
    @GetMapping("/member/logout")
    public String logout() {
        HttpSession session = request.getSession();
        session.setAttribute("logindto", null);   // 기존 세션을 null 로 변경
        return "redirect:/"; // 로그아웃 성공시 메인페이지로 이동
    }

}
