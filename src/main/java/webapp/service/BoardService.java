package webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webapp.domain.dto.BoardDto;
import webapp.domain.dto.MemberDto;
import webapp.domain.entity.board.BoardEntity;
import webapp.domain.entity.board.BoardRepository;
import webapp.domain.entity.board.ReplyEntity;
import webapp.domain.entity.board.ReplyRepository;
import webapp.domain.entity.category.CategoryEntity;
import webapp.domain.entity.category.CategoryRepository;
import webapp.domain.entity.member.MemberEntity;
import webapp.domain.entity.member.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReplyRepository replyRepository;

    // boardlist
    public ArrayList<BoardDto> boardlist() {
        List<BoardEntity> boardEntities = boardRepository.findAll();

        ArrayList<BoardDto> boardDtos = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntities) {
            BoardDto boardDto = new BoardDto(
                    boardEntity.getBno(),
                    boardEntity.getBtitle(),
                    boardEntity.getBcontents(),
                    boardEntity.getMemberEntity().getMid(),
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
                .categoryEntity(categoryEntity.get())
                .memberEntity(memberEntity.get())
                .build();
        boardRepository.save(boardEntity);
        return true;
    }

    // boarddelete
    @Transactional
    public boolean boarddelete(int bno) {
        Optional<BoardEntity> entityOptional = boardRepository.findById(bno);
        if (entityOptional.get() != null) {
            boardRepository.delete(entityOptional.get());
            return true;
        } else {
            return false;
        }
    }

    // boardview
    @Transactional
    public BoardDto getboard(int bno) {
        // 게시물을 찾는다
        Optional<BoardEntity> entityOptional = boardRepository.findById(bno);
        int mno = entityOptional.get().getMemberEntity().getMno();
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);

        // dto에 값을 넣고 리턴한다
        return BoardDto.builder()
                .bno(entityOptional.get().getBno())
                .btitle(entityOptional.get().getBtitle())
                .bcontents(entityOptional.get().getBcontents())
                .bwriter(memberEntity.get().getMid())
                .bcreateDate(entityOptional.get().getCreatedDate())
                .build();

    }

    // boardupdate
    @Transactional
    public boolean boardupdate(BoardDto boardDto) {
        try {
            Optional<BoardEntity> entityOptional = boardRepository.findById(boardDto.getBno());
            entityOptional.get().setBtitle(boardDto.getBtitle());
            entityOptional.get().setBcontents(boardDto.getBcontents());
            return true;
        } catch (Exception e) {
            System.out.println("에러");
            System.out.println(e);
            return false;
        }

    }


    // 댓글 등록
    public boolean replywrite(int bno, String rcontents, String rwriter) {
        // 해당 게시물 가져오기
        Optional<BoardEntity> entityOptional = boardRepository.findById(bno);
        int mno = entityOptional.get().getMemberEntity().getMno();
        int cano = entityOptional.get().getCategoryEntity().getCano();
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(cano);
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);

        ReplyEntity replyEntities = ReplyEntity.builder()
                .rcontents(rcontents)
                .boardEntity(entityOptional.get())
                .categoryEntity2(categoryEntity.get())
                .memberEntity2(memberEntity.get())
                .build();

        replyRepository.save(replyEntities); // 댓글 저장
        entityOptional.get().getReplyEntities().add(replyEntities); // 게시물에 댓글 저장
        return false;
    }

    // 모든 댓글 출력
    public List<ReplyEntity> getreplylist(int bno) {

        Optional<BoardEntity> entityOptional = boardRepository.findById(bno);
        List<ReplyEntity> replyEntities = entityOptional.get().getReplyEntities();

        return replyEntities;
    }

    // replydelete
    @Transactional
    public boolean replydelete(int rno) {
        Optional<ReplyEntity> entityOptional = replyRepository.findById(rno);
        if (entityOptional != null) {
            replyRepository.delete(entityOptional.get());
            return true;
        } else {
            return false;
        }
    }

    // 댓글 수정
    @Transactional
    public boolean replyupdate(int rno, String newcontents) {
        // 댓글 가져오기
        ReplyEntity replyEntity = replyRepository.findById(rno).get();
        // 내용 수정
        replyEntity.setRcontents(newcontents);
        return true;
    }

}
