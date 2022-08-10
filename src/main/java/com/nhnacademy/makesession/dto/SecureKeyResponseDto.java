package com.nhnacademy.makesession.dto;

import lombok.Getter;

/**
 * SecureKey 에 대한 정보를 받고 다른 객체에 전달하기 위한 Dto 입니다.
 *
 * @author 유호철, 조재철
 * @version 1.0.0
 */
@Getter
public class SecureKeyResponseDto {

    private Header header;

    private Body body;

    /**
     * SecureKey Field 중 Header 에 대한 클래스.
     *
     * @version 1.0.0
     */
    @Getter
    public static class Header {

        private Integer resultCode;
        private String resultMessage;
        private Boolean isSuccessful;
    }

    /**
     * SecureKey 에 Field 중 Body 에 대한 클래스.
     *
     * @version 1.0.0
     */
    @Getter
    public static class Body {

        private String secret;
    }

}

