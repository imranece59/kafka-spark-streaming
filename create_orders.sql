CREATE TABLE orders(
 order_id integer not null,
 delivery_date date,
 user_id integer,
 zipcode varchar(10),
 total double precision,
 item_count integer)