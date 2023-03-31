package com.bom.rentalmarket.UserController.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Member implements UserDetails {
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
    private String imageUrl; // url 경로

    @Override
    public String toString() {
        return "FileEntity{" +
                "id=" + id +
                ", s3Url='" + imageUrl + '\'' +
                '}';
    }

    @ElementCollection(fetch = FetchType.EAGER) //roles 컬렉션
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
