USE BOOKMARKETDB;
/*
CREATE TABLE IF NOT EXISTS customer ( 
    id int(11) NOT NULL AUTO_INCREMENT,
    customerid varchar(10) NOT NULL,
    name varchar(40) ,   
    phone varchar(40),     
    primary key(id) 
) default CHARSET=utf8;
*/

CREATE TABLE IF NOT EXISTS customer ( 
    id varchar(40) NOT NULL,   
    name varchar(40) ,   
    phone varchar(40),     
    primary key(id) 
) default CHARSET=utf8;


