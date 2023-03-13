package com.bom.rentalmarket.member;

import com.bom.rentalmarket.member.domain.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


    int countByEmail(String email);

    int countByNickName(String nickName);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByIdAndPassword(long id, String password);

}
