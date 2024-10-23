CREATE TABLE city (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      lat DOUBLE NOT NULL,
                      lon DOUBLE NOT NULL,
                      depo_address VARCHAR(255) NOT NULL
);

CREATE TABLE car (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     make VARCHAR(255) NOT NULL,
                     model VARCHAR(255) NOT NULL,
                     registration_number VARCHAR(255) NOT NULL,
                     fuel_consumption DOUBLE NOT NULL,
                     city_id BIGINT,
                     CONSTRAINT fk_car_city FOREIGN KEY (city_id) REFERENCES city(id)
);

CREATE TABLE car_feature (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             feature_type INT NOT NULL,
                             feature_text VARCHAR(255) NOT NULL
);

CREATE TABLE car_photos (
                            car_id BIGINT,
                            photo_base64 MEDIUMTEXT NOT NULL,
                            CONSTRAINT fk_car_photos_car FOREIGN KEY (car_id) REFERENCES car(id) ON DELETE CASCADE
);

CREATE TABLE car_feature_mapping (
                                     car_id BIGINT,
                                     feature_id BIGINT,
                                     PRIMARY KEY (car_id, feature_id),
                                     CONSTRAINT fk_car_features FOREIGN KEY (car_id) REFERENCES car(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_feature_features FOREIGN KEY (feature_id) REFERENCES car_feature(id) ON DELETE CASCADE
);