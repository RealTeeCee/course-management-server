DECLARE @StartDate AS date;
DECLARE @EndDate AS date;
DECLARE @DaysBetween AS int;
DECLARE @Time time = '00:00:00.0000000';

SELECT @StartDate   = '01/01/2022',
       @EndDate     = '12/31/2023',
       @DaysBetween = (1+DATEDIFF(DAY, @StartDate, @EndDate));

-- Insert 5 courses for category 4 (Data Science)
INSERT INTO course (created_at, description, duration, image, level, name, net_price, price, published_at, 
rating, requirement, slug, status, updated_at, author_id, category_id)

SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))*@DaysBetween,@StartDate) , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
       'This course covers the principles of typography and how to use type effectively in graphic design. Students will learn how to choose appropriate typefaces, use spacing and layout to create hierarchy and visual interest, and apply typographic principles to create effective designs.',
       0,
       'https://i.ibb.co/7GDTVRf/aptech.png',
       1,
       'Typography and Layout Design',
       ROUND(RAND()*(20-5)+5, 2),
       ROUND(RAND()*(100-10)+10, 2),
       GETDATE(),
       ROUND(RAND()*(5-1)+1,1),
       'No prerequisite knowledge is required for this course.',
       'typography-and-layout-design',
       1,
       GETDATE(),
       ROUND(RAND()*(7-6)+6, 0),
       2
UNION ALL
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))*@DaysBetween,@StartDate) , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
       'This course covers the principles and techniques of digital illustration. Students will learn how to
        use digital tools to create illustrations, including selecting colors, creating shapes, adding textures
         and effects, and applying various techniques to create unique and engaging illustrations.',
       0,
       'https://i.ibb.co/7GDTVRf/aptech.png',
       1,
       'Digital Illustration Techniques',
       ROUND(RAND()*(20-5)+5, 2),
       ROUND(RAND()*(100-10)+10, 2),
       GETDATE(),
       ROUND(RAND()*(5-1)+1,1),
       'No prerequisite knowledge is required for this course.',
       'digital-illustration-techniques',
       1,
       GETDATE(),
       ROUND(RAND()*(7-6)+6, 0),
       2
UNION ALL
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))*@DaysBetween,@StartDate) , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
       'This course covers the principles and techniques of branding and logo design. Students will learn how to
        create effective logos and branding materials that communicate a brands values and personality. Topics
         covered include logo design, color theory, typography, and brand identity.',
       0,
       'https://i.ibb.co/7GDTVRf/aptech.png',
       1,
       'Branding and Logo Design',
       ROUND(RAND()*(20-5)+5, 2),
       ROUND(RAND()*(100-10)+10, 2),
       GETDATE(),
       ROUND(RAND()*(5-1)+1,1),
       'No prerequisite knowledge is required for this course.',
       'branding-and-logo-design',
       1,
       GETDATE(),
       ROUND(RAND()*(7-6)+6, 0),
       2
UNION ALL
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))*@DaysBetween,@StartDate) , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
       'This course covers the fundamentals of web design, including HTML, CSS, and responsive design. Students will learn how to create effective and engaging websites that are optimized for all devices. Topics covered include web design principles, layout and typography, color theory, and user experience design.',
       0,
       'https://i.ibb.co/7GDTVRf/aptech.png',
       1,
       'Web Design Fundamentals',
       ROUND(RAND()*(20-5)+5, 2),
       ROUND(RAND()*(100-10)+10, 2),
       GETDATE(),
ROUND(RAND()*(5-1)+1,1),
       'No prerequisite knowledge is required for this course.',
       'web-design-fundamentals',
       1,
       GETDATE(),
       ROUND(RAND()*(7-6)+6, 0),
       2
UNION ALL
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))
*@DaysBetween,@StartDate) , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
        'Course description',
        0,
        'https://i.ibb.co/7GDTVRf/aptech.png',
        1,
        'Natural Language Processing',
        ROUND(RAND()*(20-5)+5, 2),
        ROUND(RAND()*(100-10)+10, 2),
        GETDATE(),
        ROUND(RAND()*(5-1)+1, 1),
        'Course requirement',
        'natural-language-processing',
        1,
        GETDATE(),
        ROUND(RAND()*(9-8)+8, 0),
        3
UNION ALL
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))
*@DaysBetween,@StartDate) , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
        'Course description',
        0,
        'https://i.ibb.co/7GDTVRf/aptech.png',
        1,
        'Computer Vision Fundamentals',
        ROUND(RAND()*(20-5)+5, 2),
        ROUND(RAND()*(100-10)+10, 2),
        GETDATE(),
        ROUND(RAND()*(5-1)+1, 1),
        'Course requirement',
        'computer-vision-fundamentals',
        1,
        GETDATE(),
        ROUND(RAND()*(9-8)+8, 0),
        3
