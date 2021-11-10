package com.web.tip.common;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberHasTeamDto {

    private String memberId;
    private String teamId;

    public static MemberHasTeamDto entityToDto(MemberHasTeam memberHasTeam) {
        return MemberHasTeamDto.builder()
                .memberId(memberHasTeam.getMemberId())
                .teamId(memberHasTeam.getTeamId())
                .build();
    }

}
