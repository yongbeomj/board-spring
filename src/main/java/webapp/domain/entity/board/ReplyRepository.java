package webapp.domain.entity.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {

    @Query(nativeQuery = true, value = "select * from reply where bno = :bno order by rparent ASC")
    List<ReplyEntity> replyall(@Param("bno") int bno);



}
