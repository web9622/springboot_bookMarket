package com.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.math.BigDecimal; // BigDecimal 사용을 위해 import

@Entity
@Getter @Setter
// StackOverflowError 해결을 위해 순환 참조 필드를 제외합니다.
@EqualsAndHashCode(exclude = {"cart", "totalPrice"})
@ToString(exclude = {"cart"})
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@OneToOne
	@JoinColumn(name = "book_id")
	private Book book;

	private int quantity;

	// totalPrice가 double 타입이라고 가정하고 코드를 작성합니다.
	private double totalPrice;

	public CartItem() {
		// 기본 생성자
	}

	public CartItem(Book book) {
		this.book = book;
		this.quantity = 1;
		this.updateTotalPrice();
	}

	// 🚨 최종 수정된 부분: BigDecimal과 int의 곱셈 오류를 해결합니다.
	public void updateTotalPrice() {
		if (this.book != null) {
			// book.getUnitPrice() (BigDecimal)와 quantity (int)를 곱셈 후 double로 변환
			this.totalPrice = this.book.getUnitPrice().multiply(
					BigDecimal.valueOf(this.quantity)
			).doubleValue();
		}
	}
}