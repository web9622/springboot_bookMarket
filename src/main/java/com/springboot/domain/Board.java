package com.springboot.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Board {
    // ✨ 1. 시퀀스 생성기를 명시합니다. (전략 이름과 allocationSize=1 설정)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq_gen")
    @SequenceGenerator(
            name = "board_seq_gen",
            sequenceName = "board_seq", // DB에 생성될 시퀀스 이름
            initialValue = 1,
            allocationSize = 1 // 🚨 핵심 수정: ID 할당 크기를 1로 설정
    )
    @Id
    private Long id;
    @Column(length = 10, nullable = false)
    private String writerid;
    @Column(length = 10, nullable = false)
    private String writer;
    @Column(length = 100, nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @CreatedDate
//    @Builder.Default // ✨ @Builder.Default 사용 시 기본값 할당
    @Column(updatable = false)
    private LocalDateTime createdDate; // ✨ 초기값 할당 제거

    @LastModifiedDate
    private LocalDateTime modifiedDate; // ✨ 초기값 할당 제거
    @Builder
    public Board(Long id, String writerid, String writer, String title, String content) {
        this.id= id;
        this.writerid = writerid;
        this.writer = writer;
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        // 받은 매개변수로 내부 필드 값을 변경합니다.
        this.title = title;
        this.content = content;

        // 참고: modifiedDate는 @LastModifiedDate에 의해 자동으로 업데이트됩니다.
    }

}
