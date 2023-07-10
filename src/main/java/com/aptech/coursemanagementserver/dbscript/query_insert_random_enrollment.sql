-- DECLARE @StartDate AS date;
-- DECLARE @EndDate AS date;
-- DECLARE @DaysBetween AS int;
-- SELECT @StartDate   = '01/01/2022',
--        @EndDate     = '12/31/2023',
--        @DaysBetween = (1+DATEDIFF(DAY, @StartDate, @EndDate));


-- INSERT enrollment ([created_at], [is_published], [progress], [rating], [updated_at], [course_id], [user_id])
-- SELECT DATEADD(DAY, RAND(CHECKSUM(NEWID()))*@DaysBetween,@StartDate) , 1 , 0,
-- CAST(RAND(CHECKSUM(NEWID())) * 5 AS INT) + 1 ,
-- GETUTCDATE(),  c.id, u.id  
-- FROM course c CROSS JOIN users u

DECLARE @StartDate AS date;
DECLARE @EndDate AS date;
DECLARE @DaysBetween AS int;
DECLARE @Time time = '00:00:00.0000000';
SELECT @StartDate   = '01/01/2022',
       @EndDate     = '12/31/2023',
       @DaysBetween = (1+DATEDIFF(DAY, @StartDate, @EndDate));


INSERT enrollment ([created_at], [is_published], [progress], [rating], [updated_at], [course_id], [user_id])
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))
*@DaysBetween,@StartDate) , 120) + ' ' + CONVERT(varchar(12), @Time, 114)) , 1 , 0,
CAST(RAND(CHECKSUM(NEWID())) * 5 AS INT) + 1 ,
GETUTCDATE(),  c.id, u.id  
FROM course c CROSS JOIN users u
