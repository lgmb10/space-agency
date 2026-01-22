CREATE TABLE passenger
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    first_name        VARCHAR(255),
    last_name         VARCHAR(255),
    email             VARCHAR(255) NOT NULL,
    weight            INT,
    medical_clearance VARCHAR(255),
    CONSTRAINT uk_passenger_email UNIQUE (email)
);