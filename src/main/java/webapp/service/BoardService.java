package webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Transactional
    public ArrayList<BoardDto> boardlist(int cano) {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        ArrayList<BoardDto> boardDtos = new ArrayList<>();
        for (int i = 0; i < boardEntities.size(); i++) {
            if (boardEntities.get(i).getCategoryEntity().getCano() == cano) {
                BoardDto boardDto = new BoardDto(
                        boardEntities.get(i).getBno(),
                        cano,
                        boardEntities.get(i).getBtitle(),
                        boardEntities.get(i).getBcontents(),
                        boardEntities.get(i).getMemberEntity().getMid(),
                        boardEntities.get(i).getCreatedDate());
                boardDtos.add(boardDto);
            }
        }
        return boardDtos;
    }

    // boardwrite
    @Transactional
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
                .cano(entityOptional.get().getCategoryEntity().getCano())
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


    // 댓글 등록(첫번째)
    public boolean replywrite(int bno, String rcontents) {
        // 해당 게시물 가져오기
        Optional<BoardEntity> entityOptional = boardRepository.findById(bno);
        int mno = entityOptional.get().getMemberEntity().getMno();
        int cano = entityOptional.get().getCategoryEntity().getCano();
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(cano);
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);
        List<ReplyEntity> reply = replyRepository.findAll();
        int maxrparent;
        try {
            int rparent = reply.get(1).getRparent();
            for (int i = 0; i < reply.size(); i++) {
                if (rparent < reply.get(i).getRparent()) {
                    rparent = reply.get(i).getRparent();
                }
            }
            maxrparent = rparent + 1;
        } catch (Exception e) {
            maxrparent = 0;
        }
        ReplyEntity replyEntities = ReplyEntity.builder()
                .rcontents(rcontents)
                .boardEntity(entityOptional.get())
                .categoryEntity2(categoryEntity.get())
                .memberEntity2(memberEntity.get())
                .rparent(maxrparent)
                .build();


        replyRepository.save(replyEntities); // 댓글 저장
        entityOptional.get().getReplyEntities().add(replyEntities); // 게시물에 댓글 저장
        return false;
    }

    // 대댓글 등록
    public boolean rereplywrite(int rno, String rcontents) {
        // 해당 댓글 가져오기
        Optional<ReplyEntity> replyEntities = replyRepository.findById(rno);
        // 게시물 가져오기
        int bno = replyEntities.get().getBoardEntity().getBno();
        Optional<BoardEntity> boardEntity = boardRepository.findById(bno);
        // 댓글 작성자 정보
        int mno = replyEntities.get().getMemberEntity2().getMno();
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);
        // 카테고리 번호
        int cano = replyEntities.get().getCategoryEntity2().getCano();
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(cano);

        // 해당 댓글 번호를 찾아서 부모번호는 그대로, 깊이는 해당 댓글의 +1, 순서는 부모 댓글 전체 수량 +1
        List<ReplyEntity> reply = replyRepository.findAll();
        int rparent = replyEntities.get().getRparent();
        System.out.println("확인 : " + rparent);
        int rorder;
        try {
            rorder = reply.get(1).getRparent();
            for (int i = 0; i < reply.size(); i++) {
                if (reply.get(i).getRparent() == rparent) {
                    if (rorder < reply.get(i).getRorder()) {
                        rorder = reply.get(i).getRorder();
                    }
                }
            }
        } catch (Exception e) {
            rorder = 0;
        }

        ReplyEntity replyEntity = ReplyEntity.builder()
                .rcontents(rcontents)
                .boardEntity(boardEntity.get())
                .categoryEntity2(categoryEntity.get())
                .memberEntity2(memberEntity.get())
                .rparent(replyEntities.get().getRparent())
                .rorder(rorder + 1)
                .rdepth(replyEntities.get().getRdepth() + 1)
                .build();

        replyRepository.save(replyEntity); // 댓글 저장
        boardEntity.get().getReplyEntities().add(replyEntity); // 대댓글 저장

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
