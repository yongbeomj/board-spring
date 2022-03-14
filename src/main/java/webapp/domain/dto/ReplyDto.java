package webapp.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyDto {
    private int rno;
    private String rcontents;
    private String rwriter;
    private LocalDateTime rcreatedDate;
}
