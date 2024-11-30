CREATE TABLE auction (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         end_date_time TIMESTAMP NOT NULL,
                         description VARCHAR(255) NOT NULL,
                         unlock_code VARCHAR(255) NOT NULL,
                         is_code_used BOOLEAN NOT NULL,
                         car_id BIGINT NOT NULL,
                         user_id BIGINT,
                         min_bid_amount DECIMAL(18, 2) NOT NULL,
                         CONSTRAINT fk_auction_car FOREIGN KEY (car_id) REFERENCES car(id) ON DELETE CASCADE,
                         CONSTRAINT fk_auction_winner_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE bid (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     date_time TIMESTAMP NOT NULL,
                     amount DECIMAL(18, 2) NOT NULL,
                     auction_id BIGINT NOT NULL,
                     user_id BIGINT NOT NULL,
                     CONSTRAINT fk_bid_auction FOREIGN KEY (auction_id) REFERENCES auction(id) ON DELETE CASCADE,
                     CONSTRAINT fk_bid_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE auction_bids_mapping (
                                      auction_id BIGINT NOT NULL,
                                      bid_id BIGINT NOT NULL,
                                      PRIMARY KEY (auction_id, bid_id),
                                      CONSTRAINT fk_auction_bids_auction FOREIGN KEY (auction_id) REFERENCES auction(id) ON DELETE CASCADE,
                                      CONSTRAINT fk_auction_bids_bid FOREIGN KEY (bid_id) REFERENCES bid(id) ON DELETE CASCADE
);