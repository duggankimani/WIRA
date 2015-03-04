
psql -U postgres workflowmgr
CREATE TABLE advalue_forms AS select * from advalue where propertyid is not null or fieldid is not null;


/**pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t buser -t bgroup -t usergroup -t adform -t adfield -t adproperty -t advalue_forms -t addoctype -t adkeyvaluepair -t adoutputdoc -t adtrigger -t taskstepmodel -t adtasksteptrigger > import.sql**/



pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t buser >> import.sql
pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t bgroup >> import.sql
pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t usergroup >> import.sql
pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t adform >> import.sql
pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t adfield >> import.sql
pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t adproperty >> import.sql
pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t advalue_forms >> import.sql
pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t addoctype >> import.sql
pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t adkeyvaluepair >> import.sql
pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t adoutputdoc >> import.sql
pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t adtrigger >> import.sql
pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t taskstepmodel >> import.sql
pg_dump -U postgres --column-inserts --data-only  workflowmgr1 -t adtasksteptrigger >> import.sql
cat create_table_docnums.sql >> import.sql
cat fun_generatedocnum_postgresql.sql >> import.sql


Edit import.sql  - change the name advalue_forms -> advalue

psql -U postgres workflowmgr<import.sql

