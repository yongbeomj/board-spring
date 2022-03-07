package webapp.domain.dto;

import lombok.*;
import webapp.domain.entity.member.MemberEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MemberDto {

    private int mno;
    private String mid;
    private String mpw;
    private String mname;
    private String memail;

    public MemberEntity toentity() {
        return MemberEntity.builder()
                .mid(this.mid)
                .mpw(this.mpw)
                .mname(this.mname)
                .memail(this.memail)
                .build();
    }
}
