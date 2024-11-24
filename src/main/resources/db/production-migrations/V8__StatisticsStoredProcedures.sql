-- Procedure: Calculate total distance for a specific car
CREATE PROCEDURE sum_distance_by_car(IN car_id_input BIGINT)
BEGIN
    SELECT COALESCE(SUM(distance), 0) AS total_distance
    FROM booking
    WHERE car_id = car_id_input;
END;

-- Procedure: Calculate total distance across all cars
CREATE PROCEDURE sum_all_distances()
BEGIN
    SELECT COALESCE(SUM(distance), 0) AS total_distance
    FROM booking;
END;

-- Procedure: Calculate total price for a specific car
CREATE PROCEDURE sum_price_by_car(IN car_id_input BIGINT)
BEGIN
    SELECT COALESCE(SUM(total_price), 0) AS total_price
    FROM booking
    WHERE car_id = car_id_input;
END;

-- Procedure: Calculate total price across all cars
CREATE PROCEDURE sum_all_prices()
BEGIN
    SELECT COALESCE(SUM(total_price), 0) AS total_price
    FROM booking;
END;

-- Procedure: Get the most popular cars
CREATE PROCEDURE get_most_popular_cars()
BEGIN
    SELECT
        CONCAT(c.make, ' ', c.model) AS `key`,
        COUNT(*) AS `value`
    FROM booking b
             JOIN car c ON b.car_id = c.id
    GROUP BY c.make, c.model
    ORDER BY `value` DESC;
END;

-- Procedure: Get the most popular trips
CREATE PROCEDURE get_most_popular_trips()
BEGIN
    SELECT
        CONCAT(sc.name, ' -> ', ec.name) AS `key`,
        COUNT(*) AS `value`
    FROM booking b
             JOIN city sc ON b.start_city_id = sc.id
             JOIN city ec ON b.end_city_id = ec.id
    GROUP BY sc.name, ec.name
    ORDER BY `value` DESC;
END;

-- Procedure: Get bookings per month
CREATE PROCEDURE get_bookings_per_month()
BEGIN
    SELECT
        MONTHNAME(start_date_time) AS `key`,
        COUNT(*) AS `value`
    FROM booking
    GROUP BY MONTH(start_date_time), MONTHNAME(start_date_time)
    ORDER BY MONTH(start_date_time) DESC;
END;

-- Procedure: Calculate average ratings by car ID
CREATE PROCEDURE avg_ratings_by_car_id(IN car_id_input BIGINT)
BEGIN
    SELECT
        COALESCE(AVG((car_condition + car_speed + value_for_money) / 3.0), 0) AS avg_rating
    FROM review r
             JOIN booking b ON r.booking_id = b.id
    WHERE b.car_id = car_id_input;
END;


-- Procedure: Get most bought discount plans
CREATE PROCEDURE get_most_bought_discount_plans()
BEGIN
    SELECT
        dp.title AS discount_plan_title,
        COUNT(dpp.discount_plan_id) AS purchase_count
    FROM
        discount_plan_purchase dpp
            JOIN
        discount_plan dp ON dpp.discount_plan_id = dp.id
    GROUP BY
        dp.title
    ORDER BY
        purchase_count DESC;
END;
