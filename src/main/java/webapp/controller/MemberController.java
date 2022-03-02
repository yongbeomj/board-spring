package webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    // 회원가입
    @GetMapping("/member/signup")
    public String Signup(){
        return "member/signup";
    }

    // 회원가입
    @GetMapping("/member/login")
    public String Login(){
        return "member/login";
    }

    // 회원가입
    @GetMapping("/member/memberinfo")
    public String Memberinfo(){
        return "member/memberinfo";
    }

}
