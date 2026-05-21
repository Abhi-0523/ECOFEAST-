package com.ecofeast.controllers;

import com.ecofeast.config.AppConfig;
import com.ecofeast.model.User;
import com.ecofeast.service.UserService;
import com.ecofeast.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    private final UserService userService;

    public RegisterServlet() {
        this.userService = new UserService();
    }

    public RegisterServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = req.getParameter("role");
        String normalizedRole = role == null ? "" : role.trim().toUpperCase();
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String org = req.getParameter("organization");
        String city = req.getParameter("city");

        if (!ValidationUtil.isNotEmpty(fullName) || !ValidationUtil.isNotEmpty(email)
                || !ValidationUtil.isNotEmpty(phone) || !ValidationUtil.isNotEmpty(password)
                || !ValidationUtil.isNotEmpty(normalizedRole)) {
             req.setAttribute("error", "Please fill in all required fields.");
             req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
             return;
        }

        if (!isAllowedRegistrationRole(normalizedRole)) {
            req.setAttribute("error", "Please select a valid registration role.");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("error", "Please enter a valid email address.");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        if (!ValidationUtil.isValidPhone(phone)) {
            req.setAttribute("error", "Please enter a valid phone number.");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPasswordHash(password);
        user.setOrganization(org);
        user.setCity(city);

        try {
            boolean success = userService.register(user, normalizedRole);
            if (success) {
                if ("PENDING".equals(user.getAccountStatus())) {
                    req.setAttribute("success", "Registration successful. Sign in with your email and password — your account is pending until an administrator approves it.");
                } else {
                    req.setAttribute("success", "Registration successful. You can now login.");
                }
                req.setAttribute("demoAdminEmail", AppConfig.DEMO_ADMIN_EMAIL);
                req.setAttribute("demoAdminPassword", AppConfig.DEMO_ADMIN_PASSWORD);
                req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "Registration failed.");
                req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace(); // Log to console for debugging
            req.setAttribute("error", "System Error: " + e.getMessage());
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
        }
    }

    private boolean isAllowedRegistrationRole(String role) {
        return "DONOR".equals(role) || "NGO".equals(role) || "VOLUNTEER".equals(role);
    }
}
