package com.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter @Setter
// 🚨 StackOverflowError 해결을 위해 순환 참조 필드를 제외합니다.
@EqualsAndHashCode(exclude = {"cartItems", "grandTotal"})
@ToString(exclude = {"cartItems"})
public class Cart {

	@Id
	private String cartId;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	private Map<String, CartItem> cartItems = new HashMap<>();

	private double grandTotal;

	public Cart() {
		// 기본 생성자
	}

	public Cart(String cartId) {
		this.cartId = cartId;
	}

	// -------------------------------------------------------------------------
	// 🚨 [필수 추가 메소드 1] 장바구니 항목 추가 메소드 (중복 제거 및 로직 통합)
	// -------------------------------------------------------------------------
	public void addCartItem(CartItem item) {
		if (this.cartItems == null) {
			this.cartItems = new HashMap<>();
		}

		String bookId = item.getBook().getBookId();
		CartItem existingItem = this.cartItems.get(bookId);

		if (existingItem != null) {
			// 이미 존재하는 항목이면 수량 증가
			existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
			existingItem.updateTotalPrice(); // CartItem의 가격 업데이트 호출
		} else {
			// 새 항목이면 Map에 추가
			this.cartItems.put(bookId, item);
			item.setCart(this); // CartItem이 Cart를 참조하도록 설정
		}

		this.updateGrandTotal(); // 총액 업데이트
	}

	// -------------------------------------------------------------------------
	// 🚨 [필수 추가 메소드 2] 장바구니 항목 제거 메소드 (컴파일 에러 해결)
	// -------------------------------------------------------------------------
	public void removeCartItem(CartItem item) {
		if (this.cartItems != null) {
			// Book ID를 키로 사용하여 항목을 제거합니다.
			this.cartItems.remove(item.getBook().getBookId());
			this.updateGrandTotal(); // 총액 업데이트
		}
	}

	// -------------------------------------------------------------------------
	// 🚨 [필수 추가 메소드 3] 총액 업데이트 메소드
	// -------------------------------------------------------------------------
	public void updateGrandTotal() {
		double total = 0;
		for (CartItem item : this.cartItems.values()) {
			total += item.getTotalPrice();
		}
		this.grandTotal = total;
	}
}