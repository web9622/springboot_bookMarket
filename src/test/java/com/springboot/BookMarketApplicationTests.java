package com.springboot;

import com.springboot.domain.Customer;
import com.springboot.domain.Shipping;
import com.springboot.repository.mybatis.CustomerMapper;
import com.springboot.repository.mybatis.ShippingMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookMarketApplicationTests {

	@Autowired
	CustomerMapper customerMapper;
	@Autowired
	ShippingMapper shippingMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void mybatisInsertTest1() {
		Customer customer = new Customer();
		customer.setName("Alice");
		customer.setPhone("010-1234-5678");

		int rowCount = customerMapper.insertCustomer(customer);
		System.out.println("Inserted row count: " + rowCount);
	}

	@Test
	void mybatisSelectTest1() {
		long searchId = 1L; // 조회할 고객 ID
		Customer customer = customerMapper.findCustomerById(searchId);
		if (customer != null) {
			System.out.println("Customer found: " + customer.getName() + ", Phone: " + customer.getPhone());
		} else {
			System.out.println("Customer with ID " + searchId + " not found.");
		}
	}
	@Test
	void mybatisInsertTest2() {
		Shipping shipping = new Shipping();
		shipping.setName("Bob");
		shipping.setDate("2024/10/01");
		int rowCount = shippingMapper.insertShipping(shipping);
		System.out.println("Inserted row count: " + rowCount);
	}

	@Test
	void mybatisSelectTest2() {
		long searchId = 1L; // 조회할 배송 ID
		Shipping shipping = shippingMapper.findShippingById(searchId);
		if (shipping != null) {
			System.out.println("Shipping found: " + shipping.getName() + ", Date: " + shipping.getDate());
		} else {
			System.out.println("Shipping with ID " + searchId + " not found.");
		}
	}

	@Test
	void mybatisDeleteTest() {
		long deleteId = 1L; // 삭제할 배송 ID
		int rowCount = shippingMapper.deleteShippingById(deleteId);
		System.out.println("Deleted row count: " + rowCount);
	}
}
