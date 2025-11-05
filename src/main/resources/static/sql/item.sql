USE BOOKMARKETDB;

CREATE TABLE IF NOT EXISTS item(
	b_itemId VARCHAR(10) NOT NULL,
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

