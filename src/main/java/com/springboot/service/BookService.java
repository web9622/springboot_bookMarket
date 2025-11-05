// BookService.java (추정되는 인터페이스 내용)

package com.springboot.service;

import com.springboot.domain.Book;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BookService {

	List<Book> getAllBookList();
	List<Book> getBookListByCategory(String category);
	Set<Book> getBookListByFilter(Map<String, List<String>> filter);
	Book getBookById(String bookId);
	void setNewBook(Book book);
	// ✨ Controller에서 호출하는 메서드 선언을 추가
	List<Book> getBookListByPublisher(String publisherName);
	// BookService.java 파일에 추가
	void setUpdateBook(Book book);
	void deleteBook(String bookId);
}