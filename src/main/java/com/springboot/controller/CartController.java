package com.springboot.controller;

import com.springboot.repository.CartRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.springboot.domain.Book;
import com.springboot.domain.Cart;
import com.springboot.domain.CartItem;
import com.springboot.service.BookService;
import com.springboot.service.CartService;
import com.springboot.exception.BookIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(value = "/cart") // 🚨 로그인 오류 해결을 위해 복구
public class CartController {

	private static final Logger log = LoggerFactory.getLogger(CartController.class);

	@Autowired
	private CartService cartService;

	@Autowired
	private BookService bookService;

	@Autowired
	private CartRepository cartRepository;

	/**
	 * 장바구니 ID 결정 및 장바구니 객체 획득 (로그인 사용자 또는 비회원 세션 ID)
	 */
	private String getOrCreateCartId(HttpSession session) {
		String memberId = (String) session.getAttribute("memberId");

		if (memberId != null) {
			return memberId;
		} else {
			return session.getId();
		}
	}

	/**
	 * 장바구니 페이지 조회 (GET /cart)
	 */
	@GetMapping
	public String requestCart(HttpSession session) {
		String cartId = getOrCreateCartId(session);
		// 장바구니 상세 조회 로직을 GET /cart/{cartId}로 위임
		return "redirect:/cart/" + cartId;
	}

	/**
	 * 장바구니 상세 정보 조회 (GET /cart/{cartId})
	 */
	@GetMapping( "/{cartId}")
	public String requestCartList(@PathVariable(value = "cartId") String cartId, Model model, HttpSession session) {
		Cart cart = cartService.read(cartId);

		if (cart == null) {
			cart = new Cart(cartId);
			cartService.create(cart);
		}

		model.addAttribute("cart", cart);
		return "cart";
	}

	/**
	 * 장바구니 항목 추가 (POST /cart/book/{bookId})
	 * (HTML 폼에서 /cart/book/{bookId} 경로로 요청해야 정상 작동합니다.)
	 */
	@PostMapping("/book/{bookId}")
	public String addCartByNewItem(@PathVariable("bookId") String bookId, HttpSession session) {

		// (CartController의 다른 서비스와 getOrCreateCartId를 사용한다고 가정)

		try {
			String cartId = getOrCreateCartId(session);

			Cart cart = cartService.read(cartId); // CartService를 통해 DB/세션에서 Cart를 가져옴

			if (cart == null) {
				cart = cartService.create(new Cart(cartId)); // Cart 없으면 새로 생성
			}

			Book book = bookService.getBookById(bookId); // 도서 정보 조회

			if (book == null) {
				// 도서가 없는 경우 예외 처리
				return "redirect:/books";
			}

			// 장바구니 항목 생성 및 추가
			CartItem cartItem = new CartItem(book); // Book 객체를 인자로 받는 CartItem 생성자 사용 가정
			cart.addCartItem(cartItem);
			cartService.update(cartId, cart); // DB/세션에 저장

			// 성공 후 장바구니 페이지로 리다이렉트
			return "redirect:/cart";

		} catch (Exception e) {
			// 서버 로그를 확인하여 정확한 500 오류 원인(NullPointer 등)을 찾아야 합니다.
			// 현재는 오류 우회를 위해 도서 목록으로 리다이렉트합니다.
			// log.error("장바구니 추가 중 오류 발생: bookId={}", bookId, e);
			return "redirect:/books";
		}
	}

	/**
	 * 장바구니 항목 삭제 (DELETE /cart/book/{bookId})
	 */
	@DeleteMapping("/book/{bookId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void removeCartByItem(@PathVariable("bookId") String bookId, HttpSession session) {

		String cartId = getOrCreateCartId(session);

		Cart cart = cartService.read(cartId);

		if(cart == null)
			throw new IllegalArgumentException("삭제할 장바구니를 찾을 수 없습니다.");

		Book book = bookService.getBookById(bookId);
		if(book == null)
			throw new IllegalArgumentException(new BookIdException(bookId));

		// Cart 도메인 내부의 로직 (항목 제거) 실행
		cart.removeCartItem(new CartItem(book));

		// DB에 저장
		cartService.update(cartId, cart);
	}

	/**
	 * 장바구니 전체 삭제 (DELETE /cart/{cartId})
	 */
	@DeleteMapping("/{cartId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteCartList(@PathVariable("cartId") String cartId) {
		cartService.delete(cartId); // DB에서 장바구니 레코드 자체를 삭제
	}
}