
drop procedure if exists proc_search;

DELIMITER $$
create procedure proc_search(in p_subject varchar(50), in p_date timestamp,
in p_dateend timestamp,
in p_priority tinyint,
in p_phrase varchar(50), in p_doctype char(30), in p_hasattachment boolean)

BEGIN
	if(p_subject is not null) then
		select id, subject from localdocument where subject like (p_subject); 
	end if;
END $$

DELIMITER ;