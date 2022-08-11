package com.nhnacademy.makesession.service;

import com.nhnacademy.makesession.dto.LoginRequestDto;

/**
 * 인증관련 로직을 처리하는 서비스 클래스 입니다.
 *
 * @author : 유호철, 조재철
 * @since 1.0
 */
public interface AuthService {

    boolean login(LoginRequestDto loginRequestDto);
}
