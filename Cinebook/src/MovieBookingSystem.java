import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

class BookingRecord {
    String user, location, movie, theater, time, seats;
    public BookingRecord(String user, String location, String movie, String theater, String time, String seats) {
        this.user = user;
        this.location = location;
        this.movie = movie;
        this.theater = theater;
        this.time = time;
        this.seats = seats;
    }
    @Override
    public String toString() {
        return String.format("%s | %s | %s @ %s | %s | Seats: %s", user, location, movie, theater, time, seats);
    }
}

class UITheme {
    static final Color PRIMARY_COLOR = new Color(220, 38, 38);
    static final Color PRIMARY_DARK = new Color(185, 28, 28);
    static final Color SECONDARY_COLOR = new Color(37, 99, 235);
    static final Color ACCENT_COLOR = new Color(234, 179, 8);
    static final Color SUCCESS_COLOR = new Color(22, 163, 74);
    static final Color BACKGROUND_LIGHT = new Color(249, 250, 251);
    static final Color BACKGROUND_DARK = new Color(17, 24, 39);
    static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    static final Color CARD_BACKGROUND = Color.WHITE;java -cp ".;..\lib\mysql-connector-j-9.4.0.jar" MovieBookingSystem
    static final Color BORDER_COLOR = new Color(229, 231, 235);
    
    static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 32);
    static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 20);
    static final Font HEADING_FONT = new Font("Arial", Font.BOLD, 16);
    static final Font BODY_FONT = new Font("Arial", Font.PLAIN, 14);
    static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);
}

class StyledButton extends JButton {
    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    
    public StyledButton(String text, Color bgColor) {
        super(text);
        this.backgroundColor = bgColor;
        this.hoverColor = bgColor.darker();
        this.pressedColor = bgColor.darker().darker();
        
        setFont(UITheme.HEADING_FONT);
        setForeground(Color.WHITE);
        setBackground(backgroundColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(true);
        setOpaque(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (isEnabled()) setBackground(hoverColor); }
            public void mouseExited(MouseEvent e) { setBackground(backgroundColor); }
            public void mousePressed(MouseEvent e) { if (isEnabled()) setBackground(pressedColor); }
            public void mouseReleased(MouseEvent e) { if (isEnabled()) setBackground(hoverColor); }
        });
    }
}

class StyledTextField extends JTextField {
    public StyledTextField(int columns) {
        super(columns);
        setFont(UITheme.BODY_FONT);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        setBackground(Color.WHITE);
        setForeground(UITheme.TEXT_PRIMARY);
        
        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
            public void focusLost(FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
        });
    }
}

public class MovieBookingSystem {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}

class MainFrame extends JFrame {
    CardLayout cardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(cardLayout);
    
    LoginPanel loginPanel;
    HomePanel homePanel;
    MoviePanel moviePanel;
    TheaterPanel theaterPanel;
    TimePanel timePanel;
    SeatPanel seatPanel;
    
    String selectedUser, selectedLocation, selectedMovie, selectedTheater, selectedTime;
    int selectedUserId = -1;
    
    public MainFrame() {
        setTitle("🎬  CineBook - Premium Movie Booking");
        setSize(1200, 800);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        loginPanel = new LoginPanel(this);
        homePanel = new HomePanel(this);
        moviePanel = new MoviePanel(this);
        theaterPanel = new TheaterPanel(this);
        timePanel = new TimePanel(this);
        seatPanel = new SeatPanel(this);
        
        cardPanel.add(loginPanel, "Login");
        cardPanel.add(homePanel, "Home");
        cardPanel.add(moviePanel, "Movie");
        cardPanel.add(theaterPanel, "Theater");
        cardPanel.add(timePanel, "Time");
        cardPanel.add(seatPanel, "Seat");
        
        add(cardPanel);
        showCard("Login");
        setVisible(true);
    }
    
    public void showCard(String name) {
        cardLayout.show(cardPanel, name);
    }
}

