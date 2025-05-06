package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login_Screen extends JPanel {

    // User data: {username, password, security_level_string}
    private static final String[][] USERS = {
            {"employee", "emp123", "10"},
            {"manager", "man456", "100"},
            {"admin", "adm789", "1000"}
            // Add more users here
    };

    // UI Components
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton logoutButton;
    private JLabel statusLabel; // To display current security level or welcome message

    public Login_Screen() { // TODO make this use the Main_Window label changes
        // Set layout for the JPanel
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize UI components
        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15); // Width of 15 columns

        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        loginButton = new JButton("Login");
        logoutButton = new JButton("Logout");

        statusLabel = new JLabel();
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                int newSecurityLevel = checkEligibility(username, password);

                if (newSecurityLevel > 1) { // Successfully logged in to a non-guest level
                    Main.loggedInUsername = username; // Store username
                    Main.callEvent("security_level_change", newSecurityLevel);
                    // Main.security_level is updated by callEvent in this example
                    JOptionPane.showMessageDialog(Login_Screen.this,
                            "Login successful! Welcome, " + username + ".",
                            "Login Status", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Main.loggedInUsername = null; // Clear username on failed login
                    // If checkEligibility returns 1 (guest), it means login failed or was for guest
                    // Ensure security level is set to guest if it wasn't already
                    Main.callEvent("security_level_change", 1);
                    JOptionPane.showMessageDialog(Login_Screen.this,
                            "Invalid username or password.",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
                // Clear password field after attempt
                passwordField.setText("");
                updateView(); // Refresh the view
            }
        });

        // Add action listener for the logout button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String formerUser = Main.loggedInUsername;
                Main.callEvent("security_level_change", 1); // Revert to guest level
                // Main.loggedInUsername is set to null by callEvent in this example's Main
                JOptionPane.showMessageDialog(Login_Screen.this,
                        (formerUser != null ? formerUser : "User") + " logged out successfully.",
                        "Logout Status", JOptionPane.INFORMATION_MESSAGE);
                updateView(); // Refresh the view
            }
        });

        // Initial view setup
        updateView();
    }

    /**
     * Updates the panel's content based on the current security level.
     */
    private void updateView() {
        // Remove all existing components before adding new ones
        removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // More padding for the overall layout
        gbc.gridwidth = 2; // Span across two columns for titles/status
        gbc.anchor = GridBagConstraints.CENTER; // Center components

        if (Main.getSecurity_level() == 1) { // Guest view (Login form)
            titleLabel.setText("User Login");
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(titleLabel, gbc);

            gbc.gridwidth = 1; // Reset gridwidth
            gbc.anchor = GridBagConstraints.LINE_END; // Align labels to the right
            gbc.gridx = 0;
            gbc.gridy = 1;
            add(usernameLabel, gbc);

            gbc.anchor = GridBagConstraints.LINE_START; // Align fields to the left
            gbc.gridx = 1;
            gbc.gridy = 1;
            add(usernameField, gbc);
            usernameField.setText(""); // Clear username field

            gbc.anchor = GridBagConstraints.LINE_END;
            gbc.gridx = 0;
            gbc.gridy = 2;
            add(passwordLabel, gbc);

            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.gridx = 1;
            gbc.gridy = 2;
            add(passwordField, gbc);
            passwordField.setText(""); // Clear password field

            gbc.gridwidth = 2; // Span for button
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.gridx = 0;
            gbc.gridy = 3;
            add(loginButton, gbc);

            statusLabel.setText("Current Security Level: " + getSecurityLevelName(Main.getSecurity_level()));
            gbc.gridy = 4;
            add(statusLabel, gbc);

        } else { // Logged-in view
            titleLabel.setText("User Dashboard");
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(titleLabel, gbc);

            String welcomeMessage = "Welcome, " + (Main.loggedInUsername != null ? Main.loggedInUsername : "User") + "!";
            JLabel welcomeLabel = new JLabel(welcomeMessage); // Use a local label for dynamic text
            welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            gbc.gridy = 1;
            add(welcomeLabel, gbc);

            statusLabel.setText("Current Security Level: " + getSecurityLevelName(Main.getSecurity_level()));
            gbc.gridy = 2;
            add(statusLabel, gbc);

            gbc.gridy = 3;
            add(logoutButton, gbc);
        }

        // Refresh the panel
        revalidate();
        repaint();
    }

    /**
     * Checks if the provided username and password are correct.
     *
     * @param username The username to check.
     * @param password The password to check.
     * @return The security level (int) if credentials are valid, otherwise returns 1 (guest level).
     */
    private int checkEligibility(String username, String password) {
        if (username == null || password == null) {
            return 1; // Guest level for null inputs
        }
        for (String[] user : USERS) {
            if (user[0].equals(username) && user[1].equals(password)) {
                try {
                    return Integer.parseInt(user[2]); // Return the security level
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing security level for user: " + username);
                    return 1; // Default to guest on error
                }
            }
        }
        return 1; // Default to guest level if no match found (login failed)
    }

    /**
     * Helper method to get a string representation of the security level.
     *
     * @param level The integer security level.
     * @return String name of the security level.
     */
    private String getSecurityLevelName(int level) {
        switch (level) {
            case 1:
                return "Guest";
            case 10:
                return "Employee";
            case 100:
                return "Manager";
            case 1000:
                return "Admin";
            default:
                return "Unknown (" + level + ")";
        }
    }
}
