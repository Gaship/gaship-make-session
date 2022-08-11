package com.nhnacademy.makesession.aspect;

import com.nhnacademy.makesession.service.impl.SessionHolder;
import com.nhnacademy.makesession.session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author : 조재철
 * @since 1.0
 */
@Aspect
@RequiredArgsConstructor
public class SessionExpireResetAspect {

    private final SessionManager sessionManager;

    @After("@annotation(com.nhnacademy.makesession.aspect.annotation.SessionReset)")
    public void sessionExpireReset() {
        sessionManager.resetSessionExpireTime(SessionHolder.getSessionId());
    }
}
