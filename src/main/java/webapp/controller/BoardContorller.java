package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BoardContorller {

    @Autowired
    BoardService boardService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;

    // boardlist1_1
    @GetMapping("/board1/boardlist")
    public String boardlist(Model model) {
        ArrayList<BoardDto> boardDtos = boardService.boardlist();
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
    public String boardwritecontroller(BoardDto boardDto) {
        boardService.boardwrite(boardDto);
        // 리스트 페이지 호출
        return "redirect:/";
    }

    // boardview1_1
    @GetMapping("/board1/boardview")
    public String boardview1_1() {
        return "board1/boardview";
    }

    // boardupdate1_1
    @GetMapping("/board1/boardupdate")
    public String boardupdate1_1() {
        return "board1/boardupdate";
    }

    // boarddelete
    @GetMapping("/board1/boarddelete")
    @ResponseBody
    public int boarddelete(@RequestParam("bno") int bno){
        boolean result = boardService.delete(bno);
        if (result){
            return 1;
        } else {
            return 2;
        }
    }

}
