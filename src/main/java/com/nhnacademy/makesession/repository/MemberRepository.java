package com.nhnacademy.makesession.repository;

import com.nhnacademy.makesession.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Member entity 의 CRUD 기능을 제공하는 클래스 입니다.
 *
 * @author : 유호철, 조재철
 * @since 1.0
 */
public interface MemberRepository extends JpaRepository<Member, String> {

}
