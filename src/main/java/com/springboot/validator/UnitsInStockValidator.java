package com.springboot.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;


import com.springboot.domain.Book;
@Component
public class UnitsInStockValidator implements Validator{

   public boolean supports(Class<?> clazz) { // Book 클래스의 유효성 검사 여부를 위한 메소드
      return Book.class.isAssignableFrom(clazz);  
   }

   public void validate(Object target, Errors errors) {  // Book 클래스의 유효성 검사 메소드
      Book book = (Book) target;
     
      if(book.getUnitPrice()!=null && book.getUnitPrice().intValue()>=10000 && book.getUnitsInStock()>99) {
         errors.rejectValue("unitsInStock", "UnitsInStockValidator.message", "가격이 10000원 이상인 경우에는 99개 이상을 등록할 수 없습니다." ); //오류객체의 속성과 메시지 저장
            	
     }
   }
}