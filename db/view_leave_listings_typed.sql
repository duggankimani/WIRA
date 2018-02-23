CREATE OR REPLACE VIEW kam_leave_listings_typed as 
select applicant, caseno, 
TO_DATE(applicationdate,'DD-MM-YYYY HH24:MI') applicationdate,
staffname, staffno, unit, actingofficer,leavecategory,
TO_DATE(beginning,'DD-MM-YYYY') beginning,
TO_DATE(ending,'DD-MM-YYYY') ending,
noofdays::decimal(5,2) noofdays,
balancebefore::decimal(5,2) balancebefore, 
balanceafter::decimal(5,2) balanceafter,
currentapprover, currentstep 
from kam_leave_listings order by applicationdate desc;