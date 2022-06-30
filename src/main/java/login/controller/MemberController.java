package login.controller;

import login.dto.MemberDto;
import login.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // 템플릿 영역
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;  // member 서비스 객체 선언

    // 1. 로그인 페이지 이동 매핑
    @GetMapping("/login")
    public String login( ){
        return "member/login";
    }

    // 2. 회원가입 페이지 이동 매핑
    @GetMapping("/signup")
    public String signup(){
        return "/member/signup";
    }

    // 3. 회원가입 처리 매핑
    @PostMapping("/signup")
    @ResponseBody
    public boolean save( MemberDto memberDto ){
        System.out.println("컨트롤접근");
        // 서비스 호출
        boolean result =  memberService.signup( memberDto);
        return result;
    }

}












