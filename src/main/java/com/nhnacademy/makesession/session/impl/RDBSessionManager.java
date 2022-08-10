//package com.nhnacademy.makesession.session.impl;
//
//import com.nhnacademy.makesession.session.SessionManager;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//
///**
// *
// *
// * @author : 유호철, 조재철
// * @since 1.0
// */
//@Profile("rdb")
//@Component
//public class RDBSessionManager implements SessionManager {
//
//    @Override
//    public void createSession() {
//
//    }
//
//    @Override
//    public void setAttribute(String key, Object value) {
//
//    }
//
//    @Override
//    public Object getAttribute(String key) {
//        return null;
//    }
//
//    @Override
//    public void removeAttribute(String key) {
//
//    }
//
//    @Override
//    public boolean isNew() {
//        return false;
//    }
//
//    @Override
//    public void invalidate() {
//
//    }
//
//    @Override
//    public String getId() {
//        return null;
//    }
//
//    @Override
//    public Cookie findCookie(String cookieName) {
//        return null;
//    }
//}
