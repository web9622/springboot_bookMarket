package com.springboot.service;

import com.springboot.repository.OrderProRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.domain.Book;
import com.springboot.domain.Order;
import com.springboot.repository.BookRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private OrderProRepository orderRepository; // 기존 OrderRepository 대신

	@Transactional
	@Override
	public void confirmOrder(String bookId, long quantity) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new IllegalArgumentException("요청한 도서 ID [" + bookId + "]를 찾을 수 없습니다."));

		if (book.getUnitsInStock() < quantity) {
			throw new IllegalArgumentException("품절입니다. 사용 가능 재고: " + book.getUnitsInStock());
		}

		book.setUnitsInStock(book.getUnitsInStock() - quantity);
	}

	@Override
	@Transactional
	public Order saveOrder(Order order) {
		return orderRepository.save(order);
	}
}
