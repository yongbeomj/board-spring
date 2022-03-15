package webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.domain.dto.MemberDto;
import webapp.domain.entity.category.CategoryRepository;
import webapp.domain.entity.member.MemberEntity;
import webapp.domain.entity.member.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    HttpServletRequest request;

    // 회원가입
    public boolean signup(MemberDto memberDto) {
        // 컨트롤러에서 넘겨받은 데이터를 db에 저장
        memberRepository.save(memberDto.toentity());
        return true;
    }

    // 로그인
    public boolean login(String mid, String mpw) {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        for (MemberEntity memberEntity : memberEntityList) {
            if (memberEntity.getMid().equals(mid) && memberEntity.getMpw().equals(mpw)) {
                MemberDto memberDto = MemberDto.builder()
                        .mno(memberEntity.getMno())
                        .mid(memberEntity.getMid())
                        .mpw(memberEntity.getMpw())
                        .build();
                HttpSession session = request.getSession();
                session.setAttribute("logindto", memberDto);
                return true;
            }
        }
        return false;
    }


}
