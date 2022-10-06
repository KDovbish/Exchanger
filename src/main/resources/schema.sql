create table operday (
id int primary key,
opensign boolean,
closesign boolean
);

create table exchangerate (
    id int primary key,
    ccy varchar(3),
    buy numeric(15,5),
    sale numeric(15,5),
    ts timestamp
);

create table request (
    id int primary key,
    type int,
    ccy varchar(3),
    rate numeric(15,5),
    ccysum numeric(15,5),
    uahsum numeric(15,5),
    status int,
    firstname varchar(20),
    lastname varchar(20),
    tel varchar(15),
    otp varchar(4),
    ts timestamp
);

create sequence HIBERNATE_SEQUENCE;

