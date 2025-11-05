package com.springboot.controller;

import com.springboot.domain.*;
import com.springboot.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
@SessionAttributes("order")
public class OrderController {

	@Autowired
	private CartService cartService;
	@Autowired
	private BookService bookService;
	@Autowired
	private OrderService orderService;

	/** 1️⃣ 장바구니 → 주문 시작 **/
	@GetMapping("/{cartId}")
	public String requestCartList(@PathVariable("cartId") String cartId, Model model) {
		Cart cart = cartService.validateCart(cartId);
		Order order = new Order();
		List<Book> listOfBooks = new ArrayList<>();

		for (CartItem item : cart.getCartItems().values()) {
			OrderItem orderItem = new OrderItem();
			Book book = item.getBook();
			listOfBooks.add(book);

			orderItem.setBook(book);
			orderItem.setQuantity(item.getQuantity());
			orderItem.setTotalPrice(BigDecimal.valueOf(item.getTotalPrice()));
			order.getOrderItems().put(book.getBookId(), orderItem);
		}

		order.setCustomer(new Customer());
		order.setShipping(new Shipping());
		order.setGrandTotal(BigDecimal.valueOf(cart.getGrandTotal()));

		model.addAttribute("order", order);
		model.addAttribute("bookList", listOfBooks);

		System.out.println("🛒 [1단계] 장바구니 주문 생성 완료");
		return "redirect:/order/orderCustomerInfo";
	}

	/** 2️⃣ 고객 정보 입력 **/
	@GetMapping("/orderCustomerInfo")
	public String requestCustomerInfoForm(@ModelAttribute("order") Order order, Model model, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("userLoginInfo");
		if (loginMember == null) {
			System.out.println("❌ [2단계] 로그인 정보 없음, 로그인 페이지로 이동");
			return "redirect:/login";
		}

		Customer customer = new Customer();
		customer.setCustomerId(loginMember.getMemberId());
		customer.setName(loginMember.getName());
		customer.setPhone(loginMember.getPhone());
		customer.setEmail(loginMember.getEmail());

		// 기존 주소가 있으면 복사
		Address address = new Address();
		address.setCountry("대한민국");
		customer.setAddress(address);

		order.setCustomer(customer);
		model.addAttribute("customer", customer);

		System.out.println("✅ [2단계] 고객 정보 설정됨 → " + customer.getName());
		return "orderCustomerInfo";
	}

	@PostMapping("/orderCustomerInfo")
	public String processCustomerInfo(@ModelAttribute("order") Order order,
									  @ModelAttribute("customer") Customer customer,
									  Model model) {
		order.setCustomer(customer);
		model.addAttribute("order", order);
		System.out.println("✅ [2단계 POST] 고객 정보 저장 완료: " + customer.getName());
		return "redirect:/order/orderShippingInfo";
	}

	/** 3️⃣ 배송 정보 입력 **/
	@GetMapping("/orderShippingInfo")
	public String requestShippingForm(@ModelAttribute("order") Order order, Model model) {
		Shipping shipping = new Shipping();

		if (order.getCustomer() != null) {
			Customer customer = order.getCustomer();
			shipping.setName(customer.getName());

			if (customer.getAddress() != null) {
				Address addr = customer.getAddress();
				Address shippingAddress = new Address();
				shippingAddress.setCountry(addr.getCountry());
				shippingAddress.setZipCode(addr.getZipCode());
				shippingAddress.setAddressName(addr.getAddressName());
				shippingAddress.setDetailAddress(addr.getDetailAddress());
				shipping.setAddress(shippingAddress);
			}
		}

		// ✅ 오늘 날짜 자동 설정
		LocalDate today = LocalDate.now();
		shipping.setDate(today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));

		model.addAttribute("shipping", shipping);
		System.out.println("✅ [3단계] 배송 정보 설정 완료 → " + shipping.getName());
		System.out.println("총 금액: " + order.getTotalPrice());
		return "orderShippingInfo";
	}

	/** ✅ 3️⃣ 배송 정보 POST **/
	@PostMapping("/orderShippingInfo")
	public String requestShippingInfo(
			@Valid @ModelAttribute("shipping") Shipping shipping,
			BindingResult result,
			@ModelAttribute("order") Order order,
			Model model) {

		if (result.hasErrors()) {
			return "orderShippingInfo";
		}

		order.setShipping(shipping);
		model.addAttribute("order", order);

		return "orderConfirmation";
	}

	/** 4️⃣ 주문 확인 **/
	@GetMapping("/orderConfirmation")
	public String requestConfirmation(@ModelAttribute("order") Order order, Model model) {
		System.out.println("🧾 [4단계] 주문 확인 진입");

		model.addAttribute("order", order);
		model.addAttribute("bookList", order.getOrderItems().values());
		return "orderConfirmation";
	}

	/** 5️⃣ 주문 완료 **/
	@PostMapping("/orderCompleted")
	public String requestFinished(@ModelAttribute("order") Order order, SessionStatus status, Model model) {
		// 1. 주문 객체 저장 및 갱신된 객체 받기
		// saveOrder()가 ID가 채워진 Order 객체를 반환한다고 가정
    Order savedOrder = orderService.saveOrder(order);

		// 2. 갱신된 Order 객체를 Model에 추가하여 뷰로 전달
    model.addAttribute("order", savedOrder);

		// 3. 세션 완료
		status.setComplete();

		System.out.println("🎉 [5단계] 주문 완료 → 주문번호: " + savedOrder.getOrderId() + ", 총액: " + savedOrder.getGrandTotal());
		return "orderFinished";
	}
}
