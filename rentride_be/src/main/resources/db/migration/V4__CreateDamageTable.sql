CREATE TABLE damage (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        cost DECIMAL(18,2) NOT NULL,
                        icon_url VARCHAR(255) NOT NULL
);