CREATE TABLE booking
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    passenger_id INT NOT NULL,
    mission_id   INT NOT NULL,
    CONSTRAINT fk_booking_passenger FOREIGN KEY (passenger_id) REFERENCES passenger (id),
    CONSTRAINT fk_booking_mission FOREIGN KEY (mission_id) REFERENCES mission (id),
    CONSTRAINT uk_booking_passenger_mission UNIQUE (passenger_id, mission_id)
);