package inu.appcenter.study4.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter { //  security 커스텀하게 사용

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 x
                .and()
                .authorizeRequests()
                /**
                 * 인가 컨트롤러
                 * antMatcher는 특정 url에 인가 권한을 설정할 때 씀. permitAll(): 모두에게 허용. hasAnyRole(): 어떤 권한에게 허용
                 * */
                .antMatchers("/exception/**").permitAll()
                .antMatchers(HttpMethod.POST, "/members/login","/members").permitAll()
                .antMatchers(HttpMethod.GET, "/members").hasRole("ADMIN")
                /**
                 * 나머지 url
                 * */
                .anyRequest().hasAnyRole("MEMBER")
                .and()
                /**
                * 인증 오류 및 인가 오류를 핸들링하는 구현체들을 설정
                * */
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 오류 핸들링
                .accessDeniedHandler(new CustomAccessDeniedHandler()) // 인가 오류 핸들링
                .and()
                /**
                 * JWT토큰으로 인증 객체를 만들기 위한 필터 설정
                 * */
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

    }
}
