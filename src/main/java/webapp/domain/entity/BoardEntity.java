package webapp.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="board")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BoardEntity extends BaseTimeEntity {

    // 게시판번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autokey
    @Column(name = "bno")
    private int bno;

    // 제목
    @Column(name = "btitle")
    private String btitle;

    // 내용
    @Column(name = "bcontents")
    private String bcontents;

    // 작성자 = mno
    @ManyToOne
    @JoinColumn(name="mno")
    private MemberEntity memberEntity;

    // 카테고리 no
    @ManyToOne
    @JoinColumn(name = "cano")
    private CategoryEntity categoryEntity;

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL)
    List<ReplyEntity> replyEntities = new ArrayList<>();

}