class LoginPanel extends JPanel {
    public LoginPanel(MainFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(new Color(17, 24, 39));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        
        JLabel icon = new JLabel("🎬", SwingConstants.CENTER);
        icon.setFont(new Font("Arial", Font.PLAIN, 64));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel title = new JLabel("Welcome to CineBook", SwingConstants.CENTER);
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Your Premium Movie Booking Experience", SwingConstants.CENTER);
        subtitle.setFont(UITheme.BODY_FONT);
        subtitle.setForeground(UITheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cardPanel.add(icon);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(subtitle);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(UITheme.HEADING_FONT);
        userLabel.setForeground(UITheme.TEXT_PRIMARY);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        StyledTextField userField = new StyledTextField(25);
        
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(UITheme.HEADING_FONT);
        passLabel.setForeground(UITheme.TEXT_PRIMARY);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField passField = new JPasswordField(25);
        passField.setFont(UITheme.BODY_FONT);
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        
        cardPanel.add(userLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(userField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(passLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(passField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(450, 50));
        
        StyledButton loginBtn = new StyledButton("Login", UITheme.PRIMARY_COLOR);
        StyledButton registerBtn = new StyledButton("Register", UITheme.SECONDARY_COLOR);
        
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        cardPanel.add(buttonPanel);
        
        gbc.gridx = 0; gbc.gridy = 0;
        add(cardPanel, gbc);
        
        loginBtn.addActionListener(e -> {
            String u = userField.getText(), p = new String(passField.getPassword());
            if (DatabaseManager.loginUser(u, p)) {
                frame.selectedUser = u;
                frame.selectedUserId = DatabaseManager.getUserId(u);
                JOptionPane.showMessageDialog(frame, "✓ Welcome back, " + u + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                frame.showCard("Home");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        registerBtn.addActionListener(e -> {
            String u = userField.getText(), p = new String(passField.getPassword());
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!DatabaseManager.userExists(u)) {
                if (DatabaseManager.registerUser(u, p, "")) {
                    JOptionPane.showMessageDialog(frame, "✓ Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    userField.setText("");
                    passField.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

class HomePanel extends JPanel {
    JTextArea historyArea, reviewArea;
    
    public HomePanel(MainFrame frame) {
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BACKGROUND_LIGHT);
        
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.PRIMARY_COLOR);
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("🏠 Home");
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(Color.WHITE);
        
        StyledButton logoutBtn = new StyledButton("Logout", UITheme.BACKGROUND_DARK);
        logoutBtn.addActionListener(e -> {
            frame.selectedUser = null;
            frame.selectedUserId = -1;
            frame.showCard("Login");
        });
        
        topBar.add(title, BorderLayout.WEST);
        topBar.add(logoutBtn, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);
        
        JPanel mainContent = new JPanel(new GridLayout(1, 3, 20, 0));
        mainContent.setBackground(UITheme.BACKGROUND_LIGHT);
        mainContent.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel cityCard = createCard("📍 Select City");
        JComboBox<String> locationBox = new JComboBox<>(DatabaseManager.getAllLocations().toArray(new String[0]));
        locationBox.setFont(UITheme.BODY_FONT);
        locationBox.setBackground(Color.WHITE);
        locationBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        cityCard.add(locationBox);
        cityCard.add(Box.createRigidArea(new Dimension(0, 20)));
        
        StyledButton nextBtn = new StyledButton("Browse Movies →", UITheme.PRIMARY_COLOR);
        nextBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextBtn.addActionListener(e -> {
            frame.selectedLocation = (String) locationBox.getSelectedItem();
            frame.moviePanel.refreshMovies();
            frame.showCard("Movie");
        });
        cityCard.add(nextBtn);
        
        JPanel historyCard = createCard("📜 My Bookings");
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(UITheme.SMALL_FONT);
        historyArea.setBackground(UITheme.BACKGROUND_LIGHT);
        historyArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
        historyScroll.setPreferredSize(new Dimension(250, 400));
        historyCard.add(historyScroll);
        
        JPanel reviewCard = createCard("⭐ Reviews");
        JComboBox<String> movieReviewBox = new JComboBox<>();
        movieReviewBox.setFont(UITheme.SMALL_FONT);
        movieReviewBox.setBackground(Color.WHITE);
        
        String[] ratings = {"5 - Excellent", "4 - Good", "3 - Average", "2 - Poor", "1 - Terrible"};
        JComboBox<String> ratingBox = new JComboBox<>(ratings);
        ratingBox.setFont(UITheme.SMALL_FONT);
        ratingBox.setBackground(Color.WHITE);
        
        reviewCard.add(new JLabel("Movie:"));
        reviewCard.add(movieReviewBox);
        reviewCard.add(Box.createRigidArea(new Dimension(0, 10)));
        reviewCard.add(new JLabel("Rating:"));
        reviewCard.add(ratingBox);
        reviewCard.add(Box.createRigidArea(new Dimension(0, 10)));
        
        StyledButton submitReviewBtn = new StyledButton("Submit Review", UITheme.SUCCESS_COLOR);
        submitReviewBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitReviewBtn.addActionListener(e -> {
            String selectedMovie = (String) movieReviewBox.getSelectedItem();
            if (selectedMovie != null && !selectedMovie.isEmpty()) {
                String rating = (String) ratingBox.getSelectedItem();
                int ratingValue = Character.getNumericValue(rating.charAt(0));
                
                String movieLocation = frame.selectedLocation;
                if (movieLocation == null || movieLocation.isEmpty()) {
                    List<String> bookings = DatabaseManager.getUserBookings(frame.selectedUserId);
                    for (String booking : bookings) {
                        if (booking.contains(selectedMovie)) {
                            String[] parts = booking.split("\n");
                            for (String part : parts) {
                                if (part.startsWith("📍 ")) {
                                    movieLocation = part.substring(2).trim();
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
                
                DatabaseManager.saveReview(frame.selectedUserId, selectedMovie, movieLocation, ratingValue);
                JOptionPane.showMessageDialog(frame, "✓ Review submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshReviews(frame, reviewArea);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a movie to review", "No Movie Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
        reviewCard.add(submitReviewBtn);
        reviewCard.add(Box.createRigidArea(new Dimension(0, 15)));
        
        reviewArea = new JTextArea();
        reviewArea.setEditable(false);
        reviewArea.setFont(UITheme.SMALL_FONT);
        reviewArea.setBackground(UITheme.BACKGROUND_LIGHT);
        JScrollPane reviewScroll = new JScrollPane(reviewArea);
        reviewScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
        reviewScroll.setPreferredSize(new Dimension(250, 200));
        reviewCard.add(reviewScroll);
        
        mainContent.add(cityCard);
        mainContent.add(historyCard);
        mainContent.add(reviewCard);
        add(mainContent, BorderLayout.CENTER);
        
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                refreshBookingHistory(frame, historyArea);
                updateMovieReviewBox(frame, movieReviewBox);
                refreshReviews(frame, reviewArea);
            }
        });
    }
    
    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel cardTitle = new JLabel(title);
        cardTitle.setFont(UITheme.SUBTITLE_FONT);
        cardTitle.setForeground(UITheme.TEXT_PRIMARY);
        cardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(cardTitle);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        return card;
    }
    
    private void updateMovieReviewBox(MainFrame frame, JComboBox<String> movieReviewBox) {
        movieReviewBox.removeAllItems();
        Set<String> bookedMovies = new HashSet<>();
        for (String booking : DatabaseManager.getUserBookings(frame.selectedUserId)) {
            String[] parts = booking.split("\n");
            for (String part : parts) {
                if (part.startsWith("🎬 ")) {
                    String movie = part.substring(2).trim();
                    if (!movie.isEmpty()) {
                        bookedMovies.add(movie);
                    }
                    break;
                }
            }
        }
        for (String movie : bookedMovies) {
            movieReviewBox.addItem(movie);
        }
    }
    
    private void refreshBookingHistory(MainFrame frame, JTextArea historyArea) {
        historyArea.setText("");
        List<String> bookings = DatabaseManager.getUserBookings(frame.selectedUserId);
        if (bookings.isEmpty()) {
            historyArea.setText("No bookings yet.\n\nStart booking to see your history here!");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String booking : bookings) {
                sb.append("━━━━━━━━━━━━━━━━\n");
                sb.append(booking).append("\n\n");
            }
            historyArea.setText(sb.toString());
            historyArea.setCaretPosition(0);
        }
    }
    
    private void refreshReviews(MainFrame frame, JTextArea reviewArea) {
        reviewArea.setText("");
        List<String> reviews = DatabaseManager.getReviewsForUser(frame.selectedUserId);
        if (reviews.isEmpty()) {
            reviewArea.setText("No reviews yet.\n\nSubmit reviews for movies you've watched!");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String review : reviews) {
                sb.append(review).append("\n\n");
            }
            reviewArea.setText(sb.toString());
            reviewArea.setCaretPosition(0);
        }
    }
}

class MoviePanel extends JPanel {
    MainFrame frame;
    JPanel movieListPanel;
    JTextField searchField;
    JLabel headerLabel;
    
    public MoviePanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BACKGROUND_LIGHT);
        
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.PRIMARY_COLOR);
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        headerLabel = new JLabel("🎬 Now Showing");
        headerLabel.setFont(UITheme.TITLE_FONT);
        headerLabel.setForeground(Color.WHITE);
        topBar.add(headerLabel, BorderLayout.WEST);
        
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navButtons.setOpaque(false);
        StyledButton homeBtn = new StyledButton("🏠 Home", UITheme.BACKGROUND_DARK);
        homeBtn.addActionListener(e -> frame.showCard("Home"));
        navButtons.add(homeBtn);
        topBar.add(navButtons, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);
        
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        searchField = new StyledTextField(30);
        searchField.setText("Search for a movie...");
        searchField.setForeground(UITheme.TEXT_SECONDARY);
        
        searchPanel.add(new JLabel("🔍"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.PAGE_START);
        
        movieListPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        movieListPanel.setBackground(UITheme.BACKGROUND_LIGHT);
        movieListPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JScrollPane scrollPane = new JScrollPane(movieListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search for a movie...")) {
                    searchField.setText("");
                    searchField.setForeground(UITheme.TEXT_PRIMARY);
                }
            }
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search for a movie...");
                    searchField.setForeground(UITheme.TEXT_SECONDARY);
                }
            }
        });
        
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { refreshMovies(); }
        });
        
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                headerLabel.setText("🎬 Now Showing in " + frame.selectedLocation);
                refreshMovies();
            }
        });
    }
    
    public void refreshMovies() {
        movieListPanel.removeAll();
        List<String> movies = DatabaseManager.getMoviesByLocation(frame.selectedLocation);
        String query = searchField.getText().toLowerCase().replace("search for a movie...", "");
        
        for (String movie : movies) {
            if (movie.toLowerCase().contains(query)) {
                JPanel movieCard = createMovieCard(movie);
                movieListPanel.add(movieCard);
            }
        }
        movieListPanel.revalidate();
        movieListPanel.repaint();
    }
    
    private JPanel createMovieCard(String movie) {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 280));
        imageLabel.setBackground(new Color(240, 240, 240));
        imageLabel.setOpaque(true);
        
        boolean imageLoaded = false;
        try {
            String baseFilename = movie.replaceAll("[^a-zA-Z0-9]", "");
            String[] extensions = {".jpg", ".jpeg", ".JPG", ".JPEG", ".png", ".PNG"};
            
            for (String ext : extensions) {
                java.io.File imgFile = new java.io.File("movie_posters/" + baseFilename + ext);
                if (imgFile.exists()) {
                    ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
                    Image img = icon.getImage().getScaledInstance(200, 280, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                    imageLabel.setText("");
                    imageLoaded = true;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading image for " + movie + ": " + e.getMessage());
        }
        
        if (!imageLoaded) {
            imageLabel.setText("<html><center><div style='font-size:48px'>🎬</div><br>" + movie + "</center></html>");
            imageLabel.setFont(UITheme.BODY_FONT);
            imageLabel.setForeground(UITheme.TEXT_SECONDARY);
        }
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(movie);
        titleLabel.setFont(UITheme.HEADING_FONT);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        StyledButton bookBtn = new StyledButton("Book Now", UITheme.PRIMARY_COLOR);
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookBtn.addActionListener(e -> {
            frame.selectedMovie = movie;
            frame.showCard("Theater");
        });
        
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(bookBtn);
        
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
                ));
            }
        });
        
