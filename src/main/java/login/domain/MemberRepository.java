package login.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    //1. 아이디를 이요한 엔티티 검색
    Optional< MemberEntity> findBymid( String mid ); // select  sql 문법 없이 검색 메소드 생성

}
