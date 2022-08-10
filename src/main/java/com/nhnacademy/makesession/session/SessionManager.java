package com.nhnacademy.makesession.session;

import javax.servlet.http.Cookie;

/**
 * @author : 조재철
 * @since 1.0
 */
public interface SessionManager {

    String SESSION_COOKIE_NAME = "gashipSessionId";

    void createSession();

    void setAttribute(String key, Object value);

    Object getAttribute(String key);

    void removeAttribute(String key);

    boolean isNew();

    void invalidate();

    String getId();

    long getCreationTime() //세션이 생성된 시간을 January 1 ,1970 GMT 부터 long 형 밀리세컨드 값으로 반환
    ;

//    long getLastAccessedTime()  //웹 브라우저의 요청이 마지막으로 시도된 시간을 long 형 ms 값으로 반환
//    ;

    void setMaxInactiveInterval(int second)  //세션을 유지할 시간을 초단위로 설정 합니다.
    ;

    int getMaxInactiveInterval() //세션의 유효시간을 초 단위로 반환 합니다. 기본값은 30초 입니다.
    ;

    Cookie findCookie(String cookieName);
}
