DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS passenger;
DROP TABLE IF EXISTS flight;


CREATE TABLE IF NOT EXISTS passenger (
    passenger_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone_no BIGINT NOT NULL,
    email_id VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS address (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    house_no INT NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    pass_id INT UNIQUE,
    CONSTRAINT fk_address_passenger FOREIGN KEY (pass_id) REFERENCES passenger(passenger_id)
);

-- Flight Table
CREATE TABLE IF NOT EXISTS flight (
    flight_id INT AUTO_INCREMENT PRIMARY KEY,
    airline VARCHAR(50),
    origin VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    price DOUBLE,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL
);

-- Ticket Table
CREATE TABLE IF NOT EXISTS ticket (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    pnr VARCHAR(50) NOT NULL,
    seat_no VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    flight_id INT,
    passenger_id INT,
    CONSTRAINT fk_ticket_flight FOREIGN KEY (flight_id) REFERENCES flight(flight_id),
    CONSTRAINT fk_ticket_passenger FOREIGN KEY (passenger_id) REFERENCES passenger(passenger_id)
);
