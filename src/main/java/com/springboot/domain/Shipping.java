package com.springboot.domain;


import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@Entity
@Data			// Getter Setter
//@Builder		// DTO -> Entity화
//@AllArgsConstructor	// 모든 컬럼 생성자 생성
//@NoArgsConstructor	// 기본 생성자
public class Shipping{
	
	 @Id 
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 

	private String name;        //배송고객명	
	
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private String date;          //배송일
	// @CreatedDate
	// private LocalDateTime date;
	 
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
	private Address address;   //배송 주소 객체
	private String country;
	private String zipCode;
	private String addressName;
	private String detailAddress;

}
