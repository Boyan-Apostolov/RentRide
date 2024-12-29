DROP PROCEDURE IF EXISTS get_popular_cars_over_time;

CREATE PROCEDURE get_popular_cars_over_time(
    IN start_time DATETIME,
    IN end_time DATETIME
)
BEGIN
    SELECT
        CONCAT(c.make, ' ', c.model) AS `car`,
        YEAR(b.start_date_time) AS `year`,
        MONTHNAME(b.start_date_time) AS `month`,
        COUNT(*) as `count`
    FROM booking b
             JOIN car c ON b.car_id = c.id
    WHERE (start_time IS NULL OR b.start_date_time >= start_time)
      AND (end_time IS NULL OR b.start_date_time <= end_time)
    GROUP BY c.make, c.model, YEAR(b.start_date_time), MONTHNAME(b.start_date_time)
    ORDER BY `year` DESC, `month` DESC;
END;