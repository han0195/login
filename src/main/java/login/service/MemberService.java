package login.service;

import login.domain.MemberEntity;
import login.domain.MemberRepository;
import login.dto.LoginDto;
import login.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService implements UserDetailsService{
    // UserDetailsService 인터페이스 : 일반회원

    @Autowired
    private MemberRepository memberRepository;

    //회원가입
    @Transactional
    public boolean signup( MemberDto memberDto){
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

        authorityList.add( new SimpleGrantedAuthority(memberEntity.getrolekey())); // 리스트의 인증된 엔티티의 키를 보관

        return new LoginDto( memberEntity, authorityList); //회원엔티티, 인증된 리스트를 인증세션 부여
    }

}
