package com.nhnacademy.makesession.service.impl;

import com.nhnacademy.makesession.dto.LoginRequestDto;
import com.nhnacademy.makesession.repository.MemberRepository;
import com.nhnacademy.makesession.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 인증관련 로직을 처리하는 서비스 클래스 입니다.
 *
 * @author : 유호철, 조재철
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    @Override
    public boolean login(LoginRequestDto loginRequestDto) {
        return memberRepository.findById(loginRequestDto.getUsername())
                               .filter(member -> loginRequestDto.getPassword()
                                                                .equals(member.getPassword()))
                               .isPresent();
    }
}
