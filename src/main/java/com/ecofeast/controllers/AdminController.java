package com.ecofeast.controllers;

import com.ecofeast.model.User;
import com.ecofeast.service.DonationService;
import com.ecofeast.service.VolunteerService;
import com.ecofeast.dao.UserDao;
import com.ecofeast.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * AdminController - Servlet handling all admin operations.
 *
 * URL Pattern: /admin
 * Actions: dashboard, manageUsers, approveUser, rejectUser, manageDonations, manageTasks
 */
public class AdminController extends HttpServlet {

    private final DonationService donationService = new DonationService();
    private final VolunteerService volunteerService = new VolunteerService();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = SessionUtil.getLoggedInUser(request);
        if (user == null || !SessionUtil.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "dashboard";

        try {
            switch (action) {
                case "dashboard":
                    request.setAttribute("stats", donationService.getDashboardStats());
                    request.setAttribute("taskStats", volunteerService.getTaskStats());
                    forward(request, response, "/WEB-INF/views/admin/admin-dashboard.jsp");
                    break;
                case "manageUsers":
                    List<User> pendingUsers = userDao.getUsersByStatus("PENDING");
                    request.setAttribute("pendingUsers", pendingUsers);
                    forward(request, response, "/WEB-INF/views/admin/manage-users.jsp");
                    break;
                case "manageDonations":
                    request.setAttribute("donations", donationService.getAllDonations());
                    forward(request, response, "/WEB-INF/views/admin/manage-donations.jsp");
                    break;
                case "manageTasks":
                    request.setAttribute("tasks", volunteerService.getAllTasks());
                    forward(request, response, "/WEB-INF/views/admin/manage-tasks.jsp");
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
            }
        } catch (Exception e) {
            handleError(request, response, e, "/WEB-INF/views/admin/admin-dashboard.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = SessionUtil.getLoggedInUser(request);
        if (user == null || !SessionUtil.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "") {
                case "approveUser":
                    int userIdApprove = parseIntOrZero(request.getParameter("userId"));
                    if (userDao.updateStatus(userIdApprove, "APPROVED")) {
                        SessionUtil.setSuccessMessage(request, "User approved successfully.");
                    } else {
                        SessionUtil.setErrorMessage(request, "Failed to approve user.");
                    }
                    response.sendRedirect(request.getContextPath() + "/admin?action=manageUsers");
                    break;

                case "rejectUser":
                    int userIdReject = parseIntOrZero(request.getParameter("userId"));
                    if (userDao.updateStatus(userIdReject, "REJECTED")) {
                        SessionUtil.setSuccessMessage(request, "User rejected.");
                    } else {
                        SessionUtil.setErrorMessage(request, "Failed to reject user.");
                    }
                    response.sendRedirect(request.getContextPath() + "/admin?action=manageUsers");
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
            }
        } catch (Exception e) {
            handleError(request, response, e, "/WEB-INF/views/admin/admin-dashboard.jsp");
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse res, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, res);
    }

    private void handleError(HttpServletRequest req, HttpServletResponse res, Exception e, String fallback)
            throws ServletException, IOException {
        System.err.println("[AdminController] Error: " + e.getMessage());
        req.setAttribute("error", "An error occurred: " + e.getMessage());
        req.getRequestDispatcher(fallback).forward(req, res);
    }

    private int parseIntOrZero(String value) {
        try { return Integer.parseInt(value); } catch (Exception e) { return 0; }
    }
}
