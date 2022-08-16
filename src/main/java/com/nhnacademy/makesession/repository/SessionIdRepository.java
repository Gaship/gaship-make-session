package com.nhnacademy.makesession.repository;

import com.nhnacademy.makesession.entity.SessionId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 설명작성란
 *
 * @author : 유호철, 조재철
 * @see JpaRepository
 * @since 1.0
 */
public interface SessionIdRepository extends JpaRepository<SessionId,String> {
}
