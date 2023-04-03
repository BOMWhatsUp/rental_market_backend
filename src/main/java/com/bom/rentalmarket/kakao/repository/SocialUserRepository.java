package com.bom.rentalmarket.kakao.repository;

import com.bom.rentalmarket.kakao.entity.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {

}
