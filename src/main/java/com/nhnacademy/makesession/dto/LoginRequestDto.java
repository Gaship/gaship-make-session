package com.nhnacademy.makesession.dto;

import lombok.Data;

/**
 * 로그인 요청시 전달하는 데이터를 담은 Dto 클래스 입니다.
 *
 * @author : 유호철, 조재철
 * @since 1.0
 */
@Data
public class LoginRequestDto {

    private final String username;

    private final String password;

}
