package org.example.expert.log.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String errorMessage;

    @CreatedDate
    private LocalDateTime createAt;

    public Log(String nickname, String errorMessage) {
        this.nickname = nickname;
        this.errorMessage = errorMessage;
        this.createAt = LocalDateTime.now();
    }
}
