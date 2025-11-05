USE BOOKMARKETDB;

CREATE TABLE IF NOT EXISTS book(
	b_bookId VARCHAR(10) NOT NULL,
	b_name VARCHAR(30),
	b_unitPrice  INTEGER,
	b_author VARCHAR(50),
	b_description TEXT,
	b_publisher VARCHAR(20),
	b_category VARCHAR(20),
	b_unitsInStock LONG,	
	b_releaseDate VARCHAR(20),
	b_condition VARCHAR(20),
	b_fileName  VARCHAR(20),
	PRIMARY KEY (b_bookId)
)DEFAULT CHARSET=utf8;

DELETE FROM book;
INSERT INTO book
(book_id, name, unit_price, author, description, publisher,
 category, units_in_stock, release_date, book_condition, file_name) -- ✨ 컬럼 이름 수정
VALUES
('ISBN1234', '자바스크립트 입문', 30000, '조현영', '자바스크립트의 기초부터 심화까지...',
 '길벗', 'IT전문서', 1000, '2024/02/20', 'new', 'ISBN1234.jpg');


