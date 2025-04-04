package maumrecord.maumrecord.config;

import lombok.RequiredArgsConstructor;
import maumrecord.maumrecord.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserDetailService userService;

    @Bean
    public WebSecurityCustomizer configure(){
        return (web)->web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(new AntPathRequestMatcher(("/static/**")));
    }

    //todo: 현재 개발을 위해 스웨거 주소 항상 열어둠
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(   //회원가입, 로그인 페이지는 모두 허용 - 주소 수정
                                new AntPathRequestMatcher("/home"),
                                new AntPathRequestMatcher("/home/**"),
                                new AntPathRequestMatcher("/swagger-ui/**"),
                                new AntPathRequestMatcher("/v3/api-docs/**")
                        ).permitAll()
                        .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")  // 관리자 권한 필요
                        .requestMatchers("/user", "/user/**").hasRole("USER")    // 일반 사용자 권한 필요
                        .anyRequest().authenticated())
                .formLogin(formLogin->formLogin
                        .loginPage("http://localhost:3000/login")    //login 페이지 주소
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("http://localhost:3000/record"); // 로그인 성공 후 이동
                        }))
                .logout(logout->logout
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.sendRedirect("http://localhost:3000/login"); // 로그아웃 성공 후 이동
                        })
                        .invalidateHttpSession(true))
                .csrf(AbstractHttpConfigurer::disable)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception{
        DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
