package login.service;

import login.domain.MemberEntity;
import login.domain.MemberRepository;
import login.dto.LoginDto;
import login.dto.MemberDto;
import login.dto.Oauth2Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class MemberService implements UserDetailsService,
        OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    // UserDetailsService 인터페이스 : 일반회원
    // OAuth2UserService<OAuth2UserRequest, OAuth2User> : OAuth2User 은 UserDetail 이랑 동일한 역활 수행

    @Autowired
    private MemberRepository memberRepository;

    //회원가입
    @Transactional
    public boolean signup(MemberDto memberDto) {
        MemberEntity memberEntity = memberDto.toentity(); // 엔티티변환
        // entitity 저장
        memberRepository.save(memberEntity); // 저장
        return true;
    }

    //로그인 서비스
    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
        Optional<MemberEntity> entityOptional = memberRepository.findBymid(mid); // 회원 아이디로 엔티티찾기
        MemberEntity memberEntity = entityOptional.orElse(null); // 비어있다면 반환할 객체

        List<GrantedAuthority> authorityList = new ArrayList<>(); //권한을 담을 리스트 선언

        authorityList.add(new SimpleGrantedAuthority(memberEntity.getrolekey())); // 리스트의 인증된 엔티티의 키를 보관

        return new LoginDto(memberEntity, authorityList); //회원엔티티, 인증된 리스트를 인증세션 부여
    }


    //Oauth2 로그인 서비스
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 인증[ 로그인 ] 결과 정보 요청 / DefaultOAuth2UserService : Oauth2UserService의 구현체이다.
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();

        //oAuth2UserService[인터페이스] = loadUser : Oauth2User을 반환
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // getRegistrationID(); 식별자를 반환한다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 사용자 정보 응답에서 사용자 이름에 액세스하는 사용되는 속성 을 반환
        String nameAttributeKey = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName(); // 회원정보

        System.out.println("클라이언트 등록 이름: " + registrationId);
        System.out.println("회원 정보(json)호출시 사용되는 키: " + nameAttributeKey);
        System.out.println("인증 정보(로그인) 결과내용: " + oAuth2User.getAttributes());

        // 토큰 값 반환 [로그인정보]
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // db 저장을 위한 Dto
        Oauth2Dto oauth2Dto = Oauth2Dto.sns확인(registrationId, attributes, nameAttributeKey);

        // mid 와 동일한값 찾기
        Optional<MemberEntity> optional = memberRepository.findBymid(oauth2Dto.getMid());
        if(!optional.isPresent()){ // DB의 동일한 데이터가없다면
            memberRepository.save(oauth2Dto.toenMemberEntity()); // DB 저장
        }else{// DB의 동일한 데이터가 있다면
            // 로그인 한적이 있으면 db 업데이트 처리
        }
        return new DefaultOAuth2User(
                //매개변수 1. : GrantedAuthority 상속받은 컬렉션 프레임워크
                //        2. : 토큰 값 [ 로그인정보 ]
                //        3. : 액세스시 사용되는 key값
                Collections.singleton(new SimpleGrantedAuthority("SNS사용자")),
                attributes, nameAttributeKey);
        //SimpleGrantedAuthority Role 저장
    }

    public String 인증된회원아이디호출(){
        //SecurityContextHolder : SecurityContext와 연결
        //getContext : security context 리턴
        //getAuthentication : Authentication 리턴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("입증정보: "+ authentication);

        // getPrincipal = 로그인정보 리턴
        Object principal = authentication.getPrincipal();

        String mid = null;

        if(principal != null){ // 로그인 정보가 null 아니라면
            if(principal instanceof UserDetails ){//일반회원
                mid = (((UserDetails) principal).getUsername()); // 회원이름저장
            }else if(principal instanceof DefaultOAuth2User){// oauth회원
                Map<String, Object> attributes = ((DefaultOAuth2User) principal).getAttributes();
                if(attributes.get("response") != null){
                    Map<String, Object> map = (Map<String, Object>) attributes.get("response"); //회원정보저장
                    mid = map.get("email").toString(); // 이메일빼오기
                }else{
                    Map<String, Object> map = (Map<String, Object>) attributes.get("kakao_account");//회원정보저장
                    mid = map.get("email").toString();// 이메일빼오기
                }
            }
            return mid; // 리턴
        }else{
            // 인증(로그인)이 안되어 있는 상태
            return mid;
        }
    }
}


