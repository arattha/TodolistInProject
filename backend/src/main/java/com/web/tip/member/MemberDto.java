package com.web.tip.member;

import com.web.tip.jwt.TokenDto;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private boolean isValid;
    private String id;
    private Optional<TokenDto> optionalTokenDto;
}