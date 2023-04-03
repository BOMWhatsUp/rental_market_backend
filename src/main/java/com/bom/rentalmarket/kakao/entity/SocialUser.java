package com.bom.rentalmarket.kakao.entity;

import com.bom.rentalmarket.kakao.type.SocialProvider;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "social_users")
public class SocialUser implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String providerUserId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SocialProvider provider;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column
  private String imageUrl;

//  @OneToOne
//  @JoinColumn(name = "member_id")
//  private Member member;
}