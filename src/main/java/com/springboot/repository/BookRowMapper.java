package com.springboot.repository;

import com.springboot.domain.Book;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
//서버(BoardService 또는 이를 호출하는 DAO 계층)가 데이터베이스(bookmarketdb)에 SQL 쿼리를 전송하고, 그 결과를 자바 객체로 변환하여 서버의 메모리로 가져오는 목적
//도서 조회결과 가져오기
//JDBC Template을 사용할 때 SQL 조회 결과(ResultSet)를 자바 객체(Book)로 변환하는 역할을 수행

//RowMapper<Book> 인터페이스를 구현하여, 각 데이터베이스 레코드(행)를 Book 객체로 매핑
@Component
public class BookRowMapper implements RowMapper<Book> {
        @Override
        //SQL 조회 결과의 한 행(Row)을 처리할 때마다 호출
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException{
            //데이터베이스의 현재 행 데이터를 담을 빈 Book 객체
        Book book = new Book();
        // rs.getString(컬럼명)을 사용하여 순서에 대한 의존성을 제거합니다.
            // ResultSet에서 "book_id"라는 이름의 컬럼 값을 가져와 Book 객체의 bookId 필드에 설정
        book.setBookId(rs.getString("book_id"));
        book.setName(rs.getString("name"));
        //DB의 unit_price 컬럼 타입에 맞는 getBigDecimal() 메서드를 사용하여 값을 가져오므로, 타입 안전성이 높습니다.
        book.setUnitPrice(rs.getBigDecimal("unit_price"));
        book.setAuthor(rs.getString("author"));
        book.setDescription(rs.getString("description"));
        book.setPublisher(rs.getString("publisher"));
        book.setCategory(rs.getString("category"));
            //units_in_stock (DB의 스네이크 케이스) 값을 setUnitsInStock (Java의 카멜 케이스)에 매핑
        book.setUnitsInStock(rs.getLong("units_in_stock"));
        book.setReleaseDate(rs.getString("release_date"));
        book.setCondition(rs.getString("book_condition"));
        book.setFileName(rs.getString("file_name"));
        //데이터가 채워진 Book 객체를 반환하여, 호출한 서비스 계층에 최종 결과 리스트를 전달
        return book;
    }

}
