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
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@AllArgsConstructor
public class ImgService {

    MemberDetailDao memberDetailDao;

    public Resource getFile(final String id) {

        MemberDetail memberDetail = memberDetailDao.findMemberDetailByMemberId(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Path path = Paths.get(".", "upload");
        log.info("path: " + path.toUri().toString());

        Resource img = new FileSystemResource(Paths.get(path.toString(), memberDetail.getProfileImg()));

        if (!img.exists()) {
            img = new FileSystemResource(Paths.get(path.toString(), "default.png"));
        }
        return img;
    }


    @Transactional
    public void addFile(ImgFileDto newFile) throws IllegalStateException, IOException {

        Path path = Paths.get(".", "upload");
        File Folder = path.toFile();
        if (!Folder.exists()) Folder.mkdir();

        if (newFile.getFile() != null) {//파일이 존재할 때에만,

            try {
                MultipartFile multipartFile = newFile.getFile();
                String ext = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
                String fileName = newFile.getId() + ext;

                MemberDetail memberDetail = memberDetailDao.findMemberDetailByMemberId(newFile.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

                memberDetail.setProfileImgLink(fileName);

                Path filePath = Paths.get(path.toString(), fileName);
                log.info("파일 저장 위치:" + filePath.toString());
                multipartFile.transferTo(new File(filePath.toUri()));
                memberDetailDao.save(memberDetail);
            } catch (DataAccessException e) {
                e.printStackTrace();
                throw new JpaException(JpaErrorCode.SAVE_DETAIL_ERROR);
            }

        }
    }
}
