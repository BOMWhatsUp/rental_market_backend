package com.bom.rentalmarket.S3;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class FileController {
    private final AwsS3Uploader awsS3Uploader;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileName = awsS3Uploader.upload(multipartFile, "test");
        return fileName;
    }
}