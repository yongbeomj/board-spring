package webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webapp.domain.dto.BoardDto;
import webapp.domain.dto.MemberDto;
import webapp.domain.entity.board.BoardEntity;
import webapp.domain.entity.board.BoardRepository;
import webapp.domain.entity.category.CategoryEntity;
import webapp.domain.entity.category.CategoryRepository;
import webapp.domain.entity.member.MemberEntity;
import webapp.domain.entity.member.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MemberRepository memberRepository;

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
    public boolean boardwrite(BoardDto boardDto, int cano, int mno) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(cano);
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);

        BoardEntity boardEntity = BoardEntity.builder()
                .btitle(boardDto.getBtitle())
                .bcontents(boardDto.getBcontents())
                .bwriter(boardDto.getBwriter())
                .categoryEntity(categoryEntity.get())
                .memberEntity(memberEntity.get())
                .build();
        boardRepository.save(boardEntity);
        return true;
    }

    // boarddelete
    public boolean delete(int bno){
        Optional<BoardEntity> entityOptional = boardRepository.findById(bno);
        if (entityOptional.get() != null) {
            boardRepository.delete(entityOptional.get());
            return true;
        } else {
            return false;
        }
    }


}
