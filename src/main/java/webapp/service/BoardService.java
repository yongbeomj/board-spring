package webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webapp.domain.dto.BoardDto;
import webapp.domain.dto.MemberDto;
import webapp.domain.entity.board.BoardEntity;
import webapp.domain.entity.board.BoardRepository;
import webapp.domain.entity.category.CategoryRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    CategoryRepository categoryRepository;

    // boardlist
    public ArrayList<BoardDto> boardlist() {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        ArrayList<BoardDto> boardDtos = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntities) {
            BoardDto boardDto = new BoardDto(
                    boardEntity.getBno(),
                    boardEntity.getBtitle(),
                    boardEntity.getBcontents(),
                    boardEntity.getBwriter(),
                    boardEntity.getCreatedDate());
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }

    // boardwrite
    public boolean boardwrite(BoardDto boardDto) {
        // 컨트롤러에서 받은 데이터를 엔티티로 변환 후 db에 저장한다.
        boardRepository.save(boardDto.toentity());
        return true;
    }


}
