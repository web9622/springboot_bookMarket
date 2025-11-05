package com.springboot.controller;

import com.springboot.domain.Board;
import com.springboot.domain.BoardFormDto;
import com.springboot.domain.Member;
import com.springboot.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/board")
public class BoardController {
    @Autowired
    private BoardService boardService;

    // 전체 게시글 목록 가져오기
    @GetMapping("/list")
    public String viewHomePage(Model model) {
        return viewPage(1, "id", "desc", model);//int pageNumber, String sortField, String sortDir
    }

    // 전체 게시글 가져오기
    @GetMapping("/page")
    public String viewPage(@RequestParam("pageNum") int pageNum,
                           @RequestParam("sortField") String sortField,
                           @RequestParam("sortDir") String sortDir, Model model
    ) {
        Page<Board> page = boardService.listAll(pageNum, sortField, sortDir);
        List<Board> listBoard = page.getContent();
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listBoard", listBoard);
        return "board/list";
    }

    // 게시글 글쓰기 페이지 출력하기
    @GetMapping("/write")
    public String post() {
        return "board/write";
    }

    // 게시글 글쓰기 저장하기
    @PostMapping("/write")
    public String write(BoardFormDto boardDto) {
        // 🚨 최종 조치: String 필드가 null 또는 빈 문자열일 경우 " " 공백으로 초기화

        // writerid/writer 필드가 null이거나 비어있을 경우 " "로 대체
        if (boardDto.getWriterid() == null || boardDto.getWriterid().isEmpty()) {
            boardDto.setWriterid(" ");
        }
        if (boardDto.getWriter() == null || boardDto.getWriter().isEmpty()) {
            boardDto.setWriter(" ");
        }

        // 제목/내용 필드가 null이거나 비어있을 경우 " "로 대체 (Optional: @Valid가 실패하지 않았을 경우)
        if (boardDto.getTitle() == null || boardDto.getTitle().isEmpty()) {
            boardDto.setTitle(" ");
        }
        if (boardDto.getContent() == null || boardDto.getContent().isEmpty()) {
            boardDto.setContent(" ");
        }

        boardService.savePost(boardDto);
        return "redirect:/board/list";
    }

    // 게시글 상세 보기
    // BoardController.java (requestUpdateBoardForm 메서드)

    // BoardController.java (requestUpdateBoardForm 메서드 - 최종 정리)

    @GetMapping("/view/{id}")
    public String requestUpdateBoardForm(@PathVariable(name = "id") Long id, HttpServletRequest httpServletRequest, Model model) {

        // 1. DTO를 반환하는 메서드만 호출
        BoardFormDto boardDto = boardService.getBoardDtoById(id);
        model.addAttribute("boardFormDto", boardDto); // 뷰로 전달

        // ✨ 2. 누락된 member 변수 선언 및 세션 획득 로직 복구
        HttpSession session = httpServletRequest.getSession(true);
        Member member = (Member) session.getAttribute("userLoginInfo"); // ⬅️ 이 코드가 필요합니다.

        model.addAttribute("buttonOk", false);

        // 3. 권한 체크 로직:
        if (member != null) {
            String loggedInMemberId = member.getMemberId();
            String boardWriterId = boardDto.getWriterid();

            if (boardWriterId != null && boardWriterId.equals(loggedInMemberId)) {
                model.addAttribute("buttonOk", true);
            }
        }

        return "board/view";
    }

    // 2. 수정한 데이터를 받아 처리하는 POST 또는 PUT 요청 ✨이곳에서 updatePost를 호출합니다.
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute BoardFormDto boardDto) {

        // ✨ BoardService의 수정 로직을 호출합니다.
        boardService.updatePost(id, boardDto);

        // 수정 완료 후 목록 페이지로 리다이렉트 (PRG 패턴)
        return "redirect:/board/list";
    }

    // 게시글 삭제하기
    @GetMapping("/delete/{id}")
    public String deleteBoard(@PathVariable(name = "id") Long id) {
        boardService.deleteBoardById(id);
        return "redirect:/board/list";
    }

}


