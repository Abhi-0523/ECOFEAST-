package com.ecofeast.controllers;

import com.ecofeast.model.FoodDonation;
import com.ecofeast.model.FoodCategory;
import com.ecofeast.model.DonationRequest;
import com.ecofeast.model.User;
import com.ecofeast.service.DonationService;
import com.ecofeast.util.SessionUtil;
import com.ecofeast.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * DonorController - Servlet handling all food donor operations.
 *
 * URL Pattern: /donor
 * Actions via ?action= parameter:
 *   GET:  dashboard, addDonation, manageDonations, donationHistory, requests, editDonation
 *   POST: submitDonation, updateDonation, deleteDonation, approveRequest, rejectRequest
 *
 * MVC Role: Controller — routes between Service (Model) and JSP views.
 */
public class DonorController extends HttpServlet {

    private final DonationService donationService = new DonationService();

    /* -------------------------------------------------------
     * HTTP GET — Page Navigation
     * ----------------------------------------------------- */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verify logged-in donor
        User user = SessionUtil.getLoggedInUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "dashboard";

        try {
            switch (action) {

                case "dashboard":
                    // Load donor's donations for summary cards
                    List<FoodDonation> myDonations = donationService.getDonorDonations(user.getId());
                    request.setAttribute("donations", myDonations);
                    // Count stats for dashboard cards
                    long available = myDonations.stream().filter(d -> "AVAILABLE".equals(d.getStatus())).count();
                    long distributed = myDonations.stream().filter(d -> "DISTRIBUTED".equals(d.getStatus())).count();
                    request.setAttribute("totalDonations", myDonations.size());
                    request.setAttribute("availableDonations", available);
                    request.setAttribute("distributedDonations", distributed);
                    forward(request, response, "/WEB-INF/views/donor/donor-dashboard.jsp");
                    break;

                case "addDonation":
                    // Load categories for the dropdown
                    List<FoodCategory> categories = donationService.getAllCategories();
                    request.setAttribute("categories", categories);
                    forward(request, response, "/WEB-INF/views/donor/add-donation.jsp");
                    break;

                case "manageDonations":
                    // Show paginated donation list
                    request.setAttribute("donations", donationService.getDonorDonations(user.getId()));
                    forward(request, response, "/WEB-INF/views/donor/manage-donations.jsp");
                    break;

                case "donationHistory":
                    // Completed / distributed donations
                    request.setAttribute("donations", donationService.getDonorDonations(user.getId()));
                    forward(request, response, "/WEB-INF/views/donor/donation-history.jsp");
                    break;

                case "requests":
                    // Incoming food requests from NGOs
                    request.setAttribute("requests", donationService.getDonorRequests(user.getId()));
                    forward(request, response, "/WEB-INF/views/donor/manage-donations.jsp");
                    break;

                case "editDonation":
                    // Load donation for editing
                    int editId = parseIntOrZero(request.getParameter("id"));
                    FoodDonation editDonation = donationService.getDonationById(editId);
                    if (editDonation == null || editDonation.getDonorId() != user.getId()) {
                        request.setAttribute("error", "Donation not found or access denied.");
                        forward(request, response, "/WEB-INF/views/donor/manage-donations.jsp");
                    } else {
                        request.setAttribute("donation", editDonation);
                        request.setAttribute("categories", donationService.getAllCategories());
                        forward(request, response, "/WEB-INF/views/donor/add-donation.jsp");
                    }
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/donor?action=dashboard");
            }
        } catch (Exception e) {
            handleError(request, response, e, "/WEB-INF/views/donor/donor-dashboard.jsp");
        }
    }

    /* -------------------------------------------------------
     * HTTP POST — Form Submissions
     * ----------------------------------------------------- */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = SessionUtil.getLoggedInUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "") {

                case "submitDonation":
                    submitDonation(request, response, user);
                    break;

                case "updateDonation":
                    updateDonation(request, response, user);
                    break;

                case "deleteDonation":
                    int delId = parseIntOrZero(request.getParameter("donationId"));
                    boolean deleted = donationService.deleteDonation(delId, user.getId());
                    setMessage(request, deleted, "Donation removed successfully.", "Failed to remove donation.");
                    response.sendRedirect(request.getContextPath() + "/donor?action=manageDonations");
                    break;

                case "approveRequest":
                    int approveId = parseIntOrZero(request.getParameter("requestId"));
                    boolean approved = donationService.approveRequest(approveId, user.getId());
                    setMessage(request, approved, "Request approved! Volunteer task created.", "Failed to approve request.");
                    response.sendRedirect(request.getContextPath() + "/donor?action=requests");
                    break;

                case "rejectRequest":
                    int rejectId  = parseIntOrZero(request.getParameter("requestId"));
                    String reason = request.getParameter("reason");
                    boolean rejected = donationService.rejectRequest(rejectId, user.getId(), reason);
                    setMessage(request, rejected, "Request rejected.", "Failed to reject request.");
                    response.sendRedirect(request.getContextPath() + "/donor?action=requests");
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/donor?action=dashboard");
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            try {
                request.setAttribute("categories", donationService.getAllCategories());
            } catch (java.sql.SQLException sqle) {
                request.setAttribute("error", "Database error loading categories.");
            }
            forward(request, response, "/WEB-INF/views/donor/add-donation.jsp");
        } catch (Exception e) {
            handleError(request, response, e, "/WEB-INF/views/donor/donor-dashboard.jsp");
        }
    }

    /* -------------------------------------------------------
     * PRIVATE HELPERS
     * ----------------------------------------------------- */

    /**
     * Processes the add-donation form submission.
     * Validates all required fields before calling service.
     */
    private void submitDonation(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        String foodName     = request.getParameter("foodName");
        String description  = request.getParameter("description");
        String categoryStr  = request.getParameter("categoryId");
        String quantityStr  = request.getParameter("quantity");
        String unit         = request.getParameter("quantityUnit");
        String expiryStr    = request.getParameter("expiryTime");
        String location     = request.getParameter("pickupLocation");
        String city         = request.getParameter("pickupCity");
        String imageUrl     = request.getParameter("imageUrl");

        // Validation
        if (!ValidationUtil.isNotEmpty(foodName)) {
            request.setAttribute("error", "Food name is required.");
            request.setAttribute("categories", donationService.getAllCategories());
            forward(request, response, "/WEB-INF/views/donor/add-donation.jsp");
            return;
        }

        int categoryId = parseIntOrZero(categoryStr);
        int quantity   = parseIntOrZero(quantityStr);

        LocalDateTime expiryTime;
        try {
            expiryTime = LocalDateTime.parse(expiryStr);
        } catch (DateTimeParseException e) {
            request.setAttribute("error", "Invalid expiry date format.");
            request.setAttribute("categories", donationService.getAllCategories());
            forward(request, response, "/WEB-INF/views/donor/add-donation.jsp");
            return;
        }

        FoodDonation donation = new FoodDonation(
            user.getId(), categoryId, foodName, description,
            quantity, unit, expiryTime, location, city
        );
        donation.setImageUrl(imageUrl);

        int id = donationService.addDonation(donation);
        if (id > 0) {
            request.getSession().setAttribute("successMsg", "Donation listed successfully!");
            response.sendRedirect(request.getContextPath() + "/donor?action=manageDonations");
        } else {
            request.setAttribute("error", "Failed to save donation. Please try again.");
            request.setAttribute("categories", donationService.getAllCategories());
            forward(request, response, "/WEB-INF/views/donor/add-donation.jsp");
        }
    }

    /**
     * Processes the edit-donation form submission.
     */
    private void updateDonation(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        int donationId = parseIntOrZero(request.getParameter("donationId"));
        FoodDonation donation = donationService.getDonationById(donationId);
        if (donation == null || donation.getDonorId() != user.getId()) {
            request.setAttribute("error", "Access denied or donation not found.");
            forward(request, response, "/WEB-INF/views/donor/manage-donations.jsp");
            return;
        }

        donation.setCategoryId(parseIntOrZero(request.getParameter("categoryId")));
        donation.setFoodName(request.getParameter("foodName"));
        donation.setDescription(request.getParameter("description"));
        donation.setQuantity(parseIntOrZero(request.getParameter("quantity")));
        donation.setQuantityUnit(request.getParameter("quantityUnit"));
        donation.setPickupLocation(request.getParameter("pickupLocation"));
        donation.setPickupCity(request.getParameter("pickupCity"));
        donation.setImageUrl(request.getParameter("imageUrl"));

        try {
            String expiryStr = request.getParameter("expiryTime");
            donation.setExpiryTime(LocalDateTime.parse(expiryStr));
        } catch (DateTimeParseException e) {
            request.setAttribute("error", "Invalid expiry date format.");
            forward(request, response, "/WEB-INF/views/donor/add-donation.jsp");
            return;
        }

        boolean updated = donationService.updateDonation(donation);
        setMessage(request, updated, "Donation updated successfully.", "Update failed.");
        response.sendRedirect(request.getContextPath() + "/donor?action=manageDonations");
    }

    /** Forwards to a JSP view path. */
    private void forward(HttpServletRequest req, HttpServletResponse res, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, res);
    }

    /** Sets success or error attribute based on result boolean. */
    private void setMessage(HttpServletRequest req, boolean success, String successMsg, String errorMsg) {
        if (success) req.setAttribute("success", successMsg);
        else         req.setAttribute("error", errorMsg);
    }

    /** Logs and forwards to an error page. */
    private void handleError(HttpServletRequest req, HttpServletResponse res, Exception e, String fallback)
            throws ServletException, IOException {
        System.err.println("[DonorController] Error: " + e.getMessage());
        req.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
        req.getRequestDispatcher(fallback).forward(req, res);
    }

    /** Safely parses an int param, returning 0 on failure. */
    private int parseIntOrZero(String value) {
        try { return Integer.parseInt(value); } catch (Exception e) { return 0; }
    }
}
