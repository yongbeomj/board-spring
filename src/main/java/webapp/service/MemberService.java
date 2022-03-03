package webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.domain.dto.MemberDto;
import webapp.domain.entity.MemberRepository;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    // 회원가입
    public boolean Signup(MemberDto memberDto){
        // 컨트롤러에서 넘겨받은 데이터를 db에 저장
        memberRepository.save(memberDto.toentity());

        return true;
    }
}
