--Tổng doanh thu 
GO
CREATE OR ALTER PROC sp_get_revenue_by_date @year INT
AS
BEGIN
SELECT  MONTH(CONVERT(date, DATEADD(hour, 7, o.created_at))) month,
YEAR(CONVERT(date, DATEADD(hour, 7, o.created_at))) year, SUM(o.net_price) revenue FROM orders o 
INNER JOIN users u ON user_id = u.id
WHERE o.status = 'COMPLETED' AND u.role = 'USER'
AND YEAR(CONVERT(date, DATEADD(hour, 7, o.created_at))) = @year
GROUP BY MONTH(CONVERT(date, DATEADD(hour, 7, o.created_at))),YEAR(CONVERT(date, DATEADD(hour, 7, o.created_at)))
ORDER BY YEAR, MONTH
END

EXEC sp_get_revenue_by_date 2022