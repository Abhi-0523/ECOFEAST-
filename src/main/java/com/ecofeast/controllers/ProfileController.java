package com.ecofeast.controllers;

import com.ecofeast.model.User;
import com.ecofeast.service.UserService;
import com.ecofeast.util.SessionUtil;
import com.ecofeast.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * ProfileController - View and update the logged-in user's profile.
 *
 * URL: /profile
 * GET  - show profile form
 * POST - save profile (validates required fields)
 */
public class ProfileController extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionUtil.getLoggedInUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String success = SessionUtil.consumeSuccessMessage(request);
        if (success != null) {
            request.setAttribute("success", success);
        }
        forwardProfile(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionUtil.getLoggedInUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String organization = request.getParameter("organization");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zipCode = request.getParameter("zipCode");

        if (!ValidationUtil.isNotEmpty(fullName)) {
            request.setAttribute("error", "Name is required.");
            bindFormFields(request, fullName, phone, organization, address, city, state, zipCode);
            forwardProfile(request, response);
            return;
        }

        if (!ValidationUtil.isNotEmpty(phone)) {
            request.setAttribute("error", "Phone number is required.");
            bindFormFields(request, fullName, phone, organization, address, city, state, zipCode);
            forwardProfile(request, response);
            return;
        }

        if (!ValidationUtil.isValidPhone(phone)) {
            request.setAttribute("error", "Please enter a valid phone number (10–15 digits).");
            bindFormFields(request, fullName, phone, organization, address, city, state, zipCode);
            forwardProfile(request, response);
            return;
        }

        try {
            User updated = userService.updateProfile(
                    user.getId(),
                    ValidationUtil.sanitizeAndTrim(fullName, 100),
                    phone.trim(),
                    ValidationUtil.sanitizeAndTrim(organization, 150),
                    ValidationUtil.sanitizeAndTrim(address, 255),
                    ValidationUtil.sanitizeAndTrim(city, 80),
                    ValidationUtil.sanitizeAndTrim(state, 80),
                    ValidationUtil.sanitizeAndTrim(zipCode, 20)
            );

            if (updated == null) {
                request.setAttribute("error", "Failed to update profile. Please try again.");
                bindFormFields(request, fullName, phone, organization, address, city, state, zipCode);
                forwardProfile(request, response);
                return;
            }

            SessionUtil.setLoggedInUser(request, updated);
            SessionUtil.setSuccessMessage(request, "Profile updated successfully.");
            response.sendRedirect(request.getContextPath() + "/profile");
        } catch (SQLException e) {
            request.setAttribute("error", "A database error occurred. Please try again.");
            bindFormFields(request, fullName, phone, organization, address, city, state, zipCode);
            forwardProfile(request, response);
        }
    }

    private void bindFormFields(HttpServletRequest request, String fullName, String phone,
                                String organization, String address, String city,
                                String state, String zipCode) {
        request.setAttribute("fullName", fullName);
        request.setAttribute("phone", phone);
        request.setAttribute("organization", organization);
        request.setAttribute("address", address);
        request.setAttribute("city", city);
        request.setAttribute("state", state);
        request.setAttribute("zipCode", zipCode);
    }

    private void forwardProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }
}
