drop table if exists suppliers;
CREATE  TABLE suppliers (
  id serial NOT NULL,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  phone VARCHAR(255) NOT NULL,
  fax VARCHAR(10) NOT NULL,
  PRIMARY KEY (id) );

insert into suppliers (name, address, email, phone,fax) values 
('Zambezi Contractors','P.O Box 232 Kisumu','info@zambezi.co.ke','020343445','778664'),
('Kureshi Contractors','P.O Box 5454-00100 Nairobi','info@zambezi.co.ke','020204909',''),
('Malezi Design Build','P.O Box 0934-0010 Nairobi','info@zambezi.co.ke','0723099090',''),
('Handy Contractors','P.O Box 5653 Kisii','info@zambezi.co.ke','0729777423','');


drop table if exists contracts;
CREATE  TABLE contracts (
  id serial NOT NULL,
  contract VARCHAR(45) NOT NULL,
  contractamount decimal(10,2) NOT NULL default 0,
  paid decimal(10,2) NOT NULL default 0,
  contractdate date NOT NULL ,
  supplierid int not null,
  PRIMARY KEY (id) );

insert into contracts (contract, contractamount, paid, contractdate,supplierid) values 
('Contract-KC012/14 - Zambezi Contractors',50000000,5000000,'2014-03-20',1),
('Contract-KC013/14 - Kureshi Contractors',50000000,5000000,'2014-05-02',2),
('Contract-KC098/15 - Malezi Design Build',50000000,5000000,'2014-06-01',3),
('Contract-KC098/15 - Handy Contractors',50000000,5000000,'2014-10-11',4);


drop function if exists func_getAmount(int, varchar(45));
CREATE FUNCTION func_getAmount(in p_contractid int, in p_fieldname varchar(45)) RETURNS text AS $$
DECLARE
  v_amount decimal(10,2);
  v_paid decimal(10,2);
  v_resp decimal(10,2);
BEGIN

    if(p_fieldname='Amount') then
     	select contractamount into v_resp from contracts where id=p_contractid;
    elsif(p_fieldname='Paid') then
	select paid into v_resp from contracts where id=p_contractid;
    else
	select (contractamount-paid) into v_resp from contracts where id=p_contractid;
    end if;

	RAISE INFO 'p_contractid = %, v_amount=%, v_resp=%',p_contractid,v_Amount,v_resp;
    RETURN cast(v_resp as text);
END;
$$ LANGUAGE plpgsql;

drop function if exists func_updateAmount(int, numeric);
CREATE FUNCTION func_updateAmount(in p_contractid int, in p_amount numeric) RETURNS text AS $$
DECLARE
  v_resp decimal(10,2);
BEGIN
    	update contracts set contractamount=(contractamount-p_amount) where id=p_contractid;
	
	select contractamount into v_resp from contracts where id=p_contractid;
	
	RETURN cast(v_resp as text);
END;
$$ LANGUAGE plpgsql;



drop function if exists func_getSupplierInfo(int, varchar(45));
CREATE FUNCTION func_getSupplierInfo(in p_contractid int, in p_fieldname varchar(45)) RETURNS text AS $$
DECLARE

  v_resp varchar(255);
BEGIN

    if(p_fieldname='Name') then
     	select name into v_resp from suppliers s inner join contracts c on s.id=c.supplierid and c.id=p_contractid;
    elsif(p_fieldname='Address') then
	select address into v_resp from suppliers s inner join contracts c on s.id=c.supplierid and c.id=p_contractid;
    elsif(p_fieldname='Email') then
	select email into v_resp from suppliers s inner join contracts c on s.id=c.supplierid and c.id=p_contractid;
    elsif(p_fieldname='Phone') then
	select phone into v_resp from suppliers s inner join contracts c on s.id=c.supplierid and c.id=p_contractid;
    elsif(p_fieldname='Fax') then
	select fax into v_resp from suppliers s inner join contracts c on s.id=c.supplierid and c.id=p_contractid;
    else
	select name into v_resp from suppliers s inner join contracts c on s.id=c.supplierid and c.id=p_contractid;
    end if;

    RETURN v_resp;
END;
$$ LANGUAGE plpgsql;

drop table if exists votes;
CREATE  TABLE votes (
  id serial NOT NULL,
  code VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id) );

insert into votes (code,name) values 
('1000232','Materials'),('2000344','Building And Construction'),('300454','Fuel'),('4000343','Operations'),('5000754','Motor Vehicle & Equipment');
