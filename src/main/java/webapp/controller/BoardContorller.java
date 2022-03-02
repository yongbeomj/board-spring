package webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardContorller {

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
