package com.ecofeast.models;

/**
 * User Model Class
 * Represents a user in the EcoFeast system with role-based access control.
 * Users can be either ADMIN or regular USER.
 */
public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String role; // ADMIN or USER
    private String accountStatus; // PENDING, APPROVED, REJECTED
    private boolean isActive;

    // Default Constructor
    public User() {
    }

    // Constructor with parameters
    public User(int userId, String firstName, String lastName, String email,
                String phoneNumber, String password, String role, String accountStatus, boolean isActive) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.accountStatus = accountStatus;
        this.isActive = isActive;
    }

    // Constructor for registration (without userId)
    public User(String firstName, String lastName, String email,
                String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = "USER";
        this.accountStatus = "PENDING";
        this.isActive = true;
    }

    // Getters and Setters
    /**
     * Gets the unique user ID.
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the unique user ID.
     * @param userId the user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the user's first name.
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the user's email address.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's phone number.
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the user's phone number.
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the user's password (hashed in production).
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's role (ADMIN or USER).
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user's role (ADMIN or USER).
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the account status.
     * @return the account status
     */
    public String getAccountStatus() {
        return accountStatus;
    }

    /**
     * Sets the account status.
     * @param accountStatus the status to set
     */
    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    /**
     * Checks if the user account is active.
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of the user account.
     * @param active the active status to set
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Gets the full name of the user.
     * @return the full name (firstName + lastName)
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Checks if the user is an administrator.
     * @return true if user is ADMIN, false otherwise
     */
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + role + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
