package com.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
@Entity
public class Address {

	@Id
	@GeneratedValue
	private Long id;

	private String country;        // 국가명
	private String zipCode;        // ✅ camelCase로 수정
	private String addressName;    // ✅ camelCase로 수정
	private String detailAddress;  // ✅ camelCase로 수정
}
