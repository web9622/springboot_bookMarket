package com.springboot.domain;

import java.math.BigDecimal;

import java.util.Map;
import java.util.HashMap;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "orders")
@Data
public class Order{	
	
	@Id @GeneratedValue//(strategy = GenerationType.IDENTITY)
	private Long orderId;

	private int totalPrice; // ✅ 총 금액 필드 추가//주문ID
			
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	private Customer customer;            //고객 객체	
	
	@OneToOne(cascade = CascadeType.ALL)	
	@JoinColumn(name = "shipping_id")
	private Shipping shipping;             //배송지 객체	
	
	
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_order_id")
	private Map<String,OrderItem> orderItems =new HashMap<>();
	
	private BigDecimal grandTotal; //주문 총 금액	
	@Transient
	public double getTotalPrice() {
		if (orderItems == null || orderItems.isEmpty()) return 0.0;
		return orderItems.values().stream()
				.mapToDouble(OrderItem::getTotalPrice)
				.sum();
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

}
