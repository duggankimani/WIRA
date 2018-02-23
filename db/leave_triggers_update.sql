select p.name,p.processid,t.name,t.processrefid from adtrigger t 
left join processdefmodel p on (p.refId=t.processrefid)
where t.name like 'kam.general.%' 

update adtrigger set processrefId='srqDXt4o8UFma1Yn' where 
processrefid= '3P2zezNyK88lzVqM' and name like 'kam.general.%'; 
