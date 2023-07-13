-- DECLARE @StartDate AS date;
-- DECLARE @EndDate AS date;
-- DECLARE @DaysBetween AS int;
-- SELECT @StartDate   = '01/01/2022',
--        @EndDate     = '12/31/2023',
--        @DaysBetween = (1+DATEDIFF(DAY, @StartDate, @EndDate));
-- INSERT orders ([created_at], [description], [duration], [image], [name], [net_price], [payment], [price], [status], 
-- [transaction_id], [updated_at], [user_description], [course_id], [user_id])
-- SELECT DATEADD(DAY, RAND(CHECKSUM(NEWID()))*@DaysBetween,@StartDate) , c.description, c.duration, c.image, c.name, c.net_price
-- , 'PAYPAL', c.price, 'COMPLETED', null, GETUTCDATE(), null, c.id, u.id  
-- FROM course c CROSS JOIN users u
-- WHERE u.role ='USER'

DECLARE @StartDate AS date;
DECLARE @EndDate AS date;
DECLARE @DaysBetween AS int;
DECLARE @Time time = '00:00:00.0000000';
SELECT @StartDate   = '01/01/2022',
       @EndDate     = GETUTCDATE(),
       @DaysBetween = (1+DATEDIFF(DAY, @StartDate, @EndDate));
INSERT orders ([created_at], [description], [duration], [image], [name], [net_price], [payment], [price], [status], 
[transaction_id], [updated_at], [user_description], [course_id], [user_id])
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))
*@DaysBetween,@StartDate) , 120) + ' ' + CONVERT(varchar(12), @Time, 114)) , c.description, c.duration, c.image, c.name, c.net_price
, 'PAYPAL', c.price, 'COMPLETED', NEWID(), GETUTCDATE(), null, c.id, u.id  
FROM course c CROSS JOIN users u
WHERE u.role ='USER'

UPDATE orders set created_at= DATEADD(HOUR,CAST(RAND(CHECKSUM(NEWID())) * 24 AS INT) + 1,created_at)