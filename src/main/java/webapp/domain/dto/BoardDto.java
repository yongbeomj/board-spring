package webapp.domain.dto;

import lombok.*;
import webapp.domain.entity.board.BoardEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BoardDto {

    private int bno;
    private int cano;
    private String btitle;
    private String bcontents;
    private String bwriter;
    private LocalDateTime bcreateDate;

    public BoardEntity toentity() {
        return BoardEntity.builder()
                .btitle(this.btitle)
                .bcontents(this.bcontents)
                .build();
    }
}
