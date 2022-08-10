package com.nhnacademy.makesession.controller;

import com.nhnacademy.makesession.dto.LoginRequestDto;
import com.nhnacademy.makesession.service.AuthService;
import com.nhnacademy.makesession.session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 로그인 관련 컨트롤러 클래스 입니다.
 *
 * @author : 유호철, 조재철
 * @since 1.0
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final SessionManager sessionManager;
    private final AuthService authService;

    @GetMapping(value = "/")
    public String main() {
        return "main";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @PostMapping(value = "/login")
    public String login(@ModelAttribute LoginRequestDto loginRequestDto) {

        if (!authService.login(loginRequestDto)) {
            return "login";
        }

        sessionManager.createSession();

        sessionManager.setAttribute("sessionId", loginRequestDto.getUsername());

        return "redirect:/";
    }

//    @GetMapping(value = "/logout")
//    public String logout() {
//        return ""
//    }
}
