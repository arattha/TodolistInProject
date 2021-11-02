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

@Slf4j
@Service
@AllArgsConstructor
public class ImgService {

    MemberDetailDao memberDetailDao;

    public Resource getFile(final String id) {

        MemberDetail memberDetail = memberDetailDao.findMemberDetailByMemberId(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Resource img =  new FileSystemResource(".//upload//"+memberDetail.getProfileImg());

        if(!img.exists()){
            img =  new FileSystemResource(".//upload//default.png");
        }
        return img;
    }


    @Transactional
    public void addFile(ImgFileDto newFile) throws IllegalStateException, IOException {

        String path = ".//upload";
        File Folder = new File(path);
        if(!Folder.exists()) Folder.mkdir();

        if(newFile.getMultipartFile() != null) {//파일이 존재할 때에만,

            try {
                MultipartFile multipartFile = newFile.getMultipartFile();
                String ext =  multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
                String fileName = newFile.getId() + ext;

                MemberDetail memberDetail = memberDetailDao.findMemberDetailByMemberId(newFile.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

                memberDetail.setProfileImgLink(fileName);

                multipartFile.transferTo(new File( path +"//"+fileName));
                memberDetailDao.save(memberDetail);
            }  catch (DataAccessException e) {
                e.printStackTrace();
                throw new JpaException(JpaErrorCode.SAVE_DETAIL_ERROR);
            }

        }
    }
}
