package com.web.tip.bookmark;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookmarkDto {

    private String memberId;
    private String todoId;
}
