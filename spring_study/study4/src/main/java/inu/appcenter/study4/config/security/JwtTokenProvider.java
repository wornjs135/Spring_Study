package inu.appcenter.study4.config.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    private Long tokenValidMilliseconds = 1000L * 60 * 60; //1시간 유효

    private final UserDetailsService userDetailsService;

    @PostConstruct // 스프링 빈이 초기화되고 실행되는 놈
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 인증 객체를 반환
    public Authentication getAuthentication(String jwtToken){
        /**
         * 5. GetMemberPk(jwtToken)을 호출하여 토큰에 저장되어 있는 회원의 정보를 가져온다.
         * 6. userDetailsService.loadUserByUsername(회원아이디)를 호출하여 UserDetails 타입의 객체를 반환
         *    userDetailsService는 인터페이스이므로 실제 호출은 MemberDetailsService의 loadUserByUsername() 호출
         * 10. 반환받은 UserDetails타입의 객체를 이용하여 Authentication도 인터페이스이므로
         *     Authentication을 구현하는 UsernamePasswordAuthenticationToken을 생성하여 반환
         *     이때 인증 객체의 Principal에는 UserDetails가 들어감.
         *     결국 Authentication -> Principal(User타입)-> username이 회원의 정보s
         */
        UserDetails userDetails = userDetailsService.loadUserByUsername(getMemberPk(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 회원 id를 사용하여 JWT 토큰을 발급
     * Claim은 JWT 토큰 payload 라고 보면 됨.
     * Claims안에는 제목, 바디, 발행일자, 유효시간 설정 가능
     * 시크릿 키를 사용하여 JWT 토큰에 사인한다.
     * */
    //  JWT 토큰 발급
    public String createToken(String memberPk){
        Claims claims = Jwts.claims().setSubject(memberPk); //페이로드
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) //페이로드
                .setIssuedAt(now) //언제 발행됐는지
                .setExpiration(new Date(now.getTime() + tokenValidMilliseconds)) //언제까지 유효
                .signWith(SignatureAlgorithm.HS256, secretKey) //시그니처
                .compact();
    }

    /**
     * 6. 서버가 가지고 있는 시크릿 키를 활용해서 JWT 토큰을 파싱하고 토큰의 제목에서 회원의 ID값을 추출
     */
    // 토큰을 발행할 때 넣었던 memberPk가 나온다.
    public String getMemberPk(String jwtToken){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken)
                .getBody().getSubject();
    }

    // JWT 토큰 검증

    /**
     * 4. JWT 토큰의 유효성을 검사한다.
     * 검사하는 내용
     * 4-1. SecretKey를 통해서 JWT 파싱하는데 만약 서버가 가지고 있는 시크릿키로 파싱이 안된다면 변조가 되었거나 다른 서버의 JWT이므로
     *      예외가 발생.
     * 4-2. JWT 토큰의 유효시간과 서버시간을 비교해서 유효시간이 서버 시간보다 전이라면 false를 리턴
     *      -> 토큰이 만료가 되었다.
     */
    public boolean validateToken(String jwtToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken); //  페이로드
            return !claims.getBody().getExpiration().before(new Date()); // 지금시간보다 유효시간이 과거인지 검증
        } catch (Exception e){
            return false;
        }
    }
}