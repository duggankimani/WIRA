drop function if exists fun_LeaveBalance(varchar, varchar);

CREATE FUNCTION fun_LeaveBalance(in p_userid varchar, in p_leavetype varchar) 
returns int as  $$
DECLARE
	v_leavebalance int;	
	v_daysallocated int;
	v_leavedays_taken int;
	v_openingleavetaken int;
	v_leavedaystoutilize int;
BEGIN
    
	v_leavedays_taken = 0;
	v_daysallocated = 0;
	
	--allocation
	select days into v_daysallocated from ext_leavetype where leavetype=p_leavetype;
	
	--leave days taken
	select coalesce(sum(days),0) into v_leavedays_taken from ext_kam_leave_report_per_type t 
	inner join ext_leaveapplications a 
	on (t."caseNo" = a."caseNo") where t."leaveCategory"=p_leavetype and t."staffId"=p_userid;
	
	if(p_leavetype='Annual Leave') then
		--opening balances
		select coalesce(leave_taken, 0), coalesce(days_to_utilize,0) into v_openingleavetaken,v_leavedaystoutilize 
		from ext_kam_leave_master where email = p_userid;
		
		v_leavedays_taken = v_leavedays_taken + v_openingleavetaken;
		v_daysallocated = v_leavedaystoutilize;
	end if;
	
	RAISE NOTICE 'v_daysallocated = % , v_leavedays_taken = %', v_daysallocated,v_leavedays_taken ;
	
	v_leavebalance = v_daysallocated - v_leavedays_taken;
	
	return v_leavebalance;
END;
$$ LANGUAGE plpgsql;


	