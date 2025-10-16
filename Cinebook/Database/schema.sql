-- Create Database
CREATE DATABASE IF NOT EXISTS cinebook_db;
USE cinebook_db;

-- Users Table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Locations Table
CREATE TABLE locations (
    location_id INT PRIMARY KEY AUTO_INCREMENT,
    location_name VARCHAR(50) UNIQUE NOT NULL
);

-- Movies Table (UPDATED - added genre, duration, release_date)
CREATE TABLE movies (
    movie_id INT PRIMARY KEY AUTO_INCREMENT,
    movie_name VARCHAR(100) NOT NULL,
    location_id INT NOT NULL,
    genre VARCHAR(50) DEFAULT 'General',
    duration INT DEFAULT 150,
    release_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (location_id) REFERENCES locations(location_id),
    UNIQUE KEY unique_movie_location (movie_name, location_id)
);

-- Theaters Table
CREATE TABLE theaters (
    theater_id INT PRIMARY KEY AUTO_INCREMENT,
    theater_name VARCHAR(100) UNIQUE NOT NULL,
    theater_type VARCHAR(50),
    theater_details VARCHAR(200),
    seating_config VARCHAR(500) NOT NULL
);

-- Show Times Table
CREATE TABLE show_times (
    show_time_id INT PRIMARY KEY AUTO_INCREMENT,
    time_slot VARCHAR(20) NOT NULL,
    time_label VARCHAR(30) NOT NULL
);

-- Seats Table
CREATE TABLE seats (
    seat_id INT PRIMARY KEY AUTO_INCREMENT,
    theater_id INT NOT NULL,
    row_label CHAR(1) NOT NULL,
    seat_number INT NOT NULL,
    FOREIGN KEY (theater_id) REFERENCES theaters(theater_id),
    UNIQUE KEY unique_seat (theater_id, row_label, seat_number)
);

-- Shows Table (Movie + Theater + Time combinations)
CREATE TABLE shows (
    show_id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT NOT NULL,
    theater_id INT NOT NULL,
    show_time_id INT NOT NULL,
    show_date DATE NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (theater_id) REFERENCES theaters(theater_id),
    FOREIGN KEY (show_time_id) REFERENCES show_times(show_time_id),
    UNIQUE KEY unique_show (movie_id, theater_id, show_time_id, show_date)
);

-- Seat Bookings Table
CREATE TABLE seat_bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    show_id INT NOT NULL,
    seat_id INT NOT NULL,
    user_id INT,
    booking_status ENUM('available', 'booked', 'cancelled') DEFAULT 'available',
    booked_at TIMESTAMP,
    FOREIGN KEY (show_id) REFERENCES shows(show_id),
    FOREIGN KEY (seat_id) REFERENCES seats(seat_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    UNIQUE KEY unique_booking (show_id, seat_id)
);

-- Bookings Table (Booking Records)
CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    location_id INT NOT NULL,
    movie_id INT NOT NULL,
    theater_id INT NOT NULL,
    show_time_id INT NOT NULL,
    seats_booked VARCHAR(500) NOT NULL,
    total_seats INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50),
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (location_id) REFERENCES locations(location_id),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (theater_id) REFERENCES theaters(theater_id),
    FOREIGN KEY (show_time_id) REFERENCES show_times(show_time_id)
);

-- Reviews Table
CREATE TABLE reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    movie_id INT NOT NULL,
    rating INT CHECK(rating >= 1 AND rating <= 5),
    review_text VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    UNIQUE KEY unique_review (user_id, movie_id)
);

-- Insert sample data
INSERT INTO locations (location_name) VALUES 
('Hyderabad'), ('Bengaluru'), ('Chennai'), ('Mumbai'), ('Delhi');

INSERT INTO show_times (time_slot, time_label) VALUES 
('10:00 AM', 'Morning Show'),
('1:00 PM', 'Matinee Show'),
('4:00 PM', 'Evening Show'),
('7:00 PM', 'Prime Time'),
('9:00 PM', 'Night Show'),
('11:00 PM', 'Late Night');

