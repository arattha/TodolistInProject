package com.web.tip.member.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRequest {
    private String id;
    private String name;
    private String email;
    private String phone;
    private MultipartFile multipartFile;
}
