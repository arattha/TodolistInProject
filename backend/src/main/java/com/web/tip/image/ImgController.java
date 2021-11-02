package com.web.tip.image;

import com.web.tip.BasicResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController
@AllArgsConstructor
@RequestMapping("/img")
public class ImgController {

    ImgService imgService;

    @PostMapping()
    public ResponseEntity<BasicResponse> addImgFile(@RequestBody ImgFileDto imgFileDto) throws IOException {

        imgService.addFile(imgFileDto);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "Success";

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