        return card;
    }
}

class TheaterPanel extends JPanel {
    public TheaterPanel(MainFrame frame) {
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BACKGROUND_LIGHT);
        
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.PRIMARY_COLOR);
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("🏛 Select Theater");
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(Color.WHITE);
        topBar.add(title, BorderLayout.WEST);
        
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navButtons.setOpaque(false);
        StyledButton homeBtn = new StyledButton("🏠 Home", UITheme.BACKGROUND_DARK);
        homeBtn.addActionListener(e -> frame.showCard("Home"));
        StyledButton backBtn = new StyledButton("← Back", UITheme.BACKGROUND_DARK);
        backBtn.addActionListener(e -> frame.showCard("Movie"));
        navButtons.add(homeBtn);
        navButtons.add(backBtn);
        topBar.add(navButtons, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);
        
        JPanel theaterGrid = new JPanel(new GridLayout(0, 2, 20, 20));
        theaterGrid.setBackground(UITheme.BACKGROUND_LIGHT);
        theaterGrid.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        List<String> theaters = DatabaseManager.getAllTheaters();
        for (String theater : theaters) {
            Map<String, String> details = DatabaseManager.getTheaterDetails(theater);
            JPanel theaterCard = createTheaterCard(theater, details.get("type"), details.get("details"), frame);
            theaterGrid.add(theaterCard);
        }
        
        JScrollPane scrollPane = new JScrollPane(theaterGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createTheaterCard(String name, String type, String details, MainFrame frame) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(UITheme.SUBTITLE_FONT);
        nameLabel.setForeground(UITheme.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel typeLabel = new JLabel(type != null ? type : "Standard");
        typeLabel.setFont(UITheme.BODY_FONT);
        typeLabel.setForeground(UITheme.PRIMARY_COLOR);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel detailsLabel = new JLabel(details != null ? details : "Quality Entertainment");
        detailsLabel.setFont(UITheme.SMALL_FONT);
        detailsLabel.setForeground(UITheme.TEXT_SECONDARY);
        detailsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        StyledButton selectBtn = new StyledButton("Select Theater →", UITheme.PRIMARY_COLOR);
        selectBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectBtn.addActionListener(e -> {
            frame.selectedTheater = name;
            frame.showCard("Time");
        });
        
        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(typeLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(detailsLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(selectBtn);
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
        });
        
        return card;
    }
}

