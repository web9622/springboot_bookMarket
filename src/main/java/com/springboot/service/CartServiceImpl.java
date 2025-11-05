package com.springboot.service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.domain.Cart;
import com.springboot.repository.CartRepository;
import com.springboot.exception.CartException;
@Service
@Transactional
public class CartServiceImpl implements CartService{

	@Autowired
	private CartRepository cartRepository; // 이제 JpaRepository를 상속받는 인터페이스입니다.

	public Cart create(Cart cart) {
		// JPA의 save()는 생성과 업데이트를 모두 처리합니다.
		return cartRepository.save(cart);
	}

	public Cart read(String cartId) {
		// JPA의 findById는 Optional<Cart>를 반환하므로 .orElse(null)로 처리합니다.
		return cartRepository.findCartWithItemsById(cartId).orElse(null);
	}

	public void update(String cartId, Cart cart) {
		// 장바구니 존재 여부 확인 후 save() (JPA의 save는 객체가 있으면 update, 없으면 insert)
		if (cartRepository.existsById(cartId)) {
			cartRepository.save(cart);
		} else {
			// 예외 처리
			throw new IllegalArgumentException(String.format("장바구니 id(%s)가 존재하지 않습니다", cartId));
		}
	}

	public void delete(String cartId) {
		cartRepository.deleteById(cartId); // JPA의 deleteById 사용
	}

	public Cart validateCart(String cartId) {
		// 이제 read() 메소드를 호출하여 DB에서 장바구니를 조회합니다.
		Cart cart = read(cartId);

		// 아니면, 직접 findById를 사용해도 됩니다: cartRepository.findById(cartId).orElse(null);

		if(cart == null || cart.getCartItems().size() == 0) {
			throw new CartException(cartId);
		}
		return cart;
	}
}
