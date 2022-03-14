package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BoardContorller {

    @Autowired
    BoardService boardService;

    @Autowired
    HttpServletRequest request;

    // boardlist1
    @GetMapping("/board1/boardlist")
    public String boardlist(Model model) {
        ArrayList<BoardDto> boardDtos = boardService.boardlist();
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        model.addAttribute("memberDto",memberDto);
        model.addAttribute("boardDtos",boardDtos);
        return "board1/boardlist";
    }

    // boardwrite1
    @GetMapping("/board1/boardwrite")
    public String boardwrite1() {
        return "board1/boardwrite";
    }

    @PostMapping("/board/writecontroller")
    @ResponseBody
    public String boardwritecontroller(BoardDto boardDto, @RequestParam("cano") int cano) {
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        boardService.boardwrite(boardDto, cano);
        return "redirect:/board1/boardlist";
    }


    // boardview1
    @GetMapping("/board1/boardview/{bno}")
    public String boardview1(@PathVariable("bno") int bno, Model model) {
        BoardDto boardDto = boardService.getboard(bno);
        model.addAttribute("boardDto",boardDto);
        return "board1/boardview";
    }

    // boardupdate1
    @GetMapping("/board1/boardupdate/{bno}")
    public String boardupdate(@PathVariable("bno") int bno, Model model) {
        // 해당 게시물 가져오기
        BoardDto boardDto = boardService.getboard(bno);
        System.out.println("bno : " + boardDto.getBno());
        System.out.println("제목 : " + boardDto.getBtitle());
        System.out.println("내용 : " + boardDto.getBcontents());
        System.out.println("작성자 : " + boardDto.getBwriter());
        model.addAttribute("boardDto",boardDto);
        return "board1/boardupdate";
    }

    @PostMapping("/board1/updatecontroller")
    public String updatecontroller(@RequestParam("bno") int bno,
                                   @RequestParam("btitle") String btitle,
                                   @RequestParam("bcontents") String bcontents) {
        try {
            BoardDto boardDto = BoardDto.builder()
                    .bno(bno)
                    .btitle(btitle)
                    .bcontents(bcontents)
                    .build();
            System.out.println("확인 : " + bno +","+ btitle +","+ bcontents);
            boardService.boardupdate(boardDto);
        } catch (Exception e){
            System.out.println("에러");
            System.out.println(e);

        }


        return "redirect:/board1/boardview/" + bno;
    }

    // boarddelete
    @GetMapping("/board1/boarddelete")
    @ResponseBody
    public int boarddelete(@RequestParam("bno") int bno){
        boolean result = boardService.boarddelete(bno);
        if (result){
            return 1;
        } else {
            return 2;
        }
    }

}
