package com.nhnacademy.makesession.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;

/**
 * 회원 관련 엔티티 클래스 입니다.
 *
 * @author : 유호철, 조재철
 * @since 1.0
 */
@Getter
@Entity
public class Member {

    @Id
    private String username;

    private String password;

}
