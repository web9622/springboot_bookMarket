package com.springboot.exception;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class BookIdException extends RuntimeException {

	private String bookId;

	public BookIdException(String bookId) { //생성자
		this.bookId = bookId;
	}

	/*public String getBookId() {  //Getter() 메소드
		return bookId;
	}
	*/
}
