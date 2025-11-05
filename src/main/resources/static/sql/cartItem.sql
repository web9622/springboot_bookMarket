USE BOOKMARKETDB;

CREATE TABLE IF NOT EXISTS cartitem ( 
     id int(11) NOT NULL AUTO_INCREMENT,
    quantity INTEGER,  
    totalPrice  LONG ,  
      primary key(id) 
) default CHARSET=utf8;


