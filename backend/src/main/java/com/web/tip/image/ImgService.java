package com.web.tip.image;

import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.error.JpaErrorCode;
import com.web.tip.error.JpaException;
import com.web.tip.mypage.MemberDetail;
import com.web.tip.mypage.MemberDetailDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ImgService {

    MemberDetailDao memberDetailDao;

    public Resource getFile(final String id) {

        Resource img = new FileSystemResource("//home//ubuntu//upload//default.png");

        if(Optional.ofNullable(id).isPresent() && !"null".equals(id)) {
            MemberDetail memberDetail = memberDetailDao.findMemberDetailByMemberId(id)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            img = new FileSystemResource("//home//ubuntu//upload//" + Optional.ofNullable(memberDetail.getProfileImg())
                    .orElse("default.png"));
        }

        return img;
    }


    @Transactional
    public void addFile(ImgFileDto newFile) throws IllegalStateException, IOException {

        String path = "//home//ubuntu//upload";
        File folder = new File(path);
        if (!folder.exists()) folder.mkdir();

        MultipartFile multipartFile = Optional.ofNullable(newFile.getFile())
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
        try {
            String ext = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
            String fileName = newFile.getId() + ext;

            MemberDetail memberDetail = memberDetailDao.findMemberDetailByMemberId(newFile.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

            memberDetail.setProfileImgLink(fileName);

            String filePath = path + "//" + fileName;
            log.info("파일 저장 위치:" + filePath);
            multipartFile.transferTo(new File(filePath));
            memberDetailDao.save(memberDetail);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new JpaException(JpaErrorCode.SAVE_DETAIL_ERROR);
        }

    }
}
