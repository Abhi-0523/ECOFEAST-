package com.ecofeast.controllers;
import com.ecofeast.model.DonationRequest;
import com.ecofeast.model.FoodDonation;
import com.ecofeast.model.User;
import com.ecofeast.service.DonationService;
import com.ecofeast.util.SessionUtil;
import com.ecofeast.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * NgoController - Servlet handling all NGO operations.
 *
 * URL Pattern: /ngo
 * Actions via ?action= parameter:
 *   GET:  dashboard, browse, requestFood, requests, requestHistory
 *   POST: submitRequest, cancelRequest
 *
 * MVC Role: Controller — orchestrates NGO features between Service and JSP.
 */
public class NgoController extends HttpServlet {

    private final DonationService donationService = new DonationService();

    /* -------------------------------------------------------
     * HTTP GET
     * ----------------------------------------------------- */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
                    // Load summary counts for NGO dashboard cards
                    List<DonationRequest> allMyRequests = donationService.getNgoRequests(user.getId());
                    long pendingCount   = allMyRequests.stream().filter(r -> "PENDING".equals(r.getStatus())).count();
                    long approvedCount  = allMyRequests.stream().filter(r -> "APPROVED".equals(r.getStatus())).count();
                    long collectedCount = allMyRequests.stream().filter(r -> "COLLECTED".equals(r.getStatus())).count();
                    request.setAttribute("totalRequests",    allMyRequests.size());
                    request.setAttribute("pendingRequests",  pendingCount);
                    request.setAttribute("approvedRequests", approvedCount);
                    request.setAttribute("collectedCount",   collectedCount);
                    // Show recent 5 requests in dashboard
                    request.setAttribute("recentRequests",
                        allMyRequests.subList(0, Math.min(5, allMyRequests.size())));
                    forward(request, response, "/WEB-INF/views/ngo/ngo-dashboard.jsp");
                    break;

                case "browse":
                    // Search/filter available donations
                    String keyword    = request.getParameter("keyword");
                    String categoryId = request.getParameter("categoryId");
                    String city       = request.getParameter("city");

                    List<FoodDonation> foods;
                    int catId = parseIntOrZero(categoryId);

                    if (keyword != null || catId > 0 || city != null) {
                        foods = donationService.searchDonations(keyword, catId, city);
                    } else {
                        foods = donationService.getAvailableDonations();
                    }

                    request.setAttribute("donations",   foods);
                    request.setAttribute("categories",  donationService.getAllCategories());
                    request.setAttribute("keyword",     keyword);
                    request.setAttribute("selectedCat", catId);
                    request.setAttribute("city",        city);
                    forward(request, response, "/WEB-INF/views/ngo/browse-food.jsp");
                    break;

                case "requestFood":
                    // Show request form for a specific donation
                    int donId = parseIntOrZero(request.getParameter("donationId"));
                    FoodDonation donation = donationService.getDonationById(donId);
                    if (donation == null || !donation.isAvailable()) {
                        request.setAttribute("error", "Donation not available.");
                        response.sendRedirect(request.getContextPath() + "/ngo?action=browse");
                        return;
                    }
                    request.setAttribute("donation", donation);
                    forward(request, response, "/WEB-INF/views/ngo/request-food.jsp");
                    break;

                case "requests":
                case "requestHistory":
                    // All requests by this NGO
                    request.setAttribute("requests", donationService.getNgoRequests(user.getId()));
                    forward(request, response, "/WEB-INF/views/ngo/request-history.jsp");
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/ngo?action=dashboard");
            }
        } catch (Exception e) {
            handleError(request, response, e, "/WEB-INF/views/ngo/ngo-dashboard.jsp");
        }
    }

    /* -------------------------------------------------------
     * HTTP POST
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

                case "submitRequest":
                    submitRequest(request, response, user);
                    break;

                case "cancelRequest":
                    int reqId = parseIntOrZero(request.getParameter("requestId"));
                    boolean cancelled = donationService.cancelRequest(reqId, user.getId());
                    setMessage(request, cancelled, "Request cancelled.", "Failed to cancel request.");
                    response.sendRedirect(request.getContextPath() + "/ngo?action=requests");
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/ngo?action=dashboard");
            }
        } catch (Exception e) {
            handleError(request, response, e, "/WEB-INF/views/ngo/ngo-dashboard.jsp");
        }
    }

    /* -------------------------------------------------------
     * PRIVATE HELPERS
     * ----------------------------------------------------- */

    /**
     * Handles NGO food request submission.
     * Validates quantity and checks for duplicate requests.
     */
    private void submitRequest(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        int donationId = parseIntOrZero(request.getParameter("donationId"));
        int quantity   = parseIntOrZero(request.getParameter("quantity"));
        String message = request.getParameter("message");

        if (donationId <= 0 || quantity <= 0) {
            request.setAttribute("error", "Invalid donation or quantity.");
            FoodDonation donation = donationService.getDonationById(donationId);
            request.setAttribute("donation", donation);
            forward(request, response, "/WEB-INF/views/ngo/request-food.jsp");
            return;
        }

        DonationRequest req = new DonationRequest(donationId, user.getId(), quantity, message);

        try {
            int id = donationService.submitRequest(req);
            if (id > 0) {
                request.getSession().setAttribute("successMsg", "Food request submitted successfully!");
                response.sendRedirect(request.getContextPath() + "/ngo?action=requests");
            } else {
                request.setAttribute("error", "Failed to submit request.");
                forward(request, response, "/WEB-INF/views/ngo/request-food.jsp");
            }
        } catch (IllegalStateException ex) {
            // Duplicate request
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("donation", donationService.getDonationById(donationId));
            forward(request, response, "/WEB-INF/views/ngo/request-food.jsp");
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse res, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, res);
    }

    private void setMessage(HttpServletRequest req, boolean success, String ok, String fail) {
        req.setAttribute(success ? "success" : "error", success ? ok : fail);
    }

    private void handleError(HttpServletRequest req, HttpServletResponse res, Exception e, String fallback)
            throws ServletException, IOException {
        System.err.println("[NgoController] Error: " + e.getMessage());
        req.setAttribute("error", "An error occurred: " + e.getMessage());
        req.getRequestDispatcher(fallback).forward(req, res);
    }

    private int parseIntOrZero(String val) {
        try { return Integer.parseInt(val); } catch (Exception e) { return 0; }
    }
}
