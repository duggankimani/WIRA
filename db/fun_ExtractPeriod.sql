drop function if exists fun_ExtractPeriod(TIMESTAMP,VARCHAR);

CREATE FUNCTION fun_ExtractPeriod(in p_date TIMESTAMP, in p_period varchar(30)) 
returns int as  $$
DECLARE
	v_period int;
BEGIN
	
	if(p_period='Year') then
		v_period = EXTRACT(YEAR FROM p_date);
	elsif (p_period='Quarter') then 
		v_period = EXTRACT(QUARTER FROM p_date);
	elsif (p_period='Month') then 
		v_period = EXTRACT(MONTH FROM p_date);
	elsif (p_period='Week') then 
		v_period = EXTRACT(WEEK FROM p_date);
	elsif (p_period='Date') then 
		v_period = EXTRACT(DATE FROM p_date);
	elsif (p_period='Day') then 
		v_period = EXTRACT(DAY FROM p_date);
	elsif (p_period='Hour') then 
		v_period = EXTRACT(HOUR FROM p_date);
	end if;
	
	return v_period;
	
END;
$$ LANGUAGE plpgsql;
