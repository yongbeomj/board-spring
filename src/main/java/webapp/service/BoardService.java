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

        List<ReplyEntity> reply = replyRepository.findAll();
        // 부모번호는 그대로
        int rparent = replyEntities.get().getRparent();
        // 깊이는 해당 댓글의 +1
        int rdepth = replyEntities.get().getRdepth() + 1;

        int rorder;
        int temp = 0;
        try {
            // 만약 다음 뎁스가 있다면 순서는 다음 뎁스에서 최대값 +1
            for (int i = 0; i < reply.size(); i++) {
                if (reply.get(i).getRparent() == rparent && reply.get(i).getRdepth() == rdepth) {
                    if (reply.get(i).getRorder() > temp) {
                        temp = reply.get(i).getRorder();
                    }
                }
            }
            rorder = temp + 1;
        } catch (Exception e){
            // 다음 뎁스가 없으면 현재 댓글 순서 +1
            rorder = replyEntities.get().getRorder() + 1;
        }

        try {
            // 중간에 댓글이 등록될 경우 뒷 순서 댓글은 하나씩 밀려야 함
            for (int i = 0; i < reply.size(); i++) {
                // 만약 부모가 같고 순서가 신규 댓글 이상이라면
                if (reply.get(i).getRparent() == rparent && reply.get(i).getRorder() >= rorder) {
                    // 해당 댓글의 order를 +1한다
                    reply.get(i).setRorder(reply.get(i).getRorder() + 1);
                }
            }
        } catch (Exception e) {
        }

        ReplyEntity replyEntity = ReplyEntity.builder()
                .rcontents(rcontents)
                .boardEntity(boardEntity.get())
                .categoryEntity2(categoryEntity.get())
                .memberEntity2(memberEntity.get())
                .rparent(rparent)
                .rorder(rorder)
                .rdepth(rdepth)
                .build();

        replyRepository.save(replyEntity); // 댓글 저장
        boardEntity.get().getReplyEntities().add(replyEntity); // 대댓글 저장

        return false;
    }

    // 모든 댓글 출력
    public List<ReplyEntity> getreplylist(int bno) {

        List<ReplyEntity> replyEntities = new ArrayList<>();

        try {
            replyEntities = replyRepository.replylist(bno);

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
