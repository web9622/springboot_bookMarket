USE BOOKMARKETDB;

CREATE TABLE IF NOT EXISTS shipping ( 
    id int(11) NOT NULL AUTO_INCREMENT,
    name varchar(40),
    date varchar(40),   
    primary key(id) 
) default CHARSET=utf8;
