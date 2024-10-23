CREATE TABLE users (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      role INT NOT NULL,
                      birth_date DATE NOT NULL
);

CREATE TABLE booking (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         status INT NOT NULL,
                         coverage INT NOT NULL,
                         start_date_time TIMESTAMP NOT NULL,
                         end_date_time TIMESTAMP NOT NULL,
                         start_city_id BIGINT NOT NULL,
                         end_city_id BIGINT NOT NULL,
                         car_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         distance BIGINT NOT NULL,
                         total_price DECIMAL(18,2) NOT NULL,

                         CONSTRAINT fk_booking_start_city FOREIGN KEY (start_city_id) REFERENCES city(id),
                         CONSTRAINT fk_booking_end_city FOREIGN KEY (end_city_id) REFERENCES city(id),
                         CONSTRAINT fk_booking_car FOREIGN KEY (car_id) REFERENCES car(id),
                         CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id)
);