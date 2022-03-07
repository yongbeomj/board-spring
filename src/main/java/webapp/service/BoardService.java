package webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.domain.dto.BoardDto;
import webapp.domain.dto.MemberDto;
import webapp.domain.entity.board.BoardEntity;
import webapp.domain.entity.board.BoardRepository;
import webapp.domain.entity.category.CategoryRepository;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CategoryRepository categoryRepository;
    // boardwrite
    public boolean boardwrite(BoardEntity boardEntity) {
        // 컨트롤러에서 받은 데이터를 엔티티로 변환 후 db에 저장한다.
        boardRepository.save(boardEntity);
        return true;
    }

    //boardlist



}
