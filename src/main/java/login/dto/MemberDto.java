package login.dto;

import login.domain.MemberEntity;
import login.domain.Role;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private int mno; // 회원번호
    private String mid; // 회원아이디
    private String mpassword;// 회원 비밀번호
    private String mname; // 회원이름
    private String memail; // 회원이메일

    //entity 변환 메소드
    public MemberEntity toentity(){

        //암호화 객체 선언
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return MemberEntity.builder()
                .mid(this.mid)
                .mpassword(passwordEncoder.encode(this.mpassword)) //패스워드 암호화
                .mname(this.mname)
                .memail(this.memail)
                .role(Role.MEMBER) // 멤버권한 부여
                .build();
    }

}
