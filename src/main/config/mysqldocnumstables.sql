drop table if exists doctypeseq;
create table if not exists doctypeseq(
	doctype varchar(45) primary key not null,
	startval int default 0,
	nextval int not null
);

drop table if exists docnums;
create table if not exists docnums(
	casenumber varchar(20),
        doctype varchar(45),
	docno varchar(20) not null,
        constraint pk_docnums primary key (casenumber,doctype)
);

