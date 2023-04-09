create sequence user_sequence
    MINVALUE 0
    MAXVALUE 100500
    INCREMENT BY 1
    START WITH 10;

create table users
(
    user_id    numeric(18, 10) default random() primary key,
    user_name  varchar(255) not null,
    password   text,
    first_name varchar(100),
    last_name  varchar(100),
    email      varchar(100),
    phone      varchar(100),
    CONSTRAINT idx_users_username UNIQUE (user_name)
);

create table roles
(
    role_id int8        not null,
    name    varchar(50) not null,
    CONSTRAINT roles_uk_name UNIQUE (name),
    CONSTRAINT roles_pk_id PRIMARY KEY (role_id)
);

create table user_roles
(
    role_id int8 not null,
    user_id int8 not null,
    CONSTRAINT user_roles_role_id_fk FOREIGN KEY (role_id) REFERENCES roles (role_id),
    CONSTRAINT user_roles_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- setting data
insert into roles (role_id, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER');

insert into users
(user_id,
 user_name,
 password,
 first_name,
 last_name,
 email,
 phone)
values (1,
        'admin',
        '$2a$12$vJccdUOw9C6joXYOY7hFLO5qrCleHDbmsDVQFECS1FO3WDdWViuiG',
        'admin',
        'admin',
        'admin@gg.wp',
        '+1234567890');

insert into user_roles(role_id, user_id) values (1, 1);