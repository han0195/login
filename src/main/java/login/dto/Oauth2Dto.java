package login.dto;

import login.domain.MemberEntity;
import login.domain.Role;
import lombok.*;

import java.lang.reflect.Member;
import java.util.Map;
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@ToString@Builder
public class Oauth2Dto {

    private String mid; // 회원 아이디
    private String registrationId; // 어떤경로로그인이지 식별자 아이디
    private Map<String, Object> attributes; // 로인 정보
    private String nameAttributeKey; // 사용자를 액세스하는 속성

    public static Oauth2Dto sns확인(String registrationId, Map<String, Object> attributes,String nameAttributeKey){
        if(registrationId.equals("naver")){ // 만약 식별자가 네이버라면
            return 네이버변환(registrationId, attributes, nameAttributeKey);
        }else if(registrationId.equals("kakao")){ // 만약 식별자가 카카오라면
            return 카카오변환(registrationId, attributes, nameAttributeKey);
        }else{
            return null;
        }
    }

    public static Oauth2Dto 네이버변환(String registrationId, Map<String, Object> attributes,String nameAttributeKey){
        Map<String, Object> response = (Map<String, Object>) attributes.get(nameAttributeKey); // response value 값 가져오기
        return  Oauth2Dto.builder()
                .mid((String)response.get("email")) // email 키값 가져오기
                .build();// 이메일 빌더후 리턴
    }
    public static Oauth2Dto 카카오변환(String registrationId, Map<String, Object> attributes,String nameAttributeKey){
        Map<String, Object> kakao_accountMap = (Map<String, Object>) attributes.get(nameAttributeKey);// response value 값 가져오기
        return Oauth2Dto.builder()
                .mid((String)kakao_accountMap.get("email"))// email 키값 가져오기
                .build();// 이메일 빌더후 리턴
    }
    public MemberEntity toenMemberEntity(){
        return MemberEntity.builder().mid(this.mid).role(Role.MEMBER).build();
    }

}
