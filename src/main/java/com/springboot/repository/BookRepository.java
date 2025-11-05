package com.springboot.repository;

import com.springboot.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Set;
import java.util.Optional; // findById의 반환 타입 변경을 위해 사용

// JpaRepository<엔티티 타입, 기본 키(ID) 타입>을 상속합니다.
// JpaRepository는 findAll(), save(), findById() 등을 이미 제공합니다.
public interface BookRepository extends JpaRepository<Book, String> {

	// 1. 카테고리별 조회 (이미 정의됨)
	// - Spring Data JPA가 자동으로 SELECT * FROM book WHERE category = ? 쿼리를 생성합니다.
	List<Book> findByCategory(String category);


	/* * 2. ID 기준 단일 도서 조회:
	 * JpaRepository의 findById(String id)가 이미 존재하므로 별도 선언이 필요 없습니다.
	 * 단, Service 계층에서 Optional<Book>을 Book으로 변환하고 예외를 던지는 로직이 필요합니다.
	 */


	/* * 3. 전체 도서 목록 조회:
	 * JpaRepository의 findAll()이 이미 존재하므로 별도 선언이 필요 없습니다.
	 */

	/* * 4. 새로운 도서 추가:
	 * JpaRepository의 save(Book book)이 이미 존재하므로 별도 선언이 필요 없습니다.
	 */


	// 5. 복잡한 필터링 조회 (JpaRepository가 자동 지원하지 않으므로, @Query 예시를 추가)
	//    실제 복잡한 로직은 Service 계층에서 구현하는 것이 일반적입니다.
	//    이 메서드는 JPQL(Java Persistence Query Language)을 사용하여 DB에서 필터링합니다.
	@Query("SELECT b FROM Book b WHERE " +
			"(:publisher IS NULL OR b.publisher IN :publisher) AND " +
			"(:category IS NULL OR b.category IN :category)")
	List<Book> findByFilter(@Param("publisher") List<String> publisher,
							@Param("category") List<String> category);

	// *주의: getBookListByFilter 메서드는 Service 계층에서 Map<String, List<String>>을 처리해야 합니다.
	//        Repository는 위와 같이 List<String> 형태의 파라미터를 받는 것이 일반적입니다.

	List<Book> findByPublisher(String publisher);

}