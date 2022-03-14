package webapp.domain.entity.board;

import lombok.*;
import webapp.domain.entity.BaseTimeEntity;
import webapp.domain.entity.board.BoardEntity;
import webapp.domain.entity.category.CategoryEntity;
import webapp.domain.entity.member.MemberEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "reply")
public class ReplyEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rno")
    private int rno;

    @Column(name = "rcontents")
    private String rcontents;

    @Column(name = "rwriter")
    private String rwriter;

    @ManyToOne
    @JoinColumn(name = "mno")
    private MemberEntity memberEntity2;

    @ManyToOne
    @JoinColumn(name = "cano")
    private CategoryEntity categoryEntity2;

    @ManyToOne
    @JoinColumn(name = "bno")
    private BoardEntity boardEntity;


}
