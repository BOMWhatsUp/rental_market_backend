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
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String email;

    @Column
    private String nickName;

    @Column
    private String password;

    @Column
    private String regin;

    @Column
    private LocalDateTime regDate;// 회원가입 날짜

    @Column
    private LocalDateTime updateDate; // 회원정보 수정 날짜


    private String filename;




}