INSERT INTO theaters (theater_name, theater_type, theater_details, seating_config) VALUES 
('PVR Cinemas', 'Premium Large', 'Dolby Atmos • Recliner Seats', '6,8,10,12,12,12,10,8,6'),
('INOX Multiplex', 'Standard', 'Premium Sound • Comfort Seats', '8,10,10,12,12,10,10,8'),
('Cinepolis Premium', 'Luxury Compact', 'VIP Experience • Premium', '6,8,8,10,10,8,6'),
('Acropollis Theatre', 'IMAX Experience', 'Giant Screen • IMAX Sound', '10,12,14,16,16,16,14,12,10'),
('Fame Adlabs', 'Luxury Seating', 'Luxury Recliners • Bar', '4,6,6,8,8,6'),
('Shringar Cinema', 'Budget Friendly', 'Affordable • Good Sound', '10,12,12,14,14,12,10'),
('Miraj Cinema', 'Classic Cinema', 'Classic Ambience • AC', '8,10,12,12,12,10,8,6'),
('Rajmahal Cinema', 'Dolby Atmos', 'Latest Sound Tech', '8,10,12,14,14,12,10,8'),
('Galaxy Theatre', 'Family Theater', 'Kids Friendly • Spacious', '6,8,10,10,10,8,6'),
('Cinestars Platinum', '4DX Adventure', 'Motion Seats • 4D Effects', '8,10,12,14,14,14,12,10,8');

-- Sample movies for each location (with genre and duration)
INSERT INTO movies (movie_name, location_id, genre, duration) VALUES 
('RRR', 1, 'Action', 182), ('Kalki 2898 AD', 1, 'Sci-Fi', 180), ('Pushpa 2', 1, 'Action', 165), 
('Baahubali', 1, 'Action', 159), ('Arjun Reddy', 1, 'Drama', 182), ('Eega', 1, 'Fantasy', 134),
('Leo', 2, 'Action', 164), ('Vikram', 2, 'Action', 174), ('Jailer', 2, 'Action', 168), 
('KGF', 2, 'Action', 156), ('Kantara', 2, 'Thriller', 148), ('777 Charlie', 2, 'Drama', 164), ('Rathnan Prapancha', 2, 'Drama', 147),
('Ponniyin Selvan', 3, 'Historical', 167), ('Beast', 3, 'Action', 155), ('Vikram', 3, 'Action', 174), 
('Master', 3, 'Action', 179), ('Enthiran', 3, 'Sci-Fi', 155), ('Jawan', 3, 'Action', 169), ('Varisu', 3, 'Action', 169),
('Pathaan', 4, 'Action', 146), ('Jawan', 4, 'Action', 169), ('Dunki', 4, 'Drama', 161), 
('Animal', 4, 'Action', 201), ('Rocky Aur Rani', 4, 'Romance', 168), ('Gadar 2', 4, 'Action', 170), ('OMG 2', 4, 'Drama', 156),
('Jawan', 5, 'Action', 169), ('Pathaan', 5, 'Action', 146), ('Tiger 3', 5, 'Action', 155), 
('Dunki', 5, 'Drama', 161), ('12th Fail', 5, 'Drama', 147), ('Dream Girl 2', 5, 'Comedy', 139), ('Fukrey 3', 5, 'Comedy', 147);

-- Create seats for all theaters
DELIMITER //
CREATE PROCEDURE generate_seats()
BEGIN
    DECLARE theater_id INT;
    DECLARE seating_config VARCHAR(500);
    DECLARE row_count INT;
    DECLARE seat_count INT;
    DECLARE current_row INT;
    DECLARE seats_in_row INT;
    DECLARE seat_num INT;
    DECLARE done INT DEFAULT FALSE;
    DECLARE theater_cursor CURSOR FOR SELECT theater_id, seating_config FROM theaters;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN theater_cursor;
    
    theater_loop: LOOP
        FETCH theater_cursor INTO theater_id, seating_config;
        IF done THEN
            LEAVE theater_loop;
        END IF;
        
        SET current_row = 0;
        SET row_count = (LENGTH(seating_config) - LENGTH(REPLACE(seating_config, ',', ''))) + 1;
        
        WHILE current_row < row_count DO
            SET seats_in_row = CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(seating_config, ',', current_row + 1), ',', -1) AS UNSIGNED);
            SET seat_num = 1;
            
            WHILE seat_num <= seats_in_row DO
                INSERT IGNORE INTO seats (theater_id, row_label, seat_number) 
                VALUES (theater_id, CHAR(65 + current_row), seat_num);
                SET seat_num = seat_num + 1;
            END WHILE;
            
            SET current_row = current_row + 1;
        END WHILE;
    END LOOP;
    
    CLOSE theater_cursor;
END//
DELIMITER ;

CALL generate_seats();
DROP PROCEDURE generate_seats;