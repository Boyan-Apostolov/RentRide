CREATE TABLE payment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         description VARCHAR(255) NOT NULL,
                         amount DECIMAL(10, 2) NOT NULL,
                         user_id BIGINT NOT NULL,
                         is_paid BOOLEAN NOT NULL,
                         stripe_link TEXT NOT NULL,
                         created_on TIMESTAMP NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);