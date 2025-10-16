# CineBook - Movie Booking System with MySQL Backend

A fully functional movie booking application built with Java Swing UI and MySQL database for persistent data storage.

## Overview

CineBook is a complete desktop application that allows users to:
- Register and login securely
- Browse movies by city/location
- Select theaters and show times
- Book movie tickets with interactive seat selection
- Make payments through various methods
- View booking history
- Submit and view movie reviews

All data is persistently stored in MySQL database, ensuring information is retained between sessions.

## Project Files

```
CineBook/
├── src/
│   ├── DatabaseManager.java          # JDBC operations & database layer
│   └── MovieBookingSystem.java        # UI & application logic
│
├── database/
│   └── schema.sql                     # Complete MySQL database schema
│
├── lib/
│   └── mysql-connector-java-8.0.33.jar  # MySQL JDBC Driver
│
├── bin/                               # Compiled .class files (after compilation)
│
└── README.md                          # This file
```

## System Requirements

- **Java:** Version 8 or higher
- **MySQL:** Version 5.7 or higher
- **Operating System:** Windows, Linux, or macOS
- **RAM:** Minimum 512 MB
- **Disk Space:** 50 MB for application + database

## Installation Instructions

### Step 1: Download MySQL JDBC Driver

1. Visit: https://dev.mysql.com/downloads/connector/j/
2. Select "Platform Independent"
3. Download version 8.0.33
4. Extract the ZIP file
5. Copy `mysql-connector-java-8.0.33.jar` to `lib/` folder in your project

### Step 2: Create Project Directories

```bash
# Create necessary folders
mkdir src
mkdir bin
mkdir lib
mkdir database
```

### Step 3: Setup MySQL Database

1. Start MySQL Server
2. Open MySQL command line
3. Run the entire content of `database/schema.sql`

```sql
-- Inside MySQL command line:
CREATE DATABASE IF NOT EXISTS cinebook_db;
USE cinebook_db;
source /path/to/database/schema.sql;
```

### Step 4: Configure Database Connection

