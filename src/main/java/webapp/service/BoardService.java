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
        // ???????????? ?????????
        Optional<BoardEntity> entityOptional = boardRepository.findById(bno);
        int mno = entityOptional.get().getMemberEntity().getMno();
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);

        // dto??? ?????? ?????? ????????????
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
            System.out.println("??????");
            System.out.println(e);
            return false;
        }

    }


    // ?????? ??????(?????????)
    public boolean replywrite(int bno, String rcontents) {
        // ?????? ????????? ????????????
        Optional<BoardEntity> entityOptional = boardRepository.findById(bno);
        // ?????? ??????
        List<ReplyEntity> reply = replyRepository.findAll();
        // ???????????? ??????
        int cano = entityOptional.get().getCategoryEntity().getCano();
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(cano);
        // ?????? ????????? ??????
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


        replyRepository.save(replyEntities); // ?????? ??????
        entityOptional.get().getReplyEntities().add(replyEntities); // ???????????? ?????? ??????
        return false;
    }

    // ????????? ??????
    public boolean rereplywrite(int rno, String rcontents) {
        // ?????? ?????? ????????????
        Optional<ReplyEntity> replyEntities = replyRepository.findById(rno);
        // ????????? ????????????
        int bno = replyEntities.get().getBoardEntity().getBno();
        Optional<BoardEntity> boardEntity = boardRepository.findById(bno);
        // ?????? ????????? ??????
        int mno = replyEntities.get().getMemberEntity2().getMno();
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);
        // ???????????? ??????
        int cano = replyEntities.get().getCategoryEntity2().getCano();
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(cano);

        List<ReplyEntity> reply = replyRepository.findAll();
        // ??????????????? ?????????
        int rparent = replyEntities.get().getRparent();
        // ????????? ?????? ????????? +1
        int rdepth = replyEntities.get().getRdepth() + 1;

        int rorder = 0;
        int temp = 0;
        // ?????? ?????? ????????? ?????? ????????????
        List<ReplyEntity> nextreply = replyRepository.nextreply(bno, rparent, replyEntities.get().getRorder() + 1);
        System.out.println(nextreply);

        try {
            // ?????? ?????? ?????? ????????? ?????? ?????? ????????? ?????????
            if (nextreply.get(0).getRdepth() == rdepth) {
                // ?????? ???????????? ????????? ?????????
                for (int i = 0; i < reply.size(); i++) {
                    if (reply.get(i).getRparent() == rparent && reply.get(i).getRdepth() == rdepth) {
                        if (reply.get(i).getRorder() > temp) {
                            temp = reply.get(i).getRorder();
                        }
                    }
                }
                // ????????? +1
                rorder = temp + 1;
            } else { // ?????? ?????? ?????? ????????? ?????? ?????? ????????? ????????????
                // ?????? ?????? ?????? +1
                rorder = replyEntities.get().getRorder() + 1;
            }

        } catch (Exception e) { // ?????? ????????? ?????????(?????? ????????? ?????????)
            // ?????? ?????? ????????? ????????? ?????? ?????? ?????? +1
            rorder = replyEntities.get().getRorder() + 1;
        }

        // ?????? ?????? ?????? ????????????
        List<ReplyEntity> findreply = replyRepository.findreply(bno, rparent);

        try {
            // ????????? ????????? ????????? ?????? ??? ?????? ????????? ????????? ????????? ???
            for (int i = 0; i < findreply.size(); i++) {
                // ?????? ????????? ?????? ????????? ?????? ?????? ???????????????
                if (findreply.get(i).getRparent() == rparent && findreply.get(i).getRorder() >= rorder) {
                    // ?????? ????????? order??? +1??????
                    findreply.get(i).setRorder(findreply.get(i).getRorder() + 1);
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

        replyRepository.save(replyEntity); // ?????? ??????
        boardEntity.get().getReplyEntities().add(replyEntity); // ????????? ??????

        return false;
    }

    // ?????? ?????? ??????
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

    // ?????? ??????
    @Transactional
    public boolean replyupdate(int rno, String newcontents) {
        // ?????? ????????????
        ReplyEntity replyEntity = replyRepository.findById(rno).get();
        // ?????? ??????
        replyEntity.setRcontents(newcontents);
        return true;
    }

}
