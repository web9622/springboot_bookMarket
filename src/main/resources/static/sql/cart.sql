USE BOOKMARKETDB;

CREATE TABLE IF NOT EXISTS cart ( 
    cartId int(11) NOT NULL,  
    grandTotal  LONG,    
    primary key(cartId) 
) default CHARSET=utf8;


