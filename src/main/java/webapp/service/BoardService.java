package webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.domain.dto.BoardDto;
import webapp.domain.entity.board.BoardEntity;
import webapp.domain.entity.board.BoardRepository;
import webapp.domain.entity.board.ReplyEntity;
import webapp.domain.entity.board.ReplyRepository;
import webapp.domain.entity.category.CategoryEntity;
import webapp.domain.entity.category.CategoryRepository;
import webapp.domain.entity.member.MemberEntity;
import webapp.domain.entity.member.MemberRepository;

import javax.transaction.Transactional;
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
        // 댓글 전체
        List<ReplyEntity> reply = replyRepository.findAll();
        // 카테고리 번호
        int cano = entityOptional.get().getCategoryEntity().getCano();
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(cano);
        // 댓글 작성자 정보
        int mno = entityOptional.get().getMemberEntity().getMno();
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);

        int maxrparent;
        try {
            int rparent = reply.get(0).getRparent();
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

        // 해당 댓글 번호를 찾아서 부모번호는 그대로, 깊이 해당 댓글의 +1, 순서 같은 부모의 순서 최대값 +1
        // 중간에 댓글이 등록될 경우 뒷 순서 댓글은 하나씩 밀려야 함
        List<ReplyEntity> reply = replyRepository.findAll();
        int rparent = replyEntities.get().getRparent();
        int maxrorder;
        try {
            int rorder = reply.get(0).getRparent();
            for (int i = 0; i < reply.size(); i++) {
                if (reply.get(i).getRparent() == rparent) {
                    if (rorder < reply.get(i).getRorder()) {
                        rorder = reply.get(i).getRorder();
                    }
                }
            }
            maxrorder = rorder + 1;
        } catch (Exception e) {
            maxrorder = 0;
        }

        ReplyEntity replyEntity = ReplyEntity.builder()
                .rcontents(rcontents)
                .boardEntity(boardEntity.get())
                .categoryEntity2(categoryEntity.get())
                .memberEntity2(memberEntity.get())
                .rparent(replyEntities.get().getRparent())
                .rorder(maxrorder)
                .rdepth(replyEntities.get().getRdepth() + 1)
                .build();

        replyRepository.save(replyEntity); // 댓글 저장
        boardEntity.get().getReplyEntities().add(replyEntity); // 대댓글 저장

        return false;
    }


//    // 모든 댓글 출력 - 수정 중
//    public List<ReplyEntity> getreplylist(int bno) {
//
//        Optional<BoardEntity> boardEntity = boardRepository.findById(bno);
//        List<ReplyEntity> replyEntities = boardEntity.get().getReplyEntities();
//        List<ReplyEntity> getreply = new ArrayList<>();
//        // 같은 부모 번호로 묶어서 저장한다
//        for (int i = 0; i < replyEntities.size(); i++){
//
//            // 만약 부모번호가 리스트 인덱스와 같으면
//            if (replyEntities.get(i).getRparent() == i){
//                // 댓글 작성자 정보
//                int mno = replyEntities.get(i).getMemberEntity2().getMno();
//                Optional<MemberEntity> memberEntity = memberRepository.findById(mno);
//                // 카테고리 번호
//                int cano = replyEntities.get(i).getCategoryEntity2().getCano();
//                Optional<CategoryEntity> categoryEntity = categoryRepository.findById(cano);
//
//                // 인덱스에 해당 댓글정보 저장
//                ReplyEntity replyEntity = ReplyEntity.builder()
//                        .rno(replyEntities.get(i).getRno())
//                        .rcontents(replyEntities.get(i).getRcontents())
//                        .rparent(replyEntities.get(i).getRparent())
//                        .rdepth(replyEntities.get(i).getRdepth())
//                        .rorder(replyEntities.get(i).getRorder())
//                        .memberEntity2(memberEntity.get())
//                        .categoryEntity2(categoryEntity.get())
//                        .boardEntity(boardEntity.get())
//                        .build();
//                getreply.add(replyEntity);
//            }
//        }
//        System.out.println(getreply);
//
//        return getreply;
//    }

//    // 모든 댓글 출력
//    public List<ReplyEntity> getreplylist(int bno) {
//
//        Optional<BoardEntity> entityOptional = boardRepository.findById(bno);
//        List<ReplyEntity> replyEntities = entityOptional.get().getReplyEntities();
//        return replyEntities;
//    }

    // 모든 댓글 출력
    public List<ReplyEntity> getreplylist(int bno) {

        List<ReplyEntity> replyEntities = new ArrayList<>();

        try {
            replyEntities = replyRepository.replyall(bno);
        } catch (Exception e) {

        }
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
