package login.domain;

import lombok.*;


import javax.persistence.*;

@Entity
@Table(name="member2")
@Builder
@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
public class MemberEntity extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno; // 회원번호
    private String mid; // 회원아이디
    private String mpassword; // 회원비밀번호
    private String mname; // 회원이름
    private String memail; // 회원이메일

    @Enumerated(EnumType.STRING)// 열거형 이름
    private Role role; // 열거형 데이터

    public String getrolekey() { return role.getKey(); } // 키반환 메소드

}
