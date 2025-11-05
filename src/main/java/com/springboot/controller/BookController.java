package com.springboot.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.domain.Book;
import com.springboot.service.BookService;
import com.springboot.validator.BookValidator;
import com.springboot.validator.UnitsInStockValidator;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import com.springboot.exception.CategoryException;

import jakarta.servlet.http.HttpServletRequest;
import com.springboot.exception.BookIdException;


@Controller
@RequestMapping(value = "/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@Value("${file.uploadDir}")
	 String fileDir;
	
	@Autowired
	 private BookValidator bookValidator; // BookValidator 인스턴스 선언
	
	@GetMapping
	public String requestBookList(Model model) {
		List<Book> list = bookService.getAllBookList();
		model.addAttribute("bookList", list);
		return "books";
	}
	
	@GetMapping("/all")   
	public ModelAndView requestAllBooks() {
		ModelAndView modelAndView = new ModelAndView(); 
		List<Book> list = bookService.getAllBookList();
		modelAndView.addObject("bookList", list); 
		modelAndView.setViewName("books"); 
		return modelAndView;
	}
	
	@GetMapping("/{category}") 	
	public String requestBooksByCategory(
			@PathVariable("category") String bookCategory, Model model) {
	    List<Book> booksByCategory =bookService.getBookListByCategory(bookCategory); 
	    
	    if (booksByCategory == null || booksByCategory.isEmpty()) {
			throw new CategoryException();
	    	//throw new IllegalArgumentException("도서ID가 인 해당 도서를 찾을 수 없습니다.");
	    	
		}
	   
	    model.addAttribute("bookList", booksByCategory); 
	    return "books"; 
	 }
	@GetMapping("/books")
	public String books(Model model, HttpServletRequest request) {

		// 세션에서 memberId 가져오기
		HttpSession session = request.getSession(false);
		String memberId = null;

		if (session != null) {
			memberId = (String) session.getAttribute("memberId");
		}

		// Model에 memberId 추가 (Thymeleaf에서 사용)
		model.addAttribute("memberId", memberId);

		// 책 목록도 추가
		// List<Book> bookList = bookService.getAllBooks();
		// model.addAttribute("bookList", bookList);

		return "books";
	}
	@GetMapping("/publisher/{publisherName}")
	public String requestBooksByPublisher(
			@PathVariable("publisherName") String publisherName, Model model) {

		// 1. Service 호출 (findByPublisher 메서드가 Service에 존재한다고 가정)
		List<Book> booksByPublisher = bookService.getBookListByPublisher(publisherName);

		// 2. 모델에 추가
		model.addAttribute("bookList", booksByPublisher);

		// 3. 뷰 반환
		return "books";
	}
	
	@GetMapping("/filter/{bookFilter}")
	public String requestBooksByFilter(
	    @MatrixVariable(pathVar="bookFilter") Map<String,List<String>> bookFilter, Model model) {
	    Set<Book> booksByFiter = bookService.getBookListByFilter(bookFilter);
	    model.addAttribute("bookList", booksByFiter);
	    return "books";
	}
	
	@GetMapping("/book") 
	public String requestBookById(@RequestParam("id") String bookId, Model model) {  
	   Book bookById = bookService.getBookById(bookId);
	   model.addAttribute("book", bookById );
	   return "book";
	}
	
	 @GetMapping("/add")
	 public String requestAddBookForm(Model model) {
		 model.addAttribute("book", new Book()); //유효검사기 추가
		 return "addBook";
	 }

	@GetMapping("/update/{bookId}") // ✨ HTML 링크와 정확히 일치하는 경로
	public String requestUpdateBookForm(@PathVariable("bookId") String bookId, Model model) {

		// 1. Service 호출: 도서 ID를 사용하여 DB에서 기존 도서 정보를 조회
		Book bookById = bookService.getBookById(bookId);

		// 2. 모델 바인딩: 조회된 도서 정보를 "book"이라는 이름으로 뷰에 전달
		model.addAttribute("book", bookById);

		// 3. 뷰 반환: 수정 폼 템플릿의 이름 (bookController의 @PostMapping("/update")를 보면 "updateForm"일 가능성이 높음)
		return "updateForm";
	}

	@PutMapping("/update/{bookId}") // ✨ PUT 요청을 처리 (폼의 _method=put에 의해 호출)
	public String submitUpdateBook(
			@PathVariable("bookId") String bookId, // URL에서 bookId를 받아옴 (사용은 필수 아님)
			@Valid @ModelAttribute("book") Book book, // 수정된 데이터와 MultipartFile 포함
			BindingResult bindingResult) {

		// 1. 유효성 검사
		if (bindingResult.hasErrors()) {
			// 오류 발생 시 다시 수정 폼으로 돌아감
			return "updateForm";
		}

		MultipartFile bookImage = book.getBookImage();

		// 2. 이미지 업로드 처리 (새 이미지가 첨부된 경우에만)
		if (bookImage != null && !bookImage.isEmpty()) {
			try {
				String originalFilename = bookImage.getOriginalFilename();
				String uniqueFileName = java.util.UUID.randomUUID().toString() + "_" + originalFilename;

				// 이미 확인된 안전한 절대 경로 생성 로직 사용
				File uploadDirectory = new File(fileDir).getAbsoluteFile();
				if (!uploadDirectory.exists()) {
					uploadDirectory.mkdirs();
				}

				File saveFile = new File(uploadDirectory, uniqueFileName);
				bookImage.transferTo(saveFile);

				// DB에 저장할 파일 이름 업데이트
				book.setFileName(uniqueFileName);

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("도서 이미지 업데이트가 실패하였습니다: " + e.getMessage(), e);
			}
		} else if (book.getFileName() != null && !book.getFileName().isEmpty()) {
			// 새 이미지가 없어도 기존 이미지를 유지하는 경우 (updateForm.html 구현 방식에 따라 다름)
			// 이 경우, Book 객체에 기존 파일 이름이 이미 담겨 있어야 합니다.
		} else {
			book.setFileName(null);
		}

		// 3. 서비스 호출: JPA 변경 감지 로직 실행
		bookService.setUpdateBook(book);

		// 4. 리다이렉트: 수정된 도서의 상세 페이지로 이동
		return "redirect:/books/book?id=" + book.getBookId();
	}
	@PostMapping("/add")
	public String submitAddNewBook(@Valid @ModelAttribute Book book,  BindingResult bindingResult) {

		MultipartFile bookImage = book.getBookImage();

		if (bookImage != null && !bookImage.isEmpty()) {
			try {
				String originalFilename = bookImage.getOriginalFilename();
				String uniqueFileName = java.util.UUID.randomUUID().toString() + "_" + originalFilename;

				// ✨ 1. 절대 경로 획득 및 폴더 생성 로직 복구
				File uploadDirectory = new File(fileDir).getAbsoluteFile();
				if (!uploadDirectory.exists()) {
					uploadDirectory.mkdirs(); // 디렉터리 생성
				}

				// 2. 최종 파일 저장 경로 생성
				File saveFile = new File(uploadDirectory, uniqueFileName);

				// 파일 저장 실행
				bookImage.transferTo(saveFile);

				// 3. DB에 순수 파일 이름만 저장 (가장 중요)
				book.setFileName(uniqueFileName);

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("도서 이미지 업로드가 실패하였습니다: " + e.getMessage(), e);
			}
		} else {
			book.setFileName(null);
		}

		bookService.setNewBook(book);
		return "redirect:/books";
	}
	
	@GetMapping("/delete/{bookId}")
	public String deleteBook(@PathVariable("bookId") String bookId){
		bookService.deleteBook(bookId);
		return "redirect:/books";
	}

	 
	 @ModelAttribute 
	 public void addAttributes(Model model) { 
	     model.addAttribute("addTitle", "신규 도서 등록");
	 }
	 
	 @InitBinder
	 public void initBinder(WebDataBinder binder) {
		 //binder.setValidator(unitsInStockValidator); 
		binder.setValidator(bookValidator); 
		 binder.setAllowedFields("bookId","name","unitPrice","author","description","publisher","category",
	                            "unitsInStock","totalPages", "releaseDate", "condition", "bookImage");
	 }
	 @ExceptionHandler(value={BookIdException.class}) 
	    public ModelAndView handleError(HttpServletRequest req, BookIdException exception) {
		   ModelAndView mav = new ModelAndView(); 
		   mav.addObject("invalidBookId", exception.getBookId()); 
		   mav.addObject("exception", exception); 
		   mav.addObject("url", req.getRequestURL()+"?"+req.getQueryString()); 
		   mav.setViewName("errorBook");
		   return mav; 
	   }
}
