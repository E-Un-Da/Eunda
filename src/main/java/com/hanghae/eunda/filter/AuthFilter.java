package com.hanghae.eunda.filter;

import com.hanghae.eunda.entity.Member;
import com.hanghae.eunda.jwt.TokenManager;
import com.hanghae.eunda.jwt.TokenValidator;
import com.hanghae.eunda.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j(topic = "AuthFilter")
@Component
@Order(1)
@RequiredArgsConstructor
public class AuthFilter implements Filter {

    private final MemberRepository memberRepository;
    private final TokenManager tokenManager;
    private final TokenValidator tokenValidator;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();
        System.out.println(method);

        if (StringUtils.hasText(url) && (url.startsWith("/signup") || url.startsWith("/signin") || ("GET".equals(method) && url.equals("/studies")) || url.matches("/studies/\\+d"))) {
            // 회원가입, 로그인, 스터디 조회 관련 API 는 인증 필요없이 요청 진행
            chain.doFilter(request, response); // 다음 Filter 로 이동
        } else {
            String tokenValue = tokenManager.getTokenFromRequest(httpServletRequest);

            if (StringUtils.hasText(tokenValue)) { // 토큰이 존재하면 검증 시작
                // JWT 토큰 substring
                String token = tokenValidator.substringToken(tokenValue);

                // 토큰 검증
                if (!tokenValidator.validateToken(token)) {
                    throw new IllegalArgumentException("Token Error");
                }

                // 토큰에서 사용자 정보 가져오기
                Claims info = tokenManager.getMemberInfoFromToken(token);

                Member member = memberRepository.findByEmail(info.getSubject()).orElseThrow(() ->
                        new NullPointerException("Not Found User")
                );

                request.setAttribute("member", member);
                chain.doFilter(request, response); // 다음 Filter 로 이동
            } else {
                throw new IllegalArgumentException("Not Found Token");
            }
        }
    }
}