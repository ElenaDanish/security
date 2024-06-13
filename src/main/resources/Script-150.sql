CREATE TABLE customer (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
	email varchar(50) NOT NULL,
	pwd varchar(200) NOT NULL,
	"role" varchar(50) NOT NULL,
	CONSTRAINT customer_pkey PRIMARY KEY (id)
);


INSERT INTO customer
(email, pwd, "role")
values ('lucien@mail.ru', '$2a$12$V7Nr1kksonKfd7RgSZPmA..VAtLkZXKFlMreD0AFtOSXtj7nV6ZIC', 'user'),
('helena@mail.ru', '$2a$12$H4/bZi9E05vKd8em6DcF6.SyVdipZyI2mntUxMarIMYmCzo3W6Q8m', 'admin');