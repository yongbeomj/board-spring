package webapp.domain.dto;

import lombok.*;
import webapp.domain.entity.BoardEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BoardDto {

    private int bno;
    private String btitle;
    private String bcontents;

    public BoardEntity toentity() {
        return BoardEntity.builder()
                .btitle(this.btitle)
                .bcontents(this.bcontents)
                .build();
    }

}
