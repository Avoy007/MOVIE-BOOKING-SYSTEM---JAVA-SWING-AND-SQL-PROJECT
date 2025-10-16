import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cinebook_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Mbk5059@";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    // USER OPERATIONS
    public static boolean registerUser(String username, String password, String email) {
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean loginUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Login failed: " + e.getMessage());
            return false;
        }
    }
    
    public static int getUserId(String username) {
        String query = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting user ID: " + e.getMessage());
        }
        return -1;
    }
    
    public static boolean userExists(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking user: " + e.getMessage());
            return false;
        }
    }
    
    // LOCATION OPERATIONS
    public static List<String> getAllLocations() {
        List<String> locations = new ArrayList<>();
        String query = "SELECT location_name FROM locations";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                locations.add(rs.getString("location_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching locations: " + e.getMessage());
        }
        return locations;
    }
    
    public static int getLocationId(String locationName) {
        String query = "SELECT location_id FROM locations WHERE location_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, locationName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("location_id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting location ID: " + e.getMessage());
        }
        return -1;
    }
    
    // MOVIE OPERATIONS
    public static List<String> getMoviesByLocation(String locationName) {
        List<String> movies = new ArrayList<>();
        String query = "SELECT m.movie_name FROM movies m JOIN locations l ON m.location_id = l.location_id WHERE l.location_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, locationName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                movies.add(rs.getString("movie_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching movies: " + e.getMessage());
        }
        return movies;
    }
    
    public static int getMovieId(String movieName, String locationName) {
        String query = "SELECT m.movie_id FROM movies m JOIN locations l ON m.location_id = l.location_id WHERE m.movie_name = ? AND l.location_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, movieName);
            pstmt.setString(2, locationName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("movie_id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting movie ID: " + e.getMessage());
        }
        return -1;
    }
    
    private static int getOrCreateMovieId(String movieName, String locationName) {
        int movieId = getMovieId(movieName, locationName);
        if (movieId != -1) {
            return movieId;
        }
        
        int locationId = getLocationId(locationName);
        if (locationId == -1) {
            System.err.println("Location not found: " + locationName);
            return -1;
        }
        
        String insertQuery = "INSERT INTO movies (movie_name, location_id, genre, duration, release_date) VALUES (?, ?, 'General', 150, CURDATE())";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, movieName);
            pstmt.setInt(2, locationId);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Created new movie entry: " + movieName + " in " + locationName);
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating movie: " + e.getMessage());
        }
        return -1;
    }
    
    // THEATER OPERATIONS
    public static List<String> getAllTheaters() {
        List<String> theaters = new ArrayList<>();
        String query = "SELECT theater_name FROM theaters";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                theaters.add(rs.getString("theater_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching theaters: " + e.getMessage());
        }
        return theaters;
    }
    
    public static int getTheaterId(String theaterName) {
        String query = "SELECT theater_id FROM theaters WHERE theater_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, theaterName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("theater_id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting theater ID: " + e.getMessage());
        }
        return -1;
    }
    
    public static int[] getTheaterSeatingConfig(String theaterName) {
        String query = "SELECT seating_config FROM theaters WHERE theater_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, theaterName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String config = rs.getString("seating_config");
                String[] parts = config.split(",");
                int[] seatingConfig = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    seatingConfig[i] = Integer.parseInt(parts[i].trim());
                }
                return seatingConfig;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching seating config: " + e.getMessage());
        }
        return new int[]{8, 10, 10, 12, 12, 10, 10, 8};
    }
    
    public static Map<String, String> getTheaterDetails(String theaterName) {
        Map<String, String> details = new HashMap<>();
        String query = "SELECT theater_type, theater_details FROM theaters WHERE theater_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, theaterName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                details.put("type", rs.getString("theater_type"));
                details.put("details", rs.getString("theater_details"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching theater details: " + e.getMessage());
        }
        return details;
    }
    
    // VERIFY AND CREATE SEATS IF MISSING
    public static void ensureSeatsExist(String theaterName) {
        int theaterId = getTheaterId(theaterName);
        if (theaterId == -1) return;
        
        // Check if seats exist for this theater
        String checkQuery = "SELECT COUNT(*) as seat_count FROM seats WHERE theater_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkQuery)) {
            pstmt.setInt(1, theaterId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt("seat_count") > 0) {
                System.out.println("Theater " + theaterName + " already has seats");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Error checking seats: " + e.getMessage());
        }
        
        // Create seats if they don't exist
        int[] config = getTheaterSeatingConfig(theaterName);
        String insertQuery = "INSERT INTO seats (theater_id, row_label, seat_number) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            
            int totalSeats = 0;
            for (int row = 0; row < config.length; row++) {
                char rowLabel = (char)('A' + row);
                for (int seat = 1; seat <= config[row]; seat++) {
                    pstmt.setInt(1, theaterId);
                    pstmt.setString(2, String.valueOf(rowLabel));
                    pstmt.setInt(3, seat);
                    pstmt.addBatch();
                    totalSeats++;
                }
            }
            pstmt.executeBatch();
            System.out.println("Created " + totalSeats + " seats for theater: " + theaterName);
        } catch (SQLException e) {
            System.err.println("Error creating seats: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // SEAT OPERATIONS - FIXED VERSION
    public static boolean[][] getBookedSeats(String movieName, String theaterName, String timeSlot, String locationName) {
        System.out.println("\n=== DEBUG: Getting booked seats ===");
        System.out.println("Movie: " + movieName + ", Theater: " + theaterName + ", Time: " + timeSlot + ", Location: " + locationName);
        
        // First ensure seats exist for this theater
        ensureSeatsExist(theaterName);
        
        int movieId = getMovieId(movieName, locationName);
        int theaterId = getTheaterId(theaterName);
        int showTimeId = getShowTimeId(timeSlot);
        int[] seatingConfig = getTheaterSeatingConfig(theaterName);
        
        boolean[][] bookedSeats = new boolean[seatingConfig.length][];
        for (int i = 0; i < seatingConfig.length; i++) {
            bookedSeats[i] = new boolean[seatingConfig[i]];
        }
        
        if (movieId == -1 || theaterId == -1 || showTimeId == -1) {
            System.err.println("Invalid IDs - Movie: " + movieId + ", Theater: " + theaterId + ", ShowTime: " + showTimeId);
            return bookedSeats;
        }
        
        // First, ensure the show exists
        int showId = getOrCreateShow(movieId, theaterId, showTimeId);
        System.out.println("Show ID: " + showId);
        
        // Query to get booked seats from seat_bookings table
        String query = "SELECT s.row_label, s.seat_number, sb.booking_status " +
                       "FROM seats s " +
                       "JOIN seat_bookings sb ON s.seat_id = sb.seat_id " +
                       "WHERE sb.show_id = ? AND sb.booking_status = 'booked'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, showId);
            ResultSet rs = pstmt.executeQuery();
            
            int bookedCount = 0;
            while (rs.next()) {
                String rowLabel = rs.getString("row_label");
                int seatNum = rs.getInt("seat_number");
                int rowIndex = rowLabel.charAt(0) - 'A';
                
                System.out.println("Found booked seat: " + rowLabel + seatNum + " (Row index: " + rowIndex + ", Seat: " + seatNum + ")");
                
                if (rowIndex >= 0 && rowIndex < bookedSeats.length && 
                    seatNum > 0 && seatNum <= bookedSeats[rowIndex].length) {
                    bookedSeats[rowIndex][seatNum - 1] = true;
                    bookedCount++;
                }
            }
            System.out.println("Total booked seats loaded: " + bookedCount);
        } catch (SQLException e) {
            System.err.println("Error fetching booked seats: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== END DEBUG ===\n");
        return bookedSeats;
    }
    
    public static int getShowTimeId(String timeSlot) {
        String query = "SELECT show_time_id FROM show_times WHERE time_slot = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, timeSlot);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("show_time_id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting show time ID: " + e.getMessage());
        }
        return -1;
    }
    
    public static int getOrCreateShow(int movieId, int theaterId, int showTimeId) {
        // Check if show exists for today
        String selectQuery = "SELECT show_id FROM shows WHERE movie_id = ? AND theater_id = ? AND show_time_id = ? AND show_date = CURDATE()";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            pstmt.setInt(1, movieId);
            pstmt.setInt(2, theaterId);
            pstmt.setInt(3, showTimeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int showId = rs.getInt("show_id");
                System.out.println("Found existing show ID: " + showId);
                return showId;
            }
        } catch (SQLException e) {
            System.err.println("Error checking show: " + e.getMessage());
        }
        
        // Create new show if it doesn't exist
        String insertQuery = "INSERT INTO shows (movie_id, theater_id, show_time_id, show_date) VALUES (?, ?, ?, CURDATE())";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, movieId);
            pstmt.setInt(2, theaterId);
            pstmt.setInt(3, showTimeId);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int showId = rs.getInt(1);
                System.out.println("Created new show ID: " + showId);
                initializeSeatBookings(showId, theaterId);
                return showId;
            }
        } catch (SQLException e) {
            System.err.println("Error creating show: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    private static void initializeSeatBookings(int showId, int theaterId) {
        String query = "INSERT INTO seat_bookings (show_id, seat_id, booking_status) " +
                       "SELECT ?, seat_id, 'available' FROM seats WHERE theater_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, showId);
            pstmt.setInt(2, theaterId);
            int rows = pstmt.executeUpdate();
            System.out.println("Initialized " + rows + " seat bookings for show ID: " + showId);
        } catch (SQLException e) {
            System.err.println("Error initializing seat bookings: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static boolean bookSeats(int userId, String movieName, String theaterName, String timeSlot, String locationName, String seatsList) {
        System.out.println("\n=== DEBUG: Booking seats ===");
        System.out.println("User ID: " + userId + ", Seats: " + seatsList);
        
        // First ensure seats exist
        ensureSeatsExist(theaterName);
        
        int movieId = getMovieId(movieName, locationName);
        int theaterId = getTheaterId(theaterName);
        int showTimeId = getShowTimeId(timeSlot);
        
        if (movieId == -1 || theaterId == -1 || showTimeId == -1) {
            System.err.println("Invalid booking parameters");
            return false;
        }
        
        int showId = getOrCreateShow(movieId, theaterId, showTimeId);
        
        if (showId == -1) {
            System.err.println("Could not get or create show");
            return false;
        }
        
        String[] seats = seatsList.split(", ");
        int successCount = 0;
        List<String> alreadyBookedSeats = new ArrayList<>();
        List<String> failedSeats = new ArrayList<>();
        
        for (String seat : seats) {
            if (seat.trim().isEmpty()) continue;
            
            char rowLabel = seat.charAt(0);
            int seatNum = Integer.parseInt(seat.substring(1));
            
            int seatId = getSeatId(theaterId, rowLabel, seatNum);
            
            if (seatId == -1) {
                System.err.println("Seat not found: " + seat + " (Theater: " + theaterName + ", ID: " + theaterId + ")");
                failedSeats.add(seat);
                continue;
            }
            
            // CRITICAL FIX: Check if seat is already booked (by anyone, including this user)
            String checkQuery = "SELECT booking_status, user_id FROM seat_bookings WHERE show_id = ? AND seat_id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, showId);
                checkStmt.setInt(2, seatId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    String currentStatus = rs.getString("booking_status");
                    Integer bookedByUserId = rs.getObject("user_id", Integer.class);
                    
                    if ("booked".equals(currentStatus)) {
                        if (bookedByUserId != null && bookedByUserId == userId) {
                            System.err.println("✗ Seat " + seat + " is already booked by YOU!");
                            alreadyBookedSeats.add(seat + " (your booking)");
                        } else {
                            System.err.println("✗ Seat " + seat + " is already booked by another user!");
                            alreadyBookedSeats.add(seat);
                        }
                        continue;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error checking seat status: " + e.getMessage());
                failedSeats.add(seat);
                continue;
            }
            
            // Try to book the seat with optimistic locking
            String query = "UPDATE seat_bookings SET user_id = ?, booking_status = 'booked', booked_at = NOW() " +
                           "WHERE show_id = ? AND seat_id = ? AND booking_status = 'available'";
            
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, showId);
                pstmt.setInt(3, seatId);
                int updated = pstmt.executeUpdate();
                if (updated > 0) {
                    System.out.println("✓ Booked seat: " + seat + " (Show ID: " + showId + ", Seat ID: " + seatId + ")");
                    successCount++;
                } else {
                    System.err.println("✗ Could not book seat (already booked): " + seat);
                    alreadyBookedSeats.add(seat);
                }
            } catch (SQLException e) {
                System.err.println("Error booking seat " + seat + ": " + e.getMessage());
                e.printStackTrace();
                failedSeats.add(seat);
            }
        }
        
        System.out.println("Successfully booked " + successCount + " out of " + seats.length + " seats");
        
        if (!alreadyBookedSeats.isEmpty()) {
            System.out.println("Already booked seats: " + String.join(", ", alreadyBookedSeats));
        }
        if (!failedSeats.isEmpty()) {
            System.out.println("Failed to book seats: " + String.join(", ", failedSeats));
        }
        
        System.out.println("=== END DEBUG ===\n");
        
        // Return true only if at least one seat was successfully booked
        return successCount > 0;
    }
    
    private static void listSeatsForTheater(int theaterId) {
        String query = "SELECT row_label, seat_number FROM seats WHERE theater_id = ? LIMIT 10";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, theaterId);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Available seats in theater (first 10):");
            while (rs.next()) {
                System.out.println("  " + rs.getString("row_label") + rs.getInt("seat_number"));
            }
        } catch (SQLException e) {
            System.err.println("Error listing seats: " + e.getMessage());
        }
    }
    
    private static int getSeatId(int theaterId, char rowLabel, int seatNum) {
        String query = "SELECT seat_id FROM seats WHERE theater_id = ? AND row_label = ? AND seat_number = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, theaterId);
            pstmt.setString(2, String.valueOf(rowLabel));
            pstmt.setInt(3, seatNum);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("seat_id");
            } else {
                System.err.println("DEBUG: No seat found for theater_id=" + theaterId + 
                                 ", row=" + rowLabel + ", seat=" + seatNum);
            }
        } catch (SQLException e) {
            System.err.println("Error getting seat ID: " + e.getMessage());
        }
        return -1;
    }
    
    // BOOKING OPERATIONS
    public static void saveBooking(int userId, String locationName, String movieName, String theaterName, 
                                   String timeSlot, String seatsList, int totalSeats, double totalAmount, String paymentMethod) {
        int locationId = getLocationId(locationName);
        int movieId = getMovieId(movieName, locationName);
        int theaterId = getTheaterId(theaterName);
        int showTimeId = getShowTimeId(timeSlot);
        
        String query = "INSERT INTO bookings (user_id, location_id, movie_id, theater_id, show_time_id, seats_booked, total_seats, total_amount, payment_method) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, locationId);
            pstmt.setInt(3, movieId);
            pstmt.setInt(4, theaterId);
            pstmt.setInt(5, showTimeId);
            pstmt.setString(6, seatsList);
            pstmt.setInt(7, totalSeats);
            pstmt.setDouble(8, totalAmount);
            pstmt.setString(9, paymentMethod);
            pstmt.executeUpdate();
            System.out.println("✓ Booking record saved successfully!");
        } catch (SQLException e) {
            System.err.println("Error saving booking: " + e.getMessage());
        }
    }
    
    public static List<String> getUserBookings(int userId) {
        List<String> bookings = new ArrayList<>();
        String query = "SELECT b.booking_id, m.movie_name, l.location_name, t.theater_name, st.time_slot, " +
                       "b.seats_booked, b.total_amount, b.booking_date " +
                       "FROM bookings b " +
                       "JOIN movies m ON b.movie_id = m.movie_id " +
                       "JOIN locations l ON b.location_id = l.location_id " +
                       "JOIN theaters t ON b.theater_id = t.theater_id " +
                       "JOIN show_times st ON b.show_time_id = st.show_time_id " +
                       "WHERE b.user_id = ? ORDER BY b.booking_date DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String booking = String.format("🎬 %s\n📍 %s\n🏛 %s\n⏰ %s\n💺 %s",
                        rs.getString("movie_name"),
                        rs.getString("location_name"),
                        rs.getString("theater_name"),
                        rs.getString("time_slot"),
                        rs.getString("seats_booked"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
        }
        return bookings;
    }
    
    // REVIEW OPERATIONS
    public static void saveReview(int userId, String movieName, String locationName, int rating) {
        int movieId = getOrCreateMovieId(movieName, locationName);
        
        if (movieId == -1) {
            System.err.println("Could not get or create movie ID for: " + movieName);
            return;
        }
        
        String checkQuery = "SELECT review_id FROM reviews WHERE user_id = ? AND movie_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement checkPstmt = conn.prepareStatement(checkQuery)) {
            checkPstmt.setInt(1, userId);
            checkPstmt.setInt(2, movieId);
            ResultSet rs = checkPstmt.executeQuery();
            
            if (rs.next()) {
                String updateQuery = "UPDATE reviews SET rating = ?, created_at = NOW() WHERE review_id = ?";
                try (PreparedStatement updatePstmt = conn.prepareStatement(updateQuery)) {
                    updatePstmt.setInt(1, rating);
                    updatePstmt.setInt(2, rs.getInt("review_id"));
                    updatePstmt.executeUpdate();
                    System.out.println("✓ Review updated successfully for " + movieName);
                }
            } else {
                String insertQuery = "INSERT INTO reviews (user_id, movie_id, rating) VALUES (?, ?, ?)";
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertQuery)) {
                    insertPstmt.setInt(1, userId);
                    insertPstmt.setInt(2, movieId);
                    insertPstmt.setInt(3, rating);
                    insertPstmt.executeUpdate();
                    System.out.println("✓ Review saved successfully for " + movieName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving review: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static List<String> getReviewsForUser(int userId) {
        List<String> reviews = new ArrayList<>();
        String query = "SELECT m.movie_name, r.rating, r.created_at FROM reviews r " +
                       "JOIN movies m ON r.movie_id = m.movie_id " +
                       "WHERE r.user_id = ? ORDER BY r.created_at DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String rating = convertRatingToText(rs.getInt("rating"));
                String date = rs.getTimestamp("created_at").toString().substring(0, 10);
                reviews.add(String.format("%s - %s (%s)", 
                    rs.getString("movie_name"), rating, date));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reviews: " + e.getMessage());
        }
        return reviews;
    }
    
    private static String convertRatingToText(int rating) {
        switch (rating) {
            case 5: return "⭐⭐⭐⭐⭐ Excellent";
            case 4: return "⭐⭐⭐⭐ Good";
            case 3: return "⭐⭐⭐ Average";
            case 2: return "⭐⭐ Poor";
            case 1: return "⭐ Terrible";
            default: return "Not Rated";
        }
    }
}