package com.springboot.domain;

import lombok.*;

import java.time.LocalDateTime;

// ✨ DTO 객체 생성을 PROTECTED로 제한하고 @Data 대신 필요한 Getter/Setter만 유지
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED) // ✨ access level 설정
public class BoardFormDto {
    private Long id;
    private String writerid;
    private String writer;
    private String title;
    private String content;

    // ✨ @Builder.Default 제거 (Null 체크는 fromEntity에서 수행)
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    // ... (toEntity 메서드 유지) ...

    @Builder // ✨ @Builder는 유지하여 모든 필드 초기화 가능하도록 함
    public BoardFormDto(Long id, String writerid, String writer, String title, String
            content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.writerid = writerid;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    // ✨ fromEntity 메서드 유지 (content null 체크 로직 포함)
    // BoardFormDto.java (fromEntity 메서드 내부)

    public static BoardFormDto fromEntity(Board board) {
        // 1. DTO Builder 시작
        BoardFormDtoBuilder builder = BoardFormDto.builder()

                // 🚨 ID 필드: 널 체크 로직 대신, DB에서 가져온 값을 그대로 전달 (가장 간결)
                .id(board.getId())

                // ✨ 수정: 모든 String 필드의 널 대체 로직("?")을 제거하고, DB 값을 그대로 전달합니다.
                //    DTO Builder가 Null을 객체 타입으로 처리하도록 강제합니다.
                .writerid(board.getWriterid())
                .writer(board.getWriter())
                .title(board.getTitle())
                .content(board.getContent());

        // ✨ 2. LocalDateTime 필드에 대한 조건부 할당 (유지)
        //    이 로직은 null일 때 Builder 메서드를 아예 호출하지 않아 오류를 회피합니다.
        if (board.getCreatedDate() != null) {
            builder.createdDate(board.getCreatedDate());
        }

        if (board.getModifiedDate() != null) {
            builder.modifiedDate(board.getModifiedDate());
        }

        // 3. Builder 최종 실행
        return builder.build();
    }
    public Board toEntity() {
        // 🚨 이 메서드는 Board 엔티티에 Lombok의 @Builder가 있다고 가정합니다.

        // DTO의 필드 값을 사용하여 Board 엔티티 객체를 생성
        Board board = Board.builder()
                .id(this.id)
                .writerid(this.writerid)
                .writer(this.writer)
                .title(this.title)
                .content(this.content)
                // createdDate, modifiedDate는 JPA Auditing이 처리하므로 제외
                .build();

        return board;
    }
}