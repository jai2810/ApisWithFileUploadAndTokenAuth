drop database usermanagementdb;
drop user usermanager;
create user usermanager with password 'password';
create database usermanagementdb with template=template0 owner=usermanager;
\connect usermanagementdb;
alter default privileges grant all on tables to usermanager;
alter default privileges grant all on sequences to usermanager;

create table users(
user_id integer primary key not null,
user_name varchar(20) not null,
email varchar(50) not null,
password varchar(20) not null,
created_at bigint not null,
updated_at bigint not null,
CONSTRAINT uniqueness UNIQUE (user_name, email)
);

create table image_data(
image_id integer primary key not null,
image_name varchar(50) not null,
user_id integer not null,
type varchar(50) not null,
data bytea not null
);

alter table image_data add constraint users_fk
foreign key (user_id) references users(user_id);

create sequence um_users_seq increment 1 start 1;
create sequence um_image_data_seq increment 1 start 1;