package com.springboot.repository;

// CartRepository.java (인터페이스)
import com.springboot.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


// Cart 엔티티와 기본 키 타입(String)을 지정하여 JpaRepository를 상속
public interface CartRepository extends JpaRepository<Cart, String> {

	// 여기에 필요한 커스텀 쿼리 메서드를 추가할 수 있습니다.
	// 예: 회원의 ID로 장바구니를 찾는 메서드
	// Cart findByMemberId(String memberId);
    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems WHERE c.cartId = :cartId")
    Optional<Cart> findCartWithItemsById(@Param("cartId") String cartId);
}