package com.bom.rentalmarket.s3;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final S3Config s3Config;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private String uploadFile(MultipartFile file, String fileName) throws IOException {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    try {
      s3Config.amazonS3()
          .putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
    } catch (AmazonS3Exception e) {
      throw new IOException("Failed to upload file to S3", e);
    }

    return fileName;
  }

  public String upload(MultipartFile file) throws IOException {
    String fileName = UUID.randomUUID().toString().substring(0, 20);
    return uploadFile(file, fileName);
  }

  public String update(MultipartFile file, String oldFileName) throws IOException {
    delete(oldFileName);
    String fileName = UUID.randomUUID().toString().substring(0, 20);
    return uploadFile(file, fileName);
  }

  public void delete(String fileName) {
    try {
      s3Config.amazonS3().deleteObject(bucket, fileName);
    } catch (AmazonS3Exception e) {
      throw new RuntimeException("Failed to delete file from S3", e);
    }
  }

  // 추후에 Product 삭제 시
//  public void deleteImageUrls(List<String> imageUrls, String mainImageUrl) {
//    for (String imageUrl : imageUrls) {
//      if (!imageUrl.equals(mainImageUrl)) {
//        try {
//          s3Config.amazonS3().deleteObject(bucket, imageUrl);
//        } catch (AmazonS3Exception e) {
//          throw new RuntimeException("Failed to delete file from S3", e);
//        }
//      }
//    }
//  }
}