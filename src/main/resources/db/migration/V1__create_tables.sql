create schema if not exists car_service;

create table if not exists car_service.make (
	id serial primary key,
	name varchar(255) not null
	);
	
create table if not exists car_service.model (
	id serial primary key,
	name varchar(255) not null
	);
	
create table if not exists car_service.category (
	id serial primary key,
	name varchar(255) not null
	);
	
create table if not exists car_service.car (
	id serial primary key,
	make_id bigint not null,
	model_id bigint not null,
	category_id bigint not null,
	year integer not null,
	object_id varchar(255) not null,
	foreign key (make_id) references car_service.make(id),
	foreign key (model_id) references car_service.model(id),
	foreign key (category_id) references car_service.category(id)
	);
