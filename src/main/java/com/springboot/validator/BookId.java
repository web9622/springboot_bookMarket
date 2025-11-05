package com.springboot.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;


@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BookIdValidator.class) 
public @interface BookId {
	 String message() default "{BookId.book.bookId}"; 
	   Class<?>[] groups() default {};
	   Class<?>[] payload() default {};  	 
}