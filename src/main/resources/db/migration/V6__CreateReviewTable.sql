CREATE TABLE review (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        created_on TIMESTAMP NOT NULL,
                        text VARCHAR(255) NOT NULL,
                        user_id BIGINT NOT NULL,
                        booking_id BIGINT NOT NULL UNIQUE,
                        value_for_money INT NOT NULL,
                        car_condition INT NOT NULL,
                        car_speed INT NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (booking_id) REFERENCES booking(id)
);