package com.nhnacademy.makesession.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 설명작성란
 *
 * @author : 유호철, 조재철
 * @since 1.0
 */

@Entity
@Table(name = "session_map")
@Getter
@NoArgsConstructor
public class SessionMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_value_no")
    private Integer sessionNo;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private SessionId sessionId;

    @Column(name = "session_key")
    private String key;

    @Column(name = "session_value")
    private String value;

    public SessionMap(SessionId sessionId, String key, String value) {
        this.sessionId = sessionId;
        this.key = key;
        this.value = value;
    }
}
