package com.springboot.domain;

import java.math.BigDecimal;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.validator.BookId;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data 
@Entity
@Table(name="book")
public class Book  implements Serializable {
	
	private static final long serialVersionUID = -7715651009026349175L;
	@Id//기본 키(Primary Key) 지정
	@Column(name = "book_id")
	private String bookId; //도서ID

	// 카멜 케이스를 스네이크 케이스로 자동 변환하기 위해 @Column을 생략하거나 명시합니다.
	@Column(name = "name")
	private String name; // 도서명
	@Column(name = "unit_price")
	private BigDecimal unitPrice; // 가격	
	@Column(name = "author")
	private String author; // 저자
	@Column(name = "description", columnDefinition = "TEXT")
	private String description; // 설명	
	@Column(name = "publisher")
	private String publisher; // 출판사	
	@Column(name = "category")
	private String category; // 분류	
	@Column(name = "units_in_stock")
	private long unitsInStock; // 재고수	
	@Column(name = "release_date")
	private String releaseDate; // 출판일	B
	@Column(name = "book_condition") // 예약어 충돌 회피 명시
	private String condition; // 상태 : 신규도서/E-Book/중고도서	
	@Column(name = "file_name")
	private String fileName; //도서 이미지 파일	
	@Transient // DB에 저장하지 않을 필드
	private MultipartFile bookImage;  //도서 이미지
	// @Data 어노테이션이 Getter, Setter, 기본 생성자 등을 대신 생성한다고 가정합니다.


}
