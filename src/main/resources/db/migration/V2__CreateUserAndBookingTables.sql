CREATE TABLE users (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      birth_date DATE NOT NULL
);

CREATE TABLE user_role
(
    id        int         NOT NULL AUTO_INCREMENT,
    user_id   BIGINT         NOT NULL,
    role_name varchar(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES users (id)
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
                         payment_id nvarchar(50) not null,

                         CONSTRAINT fk_booking_start_city FOREIGN KEY (start_city_id) REFERENCES city(id),
                         CONSTRAINT fk_booking_end_city FOREIGN KEY (end_city_id) REFERENCES city(id),
                         CONSTRAINT fk_booking_car FOREIGN KEY (car_id) REFERENCES car(id),
                         CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id)
);