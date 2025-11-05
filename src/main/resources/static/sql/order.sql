USE BOOKMARKETDB;

CREATE TABLE IF NOT EXISTS orders ( 
    orderId int(11) NOT NULL AUTO_INCREMENT,
    grandTotal INTEGER,
    primary key(orderId) 
) default CHARSET=utf8;
