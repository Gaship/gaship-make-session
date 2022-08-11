package com.nhnacademy.makesession.repository;

import com.nhnacademy.makesession.entity.SessionMap;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 설명작성란
 *
 * @author : 유호철, 조재철
 * @see JpaRepository
 * @since 1.0
 */
public interface SessionMapRepository extends JpaRepository<SessionMap, Integer> {
    SessionMap findByKey(String key);

    void deleteByKey(String key);
}
