package com.ecofeast.service;

import com.ecofeast.dao.UserDao;
import com.ecofeast.model.User;
import com.ecofeast.util.PasswordUtil;
import com.ecofeast.util.ValidationUtil;

import java.sql.SQLException;
import java.util.Locale;

public class UserService {
    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public enum LoginFailure {
        SUCCESS,
        UNKNOWN_EMAIL,
        WRONG_PASSWORD
    }

    /** Outcome of a login attempt (distinguishes unknown email vs wrong password for clearer UI). */
    public static final class LoginResult {
        public final User user;
        public final LoginFailure failure;

        private LoginResult(User user, LoginFailure failure) {
            this.user = user;
            this.failure = failure;
        }

        public boolean isSuccess() {
            return failure == LoginFailure.SUCCESS && user != null;
        }
    }

    /**
     * Authenticates credentials. Does not check account_status (caller handles PENDING/REJECTED).
     */
    public LoginResult tryLogin(String email, String password) throws SQLException {
        if (email == null || password == null) {
            return new LoginResult(null, LoginFailure.UNKNOWN_EMAIL);
        }
        email = email.trim().toLowerCase(Locale.ROOT);
        password = password.trim();
        if (email.isEmpty() || password.isEmpty()) {
            return new LoginResult(null, LoginFailure.UNKNOWN_EMAIL);
        }
        User user = userDao.findByEmail(email);
        if (user == null) {
            return new LoginResult(null, LoginFailure.UNKNOWN_EMAIL);
        }
        if (!PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            return new LoginResult(null, LoginFailure.WRONG_PASSWORD);
        }
        return new LoginResult(user, LoginFailure.SUCCESS);
    }

    public User login(String email, String password) throws SQLException {
        LoginResult r = tryLogin(email, password);
        return r.isSuccess() ? r.user : null;
    }

    public boolean register(User user, String roleName) throws SQLException {
        roleName = roleName == null ? "" : roleName.trim().toUpperCase();

        user.setEmail(user.getEmail().trim().toLowerCase(Locale.ROOT));

        if (userDao.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists.");
        }
        int roleId = userDao.getRoleIdByName(roleName);
        if (roleId <= 0) {
            throw new IllegalArgumentException("Invalid role.");
        }
        user.setRoleId(roleId);

        if ("ADMIN".equals(roleName)) {
            user.setAccountStatus("APPROVED");
        } else {
            user.setAccountStatus("PENDING");
        }

        user.setPasswordHash(PasswordUtil.hashPassword(user.getPasswordHash()));

        return userDao.createUser(user) > 0;
    }

    /**
     * Updates profile fields for an existing user. Email and role are not changed here.
     *
     * @return refreshed User from DB, or null if update failed
     * @throws IllegalArgumentException if required fields are blank
     */
    public User updateProfile(int userId, String fullName, String phone, String organization,
                              String address, String city, String state, String zipCode)
            throws SQLException {
        if (!ValidationUtil.isNotEmpty(fullName)) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (!ValidationUtil.isNotEmpty(phone)) {
            throw new IllegalArgumentException("Phone number is required.");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number.");
        }

        User existing = userDao.findById(userId);
        if (existing == null) {
            return null;
        }

        existing.setFullName(fullName);
        existing.setPhone(phone);
        existing.setOrganization(organization);
        existing.setAddress(address);
        existing.setCity(city);
        existing.setState(state);
        existing.setZipCode(zipCode);

        if (!userDao.updateProfile(existing)) {
            return null;
        }
        return userDao.findById(userId);
    }
}
