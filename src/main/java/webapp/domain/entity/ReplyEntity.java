package webapp.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "reply")
public class ReplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rno")
    private int rno;

    @Column(name ="rcontents")
    private String rcontents;

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
