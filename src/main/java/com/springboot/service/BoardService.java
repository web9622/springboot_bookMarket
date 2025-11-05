package com.springboot.service;

import com.springboot.domain.Board;
import com.springboot.domain.BoardFormDto;
import com.springboot.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    @Autowired
    public BoardRepository boardRepository;
    /// 게시글 등록

    @Transactional(readOnly = true)
    public BoardFormDto getBoardDtoById(Long id) {

        // BoardRepository는 BoardService에 @Autowired 되어 있어야 합니다.
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 ID를 찾을 수 없습니다."));

        // DTO 변환 로직은 DTO 파일에 위임합니다.
        return BoardFormDto.fromEntity(board);
    }
    @Transactional
    public Long savePost(BoardFormDto boardDto) {
        // DTO -> Entity 변환 후, DB에 INSERT
        return boardRepository.save(boardDto.toEntity()).getId();
    }
    // 게시글 수정
    @Transactional
    public void updatePost(Long id, BoardFormDto boardDto) {
        // 1. DB에서 영속 상태의 엔티티 조회
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        // 2. 엔티티 내부의 수정 메서드를 호출하여 필드 값 변경
        //    (단, Board 엔티티에 update(title, content) 메서드가 정의되어 있어야 함)
        board.update(boardDto.getTitle(), boardDto.getContent());

        // 3. save() 호출 없이 트랜잭션 종료 시 변경 감지(Dirty Checking)로 DB에 UPDATE 반영
    }

    
    // 전체 게시글 가져오기
    public List<BoardFormDto> getBoardList() {
        List<Board> boards = boardRepository.findAll();
        List<BoardFormDto> boardDtoList = new ArrayList<>();
        for (Board board : boards) {

            // ✨ DTO Builder에 Null 안전성을 적용
            BoardFormDto boardDto = BoardFormDto.builder()// BoardFormDto 빌더 시작
                    .id(board.getId())
                    .writerid(board.getWriterid())
                    .writer(board.getWriter())
                    .title(board.getTitle())
                    .content(board.getContent() != null ? board.getContent() : "")

                    // ✨ 수정: createdDate 필드에 대한 명시적 널 체크 추가
                    .createdDate(board.getCreatedDate() != null ? board.getCreatedDate() : null)
                    .modifiedDate(board.getModifiedDate() != null ? board.getModifiedDate() : null)
                    .build();

            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    // 게시글 삭제하기
    public void deleteBoardById(Long id) {
        boardRepository.deleteById(id);
    }

    // 전체 게시글의 목록 개수, 정렬 가져오기
    public Page<Board> listAll(int pageNumber, String sortField, String sortDir) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());
        return boardRepository.findAll(pageable);
    }
}
