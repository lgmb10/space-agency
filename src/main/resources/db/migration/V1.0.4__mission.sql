CREATE TABLE mission
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    ship_id        INT          NOT NULL,
    departure_date DATETIME     NOT NULL,
    arrival_date   DATETIME     NOT NULL,
    origin         VARCHAR(255) NOT NULL,
    destination    VARCHAR(255) NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    max_passengers INT,
    CONSTRAINT fk_mission_ship FOREIGN KEY (ship_id) REFERENCES ship (id)
);