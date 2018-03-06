create or replace view kam_leave_listings_2018 as 
(
select l.applicant, 
balcf.name,l.caseno, l.applicationdate, l.staffname, l.staffno, l.unit, l.actingofficer, l.leavecategory, 
l.beginning, l.ending, l.noofdays,
sum(noofdays) over (partition by applicant, leavecategory order by applicationdate) as totaldaystaken,
case when l.leavecategory='Annual Leave' then
  balcf.initial - (sum(noofdays) over (partition by applicant, leavecategory order by applicationdate)) 
when cast(lt.days as integer) <=0 then 
  0 
else 
  cast(lt.days as integer) - (sum(noofdays) over (partition by applicant, leavecategory order by applicationdate))  
end as balance,
l.currentapprover, l.currentstep
from kam_leave_listings_typed l 
left join 
(select name, email, 'Annual Leave' leave_type,  (balance_from_previous_year + leave_allocation - leave_taken) initial
from ext_kam_leave_master) as balcf on balcf.email=l.applicant 
left join 
ext_leavetype lt on (lt.leavetype=l.leavecategory) 
where l.applicationdate >= to_date('2018-01-01', 'YYYY-mm-dd') and l.applicationdate <= to_date('2018-12-30', 'YYYY-mm-dd') 
);