class TimePanel extends JPanel {
    String[] times = {"10:00 AM", "1:00 PM", "4:00 PM", "7:00 PM", "9:00 PM", "11:00 PM"};
    String[] labels = {"Morning Show", "Matinee Show", "Evening Show", "Prime Time", "Night Show", "Late Night"};
    
    public TimePanel(MainFrame frame) {
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BACKGROUND_LIGHT);
        
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.PRIMARY_COLOR);
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("⏰ Select Show Time");
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(Color.WHITE);
        topBar.add(title, BorderLayout.WEST);
        
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navButtons.setOpaque(false);
        StyledButton homeBtn = new StyledButton("🏠 Home", UITheme.BACKGROUND_DARK);
        homeBtn.addActionListener(e -> frame.showCard("Home"));
        StyledButton backBtn = new StyledButton("← Back", UITheme.BACKGROUND_DARK);
        backBtn.addActionListener(e -> frame.showCard("Theater"));
        navButtons.add(homeBtn);
        navButtons.add(backBtn);
        topBar.add(navButtons, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UITheme.BACKGROUND_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        for (int i = 0; i < times.length; i++) {
            JPanel timeCard = createTimeCard(times[i], labels[i], frame);
            contentPanel.add(timeCard);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createTimeCard(String time, String label, MainFrame frame) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        timeLabel.setForeground(UITheme.PRIMARY_COLOR);
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(UITheme.BODY_FONT);
        labelText.setForeground(UITheme.TEXT_SECONDARY);
        
        infoPanel.add(timeLabel);
        infoPanel.add(labelText);
        
        StyledButton selectBtn = new StyledButton("Select Time →", UITheme.PRIMARY_COLOR);
        selectBtn.addActionListener(e -> {
            frame.selectedTime = time;
            frame.seatPanel.refreshSeats();
            frame.showCard("Seat");
        });
        
        card.add(infoPanel, BorderLayout.WEST);
        card.add(selectBtn, BorderLayout.EAST);
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(25, 30, 25, 30)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(25, 30, 25, 30)
                ));
            }
        });
        
        return card;
    }
}

