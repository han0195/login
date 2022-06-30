package login.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class) // 해당 엔티티를 감지 해라
@MappedSuperclass // 상속받은 자식 클래스내 부모클래스의 필드 생성
public class BaseTime {

    @CreatedDate //생성시간
    private LocalDateTime createdate;
    @LastModifiedDate  //수정시간
    private LocalDateTime modifiedate;
}
