USE BOOKMARKETDB;

CREATE TABLE IF NOT EXISTS orderItem1( 
    itemId int(11) NOT NULL AUTO_INCREMENT,
    bookid int(11) NOT NULL,
    quantity INTEGER,
    totalPrice DOUBLE,   
    primary key(itemId) 
) default CHARSET=utf8;
