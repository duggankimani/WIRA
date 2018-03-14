create view view_leave_applicaitons_overdue as (
select * from view_leave_applications_inprogress where beginning < current_date);
