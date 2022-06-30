package login.config;

import login.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 설정
public class SecurityConfig extends WebSecurityConfigurerAdapter {//웹 시큐리티 설정 관련 상속클래스

    @Override // 재정의
    protected void configure(HttpSecurity http) throws Exception {
        // HTTP(URL) 관련 시큐리티 보안
        http //   http
                .authorizeHttpRequests()                                    //인증요청
                .antMatchers("/admin/**").hasRole("ADMIN")      // 어드민접근 가능 URL
                .antMatchers("/member/info").hasRole("MEMBER")  // 중개자 접근 가능 URL
                .antMatchers("/board/save") .hasRole("MEMBER")  // 멤버 접근가능 URL
                .antMatchers( "/**" ).permitAll()               //  모든 접근 허용
                .and()
                ////////////////////////////////////// 로그인 요청/////////////////////////////////////////////////////
                .formLogin()
                .loginPage("/member/login")                                 // 아이디 ,비밀번호를 입력받을 페이지 URL
                .loginProcessingUrl("/member/logincontroller")              // 로그일 처리할 URL 정의 -> loadUserByUsername 서비스로 이동
                .defaultSuccessUrl("/")                                     // 로그인 성공시 이동할 URL
                .usernameParameter("mid")                                   // 로그인시 아이디로 입력받을 변수명
                .passwordParameter("mpassword")                             // 로그인시 비밀번호로 입력받을 변수명
                .failureUrl("/")                          // 실패시 이동할 URL
                .and()//////////////////로그아웃////////////////////////////////////////////////////////////////////////
                .logout()                                                                   // 로그아웃
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))// 로그아웃 접근 URL
                .logoutSuccessUrl("/")                                                  //로그아웃 성공시
                .invalidateHttpSession(true)                                            //세션초기화
                .and()//////////////////////////////////회원가입/////////////////////////////////////////////////////
                .csrf()                                                                 // 예외처리???
                .ignoringAntMatchers("/member/logincontroller")             // 해당 URL 접근 예외
                .ignoringAntMatchers("/member/signup")
                .and()////////////////////////////////////공통///////////////////////////////////////////////////
                .exceptionHandling()                                                    // 오류페이지 발생시 시큐리티 페이지 전환
                .accessDeniedPage("/error");

    }
        @Autowired
        private MemberService memberService;

        @Override // DB의 있는 유저정보랑 mpassword 비교 ??
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(memberService).passwordEncoder(new BCryptPasswordEncoder());
            // userDetailsService
        }

    }


