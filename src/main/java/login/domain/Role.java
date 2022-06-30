package login.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor// 생성자
@Getter
public enum Role { // enum 열거형 데이터

    MEMBER("ROLE_MEMBER","회원"),
    INTERME("ROLE_INTERME" , "중개인"),
    ADMIN("ROLE_ADMIN" , "관리자");

    //열거형의 들어가는 핃드 항목들
    private final String key;
    private final String title;


}
