package webapp.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cano")
    private int cano;

    @Column(name = "catitle")
    private String catitle;

    // 게시판 연결
    @OneToMany(mappedBy = "categoryEntity")
    private List<BoardEntity> boardEntities = new ArrayList<>();

    // 댓글 연결
    @OneToMany(mappedBy = "categoryEntity2")
    private List<ReplyEntity> replyEntities = new ArrayList<>();





}
