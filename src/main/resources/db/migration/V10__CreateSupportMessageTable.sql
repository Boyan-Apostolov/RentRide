CREATE TABLE message (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         message TEXT,
                         answer TEXT,
                         user_id BIGINT NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES users (id)
);