package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import webapp.domain.dto.BoardDto;
import webapp.domain.dto.MemberDto;
import webapp.domain.entity.board.BoardEntity;
import webapp.domain.entity.category.CategoryEntity;
import webapp.domain.entity.category.CategoryRepository;
import webapp.domain.entity.member.MemberEntity;
import webapp.domain.entity.member.MemberRepository;
import webapp.service.BoardService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Optional;

@Controller
public class BoardContorller {

    @Autowired
    BoardService boardService;

    @Autowired
    HttpServletRequest request;

    // boardlist1_1
    @GetMapping("/board1/boardlist1_1")
    public String boardlist1_1() {
        return "board1/boardlist1_1";
    }

    // boardwrite1_1
    @GetMapping("/board1/boardwrite1_1")
    public String boardwrite1_1() {
        return "board1/boardwrite1_1";
    }

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping("/board/boardwritecontroller")
    @ResponseBody
    public String boardwritecontroller(BoardDto boardDto) {
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("loginDto");
        System.out.println("mno : " + memberDto.getMno());
        int cano = Integer.parseInt( request.getParameter("cano"));
        System.out.println("cano : " + cano);
        Optional<MemberEntity> memberEntity = memberRepository.findById(memberDto.getMno());
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(cano);

        BoardEntity boardEntity = BoardEntity.builder()
                .btitle(request.getParameter("btitle"))
                .bcontents(request.getParameter("bcontents"))
                .memberEntity(memberEntity.get())
                .categoryEntity(categoryEntity.get())
                .build();
        boardService.boardwrite(boardEntity);

        // 리스트 페이지 호출
        return "redirect:/board1/boardlist1_1";
    }

    // boardview1_1
    @GetMapping("/board1/boardview1_1")
    public String boardview1_1() {
        return "board1/boardview1_1";
    }

    // boardupdate1_1
    @GetMapping("/board1/boardupdate1_1")
    public String boardupdate1_1() {
        return "board1/boardupdate1_1";
    }


}