class SeatPanel extends JPanel {
    List<JButton> seatButtons = new ArrayList<>();
    JLabel infoLabel, priceLabel;
    List<Integer> currentSelection = new ArrayList<>();
    private static final String[] ROW_LABELS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
    private int[] currentSeatsPerRow;
    
    public SeatPanel(MainFrame frame) {
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BACKGROUND_LIGHT);
        
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.PRIMARY_COLOR);
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel title = new JLabel("💺 Select Your Seats");
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(Color.WHITE);
        
        infoLabel = new JLabel("Loading...");
        infoLabel.setFont(UITheme.SMALL_FONT);
        infoLabel.setForeground(new Color(255, 255, 255, 200));
        
        titlePanel.add(title);
        titlePanel.add(infoLabel);
        topBar.add(titlePanel, BorderLayout.WEST);
        
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navButtons.setOpaque(false);
        StyledButton homeBtn = new StyledButton("🏠 Home", UITheme.BACKGROUND_DARK);
        homeBtn.addActionListener(e -> frame.showCard("Home"));
        StyledButton backBtn = new StyledButton("← Back", UITheme.BACKGROUND_DARK);
        backBtn.addActionListener(e -> frame.showCard("Time"));
        navButtons.add(homeBtn);
        navButtons.add(backBtn);
        topBar.add(navButtons, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(UITheme.BACKGROUND_LIGHT);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel screenPanel = new JPanel();
        screenPanel.setBackground(UITheme.BACKGROUND_LIGHT);
        JLabel screenLabel = new JLabel("🎬 SCREEN");
        screenLabel.setFont(UITheme.SUBTITLE_FONT);
        screenLabel.setOpaque(true);
        screenLabel.setBackground(new Color(50, 50, 50));
        screenLabel.setForeground(Color.WHITE);
        screenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        screenLabel.setBorder(BorderFactory.createEmptyBorder(15, 100, 15, 100));
        screenPanel.add(screenLabel);
        mainPanel.add(screenPanel, BorderLayout.NORTH);
        
        JPanel seatContainer = new JPanel();
        seatContainer.setLayout(new BoxLayout(seatContainer, BoxLayout.Y_AXIS));
        seatContainer.setBackground(UITheme.BACKGROUND_LIGHT);
        
        JScrollPane scrollPane = new JScrollPane(seatContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(20, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        legendPanel.setOpaque(false);
        legendPanel.add(createLegendItem(UITheme.SUCCESS_COLOR, "Available"));
        legendPanel.add(createLegendItem(new Color(220, 53, 69), "Booked"));
        legendPanel.add(createLegendItem(UITheme.SECONDARY_COLOR, "Selected"));
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        priceLabel = new JLabel("Total: ₹0");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        priceLabel.setForeground(UITheme.TEXT_PRIMARY);
        
        StyledButton confirmBtn = new StyledButton("Confirm Booking →", UITheme.PRIMARY_COLOR);
        confirmBtn.addActionListener(e -> confirmBooking(frame));
        
        rightPanel.add(priceLabel);
        rightPanel.add(confirmBtn);
        
        bottomPanel.add(legendPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) { refreshSeats(); }
        });
    }
    
    private JPanel createLegendItem(Color color, String text) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        item.setOpaque(false);
        
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(24, 24));
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(color.darker(), 1));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(UITheme.BODY_FONT);
        textLabel.setForeground(UITheme.TEXT_PRIMARY);
        
        item.add(colorBox);
        item.add(textLabel);
        return item;
    }
    
    public void refreshSeats() {
        MainFrame frame = (MainFrame) SwingUtilities.getWindowAncestor(this);
        if (frame == null || frame.selectedMovie == null || frame.selectedTheater == null || frame.selectedTime == null) return;
        
        System.out.println("\n=== SEAT PANEL: Refreshing seats ===");
        System.out.println("Movie: " + frame.selectedMovie);
        System.out.println("Theater: " + frame.selectedTheater);
        System.out.println("Time: " + frame.selectedTime);
        System.out.println("Location: " + frame.selectedLocation);
        
        currentSeatsPerRow = DatabaseManager.getTheaterSeatingConfig(frame.selectedTheater);
        
        JScrollPane scrollPane = null;
        for (Component comp : ((JPanel)getComponent(1)).getComponents()) {
            if (comp instanceof JScrollPane) {
                scrollPane = (JScrollPane) comp;
                break;
            }
        }
        
        if (scrollPane != null) {
            JPanel seatContainer = (JPanel) scrollPane.getViewport().getView();
            seatButtons.clear();
            seatContainer.removeAll();
            
            int seatIndex = 0;
            for (int row = 0; row < currentSeatsPerRow.length; row++) {
                JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
                rowPanel.setBackground(UITheme.BACKGROUND_LIGHT);
                
                JLabel rowLabel = new JLabel(ROW_LABELS[row]);
                rowLabel.setFont(UITheme.HEADING_FONT);
                rowLabel.setForeground(UITheme.TEXT_PRIMARY);
                rowLabel.setPreferredSize(new Dimension(30, 40));
                rowPanel.add(rowLabel);
                
                int seatsInRow = currentSeatsPerRow[row];
                for (int seat = 1; seat <= seatsInRow; seat++) {
                    String seatName = ROW_LABELS[row] + seat;
                    JButton seatBtn = new JButton(seatName);
                    seatBtn.setFont(new Font("Arial", Font.BOLD, 12));
                    seatBtn.setPreferredSize(new Dimension(55, 40));
                    seatBtn.setFocusPainted(false);
                    seatBtn.setBorderPainted(false);
                    seatBtn.setOpaque(true);
                    seatBtn.setForeground(Color.WHITE);
                    seatBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    final int currentIndex = seatIndex;
                    seatBtn.addActionListener(e -> handleSeatClick(frame, seatBtn, currentIndex));
                    
                    seatButtons.add(seatBtn);
                    rowPanel.add(seatBtn);
                    seatIndex++;
                }
                seatContainer.add(rowPanel);
            }
            seatContainer.revalidate();
            seatContainer.repaint();
        }
        
        infoLabel.setText(String.format("%s @ %s | %s | %s", 
            frame.selectedMovie, frame.selectedTheater, frame.selectedTime, frame.selectedLocation));
        
        currentSelection.clear();
        updatePriceLabel();
        
        // CRITICAL FIX: Get booked seats and apply them correctly
        boolean[][] booked = DatabaseManager.getBookedSeats(frame.selectedMovie, frame.selectedTheater, frame.selectedTime, frame.selectedLocation);
        
        System.out.println("\n=== APPLYING BOOKED STATUS TO BUTTONS ===");
        System.out.println("Total seat buttons created: " + seatButtons.size());
        System.out.println("Seating config: " + Arrays.toString(currentSeatsPerRow));
        
        // Apply booked status to seat buttons
        int buttonIndex = 0;
        for (int row = 0; row < booked.length && row < currentSeatsPerRow.length; row++) {
            for (int seat = 0; seat < booked[row].length && seat < currentSeatsPerRow[row]; seat++) {
                if (buttonIndex < seatButtons.size()) {
                    JButton btn = seatButtons.get(buttonIndex);
                    boolean isBooked = booked[row][seat];
                    
                    if (isBooked) {
                        System.out.println("Setting seat " + ROW_LABELS[row] + (seat + 1) + " as BOOKED (button index: " + buttonIndex + ")");
                    }
                    
                    btn.setEnabled(!isBooked);
                    btn.setBackground(isBooked ? new Color(220, 53, 69) : UITheme.SUCCESS_COLOR);
                    btn.setToolTipText(isBooked ? "Booked" : "Available - ₹250");
                    
                    if (!isBooked) {
                        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    } else {
                        btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                    
                    buttonIndex++;
                }
            }
        }
        
        System.out.println("Total buttons processed: " + buttonIndex);
        System.out.println("=== END APPLYING BOOKED STATUS ===\n");
    }
    
    private void handleSeatClick(MainFrame frame, JButton seat, int seatIndex) {
        if (currentSelection.contains(seatIndex)) {
            currentSelection.remove(Integer.valueOf(seatIndex));
            seat.setBackground(UITheme.SUCCESS_COLOR);
            seat.setToolTipText("Available - ₹250");
        } else {
            currentSelection.add(seatIndex);
            seat.setBackground(UITheme.SECONDARY_COLOR);
            seat.setToolTipText("Selected - ₹250");
        }
        updatePriceLabel();
    }
    
    private void updatePriceLabel() {
        int total = currentSelection.size() * 250;
        priceLabel.setText(String.format("Total: ₹%d (%d seats)", total, currentSelection.size()));
    }
    
    private void confirmBooking(MainFrame frame) {
        if (currentSelection.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please select at least one seat", "No Seats Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        showPaymentDialog(frame);
    }
    
    private void showPaymentDialog(MainFrame frame) {
        JDialog paymentDialog = new JDialog(frame, "Payment Method", true);
        paymentDialog.setSize(500, 400);
        paymentDialog.setLocationRelativeTo(frame);
        paymentDialog.setLayout(new BorderLayout(0, 0));
        
        JPanel header = new JPanel();
        header.setBackground(UITheme.PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        JLabel headerLabel = new JLabel("💳 Choose Payment Method");
        headerLabel.setFont(UITheme.SUBTITLE_FONT);
        headerLabel.setForeground(Color.WHITE);
        header.add(headerLabel);
        paymentDialog.add(header, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        String[] paymentMethods = {"💳 Credit/Debit Card", "📱 UPI Payment", "🏦 Net Banking", "💰 Digital Wallet"};
        
        for (String method : paymentMethods) {
            StyledButton btn = new StyledButton(method, UITheme.SECONDARY_COLOR);
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            btn.addActionListener(e -> {
                processPayment(frame, method);
                paymentDialog.dispose();
            });
            buttonPanel.add(btn);
        }
        
        paymentDialog.add(buttonPanel, BorderLayout.CENTER);
        
        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        StyledButton cancelBtn = new StyledButton("Cancel", new Color(107, 114, 128));
        cancelBtn.addActionListener(e -> paymentDialog.dispose());
        footer.add(cancelBtn);
        paymentDialog.add(footer, BorderLayout.SOUTH);
        
        paymentDialog.setVisible(true);
    }
    
    private void processPayment(MainFrame frame, String paymentMethod) {
        StringBuilder seatNames = new StringBuilder();
        int row = 0, index = 0;
        int[] seatingConfig = DatabaseManager.getTheaterSeatingConfig(frame.selectedTheater);
        
        for (Integer seatIndex : currentSelection) {
            index = 0;
            for (row = 0; row < seatingConfig.length; row++) {
                if (index + seatingConfig[row] > seatIndex) {
                    break;
                }
                index += seatingConfig[row];
            }
            int seatNum = seatIndex - index + 1;
            seatNames.append(ROW_LABELS[row]).append(seatNum).append(", ");
        }
        
        String seatsList = seatNames.length() > 0 ? seatNames.substring(0, seatNames.length() - 2) : "";
        double totalAmount = currentSelection.size() * 250.0;
        
        // CRITICAL FIX: Check if booking was successful
        boolean bookingSuccess = DatabaseManager.bookSeats(frame.selectedUserId, frame.selectedMovie, frame.selectedTheater, frame.selectedTime, frame.selectedLocation, seatsList);
        
        if (!bookingSuccess) {
            JOptionPane.showMessageDialog(frame,
                "⚠️ BOOKING FAILED!\n\n" +
                "The selected seats are already booked or unavailable.\n" +
                "Please select different seats and try again.",
                "Booking Failed",
                JOptionPane.WARNING_MESSAGE);
            refreshSeats(); // Refresh to show current status
            return;
        }
        
        // Only save booking record if seats were actually booked
        DatabaseManager.saveBooking(frame.selectedUserId, frame.selectedLocation, frame.selectedMovie, frame.selectedTheater, frame.selectedTime, seatsList, currentSelection.size(), totalAmount, paymentMethod);
        
        JOptionPane.showMessageDialog(frame,
            String.format("✅ BOOKING CONFIRMED!\n\n" +
                "Movie: %s\n" +
                "Theater: %s\n" +
                "Location: %s\n" +
                "Time: %s\n" +
                "Seats: %s\n" +
                "Total Seats: %d\n" +
                "Amount Paid: ₹%.2f\n" +
                "Payment: %s\n\n" +
                "Thank you, %s!",
                frame.selectedMovie, frame.selectedTheater, frame.selectedLocation,
                frame.selectedTime, seatsList, currentSelection.size(), totalAmount,
                paymentMethod, frame.selectedUser),
            "Booking Successful",
            JOptionPane.INFORMATION_MESSAGE);
        
        refreshSeats();
    }
}