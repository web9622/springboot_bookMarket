package com.springboot.domain;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ✅ Book 엔티티와 다대일 관계 (컬럼명: book_id)
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book; // 필드명은 반드시 book으로!

	private int quantity; // 주문 수량

	private BigDecimal totalPrice; // 주문 가격

	// ✅ 개별 항목 총액 계산
	public double getTotalPrice() {
		if (book == null || book.getUnitPrice() == null) return 0.0;
		return book.getUnitPrice().doubleValue() * quantity;
	}
}
