create table currencies
(
   id integer not null AUTO_INCREMENT,
   currency varchar(255) not null,
   exchange_rate FLOAT,
   description varchar(255),
   updated_date_str varchar(255),
   primary key(id)
);