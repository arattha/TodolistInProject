package com.web.tip.image;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImgFileDto {

    private String id;
    private MultipartFile file;

}
