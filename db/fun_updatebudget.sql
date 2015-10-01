drop function if exists proc_updatebudget(varchar(45),decimal(10,2));

CREATE procedure proc_updatebudget(in p_casenumber varchar(45),in p_department varchar(45), in p_glcode varchar(45), in p_amount decimal(10,2)) 
DECLARE
in v_casenumber varchar(45),
in v_department varchar(45), 
in v_glcode varchar(45), 
in v_amount decimal(10,2)
BEGIN

	-- remove previous entry
	select casenumber, department,glcode,amount into v_casenumber, v_department,v_glcode,v_amount from ext_budgetexpenditure where casenumber=p_casenumber;
	if(v_casenumber is null){
	 --does not exist
	   v_casenumber=p_casenumber;
	   v_department=p_department;
	   v_glcode=p_glcode;
	   v_amount=0.0;
	}
	--deduct previous expenditure Note: Department and GLcode may have changed, hence this approach
        update ext_budget set p_department=(v_department-v_amount) where gl_code=v_glcode;
        delete from ext_budgetexpenditure where casenumber=v_casenumber;

        --record new 
	insert into ext_budgetexpenditure(casenumber,department,glcode,amount) values (p_casenumber,p_department,p_glcode,p_amount);
	update ext_budget set p_department=(p_department+p_amount) where gl_code=p_glcode;
	
END;
$$ LANGUAGE plpgsql;
