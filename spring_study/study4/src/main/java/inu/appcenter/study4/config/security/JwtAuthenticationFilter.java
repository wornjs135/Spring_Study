package inu.appcenter.study4.config.security;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final  JwtTokenProvider jwtTokenProvider;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    // 인증 작업을 수행
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        /**
         * 1. HttpServletRequest의 헤더에서 Authorization이라는 헤더 값을 가져오는 메서드를 호출.
         * */
        String jwtToken = resolveToken((HttpServletRequest) request);

        /**
         * 3. 토큰이 유효한지 검사하는 메서드를 호출.
         */
        if(StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)){
            /**
             * 5. 토큰이 유효하다면 인증 객체를 만드는 메서드를 호출.
             */
            Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
            /**
             * 11. 만들어온 인증 객체를 스레드로컬의 SecurityContextHolder에 넣는다.
             *     -> 인증이 완료됨.
             */
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    // JWT 토큰을 구해옴
    public String resolveToken(HttpServletRequest request){
        /**
         * 2. HttpServletRequest의 헤더에서 Authorization 이라는 헤더의 값을 가져온다.
         *    가져온 결과는 Bearer + JWT 토큰.
         *    토큰이 존재한다면 Bearer를 제거하고 반환.
         */
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
