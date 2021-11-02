package com.web.tip.image;

import com.web.tip.BasicResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;

@Slf4j
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController
@AllArgsConstructor
@RequestMapping("/img")
public class ImgController {

    ImgService imgService;

    @GetMapping("/{id}")
    public Object getImgFile(@PathVariable String id) throws MalformedURLException {
        Resource resource =  imgService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @PostMapping()
    public ResponseEntity<BasicResponse> addImgFile(@RequestBody ImgFileDto imgFileDto) throws IOException {

        imgService.addFile(imgFileDto);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "Success";

        return new ResponseEntity<>(result, HttpStatus.OK);
    }



}
