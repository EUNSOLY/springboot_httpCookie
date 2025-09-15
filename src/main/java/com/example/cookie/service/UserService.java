package com.example.cookie.service;

import com.example.cookie.db.LoginRequest;
import com.example.cookie.db.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public void login(
            LoginRequest loginRequest,
            HttpServletResponse httpServletResponse
    ){
        var id = loginRequest.getId();
        var password = loginRequest.getPassword();

        var optionalUser = userRepository.findByName(id);

        if(optionalUser.isPresent()){
            var userDto = optionalUser.get();
            if(userDto.getPassword().equals(password)){
                // 쿠키 해당 정보 저장
                // JWT 토큰이 될수도..특정값  내려주기
                var cookie = new Cookie("authorization-cookie", userDto.getId());
                cookie.setDomain("localhost"); // 특정 도메인값 넣어주기 (회사일경우 회사 도메인) ex) naver.com, daum.net 등등
                cookie.setPath("/"); // 경로지정
                cookie.setMaxAge(-1); // session과 동일 -1 : 연결된 동안만 사용

                httpServletResponse.addCookie(cookie);

            }else{
                throw new RuntimeException("패스워드가 맞지않습니다.");
            }
        }else{
            throw new RuntimeException("유저가 존재 하지 않습니다.");
        }
    }
}
