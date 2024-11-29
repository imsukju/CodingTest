insert into users (USER_ID, USERNAME, PASSWORD, NICKNAME, ACTIVATED) values (1, 'admin', '$2a$10$03K2Nu.o7wFWwBQbTyV1hO4QwUjt1LAbzD2mkYNw0avOx.2tGeurG', 'admin', 1);

insert into AUTHORITY (AUTHORITY_NAME) values ('ROLE_USER');
insert into AUTHORITY (AUTHORITY_NAME) values ('ROLE_ADMIN');

insert into USER_AUTHORITY (USER_ID, AUTHORITY_NAME) values (1, 'ROLE_USER');
insert into USER_AUTHORITY (USER_ID, AUTHORITY_NAME) values (1, 'ROLE_ADMIN');