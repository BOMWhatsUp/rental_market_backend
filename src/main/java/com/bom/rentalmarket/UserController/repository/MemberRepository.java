package com.bom.rentalmarket.UserController.repository;

import com.bom.rentalmarket.UserController.domain.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    Optional<Member> findByIdAndNickName(long id, String nickName);

    Optional<Member> findByIdAndRegion(long id, String region);
}
