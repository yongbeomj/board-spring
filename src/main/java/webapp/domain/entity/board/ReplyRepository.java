package webapp.domain.entity.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM reply where bno = :bno order by rparent asc, rorder asc, rdepth asc")
    List<ReplyEntity> replylist(@Param("bno") int bno);

    // 다음 뎁스 댓글 여부 확인
    @Query(nativeQuery = true, value = "SELECT * FROM reply where bno = :bno and rparent = :rparent and rdepth = :rdepth")
    List<ReplyEntity> findreply(@Param("bno") int bno, @Param("rparent") int rparent, @Param("rdepth") int rdepth);




}
