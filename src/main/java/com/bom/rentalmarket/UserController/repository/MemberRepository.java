package com.bom.rentalmarket.UserController.repository;

import com.bom.rentalmarket.UserController.domain.model.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


    int countByEmail(String email);

    int countByNickName(String nickName);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    Optional<Member> findByIdAndNickName(long id, String nickName);
}
