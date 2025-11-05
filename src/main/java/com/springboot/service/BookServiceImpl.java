package com.springboot.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional; // Optional 임포트

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.domain.Book;
import com.springboot.repository.BookRepository;
import com.springboot.exception.BookIdException; // 예외 처리용

@Service
@Transactional(readOnly = true) // 모든 조회 메서드에 readOnly 트랜잭션 적용
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	// 1. 전체 도서 목록 조회 (getAllBookList -> JPA findAll로 대체)
	public List<Book> getAllBookList() {
		// JPA 표준 메서드 사용: SELECT * 쿼리 자동 전송
		return bookRepository.findAll();
	}

	// 2. 카테고리별 도서 조회 (getBookListByCategory -> findByCategory로 대체)
	public List<Book> getBookListByCategory(String category) {
		// JPA 쿼리 메서드 사용: SELECT * FROM book WHERE category = ? 쿼리 자동 전송
		return bookRepository.findByCategory(category);
	}
	// ✨ Service 인터페이스 메서드 구현
	public List<Book> getBookListByPublisher(String publisherName) {
		// Repository의 JPA 쿼리 메서드를 호출하여 DB에 쿼리를 전송합니다.
		return bookRepository.findByPublisher(publisherName);
	}

	// 3. 필터 도서 조회 (수동 로직을 JPA @Query 기반으로 구현)
	public Set<Book> getBookListByFilter(Map<String, List<String>> filter) {

		// Map에서 필터 조건 추출
		List<String> publishers = filter.get("publisher");
		List<String> categories = filter.get("category");

		// Repository의 @Query 메서드 호출 (findByFilter는 Repository에 정의되어 있어야 함)
		// findByFilter는 List<Book>을 반환한다고 가정
		List<Book> filteredList = bookRepository.findByFilter(publishers, categories);

		// Set으로 변환하여 반환
		return new HashSet<>(filteredList);
	}

	// 4. ID 기준 단일 도서 조회 (getBookById -> findById로 대체)
	public Book getBookById(String bookId) {
		// JPA 표준 findById를 호출하고, Optional을 처리하여 예외를 발생시킵니다.
		// bookById는 JPA findById의 반환 타입인 Optional<Book>을 처리해야 합니다.
		Optional<Book> bookById = bookRepository.findById(bookId);

		// 결과가 없으면 BookIdException 발생
		return bookById.orElseThrow(() -> new BookIdException(bookId));
	}

	// 5. 새 도서 등록 (setNewBook -> JPA save로 대체)
	@Transactional // 쓰기 작업이므로 트랜잭션 재정의 (클래스 레벨의 readOnly=true를 무시하고 트랜잭션 재정의)
	public void setNewBook(Book book) {
		// JPA 표준 save 호출: INSERT 쿼리 자동 전송
		bookRepository.save(book);
	}
	// ✨ 6. 도서 정보 수정 (setUpdateBook 구현)
	@Transactional // ✨ 쓰기 작업이므로 트랜잭션 필수
	public void setUpdateBook(Book updateBook) {
		// 1. DB에서 기존 엔티티를 조회하여 영속성 컨텍스트에 로드
		Book existingBook = bookRepository.findById(updateBook.getBookId())
				.orElseThrow(() -> new BookIdException(updateBook.getBookId()));

		// 2. 필드 값 변경 (변경 감지 대상)
		// Controller에서 넘어온 updateBook의 데이터로 existingBook의 필드를 업데이트합니다.
		existingBook.setName(updateBook.getName());
		existingBook.setUnitPrice(updateBook.getUnitPrice());
		existingBook.setAuthor(updateBook.getAuthor());
		existingBook.setDescription(updateBook.getDescription());
		existingBook.setPublisher(updateBook.getPublisher());
		existingBook.setCategory(updateBook.getCategory());
		existingBook.setUnitsInStock(updateBook.getUnitsInStock());
		existingBook.setReleaseDate(updateBook.getReleaseDate());
		existingBook.setCondition(updateBook.getCondition());

		// 이미지 파일이 새로 업로드된 경우에만 파일명을 변경
		if (updateBook.getFileName() != null && !updateBook.getFileName().isEmpty()) {
			existingBook.setFileName(updateBook.getFileName());
		}

		// 3. 별도의 save() 호출 없이 트랜잭션 종료 시 자동 UPDATE (변경 감지)
	}
	// 7. 도서 삭제 (deleteBook -> JPA deleteById로 대체)
	@Transactional // 쓰기 작업이므로 트랜잭션 필수
	public void deleteBook(String bookId){
		if (!bookRepository.existsById((bookId))) {
		throw new BookIdException(bookId);
		} else {
			bookRepository.deleteById(bookId);
		}

		}

}