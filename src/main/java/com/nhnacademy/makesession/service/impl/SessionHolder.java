package com.nhnacademy.makesession.service.impl;

/**
 * ThreadLocal에 Session을 저장하기 위한 클래스 입니다.
 *
 * @author : 유호철, 조재철
 * @since 1.0
 */

public class SessionHolder {

    private static final ThreadLocal<String> GASHIP_SESSION = new ThreadLocal<>();

    public static void setSessionId(String sessionCookie) {
        GASHIP_SESSION.set(sessionCookie);
    }

    public static String getSessionId() {
        return GASHIP_SESSION.get();
    }

    public static void removeSessionId() {
        GASHIP_SESSION.remove();
    }
}
