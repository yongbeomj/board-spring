package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import webapp.domain.dto.BoardDto;
import webapp.domain.dto.MemberDto;
import webapp.domain.entity.board.ReplyEntity;
import webapp.service.BoardService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class BoardContorller {

    @Autowired
    BoardService boardService;

    @Autowired
    HttpServletRequest request;

    // boardlist1
    @GetMapping("/board1/boardlist")
    public String boardlist1(Model model) {
        int cano = 1;
        ArrayList<BoardDto> boardDtos = boardService.boardlist(cano);
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        model.addAttribute("memberDto", memberDto);
        model.addAttribute("boardDtos", boardDtos);
        return "board1/boardlist";
    }

    // boardlist2
    @GetMapping("/board2/boardlist")
    public String boardlist2(Model model) {
        int cano = 2;
        ArrayList<BoardDto> boardDtos = boardService.boardlist(cano);
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        model.addAttribute("memberDto", memberDto);
        model.addAttribute("boardDtos", boardDtos);
        return "board2/boardlist";
    }

    // boardlist3
    @GetMapping("/board3/boardlist")
    public String boardlist3(Model model) {
        int cano = 3;
        ArrayList<BoardDto> boardDtos = boardService.boardlist(cano);
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        model.addAttribute("memberDto", memberDto);
        model.addAttribute("boardDtos", boardDtos);
        return "board3/boardlist";
    }

    // boardwrite1
    @GetMapping("/board1/boardwrite")
    public String boardwrite1() {
        return "board1/boardwrite";
    }

    @PostMapping("/board1/writecontroller")
    @ResponseBody
    public RedirectView boardwritecontroller1(BoardDto boardDto, @RequestParam("cano") int cano) {
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        boolean result = boardService.boardwrite(boardDto, cano, memberDto.getMno());
        if (result) {
            return new RedirectView("/board1/boardlist");
        } else {
            return new RedirectView("/board1/boardwrite");
        }
    }

    // boardwrite2
    @GetMapping("/board2/boardwrite")
    public String boardwrite2() {
        return "board2/boardwrite";
    }

    @PostMapping("/board2/writecontroller")
    @ResponseBody
    public RedirectView boardwritecontroller2(BoardDto boardDto, @RequestParam("cano") int cano) {
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        boolean result = boardService.boardwrite(boardDto, cano, memberDto.getMno());
        if (result) {
            return new RedirectView("/board2/boardlist");
        } else {
            return new RedirectView("/board2/boardwrite");
        }
    }

    // boardwrite3
    @GetMapping("/board3/boardwrite")
    public String boardwrite3() {
        return "board3/boardwrite";
    }

    @PostMapping("/board3/writecontroller")
    @ResponseBody
    public RedirectView boardwritecontroller3(BoardDto boardDto, @RequestParam("cano") int cano) {
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        boolean result = boardService.boardwrite(boardDto, cano, memberDto.getMno());
        if (result) {
            return new RedirectView("/board3/boardlist");
        } else {
            return new RedirectView("/board3/boardwrite");
        }
    }

    // boardview1
    @GetMapping("/board1/boardview/{bno}")
    public String boardview1(@PathVariable("bno") int bno, Model model) {
        BoardDto boardDto = boardService.getboard(bno);
        List<ReplyEntity> replyEntities = boardService.getreplylist(bno);

        model.addAttribute("boardDto", boardDto);
        model.addAttribute("replyEntities", replyEntities);
        return "board1/boardview";
    }

    // boardview2
    @GetMapping("/board2/boardview/{bno}")
    public String boardview2(@PathVariable("bno") int bno, Model model) {
        BoardDto boardDto = boardService.getboard(bno);
        List<ReplyEntity> replyEntities = boardService.getreplylist(bno);

        model.addAttribute("boardDto", boardDto);
        model.addAttribute("replyEntities", replyEntities);
        return "board2/boardview";
    }

    // boardview3
    @GetMapping("/board3/boardview/{bno}")
    public String boardview3(@PathVariable("bno") int bno, Model model) {
        BoardDto boardDto = boardService.getboard(bno);
        List<ReplyEntity> replyEntities = boardService.getreplylist(bno);

        model.addAttribute("boardDto", boardDto);
        model.addAttribute("replyEntities", replyEntities);
        return "board3/boardview";
    }


    // boardupdate1
    @GetMapping("/board1/boardupdate/{bno}")
    public String boardupdate1(@PathVariable("bno") int bno, Model model) {
        // 해당 게시물 가져오기
        BoardDto boardDto = boardService.getboard(bno);
        model.addAttribute("boardDto", boardDto);
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
            boardService.boardupdate(boardDto);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/board1/boardview/" + bno;
    }


    // boardupdate2
    @GetMapping("/board2/boardupdate/{bno}")
    public String boardupdate2(@PathVariable("bno") int bno, Model model) {
        // 해당 게시물 가져오기
        BoardDto boardDto = boardService.getboard(bno);
        model.addAttribute("boardDto", boardDto);
        return "board2/boardupdate";
    }

    @PostMapping("/board2/updatecontroller")
    public String updatecontroller2(@RequestParam("bno") int bno,
                                   @RequestParam("btitle") String btitle,
                                   @RequestParam("bcontents") String bcontents) {
        try {
            BoardDto boardDto = BoardDto.builder()
                    .bno(bno)
                    .btitle(btitle)
                    .bcontents(bcontents)
                    .build();
            boardService.boardupdate(boardDto);
        } catch (Exception e) {
            System.out.println("에러");
            System.out.println(e);
        }
        return "redirect:/board2/boardview/" + bno;
    }

    // boardupdate1
    @GetMapping("/board3/boardupdate/{bno}")
    public String boardupdate3(@PathVariable("bno") int bno, Model model) {
        // 해당 게시물 가져오기
        BoardDto boardDto = boardService.getboard(bno);
        model.addAttribute("boardDto", boardDto);
        return "board3/boardupdate";
    }

    @PostMapping("/board3/updatecontroller")
    public String updatecontroller3(@RequestParam("bno") int bno,
                                   @RequestParam("btitle") String btitle,
                                   @RequestParam("bcontents") String bcontents) {
        try {
            BoardDto boardDto = BoardDto.builder()
                    .bno(bno)
                    .btitle(btitle)
                    .bcontents(bcontents)
                    .build();
            boardService.boardupdate(boardDto);
        } catch (Exception e) {
            System.out.println("에러");
            System.out.println(e);
        }
        return "redirect:/board3/boardview/" + bno;
    }


    // boarddelete
    @GetMapping("/board/boarddelete")
    @ResponseBody
    public int boarddelete(@RequestParam("bno") int bno, @RequestParam("cano") int cano) {
        boolean result = boardService.boarddelete(bno);
        if (result) {
            return 1;
        } else {
            return 2;
        }
    }

    // replywrite
    @GetMapping("/replywrite")
    @ResponseBody
    public String replywrite(@RequestParam("bno") int bno,
                             @RequestParam("rcontents") String rcontents) {

        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");

        if (memberDto == null) {
            return "2";
        }
        boardService.replywrite(bno, rcontents);
        return "1";
    }

    // rereplywrite
    @GetMapping("/rereplywrite")
    @ResponseBody
    public String rereplywrite(@RequestParam("rno") int rno,
                             @RequestParam("rrecontents") String rrecontents) {

        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");

        if (memberDto == null) {
            return "2";
        }
        boardService.rereplywrite(rno, rrecontents);
        return "1";
    }

    // replydelete
    @GetMapping("/replydelete")
    @ResponseBody
    public int replydelete(@RequestParam("rno") int rno) {
        boolean result = boardService.replydelete(rno);
        if (result) {
            return 1;
        } else {
            return 2;
        }
    }

    // 댓글 수정
    @GetMapping("/replyupdate")
    @ResponseBody
    public String replyupdate(@RequestParam("rno") int rno, @RequestParam("newcontents") String newcontents) {
        boolean result = boardService.replyupdate(rno, newcontents);
        if (result) {
            return "1";
        } else {
            return "2";
        }

    }

}


