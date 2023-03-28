package com.bom.rentalmarket.UserController.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Member  {
    // 회원가입 페이지 활용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String nickName;

    @Column
    private String password;

    @Column
    private String region;

    @Column
    private LocalDateTime regDate;// 회원가입 날짜

    @Column
    private LocalDateTime updateDate; // 회원정보 수정 날짜

    @Column
    private String title; // url title

    @Column
    private String imageUrl; // url 경로
    @Override
    public String toString() {
        return "FileEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", s3Url='" + imageUrl + '\'' +
                '}';
    }

}