UNION ALL
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))
*@DaysBetween,@StartDate) , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
        'Course description',
        0,
        'https://i.ibb.co/7GDTVRf/aptech.png',
        1,
        'Reinforcement Learning',
        ROUND(RAND()*(20-5)+5, 2),
        ROUND(RAND()*(100-10)+10, 2),
        GETDATE(),
        ROUND(RAND()*(5-1)+1, 1),
        'Course requirement',
        'reinforcement-learning',
        1,
        GETDATE(),
        ROUND(RAND()*(9-8)+8, 0),
        3
UNION ALL
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))
*@DaysBetween,@StartDate) , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
        'Course description',
        0,
        'https://i.ibb.co/7GDTVRf/aptech.png',
        1,
        'Deep Learning Fundamentals',
        ROUND(RAND()*(20-5)+5, 2),
        ROUND(RAND()*(100-10)+10, 2),
        GETDATE(),
        ROUND(RAND()*(5-1)+1, 1),
        'Course requirement',
        'deep-learning-fundamentals',
        1,
        GETDATE(),
        ROUND(RAND()*(9-8)+8, 0),
        3
UNION ALL
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))
*@DaysBetween,@StartDate) , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
        'Course description',
        0,
        'https://i.ibb.co/7GDTVRf/aptech.png',
        1,
        'Machine Learning with Python',
        ROUND(RAND()*(20-5)+5, 2),
        ROUND(RAND()*(100-10)+10, 2),
        GETDATE(),
        ROUND(RAND()*(5-1)+1, 1),
        'Course requirement',
        'machine-learning-with-python',
        1,
        GETDATE(),
        ROUND(RAND()*(9-8)+8, 0),
        3
UNION ALL 
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))*@DaysBetween,@StartDate)
 , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
        'Course description',
        0,
        'https://i.ibb.co/7GDTVRf/aptech.png',
        1,
        'Big Data Analytics',
        ROUND(RAND()*(20-5)+5, 2),
        ROUND(RAND()*(100-10)+10, 2),
        GETDATE(),
        ROUND(RAND()*(5-1)+1, 1),
        'Course requirement',
        'big-data-analytics',
        1,
        GETDATE(),
        ROUND(RAND()*(11-10)+10, 0),
        4
UNION ALL
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))*@DaysBetween,@StartDate)
 , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
        'Course description',
        0,
        'https://i.ibb.co/7GDTVRf/aptech.png',
        1,
        'Time Series Analysis and Forecasting',
        ROUND(RAND()*(20-5)+5, 2),
        ROUND(RAND()*(100-10)+10, 2),
        GETDATE(),
        ROUND(RAND()*(5-1)+1, 1),
        'Course requirement',
        'time-series-analysis-and-forecasting',
        1,
        GETDATE(),
        ROUND(RAND()*(11-10)+10, 0),
        4
UNION ALL
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))*@DaysBetween,@StartDate)
 , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
        'Course description',
        0,
        'https://i.ibb.co/7GDTVRf/aptech.png',
        1,
        'Data Visualization with Tableau',
        ROUND(RAND()*(20-5)+5, 2),
        ROUND(RAND()*(100-10)+10, 2),
        GETDATE(),
        ROUND(RAND()*(5-1)+1, 1),
        'Course requirement',
        'data-visualization-with-tableau',
        1,
        GETDATE(),
        ROUND(RAND()*(11-10)+10, 0),
        4
UNION ALL
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))*@DaysBetween,@StartDate)
 , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
        'Course description',
        0,
        'https://i.ibb.co/7GDTVRf/aptech.png',
        1,
        'Data Mining and Analysis',
        ROUND(RAND()*(20-5)+5, 2),
        ROUND(RAND()*(100-10)+10, 2),
        GETDATE(),
        ROUND(RAND()*(5-1)+1, 1),
        'Course requirement',
        'data-mining-and-analysis',
        1,
        GETDATE(),
        ROUND(RAND()*(11-10)+10, 0),
        4
UNION ALL
SELECT CONVERT(datetimeoffset, CONVERT(varchar(10),DATEADD(DAY, RAND(CHECKSUM(NEWID()))*@DaysBetween,@StartDate)
 , 120) + ' ' + CONVERT(varchar(12), @Time, 114)),
        'Course description',
        0,
        'https://i.ibb.co/7GDTVRf/aptech.png',
        1,
        'Statistical Inference and Modeling',
        ROUND(RAND()*(20-5)+5, 2),
        ROUND(RAND()*(100-10)+10, 2),
        GETDATE(),
        ROUND(RAND()*(5-1)+1,1),
        'Course requirement',
        'statistical-inference-and-modeling',
        1,
        GETDATE(),
        ROUND(RAND()*(11-10)+10, 0),
        4

