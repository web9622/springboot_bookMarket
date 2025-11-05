package com.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class CategoryException extends RuntimeException{
	  private String errorMeaage;

	public CategoryException() {
		super();
		 this.errorMeaage = "요청한 도서 분야를 찾을 수 없습니다.";
	}
	  
}
