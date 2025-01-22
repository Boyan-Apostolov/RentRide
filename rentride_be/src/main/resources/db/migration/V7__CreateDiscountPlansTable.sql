CREATE TABLE discount_plan (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               title VARCHAR(255) NOT NULL,
                               description TEXT NOT NULL,
                               remaining_uses INT NOT NULL,
                               discount_value INT NOT NULL,
                               price DECIMAL(18,2) NOT NULL
);

CREATE TABLE discount_plan_purchase (
                                        user_id BIGINT NOT NULL,
                                        discount_plan_id BIGINT NOT NULL,
                                        purchase_date TIMESTAMP NOT NULL,
                                        remaining_uses INT NOT NULL,
                                        PRIMARY KEY (user_id, discount_plan_id),
                                        CONSTRAINT fk_user
                                            FOREIGN KEY (user_id) REFERENCES users(id)
                                                ON DELETE CASCADE,
                                        CONSTRAINT fk_discount_plan
                                            FOREIGN KEY (discount_plan_id) REFERENCES discount_plan(id)
                                                ON DELETE CASCADE
);