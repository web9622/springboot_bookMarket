USE BOOKMARKETDB;

CREATE TABLE IF NOT EXISTS address ( 
    id int(11) NOT NULL AUTO_INCREMENT,
    country varchar(40),
    zipcode varchar(40),
    addressname varchar(40),
    detailname varchar(40) ,
    primary key(id) 
) default CHARSET=utf8;
