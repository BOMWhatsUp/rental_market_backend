package com.bom.rentalmarket.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 토큰 받아오기
        try{

            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String token = jwtTokenProvider.resolveToken(httpRequest);

            // 토큰이 유효하다면
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰으로부터 유저 정보를 받아
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                // SecurityContext 에 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // 다음 Filter 실행
            chain.doFilter(httpRequest, response);

        } catch (ClassCastException e) {
            throw new ServletException("Invalid request", e);
        }

    }

}
