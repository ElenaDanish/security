CREATE TABLE customer (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
	email varchar(50) NOT NULL,
	pwd varchar(200) NOT NULL,
	"role" varchar(50) NOT NULL,
	CONSTRAINT customer_pkey PRIMARY KEY (id)
);


INSERT INTO customers
(email, pwd, "role")
values ('lucien@mail.ru', '$2a$10$QiTeYrwPULbAvkJjKGnve.5FD/BfuPG0Sk57B18AQitK4LcbYB0rS', 'user'),
('helena@mail.ru', '$2a$10$hytLYqsC9P4WPk6T6tVj7.BED/D5ptlsdKTxfYDBUgNeM9Uxw8Y3S', 'admin');

-- для AuthenticationProvider

CREATE TABLE public.authorities (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
	username varchar(50) NOT NULL,
	authority varchar(50) NOT NULL,
	customer_id int4 NULL,
	CONSTRAINT authorities_pkey PRIMARY KEY (id)
);

ALTER TABLE public.authorities ADD CONSTRAINT auth_fk FOREIGN KEY (customer_id) REFERENCES public.customer(id);

INSERT INTO authorities(username, authority, customer_id)
VALUES ('lucien', 'ROLE_USER', 1), ('helena', 'ROLE_ADMIN', 2);