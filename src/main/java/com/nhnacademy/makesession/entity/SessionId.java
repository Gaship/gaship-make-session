package com.nhnacademy.makesession.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 설명작성란
 *
 * @author : 유호철, 조재철
 * @since 1.0
 */
@Entity
@Table(name = "session_id")
@NoArgsConstructor
@Getter
public class SessionId {
    @Id
    @Column(name = "session_id")
    private String id;

    private LocalDateTime sessionTime;

    @OneToMany(
        mappedBy = "sessionId",
        orphanRemoval = true,
        cascade = CascadeType.ALL)
    private List<SessionMap> sessionMaps = new ArrayList<>();

    public SessionId(String id, LocalDateTime sessionTime) {
        this.id = id;
        this.sessionTime = sessionTime;
    }

    public void add(SessionMap map){
        sessionMaps.add(map);
    }

    public void addTime(int time){
        this.sessionTime = this.sessionTime.plusSeconds(time);
    }
}
