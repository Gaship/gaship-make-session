package com.nhnacademy.makesession.config;

import com.nhnacademy.makesession.interceptor.SessionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebdMvcConfigurer를 구현하여 기본적으로 제공해주는 interceptor 부분을 오버라이딩 하여 Interceptor 커스텀 하는 클래스입니다.
 *
 * @author : 유호철, 조재철
 * @since 1.0
 */

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**");
    }
}