Edit `src/DatabaseManager.java` and update these lines (around line 4-6):

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/cinebook_db";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = ""; // Change if you have MySQL password
```

### Step 5: Compile the Application

Navigate to your project folder and run:

**Windows:**
```bash
javac -cp lib/mysql-connector-java-8.0.33.jar src/DatabaseManager.java src/MovieBookingSystem.java -d bin/
```

**Linux/macOS:**
```bash
javac -cp lib/mysql-connector-java-8.0.33.jar src/DatabaseManager.java src/MovieBookingSystem.java -d bin/
```

### Step 6: Run the Application

**Windows:**
```bash
java -cp bin;lib/mysql-connector-java-8.0.33.jar MovieBookingSystem
```

**Linux/macOS:**
```bash
java -cp bin:lib/mysql-connector-java-8.0.33.jar MovieBookingSystem
```

## Default Login Credentials

After setting up the database, you can login with:

- **Username:** demo
- **Password:** pass

Or create a new account through the "Register" button.

## Features

### User Management
- ✓ User registration with validation
- ✓ Secure login system
- ✓ Persistent user sessions
- ✓ Password protection

### Movie Booking
- ✓ Browse movies by location/city
- ✓ Search functionality
- ✓ Real-time seat availability
- ✓ Interactive seat selection
- ✓ Visual seat mapping (available/booked/selected)
- ✓ Price calculation

### Theater & Show Management
- ✓ Multiple theaters with unique seating configurations
- ✓ Theater details and amenities
- ✓ 6 show time slots per day
- ✓ Show information display

### Booking & Payments
- ✓ Complete booking confirmation
- ✓ Multiple payment methods
  - Credit/Debit Card
  - UPI Payment
  - Net Banking
  - Digital Wallet
- ✓ Booking receipt generation
- ✓ Seat reservation with row/column display

### History & Reviews
- ✓ Complete booking history with all details
- ✓ 5-star rating system
- ✓ Movie review submission
- ✓ Personal review history

### Data Persistence
- ✓ All data stored in MySQL
- ✓ Zero data loss between sessions
- ✓ Transaction support
- ✓ Data integrity with foreign keys

## Database Schema

The application uses 10 interconnected tables:

1. **users** - User accounts and credentials
2. **locations** - Cities/locations
3. **movies** - Movie catalog
4. **theaters** - Theater information
5. **show_times** - Available show times
6. **seats** - Individual seat mapping
7. **shows** - Movie + Theater + Time combinations
8. **seat_bookings** - Seat reservation tracking
9. **bookings** - Complete booking records
10. **reviews** - User ratings and reviews

## Sample Data Included

- **5 Locations:** Hyderabad, Bengaluru, Chennai, Mumbai, Delhi
- **34 Movies:** Popular Indian films distributed across locations
- **10 Theaters:** Premium theaters with different seating configurations
- **6 Show Times:** 10 AM to 11 PM slots

## Usage Guide

### 1. Login/Register
- Start the application
- Use demo/pass to login or click Register for new account
- Fill required fields and click Register

### 2. Book a Movie
- Select city from dropdown
- Click "Browse Movies"
- Click on desired movie
- Select preferred theater
- Choose show time
- Select seats from interactive grid
- Review total amount
- Choose payment method
- Confirm booking

### 3. View Bookings
- Click "Home" at any time
- View booking history in the "My Bookings" section
- Shows movie, theater, location, time, and seats

### 4. Submit Reviews
- Navigate to Home panel
- In "Reviews" section, select a booked movie
- Choose rating (1-5 stars)
- Click "Submit Review"
- View past reviews below

## Troubleshooting

### Compilation Errors

**Error: "cannot find symbol"**
- Verify both Java files are in `src/` folder
- Check filenames: DatabaseManager.java, MovieBookingSystem.java
- Ensure MySQL JDBC driver is in `lib/` folder

**Error: "No suitable driver"**
- Check mysql-connector-java-8.0.33.jar is in classpath
- Verify -cp parameter includes lib folder
- Reinstall driver if necessary

### Runtime Errors

**Error: "Connection refused"**
- Verify MySQL server is running
- Check database credentials in DatabaseManager.java
- Verify port 3306 is accessible

**Error: "Table doesn't exist"**
- Run entire schema.sql file in MySQL
- Verify cinebook_db database exists
- Check all 10 tables are created: `SHOW TABLES;`

**Error: "Access denied for user"**
- Verify DB_USER and DB_PASSWORD match MySQL credentials
- Create user if needed: `CREATE USER 'username'@'localhost' IDENTIFIED BY 'password';`
- Grant permissions: `GRANT ALL PRIVILEGES ON cinebook_db.* TO 'username'@'localhost';`

### Application Issues

**No data visible in movies list**
- Check MySQL connection
- Verify sample data was inserted
- Run: `SELECT COUNT(*) FROM movies;` in MySQL

**Can't login**
- Verify MySQL is running
- Check database connection settings
- Try demo/pass credentials

**Seats not loading**
- Ensure show times are in database
- Check theater seating configuration
- Verify shows table has data

## Performance Tips

- Keep MySQL running in background for faster access
- Use SSD for MySQL data directory for better performance
- Close other applications to free up RAM
- Update Java to latest version for optimal performance

## Security Considerations

**Current Implementation:**
- Plain text password storage (for demo purposes)

**For Production:**
- Implement password hashing (bcrypt, SHA-256)
- Use SQL parameterized queries (already implemented)
- Add HTTPS for data transmission
- Implement role-based access control
- Add audit logging
- Use connection pooling

## Future Enhancements

- Email confirmation for bookings
- SMS notifications
- Cancellation and refund system
- Advanced search and filtering
- Admin panel for theater management
- Analytics and reporting
- Multiple language support
- Mobile application
- QR code ticket generation
- Loyalty program integration

## API/Methods Reference

### DatabaseManager.java Key Methods

User Operations:
```java
registerUser(String username, String password, String email)
loginUser(String username, String password)
getUserId(String username)
userExists(String username)
```

Movie Operations:
```java
getMoviesByLocation(String locationName)
getMovieId(String movieName, String locationName)
```

Booking Operations:
```java
bookSeats(int userId, String movieName, String theaterName, ...)
getUserBookings(int userId)
```

## Support & Documentation

For issues or questions:
1. Check the Troubleshooting section above
2. Verify all setup steps were followed correctly
3. Check MySQL server status
4. Review database schema for structure
5. Test individual components separately

## Version Information

- **Application Version:** 1.0
- **Database Version:** MySQL 5.7+
- **Java Version:** 8 or higher
- **JDBC Driver Version:** 8.0.33

## License & Usage

This project is designed for educational and learning purposes. Feel free to modify and extend it for your needs.

## Contact & Support

For setup issues:
1. Verify Java installation: `java -version`
2. Verify MySQL installation and running status
3. Check classpath configuration
4. Review database connection credentials

## Changelog

### Version 1.0
- Initial release
- Complete booking system
- MySQL database integration
- User authentication
- Review system
- Persistent data storage

---

**Last Updated:** 2024
**Status:** Production Ready